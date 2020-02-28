package cn.volcano.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import cn.volcano.http.HttpRequest;
import cn.volcano.http.HttpResponse;

/**
 * 这个类用来完成WebServer的抽取过程 线程类
 * 
 * @author VOLCANO
 *
 */
public class ClientHandler implements Runnable {

	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			/* 动态获取网页文件 */
			HttpRequest request = new HttpRequest(socket.getInputStream());

			// 解析请求行第二块数据
			File file = new File("WebContent" + request.getUri());

			// 响应头拼接
			HttpResponse response = new HttpResponse(socket.getOutputStream());
			response.setProtocol("HTTP/1.1");
			response.setStatus(200);
			response.setContentType("text/html");
			response.setContentLength((int) file.length());
			
			ResponseFile(response, file);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 封装网页响应的方法
	public void ResponseFile(HttpResponse response, File file) {
		try {
			// 响应实体（网页文件）
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] b = new byte[(int) file.length()];
			bis.read(b); // 文件读到数组里

			response.getOutputStream().write(b); // 把文件写出去
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
