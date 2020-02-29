package cn.volcano.http;

import java.io.OutputStream;
import java.io.PrintStream;

import cn.volcano.context.ServerContext;

/**
 * ������װ��Ӧ��Ϣ 
 * HTTP/1.1 200 OK 
 * Content-Type:text/html 
 * Content-Length:129
 * 
 * @author VOLCANO
 *
 */
public class HttpResponse {
	// ��ѭ��Э�����Ͱ汾��
	private String protocol;

	// ״̬�� 200 404 500 304
	private int status;

	// ��ӦͷContent-Type
	private String contentType;

	// ��ӦͷContent-Length
	private int contentLength;

	private OutputStream out;

	public HttpResponse(OutputStream out) {
		this.out = out;
	}

	private boolean isSend = false;

	public OutputStream getOutputStream() {
		if (!isSend) {
			PrintStream ps = new PrintStream(this.out);
			// ״̬��
			ps.println(protocol + " " + status + " "+ServerContext.statusMap.get(status));

			// ��Ӧͷ
			ps.println("Content-Type:" + contentType);
			ps.println("Content-Length:" + contentLength);

			// �հ���
			ps.println();
			
			isSend = true;
		}
		return out;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
}
