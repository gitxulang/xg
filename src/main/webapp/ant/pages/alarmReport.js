
function showNoData(width, height) {
	return "<div style='width:"
		+ width
		+ "%;height:"
		+ height
		+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
		+ height
		+ "px;' >这里暂时没有数据</div>";			
}

function showNoFunction(width, height) {
	return "<div style='width:"
		+ width
		+ "%;height:"
		+ height
		+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
		+ height
		+ "px;' >抱歉，您没有此处权限！</div>";			
}

function GetRequest() {
	var url = location.search;
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

$(document).ready(function(){
	/*// 点击下拉菜单意外区域隐藏
	window.onclick = function(event) {
	  if (!event.target.matches('.drop1btn')) {
	    var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	    }
	  }
	}*/
	
	
	$("body").append(
		'<div class="current_orderkey" style="display:none;"></div>'+
		'<div class="current_ordervalue" style="display:none;"></div>'+			
		'<div class="orderLevel_count" style="display:none;">0</div>'+
		'<div class="orderIp_count" style="display:none;">0</div>'+
		'<div class="orderBmIp_count" style="display:none;">0</div>' +
        '<div class="orderYwIp_count" style="display:none;">0</div>' +
		'<div class="orderContent_count" style="display:none;">0</div>'+
		'<div class="orderAlarmTime_count" style="display:none;">0</div>'+
		'<div class="orderDealTime_count" style="display:none;">0</div>'+
		'<div class="orderStatus_count" style="display:none;">0</div>'+
		'<div class="flag_log" style="display:none;">0</div>'
	);
	loadTable();
})

function loadTable() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	query(dataJson);
}

function selectPage(){
	var dropdowns = document.getElementsByClassName("drop1down-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData);
}

function changeOffset() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData);
}

function firstPage() {
	if ($("#page").html() != "1") {
		var begin = 1;
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData);
	}
}

function beforePage() {
	if ($("#page").html() != "1") {
		var begin = parseInt($("#page").html()) - 1;
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData);
	}
}

function nextPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#page").html()) + 1;
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData);
	}
}

function lastPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#totalPage").html())
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData);
	}
}

function setOrderKey(obj) {
	var order = $(obj).attr("id");
	var orderKey = "";
	var orderValue = 0;
	if (order == "orderLevel") {
		$(".orderLevel_count").html(parseInt($(".orderLevel_count").html()) + 1);
		orderKey = "ALevel";
		orderValue = parseInt($(".orderLevel_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIp") {
		$(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
		orderKey = "nmsAsset.AIp";
		orderValue = parseInt($(".orderIp_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderBmIp") {
        $(".orderBmIp_count").html(parseInt($(".orderBmIp_count").html()) + 1);
        orderKey = "BmIp";
        orderValue = parseInt($(".orderBmIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderYwIp") {
        $(".orderYwIp_count").html(parseInt($(".orderYwIp_count").html()) + 1);
        orderKey = "YwIp";
        orderValue = parseInt($(".orderYwIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderContent") {
		$(".orderContent_count").html(parseInt($(".orderContent_count").html()) + 1);
		orderKey = "AContent";
		orderValue = parseInt($(".orderContent_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderAlarmTime") {
		$(".orderAlarmTime_count").html(parseInt($(".orderAlarmTime_count").html()) + 1);
		orderKey = "ATime";
		orderValue = parseInt($(".orderAlarmTime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderDealTime") {
		$(".orderDealTime_count").html(parseInt($(".orderDealTime_count").html()) + 1);
		orderKey = "DTime";
		orderValue = parseInt($(".orderDealTime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderStatus") {
		$(".orderStatus_count").html(parseInt($(".orderStatus_count").html()) + 1);
		orderKey = "DStatus";
		orderValue = parseInt($(".orderStatus_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	}

	var begin = $("#page").html()
	var offset = $("#selectPage").val();
	
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;

	if (orderKey != "") {
		requestData["orderKey"] = orderKey;
		requestData["orderValue"] = orderValue;
	}

	query(requestData);
}

function query(dataJson) {
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;

	var ip = $("#ip").val();
	var alarm_level = $("#alarm_level").val();
	var alarm_status = $("#alarm_status").val();
	var alarm_content = $("#alarm_content").val();
	var alarm_start_time = $("#alarm_start_time").val();
	var alarm_end_time = $("#alarm_end_time").val();
	
	if (ip != "") {
		dataJson["AIp"] = ip;
	}
	
	if (alarm_level != null && alarm_level != "") {
		dataJson["ALevel"] = alarm_level;
	}
	
	if (alarm_status != null && alarm_status != "") {
		dataJson["DStatus"] = alarm_status;
	}
	
	if (alarm_content != null && alarm_content != "") {
		dataJson["AContent"] = alarm_content;
	}
	
	if (alarm_start_time != null && alarm_start_time != ""){
		dataJson["startDate"] = alarm_start_time;
	}
	
	if (alarm_end_time != null && alarm_end_time != "") {
		dataJson["endDate"] = alarm_end_time;
	}

	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/alarm/list/page/condition",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				loadList(json);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /alarm/list/page/condition: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /alarm/list/page/condition: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function htmlEscape(text) {
	return text.replace(/[<>"&]/g, function(match, pos, originalText) {
		switch (match) {
		case "<":
			return "&lt;";
		case ">":
			return "&gt;";
		case "&":
			return "&amp;";
		case "\"":
			return "&quot;";
		}
	});
}

function loadList(json){
	$("#tableBodyID").empty();
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	
	for (var i in json.list) {
		var level = "";
		var status = "";
		var action = "";
		
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		
		if (json.list[i].nmsAlarm.alevel == "1") {
			level = '<font color="green">低风险</font>';
		}
		
		if (json.list[i].nmsAlarm.alevel == "2") {
			level = '<font color="orange">中风险</font>';
		}
		
		if (json.list[i].nmsAlarm.alevel == "3") {
			level = '<font color="red">高风险</font>';
		}
		
		if (json.list[i].nmsAlarm.dstatus == "0") {
			status = '<font color="red">待处理</font>';
			action = '<a href="#" onclick="alarmDeal(' + json.list[i].nmsAlarm.id + ',' + json.list[i].nmsAlarm.dstatus + ')"><i class="my-material-icons">编辑&nbsp;</i></a>';
			action += '<a href="#" onclick="alarmDelete(' + json.list[i].nmsAlarm.id + ')"><i class="my-material-icons">删除</i></a>';
		} else if(json.list[i].nmsAlarm.dstatus == "1") {
			status = '<font color="yellow">处理中</font>';
			action = '<a href="#" onclick="alarmDeal(' + json.list[i].nmsAlarm.id + ',' + json.list[i].nmsAlarm.dstatus + ')"><i class="my-material-icons">编辑&nbsp;</i></a>';
			action += '<a href="#" onclick="alarmDelete(' + json.list[i].nmsAlarm.id + ')"><i class="my-material-icons">删除</i></a>';
		} else if (json.list[i].nmsAlarm.dstatus == "2") {
			status = '<font color="green">已处理</font>';
			action = '<a href="#" onclick="alarmDeal(' + json.list[i].nmsAlarm.id + ',' + json.list[i].nmsAlarm.dstatus + ')"><i class="my-material-icons">查看&nbsp;</i></a>';
			action += '<a href="#" onclick="alarmDelete(' + json.list[i].nmsAlarm.id + ')"><i class="my-material-icons">删除</i></a>';
		} else {
			status = '<font color="green">未知</font>';
		}
		
		
		var showContent = json.list[i].nmsAlarm.acontent;
		if (showContent != null) {
			showContent = showContent.substring(0, 80);
		}
		
		var dealTime = '';
		dealTime = json.list[i].nmsAlarm.dtime;
		if (dealTime == '') {
			dealTime = '--';
		}

		let startTime = json.list[i].nmsAlarm.stime
		if (!startTime) {
			startTime = '--'
		}
		let endTime = json.list[i].nmsAlarm.atime
		if (!endTime) {
			endTime = '--'
		}

		$("#tableBodyID").append(
			'<tr class="odd gradeX">' +
				'<td class="center">' + n + '</td>' +
				
				// '<td>' + htmlEscape(json.list[i].nmsAlarm.nmsAsset.aip) + '</td>' +
				// '<td>' + htmlEscape(json.list[i].nmsAlarm.acontent) + '</td>' +
				// '<td><a href="#" onclick="showDetail(' + json.list[i].nmsAlarm.id + ')">' + json.list[i].nmsAlarm.nmsAsset.aip + '</td>' +
				'<td>' + json.list[i].nmsAlarm.nmsAsset.aip + '</td>' +
				// '<td>' + htmlEscape(json.list[i].bmIp) + '</td>' +
				// '<td>' + htmlEscape(json.list[i].ywIp) + '</td>' +
				// '<td>' + json.list[i].nmsAlarm.nmsAsset.nmsAssetType.chType + '/' + json.list[i].nmsAlarm.nmsAsset.nmsAssetType.chSubtype + '</td>' +
				'<td>' + json.list[i].nmsAlarm.acontent + '</td>' +				
				// '<td>' + json.list[i].nmsAlarm.stime + '</td>' +
				// '<td>' + json.list[i].nmsAlarm.atime + '</td>' +
				'<td>' + startTime + '</td>' +
				'<td>' + endTime + '</td>' +
				'<td>' + json.list[i].nmsAlarm.acount + '</td>' +
				'<td>' + dealTime + '</td>'+
				'<td>' + level + '</td>' +
		 		'<td>' + status + '</td>'+
			'</tr>'
		)
		$('.my-material-icons').css('color', '#22B37A')
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function alarmDeal(alarmId, status) {
	$("#status").val(status);
	$("#idHidden").val(alarmId);
	if (status == 0) {
		openDialog("alarmPreDeal.html", "告警信息处理", 800, 600)
	} else if (status == 1) {
		openDialog("alarmDeal.html", "告警信息处理", 800, 600)
		
	} else if (status == 2) {
		openDialog("alarmDeal.html", "告警信息查看", 800, 600)
		
	}
}

function alarmDelete(id) {
	if (id == null || id == "") {
		alert("删除的告警记录不存在！");
		return;
	}
	if (!confirm("确认要清除选中的告警记录？")) { 
		return;
	}
	var ids = id + ",";
	var dataJson = {"id" : ids};
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/alarm/deleteAlarmById",
		data: dataJson,
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				alert("您没有操作没有权限！");
			} else {
				alert("删除成功！");
				window.location.reload();
			}
		},
		error : function() {
			alert("删除失败！")
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /alarm/deleteAlarmById: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /alarm/deleteAlarmById: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function exportTable(){
	var url = "/alarm/list/alarm/condition/exportExcel?";
	var alarm_ip = $("#alarm_ip").val();
	var alarm_level = $("#alarm_level").val();
	var alarm_status = $("#alarm_status").val();
	var alarm_content = $("#alarm_content").val();
	var alarm_start_time = $("#alarm_start_time").val();
	var alarm_end_time = $("#alarm_end_time").val();	
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();		
	
	if(alarm_ip!="" && alarm_ip != undefined && alarm_ip != null){
		url+="AIp="+alarm_ip+"&";
	}
	
	if(alarm_level!="" && alarm_level != undefined && alarm_level != null){
		url+="ALevel="+alarm_level+"&";
	}	
	
	if(alarm_status!="" && alarm_status != undefined && alarm_status != null){
		url+="DStatus="+alarm_status+"&";
	}
	
	if(alarm_content!="" && alarm_content != undefined && alarm_content != null){
		url+="AContent="+alarm_content+"&";
	}	
	
	if(alarm_start_time!="" && alarm_start_time != undefined && alarm_start_time != null){
		url+="startDate="+alarm_start_time+"&";
	}	
	
	if(alarm_end_time!="" && alarm_end_time != undefined && alarm_end_time != null){
		url+="endDate="+alarm_end_time+"&";
	}
	
	if(orderKey!="" && orderKey != undefined && orderKey != null){
		url+="orderKey="+orderKey+"&";
	}
	
	if(orderValue!="" && orderValue != undefined && orderValue != null){
		url+="orderValue="+orderValue+"&";
	}	

    window.location.href = url;
}

var reset = function(){
	$('#alarm_start_time').val("");
	$('#alarm_start_time').datetimepicker('update');
	$('#alarm_end_time').val("");
	$('#alarm_end_time').datetimepicker('update');
	$("#ip").val('');
	$("#alarm_content").val('');
	$("#alarm_status").val('');
	$("#alarm_level").val('')
	
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	}
	loadTable()
}

function exportTable() {
	var url = "/alarm/list/alarm/condition/exportExcel?";
	var ip = $("#ip").val();
	var alarm_level = $("#alarm_level").val();
	var alarm_status = $("#alarm_status").val();
	var alarm_content = $("#alarm_content").val();
	var alarm_start_time = $("#alarm_start_time").val();
	var alarm_end_time = $("#alarm_end_time").val();
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();

	if (ip != "" && ip != undefined && ip != null) {
		url += "AIp=" + ip + "&";
	}

	if (alarm_level != "" && alarm_level != undefined && alarm_level != null) {
		url += "ALevel=" + alarm_level + "&";
	}

	if (alarm_status != "" && alarm_status != undefined && alarm_status != null) {
		url += "DStatus=" + alarm_status + "&";
	}

	if (alarm_content != "" && alarm_content != undefined
			&& alarm_content != null) {
		url += "AContent=" + alarm_content + "&";
	}

	if (alarm_start_time != "" && alarm_start_time != undefined
			&& alarm_start_time != null) {
		url += "startDate=" + alarm_start_time + "&";
	}

	if (alarm_end_time != "" && alarm_end_time != undefined
			&& alarm_end_time != null) {
		url += "endDate=" + alarm_end_time + "&";
	}

	if (orderKey != "" && orderKey != undefined && orderKey != null) {
		url += "orderKey=" + orderKey + "&";
	}

	if (orderValue != "" && orderValue != undefined && orderValue != null) {
		url += "orderValue=" + orderValue + "&";
	}

	window.location.href = url;
}

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#alarm_content').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#ip').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
	$('.table-condensed').css("background-color","#d9edf7");
});

// $("#alarm_start_time").datetimepicker({
// 	language: "zh-cn",
// 	format: "yyyy-mm-dd hh:ii:ss",
// 	minView: "month",
// 	autoclose: true,
// 	todayBtn: true
// });

// $("#alarm_end_time").datetimepicker({
// 	language: "zh-cn",
// 	format: "yyyy-mm-dd hh:ii:ss",
// 	minView: "month",
// 	autoclose: true,
// 	todayBtn: true
// });
jeDate("#alarm_start_time",{
	theme:{bgcolor:"#00A1CB",pnColor:"#00CCFF"},
	format: "YYYY-MM-DD hh:mm:ss"
});

jeDate("#alarm_end_time",{
	theme:{bgcolor:"#00A1CB",pnColor:"#00CCFF"},
	format: "YYYY-MM-DD hh:mm:ss"
});

