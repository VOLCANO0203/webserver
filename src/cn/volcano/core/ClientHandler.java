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

			if(request.getUri() != null) {
				// 解析请求行第二块数据
				File file = new File(ServerContext.webRoot + request.getUri());
				
				HttpResponse response = new HttpResponse(socket.getOutputStream());
				
				//处理注册请求
				if(request.getUri().startsWith("/RegistUser")) {
					registUser(request, response);
					return;
				}
				//处理登录请求
				if(request.getUri().startsWith("/LoginUser")) {
					loginUser(request, response);
					return;
				}
				
				//文件不存在，跳转至404页面
				if(!file.exists()) {
					file = new File(ServerContext.webRoot + "/" + ServerContext.notFoundPage);
					response.setStatus(404);
				}else {
					response.setStatus(200);
				}

				// 响应头拼接
				response.setProtocol(ServerContext.protocol);
				response.setContentType(getContentTypeByEXT(file));
				response.setContentLength((int) file.length());
				
				ResponseFile(response, file);

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//处理登录请求
	private void loginUser(HttpRequest request, HttpResponse response) {
		//1.获取用户的登录信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//2.通过用户名和密码查询数据库
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//a.获取连接对象
			con = JDBCUtils.getConnection();
			//b.声明sql语句（骨架）
			String sql = "select * from user where username=? and password=?";
			//c.获取传输器
			ps = con.prepareStatement(sql);
			//d.设置sql参数
			ps.setString(1, username);
			ps.setString(2, password);
			//e.执行sql语句
			rs = ps.executeQuery();
			//f.處理結果
			File file = null;
			if(rs.next()) {
				file = new File(ServerContext.webRoot+"/login_success.html");
			}else {
				file = new File(ServerContext.webRoot+"/login_error.html");
			}
			
			//3.做出响应（提示）
			response.setProtocol(ServerContext.protocol);
			response.setStatus(200);
			response.setContentType(getContentTypeByEXT(file));
			response.setContentLength((int) file.length());
			
			//文件响应到浏览器
			ResponseFile(response, file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//f.释放资源
			JDBCUtils.close(con, ps, rs);
		}	
	}

	//处理注册请求
	private void registUser(HttpRequest request, HttpResponse response) {
		//1.获取用户的注册信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//System.out.println(username+": "+password);
		
		//2.将获取的用户信息保存到数据库
		Connection con = null;
		PreparedStatement ps = null;
		try {
			//a.获取连接对象
			con = JDBCUtils.getConnection();
			//b.声明sql语句（骨架）
			String sql = "insert into user value(null,?,?)";
			//c.获取传输器
			ps = con.prepareStatement(sql);
			//d.设置sql参数
			ps.setString(1, username);
			ps.setString(2, password);
			//e.执行sql语句
			ps.executeUpdate();
			
			//3.做出响应（提示注册成功）
			File file = new File(ServerContext.webRoot+"/regist_success.html");
			response.setProtocol(ServerContext.protocol);
			response.setStatus(200);
			response.setContentType(getContentTypeByEXT(file));
			response.setContentLength((int) file.length());
			
			//文件响应到浏览器
			ResponseFile(response, file);

		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//f.释放资源
			JDBCUtils.close(con, ps, null);
		}
		
	}

	//根据访问资源的后缀名，在map中查找对应的value
	private String getContentTypeByEXT(File file) {
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
