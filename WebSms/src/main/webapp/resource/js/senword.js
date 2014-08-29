var recordTable;
var pageTag;
var di = '第';
var page = '页';
var page1 = '页，共';
var row = '行';
var previousPage = '上一页';
var nextPage = '下一页';

var senwordUrl = url("senword");
var json;
var config_query;

$(function() {
	recordTable = new AjaxTable("recordTable", 20, 3, renderCellValue);
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
	pageTag.url = senwordUrl;
	pageTag.param = {
		"currPage" : cpage,
		"pageSize" : 20
	};
	$.ajax( {
		type : "post",
		url : senwordUrl,
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

var modifySenwordUrl = url("modifysenword");
function senwordmodify(){
	var senwordid = $("#senwordid").val();
	var senwordcontent = $("#senwordcontent").val();
	var oldsenwordid = $("#oldsenwordid").val();
	if(flag==0){
		document.getElementById('showresult').innerHTML="提示：未修改内容！";
		return;
	}
	if(senwordcontent==""){
		document.getElementById('showresult').innerHTML="提示：应用ID不能为空！";
		return;
	}
	if(oldsenwordid==""){
		document.getElementById('showresult').innerHTML="提示：请选择要修改的应用！";
		return;
	}
	$.ajax( {
		type : "post",
		url : modifySenwordUrl,
		data : {
			"senwordid" : senwordid,
			"senwordcontent" : senwordcontent,
			"oldsenwordid" : oldsenwordid
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
		document.getElementById('senwordid').value=""; 
		document.getElementById('senwordcontent').value=""; 
		document.getElementById('showresult').innerHTML="提示：修改成功！";
	}else if(json.result=="failmodify"){
		document.getElementById('showresult').innerHTML="提示：修改失败！";
	}
}

var addSenWordrUrl = url("addsenword");
function senwordadd(){
	var senwordcontent = $("#senwordcontent").val();
	if(senwordcontent==""){
		document.getElementById('showresult').innerHTML="提示：内容不能为空！";
		return;
	}
	$.ajax( {
		type : "post",
		url : addSenWordrUrl,
		data : {
			"senwordcontent" : senwordcontent
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
		document.getElementById('senwordid').value=""; 
		document.getElementById('senwordcontent').value=""; 
		document.getElementById('showresult').innerHTML="提示：添加成功！";
	}else if(json.result=="failadd"){
		document.getElementById('showresult').innerHTML="提示：添加失败！";
	}
	/*else if(json.result=="userexist"){
		document.getElementById('showresult').innerHTML="提示：该应用ID已存在！";
	}*/
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
		return '<div  align="center">' + checkDataItem(rowdata.id) + '</div>';
	case 1:
		return '<div  align="left">' + checkDataItem(rowdata.word) + '</div>';
	case 2:
		return '<div  align="center">' +"<a onclick='javascript:modify(\""+rowdata.id+"\",\""+rowdata.word+"\");return false;' href='javascript:void(0)'>修改</a>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+"<a onclick='javascript:del(\""+rowdata.id+"\");return false;' href='javascript:void(0)'>删除</a>"+"</div>";
	default:
		return "&nbsp;";
	}
}

function modify(id,word){
	flag=0;
	document.getElementById('senwordid').value=id; 
	document.getElementById('oldsenwordid').value=id; 
	document.getElementById('senwordcontent').value=word; 
	document.getElementById('showresult').innerHTML="";
}

var delSenwordUrl = url("delsenword");
function del(senwordid){
	$.ajax( {
		type : "post",
		url : delSenwordUrl,
		data : {
			"senwordid" : senwordid
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
		document.getElementById('senwordid').value=""; 
		document.getElementById('senwordcontent').value=""; 
		document.getElementById('showresult').innerHTML="提示：删除成功！";
	}else if(json.result=="fail"){
		document.getElementById('showresult').innerHTML="提示：删除失败！";
	}else if(json.result=="nothisid"){
		document.getElementById('showresult').innerHTML="提示：不存在该敏感词！";
	}
}


function submitfile(){
	var file=document.getElementById('file').value;
	if(file.contains(".")){
		var index=file.lastIndexOf(".");
		var fileType=file.substring(index+1);
		if(fileType!="txt"){
			document.getElementById('showupload').innerHTML="提示：请上传txt！";
			document.getElementById("file").value=null;
		    window.document.getElementById("submit").disabled=true;
		    return;
		}else{
			document.getElementById('showupload').innerHTML=file;
			 window.document.getElementById("submit").disabled=false;
		}
			
	}else{
		document.getElementById('showupload').innerHTML="提示：请上传txt！";
		document.getElementById("file").value=null;
		window.document.getElementById("submit").disabled=true;
		return;
	}
	
		
	
}
