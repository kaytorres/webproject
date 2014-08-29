package sjsms.service;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.xml.resolver.apps.resolver;



import sjsms.DBservice.AccountInfo;
import sjsms.DBservice.AccountInfoManager;
import sjsms.DBservice.SmsMT;
import sjsms.DBservice.SmsSendStatus;
import sjsms.DBservice.SmsSendStatusManager;

public class SmsStatusChecking implements Runnable{
	private SmsSendStatusManager smsSendStatusManager;
	private AccountInfoManager accountInfoManager;
	private TelecomWS telecomWS;
	private SmsManager smsManager;
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}

	public void setSmsSendStatusManager(SmsSendStatusManager smsSendStatusManager) {
		this.smsSendStatusManager = smsSendStatusManager;
	}

	public void setAccountInfoManager(AccountInfoManager accountInfoManager) {
		this.accountInfoManager = accountInfoManager;
	}
	public void setTelecomWS(TelecomWS telecomWS){
		this.telecomWS=telecomWS;
	}

/*	public SmsStatusChecking(){

	}*/
	

	public void queryBalance(){
		List<AccountInfo> lst=accountInfoManager.queryAccount();
		if(lst.size()>0){
			for(int i=0;i<lst.size();i++){
				AccountInfo accountInfo=new AccountInfo();
				accountInfo.setName(lst.get(i).getName());
				accountInfo.setUsername(lst.get(i).getUsername());
				accountInfo.setPassword(lst.get(i).getPassword());		
				try {
					telecomWS.setSmsManager(smsManager);
					ReResult reQuery=telecomWS.getUserSmsCount(accountInfo);
					//System.out.println("----------------"+accountInfo.getUsername()+"-----------------------"+reQuery.getReturndata());
					//System.out.println("----------------"+reQuery.getReturncode()+"-----------------------"+reQuery.getReturndata());
					if(reQuery.getReturncode().equals("0")){
						float balance = Float.parseFloat(reQuery.getReturndata());
						accountInfo.setBalance(balance);
						accountInfoManager.updateBanlance(accountInfo);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	

	public ReResult querySmsMt(){
		ReResult reResult=new ReResult();
		List<AccountInfo> lstaAccountInfos=accountInfoManager.queryAccount();
		if(lstaAccountInfos.size()>0){
			SmsMT smsMt=new SmsMT();
			smsMt.setUser(lstaAccountInfos.get(0).getUsername());
			smsMt.setPassword(lstaAccountInfos.get(0).getPassword());
	
			Calendar calnow = Calendar.getInstance ();
		    String endTime =  new  SimpleDateFormat( "yyyyMMddHHmm" ).format(calnow.getTime());		
			Calendar calago=Calendar.getInstance ();
			calago.set(calago.HOUR, calnow.get(calnow.HOUR)-1);
			String startTime =  new  SimpleDateFormat( "yyyyMMddHHmm" ).format(calago.getTime());
			
			smsMt.setStartTime(startTime);
			smsMt.setEndTime(endTime);
			
			//TelecomWS telecomWS=new TelecomWS();	
			try {
				telecomWS.setSmsManager(smsManager);
				reResult=telecomWS.getMT(smsMt);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reResult;
	}

	public void updateStatusToDb(ReResult reResult){
		if(reResult.getReturncode().equals("0")&&!reResult.getReturndata().equals("")){
			String[] reStrings=reResult.getReturndata().split(";");
			if(reStrings.length>0){
				for(int i=0;i<reStrings.length;i++){
					String oneReString=reStrings[i];
					String[] oneRestatus=oneReString.split(",");
					SmsSendStatus smsSendStatus=new SmsSendStatus();
					smsSendStatus.setSmsid(oneRestatus[0]);
					smsSendStatus.setMobile(oneRestatus[1]);
					smsSendStatus.setSendstatus(oneRestatus[2]);
					smsSendStatusManager.updateSendStatus(smsSendStatus);
				}
			}
		}
	}

	private static BlockingQueue<Object> blocking=new LinkedBlockingQueue<Object>();
	public synchronized static BlockingQueue<Object> getBlocking(){
		return blocking;
	}
	
	public void taskStatus(){
		blocking.add("status");
	}
	
	public void taskBalance(){
		blocking.add("balance");
	}
	
	private  boolean started=false;
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	private SmsStatusChecking(){
		started = false;
	}
	private static	SmsStatusChecking sChecking=null;
	public  static SmsStatusChecking getInstance() {
	       if (sChecking == null) {   
	    	   synchronized(SmsStatusChecking.class){
	    		   if (sChecking == null) {
	    			   sChecking = new SmsStatusChecking();
	    		   }
	    	   }
	       }
	       return sChecking;
	}

	public void run(){
        try {
			while(true)
			{
				
				String str=blocking.take().toString();
				if(str.equals("status")){
					updateStatusToDb(querySmsMt());
				}
				if(str.equals("balance")){
					queryBalance();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
	}


}
