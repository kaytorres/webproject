<%@ page language="java" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<script type="text/javascript" src="<c:url value="/resource/js/jquery.js"/>"></script>
	<script type="text/javascript">function url(u){return "<c:url value="/"/>" + u;}</script>
	<script type="text/javascript" src="<c:url value="/resource/js/resendsms.js"/>"></script>
	</head>
	<body >
	<input type="hidden" id="appSessionID" value="${appSessionID }"/>
	<input type="hidden" id="appID" value="${appID }"/>
	<input type="hidden" id="senderName" value="${logonName }"/>
	<input type="hidden" id="mainid" value="${mainid }"/>
	<input type="hidden" id="mobile" value="${mobile }"/>
	<table border="0" width="400px" height="400px" align="center">
	<tr style="height:40px" >
	<td width="30px" align="center">号码</td>
	<td width="15px" align="center"></td>
	<td width="240px" align="left">${mobile }
	</td>
	</tr>
	<tr style="height:200px" valign="top">
	<td width="30px" align="center">内容</td>
	<td width="270px" align="center" colspan="2">
		<textarea  id="content" style="width:300px;height:200px;resize:none;" >${content }</textarea>
	</td>
	</tr>
	<tr style="height:10px" align="center" valign="top">
		<td colspan="3"><font size=1>该短信内容可能包含敏感词,请确认！(例如：修改人名)</font>
		</td>
	</tr>
	<tr style="height:40px"  valign="top">
	<td colspan="3" align="center">
	<table >
	<tr>
	<td width="30px" ></td>
	<td  width="100px" align="center" ><input  type="button" value="发&nbsp;送" onclick="send()" style="width:60px;height:30px;" ></input></td>
	<td width="30px" ></td>
	<td  width="100px" align="right" ><input type="button" style="width:60px;height:30px;" id="close" value="取&nbsp;消" onclick="parentDialog.close();"/></td>
	<td width="30px" ></td>
	</tr>
	</table>
		
	</td>
	
	</tr>
	</table>
	</body>
</html>