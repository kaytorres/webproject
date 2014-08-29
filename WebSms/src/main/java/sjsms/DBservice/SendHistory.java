package sjsms.DBservice;

public class SendHistory {
	private int mainid;
	private String appname;
	private String mobile;
	private String sender;
	private String reciver;
	private String sendtime;
	private String sendstatus;
	private String isrecivered;
	private int resendcount;
	private String resendtype;
	private String resender;
	private String content;

	public SendHistory() {

	}

	public SendHistory(int mainid,String appname, String mobile, String sender,
			String reciver, String sendtime, String sendstatus,
			String isrecivered, int resendcount, String resendtype,
			String resender, String content) {
		super();
		this.mainid=mainid;
		this.appname=appname;
		this.mobile=mobile;
		this.sender=sender;
		this.reciver=reciver;
		this.sendtime=sendtime;
		this.sendstatus=sendstatus;
		this.isrecivered=isrecivered;
		this.resendcount=resendcount;
		this.resendtype=resendtype;
		this.resender=resender;
		this.content=content;

	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciver() {
		return reciver;
	}

	public void setReciver(String reciver) {
		this.reciver = reciver;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getSendstatus() {
		return sendstatus;
	}

	public void setSendstatus(String sendstatus) {
		this.sendstatus = sendstatus;
	}

	public String getIsrecivered() {
		return isrecivered;
	}

	public void setIsrecivered(String isrecivered) {
		this.isrecivered = isrecivered;
	}

	public int getResendcount() {
		return resendcount;
	}

	public void setResendcount(int resendcount) {
		this.resendcount = resendcount;
	}

	public String getResendtype() {
		return resendtype;
	}

	public void setResendtype(String resendtype) {
		this.resendtype = resendtype;
	}

	public String getResender() {
		return resender;
	}

	public void setResender(String resender) {
		this.resender = resender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public int getMainid() {
		return mainid;
	}

	public void setMainid(int mainid) {
		this.mainid = mainid;
	}

}
