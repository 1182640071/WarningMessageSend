package guodu.net.warning.send;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import guodu.net.warning.db.LoadDbConfig;
import guodu.net.warning.db.OperDb;
import guodu.net.warning.entity.GD_WX_INFOMATION;
import guodu.net.warning.util.ConfigContainer;
import guodu.net.warning.util.Loger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SendFunction {
	
	/**
	 * ������Ϣ
	 * @param GD_WX_INFOMATION
	 * */
    public  void sendWx_1(GD_WX_INFOMATION f){
    	String ecode = "-1";
    	StringBuffer sb = new StringBuffer();
		sb.append(f.getContent());
    	String desmobile[] = f.getDesmobile().split(",");
		for(int i = 0 ; i < desmobile.length ; i++){
			if("2".equals(f.getWarning_type())){
				ecode = SendMessage.postSendSynamicMsg(desmobile[i], "", f.getContent(), ConfigContainer.getDress(), ConfigContainer.getUser(), ConfigContainer.getPass() , f.getWarning_type());
			}else{
				ecode = SendMessage.postSendSynamicMsg(desmobile[i], "", f.getContent(), ConfigContainer.getURL(), ConfigContainer.getUserName(), ConfigContainer.getPassWord() , f.getWarning_type());
			}
			if(!"-107".equals(ecode)){
				if("1".equals(f.getWarning_type())){
					ecode = getJionEcode(ecode);
				}else if("2".equals(f.getWarning_type())){
                    ecode = getXmlEcode(ecode);
                    f.setId(f.getId().replace("W", "D"));
				}else{
					ecode = "12";//�������ʹ���
				}
			}
			sb.append(" !|mobile��" + desmobile[i] + " !|ecode: " + ecode);
			OperDb.insertInfo(f , ecode , desmobile[i]);
			ecode = "-1";
		}
		Loger.Info_log.info("[info]���ͣ�" + sb.toString());
    } 
    
	/**
	 * ������Ϣ
	 * @param GD_WX_INFOMATION
	 * */
    public static void sendWx(GD_WX_INFOMATION f){
    	if(null == f){
    		return;
    	}
    	String wx_user = null;
    	String wx_password = null;
    	String wx_url = null;
    	String mes_user = null;
    	String mes_password = null;
    	String mes_url = null;
    	String type = null;
    	String ecode = "-1";
    	StringBuffer sb = new StringBuffer();
		sb.append(f.getContent());
		String desmobile = f.getDesmobile();
		Boolean wx_flag = true;
		Boolean mes_flag = true;
		
		try {
			wx_user = LoadDbConfig.getMapType().get(f.getWarning_type()).getWx_user().trim(); 		//΢���˻�
			wx_password = LoadDbConfig.getMapUser().get(wx_user).getPassword().trim();				//�˻�����
			wx_url = LoadDbConfig.getMapUser().get(wx_user).getUrl().trim();						//��ַ
		} catch (Exception e) {
			Loger.Info_log.info("[ERROR]��ȡ����ͨ����Ϣʧ�ܣ�ȡ���˺���΢�ŷ��ͷ�ʽ��desmobile��" + desmobile + " " + f.getContent());
			wx_flag = false;
		}
		
		try {
			if(judgeKh(desmobile.trim()) > 0){
				mes_user = LoadDbConfig.getMapType().get("100").getMes_user().trim();				//�����˻�
				mes_password = LoadDbConfig.getMapUser().get(mes_user).getPassword().trim();		//�˻�����
				mes_url = LoadDbConfig.getMapUser().get(mes_user).getUrl().trim();					//��ַ
			}else{
				mes_user = LoadDbConfig.getMapType().get(f.getWarning_type()).getMes_user().trim();	//�����˻�
				mes_password = LoadDbConfig.getMapUser().get(mes_user).getPassword().trim();		//�˻�����
				mes_url = LoadDbConfig.getMapUser().get(mes_user).getUrl().trim();					//��ַ
			}
		} catch (Exception e) {
			Loger.Info_log.info("[ERROR]��ȡ����ͨ����Ϣʧ�ܣ��˺�����ŷ��ͷ�ʽ��desmobile��" + desmobile + " " + f.getContent());
			mes_flag = false;
		}
		if(!mes_flag && !wx_flag){
			return;
		}
		type = LoadDbConfig.getMapType().get(f.getWarning_type()).getType();
		if("1".equals(type)){																		//1:΢�ű���
			int index = 3;
			while(index >0 && wx_flag){
				ecode = SendMessage.postSendSynamicMsg(desmobile, "", f.getContent(), wx_url, wx_user, wx_password , "1");
				ecode = getJionEcode(ecode);
				if("0".equals(ecode) || index == 1){
					sb.append(" ΢�ŷ�ʽ��!|mobile��" + desmobile + " !|ecode: " + ecode);
					OperDb.insertInfo(f , ecode , desmobile);
					Loger.Info_log.info("[info]���ͣ�" + sb.toString());
					break;
				}else{
					index--;
					Loger.Info_log.info("[info]�����ύʧ�ܣ����·��ͣ�desmobile��" + desmobile + " " + f.getContent());
				}
			}
		}else if("2".equals(type) && mes_flag){														//2:���ű���
			int index = 3;
			while(index >0){
				ecode = SendMessage.postSendSynamicMsg(desmobile, "", f.getContent(), mes_url, mes_user, mes_password , "2");
				ecode = getXmlEcode(ecode);
                f.setId(f.getId().replace("W", "D"));
				if("03".equals(ecode) || index == 1){
					sb.append(" ���ŷ�ʽ��!|mobile��" + desmobile + " !|ecode: " + ecode);
					OperDb.insertInfo(f , ecode , desmobile);
					Loger.Info_log.info("[info]���ͣ�" + sb.toString());
					break;
				}else{
					index--;
					Loger.Info_log.info("[info]�����ύʧ�ܣ����·��ͣ�desmobile��" + desmobile + " " + f.getContent());
				}
			}
		}else{																						//3:΢��and����
			int index = 3;
			while(index >0 && wx_flag){
				ecode = SendMessage.postSendSynamicMsg(desmobile, "", f.getContent(), wx_url, wx_user, wx_password , "1");
				ecode = getJionEcode(ecode);
				if("0".equals(ecode) || index == 1){
					sb.append(" ΢�ŷ�ʽ��!|mobile��" + desmobile + " !|ecode: " + ecode);
					OperDb.insertInfo(f , ecode , desmobile);
					Loger.Info_log.info("[info]���ͣ�" + sb.toString());
					break;
				}else{
					index--;
					Loger.Info_log.info("[info]�����ύʧ�ܣ����·��ͣ�desmobile��" + desmobile + " " + f.getContent());
				}
			}			
			index = 3;
			while(index >0 && mes_flag){
				ecode = SendMessage.postSendSynamicMsg(desmobile, "", f.getContent(), mes_url, mes_user, mes_password , "2");
				ecode = getXmlEcode(ecode);
                f.setId(f.getId().replace("W", "D"));
				if("03".equals(ecode) || index == 1){
					sb.append(" ���ŷ�ʽ��!|mobile��" + desmobile + " !|ecode: " + ecode);
					OperDb.insertInfo(f , ecode , desmobile);
					Loger.Info_log.info("[info]���ͣ�" + sb.toString());
					break;
				}else{
					index--;
					Loger.Info_log.info("[info]�����ύʧ�ܣ����·��ͣ�desmobile��" + desmobile + " " + f.getContent());
				}
			}
		}
    }
    
    
    /**
     * ����json��ʽ
     * ����΢�ŷ��͵�ecode
     * @param ecode
     * @return ecode
     * */
    private static String getJionEcode(String ecode){
    	try {
			ecode = "[" + ecode + "]";
			JSONArray array = JSONArray.fromObject(ecode);
			JSONObject object = JSONObject.fromObject(array.get(0));
			ecode = object.get("errcode").toString();
		} catch (Exception e) {
            ecode = "-101";
		}
    	return ecode;
    }
    
    /**
     * ����xml��ʽ
     * �������ŷ��͵�ecode
     * @param ecode
     * @return ecode
     * */
    private static String getXmlEcode(String ecode){
    	SAXReader saxReader = new SAXReader();
	    Document document = null;
	    try {
			document = saxReader.read(new ByteArrayInputStream(ecode.getBytes("UTF-8")));
		    Element root1 = document.getRootElement().element("code");
		    ecode = root1.getData().toString();
		} catch (UnsupportedEncodingException e) {
			ecode = "-101";
		} catch (DocumentException e) {
			ecode = "-101";
		}
	    return ecode;
    }
    
    /**
     * �жϴ��ֻ����Ƿ�Ϊ�ͻ��ֻ���
     * ���ؽ��Ϊ0����ʾ���ǿͻ��ֻ��ţ�����0��Ϊ�ͻ��ֻ���
     * @param mobile �ֻ�����
     * @return int rs���
     * @exception ������Ϊ�ǿͻ�����
     * */
    private static int judgeKh(String mobile){
    	int rs = 0;
    	String sql = "select count(*) from gd_warning_khmobile where desmobile = '" + mobile.trim() + "'";
    	try {
			List<Object[]> list = LoadDbConfig.fetchInfo(sql);
			if(null != list && list.size() != 0 ){
				if(null != list.get(0) && list.get(0).length > 0){
					rs = Integer.parseInt(list.get(0)[0].toString());
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Loger.Info_log.info("[ERROR]�жϺ����Ƿ�Ϊ�ͻ������쳣��");
		}
    	return rs;
    }
    
    public static void main(String[] args){
    	int x = judgeKh("15810114808");
    	System.out.println(x);
    }
}
