var accountUrl = url("account");
function saveaccount(){
	var account = $("#account").val();
	var password = $("#password").val();
	var gwurl = $("#gwurl").val();
	var despassword = $("#despassword").val();
	var name = $("#name").val();
	var oldaccountname = $("#oldaccountname").val();
	if (password.indexOf(" ") >= 0) {
		document.getElementById('showresult').innerHTML="提示：密码不能包含空格！";
		return;
	}
	if (account== "") {
		document.getElementById('showresult').innerHTML="提示：账号不能为空！";
		return;
	}
	if (password== "") {
		document.getElementById('showresult').innerHTML="提示：密码不能为空！";
		return;
	}
	if (gwurl=="") {
		document.getElementById('showresult').innerHTML="提示：网关URL不能为空！";
		return;
	}
	if (despassword== "") {
		document.getElementById('showresult').innerHTML="提示：3des密码不能为空！";
		return;
	}
	if(flag=="0"){
		document.getElementById('showresult').innerHTML="提示：未修改内容！";
		return;
	}
	$.ajax( {
		type : "post",
		url : accountUrl,
		data : {
			"account" : account,
			"password" : password,
			"gwurl" : gwurl,
			"despassword" : despassword,
			"name" : name,
			"oldaccountname":oldaccountname
		},
		success : handleResult,
		dataType : "text"
	});
}

function handleResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		flag=0;
		queryaccount();
		document.getElementById('showresult').innerHTML="提示：修改成功！";
	}else{
		flag=0;
		queryaccount();
		document.getElementById('showresult').innerHTML="提示：修改失败！";
	}
}
var queryaccountUrl = url("queryaccount");
function queryaccount(){
	$.ajax( {
		type : "post",
		url : queryaccountUrl,
		data : {
		},
		success : handleQueryResult,
		dataType : "text"
	});
}
function handleQueryResult(result){
	json = eval('(' + result + ')');
	if(json!=undefined&&json.result.length>0){
		document.getElementById('account').value=json.result[0].name;	
		document.getElementById('password').value=json.result[0].password;
		document.getElementById('gwurl').value=json.result[0].url;
		document.getElementById('despassword').value=json.result[0].passwordfor3des;
		document.getElementById('oldaccountname').value=json.result[0].name;
		}
}
var flag=0;
function keyinput(){
	flag=1;
	document.getElementById('showresult').innerHTML="";
}