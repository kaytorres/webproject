package sjsms.DBservice;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import sjsms.service.Encrypt;

public class SmsRecv {
	private int id;
	private String mobile;
	private String receiver;
	private String content;
	private String sendtime;
	private String recvtime;
	public SmsRecv(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//this.sendtime =sdf.format(new java.util.Date());
		this.recvtime =sdf.format(new java.util.Date());
	}
	public SmsRecv(int id,String mobile,String receiver,String content,String sendtime,String recvtime){
		super();
		this.id=id;
		this.mobile=mobile;
		this.receiver=receiver;
		this.content=content;
		this.sendtime=sendtime;
		this.recvtime=recvtime;
		
	}
	public int getId() {
		return id;
}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		Encrypt encrypt=new Encrypt();
		try {
			this.content = encrypt.decrypt(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getRecvtime() {
		return recvtime;
	}
	public void setRecvtime(String recvtime) {
		 String Fmt = "[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}"; //2010-01-01 00:00:00 format
		  Pattern p = Pattern.compile(Fmt);
		  if(p.matcher(recvtime).matches())
		  {
			  try {
				  this.recvtime = recvtime;	
			} catch (Exception e) {
				// TODO: handle exception
			}  
		  }		
	}
	public String getSendtime() {
		return sendtime;
	}
	
	//"20140731092830"
	public void setSendtime(String sendtime) {
		String motime="";
		if(!sendtime.equals("")){
			motime=sendtime.substring(0, 4)+"-"+sendtime.substring(4, 6)+"-"+sendtime.substring(6, 8)+" "+
					sendtime.substring(8, 10)+":"+sendtime.substring(10, 12)+":"+sendtime.substring(12, 14);
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//this.sendtime =sdf.format(new java.util.Date());
			motime =sdf.format(new java.util.Date());
		}
		this.sendtime = motime;	
			
	}
}
