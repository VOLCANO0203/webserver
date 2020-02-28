package cn.volcano.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 这个类用来完成WebServer的抽取过程
 * 		线程类
 * @author VOLCANO
 *
 */
public class ClientHandler implements Runnable{

	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			PrintStream printStream = new PrintStream(socket.getOutputStream());
			
			//拼接正确格式的响应代码
			//状态行
			printStream.println("HTTP/1.1 200 OK");
			
			//若干响应头
			printStream.println("Content-Type:text/html");
			
//			String data = "hello server~~";
//			printStream.println("Content-Length:"+data.getBytes().length);
//			printStream.write(data.getBytes());
			
			//响应网页文件
			File file = new File("WebContent/hello.html");
			printStream.println("Content-Length:"+file.length());
			
			//空白行
			printStream.println();
			
			//响应实体（网页文件）
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] b = new byte[(int) file.length()];
			bis.read(b);	//文件读到数组里
			
			printStream.write(b);	//把文件写出去
			printStream.flush();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
