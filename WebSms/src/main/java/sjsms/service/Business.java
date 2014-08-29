package sjsms.service;



import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.xml.resolver.apps.resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;


import sjsms.DBservice.*;


public class Business {
	private SmsRecvManager smsRecvManager;
	private ApplicationInfoManager applicationInfoManager;
	private SmsSendManager smsSendManager;
	private CreateSyslog createSyslog;
	private SmsForApp smsForApp;
	private AccountInfoManager accountInfoManager;
	private SmsSendStatusManager smsSendStatusManager;
	private TelecomWS telecomWS;
	private SmsManager smsManager;
	private Logger logger = LoggerFactory.getLogger(Business.class);
	
	public void setSmsRecvManager(SmsRecvManager smsRecvManager) {
		this.smsRecvManager = smsRecvManager;
		
	}
	public void setApplicationInfoManager(ApplicationInfoManager applicationInfoManager) {
		this.applicationInfoManager = applicationInfoManager;
		
	}

	public void setSmsSendManager(SmsSendManager smsSendManager){
		this.smsSendManager=smsSendManager;
		
	}
	public void setSmsSendStatusManager(SmsSendStatusManager smsSendStatusManager) {
		this.smsSendStatusManager = smsSendStatusManager;
		
	}
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}
	public static String systemTime;
	public void setTelecomWS(TelecomWS telecomWS){
		this.telecomWS=telecomWS;
		SmsStatusChecking smsStatusChecking	=SmsStatusChecking.getInstance();
		smsStatusChecking.setAccountInfoManager(accountInfoManager);
		smsStatusChecking.setSmsSendStatusManager(smsSendStatusManager);
		smsStatusChecking.setTelecomWS(telecomWS);
		Encrypt.key=accountInfoManager.queryAccount().get(0).getPasswordfor3des();
		smsStatusChecking.setSmsManager(smsManager);
		if(!smsStatusChecking.isStarted()){
			smsStatusChecking.setStarted(true);
			Thread thread=new Thread(smsStatusChecking);
			thread.start();
		}
		
		SendStatusToDB sendStatusToDB=SendStatusToDB.getInstance();
		sendStatusToDB.setSmsSendManager(smsSendManager);
		sendStatusToDB.setSmsSendStatusManager(smsSendStatusManager);
		if(!sendStatusToDB.isStarted()){
			sendStatusToDB.setStarted(true);
			Thread thread=new Thread(sendStatusToDB);
			thread.start();
		}
		
		ReSendSMS reSendSMS=ReSendSMS.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		reSendSMS.setSendManager(smsSendManager);
		reSendSMS.setCreateSyslog(createSyslog);
		reSendSMS.setSendStatusManager(smsSendStatusManager);
		if(!reSendSMS.isStarted()){
			systemTime=df.format(new Date());
			reSendSMS.setStarted(true);
			Thread thread=new Thread(reSendSMS);
			thread.start();
		}
	}

	public void setCreateSyslog(CreateSyslog createSyslog){
		this.createSyslog=createSyslog;
	}
	public void setSmsForApp(SmsForApp smsForApp) {
		this.smsForApp = smsForApp;
		
	}
	/*public void setSMSSending(SMSSending smsSending){
		this.smsSending=smsSending;
	}*/
	
	public void setAccountInfoManager(AccountInfoManager accountInfoManager) {
		this.accountInfoManager = accountInfoManager;
	}

	private static Business business = null;
	private static List<String> sessionId  =new ArrayList<String>() ;   //��ʼ��
	
	private Business() {
		// TODO Auto-generated constructor stub
		
	}
	
	public static synchronized Business getInstance() {
		if (business == null) {
			business = new Business();
		}
		return business;
	}
	
	
	public String createSessionID(String type) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sessionid = "root_" + type + "_" + sdf.format(date) + "_"
				+ date.getTime() + "_" + new Random().nextInt(100000000);
		return Utility.getMD5(sessionid);
	}
	

	public ReResult login(String appid,String captcha){
		ReResult reResult=new ReResult();
		ApplicationInfo applicationInfo=new ApplicationInfo();
		applicationInfo.setAppid(appid);
		applicationInfo.setCaptcha(captcha);
		
		//ApplicationInfoManager applicationInfoManager=new ApplicationInfoManager();  //test code
		
		if(applicationInfoManager.appLogin(applicationInfo)){
			List<ApplicationInfo> lst=applicationInfoManager.queryAppname(appid);
			
			String session=createSessionID("login");
			sessionId.add(session);    
			createSyslog.createSyslog(lst.get(0).getAppname(), "06",lst.get(0).getAppname()); 
			reResult.setReturncode("0");
			reResult.setReturndata(session);
		}else{
			reResult.setReturncode("100");
			reResult.setReturndata("");
		}
		
		return reResult;
	}

	public ReResult loginOut(String appid,String session){
		ReResult reResult=new ReResult();
		ApplicationInfo applicationInfo =new ApplicationInfo();
		for(int i=0;i<sessionId.size();i++){
			if(sessionId.get(i).equals(session)){
				applicationInfo.setAppid(appid);
				if(applicationInfoManager.appLoginOut(applicationInfo)){
					reResult.setReturncode("0");
					reResult.setReturndata(session);
					sessionId.remove(session);        //test code	
				}else {
					reResult.setReturncode("100");
					reResult.setReturndata("");
				}
				
			}
		}
		return reResult;
	}
	

/*//	public ReResult recvSMS(String appid,String session){
//		ReResult reResult=new ReResult();
//		for(int i=0;i<sessionId.size();i++){
//			if(sessionId.get(i).equals(session)){
//				SmsRecv smsRecv=new SmsRecv();
//				smsRecv.setReceiver(appid);
//				List<SmsRecv> reMessList=smsRecvManager.querySmsRecv(smsRecv);
//				if(reMessList.size()>0){
//					String data="";
//					for(int j=0;j<reMessList.size();j++){
//						data=reMessList.get(j).getMobile()+","+reMessList.get(i).getContent()+","+reMessList.get(i).getSendtime()+";";
//					}
//					reResult.setReturncode("0");
//					reResult.setReturndata(data);
//				}else {
//					reResult.setReturncode("100");
//					reResult.setReturndata("");
//				}
//			}else{
//				reResult.setReturncode("200");
//				reResult.setReturndata("");
//			}
//		}
//		return reResult;
//	}
*/	
	

	public ReResult checkSMS(String smsid,String appid,String session){
		ReResult reResult=new ReResult();
		for(int i=0;i<sessionId.size();i++){
			if(sessionId.get(i).equals(session)){
				SmsSend smsSend=new SmsSend();
				smsSend.setAppid(appid);
				int mainid=Integer.parseInt(smsid);
				smsSend.setId(mainid);
			//	SmsSendManager sendManager=new SmsSendManager();
				List<SmsSend> lst=smsSendManager.querySmsSendStatus(smsSend);
				if(lst.size()>0){
					String status=lst.get(0).getSendingstatus();
					reResult.setReturncode("0");
					reResult.setReturndata(status);
				}else {
					reResult.setReturncode("100");
					reResult.setReturndata("");
				}
				
			}else {
				reResult.setReturncode("200");
				reResult.setReturndata("");
			}
		}
		return reResult;
	}
	
	/*
	 * 0        发送成功
	 * 101 未登录
	 * 103 内容包含敏感词
	 * 105 余额不足
	 * 201 手机为空
	 * 202 手机号多于1000个
	 */
	public ReResult sendSMS(String appid,String sender,String mobiles,String content,String sessionid) throws UnsupportedEncodingException{
		ReResult result = new ReResult();
		for(int i=0;i<sessionId.size();i++){
			if(sessionId.get(i).equals(sessionid)){
				String[] str=mobiles.split(",");				
				if(str.length==0){
					result.setReturncode("201");
					result.setReturndata("");
				}else if(str.length>1000){
					result.setReturncode("202");
					result.setReturndata("");
				}else{
					
					SmsSend smsSend=new SmsSend();
	
					smsSend.setAppid(appid);
					//smsSend.setSmstype(smstype);   
					smsSend.setSender(sender); 
					smsSend.setMobile(mobiles);
					smsSend.setContent(content);
					if(content.length()>500){
						smsSend.setIslongsms("1");
					}else {
						smsSend.setIslongsms("0");
					}
					//@8.27添加日志
					createSyslog.createSyslog("系统", "08", "发送者："+sender+" 目的手机："+mobiles);
					result =smsForApp.preprocess(smsSend);
				}
			}else {
				result.setReturncode("101");
				result.setReturndata("");	
			}
		}
		return result;
	}
	/*
	 *
	 * http://http.asp.sh.cn/MT.do?Username=username&Password=password&Mobile=mobile&Content=content&Keyword=keyword
此URL用于发送信息。
参数说明：Username：帐户名
      Password：密码
　　　　　Mobile：手机号码，多个号码用半角逗号(,)隔开，单次发送不超过100个号码，超过100个号码则循环调用URL。根据测试结果，为提高送达的准确率，强烈建议以20个号码作为一组进行发送。
　　　　　Content：发送内容，最大字符数取决于运营商端口的限制，一般在60-70个字之间，具体可咨询相关客服人员。
　　　　　Keyword：密钥，密钥用于验证用户执行该操作的合法性，如果您使用的是测试帐户，可将Keyword值设置为空，如果您是正式用户，客服人员会告知您帐户密钥。得到密钥后，您需要在本地进行相关的加密处理，加密规则为：Mobile参数值的前8位 + Mobile参数值的后10位 + 密钥，三者以字符串形式相加，将相加的结果用MD5的32位加密方式加密后得出的值即为Keyword的值。例如，帐户密钥为abcxyz，同时发送短信给13636389987和13166057339(此时Mobile的值为“13636389987,13166057339”)，根据规则，三者相加后得出136363893166057339abcxyz，再用MD5的32位加密方式加密后得出D177F229627442CBDD0321E0F55FB965，此值即为Keyword的值。
返回值：· 如果发送成功，则返回０
· 如果发送失败，则返回如下错误代码：
-1  用户名或密码验证错误内容非法（内容含被过滤的关键字）
-5  内容超长
-6  密钥验证出错
-9  函数入参不正确（某参数为空或参数数据类型不正确）
	 * 
	 */
	public ReResult MT(String Username,String Password,String Mobile,String Content,String Sender,String receiverinfo){
		ReResult reResult=new ReResult();
		if(!Username.equals("")&&!Password.equals("")&&!Mobile.equals("")&&!Content.equals("")){
			//if(Content.length()<70){
				ApplicationInfo applicationInfo=new ApplicationInfo();
				List<ApplicationInfo> lst=applicationInfoManager.queryAppid(Username);
				if(lst.size()>0){
					applicationInfo.setAppid(lst.get(0).getAppid());
					applicationInfo.setCaptcha(Password);
					if(applicationInfoManager.appLogin(applicationInfo)){
						String[] str=Mobile.split(",");				
						if(str.length>0&&str.length<1000){
							SmsSend smsSend=new SmsSend();
							smsSend.setAppid(lst.get(0).getAppid());  
							smsSend.setSender(Sender); 
							smsSend.setMobile(Mobile);
							smsSend.setContent(Content);
							smsSend.setReceiverinfo(receiverinfo);
							smsSend.setIslongsms("0");	
							//@8.27添加日志
							createSyslog.createSyslog("系统", "07", "发送者："+Sender+" 发送目的："+Mobile);
							
							reResult =smsForApp.preprocess(smsSend);
							}
						}else{
							reResult.setReturncode("-1");
						}
				}else {
					reResult.setReturncode("-1");
				}
				/*}else {
					reResult.setReturncode("-5");
					}*/
			}else {
				reResult.setReturncode("-9");
			}
		
		return reResult;
	}
	
	public ReResult pushSms(String receiver,String pswd,String mobile,String content,String motime){
		ReResult reResult=new ReResult();
		SmsRecv smsRecv=new SmsRecv();
		smsRecv.setMobile(mobile);
		smsRecv.setContent(content);
		//@8.27添加日志
		createSyslog.createSyslog("系统", "10", "接收者："+receiver+" 发送者："+mobile);
		if(!receiver.equals("")&&!receiver.equals(null)&&!pswd.equals("")&&!pswd.equals(null)){
			ApplicationInfo applicationInfo=new ApplicationInfo();
			applicationInfo.setAppid(receiver);
			applicationInfo.setCaptcha(pswd);
			//if(applicationInfoManager.appLogin(applicationInfo)){                     //网关推送验证关闭，后面需要修改。
				smsRecv.setReceiver(receiver);
				if(!motime.equals("")&&!motime.equals(null)&&motime.length()==14){
					smsRecv.setSendtime(motime);
				}else {
					smsRecv.setSendtime("");
				}	
				if(smsRecvManager.addSmsRecv(smsRecv)){
					reResult.setReturncode("200k");
				}else {
					reResult.setReturncode("");
				}
			/*}else {																    //网关推送验证关闭，后面需要修改。
				reResult.setReturncode("");
			}*/
		}else {
			reResult.setReturncode("");
		}
		
		/*if(!receiver.equals("")&&!receiver.equals(null)){
			if(accountInfoManager.urlccountExist(receiver)){
				smsRecv.setReceiver(receiver);
				if(!motime.equals("")&&!motime.equals(null)&&motime.length()==14){
					smsRecv.setSendtime(motime);
				}else {
					smsRecv.setSendtime("");
				}	
				if(smsRecvManager.addSmsRecv(smsRecv)){
					reResult.setReturncode("200k");
				}else {
					reResult.setReturncode("");
				}
			}
			
		}else {
			smsRecv.setReceiver("");
			if(!motime.equals("")&&!motime.equals(null)&&motime.length()==14){
				smsRecv.setSendtime(motime);
			}else {
				smsRecv.setSendtime("");
			}	
			if(smsRecvManager.addSmsRecv(smsRecv)){
				reResult.setReturncode("200k");
			}else {
				reResult.setReturncode("");
			}
		}*/
		return reResult;
		
	}
	
	public ReResult pushSmsStatus(String receiver,String pswd,String msgid,String rpttime,String rptcode,String mobile,String status){
		ReResult reResult=new ReResult();
		//@8.27添加日志
		createSyslog.createSyslog("系统", "10", "smsid："+msgid+" 状态码:"+rptcode);
		SmsSendStatus smsSendStatus=new SmsSendStatus();
		//账号判断
		if(!receiver.equals("")&&!receiver.equals(null)&&!pswd.equals("")&&!pswd.equals(null)){
			ApplicationInfo applicationInfo=new ApplicationInfo();
			applicationInfo.setAppid(receiver);
			applicationInfo.setCaptcha(pswd);
		//	if(applicationInfoManager.appLogin(applicationInfo)){					 //网关推送验证关闭，后面需要修改。
				if(!msgid.equals("")&&!msgid.equals(null)){
					smsSendStatus.setSmsid(msgid);
					if(rptcode.equals("1")||rptcode.equals("0")){
						smsSendStatus.setSendstatus(rptcode);
					}
					if(!mobile.equals("")&&!mobile.equals(null)){
						smsSendStatus.setMobile(mobile);
					}
					if(!rpttime.equals("")&&!rpttime.equals(null)&&rpttime.length()==14){
						String sendtime=rpttime.substring(0,4)+"-"+rpttime.substring(4,6)+"-"+rpttime.substring(6,8)+" "
								+rpttime.substring(8,10)+":"+rpttime.substring(10,12)+":"+rpttime.substring(12,14);
						smsSendStatus.setSendtime(sendtime);
					}
					if(!smsSendStatusManager.mesidExist(msgid)){
						if(smsSendStatusManager.urlAddSendStatus(smsSendStatus)){
							reResult.setReturncode("200k");
						}else {
							reResult.setReturncode("");
						}
					}else {
						if(smsSendStatusManager.urlUpdateSendStatus(smsSendStatus)){
							reResult.setReturncode("200k");
						}else {
							reResult.setReturncode("");
						}
					}
				}
			/*}else {													 //网关推送验证关闭，后面需要修改。
				reResult.setReturncode("");
			}*/
		}else {
			reResult.setReturncode("");
		}
		/*if(!receiver.equals("")&&!receiver.equals(null)){
			if(accountInfoManager.urlccountExist(receiver)){
				if(!msgid.equals("")&&!msgid.equals(null)){
					smsSendStatus.setSmsid(msgid);
					if(rptcode.equals("1")||rptcode.equals("0")){
						smsSendStatus.setSendstatus(rptcode);
					}
					if(!mobile.equals("")&&!mobile.equals(null)){
						smsSendStatus.setMobile(mobile);
					}
					if(!rpttime.equals("")&&!rpttime.equals(null)&&rpttime.length()==14){
						String sendtime=rpttime.substring(0,4)+"-"+rpttime.substring(4,6)+"-"+rpttime.substring(6,8)+" "
								+rpttime.substring(8,10)+":"+rpttime.substring(10,12)+":"+rpttime.substring(12,14);
						smsSendStatus.setSendtime(sendtime);
					}
					if(!smsSendStatusManager.mesidExist(msgid)){
						if(smsSendStatusManager.urlAddSendStatus(smsSendStatus)){
							reResult.setReturncode("200k");
						}else {
							reResult.setReturncode("");
						}
					}else {
						if(smsSendStatusManager.urlUpdateSendStatus(smsSendStatus)){
							reResult.setReturncode("200k");
						}else {
							reResult.setReturncode("");
						}
					}
				}
			}else {
				if(!msgid.equals("")&&!msgid.equals(null)){
					smsSendStatus.setSmsid(msgid);
					if(rptcode.equals("1")||rptcode.equals("0")){
						smsSendStatus.setSendstatus(rptcode);
					}
					if(!mobile.equals("")&&!mobile.equals(null)){
						smsSendStatus.setMobile(mobile);
					}
					if(!rpttime.equals("")&&!rpttime.equals(null)&&rpttime.length()==14){
						String sendtime=rpttime.substring(0,4)+"-"+rpttime.substring(4,6)+"-"+rpttime.substring(6,8)+" "
								+rpttime.substring(8,10)+":"+rpttime.substring(10,12)+":"+rpttime.substring(12,14);
						smsSendStatus.setSendtime(sendtime);
					}
					if(!smsSendStatusManager.mesidExist(msgid)){
						if(smsSendStatusManager.urlAddSendStatus(smsSendStatus)){
							reResult.setReturncode("200k");
						}else {
							reResult.setReturncode("");
						}
					}else {
						if(smsSendStatusManager.urlUpdateSendStatus(smsSendStatus)){
							reResult.setReturncode("200k");
						}else {
							reResult.setReturncode("");
						}
					}
				}
			}
		}*/
		
		
		return reResult;
	}
	
}
