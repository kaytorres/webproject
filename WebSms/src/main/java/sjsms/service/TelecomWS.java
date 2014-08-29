package sjsms.service;

import _81._70._129._61.GeneralWs.services.DkfServices.*;

import java.awt.RenderingHints.Key;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import com.sun.tools.ws.processor.model.Message;

import sjsms.DBservice.Sms;
import sjsms.DBservice.SmsMT;
import sjsms.DBservice.AccountInfo;

public class TelecomWS {
	// static Encrypt e = new Encrypt();
/*	private DkfServicesProxy dkfServicesProxy;
	public void setDkfServicesProxy(DkfServicesProxy dkfServicesProxy){
		this.dkfServicesProxy=dkfServicesProxy;
	}*/
	
	private SmsManager smsManager;
	public void setSmsManager(SmsManager smsManager){
		this.smsManager=smsManager;
	}

	public ReResult sendSMS(Sms sms) throws RemoteException, UnsupportedEncodingException {
		//SmsManager smsManager = new SmsManager();
		DkfServicesProxy df = new DkfServicesProxy();
		ReResult reResult=new ReResult();
		if (!sms.getUser().equals("") && !sms.getUser().equals(null)
				&& !sms.getPassword().equals("")
				&& !sms.getPassword().equals(null)
				&& !sms.getMobiles().equals("")
				&& !sms.getMobiles().equals(null)
				&& !sms.getContent().equals("")
				&& !sms.getContent().equals(null)) {
			Sms message = smsManager.encrptSms(sms);
			String[] strings=new String[message.getMobiles().length];
			for(int i=0;i<message.getMobiles().length;i++){
				strings[i]=message.getMobiles()[i];
			}
			/*String result = dkfServicesProxy.sendSMS(message.getUser(), message.getPassword(),
			message.getMobiles(), message.getContent(), "", "");*/
			String result = df.sendSMS(message.getUser(), message.getPassword(),
					message.getMobiles(), message.getContent(), "", "");
			reResult = reSend(result);
		}else {
			reResult.setReturncode("200");
			reResult.setReturndata("");
		}
		return reResult;
	}


	 public ReResult getMT(SmsMT messMt) throws RemoteException, UnsupportedEncodingException {
		// SmsManager smsManager = new SmsManager();
		 ReResult reresult = new ReResult();
	     if (!messMt.getUser().equals("") &&!messMt.getUser().equals(null)
	    		 &&!messMt.getPassword().equals("")
	    		 &&!messMt.getPassword().equals(null)
	    		 &&!messMt.getStartTime().equals("")
	    		 &&!messMt.getStartTime().equals(null) 
	    		 &&!messMt.getEndTime().equals("")
	    		 &&!messMt.getEndTime().equals(null)) {
		    DkfServicesProxy df = new DkfServicesProxy();
	    	 SmsMT message =smsManager.encrptQuery(messMt);
	    	 String result = df.getMT(message.getUser(), message.getPassword(),
	    			  message.getStartTime(), message.getEndTime(), message.getMobile(),
	    			  message.getMsgid()); 
	    	 reresult=reQuery(result);
	    	 }else {
	    		 reresult.setReturncode("200");
	    		 reresult.setReturndata("");
	    		 }
	     return reresult;
	     }
	 

	public ReResult getUserSmsCount(AccountInfo accoutinfo) throws RemoteException, UnsupportedEncodingException {
		ReResult reResult=new ReResult();
		//SmsManager smsManager = new SmsManager();
		
		if(!accoutinfo.getUsername().equals("")&&!accoutinfo.getUsername().equals(null)&&!accoutinfo.getPassword().equals("")&&!accoutinfo.getPassword().equals(null)){
			DkfServicesProxy df = new DkfServicesProxy();
			AccountInfo reaccoutinfo= smsManager.encrptAccout(accoutinfo);
			String result = df.getUserSmsCount(reaccoutinfo.getUsername(),
					reaccoutinfo.getPassword());
			reResult=reBalance(result);
		}else{
			reResult.setReturncode("200");
			reResult.setReturndata("");
		}
		
		
		return reResult;
	}

	public ReResult reSend(String result) {
		ReResult reresult = new ReResult();
		if (!result.equals("") && !result.equals(null)) {
			String[] str = result.split(",");
			if (str.length == 2) {
				reresult.setReturncode(str[1]);
				reresult.setReturndata(str[0]);
			}
			if (str.length == 3) {
				reresult.setReturncode(str[1]);
				reresult.setReturndata(str[0] + "," + str[2]);
			}
		}
		return reresult;
	}
	

	public ReResult reQuery(String result){
		ReResult reresult = new ReResult();
		if (!result.equals("") && !result.equals(null)) {
			String[] str = result.split(";");
			if (str.length == 1) {
				reresult.setReturncode(str[0]);
				reresult.setReturndata("");
			}
			if (str.length > 1) {
				reresult.setReturncode(str[0]);
				String data="";
				for(int i=1;i<str.length;i++){
					data=data.concat(str[i]+";");
				}
				reresult.setReturndata(data);
			}
		}
		return reresult;
	}
	

	public ReResult reBalance(String result) {
		ReResult reresult = new ReResult();
		if (!result.equals("") && !result.equals(null)) {
			String[] str = result.split(";");
			if (str.length == 1) {
				reresult.setReturncode(str[0]);
				reresult.setReturndata("");
			}
			if (str.length == 2) {
				reresult.setReturncode(str[0]);
				reresult.setReturndata(str[1]);
			}
		}
		return reresult;
	}
	
	public ReResult rePush(String result){
		ReResult reResult=new ReResult();
		if(result.equals("200k")){
			reResult.setReturncode("0");    //接收成功
			reResult.setReturndata("");
		}else {
			reResult.setReturncode("1");    //接收失败
			reResult.setReturndata("");
		}
		return reResult;
	}

	public static void main(String arg[]) throws RemoteException, UnsupportedEncodingException{
		
	/*	Sms s = new Sms();
		s.setUser("12001");
		s.setPassword("gvGFmF");
		s.setMobiles("15601788266");
		s.setContent("test");
		s.setPlantime("");
		s.setFilename("");*/
//
		/*SmsMT smt=new SmsMT();
		smt.setUser("12001");
		smt.setPassword("gvGFmF");
     	smt.setStartTime("201407221720");
		smt.setEndTime("201407221818");
		smt.setMobile("");
		smt.setMsgid("");*/
		//DkfServicesProxy df = new DkfServicesProxy();
	//	AccountInfo acc=new AccountInfo();
	//	acc.setUsername("12001");
	//	acc.setPassword("gvGFmF");
		//acc.setName("test");
		
		//Object[] reString=df.getMO("12001","gvGFmF");
		DkfServicesProxy df = new DkfServicesProxy();
		String[] mobileStrings = new String[] { "17a66230c467d8c3095bb0ddd7c203eb" };
		String reString=df.sendSMS("7a7b574d1b7d9f4a", "85125f8084806495", mobileStrings, "6661a0fd3f58db3c0be55c962d4c614f4c30348a2375576a", "", "");
		System.err.println(reString);
/*		String[] s=reString.split(",");
		if(s.length>1){
			System.err.println(s[1]);
		}*/
			/*SmsManager smsManager = new SmsManager();
			Sms reSms=smsManager.encrptSms(s);*/
	//	TelecomWS ts=new TelecomWS();
			//ReResult re = ts.sendSMS(s);
	//		ReResult re =ts.getMT(smt);
		//	ReResult re=ts.getUserSmsCount(acc);
			// code = ts.getUserSmsCount(s);
	//	    System.err.println(re.getReturncode());
		//	System.err.println(re.getReturndata());
//			System.err.println(reSms.getUser());
//			System.err.println(reSms.getPassword());
//			System.err.println(reSms.getMobiles());
//			System.err.println(reSms.getContent());
//			System.err.println(reSms.getPlantime());
//			System.err.println(reSms.getFilename());
//	

	}
}
