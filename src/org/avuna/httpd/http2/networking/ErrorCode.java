package org.avuna.httpd.http2.networking;

public enum ErrorCode {
	NO_ERROR(0), PROTOCOL_ERROR(1), INTERNAL_ERROR(2), FLOW_CONTROL_ERROR(3), SETTINGS_TIMEOUT(4), STREAM_CLOSED(5), FRAME_SIZE_ERROR(6), REFUSED_STREAM(7), CANCEL(8), COMPRESSION_ERROR(10), ENHANCE_YOUR_CALM(11), INADEQUATE_SECURITY(12), HTTP_1_1_REQUIRED(13);
	public final int id;
	
	private ErrorCode(int id) {
		this.id = id;
	}
}
