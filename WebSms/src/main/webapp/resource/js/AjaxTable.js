function AjaxTable(id, rowCnt, colCnt, renderCell) 
{
	this.tbl = document.getElementById(id);
	this.rowCnt = rowCnt;
	this.colCnt = colCnt;
	this.renderCell = renderCell;
	this.preRowNum = 0;
}
AjaxTable.prototype = {
showTable:function () 
{
//	$(".teStyleTable").children("thead").children("tr").children("th").addClass("dxgvHeader_Aqua");
//	alert(this.rowCnt);
	if(this.rowCnt != -1)
	{	
//	alert("------111111");
		for (var i = 0; i < this.rowCnt; i++) 
		{
			var row = this.tbl.insertRow(i + 1);
			checkRowStyle(row, i);
			for (var j = 0; j < this.colCnt; j++) 
			{
				var cell = row.insertCell(j);
				cell.className = "dxgv";
				cell.innerHTML = "&nbsp;";
			}
		}
	}
	else
	{
//		alert("fuyu");
		for (var i = 0; i < 10; i++) 
		{
			var row = this.tbl.insertRow(i + 1);
			checkRowStyle(row, i);
			for (var j = 0; j < this.colCnt; j++) 
			{
				var cell = row.insertCell(j);
				cell.className = "dxgv";
				cell.innerHTML = "&nbsp;";
			}
		}
		this.preRowNum = i;	
	}

}, 
showData:function (data) 
{
	var rows = this.tbl.rows;
	if(this.rowCnt != -1)
	{
//	alert("------222222");
		var i = 0;
		for (; i < data.length; i++) 
		{
			var cells = rows[i + 1].cells;
			for (var j = 0; j < cells.length; j++) 
			{
				cells[j].innerHTML = this.renderCell(data[i], i, j);
			}
		}
		for (; i + 1 < rows.length; i++) 
		{
			var cells = rows[i + 1].cells;
			for (var j = 0; j < cells.length; j++) 
			{
				cells[j].innerHTML = "&nbsp;";
			}
		}
	}
	else
	{	
		if(this.preRowNum > 0)
		{
			for(; this.preRowNum > 0; this.preRowNum--)
			{
				this.tbl.deleteRow(this.preRowNum);
			}
		}
		var i = 0;
		for(; i < data.length; i++)
		{
			var row = this.tbl.insertRow(i+1);
			checkRowStyle(row, i);
			for (var j = 0; j < this.colCnt; j++) 
			{
				var cell = row.insertCell(j);
				cell.className = "dxgv";
				cell.innerHTML = this.renderCell(data[i], i, j);
			}
		}
		for(; i < 10; i++)
		{
			var row = this.tbl.insertRow(i+1);
			checkRowStyle(row, i);
			for (var j = 0; j < this.colCnt; j++) 
			{
				var cell = row.insertCell(j);
				cell.className = "dxgv";
				cell.innerHTML = "&nbsp;";
			}
		}
		this.preRowNum = i;
	}
},

showDataScroll:function (data,id) 
{
	//var rows = this.tbl.rows;
	var rows=window.document.getElementById(id).rows;

	if(this.rowCnt != -1)
	{
//	alert("------222222");
		var i = 0;
		for (; i < data.length; i++) 
		{
			var cells = rows[i + 1].cells;
			for (var j = 0; j < cells.length; j++) 
			{
				cells[j].innerHTML = this.renderCell(data[i], i, j);
			}
		}
		for (; i + 1 < rows.length; i++) 
		{
			var cells = rows[i + 1].cells;
			for (var j = 0; j < cells.length; j++) 
			{
				cells[j].innerHTML = "&nbsp;";
			}
		}
	}
	else
	{	
		if(this.preRowNum > 0)
		{
			for(; this.preRowNum > 0; this.preRowNum--)
			{
				window.document.getElementById(id).deleteRow(this.preRowNum);
			}
		}
		var i = 0;
		for(; i < data.length; i++)
		{
			var row = window.document.getElementById(id).insertRow(i+1);
			checkRowStyle(row, i);
			for (var j = 0; j < this.colCnt; j++) 
			{
				var cell = row.insertCell(j);
				cell.className = "dxgv";
				cell.innerHTML = this.renderCell(data[i], i, j);
			}
		}
		for(; i < 10; i++)
		{
			var row = window.document.getElementById(id).insertRow(i+1);
			checkRowStyle(row, i);
			for (var j = 0; j < this.colCnt; j++) 
			{
				var cell = row.insertCell(j);
				cell.className = "dxgv";
				cell.innerHTML = "&nbsp;";
			}
		}
		this.preRowNum = i;
	}
}
};

function checkDataItem(str)
{
	if(str == null || str == "")
		return "&nbsp;";
	else
		return str;
}

function checkRowStyle(row, i)
{
	if(i % 2 == 0)
	{
		row.className = "dxgvDataRow_Aqua";
	}
	else if(i % 2 == 1)
	{
		row.className = "dxgvDataRow_Aqua dxgvDataRowAlt_Aqua";
	}	
}






