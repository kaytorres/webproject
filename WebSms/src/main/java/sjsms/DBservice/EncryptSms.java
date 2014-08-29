package sjsms.DBservice;

public class EncryptSms {
	private String user;
	private String password;
	private String mobile;
	private String msg;
	public EncryptSms(String user,String password,String mobile,String msg){
		this.user=user;
		this.password=password;
		this.mobile=mobile;
		this.msg=msg;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
