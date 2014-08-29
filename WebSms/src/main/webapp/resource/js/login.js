$(function(){
	$("#admin").focus();
	var login_statuspw = $("#login-statuspw").val();
	var login_statusrole = $("#login-statusrole").val();
	var login_statusnouser = $("#login-statusnouser").val();
	if(login_statusnouser=="fail"){
		document.getElementById('showresult').innerHTML="账号不存在";	
	}else if(login_statusrole=="fail"){
		document.getElementById('showresult').innerHTML="请用管理员账号";
	}else if(login_statuspw=="fail"){
		document.getElementById('showresult').innerHTML="密码错误";
	}
})

function keyinput(){
	document.getElementById('showresult').innerHTML="";
}
	