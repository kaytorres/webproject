var recordTable;
var pageTag;
var di = '第';
var page = '页';
var page1 = '页，共';
var row = '行';
var previousPage = '上一页';
var nextPage = '下一页';

var appUrl = url("app");
var json;
var config_query;

$(function() {
	recordTable = new AjaxTable("recordTable", 20, 5, renderCellValue);
	recordTable.showTable();
	pageTag = new AjaxPageTag("pageRecordTag", 20, computeRange, handleResult, {
		renderInfo : function(page, pageCnt, total) {
			return di + page + '/' + pageCnt + page1 + total + row;
		},
		renderPrev : previousPage,
		renderNext : nextPage
	});

   pageTag.param = {
		"currPage" : 0,
		"pageSize" : 20
	};
  queryapp();	
});

function queryapp(){
	var cpage = pageTag.param.currPage;
	if (cpage==0)
	{
		cpage = 1;
	}
	pageTag.url = appUrl;
	pageTag.param = {
		"currPage" : cpage,
		"pageSize" : 20
	};
	$.ajax( {
		type : "post",
		url : appUrl,
		data : {
			"currPage" : cpage,
			"pageSize" : 20
		},
		success : handleResult,
		dataType : "text"
	});
}
var flag=0;
function keyinput(){
	flag=1;
	document.getElementById('showresult').innerHTML="";
}

var modifyAppUrl = url("modifyapp");
function appmodify(){
	var appid = $("#appid").val();
	var appname = $("#appname").val();
	var externo = $("#externo").val();
	var captcha = $("#captcha").val();
	var needresend = $("#needresend").val();
	var oldappid = $("#oldappid").val();
	if(flag==0){
		document.getElementById('showresult').innerHTML="提示：未修改内容！";
		return;
	}
	if(appid==""){
		document.getElementById('showresult').innerHTML="提示：应用ID不能为空！";
		return;
	}
	if(appname==""){
		document.getElementById('showresult').innerHTML="提示：名称不能为空！";
		return;
	}
	if (captcha.indexOf(" ") >= 0) {
		document.getElementById('showresult').innerHTML="提示：密码不能包含空格！";
		return;
	}
	if(oldappid==""){
		document.getElementById('showresult').innerHTML="提示：请选择要修改的应用！";
		return;
	}
	$.ajax( {
		type : "post",
		url : modifyAppUrl,
		data : {
			"appid" : appid,
			"appname" : appname,
			"externo" : externo,
			"captcha" : captcha,
			"needresend":needresend,
			"oldappid":oldappid
		},
		success : handleModifyResult,
		dataType : "text"
	});
}
function handleModifyResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		queryapp();
		flag=0;
		document.getElementById('appid').value=""; 
		document.getElementById('appname').value=""; 
		document.getElementById('externo').value=""; 
		document.getElementById('captcha').value="";
		document.getElementById('needresend').value="-1";
		document.getElementById('showresult').innerHTML="提示：修改成功！";
	}else if(json.result=="failmodify"){
		document.getElementById('showresult').innerHTML="提示：修改失败！";
	}else if(json.result=="userexist"){
		document.getElementById('showresult').innerHTML="提示：已存在相同应用ID！";
	}
}

var addApprUrl = url("addapp");
function appadd(){
	var appid = $("#appid").val();
	var appname = $("#appname").val();
	var externo = $("#externo").val();
	var captcha = $("#captcha").val();
	var needresend = $("#needresend").val();
	if(appid==""){
		document.getElementById('showresult').innerHTML="提示：应用ID不能为空！";
		return;
	}
	if(appname==""){
		document.getElementById('showresult').innerHTML="提示：名称不能为空！";
		return;
	}
	if (captcha=="") {
		document.getElementById('showresult').innerHTML="提示：密码不能为空！";
		return;
	}
	if (captcha.indexOf(" ") >= 0) {
		document.getElementById('showresult').innerHTML="提示：密码不能包含空格！";
		return;
	}
	if (needresend== "-1") {
		document.getElementById('showresult').innerHTML="提示：请选择是否可以重发！！";
		return;
	}
	$.ajax( {
		type : "post",
		url : addApprUrl,
		data : {
			"appid" : appid,
			"appname" : appname,
			"externo" : externo,
			"captcha" : captcha,
			"needresend":needresend
		},
		success : handleAddResult,
		dataType : "text"
	});
}
function handleAddResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		queryapp();
		flag=0;
		document.getElementById('appid').value=""; 
		document.getElementById('appname').value=""; 
		document.getElementById('externo').value=""; 
		document.getElementById('captcha').value="";
		document.getElementById('needresend').value="-1";
		document.getElementById('showresult').innerHTML="提示：添加成功！";
	}else if(json.result=="failadd"){
		document.getElementById('showresult').innerHTML="提示：添加失败！";
	}else if(json.result=="userexist"){
		document.getElementById('showresult').innerHTML="提示：该应用ID已存在！";
	}
}

function computeRange(page, pageCnt) {
	var begin = Math.floor((page - 1) / 20) * 20 + 1;
	var end = begin + 19;
	end = end < pageCnt ? end : pageCnt;
	return [ begin, end, "value" ];
}

function handleResult(result) {
	json = eval('(' + result + ')');
	if (json.result != null && json.result.length > 0) {
		recordTable.showData(json.result);
	} else {
		recordTable.showData([]);
	}
	pageTag.show(json.currPage, json.totalNum);
	pageTag.bind();
}

function renderCellValue(rowdata, i, index) {
	switch (index) {
	case 0:
		return '<div  align="left">' + checkDataItem(rowdata.appid) + '</div>';
	case 1:
		return '<div  align="center">' + checkDataItem(rowdata.appname) + '</div>';
	case 2:
		return '<div  align="center">' + checkDataItem(rowdata.externo) + '</div>';
	case 3:
		var str;
		if(rowdata.needresend=="1"){
			str="是";
		}else if(rowdata.needresend=="0"){
			str="否";
		}else{
			str="";
		}
		return '<div  align="center">' + str + '</div>';
	case 4:
		return '<div  align="center">' +"<a onclick='javascript:modify(\""+rowdata.appid+"\",\""+rowdata.appname+"\",\""+rowdata.externo+"\",\""+rowdata.captcha+"\");return false;' href='javascript:void(0)'>修改</a>"+"   "+"<a onclick='javascript:del(\""+rowdata.appid+"\",\""+rowdata.appname+"\");return false;' href='javascript:void(0)'>删除</a>"+"</div>";
	default:
		return "&nbsp;";
	}
}

function modify(appid,appname,externo,captcha){
	flag=0;
	document.getElementById('appid').value=appid; 
	document.getElementById('oldappid').value=appid; 
	document.getElementById('appname').value=appname; 
	document.getElementById('externo').value=externo; 
	document.getElementById('captcha').value="";
	document.getElementById('needresend').value="-1";
	document.getElementById('showresult').innerHTML="";
}

var delAppUrl = url("delapp");
function del(appid,appname){
	var str="确认删除    "+appname+"?";
	if(!confirm(str)){
		return;
	}
	$.ajax( {
		type : "post",
		url : delAppUrl,
		data : {
			"appid" : appid
		},
		success : handleDelResult,
		dataType : "text"
	});
}
function handleDelResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		flag=0;
		queryapp();
		document.getElementById('appid').value=""; 
		document.getElementById('appname').value=""; 
		document.getElementById('externo').value=""; 
		document.getElementById('captcha').value="";
		document.getElementById('needresend').value="-1";
		document.getElementById('showresult').innerHTML="提示：删除成功！";
	}else if(json.result=="fail"){
		document.getElementById('showresult').innerHTML="提示：删除失败！";
		
	}
}
