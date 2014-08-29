package sjsms.DBservice;

public class User {
	private String account;
	private String name;
	private String password;
	private String roleid;
	
	public User(String account,String name,String password,String roleid){
		this.account=account;
		this.name=name;
		this.password=password;
		this.roleid=roleid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

}
