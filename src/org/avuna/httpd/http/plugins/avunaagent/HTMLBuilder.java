/* Avuna HTTPD - General Server Applications Copyright (C) 2015 Maxwell Bruce This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. */

package org.avuna.httpd.http.plugins.avunaagent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

public class HTMLBuilder extends PrintWriter {
	private StringWriter out;
	private boolean closed = false;
	private String fnl = null;
	
	public HTMLBuilder(StringWriter out) {
		super(out);
		this.out = out;
	}
	
	/** Clears the buffer, erasing any previously written data. */
	public void clear() {
		out = new StringWriter();
		super.out = out;
	}
	
	/** Prevents any future writing, but does not throw errors if you do, intended for cleaner code flow. */
	public void close() {
		closed = true;
		fnl = out.toString();
	}
	
	public String toString() {
		return closed ? fnl : out.toString();
	}
	
	// following are for ease of embedded page programming
	
	private HashMap<String, Object> vars = null;
	
	/** Set a value in the internal HashMap, useful for cleaner nested Avuna Agents. */
	public void set(String name, Object value) {
		if (vars == null) vars = new HashMap<String, Object>();
		vars.put(name, value);
	}
	
	/** Gets a value from the internal HashMap, useful for cleaner nested Avuna Agents. */
	
	public Object get(String name) {
		if (vars == null) return null;
		return vars.get(name);
	}
	
	/** See if a value in the internal HashMap exists, useful for cleaner nested Avuna Agents. */
	
	public boolean containsKey(String name) {
		if (vars == null) return false;
		return vars.containsKey(name);
	}
}
