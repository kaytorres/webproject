package sjsms.service;

import sjsms.DBservice.SmsSend;
import sjsms.DBservice.SmsSendManager;
import sjsms.DBservice.SmsSendStatus;
import sjsms.DBservice.SmsSendStatusManager;
import sjsms.DBservice.SmsStatusReResult;

public class SendStatusToDB implements Runnable{
	
	private SmsSendManager smsSendManager;
	private SmsSendStatusManager smsSendStatusManager;
	public void setSmsSendManager(SmsSendManager smsSendManager) {
		this.smsSendManager = smsSendManager;
	}

	public void setSmsSendStatusManager(SmsSendStatusManager smsSendStatusManager) {
		this.smsSendStatusManager = smsSendStatusManager;
	}

	


	public void updateSendResultToDb(SmsSend smsSend,ReResult reResult){
		SmsSend smsToDb=new SmsSend();
		smsToDb.setId(smsSend.getId());
		if(reResult.getReturncode().equals("0")){
			smsToDb.setSendingstatus("03");  
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
			smsSendManager.updateFailSmsStatus(smsToDb);
			String[] mobile = smsSend.getMobile().split(",");
			if (mobile.length > 0) {
				for (int i = 0; i < mobile.length; i++) {
					SmsSendStatus sendStatus = new SmsSendStatus();
					sendStatus.setMainid(smsSend.getId());
					sendStatus.setMobile(mobile[i]);
					sendStatus.setSendstatus("1");        
					smsSendStatusManager.updateSendStatusFail(sendStatus);
				}
			}
		}
	}
	
	
	private  boolean started=false;
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	private SendStatusToDB(){
		started = false;
	}
	private static	SendStatusToDB ssendStatusToDB=null;
	public  static SendStatusToDB getInstance() {
	       if (ssendStatusToDB == null) {   
	    	   synchronized(SendStatusToDB.class){
	    		   if (ssendStatusToDB == null) {
	    			   ssendStatusToDB = new SendStatusToDB();
	    		   }
	    	   }
	       }
	       return ssendStatusToDB;
	}

	public void run(){
        try {
			while(true)
			{
				SmsStatusReResult smsStatusReResult=(SmsStatusReResult) SMSSending.smsStatusReResultQueue.take();
				try{
					updateSendResultToDb(smsStatusReResult.getSmsSend(),smsStatusReResult.getReResult());
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
}
