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
 * ������������WebServer�ĳ�ȡ����
 * 		���߳���
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
			File file = new File(ServerContext.webRoot + request.getUri());
			
			HttpResponse response = new HttpResponse(socket.getOutputStream());
			
			//�ļ������ڣ���ת��404ҳ��
			if(!file.exists()) {
				file = new File(ServerContext.webRoot + "/" + ServerContext.notFoundPage);
				response.setStatus(404);
			}else {
				response.setStatus(200);
			}

			// ��Ӧͷƴ��
			response.setProtocol(ServerContext.protocol);
			response.setContentType(getContentType(file));
			response.setContentLength((int) file.length());
			
			ResponseFile(response, file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//���ݷ�����Դ�ĺ�׺������map�в��Ҷ�Ӧ��value
	private String getContentType(File file) {
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);
		
		String value = ServerContext.map.get(ext);
		
		return value;
	}

	// ��װ��ҳ��Ӧ�ķ���
	public void ResponseFile(HttpResponse response, File file) {
		try {
			// ��Ӧʵ�壨��ҳ�ļ���
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] b = new byte[(int) file.length()];
			bis.read(b); // �ļ���������

			response.getOutputStream().write(b); // ���ļ�д��ȥ
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
