<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<c:url value="/resource/js/jquery.js"/>"></script>
<script type="text/javascript">function url(u){return "<c:url value="/"/>" + u;}</script>
<link href="<c:url value="/resource/css/style/TeamEngine.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value="/resource/js/AjaxTable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resource/js/AjaxPageTag.js"/>"></script>
<script language="javascript" type="text/javascript" src="<c:url value="/resource/js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resource/js/syslog.js"/>"></script>

<title>神计短信系统·日志查询</title>
</head>
<body >
<input type="hidden" id="appSessionID" value="${appSessionID }"/>
<input type="hidden" id="appID" value="${appID }"/>
<table  align="center" width="1280px" height="800px" style="border:1px solid #000000;" cellpadding="0" cellspacing="0" >
<tr style="height:10%;background:url(/resource/image/lab3.png)"    >
<td colspan="2"  width="1280px" align="center">
<table border="0" width="1280px" align="center">
<tr>
<td width="60%" align="left">
<font size="6">&nbsp;&nbsp;神计信息短信管理系统</font>
</td>
<td width="20%" align="center">
<font size="2">剩余  ${balance } 条</font>
</td>
<td width="10%" align="center">
<font size="4"> ${logonName }</font>
</td>
<td width="10%" align="center">
<a href="<c:url value="/loginout"/>" style="text-decoration:none;font-size:18px;color:#5CACEE;" >退出</a>
</td>
</tr>
</table>
</td>
</tr>
<tr style="height:90%;valign:top">
<td width="15%" style="background:url(/resource/image/lab1.png)" valign="top">
<table height="692px" width="154px" border="0">
<tr style="height:8%" >
<td >
<a href="<c:url value="/sendsms"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;短信发送</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showsendhistory"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;发送短信查询</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showrechistory"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;接收短信查询</a>
</td>
</tr>
<tr style="height:8%" bgcolor="#CCCCCC">
<td>
<a href="<c:url value="/showsyslog"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;日志查询</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showusers"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;用户维护</a>
</td>
</tr>
<tr style="height:8%" >
<td>
<a href="<c:url value="/showaccount"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;短信账号</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showapp"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;应用维护</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showsenword"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;敏感词维护</a>
</td>
</tr>
<tr style="height:36%">
<td>
<font size="4" ></font>
</td>
</tr>
</table>
</td>
<td width="870px" align="center">
<table align="center" height="692px" width="1126px" style="border:0px solid #000000;" cellpadding="0" cellspacing="0px" >
<tr >
<td align="center">
<table height="680px" align="center" width="100%" style="border:0px solid #000000;" cellpadding="0" cellspacing="0px">
<tr style="height:50px">
<td width="100%">
<table height="50px" width="1126px" style="border:0px solid #000000;" cellpadding="0" cellspacing="0px">
<tr style="height:50px">
    <td align="right" width="70px">
		开始日期
    </td>
    <td width="80px">
		<input type="text" id="starttime" class="Wdate" value="${Todaystarttime}" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});"/>
	</td>
	<td align="center" width="7px">
	</td>
	<td align="right" width="70px">
		截止日期
    </td>				
	<td width="80px">
		<input type="text" id="endtime" class="Wdate" value="${Todayendtime}" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});"/>
	</td>
	<td align="right" width="50px">操作人</td>
	<td align="left" width="100px" valign="middle">
		 <form name="select" >
			<select id="selectname"  style="width: 100px">
		 		<option selected="selected" value="-1">请选择</option>
				<c:forEach items="${syslogOperatorlist}" var="user">
		    	 <option value="${user.operator }">${user.operator }</option>
				</c:forEach>
			 </select>
		 </form>
	</td>
	<td align="center" width="7px"></td>
	<td align="right" width="70px">操作类型</td>
	<td width="80px">
		<select id="opcode">
			<option value="-1">所有</option>
			<option value="01">系统登录</option>
		    <option value="02">网关维护</option>
			<option value="03">应用维护</option>
			<option value="04">账号维护</option>
			<option value="05">敏感词维护</option>
		</select>
	</td>
		<td align="center" width="10px"></td>
	<td align="left" width="50px">
			<input type="button" value="查&nbsp;询" onclick="querysyslog()" style="width:50px;height:25px;color:#FCFCFC;background:url(/resource/image/button.png);border-style:none;"></input>
	</td>

</tr>
</table>
</td>
</tr>
<tr style="height:600px" valign="top" >
<td width="85px">
<table style="WIDTH: 100%; BORDER-COLLAPSE: separate" border="0" cellSpacing="0" cellPadding="0">
					<tr  valign="top" style="height:50px">
						<td id="print_area" >
							<table id="recordTable"
								style="WIDTH: 100%; BORDER-COLLAPSE: collapse; EMPTY-CELLS: show;"
								border="0" cellSpacing="0" cellPadding="0" >
								<thead>
									<tr>
										<th width="150px" class="dxgvHeader_Aqua">
											操作人
										</th>
										<th width="150px" class="dxgvHeader_Aqua">
											操作类型
										</th>
										<th width="150px" class="dxgvHeader_Aqua">
											操作时间
										</th>
										<th width="410px" class="dxgvHeader_Aqua">
											操作内容
										</th>
										
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
							
							<div id="pageRecordTag" align="center" class="dxgvControl_Div">
								
							</div>
						</td>
					</tr>
				</table>
</td>
</tr>
</table>
</td></tr>
</table>




</td>
</tr>
</table>

</body>
</html>