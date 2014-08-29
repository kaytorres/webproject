package sjsms.service;


public interface SjSms {
	public ReResult login(String appid,String captcha);
	public ReResult loginOut(String appid,String sessionid);
	//public ReResult recvSMS(String appid,String session);
	public ReResult checkSMS(String smsid,String appid,String session);
	public ReResult sendSMS(String appid,String sender,String mobiles,String content,String sessionid);
	public ReResult MT(String Username,String Password,String Mobile,String Content,String Sender,String receiverinfo);
	public ReResult pushSms(String receiver,String pswd,String mobile,String content,String motime);
	public ReResult pushSmsStatus(String receiver,String pswd,String msgid,String rpttime,String rptcode,String mobile,String status);
	/*public ReResult test(String mobile,String receiver,String content);*/
}
