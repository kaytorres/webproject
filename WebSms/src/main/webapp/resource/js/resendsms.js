var resendUrl = url("resendsms");
function send(){
	var mobile = $("#mobile").val();
	var content = $("#content").val();
	var mainid = $("#mainid").val();
	var sender = $("#senderName").val();
	if(content==""){
		document.getElementById('showresult').innerHTML="提示：发送内容不可为空！";
		return;
	}
	$.ajax( {
		type : "post",
		url : resendUrl,
		data : {
			"mobile" : mobile,
			"content" : content,
			"mainid" : mainid,
			"sender" : sender
		},
		success : handleResult,
		dataType : "text"
	});
}

function handleResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		window.parent.querySendHis();	
		parentDialog.close();
	}else if(json.result=="fail"){
		alert("发送失败");
		parentDialog.close();
	}
}