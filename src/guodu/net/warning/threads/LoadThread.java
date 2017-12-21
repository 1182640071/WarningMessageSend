package guodu.net.warning.threads;

import guodu.net.warning.db.LoadDbConfig;
import guodu.net.warning.util.Loger;

public class LoadThread extends Thread{
    public LoadThread(){
    	
    }
    public void run(){
    	Loger.Info_log.info("[INFO]配置表加载线程启动");
    	while(true){
    		try {
				sleep(60000);
				LoadDbConfig.Load();
			} catch (Exception e) {
				e.printStackTrace();
				Loger.Info_log.info("[ERROR]配置表加载异常");
			}      		
    	}
    }
}
