package cn.volcano.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.volcano.context.ServerContext;

/**
 * 这个类用来代表服务器端的程序 
 * 	1.声明ServerSocket对象
 *	2.在构造方法中初始化ServerSocket
 * 	3.创建start方法，接收客户端请求并响应 
 * 	4.main方法启动服务器
 * 
 * @author VOLCANO
 *
 */
public class WebServer {
	private ServerSocket server;
	
	//声明线程池对象
	private ExecutorService threadPool;

	public WebServer() {
		try {
			server = new ServerSocket(ServerContext.port);
			//初始化线程池
			threadPool = Executors.newFixedThreadPool(ServerContext.maxSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			while (true) {
				//接收客户端请求
				Socket socket = server.accept();
				
				//利用线程池执行线程类
				threadPool.execute(new ClientHandler(socket));

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer webServer = new WebServer();
		webServer.start();
	}
}
