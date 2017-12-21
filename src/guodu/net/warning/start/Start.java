package guodu.net.warning.start;

import guodu.net.warning.db.LoadDbConfig;
import guodu.net.warning.threads.FetchThread;
import guodu.net.warning.threads.LoadThread;
import guodu.net.warning.threads.SendThreads;
import guodu.net.warning.util.ConfigContainer;
/**
 * ��ر������ݷ��ͳ���
 * 
 * ��ȡgd_warning_wx����flagΪ��1�ı�����Ϣ���洢���ڴ��к󣬸���flagΪ0
 * 
 * �����ڴ��еı�����Ϣ����¼��־����Ϊ΢�ţ����ţ�΢�źͶ���3�ַ��ͷ�ʽ����Ϊhttp�ӿڣ�ֻ��¼ecode
 * */
public class Start {
    public static void main(String[] arg){
    	
    	ConfigContainer.load();												//���������ļ�    	
    	
    	LoadDbConfig.Load();   												//�������ݿ����ñ�
    	
    	new LoadThread().start();											//�������ݿ����ñ��߳�
    	
    	for(int i = 0 ; i < ConfigContainer.getFetchthreads() ; i ++){		//����fetch�߳�
        	new FetchThread().start();
    	}
    	for(int j = 0 ; j < ConfigContainer.getSendthreads() ; j ++){		//����send�߳�
    		new SendThreads().start();
    	}
    }
}
