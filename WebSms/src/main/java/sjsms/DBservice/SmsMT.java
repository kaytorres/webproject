package sjsms.DBservice;

import java.util.regex.Pattern;

public class SmsMT {
	private String user;
	private String password;
	private String startTime;
	private String endTime;
	private String mobile;
	private String msgid;
	
	public SmsMT(){
		
	}
	public SmsMT(String user,String password,String startTime,String endTime,String mobile,String msgid){
		super();
		this.user=user;
		this.password=password;
		this.startTime=startTime;
		this.endTime=endTime;
		this.mobile=mobile;
		this.msgid=msgid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		 String Fmt = "[0-9]{4}[0-9]{2}[0-9]{2}[0-2]{1}[0-9]{1}[0-5]{1}[0-9]{1}"; //209912121259 format
		  Pattern p = Pattern.compile(Fmt);
		  if(p.matcher(startTime).matches())
		  {
			  try {
				  this.startTime = startTime;	
			} catch (Exception e) {
				// TODO: handle exception
			}  
		  }else {
			this.startTime="";
		}	
		//this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		String Fmt = "[0-9]{4}[0-9]{2}[0-9]{2}[0-2]{1}[0-9]{1}[0-5]{1}[0-9]{1}"; //209912121259 format
		  Pattern p = Pattern.compile(Fmt);
		  if(p.matcher(endTime).matches())
		  {
			  try {
				  this.endTime = endTime;	
			} catch (Exception e) {
				// TODO: handle exception
			}  
		  }	else {
			this.endTime="";
		}
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

}
