var recordTable;
var pageTag;
var di = '第';
var page = '页';
var page1 = '页，共';
var row = '行';
var previousPage = '上一页';
var nextPage = '下一页';

var syslogUrl = url("syslog");
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
  querysyslog();	
});

function querysyslog(){
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var opcode = $("#opcode").val();
	var operator = $("#selectname").val();
	var cpage = pageTag.param.currPage;
	if (cpage==0)
	{
		cpage = 1;
	}
	pageTag.url = syslogUrl;
	pageTag.param = {
		"currPage" : cpage,
		"pageSize" : 20,
		"starttime" : starttime,
		"endtime" : endtime,
		"opcode":opcode,
		"operator":operator
	};
	$.ajax( {
		type : "post",
		url : syslogUrl,
		data : {
			"currPage" : cpage,
			"pageSize" : 20,
			"starttime" : starttime,
			"endtime" : endtime,
			"opcode":opcode,
			"operator":operator
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
	var datatime=rowdata.operatetime.substring(0,19);
	var year=datatime.substring(0,4);
	var month=datatime.substring(5,7);
	var day=datatime.substring(8,10);
	var myDate = new Date();
	var datastring=null;
	sysyear=myDate.getFullYear();
	sysmonth=myDate.getMonth();
	sysday=myDate.getDate();
	if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==0){
		datastring="今天 "+rowdata.operatetime.substring(11,19);
	}else if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==1)
	{
		datastring="昨天 "+rowdata.operatetime.substring(11,19);
	}else if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==2)
	{
		datastring="前天 "+rowdata.operatetime.substring(11,19);
	}
	else{
		datastring=datatime;
	}
	switch (index) {
	case 0:
		return '<div  align="center">' + checkDataItem(rowdata.operator) + '</div>';
	case 1:
		if(rowdata.action == "01")
	    	str = "系统登录";
	    if(rowdata.action == "02")
	    	str = "网关维护";
	    if(rowdata.action == "03")
	    	str = "应用维护";
	    if(rowdata.action == "04")
	    	str = "账号维护";		
	    if(rowdata.action == "05")
	    	str = "敏感词维护";		
	    if(rowdata.action == "06")
	    	str = "应用登录";		
	    if(rowdata.action == "07")
	    	str = "MT发送到系统";		
	    if(rowdata.action == "08")
	    	str = "应用发送到系统";		
	    if(rowdata.action == "09")
	    	str = "系统发送到电信网关";	
	    if(rowdata.action == "10")
	    	str = "网关推送到应用";	
		return '<div  align="center">' + str + '</div>';
	case 2:
		return '<div  align="center">' + datastring + '</div>';
	case 3:
		return '<div  align="left">' + checkDataItem(rowdata.content) + '</div>';
	
	default:
		return "&nbsp;";
	}
}