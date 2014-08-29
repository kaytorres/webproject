package sjsms.DBservice;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/*
id
operator
action
content
operatetime
 */
public class Syslog {
	private int id;
	private String operator;
	private String action;
	private String content;
	private String operatetime;
	public Syslog(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.operatetime =sdf.format(new java.util.Date());
	}
	public Syslog(int id, String operator, String action, String content, String operatetime) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.operator=operator;
		this.action=action;
		this.content=content;
		this.operatetime=operatetime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperatetime() {
		return operatetime;
	}
	public void setOperatetime(String operatetime) {
		 String Fmt = "[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}"; //2010-01-01 00:00:00 format
		  Pattern p = Pattern.compile(Fmt);
		  if(p.matcher(operatetime).matches())
		  {
			  try {
				  this.operatetime = operatetime;	
			} catch (Exception e) {
				// TODO: handle exception
			}  
		  }		
		
	}
}
