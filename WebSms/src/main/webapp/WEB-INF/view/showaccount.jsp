<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<c:url value="/resource/js/jquery.js"/>"></script>
<script type="text/javascript">function url(u){return "<c:url value="/"/>" + u;}</script>
<script type="text/javascript" src="<c:url value="/resource/js/account.js"/>"></script>

<title>神计短信系统·网关维护</title>
</head>
<body onload="javascript:document.getElementById('account').focus();">
<input type="hidden" id="appSessionID" value="${appSessionID }"/>
<input type="hidden" id="appID" value="${appID }"/>
<input type="hidden" id="oldaccountname" value="${lstaccountname }"/>
<table  align="center" width="1280px" height="800px" style="border:1px solid #000000;" cellpadding="0" cellspacing="0" >
<tr  style="height:10%;background:url(/resource/image/lab3.png)"    >
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
<table height="692" width="154" border="0">
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
<tr style="height:8%">
<td>
<a href="<c:url value="/showsyslog"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;日志查询</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showusers"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;用户维护</a>
</td>
</tr>
<tr style="height:8%" bgcolor="#CCCCCC">
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
<td width="1126px" align="center">
<table align="center" height="692px" width="1126px" style="border:0px solid #000000;" cellpadding="0" cellspacing="0px" >
<tr style="height:60px"><td></td></tr>
<tr valign="top">
<td align="center">
<table	align="center" height="400px" width="640px"  cellpadding="0" cellspacing="0px" style="background:url(/resource/image/accountground.png)">
<tr style="height:70px">
<td width="640px" align="center" colspan="4">
  <font size="6"> 短信账号维护 </font>
</td>
</tr>
<tr style="height:10px"> <td colspan="4" align="center"> <img src="<c:url value="/resource/image/sendline.png"/>"></img></td> </tr>

	<tr style="height:80px">
		<td width="70px" align="right" >
			<font size="2">账号&nbsp; </font>
		</td>
		<td width="200px" align="left" >
			<input type="text" id="account" name="account"  value=${lstaccountname }  style="width:200px;height:25px;" onkeydown="keyinput()" ></input>
		</td>
		<td width="60px" align="right">
			<font size="2">密码&nbsp; </font>
		</td>
		<td width="200px" align="left" >
			<input type="text" id="password" name="password"  value=${lstaccountpassword } style="width:200px;height:25px;" onkeydown="keyinput()" ></input>
		</td>
	</tr>
	<tr style="height:80px">
		<td width="70px" align="right">
			<font size="2">短信网关URL&nbsp;</font>
		</td>
		<td width="200px" align="left" >
			<input type="text" id="gwurl" name="gwurl"  value=${lstaccountgwurl } style="width:200px;height:25px;" onkeydown="keyinput()" ></input>
		</td>
		<td width="60px" align="right">
			<font size="2">3des密码 &nbsp;</font>
		</td>
		<td width="200px" align="left" >
			<input type="text" id="despassword" name="despassword" style="width:200px;height:25px;" value=${lstaccount3despassword } onkeydown="keyinput()"></input>
		</td>
	</tr>
	<tr style="height:80px">
		<td width="70px" align="right">
		  <font size="2">用户名&nbsp;</font>
		</td>
		<td width="200px" align="left" >
			<input type="text" id="name" name="name" readonly style="width:200px;height:25px;"  value=${lstaccountusername } onkeydown="keyinput()"></input>
		</td>
		<td width="60px" align="right">
			<font size="2">余额 &nbsp;</font>
		</td>
		<td width="200px" align="left" >
			<input type="text" id="balance" readonly name="balance" style="width:200px;height:25px;" value=${balance } onkeydown="keyinput()"></input>
		</td>
	</tr>
	<tr style="height:80px">
		<td width="400px" align="right" colspan="3"  id="showresult" style="color:#EE2C2C;">
		</td>
		<td width="200px" align="center" >
			<input type="button" value="保存" onclick="saveaccount()" style="font-size:20px;width:70px;height:36px;color:#FCFCFC;background:url(/resource/image/sendbutton.png);border-style:none;"/>
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