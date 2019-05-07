package DataService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import Util.NameUtil;
import dataBase.JdbcBuilder;

public class IserviceImpl extends JdbcBuilder implements Iservice{
	@SuppressWarnings("finally")
	public String Excute(String content) {
		String str = "";
		BASE64Encoder encode = new BASE64Encoder();
		BASE64Decoder decode = new BASE64Decoder();
		try {
			String param = new String(decode.decodeBuffer(content));
			Document doc = DocumentHelper.parseText(param);
			//验证是否有权限权限操作
			Element root = doc.getRootElement();
			if(!chackQX(root)){
				throw new Exception("无权限访问改后台");
			}
			Element msh = root.element("MSH");
		    String state = msh.element("MSH.2").getText();
		    Element data = msh.element("DATAS");
		    String SQL = data.attributeValue("SQL");
		    if(state==null||SQL==null||"".equals(state)||"".equals(SQL)){
		    	throw new Exception("未传入完整数据");
		    }
		    @SuppressWarnings("unchecked")
			List<Element> datas = data.elements("DATA");
		    ArrayList<String> Cvalue  = null;
		    if(datas != null&&datas.size()>0){
		    	Cvalue = new ArrayList<String>();
		    	for(Element val :datas){
		    		String Values = val.attributeValue("Value");
		    		Cvalue.add(Values);
		    	}
		    }
		    switch (state) {
			case "modify":
				str = String.valueOf(this.ExecModel(SQL, Cvalue));
				str = NameUtil.RtnStrValue(str);
				break;
			case "quer":
				str = this.ExecQryXml(SQL, Cvalue).asXML();
				break;
			default:
				break;
			}
		} catch (DocumentException e) {
			str = NameUtil.RtnExcFail(e);
		}finally{
			try {
				this.ReturnConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			str = encode.encode(str.getBytes());
			return str;
		}
	}
	private boolean chackQX(Element root) throws SQLException{
		Element CZY = root.element("CZY");
		String UserName = CZY.attributeValue("UserName");
		if(UserName == null||"".equals(UserName)){
			return false;
		}else{
			List<String> list = new ArrayList<String>();
			list.add(UserName);
			ResultSet rs = this.ExecQry("SELECT * FROM BASEMENT..TBUSER WHERE VUSER = ?",list);
			if(rs.next()){
				rs.close();
				return true;
			}else{
				rs.close();
				return false;
			}
			
		}
	}
	@SuppressWarnings("finally")
	@Override
	public String Login(String content){
		String str = "";
		BASE64Encoder encode = new BASE64Encoder();
		BASE64Decoder decode = new BASE64Decoder();
		try {
			String param = new String(decode.decodeBuffer(content));
			Document doc = DocumentHelper.parseText(param);
			Element root = doc.getRootElement();
			Element data = root.element("DATA");
			String UserName = data.attributeValue("UserName");
			String SysNo = data.attributeValue("SysNo");
			if(UserName == null||SysNo == null||"".equals(UserName)||"".equals(SysNo)){
				throw new Exception("未传入完整数据");
			}
			String QSQL = "SELECT * FROM BASEMENT..TBUSER CZY WITH(NOLOCK) LEFT JOIN BASEMENT..TBGROUP CZYZ WITH(nolock) ON CZY.VascNum=CZYZ.VascNum LEFt JOIN  "
					+ "BASEMENT..TBLEVEL JB WITH(NOLOCK) ON CZY.VLEVEL=JB.VLEBEL WHERE JB.BENABLE =1 AND CZY.VUSER = ? AND CZYZ.VQXBM like ?" ;
			ArrayList<String> Cvalue = new ArrayList<String>();
			Cvalue.add(UserName);
			Cvalue.add("%"+SysNo+"%");
			str = this.ExecQryXml(QSQL, Cvalue).asXML();
		} catch (Exception e) {
			e.printStackTrace();
			str = NameUtil.RtnExcFail(e);
		}finally{
			try {
				this.ReturnConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			str = encode.encode(str.getBytes());
			return str;
		}
	}

}
