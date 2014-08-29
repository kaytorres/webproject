package sjsms.DBservice;

import java.util.regex.Pattern;

/*//id
//mainid
//smsid
//mobile
//sendstatus
//sendtime
*/
public class SmsSendStatus {
	private int id;
	private int mainid;
	private String smsid;
	private String mobile;
	private String sendstatus;
	private String sendtime;
	
	public SmsSendStatus(){
		
	}
	
	public SmsSendStatus(int id,int mainid,String smsid,String mobile,String sendstatus,String sendtime){
		super();
		this.id=id;
		this.mainid=mainid;
		this.smsid=smsid;
		this.mobile=mobile;
		this.sendstatus=sendstatus;
		this.sendtime=sendtime;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMainid() {
		return mainid;
	}

	public void setMainid(int mainid) {
		this.mainid = mainid;
	}

	public String getSmsid() {
		return smsid;
	}

	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSendstatus() {
		return sendstatus;
	}

	public void setSendstatus(String sendstatus) {
		this.sendstatus = sendstatus;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		
		 String Fmt = "[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}"; //2010-01-01 00:00:00 format
		  Pattern p = Pattern.compile(Fmt);
		  if(p.matcher(sendtime).matches())
		  {
			  try {
				  this.sendtime = sendtime;	
			} catch (Exception e) {
				// TODO: handle exception
			}  
		  }		
	}

}
