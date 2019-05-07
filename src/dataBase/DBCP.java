package dataBase;

import java.io.InputStream;
import java.sql.Connection ;  
import java.sql.SQLException ;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;  
  
public class DBCP {  
    protected static Connection conn=null ;  
    // 创建数据库连接对象 ( 数据源 )  
    private static BasicDataSource dataSource=null;  
    // 配置数据源  
    static   
    {  
        DataSourceConfig();  
    }  
    /** 
     * 设置 dataSource各属性值 
     */  
    private static void DataSourceConfig()  
    {     
        	/*dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); //数据库驱动  
            dataSource.setUsername("sa");  //用户名  
            dataSource.setPassword("123123");  //密码  
            dataSource.setUrl("jdbc:sqlserver://127.0.0.1\\WORK");  //连接url  
            dataSource.setInitialSize(10); // 初始的连接数；    
            dataSource.setMaxTotal(10);  //最大连接数  
            dataSource.setMaxIdle(80);  // 设置最大空闲连接  
            dataSource.setMaxWaitMillis(6000);  // 设置最大等待时间  
            dataSource.setMinIdle(5);  // 设置最小空闲连接  
*/    
    	InputStream in = DBCP.class.getClassLoader().getResourceAsStream("dbcp.properties");
    	Properties pro = new Properties();
    	try {
			pro.load(in);
			dataSource = BasicDataSourceFactory.createDataSource(pro);
		} catch (Exception e) {
			System.out.println("数据库连接失败，请检查配置文件!");
			e.printStackTrace();
		}
    	
    	}  
    /** 
     * 获得连接对象 
     * 
     * @return 
     */  
    public static Connection getConnection()  
    {  
        try   
        {  
            // 从连接池中获得连接对象  
            if(conn==null||conn.isClosed())  
            {  
                conn=dataSource.getConnection();  
            }  
        }  
        catch(SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn ;  
    }
	public synchronized static void closeConnection(Connection conn) throws SQLException {
		if (conn != null &&!conn.isClosed() ) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		conn = null;
	}
}  
