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
     * 微信发送消息头，参数设置
     * @param mobile 手机号
     * @param todept 可设置为空          
     * @param content 消息内容
     * @return response 国都返回的自定义格式的串
     * */
 	public static String postSendSynamicMsg(String mobile, String todept, String content , String url , String user, String password ,String flag) {
		/* url地址 */
// 		http://221.179.180.138:9001/QxtSms/QxtFirewall?OperID=jlbank&OperPass=jlbank&SendTime=&ValidTime=&AppendID=&DesMobile=15810334106,&Content=%B9%FA%B6%BC%B6%CC%D0%C5%B2%E2%CA%D4&ContentType=8
 		String str = null;
 		String URL = url;
		/* 消息参数 */
 		if("2".equals(flag)){
 			try {
				content = URLEncoder.encode(content,"GBK");
			} catch (UnsupportedEncodingException e) {
				Loger.Info_log.info("[ERROR]编码转换异常");
			}
 			str = "OperID=" + user + "&OperPass=" + password + "&SendTime=&AppendID=&DesMobile=" + mobile + "&Content=" + content + "&ContentType=8";
 		}else{
				try {
					content = URLEncoder.encode(content.replace("%", "%25"),"GBK");
				} catch (UnsupportedEncodingException e) {
					Loger.Info_log.info("[ERROR]编码转换异常");
				}
 			str = "uname=" + user + "&upass=" + password + "&touser="+ mobile + "&todept=" + todept + "&content=" + content;
 		} 
		/* 使用post方式发送消息 */
		String response = postURL(str, URL);
		/* 返回响应 */
		return response;
	}

	/** post方式 发送url串 */
	/**
	 * @param commString 需要发送的url参数串
	 * @param address 需要发送的url地址
	 * @return rec_string 国都返回的自定义格式的串
	 * @catch Exception
	 */
	public static String postURL(String commString, String address) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlConn = null;
		try {
			/* 得到url地址的URL类 */
			url = new URL(address);
			/* 获得打开需要发送的url连接 */
			urlConn = (HttpURLConnection) url.openConnection();
			/* 设置连接超时时间 */
			urlConn.setConnectTimeout(30000);
			/* 设置读取响应超时时间 */
			urlConn.setReadTimeout(30000);
			/* 设置post发送方式 */
			urlConn.setRequestMethod("GET");
			/* 发送commString */
			urlConn.setDoOutput(true);
			OutputStream out = urlConn.getOutputStream();
			out.write(commString.getBytes());
			out.flush();
			out.close();
			/* 发送完毕 获取返回流，解析流数据 */
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream(), "GBK"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			/* 解析完毕关闭输入流 */
			rd.close();
		} catch (Exception e) {
			/* 异常处理 */
			rec_string = "-107";
		} finally {
			/* 关闭URL连接 */
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
		/* 返回响应内容 */
		return rec_string;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException{
//		String ss = "测试短信%25";
//		try {
//			ss = URLEncoder.encode(ss,"utf-8");
//			String aa = URLDecoder.decode(ss,"utf-8");
//			System.out.println(aa);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String content = URLEncoder.encode("请问法%26师奋斗" , "utf-8");
		System.out.println(content);
//		content = URLDecoder.decode("请问法%26师奋斗");
		content = URLDecoder.decode("请问法%26师奋斗", "utf-8");
		System.out.println(content);
		String a = new SendMessage().postSendSynamicMsg("15117956265" , "" , content , "http://221.179.180.156:8090/weixin/SendWX" , "test" , "test" ,"1");
////		String a = new SendMessage().postSendSynamicMsg("15117956265" , "" , "ceshiyixia111%" , "http://221.179.180.138:9001/QxtSms/QxtFirewall" , "gdzbw" , "mike1233" ,"2");
		System.out.println( a );
	}
}
