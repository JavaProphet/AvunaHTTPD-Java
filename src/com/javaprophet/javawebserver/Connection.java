package com.javaprophet.javawebserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
	public final Socket s;
	public final DataInputStream in;
	public final DataOutputStream out;
	public static final ResponseGenerator rg = new ResponseGenerator();
	
	public Connection(Socket s, DataInputStream in, DataOutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
	}
	
	public void run() {
		while (!s.isClosed()) {
			try {
				RequestPacket incomingRequest = RequestPacket.read(in);
				if (incomingRequest == null) {
					s.close();
					return;
				}
				System.out.println(incomingRequest.toString());
				ResponsePacket outgoingResponse = new ResponsePacket();
				rg.process(incomingRequest, outgoingResponse); // TODO: pipelining queue
				// String[] ces = incomingRequest.headers.getHeader("Accept-Encoding").value.split(", ");
				// ContentEncoding[] ces2 = new ContentEncoding[ces.length];
				// for (int i = 0; i < ces.length; i++) {
				// ces2[i] = ContentEncoding.get(ces[i]);
				// }
				ContentEncoding use = ContentEncoding.identity;// TODO: fix gzip
				// for (ContentEncoding ce : ces2) {
				// if (ce == ContentEncoding.gzip) {
				// use = ce;
				// break;
				// }else if (ce == ContentEncoding.xgzip) {
				// use = ce;
				// break;
				// }
				// }
				System.out.println(outgoingResponse.toString(use));
				outgoingResponse.write(out, use);
			}catch (Exception ex) {
				ex.printStackTrace();
				try {
					s.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		JavaWebServer.runningThreads.remove(this);
	}
}
