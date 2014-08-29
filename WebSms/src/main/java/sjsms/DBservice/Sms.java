package sjsms.DBservice;

public class Sms {
	private String user;
	private String password;
	//private Object mobiles;
//	private	Object content;
	private String[] mobiles;
	private	String content;
	private String plantime;
	private String filename;
	
	public Sms(){
		
	}
	public Sms(String user,String password,String[] mobiles,String content,String plantime,String filename){
		super();
		this.user=user;
		this.password=password;
		this.mobiles=mobiles;
		this.content=content;
		this.plantime=plantime;
		this.filename=filename;
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
	public String[] getMobiles() {
		return mobiles;
	}
	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPlantime() {
		return plantime;
	}
	public void setPlantime(String plantime) {
		this.plantime = plantime;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
