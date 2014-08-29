package sjsms.DBservice;

/*
appid
appname
externo
captcha

 */
public class ApplicationInfo {
	private String appid;
	private String appname;
	private String externo;
	private String captcha;
	private String needresend;
	public ApplicationInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public ApplicationInfo(String appid,String appname,String externo,String captcha,String needresend){
		super();
		this.appid=appid;
		this.appname=appname;
		this.externo=externo;
		this.captcha=captcha;
		this.needresend=needresend;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getExterno() {
		return externo;
	}
	public void setExterno(String externo) {
		this.externo = externo;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getNeedresend() {
		return needresend;
	}

	public void setNeedresend(String needresend) {
		this.needresend = needresend;
	}
	
	
}
