<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<c:url value="/resource/js/jquery.js"/>"></script>
<script type="text/javascript">function url(u){return "<c:url value="/"/>" + u;}</script>
    
<html>
	<head>
		<title>结果显示</title>
	</head> 
	<body>
		<c:out value="${model.returncode}"/>
	</body>
</html>
