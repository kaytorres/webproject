package sjsms.service;


import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import sjsms.DBservice.AccountInfo;
import sjsms.DBservice.AccountInfoManager;
import sjsms.DBservice.Sms;
import sjsms.DBservice.SmsSend;
import sjsms.DBservice.SmsSendManager;
import sjsms.DBservice.SmsSendStatus;
import sjsms.DBservice.SmsSendStatusManager;
import sjsms.DBservice.SmsStatusReResult;

public class SMSSending  implements Runnable{
	
	private SmsSendStatusManager smsSendStatusManager;
	private AccountInfoManager accountInfoManager;
//	private SMSPreprocessing smsPreprocessing;
	private SmsSendManager smsSendManager;
	private TelecomWS telecomWS;
	private SmsManager smsManager;
//	private static Queue<Object> mainidQueue;
	public void setSmsSendStatusManager(SmsSendStatusManager smsSendStatusManager){
		this.smsSendStatusManager=smsSendStatusManager;
	}

	public void setAccountInfoManager(AccountInfoManager accountInfoManager){
		this.accountInfoManager=accountInfoManager;
	}

/*	public void setSmsPreprocessing(SMSPreprocessing smsPreprocessing) {
		this.smsPreprocessing = smsPreprocessing;
	}*/

	public void setSmsSendManager(SmsSendManager smsSendManager) {
		this.smsSendManager = smsSendManager;
	}
	
	public void setTelecomWS(TelecomWS telecomWS){
		this.telecomWS=telecomWS;
	}
	private CreateSyslog createSyslog;
	/*private DkfServicesProxy dkfServicesProxy;
	public void setDkfServicesProxy(DkfServicesProxy dkfServicesProxy){
		this.dkfServicesProxy=dkfServicesProxy;
	}*/
	
	public void setSmsManager(SmsManager smsManager){
		this.smsManager=smsManager;
	}

	public ReResult sendSMS(Sms sms){
		//TelecomWS telecomWS= new TelecomWS();
		ReResult reResult=new ReResult();
		try {
			telecomWS.setSmsManager(smsManager);
			
			reResult=telecomWS.sendSMS(sms);
/*			if(!reResult.getReturncode().equals("200")){
				saveSendResultToDb(sms,reResult);
			}*/
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reResult;
	}
	
	public void getFromQueue(int mainid){
		//int mainid=Integer.parseInt(smsPreprocessing.getQueue().toString());
		//System.out.println("----------------------smsSendManager------------------------"+smsSendManager);
		//System.out.println("----------------------accountInfoManager------------------------"+accountInfoManager);
		List<SmsSend> lstsSmsSends=smsSendManager.querySmsSend(mainid);
		SmsSend smsSend=lstsSmsSends.get(0);
		
		createSyslog.createSyslog("系统", "09","主键ID："+String.valueOf(mainid));
		SmsSendStatus sendStatus=new SmsSendStatus();
		sendStatus.setMainid(mainid);
		List<AccountInfo> lstaAccountInfos=accountInfoManager.queryAccount();
		int index =Integer.parseInt(smsSend.getSmstype());
		ReResult reResult =new ReResult();
		String[] str=smsSend.getMobile().split(",");
		String[] mobiles=new String[str.length];
		for(int i=0;i<str.length;i++){
			mobiles[i]=str[i];
		}
		int count;
		if(smsSend.getContent().length()%500==0){
			count=smsSend.getContent().length()/500;
		}else {
			count=smsSend.getContent().length()/500+1;
		}
		for(int j=0;j<count;j++){
			if(j==count-1){
				Sms sms = new Sms();
				sms.setUser(lstaAccountInfos.get(index).getUsername());
				sms.setPassword(lstaAccountInfos.get(index).getPassword());
				sms.setMobiles(mobiles);
				sms.setContent(smsSend.getContent().substring(j*500, smsSend.getContent().length()));
				//sms.setContent(smsSend.getContent());
				int sendtimes=0;
				while(sendtimes<2){
					reResult =sendSMS(sms);
					if(!reResult.getReturncode().equals("104")&&!reResult.getReturncode().equals("107")&&!reResult.getReturncode().equals("109")){
						break;
					}
					sendtimes++;
				}
				
			/*	String[] smsidString=reResult.getReturndata().split(",");
				SmsForApp.smsid=smsidString[1];	*/								//test
				saveSendResultToDb(smsSend,reResult);
			}else {
				Sms sms = new Sms();
				sms.setUser(lstaAccountInfos.get(index).getUsername());
				sms.setPassword(lstaAccountInfos.get(index).getPassword());
				sms.setMobiles(mobiles);
				sms.setContent(smsSend.getContent().substring(j*500, (j+1)*500));
				//sms.setContent(smsSend.getContent());
				int sendtimes=0;
				while(sendtimes<2){
					reResult =sendSMS(sms);
					if(!reResult.getReturncode().equals("104")&&!reResult.getReturncode().equals("107")&&!reResult.getReturncode().equals("109")){
						break;
					}
					sendtimes++;
				}
				/*String[] smsidString=reResult.getReturndata().split(",");
				SmsForApp.smsid=smsidString[1];           */                   //test
				saveSendResultToDb(smsSend,reResult);
			}
		}
	}
	
	public static BlockingQueue<Object> smsStatusReResultQueue=new LinkedBlockingQueue<Object>();
	public synchronized BlockingQueue<Object> BlockingQueue(){
		return smsStatusReResultQueue;
	}
	public void saveSendResultToDb(SmsSend smsSend,ReResult reResult){
		/*SmsSend smsToDb=new SmsSend();
		smsToDb.setId(smsSend.getId());
		if(reResult.getReturncode().equals("0")){
			smsToDb.setSendingstatus(reResult.getReturncode());
			String[] strings=reResult.getReturndata().split(",");
			smsToDb.setSmsid(strings[1]);
			smsSendManager.updateSucSmsStatus(smsToDb);
	
			String[] mobile = smsSend.getMobile().split(",");
			if (mobile.length > 0) {
				for (int i = 0; i < mobile.length; i++) {
					SmsSendStatus sendStatus = new SmsSendStatus();
					sendStatus.setMainid(smsSend.getId());
					sendStatus.setMobile(mobile[i]);
					sendStatus.setSmsid(strings[1]);
					smsSendStatusManager.updateSendStatusSuc(sendStatus);
				}
			}
			
		}else {
			smsToDb.setSendingstatus(reResult.getReturncode());
			//smsToDb.setSendingtime(reResult.getReturndata());
			smsSendManager.updateFailSmsStatus(smsToDb);
			
			String[] mobile = smsSend.getMobile().split(",");
			if (mobile.length > 0) {
				for (int i = 0; i < mobile.length; i++) {
					SmsSendStatus sendStatus = new SmsSendStatus();
					sendStatus.setMainid(smsSend.getId());
					sendStatus.setMobile(mobile[i]);
					sendStatus.setSendstatus("1");
					smsSendStatusManager.updateSendStatusFail(sendStatus); // test code
				}
			}
		}*/
		SmsStatusReResult smsStatusReResult=new SmsStatusReResult();
		smsStatusReResult.setSmsSend(smsSend);
		smsStatusReResult.setReResult(reResult);
		smsStatusReResultQueue.add(smsStatusReResult);
	}
	
	private  boolean started=false;
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	private SMSSending(){
		started = false;
	}
	private static	SMSSending ssmsSending=null;
	public  static SMSSending getInstance() {
	       if (ssmsSending == null) {   
	    	   synchronized(SMSSending.class){
	    		   if (ssmsSending == null) {
	    			   ssmsSending = new SMSSending();
	    		   }
	    	   }
	       }
	       return ssmsSending;
	}

	public void run(){
        try {
			while(true)
			{
				int mainid=Integer.parseInt(SMSPreprocessing.smsQueue.take().toString());
				try{
					getFromQueue(mainid);
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}

	public CreateSyslog getCreateSyslog() {
		return createSyslog;
	}

	public void setCreateSyslog(CreateSyslog createSyslog) {
		this.createSyslog = createSyslog;
	}

}
