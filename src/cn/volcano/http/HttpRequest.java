package cn.volcano.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来封装请求行信息
 * @author VOLCANO
 *
 */
public class HttpRequest {
	//请求方式
	private String method;
	//请求的资源路径
	private String uri;
	//遵循的协议名和版本号
	private String protocol;
	
	//声明paramMap集合，用于存放请求中的所有参数信息
	private Map<String, String> paramMap;
	
	public HttpRequest(InputStream in) {
		try {
			BufferedReader reader = new BufferedReader(
									new InputStreamReader(in));
			
			//line-> GET /hello.html HTTP/1.1
			String line = reader.readLine();
			
			if(line != null && line.length()>0) {
				String[] datas = line.split(" ");
				method = datas[0];
				uri = datas[1];
				//设置默认主页
				if(uri.equals("/")) {
					uri = "/index.html";
				}
				protocol = datas[2];
				
				//将请求中的参数封装在paramMap集合中
				paramMap = new HashMap<>();
				if(uri.contains("?")) {
					//uri=/...?username=zhangsan&password=123
					//从请求资源中截取所有参数组成的字符串
					String paramStrs = uri.substring(uri.indexOf("?")+1);
					//将所有参数组成的字符串进行切割
					String[] params = paramStrs.split("&");
					
					for (String param : params) {
						String[] ch = param.split("=");
						String key = ch[0];
						String value = ch[1];
						//供测试用
						//System.out.println(key+": "+value);
						
						paramMap.put(key, value);
					}	
				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParameter(String key) {
		return paramMap.get(key);
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
