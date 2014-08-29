<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<c:url value="/resource/js/jquery.js"/>"></script>
<script type="text/javascript">function url(u){return "<c:url value="/"/>" + u;}</script>
<script type="text/javascript" src="<c:url value="/resource/js/login.js"/>"></script>

<title>神计短信系统·登录</title>
</head>
<body onload="javascript:document.getElementById('loginname').focus();">
<table  align="center" width="800px" height="500px" style="border:0px solid #000000;" cellpadding="0" cellspacing="1" >
<tr style="height:500px">
<td width="800px" background="<c:url value="/resource/image/background.png"/>">
<%-- <img  src="<c:url value="/resource/image/background.png"/>"  border="0" width="100%" height="100%" /> --%>
<table width="800px" height="500px" >
<tr style="height:500px">
<td width="500px">

<table width="500px" height="500px" >
<tr style="height:100px"><td width="500px" colspan="3" align="center"><font size="8">神计信息短信管理系统</font></td></tr>
<tr style="height:330px">
<td width="50px" align="center"></td> 
<td width="400px" align="center" >
	<form method="post" action="<c:url value="/authuser"/>">
	<table   cellspacing="0" cellpadding="0%" align="center" width="300px" height="218px" background="<c:url value="/resource/image/formground.png"/>"> 
	<tr >
		<td width="300px" height="30px"  colspan="4" align="center">
			<font size="5">系统登录</font>
			<input type="hidden" id="login-statuspw" value="${loginstatuspw }"/>
			<input type="hidden" id="login-statusrole" value="${loginstatusrole }"/>
			<input type="hidden" id="login-statusnouser" value="${loginstatusnouser }"/>
		</td>
	</tr>
	<tr>
		<td width="30px" height="30px" align="center">
			
		</td>
		<td width="100px" height="30px" align="center">
			<font size="5">账&nbsp;&nbsp;号&nbsp;</font>
		</td>
		<td width="150px" height="30px">
			<input type="text" id="loginname" name="loginname" style="font-size:18px;width:120px;height:30px;border-style:1px;border-color:#6CA6CD;"  onkeydown="keyinput()" ></input>
		</td>
		<td width="20px" height="30px" align="center">
			
		</td>
	</tr>
	<tr>
		<td width="30px" height="30px" align="center">
			
		</td>
		<td width="100px" height="30px" align="center">
			<font size="5">密&nbsp;&nbsp;码&nbsp;</font>
		</td>
		<td width="150px" height="30px">
			<input type="password" id="password" name="password" style="font-size:16px;width:120px;height:30px;border-style:1px;border-color:#6CA6CD;"  onkeydown="keyinput()"></input>
		</td>
		<td width="20px" height="30px" align="center">
			
		</td>
	</tr>
	<tr>
		<td width="130px" height="30px" colspan="2" id="showresult" align="right" style="color:#EE2C2C;">
			
		</td>
		<td width="150px" height="52px"   align="center" >
			<input type="submit"  value="&nbsp;登&nbsp;录&nbsp;" style="font-size:20px;color:#FCFCFC;width:80px;height:30px;border-style:none;background:url(/resource/image/buttonground.png)"   ></input>
		</td>
		<td width="20px" height="30px" >
			
		</td>
	</tr>
	</table>
  </form>
</td> 
<td width="50px" align="center"></td> 
</tr>
<tr style="height:20px"><td width="500px" colspan="3"></td></tr>
</table>
 </td>


<td width="300px"><img src="<c:url value="/resource/image/ground.png"/>"></img></td>
</tr>
</table>

 

</td>
</tr>
<tr style="height:100px">

<td width="800px">
<table width="800px" height="100px" >
<tr style="height:10px">
<td	width="800px" colspan="2">
<img src="<c:url value="/resource/image/image2.png"/>"></img>
</td>
</tr>
<tr style="height:90px" >
<td	width="80px" valign="top">
<img src="<c:url value="/resource/image/image1.jpg"/>"></img>
</td>
<td	width="720px" >
	<table width="720px" height="90px">
	<tr	style="height:20px"><td align="left"></td></tr>
	<tr	style="height:20px"><td align="left"><font size="4">·请使用1024x768分辨率浏览</font></td></tr>
	<tr style="height:20px"><td align="left"><font size="4">·您登录系统后，如需退出，请点击"退出"</font></td></tr>
	<tr style="height:20px"><td align="left"><font size="4">·如果疑问，请联系我们</font></td></tr>
	<tr	style="height:10px"><td align="left"></td></tr>
	</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
 
</body>
</html>