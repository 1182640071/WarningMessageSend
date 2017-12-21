package guodu.net.warning.db;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBManagerConnection_Main {
	String workpath = System.getProperty("user.dir") + File.separator + "config" +File.separator + "c3p0-config.xml";
	static private DBManagerConnection_Main instance; // 唯一实例
	public static ComboPooledDataSource ds = null;


	public synchronized static DBManagerConnection_Main getInstance() {
		if (instance == null) {
			instance = new DBManagerConnection_Main();
		}
		return instance;
	}
//	/**
//	 * 建构函数私有以防止其它对象创建本类实例
//	 */
	private DBManagerConnection_Main() {
		System.out.println(workpath);
		System.setProperty("com.mchange.v2.c3p0.cfg.xml", workpath);
		ds = new ComboPooledDataSource();
	}

	/**
	 * 将连接对象返回给由名字指定的连接池
	 * 
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * @param con
	 *            连接对象
	 * @throws SQLException 
	 */
	public void freeConnection(Connection con) throws SQLException {
		con.close();
	}

	/**
	 * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数 限制,则创建并返回新连接
	 * 
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * @return Connection 可用连接或null
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	
	public static void closekind( PreparedStatement stmt, Connection conn) {

		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void closekind( PreparedStatement stmt) {

		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void closekind(Statement st , Connection conn ) {

		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void closekind( Statement st) {

		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void closekind(ResultSet rs ,  Statement st , Connection conn) {
		
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void closekind(ResultSet rs ,  Statement st) {
		
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args) throws SQLException {
		DBManagerConnection_Main ConnMgr = DBManagerConnection_Main.getInstance();
		Connection Connection1 = ConnMgr.getConnection();
		if (Connection1 == null) {
			System.out.println("无法得到数据库连接对象");
		} else {
			System.out.println("已得到数据库连接对象");
		}

		ConnMgr.freeConnection(Connection1);

		Connection1 = ConnMgr.getConnection();

		if (Connection1 == null) {
			System.out.println("无法得到数据库连接对象");
		} else {
			System.out.println("已得到数据库连接对象");
		}

		ConnMgr.freeConnection(Connection1);
	}

}
