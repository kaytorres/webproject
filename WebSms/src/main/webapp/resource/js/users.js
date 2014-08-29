var recordTable;
var pageTag;
var di = '第';
var page = '页';
var page1 = '页，共';
var row = '行';
var previousPage = '上一页';
var nextPage = '下一页';

var userUrl = url("user");
var json;
var config_query;

$(function() {
	recordTable = new AjaxTable("recordTable", 20, 4, renderCellValue);
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
  queryuser();	
});

function queryuser(){
	var cpage = pageTag.param.currPage;
	if (cpage==0)
	{
		cpage = 1;
	}
	pageTag.url = userUrl;
	pageTag.param = {
		"currPage" : cpage,
		"pageSize" : 20
	};
	$.ajax( {
		type : "post",
		url : userUrl,
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

var modifyUserUrl = url("modifyuser");
function usermodify(){
	var account = $("#account").val();
	var name = $("#name").val();
	var password = $("#password").val();
	var roleid = $("#roleid").val();
	var oldaccount = $("#oldaccount").val();
	if(flag==0){
		document.getElementById('showresult').innerHTML="提示：未修改内容！";
		return;	
	}
	if(account==""){
		document.getElementById('showresult').innerHTML="提示：登录名不能为空！";
		return;
	}
	if(name==""){
		document.getElementById('showresult').innerHTML="提示：姓名不能为空！";
		return;
	}
	if(oldaccount==""){
		document.getElementById('showresult').innerHTML="提示：请选择要修改的用户！";
		return;
	}
	if (password.indexOf(" ") >= 0) {
		document.getElementById('showresult').innerHTML="提示：密码不能包含空格！";
		return;
	}
	$.ajax( {
		type : "post",
		url : modifyUserUrl,
		data : {
			"account" : account,
			"name" : name,
			"password" : password,
			"roleid" : roleid,
			"oldaccount":oldaccount
		},
		success : handleModifyResult,
		dataType : "text"
	});
}
function handleModifyResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		flag=0;
		queryuser();
		document.getElementById('account').value=""; 
		document.getElementById('name').value=""; 
		document.getElementById('password').value=""; 
		document.getElementById('roleid').value="-1";
		document.getElementById('showresult').innerHTML="提示：修改成功！";
	}else if(json.result=="failmodify"){
		document.getElementById('showresult').innerHTML="提示：修改失败！";	
	}else if(json.result=="userexist"){
		document.getElementById('showresult').innerHTML="提示：已存在相同账号！";
	}
}

var addUserUrl = url("adduser");
function useradd(){
	var account = $("#account").val();
	var name = $("#name").val();
	var password = $("#password").val();
	var roleid = $("#roleid").val();
	if(account==""){
		document.getElementById('showresult').innerHTML="提示：登录名不能为空！";
		return;
	}
	if(name==""){
		document.getElementById('showresult').innerHTML="提示：姓名不能为空！";
		return;
	}
	if (password=="") {
		document.getElementById('showresult').innerHTML="提示：密码不能为空！";
		return;
	}
	if (password.indexOf(" ") >= 0) {
		document.getElementById('showresult').innerHTML="提示：密码不能包含空格！";
		return;
	}
	$.ajax( {
		type : "post",
		url : addUserUrl,
		data : {
			"account" : account,
			"name" : name,
			"password" : password,
			"roleid" : roleid
		},
		success : handleAddResult,
		dataType : "text"
	});
}
function handleAddResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		flag=0;
		queryuser();
		document.getElementById('account').value=""; 
		document.getElementById('name').value=""; 
		document.getElementById('password').value=""; 
		document.getElementById('roleid').value="-1";
		document.getElementById('showresult').innerHTML="提示：添加成功！";
	}else if(json.result=="failadd"){
		document.getElementById('showresult').innerHTML="提示：添加失败！";	
	}else if(json.result=="userexist"){
		document.getElementById('showresult').innerHTML="提示：该账户已存在！";
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
		return '<div  align="center">' + checkDataItem(rowdata.account) + '</div>';
	case 1:
		return '<div  align="center">' + checkDataItem(rowdata.name) + '</div>';
	case 2:
		if(rowdata.roleid == "01")
	    	str = "管理员";
	    if(rowdata.roleid == "02")
	    	str = "普通用户";
	    if(rowdata.roleid == "-1")
	    	str = "";
		return '<div  align="center">' + str + '</div>';
	case 3:
		return '<div  align="center">' +"<a onclick='javascript:modify(\""+rowdata.account+"\",\""+rowdata.name+"\",\""+rowdata.password+"\",\""+rowdata.roleid+"\");return false;' href='javascript:void(0)'>修改</a>"+"   "+"<a onclick='javascript:del(\""+rowdata.account+"\",\""+rowdata.name+"\");return false;' href='javascript:void(0)'>删除</a>"+"</div>";
	default:
		return "&nbsp;";
	}
}

function modify(account,name,password,roleid){
	flag=0;
	document.getElementById('account').value=account; 
	document.getElementById('oldaccount').value=account; 
	document.getElementById('name').value=name; 
	document.getElementById('password').value=""; 
	document.getElementById('roleid').value=roleid;
	document.getElementById('showresult').innerHTML="";
}

var delUserUrl = url("deluser");
function del(account,name){
	var oldaccount = $("#oldaccount").val();
	if(account==oldaccount){
		document.getElementById('showresult').innerHTML="提示：不可删除自身！";
	}else{
		document.getElementById('showresult').innerHTML="";
		var str="确认删除    "+name+"?";
		if(!confirm(str)){
			return;
		}
		$.ajax( {
			type : "post",
			url : delUserUrl,
			data : {
				"account" : account
			},
			success : handleDelResult,
			dataType : "text"
		});
	}
}
function handleDelResult(result){
	json = eval('(' + result + ')');
	if(json.result=="success"){
		flag=0;
		queryuser();
		document.getElementById('account').value=""; 
		document.getElementById('name').value=""; 
		document.getElementById('password').value=""; 
		document.getElementById('roleid').value="-1";
		document.getElementById('showresult').innerHTML="提示：删除成功！";
	}else if(json.result=="fail"){
		document.getElementById('showresult').innerHTML="提示：删除失败！";
		
	}
}
