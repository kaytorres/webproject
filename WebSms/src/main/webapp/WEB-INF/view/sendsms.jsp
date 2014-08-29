<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<c:url value="/resource/js/jquery.js"/>"></script>
<script type="text/javascript">function url(u){return "<c:url value="/"/>" + u;}</script>
<script type="text/javascript" src="<c:url value="/resource/js/send.js"/>"></script>

<title>神计短信系统·发送短信</title>
</head>
<body onload="javascript:document.getElementById('mobiles').focus();">
<input type="hidden" id="appSessionID" value="${appSessionID }"/>
<input type="hidden" id="appID" value="${appID }"/>
<input type="hidden" id="senderName" value="${logonName }"/>
<input type="hidden" id="roleid" value="${roleid }"/>
<table  align="center" width="1280px" height="800px"  style="border:1px solid #000000;" cellpadding="0" cellspacing="0px" >
<tr style="height:10%;background:url(/resource/image/lab3.png)"   >
<td colspan="2" width="1280px" align="center" >
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
<tr style="height:8%" bgcolor="#CCCCCC">
<td >
<a href="<c:url value="/sendsms"/>" style="text-decoration:none;font-size:20px;color:black;"  >&nbsp;短信发送</a>
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
<%if(("01").equals(session.getAttribute("role"))){ %>
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
<tr style="height:8%">
<td>
<a href="<c:url value="/showaccount"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;短信账号</a>
</td>
</tr>
<tr style="height:8%">
<td>
<a href="<c:url value="/showapp"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;应用维护</a>
</td>
</tr>
<tr style="height:8%" >
<td>
<a href="<c:url value="/showsenword"/>" style="text-decoration:none;font-size:20px;color:black;" >&nbsp;敏感词维护</a>
</td>
</tr>
<%} %>
<tr style="height:36%">
<td>
<font size="4" ></font>
</td>
</tr>
</table>
</td>
<td width="85%" align="center">
<form>
<table align="center" height="692px" width="1126px" style="border:0px solid #000000;" cellpadding="0" cellspacing="0px" >
<tr style="height:60px"><td></td></tr>
<tr valign="top">
<td align="center">
<table	align="center" height="400px" width="600px"  cellpadding="0" cellspacing="0px" style="background:url(/resource/image/sendground.png)">
	 <tr style="height:90px" > <td colspan="3" align="center"> <font size="6">短信发送</font></td></tr>
	 <tr style="height:10px"> <td colspan="3" align="center"> <img src="<c:url value="/resource/image/sendline.png"/>"></img></td> </tr>
	<tr style="height:70px">  
	 <td width="150px" align="center"><font size="4">对方手机号</font> </td>
	 <td width="450px" align="left" colspan="2">
	 <!--  <textarea  id="mobiles" style="width:400px;height:40px;resize:none;" onkeydown="keyinput()"></textarea> -->
	 <input id="mobiles" style="width:400px;height:40px;resize:none;" onkeydown="keyinput()"></input>
	  </td>
	</tr>
	<tr style="height:30px">  
	<td width="150px" align="center"><font size="4"></font> </td>
	<td width="450px" align="left" colspan="2"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;如果有多个手机号，请用逗号隔开</font> </td>
	</tr>
	<tr style="height:120px">  
	<td width="150px" align="center"><font size="4">内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容</font> </td>
	 <td width="450px" align="left" colspan="2"><textarea  id="content" style="width:400px;height:120px;resize:none;" onkeydown="keyinput()"></textarea></td>
	</tr>
	<tr style="height:60px">  
	<td colspan="2" align="right">
	<table>
	<tr>
	<td width="450px" align="right" id="showresult" style="color:#EE2C2C;"></td>
	<td width="50px" align="center"></td>
	<td width="100px">
	<input id="button" class="field-button" type="button" value="发&nbsp;&nbsp;送" onclick="send()" style="font-size:20px;width:70px;height:36px;color:#FCFCFC;background:url(/resource/image/sendbutton.png);border-style:none;"/></td>
	
	</td>
	</tr>
	</table>
	 <td width="50px"></td>
	</tr> 
</table>
</td>
</tr>
	
</table>
</form>

















</td>
</tr>
</table>

</body>
</html>