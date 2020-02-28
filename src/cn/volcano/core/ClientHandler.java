package cn.volcano.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * ������������WebServer�ĳ�ȡ����
 * 		�߳���
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
			
			//ƴ����ȷ��ʽ����Ӧ����
			//״̬��
			printStream.println("HTTP/1.1 200 OK");
			
			//������Ӧͷ
			printStream.println("Content-Type:text/html");
			
//			String data = "hello server~~";
//			printStream.println("Content-Length:"+data.getBytes().length);
//			printStream.write(data.getBytes());
			
			//��Ӧ��ҳ�ļ�
			File file = new File("WebContent/hello.html");
			printStream.println("Content-Length:"+file.length());
			
			//�հ���
			printStream.println();
			
			//��Ӧʵ�壨��ҳ�ļ���
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] b = new byte[(int) file.length()];
			bis.read(b);	//�ļ�����������
			
			printStream.write(b);	//���ļ�д��ȥ
			printStream.flush();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
