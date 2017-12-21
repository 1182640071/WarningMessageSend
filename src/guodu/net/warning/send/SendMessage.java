package guodu.net.warning.send;

import guodu.net.warning.util.Loger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class SendMessage {
	
    /**
     * ΢�ŷ�����Ϣͷ����������
     * @param mobile �ֻ���
     * @param todept ������Ϊ��          
     * @param content ��Ϣ����
     * @return response �������ص��Զ����ʽ�Ĵ�
     * */
 	public static String postSendSynamicMsg(String mobile, String todept, String content , String url , String user, String password ,String flag) {
		/* url��ַ */
// 		http://221.179.180.138:9001/QxtSms/QxtFirewall?OperID=jlbank&OperPass=jlbank&SendTime=&ValidTime=&AppendID=&DesMobile=15810334106,&Content=%B9%FA%B6%BC%B6%CC%D0%C5%B2%E2%CA%D4&ContentType=8
 		String str = null;
 		String URL = url;
		/* ��Ϣ���� */
 		if("2".equals(flag)){
 			try {
				content = URLEncoder.encode(content,"GBK");
			} catch (UnsupportedEncodingException e) {
				Loger.Info_log.info("[ERROR]����ת���쳣");
			}
 			str = "OperID=" + user + "&OperPass=" + password + "&SendTime=&AppendID=&DesMobile=" + mobile + "&Content=" + content + "&ContentType=8";
 		}else{
				try {
					content = URLEncoder.encode(content.replace("%", "%25"),"GBK");
				} catch (UnsupportedEncodingException e) {
					Loger.Info_log.info("[ERROR]����ת���쳣");
				}
 			str = "uname=" + user + "&upass=" + password + "&touser="+ mobile + "&todept=" + todept + "&content=" + content;
 		} 
		/* ʹ��post��ʽ������Ϣ */
		String response = postURL(str, URL);
		/* ������Ӧ */
		return response;
	}

	/** post��ʽ ����url�� */
	/**
	 * @param commString ��Ҫ���͵�url������
	 * @param address ��Ҫ���͵�url��ַ
	 * @return rec_string �������ص��Զ����ʽ�Ĵ�
	 * @catch Exception
	 */
	public static String postURL(String commString, String address) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlConn = null;
		try {
			/* �õ�url��ַ��URL�� */
			url = new URL(address);
			/* ��ô���Ҫ���͵�url���� */
			urlConn = (HttpURLConnection) url.openConnection();
			/* �������ӳ�ʱʱ�� */
			urlConn.setConnectTimeout(30000);
			/* ���ö�ȡ��Ӧ��ʱʱ�� */
			urlConn.setReadTimeout(30000);
			/* ����post���ͷ�ʽ */
			urlConn.setRequestMethod("GET");
			/* ����commString */
			urlConn.setDoOutput(true);
			OutputStream out = urlConn.getOutputStream();
			out.write(commString.getBytes());
			out.flush();
			out.close();
			/* ������� ��ȡ������������������ */
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream(), "GBK"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			/* ������Ϲر������� */
			rd.close();
		} catch (Exception e) {
			/* �쳣���� */
			rec_string = "-107";
		} finally {
			/* �ر�URL���� */
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
		/* ������Ӧ���� */
		return rec_string;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException{
//		String ss = "���Զ���%25";
//		try {
//			ss = URLEncoder.encode(ss,"utf-8");
//			String aa = URLDecoder.decode(ss,"utf-8");
//			System.out.println(aa);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String content = URLEncoder.encode("���ʷ�%26ʦ�ܶ�" , "utf-8");
		System.out.println(content);
//		content = URLDecoder.decode("���ʷ�%26ʦ�ܶ�");
		content = URLDecoder.decode("���ʷ�%26ʦ�ܶ�", "utf-8");
		System.out.println(content);
		String a = new SendMessage().postSendSynamicMsg("15117956265" , "" , content , "http://221.179.180.156:8090/weixin/SendWX" , "test" , "test" ,"1");
////		String a = new SendMessage().postSendSynamicMsg("15117956265" , "" , "ceshiyixia111%" , "http://221.179.180.138:9001/QxtSms/QxtFirewall" , "gdzbw" , "mike1233" ,"2");
		System.out.println( a );
	}
}
