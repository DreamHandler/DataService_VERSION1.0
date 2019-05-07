package Util;
/**
 * 公共静态字段对象
 * @author Administrator
 *
 */
public class NameUtil {
	/**
	 * 系统编码
	 */
	public static final String SysNo = "01";
	
	/**
	 * DOM对象字段说明父节点名称
	 */
	public static final String Dom_Explains = "Fields";
	/**
	 * DOM对象字段说明子节点名称
	 */
	public static final String Dom_Explain = "Field";
	/**
	 * DOM对象值节点父节点名称
	 */
	public static final String Dom_Values = "FieldsValue";
	/**
	 * DOM对象值节点子节点名称
	 */
	public static final String Dom_Value = "FieldValue";
	
	/**
	 * 成功消息
	 * @return
	 */
	public static final String RtnStrSaveSuccess() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
	    buffer.append("<RES>");
	    buffer.append("<RES.2>1</RES.2>");
	    buffer.append("</RES>");
	    return buffer.toString();
	  }
	/**
	 * 返回对应值消息
	 * @param RtnStr
	 * @return
	 */
	  public static final String RtnStrValue(String RtnStr) {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
	    buffer.append("<RES>");
	    buffer.append("<DAT>" + RtnStr + "</DAT>");
	    buffer.append("</RES>");
	    return buffer.toString();
	  }
	  /**
	   * 失败消息
	   * @return
	   */
	  public static final String RtnStrFail() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
	    buffer.append("<RES>");
	    buffer.append("<RES.2>0</RES.2>");
	    buffer.append("</RES>");
	    return buffer.toString();
	  }
	  /**
	   * 返回错误消息
	   * @param e
	   * @return
	   */
	  public static final String RtnExcFail(Exception e) {
		    StringBuffer buffer = new StringBuffer();
		    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		    buffer.append("<RES>");
		    buffer.append("<RES.2>0</RES.2>");
		    buffer.append("<RES.3>"+e.getMessage()+"</RES.3>");
		    buffer.append("</RES>");
		    return buffer.toString();
		 }
}
