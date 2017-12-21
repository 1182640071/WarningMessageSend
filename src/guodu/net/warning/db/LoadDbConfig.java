package guodu.net.warning.db;

import guodu.net.warning.entity.GD_WARNING_TYPE;
import guodu.net.warning.entity.GD_WARNING_USER;
import guodu.net.warning.util.Loger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadDbConfig {
	private static Map<String , GD_WARNING_TYPE> mapType = new HashMap<String , GD_WARNING_TYPE>();	//key为任务id
	private static Map<String , GD_WARNING_USER> mapUser = new HashMap<String , GD_WARNING_USER>(); //key为username
	
	public static void Load(){
		getTableInfo(GD_WARNING_TYPE.class , "");
		getTableInfo(GD_WARNING_USER.class , "where state = '0'");
	}
	
	/**
	 * 根据文件名和特殊查询条件加载数据库中的配置表信息
	 * @param Class<?> c
	 * @param String where
	 * */
    private static void getTableInfo(Class<?> c , String where){
    	String sql = "select " + getProperty(c) + " from " + c.getSimpleName() + " " + where;
    	List<Object[]> list = fetchInfo(sql);
    	List<Object> listObject = getEntity(c , list);				//根据class文件名对此类对象进行封装
    	if(null == listObject || 0 == listObject.size()){
    		return;
    	}else{
    		setEntity(c , listObject);								//根据class文件名对此类进行封装
    	}
    }
	
    /**
     * 根据class文件名对此类进行封装
     * @param Class<?> d
     * @param List<Object>
     * */
    private static void setEntity(Class<?> d , List<Object> list){
    	if(null == list || 0 == list.size()){
    		return;
    	}
    	if(list.get(0) instanceof GD_WARNING_TYPE){
    		GD_WARNING_TYPE gwt = null;
    		Map<String , GD_WARNING_TYPE> map = new HashMap<String , GD_WARNING_TYPE>();
    		for(Object o : list){
    			gwt = (GD_WARNING_TYPE)o;
    			map.put(gwt.getWarning_id(), gwt);
    		}
    		mapType = map;
    	}else if(list.get(0) instanceof GD_WARNING_USER){
    		GD_WARNING_USER gwt = null;
    		Map<String , GD_WARNING_USER> map = new HashMap<String , GD_WARNING_USER>();
    		for(Object o : list){
    			gwt = (GD_WARNING_USER)o;
    			map.put(gwt.getUser_name(), gwt);
    		}
    		mapUser = map;
    	}else{
    		Loger.Info_log.info("[ERROR]信息封装错误，错误类LoadDbConfig，错误方法setEntity");
    	}
    }
    
    /**
     * 根据class文件名对此类对象进行封装
     * @param Class<?> d
     * @param List<Object[]>
     * @return List<Object>
     * */
    private static List<Object> getEntity(Class<?> d , List<Object[]> list){
    	if(null == list || list.size() == 0){
    		return null;
    	}
    	Class<?> c = null;
    	int index = 0 ;
    	List<Object> listObject = new ArrayList<Object>();
    	for(Object[] o : list){
    		index = 0;
    		Object k = null;
    		try {
    			c = Class.forName(d.getName());
    			k = c.newInstance();
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
        	Field f[] = c.getDeclaredFields();
        	Method met = null;
        	String method = "";
        	for(Field g : f){
        		method = "set" + g.getName().substring(0, 1).toUpperCase() + g.getName().substring(1, g.getName().length());
        		try {
        			if(("java.lang.String").equals(g.getType().getName())){
        				met = c.getMethod(method , String.class);
        				met.invoke(k , o[index].toString().trim());
        			}else{
        				met = c.getMethod(method , int.class);
        				met.invoke(k , Integer.parseInt(o[index].toString().trim()));
        			}
    				index ++;
    			} catch (SecurityException e) {
    				e.printStackTrace();
    			} catch (NoSuchMethodException e) {
    				e.printStackTrace();
    			} catch (IllegalArgumentException e) {
    				e.printStackTrace();
    			} catch (IllegalAccessException e) {
    				e.printStackTrace();
    			} catch (InvocationTargetException e) {
    				e.printStackTrace();
    			}
        	}
			listObject.add(k);
    	}
    	return listObject;
    }
    
	/**
	 * 从数据库中fetch数据
	 * @param form 表名
	 * @param count 抓取数量
	 * @return List<Object[]> Object[]数组的list
	 * */
    public static List<Object[]> fetchInfo(String sql){
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
			    Loger.Info_log.info("[ERROR]数据库连接失败");
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
			e.printStackTrace();
		}finally{
			DBManagerConnection_Main.closekind(rs , st , conn);
		}		
		return result;
    } 
    
    /**
     * 获取查询字段
     * 根据Class<?> d中的属性，拼出select的查询条件
     * */
    private static String getProperty(Class<?> d){
    	Class<?> c = null;
    	try {
			c = Class.forName(d.getName());
		} catch (ClassNotFoundException e) {
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
    
    public static Map<String, GD_WARNING_TYPE> getMapType() {
		return mapType;
	}

	public static void setMapType(Map<String, GD_WARNING_TYPE> mapType) {
		LoadDbConfig.mapType = mapType;
	}

	public static Map<String, GD_WARNING_USER> getMapUser() {
		return mapUser;
	}

	public static void setMapUser(Map<String, GD_WARNING_USER> mapUser) {
		LoadDbConfig.mapUser = mapUser;
	}

	public static void main(String[] args){
//    	new LoadDbConfig().Load();
//    	System.out.println(mapUser.get("test").getPassword());
//    	System.out.println(mapType.get("2").getMes_user());
    }

}
