package cn.volcano.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 用来封装服务器端的参数信息
 * @author VOLCANO
 *
 */
public class ServerContext {
	public static int port;
	public static int maxSize;
	public static String protocol;
	public static String webRoot;
	
	public static String notFoundPage;
	
	//存放状态码和对应的描述短语
	public static Map<Integer, String> statusMap = new HashMap<>();
	
	//存放Content-Type的值
	public static Map<String,String> map = new HashMap<>();
	
	static {
		init();
	}
	
    //解析存放web参数的xml文件
	private static void init() {
		try {
			SAXReader reader = new SAXReader();
			//加载文件
			Document doc = reader.read("config/web.xml");
			//获取元素
			Element server = doc.getRootElement();
			Element service = server.element("service");
			Element connector = service.element("connector");
			Element typemappings = service.element("typemappings");
			Element statusmappings = service.element("statusmappings");
			port = Integer.valueOf(
					connector.attributeValue("port"));
			maxSize = Integer.valueOf(
					connector.attributeValue("maxSize"));
			protocol = connector.attributeValue("protocol");
			webRoot = service.elementText("webroot");
			notFoundPage = service.elementText("not-found-page");
			
			//解析typemappings的ext值和type值
			List<Element> list = typemappings.elements("typemapping");
			for (Element element : list) {
				String key = element.attributeValue("ext");
				String value = element.attributeValue("type");
				map.put(key, value);
			}

			//解析statusmappings的st值和stline值
			List<Element> list2 = statusmappings.elements("statusmapping");
			for (Element element : list2) {
				int key = Integer.valueOf(element.attributeValue("st"));
				String value = element.attributeValue("stline");
				statusMap.put(key, value);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
