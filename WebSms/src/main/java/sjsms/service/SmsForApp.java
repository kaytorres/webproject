package sjsms.service;

import java.io.UnsupportedEncodingException;

import sjsms.DBservice.AccountInfo;
import sjsms.DBservice.ApplicationInfo;
import sjsms.DBservice.Sms;
import sjsms.DBservice.SmsSend;


public class SmsForApp {
	private SMSPreprocessing smsPreprocessing;
	
	public void setSmsPreprocessing(SMSPreprocessing smsPreprocessing) {
		this.smsPreprocessing = smsPreprocessing;
	}

	public SmsForApp(){
		
	}
	
	public static String backmainid;
	public ReResult preprocess(SmsSend smsSend){
		ReResult reResult=new ReResult();
		//SMSPreprocessing sMSPreprocessing=new SMSPreprocessing();
		if(smsPreprocessing.isSensitive(smsSend)){
			reResult.setReturncode("105");
			reResult.setReturndata("content is sensitive");
		}/*else if(!smsPreprocessing.canSending()){                //@8.25取消余额不足不能发短信的判断
			reResult.setReturncode("103");
			reResult.setReturndata("insufficient balance");
		}*/else if(smsPreprocessing.isFrequently(smsSend)) {             //短时间相同号码发送
			reResult.setReturncode("0");
			reResult.setReturndata("is Frequently");
		}
		else {
			try {
				smsPreprocessing.putToQueue(smsSend);
				reResult.setReturncode("0");
				reResult.setReturndata(backmainid);	
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reResult;
	}
}
