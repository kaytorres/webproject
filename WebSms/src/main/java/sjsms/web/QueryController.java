package sjsms.web;

import sjsms.service.JsonUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



import sjsms.DBservice.AccountInfo;
import sjsms.DBservice.AccountInfoManager;
import sjsms.DBservice.ApplicationInfo;
import sjsms.DBservice.ApplicationInfoManager;
import sjsms.DBservice.SendHistory;
import sjsms.DBservice.SensitiveWord;
import sjsms.DBservice.SensitiveWordManager;
import sjsms.DBservice.SmsRecv;
import sjsms.DBservice.SmsRecvManager;
import sjsms.DBservice.SmsSend;
import sjsms.DBservice.SmsSendManager;
import sjsms.DBservice.SmsSendStatusManager;
import sjsms.DBservice.Syslog;
import sjsms.DBservice.SyslogManager;

import sjsms.DBservice.SystemUserManager;
import sjsms.DBservice.User;
import sjsms.service.Business;
import sjsms.service.CreateSyslog;
import sjsms.service.WebCallWebService;


@Controller
public class QueryController {

	public QueryController() {
		// TODO Auto-generated constructor stub
	}
	private SmsSendManager smsSendManager;
	@Resource(name="smsSendManager")
	public void setSmsSendManager(SmsSendManager smsSendManager) {
		this.smsSendManager = smsSendManager;
	}
	private SmsRecvManager smsRecvManager;
	@Resource(name="smsRecvManager")
	public void setSmsRecvManager(SmsRecvManager smsRecvManager) {
		this.smsRecvManager = smsRecvManager;
	}
	private AccountInfoManager accountInfoManager;
	@Resource(name="accountInfoManager")
	public void setAccountInfoManager(AccountInfoManager accountInfoManager) {
		this.accountInfoManager = accountInfoManager;
	}
	private ApplicationInfoManager applicationInfoManager;
	@Resource(name="applicationInfoManager")
	public void setApplicationInfoManager(ApplicationInfoManager applicationInfoManager) {
		this.applicationInfoManager = applicationInfoManager;
	}
	private SystemUserManager systemUserManager;
	@Resource(name="systemUserManager")
	public void setSystemUserManager(SystemUserManager systemUserManager) {
		this.systemUserManager = systemUserManager;
	}
	private SyslogManager syslogManager;
	@Resource(name="syslogManager")
	public void setSyslogManager(SyslogManager syslogManager) {
		this.syslogManager = syslogManager;
	}
	private SmsSendStatusManager smsSendStatusManager;
	@Resource(name="smsSendStatusManager")
	public void setSmsSendStatusManager( SmsSendStatusManager smsSendStatusManager) {
		this.smsSendStatusManager = smsSendStatusManager;
	}
	private SensitiveWordManager sensitiveWordManager;
	@Resource(name="sensitiveWordManager")
	public void setSensitiveWordManager(SensitiveWordManager sensitiveWordManager) {
		this.sensitiveWordManager = sensitiveWordManager;
	}
	private Business business;
	@Resource(name="business")
	public void setBusiness(Business business) {
		this.business = business;
	}
	

   @RequestMapping(value="/index")
    public String index(Model mod) {
		mod.addAttribute("message", "pppp");
        return "showMessage";
    }
   
   
   @RequestMapping(value="/login")
   public String login(Model mod,HttpSession session,HttpServletResponse response) {
		return "login";
   }
   
   @RequestMapping(value="/showMT")
   public String showMT(Model mod,HttpSession session,HttpServletResponse response) {
		return "showMT";
   }
   
   @RequestMapping(value="/loginout")
   public String loginout(Model mod,HttpSession session,HttpServletResponse response) {
	   String sessionID=(String)session.getAttribute("appSessionID");
	   String appID=(String)session.getAttribute("appID");
	   WebCallWebService webCallWebService=new WebCallWebService();
	   webCallWebService.setBusiness(business);
	   if(webCallWebService.loginOut(appID, sessionID)){
		   session.removeAttribute("appSessionID");
	   }else {
		   session.removeAttribute("appSessionID");
	}
	   session.removeAttribute("logonName");
	   session.removeAttribute("role");
	   session.removeAttribute("lstaccountname");
	   session.removeAttribute("lstaccountgwurl");
	   session.removeAttribute("lstaccountusername");
	   session.removeAttribute("lstaccountpassword");
	   session.removeAttribute("lstaccount3despassword");
	   session.removeAttribute("oldaccount");
 	   return "redirect:/login";
   }
   
   @RequestMapping("/authuser")
   public  String authuser(@RequestParam(value="loginname", required=true) String loginname,
   						   @RequestParam(value="password", required=true) String password,
   						   Model mod,
   						   HttpSession session) {
	    List<User> lUsers=systemUserManager.queryName(loginname);
	    if(lUsers.size()>0){
	    	String logonName=lUsers.get(0).getName();
			String role=lUsers.get(0).getRoleid();
			//if(!roleid.equals("01")){
			//	mod.addAttribute("loginstatusrole", "fail");
			//	return "login";
			//}else{
				if (systemUserManager.login(loginname, password) == true) {
					session.setAttribute("logonName", logonName);
					session.setAttribute("role", role);
					session.setAttribute("login", true);
					List<ApplicationInfo> applicationInfos=applicationInfoManager.queryAppid("web");
					if(applicationInfos.size()>0){
						WebCallWebService webCallWebService=new WebCallWebService();
						webCallWebService.setBusiness(business);
						CreateSyslog createSyslog=new CreateSyslog();
						createSyslog.setSyslogManager(syslogManager);
						createSyslog.createSyslog(logonName, "01", logonName);   //生成管理员登录管理界面的日志
						String sessionID=webCallWebService.getSessionID(applicationInfos.get(0).getAppid(),applicationInfos.get(0).getCaptcha());
						session.setAttribute("appSessionID", sessionID);
						session.setAttribute("appID",applicationInfos.get(0).getAppid());
						List<AccountInfo> lst=accountInfoManager.queryAccount();
						if(lst.size()>0){
							float b=lst.get(0).getBalance();
							String bToString = String.valueOf(b);
							int index=bToString.indexOf(".");
							String balance=bToString.substring(0,index);
							Date dt=new Date();
							SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
							session.setAttribute("Todaystarttime",matter.format(dt));
							session.setAttribute("Todayendtime",matter.format(dt));
							session.setAttribute("balance", balance);
							session.setAttribute("oldaccount", loginname);
							return "redirect:/sendsms";
						}else {
							mod.addAttribute("loginstatuspw", "fail");
							return "login";
						}
						
					}else {
						mod.addAttribute("loginstatuspw", "fail");
						return "login";
					}
					} else {
						mod.addAttribute("loginstatuspw", "fail");
						return "login";
					}
		//	}
	    }else{
	    	mod.addAttribute("loginstatusnouser", "fail");
			return "login";
	    }
	}
   
   @RequestMapping(value="/sendsms")
   public String sendsms(Model mod,HttpSession session,HttpServletResponse response) {
		return "sendsms";
   }
   
   
   @RequestMapping(value="/send")
   public @ResponseBody void send(
		   @RequestParam(value="mobiles", required=false, defaultValue="") String mobiles,
		   @RequestParam(value="content", required=false, defaultValue="") String content,
		   HttpSession session,HttpServletResponse response) throws UnsupportedEncodingException {
	    String sessionID=(String)session.getAttribute("appSessionID");
	    String appID=(String)session.getAttribute("appID");
	    String sender=(String)session.getAttribute("logonName");
	    WebCallWebService webCallWebService=new WebCallWebService();
		webCallWebService.setBusiness(business);
		Map<String, Object> map = new HashMap<String, Object>();
		if(webCallWebService.send(appID, sender, mobiles, content, sessionID)){
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping(value="/resendsms")
   public @ResponseBody void resendsms(
		   @RequestParam(value="mobile", required=false, defaultValue="") String mobile,
		   @RequestParam(value="content", required=false, defaultValue="") String content,
		   @RequestParam(value="mainid", required=false, defaultValue="") String mainid,
		   @RequestParam(value="sender", required=false, defaultValue="") String sender,
		   HttpSession session,HttpServletResponse response) throws UnsupportedEncodingException {
	    String sessionID=(String)session.getAttribute("appSessionID");
	    String appID=(String)session.getAttribute("appID");
	    WebCallWebService webCallWebService=new WebCallWebService();
		webCallWebService.setBusiness(business);
		Map<String, Object> map = new HashMap<String, Object>();
		if(smsSendStatusManager.urlReSendStatus(mainid, mobile, sender)){
			if(webCallWebService.send(appID, sender, mobile, content, sessionID)){
				map.put("result", "success");
			}else {
				map.put("result", "fail");
			}
		}else {
			map.put("result", "fail");
		}
		
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping(value="/showsyslog")
   public String showsyslog(Model mod) {
	   List<Syslog> lstList=syslogManager.queryOperater();
		mod.addAttribute("syslogOperatorlist",lstList);
		return "showsyslog";
   }
   @RequestMapping("/syslog")
   public @ResponseBody void syslog(
   		@RequestParam(value="currPage", required=true, defaultValue="1")Integer currPage,
   		@RequestParam(value="pageSize", required=true, defaultValue="20")Integer pageSize,
   		@RequestParam(value="starttime", required=false, defaultValue="")String starttime,
   		@RequestParam(value="endtime", required=false, defaultValue="")String endtime,
   		@RequestParam(value="opcode", required=false, defaultValue="")String opcode,
   		@RequestParam(value="operator", required=false, defaultValue="")String operator,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Syslog> lst;
		int  total = syslogManager.querySyslogSize(opcode, operator, starttime, endtime);
		int index= (currPage-1)*pageSize;		
		lst= syslogManager.querySyslog(opcode, operator, starttime, endtime,index,pageSize);	
		map.put("result", lst);
		map.put("currPage", currPage);
		map.put("totalNum", total);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/user")
   public @ResponseBody void user(
   		@RequestParam(value="currPage", required=true, defaultValue="1")Integer currPage,
   		@RequestParam(value="pageSize", required=true, defaultValue="20")Integer pageSize,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<User> lst;
		int  total = systemUserManager.queryUserSize();
		int index= (currPage-1)*pageSize;		
		lst= systemUserManager.queryUser(index,pageSize);	
		map.put("result", lst);
		map.put("currPage", currPage);
		map.put("totalNum", total);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping("/adduser")
   public @ResponseBody void adduser(
			@RequestParam(value="account", required=true)String account,
	   		@RequestParam(value="name", required=true)String name,
	   		@RequestParam(value="password", required=true )String password,
	   		@RequestParam(value="roleid", required=true)String roleid,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		 String logonname=(String)session.getAttribute("logonName");
		if(!systemUserManager.userExist(account)){
			if(systemUserManager.addUser(account, name, password, roleid)){
				CreateSyslog createSyslog=new CreateSyslog();
				createSyslog.setSyslogManager(syslogManager);
				createSyslog.createSyslog(logonname, "04", "增加 "+name);   //日志
				map.put("result", "success");
			}else {
				map.put("result", "failadd");
			}
		}else {
			map.put("result", "userexist");
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping("/modifyuser")
   public @ResponseBody void modifyuser(
			@RequestParam(value="account", required=true)String account,
	   		@RequestParam(value="name", required=true)String name,
	   		@RequestParam(value="password", required=true )String password,
	   		@RequestParam(value="roleid", required=true)String roleid,
	   		@RequestParam(value="oldaccount", required=true)String oldaccount,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		if(systemUserManager.userExist(account)){
			if(password.equals("")){
				if(systemUserManager.updateUserNonePwd(account, name, roleid, oldaccount)){
					CreateSyslog createSyslog=new CreateSyslog();
					createSyslog.setSyslogManager(syslogManager);
					createSyslog.createSyslog(logonname, "04", "更新 "+name);   //日志
					map.put("result", "success");
				}else {
					map.put("result", "failmodify");
				}
			}else {
				if(systemUserManager.updateUser(account, name, password, roleid, oldaccount)){
					CreateSyslog createSyslog=new CreateSyslog();
					createSyslog.setSyslogManager(syslogManager);
					createSyslog.createSyslog(logonname, "04", "更新 "+name);   //日志
					map.put("result", "success");
				}else {
					map.put("result", "failmodify");
				}
			}
			}else{
				map.put("result", "userexist");
			}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping("/deluser")
   public @ResponseBody void deluser(
			@RequestParam(value="account", required=true)String account,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		List<User> lst=systemUserManager.queryName(account);
		if(systemUserManager.delUser(account)){
			CreateSyslog createSyslog=new CreateSyslog();
			createSyslog.setSyslogManager(syslogManager);
			createSyslog.createSyslog(logonname, "04", "删除 "+lst.get(0).getName());   //日志
			map.put("result", "success");
			session.removeAttribute("delbyloginname");
			session.removeAttribute("delbyrealname");
		}else {
			map.put("result", "fail");
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping(value="/showaccount")
   public String showaccount(Model mod,HttpSession session,HttpServletResponse response) {
	   List<AccountInfo> listAccountInfos=accountInfoManager.queryAccount();
	   if(listAccountInfos.size()>0){
		   session.setAttribute("lstaccountname", listAccountInfos.get(0).getName());
		   session.setAttribute("lstaccountgwurl", listAccountInfos.get(0).getUrl());
		   session.setAttribute("lstaccountusername", listAccountInfos.get(0).getUsername());
		   session.setAttribute("lstaccountpassword", listAccountInfos.get(0).getPassword());
		   session.setAttribute("lstaccount3despassword", listAccountInfos.get(0).getPasswordfor3des());
	   }
		return "showaccount";
   }
   @RequestMapping("/queryaccount")
   public @ResponseBody void queryaccount(
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AccountInfo> lsAccountInfos=accountInfoManager.queryAccount();
		map.put("result", lsAccountInfos);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/account")
   public @ResponseBody void account(
   		@RequestParam(value="account", required=true)String account,
   		@RequestParam(value="password", required=true)String password,
   		@RequestParam(value="gwurl", required=true )String gwurl,
   		@RequestParam(value="despassword", required=true)String despassword,
   		@RequestParam(value="name", required=true)String name,
   		@RequestParam(value="oldaccountname", required=true)String oldaccountname,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		try {
			if(accountInfoManager.updateAccount(account,gwurl,name,password,despassword,oldaccountname)){
				CreateSyslog createSyslog=new CreateSyslog();
				createSyslog.setSyslogManager(syslogManager);
				createSyslog.createSyslog(logonname, "02", oldaccountname);   //生成网关维护日志
				map.put("result", "success");
			}else {
				map.put("result", "fail");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping("/app")
   public @ResponseBody void app(
			@RequestParam(value="currPage", required=true, defaultValue="1")Integer currPage,
	   		@RequestParam(value="pageSize", required=true, defaultValue="20")Integer pageSize,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ApplicationInfo> lst;
		int  total = applicationInfoManager.queryAppSize();
		int index= (currPage-1)*pageSize;		
		lst= applicationInfoManager.queryApp(index, pageSize);
		map.put("result", lst);
		map.put("currPage", currPage);
		map.put("totalNum", total);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/senword")
   public @ResponseBody void senword(
			@RequestParam(value="currPage", required=true, defaultValue="1")Integer currPage,
	   		@RequestParam(value="pageSize", required=true, defaultValue="20")Integer pageSize,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SensitiveWord> lst;
		int  total = sensitiveWordManager.querySenWordSize();
		int index= (currPage-1)*pageSize;		
		lst= sensitiveWordManager.querySenWord(index, pageSize);
		map.put("result", lst);
		map.put("currPage", currPage);
		map.put("totalNum", total);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/addsenword")
   public @ResponseBody void addsenword(
	   		@RequestParam(value="senwordcontent", required=true)String senwordcontent,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		if(!sensitiveWordManager.senWordExist(senwordcontent)){
			if(sensitiveWordManager.addSensitiveWord(senwordcontent)){
				CreateSyslog createSyslog=new CreateSyslog();
				createSyslog.setSyslogManager(syslogManager);
				createSyslog.createSyslog(logonname, "05", "增加  "+senwordcontent);   //日志
				map.put("result", "success");
			}else {
				map.put("result", "failadd");
			}
		}else {
			map.put("result", "userexist");
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping("/modifysenword")
   public @ResponseBody void modifysenword(
		   @RequestParam(value="senwordid", required=true)int senwordid,
	   		@RequestParam(value="senwordcontent", required=true)String senwordcontent,
	   		@RequestParam(value="oldsenwordid", required=true )int oldsenwordid,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		if(!sensitiveWordManager.senWordExist(senwordcontent)){
				if(sensitiveWordManager.updateSenWord(senwordid, senwordcontent, oldsenwordid)){
					CreateSyslog createSyslog=new CreateSyslog();
					createSyslog.setSyslogManager(syslogManager);
					createSyslog.createSyslog(logonname, "05", "更新  "+senwordcontent);   //日志
					map.put("result", "success");
				}else {
					map.put("result", "failmodify");
				}
			
			}else {
				map.put("result", "userexist");
				}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/delsenword")
   public @ResponseBody void delsenword(
			@RequestParam(value="senwordid", required=true)int senwordid,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		List<SensitiveWord> sensitiveWords=sensitiveWordManager.querySenWord(senwordid);
		if(sensitiveWords.size()>0){
			if(sensitiveWordManager.delSensitiveWord(senwordid)){
				CreateSyslog createSyslog=new CreateSyslog();
				createSyslog.setSyslogManager(syslogManager);
				createSyslog.createSyslog(logonname, "05", "删除 "+sensitiveWords.get(0).getWord());   //日志
				map.put("result", "success");
			}else {
				map.put("result", "fail");
			}
		}else {
			map.put("result", "nothisid");
		}
		
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/delapp")
   public @ResponseBody void delapp(
			@RequestParam(value="appid", required=true)String appid,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		List<ApplicationInfo> appname=applicationInfoManager.queryAppname(appid);
		if(applicationInfoManager.delApp(appid)){
			CreateSyslog createSyslog=new CreateSyslog();
			createSyslog.setSyslogManager(syslogManager);
			createSyslog.createSyslog(logonname, "03", "删除 "+appname.get(0).getAppname());   //日志
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/addapp")
   public @ResponseBody void addapp(
			@RequestParam(value="appid", required=true)String appid,
	   		@RequestParam(value="appname", required=true)String appname,
	   		@RequestParam(value="externo", required=true )String externo,
	   		@RequestParam(value="captcha", required=true)String captcha,
	   		@RequestParam(value="needresend", required=true)String needresend,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		if(!applicationInfoManager.appExist(appid)){
			if(applicationInfoManager.addApp(appid, appname, externo, captcha,needresend)){
				CreateSyslog createSyslog=new CreateSyslog();
				createSyslog.setSyslogManager(syslogManager);
				createSyslog.createSyslog(logonname, "03", "增加  "+appname);   //日志
				map.put("result", "success");
			}else {
				map.put("result", "failadd");
			}
		}else {
			map.put("result", "userexist");
		}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   
   @RequestMapping("/modifyapp")
   public @ResponseBody void modifyapp(
		   @RequestParam(value="appid", required=true)String appid,
	   		@RequestParam(value="appname", required=true)String appname,
	   		@RequestParam(value="externo", required=true )String externo,
	   		@RequestParam(value="captcha", required=true)String captcha,
	   		@RequestParam(value="needresend", required=true)String needresend,
	   		@RequestParam(value="oldappid", required=true)String oldappid,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String logonname=(String)session.getAttribute("logonName");
		if(applicationInfoManager.appExist(appid)){
			if(captcha.equals("")){
				if(applicationInfoManager.updateAppNonePwd(appid, appname, externo,needresend, oldappid)){
					CreateSyslog createSyslog=new CreateSyslog();
					createSyslog.setSyslogManager(syslogManager);
					createSyslog.createSyslog(logonname, "03", "更新  "+appname);   //日志
					map.put("result", "success");
				}else {
					map.put("result", "failmodify");
				}
			}else {
				if(applicationInfoManager.updateApp(appid, appname, externo, captcha,needresend, oldappid)){
					CreateSyslog createSyslog=new CreateSyslog();
					createSyslog.setSyslogManager(syslogManager);
					createSyslog.createSyslog(logonname, "03", "更新  "+appname);   //日志
					map.put("result", "success");
				}else {
					map.put("result", "failmodify");
				}
			}
			}else {
				map.put("result", "userexist");
				}
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/sendhistory")
   public @ResponseBody void sendhistory(
   		@RequestParam(value="currPage", required=true, defaultValue="1")Integer currPage,
   		@RequestParam(value="pageSize", required=true, defaultValue="20")Integer pageSize,
   		@RequestParam(value="starttime", required=false, defaultValue="")String starttime,
   		@RequestParam(value="endtime", required=false, defaultValue="")String endtime,
   		@RequestParam(value="appname", required=false, defaultValue="")String appname,
   		@RequestParam(value="mobile", required=false, defaultValue="")String mobile,
   		@RequestParam(value="sender", required=false, defaultValue="")String sender,
   		@RequestParam(value="sendstatus", required=false, defaultValue="")String sendstatus,
   		@RequestParam(value="sendingstatus", required=false, defaultValue="")String sendingstatus,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		int  total = smsSendManager.querySendHisSize(appname, mobile, starttime, endtime,sender,sendstatus,sendingstatus);
		int index= (currPage-1)*pageSize;		
		List<SendHistory> lst= smsSendManager.querySendHis(appname, mobile, starttime, endtime,sender,sendstatus,sendingstatus, index, pageSize);	
		map.put("result", lst);
		map.put("currPage", currPage);
		map.put("totalNum", total);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping("/rechistory")
   public @ResponseBody void rechistory(
   		@RequestParam(value="currPage", required=true, defaultValue="1")Integer currPage,
   		@RequestParam(value="pageSize", required=true, defaultValue="20")Integer pageSize,
   		@RequestParam(value="starttime", required=false, defaultValue="")String starttime,
   		@RequestParam(value="endtime", required=false, defaultValue="")String endtime,
   		@RequestParam(value="mobile", required=false, defaultValue="")String mobile,
   		HttpServletResponse response,
   		HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		int  total = smsRecvManager.queryRecHisSize(mobile, starttime, endtime);
		int index= (currPage-1)*pageSize;		
		List<SmsRecv> lst= smsRecvManager.queryRecHis(mobile, starttime, endtime, index, pageSize);	
		map.put("result", lst);
		map.put("currPage", currPage);
		map.put("totalNum", total);
		String json = JsonUtil.map2json(map);
		this.outputJsonData(response, json);
   }
   @RequestMapping(value="/showrechistory")
   public String showrechistory(Model mod,HttpSession session,HttpServletResponse response) {
		return "showrechistory";
   }
   
   @RequestMapping(value="/showsendhistory")
   public String showsendhistory(Model mod,HttpSession session,HttpServletResponse response) {
	   List<ApplicationInfo> lstApplicationInfos=applicationInfoManager.queryApplicationInfo();
	 //  List<SmsSend> lstSender=smsSendManager.querySender();
	   mod.addAttribute("lstApplicationInfos",lstApplicationInfos);
	  // mod.addAttribute("lstSender",lstSender);
	   return "showsendhistory";
   }
   @RequestMapping(value="/showusers")
   public String showusers(Model mod,HttpSession session,HttpServletResponse response) {
		return "showusers";
   }
   @RequestMapping(value="/showapp")
   public String showapp(Model mod,HttpSession session,HttpServletResponse response) {
		return "showapp";
   }
   @RequestMapping(value="/showsenword")
   public String showsenword(Model mod,HttpSession session,HttpServletResponse response) {
		return "showsenword";
   }
   
 /*  @RequestMapping(value = "/upload")  
   public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {  
 
       System.out.println("开始");  
       String path = request.getSession().getServletContext().getRealPath("upload");  
       String fileName = file.getOriginalFilename();  
//       String fileName = new Date().getTime()+".jpg";  
       System.out.println(path);  
       File targetFile = new File(path, fileName);  
       if(!targetFile.exists()){  
           targetFile.mkdirs();  
       }  
 
       //保存  
       try {  
           file.transferTo(targetFile);  
       } catch (Exception e) {  
           e.printStackTrace();  
       }  
       model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);  
       return "result";  
   }*/ 
  
 /*  @RequestMapping(value="/upload", method=RequestMethod.GET)
   public @ResponseBody String getUpload() {
       return "You can upload a file by posting to this same URL.";
   }

   @RequestMapping(value="/upload", method=RequestMethod.POST)
   public @ResponseBody String postUpload(@RequestParam("name") String name,
           @RequestParam("file") MultipartFile file){
       if (!file.isEmpty()) {
           try {
               byte[] bytes = file.getBytes();
               BufferedOutputStream stream =
                       new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
               stream.write(bytes);
               stream.close();
               return "You successfully uploaded " + name + " into " + name + "-uploaded !";
           } catch (Exception e) {
               return "You failed to upload " + name + " => " + e.getMessage();
           }
       } else {
           return "You failed to upload " + name + " because the file was empty.";
       }
   }*/
   
	@RequestMapping("/resend")
    public String usermodify(
    		@RequestParam(value="mobile", required=true) String mobile,
    		@RequestParam(value="content", required=true) String content,
    		@RequestParam(value="mainid", required=true) String mainid,
    		Model mod,
    		HttpSession session) {	
		try {
			content = new String(content.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			session.setAttribute("mobile", mobile);
			session.setAttribute("content", content);
			session.setAttribute("mainid", mainid);
		return "resend";
    }
   
   protected void outputJsonData(HttpServletResponse response, String json) {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(json.getBytes("UTF-8"));
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
