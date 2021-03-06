/* Avuna HTTPD - General Server Applications Copyright (C) 2015 Maxwell Bruce This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. */

package org.avuna.httpd.util.unio;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import org.avuna.httpd.util.CException;
import org.avuna.httpd.util.CLib;

public class UNIOInputStream extends InputStream {
	private UNIOSocket socket = null;
	
	public UNIOInputStream(UNIOSocket socket) {
		this.socket = socket;
	}
	
	public int available() {
		int status = CLib.available(socket.sockfd);
		if (status < 0) return 0;
		return status;
	}
	
	@Override
	public int read() throws IOException {
		byte[] sa = new byte[1];
		if (this.read(sa) == 0) return -1; // not EOF, but may be no data since non blocking
		return sa[0];
	}
	
	public int read(byte[] array) throws IOException {
		return this.read(array, 0, array.length);
	}
	
	public int read(byte[] array, int off, int len) throws IOException {
		if (off + len > array.length) throw new ArrayIndexOutOfBoundsException("off + len MUST NOT be >= array.length");
		if (socket.isClosed()) throw new SocketException("Socket Closed!");
		byte[] buf = socket.session == 0 ? CLib.read(socket.sockfd, len) : GNUTLS.read(socket.session, len);
		if (buf == null) { // not 100% accurate, but what else?
			int i = CLib.errno();
			if (i == 11 | i == -28) { // EAGAIN/GNUTLS_EAGAIN
				return 0;
			}else if (i == 104 || i == -50) {
				throw new SocketException("Connection reset by peer");
			}else if (i == 32) {
				throw new SocketException("Broken Pipe");
			}else if (i == 9) {
				throw new SocketException("Bad Connection! Possible cross client network corruption.");
			}else throw new CException(i, "read failed");
		}
		System.arraycopy(buf, 0, array, off, buf.length);
		socket.lr = System.currentTimeMillis();
		return buf.length;
	}
	
}
