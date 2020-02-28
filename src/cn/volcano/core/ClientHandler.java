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
 * ������������WebServer�ĳ�ȡ���� �߳���
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
			/* ��̬��ȡ��ҳ�ļ� */
			HttpRequest request = new HttpRequest(socket.getInputStream());

			// ���������еڶ�������
			File file = new File("WebContent" + request.getUri());

			// ��Ӧͷƴ��
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

	// ��װ��ҳ��Ӧ�ķ���
	public void ResponseFile(HttpResponse response, File file) {
		try {
			// ��Ӧʵ�壨��ҳ�ļ���
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] b = new byte[(int) file.length()];
			bis.read(b); // �ļ�����������

			response.getOutputStream().write(b); // ���ļ�д��ȥ
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
