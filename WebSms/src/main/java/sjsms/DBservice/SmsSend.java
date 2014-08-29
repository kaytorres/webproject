package sjsms.DBservice;

import java.util.regex.Pattern;

/*
 id
 Appid
 Smstype
 Mobile
 Sender
 Content
 Islongsms
 Smsid
 Sendingstatus
 Sendingtime
 */
public class SmsSend {
	private int id;
	private String appid;
	private String smstype;
	private String mobile;  
	private String sender;
	private String content;
	private String islongsms;
	private String longsmsid;
	private String smsid;
	private String sendingstatus;
	private String sendingtime;
	private String receiverinfo;

	public String getReceiverinfo() {
		return receiverinfo;
	}

	public void setReceiverinfo(String receiverinfo) {
		this.receiverinfo = receiverinfo;
	}

	public SmsSend() {

	}

	public SmsSend(int id, String appid, String smstype, String mobile,
			String sender, String content, String islongsms, String longsmsid,String smsid,
			String sendingstatus, String sendingtime,String receiverinfo) {
		super();
		this.id = id;
		this.appid = appid;
		this.smstype = smstype;
		this.mobile = mobile;
		this.sender = sender;
		this.content = content;
		this.islongsms = islongsms;
		this.longsmsid=longsmsid;
		this.smsid = smsid;
		this.sendingstatus = sendingstatus;
		this.sendingtime = sendingtime;
		this.receiverinfo=receiverinfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSmstype() {
		return smstype;
	}

	public void setSmstype(String smstype) {
		this.smstype = smstype;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIslongsms() {
		return islongsms;
	}

	public void setIslongsms(String islongsms) {
		this.islongsms = islongsms;
	}

	public String getSmsid() {
		return smsid;
	}

	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}

	public String getSendingstatus() {
		return sendingstatus;
	}

	public void setSendingstatus(String sendingstatus) {
		this.sendingstatus = sendingstatus;
	}

	public String getSendingtime() {
		return sendingtime;
	}

	public void setSendingtime(String sendingtime) {
		String Fmt = "[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}"; // 2010-01-01
																										// 00:00:00
																										// format
		Pattern p = Pattern.compile(Fmt);
		if (p.matcher(sendingtime).matches()) {
			try {
				this.sendingtime = sendingtime;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public String getLongsmsid() {
		return longsmsid;
	}

	public void setLongsmsid(String longsmsid) {
		this.longsmsid = longsmsid;
	}

}
