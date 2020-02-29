package cn.volcano.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * ������װ��������Ϣ
 * @author VOLCANO
 *
 */
public class HttpRequest {
	//����ʽ
	private String method;
	//�������Դ·��
	private String uri;
	//��ѭ��Э�����Ͱ汾��
	private String protocol;
	
	//����paramMap���ϣ����ڴ�������е����в�����Ϣ
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
				//����Ĭ����ҳ
				if(uri.equals("/")) {
					uri = "/index.html";
				}
				protocol = datas[2];
				
				//�������еĲ�����װ��paramMap������
				paramMap = new HashMap<>();
				if(uri.contains("?")) {
					//uri=/...?username=zhangsan&password=123
					//��������Դ�н�ȡ���в�����ɵ��ַ���
					String paramStrs = uri.substring(uri.indexOf("?")+1);
					//�����в�����ɵ��ַ��������и�
					String[] params = paramStrs.split("&");
					
					for (String param : params) {
						String[] ch = param.split("=");
						String key = ch[0];
						String value = ch[1];
						//��������
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
