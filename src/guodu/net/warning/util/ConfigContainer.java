package guodu.net.warning.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigContainer {
	
	private static String  fetchCount = null;
	private static String  userName = null;
	private static String  passWord = null;
	private static String  URL = null;
    private static int  fetchthreads = 1;
    private static int  sendthreads = 1;
    private static int  fetchLimt = 200;
    private static String dress = null;
    private static String user = null;
    private static String pass = null;
    
	
		/**
		 * �������ṩ�����ļ���Ϣ���ع���
		 * */
     public static void load(){   	 
    	 Map<?,?> map = loadFunction("common");
    	 fetchCount = getInfo("fetchCount","50", map);
    	 userName = getInfo("userName","test", map);
    	 passWord = getInfo("passWord","test", map);
    	 dress = getInfo("dress","http://221.179.180.138:9001/QxtSms/QxtFirewall", map);
    	 user = getInfo("user","gdzbw", map);
    	 pass = getInfo("pass","mike1233", map);
    	 URL = getInfo("URL","http://218.241.67.222/weixin/SendWX", map);
    	 fetchthreads = Integer.parseInt(getInfo("fetchthreads","2", map));
    	 sendthreads = Integer.parseInt(getInfo("sendthreads","2", map));
    	 fetchLimt = Integer.parseInt(getInfo("fetchLimt","200", map));
     }
   
     /**
     * �˷������Ի�ýڵ�����
     * @param e �ڵ�
     * @param defult �ڵ�����Ϊ��ʱ��Ĭ��ֵ
     * @param map
     * @return result ��ѯ���
     * */
     public static String getInfo(String e , String  defult , Map<?,?> map)
     {
    	 String result = (String) map.get(e);
    	 if("".equals(result))
    	 {
    		 result = defult;
    	 }
    	 return result;
     }
     
     /**
      * �˷���ʵ�ֽ��ڵ�������ӽڵ�����Դ�ŵ�map�в�����
      * @param e �ڵ�
      * @return map 
      * */
     public  static Map<?, ?> loadFunction(String e)
     {
    	//����������
   	  SAXReader saxreader = new SAXReader();
   	  
   	  //��ȡ�ĵ�
   	  Document doc = null;
   	  Map<String,String> map = null;
		try {
			doc = saxreader.read(new File("config/config.xml"));
			map = new HashMap<String,String>();
	    	  //��ȡ�����ڵ�
	    	Element root = doc.getRootElement().element(e);
	    	  //�����нڵ�����Դ�ŵ�map��
	    	for ( Iterator<?> iterInner = root.elementIterator(); iterInner.hasNext(); ) {   
	    		Element elementInner = (Element) iterInner.next();
	    	    map.put(elementInner.getName(), root.elementText(elementInner.getName()));
	    	}
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	 return map;
     }

	public static String getFetchCount() {
		return fetchCount;
	}

	public static void setFetchCount(String fetchCount) {
		ConfigContainer.fetchCount = fetchCount;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		ConfigContainer.userName = userName;
	}

	public static String getPassWord() {
		return passWord;
	}

	public static void setPassWord(String passWord) {
		ConfigContainer.passWord = passWord;
	}

	public static String getURL() {
		return URL;
	}

	public static void setURL(String uRL) {
		URL = uRL;
	}

	public static int getFetchthreads() {
		return fetchthreads;
	}

	public static void setFetchthreads(int fetchthreads) {
		ConfigContainer.fetchthreads = fetchthreads;
	}

	public static int getSendthreads() {
		return sendthreads;
	}

	public static void setSendthreads(int sendthreads) {
		ConfigContainer.sendthreads = sendthreads;
	}

	public static int getFetchLimt() {
		return fetchLimt;
	}

	public static void setFetchLimt(int fetchLimt) {
		ConfigContainer.fetchLimt = fetchLimt;
	}

	public static String getDress() {
		return dress;
	}

	public static void setDress(String dress) {
		ConfigContainer.dress = dress;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ConfigContainer.user = user;
	}

	public static String getPass() {
		return pass;
	}

	public static void setPass(String pass) {
		ConfigContainer.pass = pass;
	}
     
	
}
