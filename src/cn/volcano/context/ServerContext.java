package cn.volcano.context;

import java.util.HashMap;
import java.util.List;
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
	
	public static HashMap<String,String> map = new HashMap<>();
	
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
			port = Integer.valueOf(
					connector.attributeValue("port"));
			maxSize = Integer.valueOf(
					connector.attributeValue("maxSize"));
			protocol = connector.attributeValue("protocol");
			webRoot = service.elementText("webroot");
			notFoundPage = service.elementText("not-found-page");
			
			//解析typemappings的ext值和type值
			List<Element> list = server.elements("typemappings");
			for (Element element : list) {
				String key = element.attributeValue("ext");
				String value = element.attributeValue("type");
				map.put(key, value);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
