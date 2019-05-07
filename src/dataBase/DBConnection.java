package dataBase;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 利用c3p0获取数据库连接
 * 使用单例模式来封装获取C3P0组件产生的数据库连接对象
 * @author Administrator
 *
 */
public class DBConnection {
	public static DBConnection db = null;
	public static DataSource ds = null;
	
	public DBConnection() {
		if (ds == null) {
			ds = new ComboPooledDataSource(); //初始化DataSource数据源
		}
	}
	
	private static DBConnection getInstance() { //初始化DBConnection的对象
		if (db == null) {
			db = new DBConnection();
		}
		return db;
	}
	
	private DataSource getDataSource() { //返回已经初始化好的DataSource对象
		return ds;
	}
	
	public synchronized static Connection getConnection() throws SQLException {
		return getInstance().getDataSource().getConnection();
	}
	
	public synchronized static void closeConnection(Connection conn) throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
			conn = null;
		}
	}
}
