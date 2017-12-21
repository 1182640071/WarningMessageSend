package guodu.net.warning.threads;

import guodu.net.warning.entity.GD_WX_INFOMATION;
import guodu.net.warning.start.OperationFetch;
import guodu.net.warning.util.ConfigContainer;
import guodu.net.warning.util.Loger;
import java.util.List;
import java.util.Vector;

public class FetchThread extends Thread{
	private static Vector<GD_WX_INFOMATION> vlist = new Vector<GD_WX_INFOMATION>();
	
    public void run(){
		Loger.Info_log.info("[info]fetch线程启动，线程id：" + this.getId());
    	while(true){
    		
    		if(vlist.size() >= ConfigContainer.getFetchLimt()){
        		try {
    				sleep(1000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		OperationFetch of = OperationFetch.getInstance();
        	List<GD_WX_INFOMATION> list = of.getMessage();
        	if(null == list || list.size() ==0){
        		try {
    				sleep(1000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        		continue;
        	}else{
        		for(GD_WX_INFOMATION gwi : list){
        			if(null == gwi){
        				continue;
        			}
        			vlist.add(gwi);
        		}
        	}
        	
    		try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}

    }

	public static Vector<GD_WX_INFOMATION> getVlist() {
		return vlist;
	}

	public static void setVlist(Vector<GD_WX_INFOMATION> vlist) {
		FetchThread.vlist = vlist;
	}
    
    
    
}
