package com.javaprophet.javawebserver.plugins.base.fcgi.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NameValue44 extends NameValue11 {
	
	public NameValue44(String name, String value, int id) {
		super(name, value, id);
	}
	
	public NameValue44(DataInputStream in, int l) throws IOException {
		readContent(in, l);
	}
	
	@Override
	protected void readContent(DataInputStream in, int l) throws IOException {
		int nl = -in.readInt();
		int vl = -in.readInt();
		name = readUTF(in, nl);
		value = readUTF(in, vl);
	}
	
	@Override
	protected void writeContent(DataOutputStream out) throws IOException {
		out.writeInt(-name.length());
		out.writeInt(-value.length());
		out.write(name.getBytes());
		out.write(value.getBytes());
	}
	
}