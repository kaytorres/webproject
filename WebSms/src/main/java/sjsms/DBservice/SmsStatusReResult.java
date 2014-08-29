package sjsms.DBservice;

import sjsms.service.ReResult;

public class SmsStatusReResult {
	private SmsSend smsSend;
	private ReResult reResult;
	public SmsStatusReResult(){
		
	}
	public SmsStatusReResult(SmsSend smsSend,ReResult reResult){
		super();
		this.smsSend=smsSend;
		this.reResult=reResult;
	}
	public SmsSend getSmsSend() {
		return smsSend;
	}
	public void setSmsSend(SmsSend smsSend) {
		this.smsSend = smsSend;
	}
	public ReResult getReResult() {
		return reResult;
	}
	public void setReResult(ReResult reResult) {
		this.reResult = reResult;
	}
	

}
