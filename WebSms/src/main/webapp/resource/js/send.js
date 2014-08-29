/*$(function(){
	$("#logonname").focus();
});
String.prototype.isEmpty = function() {
	return /^\s*$/.test(this);
}*/
var sendUrl = url("send");

function send(){
	var mobiles = $("#mobiles").val();
	var content = $("#content").val();
	//var	senderName= $("#senderName").val();
	if(mobiles==""){
		document.getElementById('showresult').innerHTML="提示：手机号不可为空！";
		return;
	}
	if(content==""){
		document.getElementById('showresult').innerHTML="提示：发送内容不可为空！";
		return;
	}
	$.ajax( {
		type : "post",
		url : sendUrl,
		data : {
			"mobiles" : mobiles,
			"content" : content
			//"senderName":senderName
		},
		success : handleResult,
		dataType : "text"
	});
}

function handleResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		document.getElementById('mobiles').value=""; 
		document.getElementById('content').value=""; 
		document.getElementById('showresult').innerHTML="提示：发送成功！";
	}else if(json.result=="fail"){
		document.getElementById('showresult').innerHTML="提示：发送失败！";
	}
}

function keyinput(){
	document.getElementById('showresult').innerHTML="";
}