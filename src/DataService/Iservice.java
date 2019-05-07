package DataService;

import java.io.IOException;

public interface Iservice {
	/**
	 * 通用后台数据库调用类
	 * @param param
	 * @return
	 */
	public abstract String Excute(String param);
	/**
	 * 后台登录类
	 * @param param
	 * @return
	 * @throws IOException 
	 */
	public abstract String Login(String param) throws IOException;
}
