package guodu.net.warning.threads;

import guodu.net.warning.db.LoadDbConfig;
import guodu.net.warning.util.Loger;

public class LoadThread extends Thread{
    public LoadThread(){
    	
    }
    public void run(){
    	Loger.Info_log.info("[INFO]���ñ�����߳�����");
    	while(true){
    		try {
				sleep(60000);
				LoadDbConfig.Load();
			} catch (Exception e) {
				e.printStackTrace();
				Loger.Info_log.info("[ERROR]���ñ�����쳣");
			}      		
    	}
    }
}
