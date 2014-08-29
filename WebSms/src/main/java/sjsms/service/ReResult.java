package sjsms.service;

public class ReResult {

	private String returncode;
	private String returndata;
	
	public ReResult() {
		super();
	}
	public ReResult(String returncode, String returndata) {
		super();
		this.returncode = returncode;
		this.returndata = returndata;
	}
	public String getReturncode()
	{
		return returncode;
	}
	public void setReturncode(String returncode)
	{
		this.returncode = returncode;
	}
	public String getReturndata()
	{
		return returndata;
	}
	public void setReturndata(String returndata)
	{
		this.returndata = returndata;
	}
}
