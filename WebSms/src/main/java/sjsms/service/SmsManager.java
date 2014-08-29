package sjsms.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import sjsms.DBservice.*;
public class SmsManager {
	
/*	public String sendSMS(Sms message){
		
		return null;
	}*/

	public Sms encrptSms(Sms sms){
		Sms resms=new Sms();
		Encrypt encrypt=new Encrypt();
		try {
			resms.setUser(encrypt.encrypt(sms.getUser()));
			resms.setPassword(encrypt.encrypt(sms.getPassword()));
			String[] str=new String[sms.getMobiles().length];
			String[] mobiles=sms.getMobiles();
			for(int i=0;i<sms.getMobiles().length;i++){
				str[i]=encrypt.encrypt(mobiles[i]);
			}

			resms.setMobiles(str);
		//	String Content=URLEncoder.encode(sms.getContent(), "utf-8");
			resms.setContent(encrypt.encrypt(sms.getContent()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resms.setPlantime(sms.getPlantime());
		resms.setFilename(sms.getFilename());
		return resms;
	}
	
	public SmsMT encrptQuery(SmsMT smsmt){
		SmsMT resmsmt=new SmsMT();
		Encrypt encrypt=new Encrypt();
		try {
			resmsmt.setUser(encrypt.encrypt(smsmt.getUser()));
			resmsmt.setPassword(encrypt.encrypt(smsmt.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resmsmt.setStartTime(smsmt.getStartTime());
		resmsmt.setEndTime(smsmt.getEndTime());
		resmsmt.setMobile(smsmt.getMobile());
		resmsmt.setMsgid(smsmt.getMsgid());
		return resmsmt;
	}
	
	public AccountInfo encrptAccout(AccountInfo accoutinfo){
		AccountInfo reaccoutinfo=new AccountInfo();
		Encrypt encrypt=new Encrypt();
		try {
			reaccoutinfo.setUsername(encrypt.encrypt(accoutinfo.getUsername()));
			reaccoutinfo.setPassword(encrypt.encrypt(accoutinfo.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reaccoutinfo;
	}
}
