package cn.volcano.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ��������������������˵ĳ��� 
 * 	1.����ServerSocket����
 *	2.�ڹ��췽���г�ʼ��ServerSocket
 * 	3.����start���������տͻ���������Ӧ 4.main��������������
 * 
 * @author VOLCANO
 *
 */
public class WebServer {
	private ServerSocket server;
	
	//�����̳߳ض���
	private ExecutorService threadPool;

	public WebServer() {
		try {
			server = new ServerSocket(8080);
			//��ʼ���̳߳�
			threadPool = Executors.newFixedThreadPool(100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			while (true) {
				//���տͻ�������
				Socket socket = server.accept();
				
				//�����̳߳�ִ���߳���
				threadPool.execute(new ClientHandler(socket));

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer webServer = new WebServer();
		webServer.start();
	}
}