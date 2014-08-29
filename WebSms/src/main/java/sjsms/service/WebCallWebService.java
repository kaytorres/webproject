package sjsms.service;

import java.io.UnsupportedEncodingException;

public class WebCallWebService {
	public WebCallWebService(){
		
	}
	private Business business;
	
	public void setBusiness(Business business) {
		this.business = business;
	}
	public String getSessionID(String appid,String captcha){
		ReResult reResult=business.login(appid, captcha);
		String sessionID="";
		if(reResult.getReturncode().equals("0")){
			sessionID=reResult.getReturndata();
		}
		return sessionID;
	}
	
	public boolean loginOut(String appid,String sessionID){
		ReResult reResult=business.loginOut(appid, sessionID);
		if(reResult.getReturncode().equals("0")){
			return true;
		}else {
			return false;
		}
	}
	public boolean send(String appID,String sender,String mobiles,String content,String sessionID) throws UnsupportedEncodingException{
		ReResult reResult=business.sendSMS(appID, sender, mobiles, content, sessionID);
		if(reResult.getReturncode().equals("0")){
			return true;
		}else{
			return false;
		}		
	}
}
