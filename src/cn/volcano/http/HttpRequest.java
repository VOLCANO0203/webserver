package cn.volcano.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
