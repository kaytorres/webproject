package sjsms.service;

import sjsms.DBservice.SyslogManager;

public class CreateSyslog {
	private SyslogManager syslogManager;
	public void setSyslogManager(SyslogManager syslogManager){
		this.syslogManager=syslogManager;
	}

	public void createSyslog(String operator,String action,String content){
		if(!operator.equals("")){
			if(action.equals("01")){
				//content="管理员登录:"+content;				
				syslogManager.addSyslog(operator, action, content);
			}else if(action.equals("02")){

				//content="网关账号维护:"+content;               
				syslogManager.addSyslog(operator, action, content);	
			}else if(action.equals("03")){

				//content="应用维护:"+content;             
				syslogManager.addSyslog(operator, action, content);	
			}else if(action.equals("04")){

				//content="系统账号维护:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}else if(action.equals("05")){

				//content="敏感词维护:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}
			else if(action.equals("06")){
			//	content="应用登录:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}
			else if(action.equals("07")){
				//content="MT发送到系统:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}
			else if(action.equals("08")){
				//content="应用发送到系统:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}
			else if(action.equals("09")){
			//	content="系统发送到电信网关:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}
			else if(action.equals("10")){
				//content="网关推送到应用:"+content;              
				syslogManager.addSyslog(operator, action, content);	
			}
		}
		/*if(!operator.equals("")){
			operator="普通用户";
			content="应用登录:"+content;               
			syslogManager.addSyslog(operator, action, content);
		}else if(operator.equals("01")){
			operator="系统管理员";
			
		}*/
	}
	


}
