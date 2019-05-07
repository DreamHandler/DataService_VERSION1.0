package DataService;

import java.util.ArrayList;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import Util.NameUtil;
import dataBase.JdbcBuilder;

@javax.jws.WebService(targetNamespace = "http://DataService/", serviceName = "IserviceImplService", portName = "IserviceImplPort", wsdlLocation = "WEB-INF/wsdl/IserviceImplService.wsdl")

public class IserviceImplDelegate {

	DataService.IserviceImpl iserviceImpl = new DataService.IserviceImpl();

	public String Excute(String content) {
		return iserviceImpl.Excute(content);
	}

	public String Login(String content) {
		return iserviceImpl.Login(content);
	}

}