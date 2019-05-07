package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import Util.NameUtil;

/**
 * 数据库操作基类
 * @author Administrator
 *
 */
public abstract class JdbcBuilder {
	private Connection conn = null;
	public Connection getConn() throws SQLException {
		this.setConn();
		if(this.conn == null||conn.isClosed()){
			throw new SQLException("未取到数据库连接！");
		}
		return this.conn;
	}
	
	public void setConn() throws SQLException {
		conn = DBCP.getConnection();
	}
	
	/**
	 * 返回List集合，且集合中存储的Map对象
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> getListForMap(String sql, Object... params) throws SQLException {
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		List<Map<String,Object>> list = queryRunner.query(conn, sql, new MapListHandler(), params);
		DBCP.closeConnection(conn);
		return list;
	}
	
	/**
	 * 封装删除，编辑，新增等操作
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected int updateAll(String sql, Object... params) throws SQLException {
		Connection conn = this.getConn();;
		QueryRunner queryRunner = new QueryRunner();
		int i = queryRunner.update(conn, sql, params);
		DBCP.closeConnection(conn);
		return i;
	}
	/**
	 * 返回Map集合
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected Map<String,Object> getForMap(String sql, Object... params) throws SQLException {
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		Map<String,Object> map = queryRunner.query(conn, sql, new MapHandler(), params);
		DBCP.closeConnection(conn);
		return map;
	}
	
	/**
	 * 从数据库取出一个对象，在进行封装
	 * 注：数据库的列名对应对象的属性名
	 * @param sql
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	protected <T> T getModel(String sql,Class<T> obj,Object... params) throws SQLException{
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		
		T t = queryRunner.query(conn, sql,new BeanHandler<T>(obj),params); 
		DBCP.closeConnection(conn);
		return t;
	}
	/**
	 * 从数据库取出一个对象，在进行封装,最后封装到一个List中
	 * 注：数据库的列名对应对象的属性名
	 * @param sql
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	protected <T>List<T> getModelList(String sql,Class<T> obj,Object... params) throws SQLException{
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		List<T> list = queryRunner.query(conn, sql, new BeanListHandler<T>(obj), params);
		DBCP.closeConnection(conn);
		return list;
	}
	/**
	 * 查询封装DOM
	 * @param sql
	 * @param Cvaluse
	 * @return
	 * @throws SQLException
	 */
	protected Document ExecQryXml(String sql,List<?> Cvaluse)throws SQLException{
		ResultSet rs = null;
		ResultSetMetaData rstr = null;
		Document document = null;
		Connection conn = this.getConn();
		PreparedStatement ps = conn.prepareStatement(sql);
		if(Cvaluse != null){
			for(int i = 1;i <= Cvaluse.size();i++){
				ps.setObject(i, Cvaluse.get(i-1));
			}
		}
		rs = ps.executeQuery();
		document = DocumentHelper.createDocument();
		Element rsElement = document.addElement("xlm");
		rstr = rs.getMetaData();
		Element fields = rsElement.addElement(NameUtil.Dom_Explains);
		for (int i = 1; i <= rstr.getColumnCount(); i++) {
			Element field = fields.addElement(NameUtil.Dom_Explain);
			//field.addAttribute("ColName", rstr.getCatalogName(i));
			field.addAttribute("ColName", rstr.getColumnLabel(i));
			field.addAttribute("TypeName", rstr.getColumnTypeName(i));
			field.addAttribute("MaxLength", String.valueOf(rstr.getColumnDisplaySize(i)));
		}
		Element value = rsElement.addElement(NameUtil.Dom_Values);
		while(rs.next()){
			Element fieldvalue = value.addElement(NameUtil.Dom_Value);
			for(int i = 1;i<=rstr.getColumnCount(); i++){
				fieldvalue.addAttribute(rstr.getColumnLabel(i),(rs.getString(i)==null?"":rs.getString(i)));
			}
		}
		
		if(rs!=null){
			rs.close();
			rs = null;
		}
		ps.close();
		ps=null;
		return document;
	}
	
	/**
	 * 普通查询方法
	 * @param sql
	 * @param Cvaluse
	 * @return
	 * @throws SQLException
	 */
	protected ResultSet ExecQry(String sql,List<?> Cvaluse)throws SQLException{
		ResultSet rs = null;
		Connection conn = this.getConn();
		PreparedStatement ps = conn.prepareStatement(sql);
		if(Cvaluse != null){
			for(int i = 1;i <= Cvaluse.size();i++){
				ps.setObject(i, Cvaluse.get(i-1));
			}
		}
		
		rs = ps.executeQuery();
		return rs;
	}
	/**
	 * 普通修改方法（返回修改的条数）
	 * @param sql
	 * @param Cvaluse
	 * @return
	 * @throws SQLException
	 */
	protected int ExecModel(String sql,List<?> Cvaluse)throws SQLException{
		int number = 0;
		Connection conn = this.getConn();
		PreparedStatement ps = conn.prepareStatement(sql);
		if(Cvaluse != null){
			for(int i = 1;i <= Cvaluse.size();i++){
				ps.setObject(i, Cvaluse.get(i-1));
			}
		}
		number = ps.executeUpdate();
		return number;
	}
	/**
	 * 归还该对象的数据库链接
	 * @throws SQLException
	 */
	protected void ReturnConn() throws SQLException{
		DBCP.closeConnection(this.conn);
	}
	/**
	 * 获取一个新的链接
	 * @return
	 * @throws SQLException
	 */
	protected Connection GetNewConn() throws SQLException{
		Connection conn = DBCP.getConnection();
		return conn;
	}
	
}	
