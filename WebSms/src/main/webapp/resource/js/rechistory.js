var recordTable;
var pageTag;
var di = '第';
var page = '页';
var page1 = '页，共';
var row = '行';
var previousPage = '上一页';
var nextPage = '下一页';

var rechistoryUrl = url("rechistory");
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
   queryRecHis();	
});

function queryRecHis(){
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var mobile = $("#mobile").val();
	var cpage = pageTag.param.currPage;
	if (cpage==0)
	{
		cpage = 1;
	}
	pageTag.url = rechistoryUrl;
	pageTag.param = {
		"currPage" : cpage,
		"pageSize" : 20,
		"starttime" : starttime,
		"endtime" : endtime,
		"mobile":mobile
	};
	$.ajax( {
		type : "post",
		url : rechistoryUrl,
		data : {
			"currPage" : cpage,
			"pageSize" : 20,
			"starttime" : starttime,
			"endtime" : endtime,
			"mobile":mobile
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

function timeTrance(time){
	var datatime=time.substring(0,19);
	var year=datatime.substring(0,4);
	var month=datatime.substring(5,7);
	var day=datatime.substring(8,10);
	var myDate = new Date();
	var datastring=null;
	sysyear=myDate.getFullYear();
	sysmonth=myDate.getMonth();
	sysday=myDate.getDate();
	if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==0){
		datastring="今天 "+time.substring(11,19);
	}else if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==1)
	{
		datastring="昨天 "+time.substring(11,19);
	}else if(Number(year)-Number(sysyear)==0&&Number(month)-Number(sysmonth)==1&&Number(sysday)-Number(day)==2)
	{
		datastring="前天 "+time.substring(11,19);
	}
	else{
		datastring=datatime;
	}
	return datastring;
}

function renderCellValue(rowdata, i, index) {
	switch (index) {
	case 0:
		return '<div  align="center">' + checkDataItem(rowdata.mobile) + '</div>';
	case 1:
		return '<div  align="center">' + checkDataItem(rowdata.receiver)+ '</div>';
	case 2:
		return '<div  align="center">' + timeTrance(rowdata.sendtime) + '</div>';
	case 3:
		return '<div  align="center">' + timeTrance(rowdata.recvtime) + '</div>';
	case 4:	
		return '<div  align="left">' + checkDataItem(rowdata.content) + '</div>';
	default:
		return "&nbsp;";
	}
}