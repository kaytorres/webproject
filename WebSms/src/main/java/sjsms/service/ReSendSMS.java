package sjsms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sjsms.DBservice.SmsSend;
import sjsms.DBservice.SmsSendManager;
import sjsms.DBservice.SmsSendStatusManager;



public class ReSendSMS implements Runnable{
	private SmsSendManager sendManager;
	private  CreateSyslog createSyslog;
	private SmsSendStatusManager sendStatusManager;
	public CreateSyslog getCreateSyslog() {
		return createSyslog;
	}

	public void setCreateSyslog(CreateSyslog createSyslog) {
		this.createSyslog = createSyslog;
	}

	public static ReSendSMS getReSendSMS() {
		return reSendSMS;
	}

	public static void setReSendSMS(ReSendSMS reSendSMS) {
		ReSendSMS.reSendSMS = reSendSMS;
	}
	private  boolean started=false;
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	private ReSendSMS(){
		started = false;
	}
	private static	ReSendSMS reSendSMS=null;
	public  static ReSendSMS getInstance() {
	       if (reSendSMS == null) {   
	    	   synchronized(ReSendSMS.class){
	    		   if (reSendSMS == null) {
	    			   reSendSMS = new ReSendSMS();
	    		   }
	    	   }
	       }
	       return reSendSMS;
	}

	public void run(){
        while(true)
		{
			if(SMSPreprocessing.smsQueue.size()==0){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now =new Date();
				Date d1;
				try {
					d1 = df.parse(Business.systemTime);
					long diff = (now.getTime()-d1.getTime())/1000;
					List<SmsSend> lst=null;
					if(diff>120){
						lst=sendManager.reQuerySmsSend();
						if(lst.size()>0){
							for(int i=0;i<lst.size();i++){
								
								createSyslog.createSyslog("系统", "09",String.valueOf(lst.get(i).getId()));
									SMSPreprocessing.smsQueue.add(lst.get(i).getId());
									sendStatusManager.ReSendStatus(lst.get(i).getId());   //@8.28
								
							}
						}
						Business.systemTime=df.format(now);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
       
	}

	public SmsSendManager getSendManager() {
		return sendManager;
	}

	public void setSendManager(SmsSendManager sendManager) {
		this.sendManager = sendManager;
	}

	public SmsSendStatusManager getSendStatusManager() {
		return sendStatusManager;
	}

	public void setSendStatusManager(SmsSendStatusManager sendStatusManager) {
		this.sendStatusManager = sendStatusManager;
	}
}
