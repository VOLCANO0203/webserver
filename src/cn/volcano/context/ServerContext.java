package cn.volcano.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ������װ�������˵Ĳ�����Ϣ
 * @author VOLCANO
 *
 */
public class ServerContext {
	public static int port;
	public static int maxSize;
	public static String protocol;
	public static String webRoot;
	
	public static String notFoundPage;
	
	//���״̬��Ͷ�Ӧ����������
	public static Map<Integer, String> statusMap = new HashMap<>();
	
	//���Content-Type��ֵ
	public static Map<String,String> map = new HashMap<>();
	
	static {
		init();
	}
	
    //�������web������xml�ļ�
	private static void init() {
		try {
			SAXReader reader = new SAXReader();
			//�����ļ�
			Document doc = reader.read("config/web.xml");
			//��ȡԪ��
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
			
			//����typemappings��extֵ��typeֵ
			List<Element> list = typemappings.elements("typemapping");
			for (Element element : list) {
				String key = element.attributeValue("ext");
				String value = element.attributeValue("type");
				map.put(key, value);
			}

			//����statusmappings��stֵ��stlineֵ
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
