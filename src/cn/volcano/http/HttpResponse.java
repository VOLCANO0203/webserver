package cn.volcano.http;

import java.io.OutputStream;
import java.io.PrintStream;

import cn.volcano.context.ServerContext;

/**
 * 用来封装响应信息 
 * HTTP/1.1 200 OK 
 * Content-Type:text/html 
 * Content-Length:129
 * 
 * @author VOLCANO
 *
 */
public class HttpResponse {
	// 遵循的协议名和版本号
	private String protocol;

	// 状态码 200 404 500 304
	private int status;

	// 响应头Content-Type
	private String contentType;

	// 响应头Content-Length
	private int contentLength;

	private OutputStream out;

	public HttpResponse(OutputStream out) {
		this.out = out;
	}

	private boolean isSend = false;

	public OutputStream getOutputStream() {
		if (!isSend) {
			PrintStream ps = new PrintStream(this.out);
			// 状态行
			ps.println(protocol + " " + status + " "+ServerContext.statusMap.get(status));

			// 响应头
			ps.println("Content-Type:" + contentType);
			ps.println("Content-Length:" + contentLength);

			// 空白行
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
