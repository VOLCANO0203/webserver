package cn.volcano.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cn.volcano.context.ServerContext;
import cn.volcano.http.HttpRequest;
import cn.volcano.http.HttpResponse;
import cn.volcano.util.JDBCUtils;

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

			if(request.getUri() != null) {
				// ���������еڶ�������
				File file = new File(ServerContext.webRoot + request.getUri());
				
				HttpResponse response = new HttpResponse(socket.getOutputStream());
				
				//����ע������
				if(request.getUri().startsWith("/RegistUser")) {
					registUser(request, response);
					return;
				}
				//�����¼����
				if(request.getUri().startsWith("/LoginUser")) {
					loginUser(request, response);
					return;
				}
				
				//�ļ������ڣ���ת��404ҳ��
				if(!file.exists()) {
					file = new File(ServerContext.webRoot + "/" + ServerContext.notFoundPage);
					response.setStatus(404);
				}else {
					response.setStatus(200);
				}

				// ��Ӧͷƴ��
				response.setProtocol(ServerContext.protocol);
				response.setContentType(getContentTypeByEXT(file));
				response.setContentLength((int) file.length());
				
				ResponseFile(response, file);

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//�����¼����
	private void loginUser(HttpRequest request, HttpResponse response) {
		//1.��ȡ�û��ĵ�¼��Ϣ
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//2.ͨ���û����������ѯ���ݿ�
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//a.��ȡ���Ӷ���
			con = JDBCUtils.getConnection();
			//b.����sql��䣨�Ǽܣ�
			String sql = "select * from user where username=? and password=?";
			//c.��ȡ������
			ps = con.prepareStatement(sql);
			//d.����sql����
			ps.setString(1, username);
			ps.setString(2, password);
			//e.ִ��sql���
			rs = ps.executeQuery();
			//f.̎��Y��
			File file = null;
			if(rs.next()) {
				file = new File(ServerContext.webRoot+"/login_success.html");
			}else {
				file = new File(ServerContext.webRoot+"/login_error.html");
			}
			
			//3.������Ӧ����ʾ��
			response.setProtocol(ServerContext.protocol);
			response.setStatus(200);
			response.setContentType(getContentTypeByEXT(file));
			response.setContentLength((int) file.length());
			
			//�ļ���Ӧ�������
			ResponseFile(response, file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//f.�ͷ���Դ
			JDBCUtils.close(con, ps, rs);
		}	
	}

	//����ע������
	private void registUser(HttpRequest request, HttpResponse response) {
		//1.��ȡ�û���ע����Ϣ
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//System.out.println(username+": "+password);
		
		//2.����ȡ���û���Ϣ���浽���ݿ�
		Connection con = null;
		PreparedStatement ps = null;
		try {
			//a.��ȡ���Ӷ���
			con = JDBCUtils.getConnection();
			//b.����sql��䣨�Ǽܣ�
			String sql = "insert into user value(null,?,?)";
			//c.��ȡ������
			ps = con.prepareStatement(sql);
			//d.����sql����
			ps.setString(1, username);
			ps.setString(2, password);
			//e.ִ��sql���
			ps.executeUpdate();
			
			//3.������Ӧ����ʾע��ɹ���
			File file = new File(ServerContext.webRoot+"/regist_success.html");
			response.setProtocol(ServerContext.protocol);
			response.setStatus(200);
			response.setContentType(getContentTypeByEXT(file));
			response.setContentLength((int) file.length());
			
			//�ļ���Ӧ�������
			ResponseFile(response, file);

		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//f.�ͷ���Դ
			JDBCUtils.close(con, ps, null);
		}
		
	}

	//���ݷ�����Դ�ĺ�׺������map�в��Ҷ�Ӧ��value
	private String getContentTypeByEXT(File file) {
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
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				socket = null;
			}
		}
	}

}
