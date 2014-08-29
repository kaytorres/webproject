function AjaxPageTag(id, pageSize, computeRange, handle, render) 
{
	this.id = id;
	this.pageSize = pageSize;
	this.computeRange = computeRange;
	this.handle = handle;
	this.renderInfo = render.renderInfo;
	this.renderPrev = render.renderPrev;
	this.renderNext = render.renderNext;
	this.links = [];
	this.total=0;
	this.page=0;

}
AjaxPageTag.prototype = {
show:function (page, total) 
{
	$("#" + this.id).empty();
	this.links = [];
	var pageCnt = Math.ceil(total / this.pageSize);
	if (this.renderInfo) 
	{
		$("<span class=\"pageInfo\">" + this.renderInfo(page, pageCnt, total) + "</span>").appendTo("#" + this.id);
	}
	if (page > 1 && this.renderPrev) 
	{
		$("<a id=\""+this.id+"prevPage\" href=\"\" class=\"pageLink\">" + " " + this.renderPrev + "</a>").appendTo("#" + this.id);
		this.links.push({"page":page - 1, "link":$("#"+this.id+"prevPage")});
	}
	var range = this.computeRange(page, pageCnt);
	for (var i = range[0]; i <= range[1]; i++) 
	{
//		$("<a id=\""+this.id+"page" + i + "\" href=\"\" class=\"pageLink\">" + "&nbsp;&nbsp;" + i + "</a>").appendTo("#" + this.id);
		if (i != page) 
		{
			$("<a id=\""+this.id+"page" + i + "\" href=\"\" class=\"pageLink\">" + "&nbsp;&nbsp;" + i + "</a>").appendTo("#" + this.id);
			this.links.push({"page":i, "link":$("#"+this.id+"page" + i)});
		}
		else
		{
		
			$("<a id=\""+this.id+"page" + i + "\" href=\"\" style=\"Color:red\">" + "&nbsp;&nbsp;" + i + "</a>").appendTo("#" + this.id);
			$("#"+this.id+"page" + i).bind('click',function(event)
			{
				event.preventDefault();
			});
		}
	}
	if (page < pageCnt && this.renderNext) 
	{
		$("<a id=\""+this.id+"nextPage\" href=\"\" class=\"pageLink\"> &nbsp;&nbsp;" + this.renderNext + "</a>").appendTo("#" + this.id);
		this.links.push({"page":page - (-1), "link":$("#"+this.id+"nextPage")});
	}
}, 
//设置页数和总记录数
setParam:function(total){
	var pageCnt = Math.ceil(total / this.pageSize);
	this.page=pageCnt;
	this.total=total;
}
,
bind:function () 
{
	var that = this;
	for (var i = 0; i < this.links.length; i++) 
	{
		this.links[i].link.bind("click", function (j) 
		{
			return function (event) 
			{
				event.preventDefault();
				that.param.currPage = that.links[j].page;
				$.post(that.url, that.param, that.handle,"text");
			};
		}(i));
	}
},
//初始化分页标签
 init:function(pagenumber){
	var data = {'pagecount':this.page,'totalcount':this.total};
	$("#pageTag").pager({ pagenumber: pagenumber, pagecount:data.pagecount,totalcount:data.totalcount, buttonClickCallback: PageClick});
	
}
};
//页码的单击事件
PageClick = function(pageclickednumber) {
	
	pageTag.param.currPage=pageclickednumber
	$.post(pageTag.url, pageTag.param, pageTag.handle,"text");
	//pageTag.init(pageclickednumber);
	//alert("pageTag.url="+pageTag.url+" pageTag.param="+ pageTag.param+"单击选中的页数"+pageclickednumber);
	//$("#result").html("Clicked Page " + pageclickednumber);
}