var recordTable;
var pageTag;
var di = '第';
var page = '页';
var page1 = '页，共';
var row = '行';
var previousPage = '上一页';
var nextPage = '下一页';

var sendhistoryUrl = url("sendhistory");
var json;
var status="";

$(function() {
	recordTable = new AjaxTable("recordTable", 20, 12, renderCellValue);
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
   querySendHis();	
});

function querySendHis(){
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var appname = $("#appname").val();
	var mobile = $("#mobile").val();
	var sender = $("#sender").val();
	var sendstatus = $("#sendstatus").val();
	var sendingstatus = $("#sendingstatus").val();
	var cpage = pageTag.param.currPage;
	if (cpage==0)
	{
		cpage = 1;
	}
	pageTag.url = sendhistoryUrl;
	pageTag.param = {
		"currPage" : cpage,
		"pageSize" : 20,
		"starttime" : starttime,
		"endtime" : endtime,
		"appname":appname,
		"mobile":mobile,
		"sender":sender,
		"sendstatus":sendstatus,
		"sendingstatus":sendingstatus
	};
	$.ajax( {
		type : "post",
		url : sendhistoryUrl,
		data : {
			"currPage" : cpage,
			"pageSize" : 20,
			"starttime" : starttime,
			"endtime" : endtime,
			"appname":appname,
			"mobile":mobile,
			"sender":sender,
			"sendstatus":sendstatus,
			"sendingstatus":sendingstatus
		},
		success : handleResult,
		dataType : "text"
	});
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
	var datatime=rowdata.sendtime.substring(0,19);
	var year=datatime.substring(0,4);
	var month=datatime.substring(5,7);
	var day=datatime.substring(8,10);
	var myDate = new Date();
	var datastring=null;
	sysyear=myDate.getFullYear();
	sysmonth=myDate.getMonth();
	sysday=myDate.getDate();
	if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==0){
		datastring="今天 "+rowdata.sendtime.substring(11,19);
	}else if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==1)
	{
		datastring="昨天 "+rowdata.sendtime.substring(11,19);
	}else if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==2)
	{
		datastring="前天 "+rowdata.sendtime.substring(11,19);
	}
	else{
		datastring=datatime;
	}
	if(rowdata.isrecivered=="2")
    	status = "等待";
	if(rowdata.isrecivered=="")
    	status = "等待";
	if(rowdata.isrecivered == "1")
		status = "否";
	if(rowdata.isrecivered == "0")
		status = "是";
	
	if(rowdata.sendstatus == "01")
    	str = "发送中";
	if(rowdata.sendstatus == "03")
    	str = "成功";
	if(rowdata.sendstatus == "09")
    	str = "失败";
	if(rowdata.sendstatus == "101")
    	str = "账号错误";
	if(rowdata.sendstatus == "102")
    	str = "密码错误";
	if(rowdata.sendstatus == "103")
    	str = "余额不足";
	if(rowdata.sendstatus == "104")
    	str = "系统忙";
	if(rowdata.sendstatus == "105")
    	str = "敏感词";
	if(rowdata.sendstatus == "106")
    	str = "长度出错";
	if(rowdata.sendstatus == "107")
    	str = "号码错误";
	if(rowdata.sendstatus == "108")
    	str = "手机号过多";
	if(rowdata.sendstatus == "109")
    	str = "内容无签名";
	switch (index) {
	case 0:
		return '<div  align="center">' + checkDataItem(rowdata.appname) + '</div>';
	case 1:
		return '<div  align="center">' + checkDataItem(rowdata.mobile)+ '</div>';
	case 2:
		if(rowdata.sender==""){
			return '<div  align="left">' + "" + '</div>';
		}else{
			return '<div  align="left">' + checkDataItem(rowdata.sender) + '</div>';
		}
	case 3:
		if(rowdata.reciver==""){
			return '<div  align="left">' + "" + '</div>';
		}else{
			var index=rowdata.reciver.indexOf(":");
			
			return '<div  align="left">' + checkDataItem(rowdata.reciver.substring(index+1)) + '</div>';
		}
	case 4:
		return '<div  align="center">' + datastring + '</div>';
	case 5:
		return '<div  align="center">' + str + '</div>';
	case 6:
		return '<div  align="center">' + status + '</div>';
	case 7:
		var type;
		if(rowdata.resendtype=="1"){
			type="重发";
		}else if(rowdata.resendtype=="0"){
			type="原始";
		}else{
			type="";
		}
		return '<div  align="center">' + type  + '</div>';
	case 8:
		return '<div  align="center">' + checkDataItem(rowdata.resendcount)   + '</div>';
	case 9:
		return '<div  align="left">' + checkDataItem(rowdata.resender)   + '</div>';
		
	case 10:
		if(rowdata.resendtype=="0"){
			return '<div  align="center">' +"<a onclick='javascript:resend(\""+rowdata.mainid+"\",\""+rowdata.mobile+"\",\""+rowdata.content+"\");return false;' href='javascript:void(0)'>重发</a></div>";
		}else
			return '<div  align="center">' +""+ '</div>';
	
	case 11:
		return '<div  align="left">' + checkDataItem(rowdata.content) + '</div>';
	default:
		return "&nbsp;";
	}
}
var resendUrl = url("resend");
function resend(mainid,mobile,content){
	var diag = new Dialog();
	diag.Width = 450;
	diag.Height = 420;
	diag.Title = "重发短信";
	diag.URL = resendUrl+"?mainid="+mainid+"&mobile="+mobile+"&content="+content;
	diag.show();
	
}