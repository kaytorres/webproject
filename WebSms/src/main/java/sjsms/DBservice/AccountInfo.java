package sjsms.DBservice;

public class AccountInfo {
	/*name
	url
	username
	password
	balance
	passwordfor3des*/
	private String name;
	private String url;
	private String username;
	private String password;
	private float balance;
	private String passwordfor3des;
	
	public AccountInfo(){
		
	}
	
	public AccountInfo(String name,String url,String username,String password,float balance,String passwordfor3des){
		super();
		this.name=name;
		this.url=url;
		this.username=username;
		this.password=password;
		this.balance=balance;
		this.passwordfor3des=passwordfor3des;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getPasswordfor3des() {
		return passwordfor3des;
	}

	public void setPasswordfor3des(String passwordfor3des) {
		this.passwordfor3des = passwordfor3des;
	}
	

}
