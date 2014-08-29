package sjsms.service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JApplet;

import org.apache.bcel.generic.NEW;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import sjsms.DBservice.SensitiveWord;
import sjsms.DBservice.SensitiveWordManager;
import sjsms.DBservice.AccountInfo;
import sjsms.DBservice.AccountInfoManager;
import sjsms.DBservice.SmsSend;
import sjsms.DBservice.SmsSendManager;
import sjsms.DBservice.SmsSendStatus;
import sjsms.DBservice.SmsSendStatusManager;

public class SMSPreprocessing {
	

	private SensitiveWordManager sensitiveWordManager;
	private AccountInfoManager accountInfoManager;
	private SmsSendManager smsSendManager;
	private SmsManager smsManager;
	private TelecomWS telecomWS;
	private SmsSendStatusManager smsSendStatusManager;
	private CreateSyslog createSyslog;
	public static BlockingQueue<Object> smsQueue=new LinkedBlockingQueue<Object>();
	
	public SMSPreprocessing(){
	//	smsSending=new SMSSending();
	}
	
	public void setSensitiveWordManager(SensitiveWordManager sensitiveWordManager) {
		this.sensitiveWordManager = sensitiveWordManager;
	}

	public void setAccountInfoManager(AccountInfoManager accountInfoManager) {
		this.accountInfoManager = accountInfoManager;
	}

	public void setSmsSendManager(SmsSendManager smsSendManager) {
		this.smsSendManager = smsSendManager;
	}
	
	public synchronized BlockingQueue<Object> BlockingQueue(){
		return smsQueue;
	}
	
	
	public void setTelecomWS(TelecomWS telecomWS){
		this.telecomWS=telecomWS;
	}
/*	private DkfServicesProxy dkfServicesProxy;
	public void setDkfServicesProxy(DkfServicesProxy dkfServicesProxy){
		this.dkfServicesProxy=dkfServicesProxy;
	}*/
	
	public void setSmsManager(SmsManager smsManager){
		this.smsManager=smsManager;
	}
	

	public void setSmsSendStatusManager(SmsSendStatusManager smsSendStatusManager){
		this.smsSendStatusManager=smsSendStatusManager;
	}
	public boolean isSensitive(SmsSend smsSend){
	//	SensitiveWordManager sensitiveWordManager=new SensitiveWordManager();
		List<SensitiveWord> lst=sensitiveWordManager.queryUser();
		for(int i=0;i<lst.size();i++){
			if(smsSend.getContent().contains(lst.get(i).getWord())){
				return true;
			}
		}
		return false;
	}
	
	public boolean canSending(){
		// AccountInfoManager accountInfoManager=new  AccountInfoManager();
		List<AccountInfo> lst=accountInfoManager.queryAccount();
		boolean b=false;
		for(int i=0;i<lst.size();i++){
			if(lst.get(i).getBalance()>0){
				b= true;
			}
		}
		return b;
	}
	
	public boolean isFrequently(SmsSend smsSend){
		/*SmsSend newsmssend=new SmsSend();
		if(smsSend.getMobile().equals(null)){
			newsmssend.setMobile("");
		}
		if(smsSend.getContent().equals(null)){
			newsmssend.setContent(smsSend.getContent());
		}*/
		boolean isfrequently=false;
		List<SmsSend> lst=smsSendManager.smssendExist(smsSend);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(lst.size()>0){
			int i=0;
			while(i<lst.size()){
				try {
					Date d1 = df.parse(lst.get(i).getSendingtime());
					Date now=new java.util.Date();
					long diff = (now.getTime()-d1.getTime())/1000;
					if(diff<120){
						isfrequently= true;
						break;
					}else {
						isfrequently=false;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
		}else {
			isfrequently= false;
		}
		
		return isfrequently;
	}
	
	public void putToQueue(SmsSend smsSend) throws UnsupportedEncodingException{
		int count;
		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");	
		String longsmsid=sdf2.format(new java.util.Date());       
		List<AccountInfo> accountInfos=accountInfoManager.queryAccount();
		//String smsType=accountInfos.get(0).getName().toString();
		//test
		String smsType=String.valueOf(accountInfos.size()-1);
		if(smsSend.getContent().length()%500==0){
			count=smsSend.getContent().length()/500;
		}else {
			count=smsSend.getContent().length()/500+1;
		}
		for(int i=0;i<count;i++){
			if(i==count-1){
				SmsSend s = new SmsSend();
				s.setAppid(smsSend.getAppid());
				s.setSmstype(smsType);
				s.setSender(smsSend.getSender()); 
				s.setReceiverinfo(smsSend.getReceiverinfo());
				s.setMobile(smsSend.getMobile());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				s.setSendingtime(sdf.format(new java.util.Date()));
				/*s.setContent(new String(smsSend.getContent()
						.substring(i * 500, smsSend.getContent().length())
						.getBytes("utf-8"), "gbk"));*/
				s.setContent(smsSend.getContent().substring(i * 500, smsSend.getContent().length()));
				s.setIslongsms(smsSend.getIslongsms());
				
				s.setSendingstatus("01");            
				
				if(i>0){
					s.setLongsmsid(longsmsid);
				}
				int mainid = smsSendManager.addSmsSend(s);
				SmsForApp.backmainid=String.valueOf(mainid);
						
				smsQueue.add(mainid);
				String[] mobiles = smsSend.getMobile().split(",");
				for (int j = 0; j < mobiles.length; j++) {
					SmsSendStatus smsSendStatus = new SmsSendStatus();
					smsSendStatus.setMainid(mainid);
					smsSendStatus.setMobile(mobiles[j]);
					smsSendStatusManager.addSendStatus(smsSendStatus);
				}
			}else{
				SmsSend s = new SmsSend();
				s.setAppid(smsSend.getAppid());
				s.setSmstype(smsType);
				s.setSender(smsSend.getSender()); 
				s.setMobile(smsSend.getMobile());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				s.setSendingtime(sdf.format(new java.util.Date()));
			/*	s.setContent(new String(smsSend.getContent()
						.substring(i * 500, (i+1)*500)
						.getBytes("utf-8"), "gbk"));*/
				s.setContent(smsSend.getContent().substring(i * 500, (i+1)*500));
				s.setIslongsms(smsSend.getIslongsms());
				if(i>0){
					s.setLongsmsid(longsmsid);
				}
				s.setSendingstatus("01"); 
				int mainid = smsSendManager.addSmsSend(s);
				SmsForApp.backmainid=String.valueOf(mainid);
				
				smsQueue.add(mainid);

				String[] mobiles = smsSend.getMobile().split(",");
				for (int j = 0; j < mobiles.length; j++) {
					SmsSendStatus smsSendStatus = new SmsSendStatus();
					smsSendStatus.setMainid(mainid);
					smsSendStatus.setMobile(mobiles[j]);
					smsSendStatusManager.addSendStatus(smsSendStatus);
				}
			}
			
			}
			
			SMSSending smsSending	=SMSSending.getInstance();
			smsSending.setSmsSendManager(smsSendManager);
			smsSending.setAccountInfoManager(accountInfoManager);
			smsSending.setTelecomWS(telecomWS);
			smsSending.setSmsManager(smsManager);
			smsSending.setCreateSyslog(createSyslog);
		    smsSending.setSmsSendStatusManager(smsSendStatusManager);
				if(!smsSending.isStarted()){
					smsSending.setStarted(true);
					Thread thread=new Thread(smsSending);
					thread.start();
				}
	//	}
	}

	public CreateSyslog getCreateSyslog() {
		return createSyslog;
	}

	public void setCreateSyslog(CreateSyslog createSyslog) {
		this.createSyslog = createSyslog;
	}
}
