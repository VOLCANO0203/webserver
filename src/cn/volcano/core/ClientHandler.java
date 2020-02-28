package cn.volcano.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import cn.volcano.context.ServerContext;
import cn.volcano.http.HttpRequest;
import cn.volcano.http.HttpResponse;

/**
 * 这个类用来完成WebServer的抽取过程
 * 		是线程类
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
			File file = new File(ServerContext.webRoot + request.getUri());
			
			HttpResponse response = new HttpResponse(socket.getOutputStream());
			
			//文件不存在，跳转至404页面
			if(!file.exists()) {
				file = new File(ServerContext.webRoot + "/" + ServerContext.notFoundPage);
				response.setStatus(404);
			}else {
				response.setStatus(200);
			}

			// 响应头拼接
			response.setProtocol(ServerContext.protocol);
			response.setContentType(getContentType(file));
			response.setContentLength((int) file.length());
			
			ResponseFile(response, file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//根据访问资源的后缀名，在map中查找对应的value
	private String getContentType(File file) {
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);
		
		String value = ServerContext.map.get(ext);
		
		return value;
	}

	// 封装网页响应的方法
	public void ResponseFile(HttpResponse response, File file) {
		try {
			// 响应实体（网页文件）
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] b = new byte[(int) file.length()];
			bis.read(b); // 文件读到数组

			response.getOutputStream().write(b); // 把文件写出去
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
