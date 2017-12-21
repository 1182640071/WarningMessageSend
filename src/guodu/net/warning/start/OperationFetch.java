package guodu.net.warning.start;

import guodu.net.warning.db.OperDb;
import guodu.net.warning.entity.GD_WX_INFOMATION;
import guodu.net.warning.util.ConfigContainer;
import guodu.net.warning.util.Loger;
import java.util.ArrayList;
import java.util.List;

public class OperationFetch{
	private static volatile OperationFetch instance = null; // 唯一实例
	
//	public synchronized static OperationFetch getInstance() {
//		if (instance == null) {
//			instance = new OperationFetch(); 
//		}
//		return instance;
//	}
	public  static OperationFetch getInstance() {
		if (instance == null) {
			synchronized (OperationFetch.class) {
				if (instance == null) {
					instance = new OperationFetch(); 
				}
			}
		}
		return instance;
	}
	
	private  OperationFetch(){
	}
	
	/**
	 * 加锁防止多线程同时操作数据重复
	 * 获取需要发送的数据
	 * 获得消息后进行删除
	 * 获取和删除必须先后进行不能在获取和删除中添加其它查询删除操作
	 * @return List<GD_WX_INFOMATION> 需要发送的信息
	 * */
	public synchronized List<GD_WX_INFOMATION> getMessage(){
    	//处理数据
		List<GD_WX_INFOMATION> result = null;
		try {
			List<Object[]> list = new ArrayList<Object[]>(); 
			//获取需要发送的数据
			list = OperDb.fetchInfo("gd_warning_wx", ConfigContainer.getFetchCount());
			if(null == list || list.size()<= 0){
				return null;
			}
			result = changeInfo(list);
			//删除数据
			Boolean flag = OperDb.updateInfo(result);
			if(!flag){
				return null;
			}
		} catch (Exception e) {
		    Loger.Info_log.info("[ERROR]数据fetch异常");
			return null;
		}   	
    	return result;
    }
    
     
    /**
     * 将object数组转为GD_WX_INFOMATION类中的数据
     * @param List<Object[]>
     * @return List<GD_WX_INFOMATION>
     * */
    private static List<GD_WX_INFOMATION> changeInfo(List<Object[]> result ){
    	if(null == result || result.size() <= 0){
    		return null;
    	}
    	List<GD_WX_INFOMATION> list = new ArrayList<GD_WX_INFOMATION>();
    	for(Object[] o : result){
    		if(null == o){
    			continue;
    		}
        	GD_WX_INFOMATION wx = new GD_WX_INFOMATION();
    		wx.setId(o[0].toString().trim());
    		wx.setCreate_time(o[1].toString().trim());
    		wx.setContent(o[2].toString().trim());
    		wx.setDesmobile(o[3].toString().trim());
    		wx.setWarning_type(o[4].toString().trim());
        	list.add(wx);
    	}
    	return list;
    }
    
    public static void main(String[] args){
    	ConfigContainer.load();
//    	getMessage();
    }
}
