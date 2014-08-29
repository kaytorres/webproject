package sjsms.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class DoDataToPush extends MultiActionController{
	public DoDataToPush(){
		
	}
	private Business business;
	public void  setBusiness(Business business){
		this.business=business;
	}
	
	public @ResponseBody void sendToPush(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String receiver = request.getParameter("receiver");
		String pswd = request.getParameter("pswd");
		String motime = request.getParameter("motime");
		String mobile = request.getParameter("mobile");
		String content = request.getParameter("content");
		ReResult reResult=business.pushSms(receiver, pswd, mobile, content, motime);
		reResult.setReturncode(reResult.getReturncode());
		//return new ModelAndView("showMT", "model", reResult);
		this.outputData(response, reResult.getReturncode());
	}
	
	public @ResponseBody void pushSmsStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String receiver = request.getParameter("receiver");
		String pswd = request.getParameter("pswd");
		String msgid = request.getParameter("msgid");
		String rpttime = request.getParameter("rpttime");
		String rptcode = request.getParameter("rptcode");
		String mobile = request.getParameter("mobile");
		String status = request.getParameter("status");
		ReResult reResult=business.pushSmsStatus(receiver, pswd, msgid, rpttime, rptcode, mobile, status);
		reResult.setReturncode(reResult.getReturncode());
		//response.getWriter().write(reResult.getReturncode());
		this.outputData(response, reResult.getReturncode());
		//return new ModelAndView("showMT", "model", reResult);
	}
	
	public @ResponseBody void sendMT(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String Username = request.getParameter("Username");
		String Password = request.getParameter("Password");
		String Mobile = request.getParameter("Mobile");
		String Content = request.getParameter("Content");
		String sender=request.getParameter("sender");
		String receiverinfo=request.getParameter("receiverinfo");
		
		//String Content=new String(request.getParameter("Content").getBytes("ISO-8859-1"),"UTF-8");
		//String C=new String(request.getParameter("Content").getBytes("ISO-8859-1"),"GBK");
	//	String Content=new String(C.getBytes("GBK"),"UTF-8");
		//String Content=request.getParameter("Content");
		//String Content=new String(C.getBytes(),"UTF-8");
		ReResult reResult=business.MT(Username, Password, Mobile, Content,sender,receiverinfo);
		reResult.setReturncode(reResult.getReturncode());
		this.outputData(response, reResult.getReturncode());
	}
	/*public @ResponseBody void ctiTest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String Password = request.getParameter("Password");
		this.outputData(response, "success");
	}
*/
	protected void outputData(HttpServletResponse response, String resultcode) {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(resultcode.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
