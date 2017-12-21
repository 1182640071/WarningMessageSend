package guodu.net.warning.db;

import guodu.net.warning.entity.GD_WX_INFOMATION;
import guodu.net.warning.util.Loger;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OperDb {	
	/**
	 * �����ݿ���fetch����
	 * @param form ����
	 * @param count ץȡ����
	 * */
    public static List<Object[]> fetchInfo(String form , String count){
    	String sql = "select " + getProperty() + " from " + form + " where flag = '-1' and rownum <= " + count;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object> list = new ArrayList<Object>();
		DBManagerConnection_Main DBManagerConnection = DBManagerConnection_Main.getInstance();
		try {
			conn = DBManagerConnection.getConnection();
		    if (null == conn) {
			    Loger.Info_log.info("[ERROR]���ݿ�����ʧ��");
				return null;
			}	
		st = conn.createStatement();
		rs = st.executeQuery(sql);
		rsmd = rs.getMetaData();
		while(rs.next()){
	    	for(int i = 1 ; i <= rsmd.getColumnCount(); i++){
	    		Object a = new Object();
	    		a = rs.getObject(i);
	    		if(a == null|| "".equals(a)){
	    			list.add((Object)"");
	    		}else{
			    	list.add(a);	
	    		}
	    	}
		    if(list == null || list.size() == 0){
		    	result = null;
		    }else{
		    	result.add(list.toArray());	
		    	list.clear();
		    }
	    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManagerConnection_Main.closekind(rs , st , conn);
		}		
		return result;
    } 
    
    
    /**
     * ��ȡ��ѯ�ֶ�
     * ����GD_WX_INFOMATION�е����ԣ�ƴ��select�Ĳ�ѯ����
     * */
    private static String getProperty(){
    	Class<?> c = null;
    	try {
			c = Class.forName("guodu.net.warning.entity.GD_WX_INFOMATION");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Field f[] = c.getDeclaredFields();
    	StringBuffer sb = new StringBuffer();
    	for(Field g : f){
    		sb.append(g.getName()).append(",");
    	}
    	String result = sb.toString().substring(0, sb.toString().length()-1);
    	return result;
    }
    
    
    /**
     * �����Ѿ���ȡ�����ݵ�flag��ʾ
     * ��ȡ�������м䲻������κβ�ѯ����
     * @param List<GD_WX_INFOMATION>
     * @return Bollean
     * */
    public static Boolean updateInfo(List<GD_WX_INFOMATION> list){
    	String sql="update  gd_warning_wx  set flag = '0' where  id = ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		DBManagerConnection_Main DBManagerConnection = DBManagerConnection_Main.getInstance();		
		Boolean flag = true;
		try {
			conn = DBManagerConnection.getConnection();
			if(null == conn){
			    Loger.Info_log.info("[ERROR]���ݿ�����ʧ��");
			    return false;
			}
		    conn.setAutoCommit(false);
		    stmt = conn.prepareStatement(sql);
		    for(GD_WX_INFOMATION gwi : list){
		    	stmt.setString(1,gwi.getId());
				stmt.addBatch();
		    }
		    stmt.executeBatch();
		    conn.commit();
		    conn.setAutoCommit(true);
		} catch (SQLException e) {
		    Loger.Info_log.info("[ERROR]���ݸ���ʧ��");
		    flag = false;
			e.printStackTrace();
		}finally{
			DBManagerConnection_Main.closekind(stmt , conn);			
		}
    	return flag;
    }
    
    /**
     * ɾ���Ѿ���ȡ������
     * ��ȡ��ɾ���м䲻������κβ�ѯ����
     * @param List<GD_WX_INFOMATION>
     * @return Bollean
     * */
    public static Boolean deleteInfo(List<GD_WX_INFOMATION> list){
    	String sql="delete from GD_WX_INFOMATION  where  id = ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		DBManagerConnection_Main DBManagerConnection = DBManagerConnection_Main.getInstance();		
		Boolean flag = true;
		try {
			conn = DBManagerConnection.getConnection();
			if(null == conn){
			    Loger.Info_log.info("[ERROR]���ݿ�����ʧ��");
			    return false;
			}
		    conn.setAutoCommit(false);
//		    Loger.Info_log.info("[DEBUG]ִ��ɾ����Ϣsql��䣺" + sql);
		    stmt = conn.prepareStatement(sql);
		    for(GD_WX_INFOMATION gwi : list){
		    	stmt.setString(1,gwi.getId());
				stmt.addBatch();
		    }
		    stmt.executeBatch();
		    conn.commit();
		    conn.setAutoCommit(true);
		} catch (SQLException e) {
		    Loger.Info_log.info("[ERROR]����ɾ��ʧ��");
		    flag = false;
			e.printStackTrace();
		}finally{
			DBManagerConnection_Main.closekind(stmt , conn);			
		}
    	return flag;
    }
    
    
    
    
    
    public static void insertInfo(GD_WX_INFOMATION gwi, String ecode , String mobile){
    	String sql="insert into gd_warning_logsend (id , content , desmobile , ecode , warning_type) values ('"+ gwi.getId() +"', '"+ gwi.getContent() +"' ,'"+ mobile +"' , '"+ ecode +"', '"+ gwi.getWarning_type() +"')";
		Connection conn = null;
		Statement st = null;
		DBManagerConnection_Main DBManagerConnection = DBManagerConnection_Main.getInstance();		
		try {
			conn = DBManagerConnection.getConnection();
			if(null == conn){
			    Loger.Info_log.info("[ERROR]���ݿ�����ʧ��");
			    return;
			}
		    Loger.Info_log.info("[DEBUG]ִ�в�����Ϣsql��䣺" + sql);
			st = conn.createStatement();
            st.executeUpdate(sql);
		} catch (SQLException e) {
		    Loger.Info_log.info("[ERROR]���ݲ���ʧ��");
			e.printStackTrace();
		}finally{
			DBManagerConnection_Main.closekind(st , conn);			
		}
    }
    
    
    public static void main(String[] arg){
//    	fetchInfo("GD_WX_INFOMATION" , "50");
    }
}
