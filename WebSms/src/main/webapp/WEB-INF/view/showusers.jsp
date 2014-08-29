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
<script type="text/javascript" src="<c:url value="/resource/js/users.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resource/js/zDialog/zDialog.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resource/js/zDialog/zDrag.js"/>"></script>

<title>神计短信系统·用户维护</title>
</head>
<body >
<input type="hidden" id="appSessionID" value="${appSessionID }"/>
<input type="hidden" id="oldaccount" value="${oldaccount }" />
<input type="hidden" id="appID" value="${appID }"/>
<table  align="center" width="1280px"  style="border:1px solid #000000;height:800px" cellpadding="0" cellspacing="0" >
<tr style="height:10%;background:url(/resource/image/lab3.png)"   >
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
<table height="692px" width="154" border="0">
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
<tr style="height:8%" bgcolor="#CCCCCC">
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
<td width="1126px" align="center">
<table align="center" height="692px" width="1126px" style="border:0px solid #000000;" cellpadding="0" cellspacing="0px" >
<tr >
<td align="center" >
<table height="680px" align="center" width="100%">
<tr style="height:20px">
<td width="690px"></td>
<td align="left" width="180px" id="showresult" style="color:#EE2C2C;"></td>
</tr>
<tr style="height:30px">
<td width="100%" colspan="2">
<table height="50px" width="870px">
<tr>
<td width="100px"></td>
<td width="40px">账号</td>
<td width="100px"><input type="text" id="account" name="account" style="width:80px" onkeydown="keyinput()" /></td>
<td width="40px">姓名</td>
<td width="100px"><input type="text" id="name" name="name" style="width:80px" onkeydown="keyinput()" /></td>
<td width="40px">密码</td>
<td width="100px"><input type="password" id="password" name="password" style="width:80px" onkeydown="keyinput()" /></td>
<td width="40px">角色</td>
<td width="100px">
<select name="roleid" id="roleid" style="width:80px" style="visibility:hidden;"  onchange="keyinput()">
			<option value="-1" >-请选择-</option>
			<option value="02" <c:if test="${roleid=='02'}">selected="selected"</c:if>>普通用户</option>
			<option value="01" <c:if test="${roleid=='01'}">selected="selected"</c:if>>管理员</option>	
		</select>
</td>
<td width="100px"></td>
<td align="center" width="60px">
		<input type="button" value="修&nbsp;改" onclick="usermodify()" style="width:50px;height:25px;color:#FCFCFC;background:url(/resource/image/button.png);border-style:none;"></input>
</td>
<td align="center" width="80px">
</td>	
<td align="center" width="60px">
		<input type="button" value="新&nbsp;增" onclick="useradd()" style="width:50px;height:25px;color:#FCFCFC;background:url(/resource/image/button.png);border-style:none;"></input>
</td>
<td align="center" width="20px">
</td>			
</tr>
</table>
</td>
</tr>
<tr style="height:600px" valign="top" >
<td width="850px"  colspan="2">
<table style="WIDTH: 100%; BORDER-COLLAPSE: separate" border="0" cellSpacing="0" cellPadding="0">
					<tr  valign="top" style="height:50px">
						<td id="print_area" >
							<table id="recordTable"
								style="WIDTH: 100%; BORDER-COLLAPSE: collapse; EMPTY-CELLS: show;"
								border="0" cellSpacing="0" cellPadding="0" >
								<thead>
									<tr>
										<th width="220px" class="dxgvHeader_Aqua">
											账号
										</th>
										<th width="220px" class="dxgvHeader_Aqua">
											姓名
										</th>
										<th width="220px" class="dxgvHeader_Aqua">
											角色
										</th>
										<th width="200px" class="dxgvHeader_Aqua">
											操作
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