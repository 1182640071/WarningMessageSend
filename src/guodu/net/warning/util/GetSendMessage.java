package guodu.net.warning.util;

import guodu.net.warning.entity.GD_WX_INFOMATION;
import guodu.net.warning.threads.FetchThread;



public class GetSendMessage {
	  /**
	    * 获取内存中的发送数据
	    * @param count
	    * @return GD_WX_INFOMATION[]
	    * */
		public static GD_WX_INFOMATION[] getGD_WX_INFOMATION(int count){
			int batchNum = 0;
			int size = FetchThread.getVlist().size();
			if (count > size) {
				batchNum = size;
			} else {
				batchNum = count;
			}
			GD_WX_INFOMATION[] result = new GD_WX_INFOMATION[batchNum];
			GD_WX_INFOMATION gwi = null;
			for (int i = 0; i < batchNum; i++) {
				gwi = get();
				if(gwi != null)
				{
					result[i] = gwi;
				}
			}
			
			return result;		
		}

		public synchronized static GD_WX_INFOMATION get() {
			GD_WX_INFOMATION gwi;
			if (FetchThread.getVlist().isEmpty()) {
				return null;
			} else {
				gwi = FetchThread.getVlist().remove(FetchThread.getVlist().size() - 1);
			}
			return gwi;
		}
		
}
