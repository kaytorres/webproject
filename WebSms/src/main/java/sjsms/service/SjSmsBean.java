package sjsms.service;

import java.io.UnsupportedEncodingException;


public class SjSmsBean implements SjSms {
	private Business business = null;
	
	public void setBusiness(Business business) {
			this.business = business;
		}
	
	public ReResult login(String appid,String captcha){
		ReResult result =new ReResult();
		result=business.login(appid, captcha);
		return result;
	}
	
	public ReResult loginOut(String appid,String sessionid){
		ReResult result =new ReResult();
		result=business.loginOut(appid, sessionid);
		return result;
	}
	
	/*public ReResult recvSMS(String appid,String session){
		ReResult result =new ReResult();
		result=business.recvSMS(appid, session);
		return result;
	}*/
	
	public ReResult checkSMS(String smsid,String appid,String session){
		ReResult result =new ReResult();
		result=business.checkSMS(smsid, appid, session);
		return result;
	}
	
	public ReResult sendSMS(String appid,String sender,String mobiles,String content,String sessionid){
		//test 
		ReResult result =new ReResult();
		try {
			result = business.sendSMS(appid,sender,mobiles, content, sessionid);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public ReResult MT(String Username,String Password,String Mobile,String Content,String Sender,String receiverinfo){
		ReResult result =new ReResult();
		result=business.MT(Username, Password, Mobile, Content,Sender,receiverinfo);
		return result;
	}
	public ReResult pushSms(String receiver,String pswd,String mobile,String content,String motime){
		ReResult result =new ReResult();
		result=business.pushSms(receiver, pswd, mobile, content, motime);
		return result;
	}
	public ReResult pushSmsStatus(String receiver,String pswd,String msgid,String rpttime,String rptcode,String mobile,String status){
		ReResult result =new ReResult();
		result=business.pushSmsStatus(receiver, pswd, msgid, rpttime, rptcode, mobile, status);
		return result;
	}
/*	public ReResult test(String mobile,String receiver,String content){
		ReResult result =new ReResult();
		result=business.test(mobile, receiver, content);
		return result;
	}*/

}
