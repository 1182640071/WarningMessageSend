package guodu.net.warning.threads;

import guodu.net.warning.entity.GD_WX_INFOMATION;
import guodu.net.warning.send.SendFunction;
import guodu.net.warning.util.ConfigContainer;
import guodu.net.warning.util.GetSendMessage;
import guodu.net.warning.util.Loger;

public class SendThreads extends Thread{
    public void run(){
		Loger.Info_log.info("[info]send线程启动，线程id：" + this.getId());
    	while(true){
    		if(FetchThread.getVlist().isEmpty()){
				try {
					sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
    		}
    		GD_WX_INFOMATION[] fetch = GetSendMessage.getGD_WX_INFOMATION(Integer.parseInt(ConfigContainer.getFetchCount()));
    		if(null == fetch || fetch.length == 0){
    			continue;
    		}else{
    			for(GD_WX_INFOMATION f : fetch ){
    				SendFunction.sendWx(f) ;
    			}
    		}
    		try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
}
