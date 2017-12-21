package guodu.net.warning.start;

import guodu.net.warning.db.LoadDbConfig;
import guodu.net.warning.threads.FetchThread;
import guodu.net.warning.threads.LoadThread;
import guodu.net.warning.threads.SendThreads;
import guodu.net.warning.util.ConfigContainer;
/**
 * 监控报警内容发送程序
 * 
 * 读取gd_warning_wx表中flag为－1的报警信息，存储到内存中后，更新flag为0
 * 
 * 发送内存中的报警信息并纪录日志，分为微信，短信，微信和短信3种发送方式，皆为http接口，只纪录ecode
 * */
public class Start {
    public static void main(String[] arg){
    	
    	ConfigContainer.load();												//加载配制文件    	
    	
    	LoadDbConfig.Load();   												//加载数据库配置表
    	
    	new LoadThread().start();											//加载数据库配置表线程
    	
    	for(int i = 0 ; i < ConfigContainer.getFetchthreads() ; i ++){		//启动fetch线程
        	new FetchThread().start();
    	}
    	for(int j = 0 ; j < ConfigContainer.getSendthreads() ; j ++){		//启动send线程
    		new SendThreads().start();
    	}
    }
}
