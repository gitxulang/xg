
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

$(document).ready(function(){
	// var request = GetRequest();
	var request = GetSelfRequest();
	var id = request.id;
	var typeid = request.typeid;
	var redirect = request.redirect;
	if (id == null || id == "" || typeid == null || typeid == "" || redirect == null || redirect == "") {
		return;
	}
	$("#assetId").val(id);
	$("#typeId").val(typeid);
	
	$("body").append(
		'<div class="current_orderkey" style="display:none;"></div>'+
		'<div class="current_ordervalue" style="display:none;"></div>'+			
		'<div class="orderLevel_count" style="display:none;">0</div>'+
		'<div class="orderIp_count" style="display:none;">0</div>'+
		'<div class="orderContent_count" style="display:none;">0</div>'+
		'<div class="orderStartAlarmTime_count" style="display:none;">0</div>'+
		'<div class="orderAlarmTime_count" style="display:none;">0</div>'+
		'<div class="orderAlarmCount_count" style="display:none;">0</div>'+
		'<div class="orderDealTime_count" style="display:none;">0</div>'+
		'<div class="orderStatus_count" style="display:none;">0</div>'+
		'<div class="flag_log" style="display:none;">0</div>'
	);
	loadTab(id, typeid, redirect);
	loadTable();
})

function loadTable() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	var id = $("#assetId").val();
	dataJson["assetId"] = id;
	query(dataJson);
}

function selectPage(){
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	var id = $("#assetId").val();
	requestData["assetId"] = id;
	query(requestData);
}

function changeOffset() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var id = $("#assetId").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	requestData["assetId"] = id;
	query(requestData);
}

function firstPage() {
	if ($("#page").html() != "1") {
		var begin = 1;
		var offset = $("#selectPage").val();
		var id = $("#assetId").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		requestData["assetId"] = id;
		query(requestData);
	}
}

function beforePage() {
	if ($("#page").html() != "1") {
		var begin = parseInt($("#page").html()) - 1;
		var offset = $("#selectPage").val();
		var id = $("#assetId").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		requestData["assetId"]  =id;
		query(requestData);
	}
}

function nextPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#page").html()) + 1;
		var offset = $("#selectPage").val();
		var id = $("#assetId").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		requestData["assetId"] = id;
		query(requestData);
	}
}

function lastPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#totalPage").html())
		var offset = $("#selectPage").val();
		var id = $("#assetId").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		requestData["assetId"] = id;
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
	} else if (order == "orderContent") {
		$(".orderContent_count").html(parseInt($(".orderContent_count").html()) + 1);
		orderKey = "AContent";
		orderValue = parseInt($(".orderContent_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderStartAlarmTime") {
		$(".orderStartAlarmTime_count").html(parseInt($(".orderStartAlarmTime_count").html()) + 1);
		orderKey = "STime";
		orderValue = parseInt($(".orderStartAlarmTime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderAlarmTime") {
		$(".orderAlarmTime_count").html(parseInt($(".orderAlarmTime_count").html()) + 1);
		orderKey = "ATime";
		orderValue = parseInt($(".orderAlarmTime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderAlarmCount") {
		$(".orderAlarmCount_count").html(parseInt($(".orderAlarmCount_count").html()) + 1);
		orderKey = "ACount";
		orderValue = parseInt($(".orderAlarmCount_count").html()) % 2;
		
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
	var id = $("#assetId").val();
	
	var requestData = {};
	requestData["assetId"] = id;	
	requestData["begin"] = begin;
	requestData["offset"] = offset;

	if (orderKey != "") {
		requestData["orderKey"] = orderKey;
		requestData["orderValue"] = orderValue;
	}

	query(requestData, begin, offset);
}

function query(dataJson) {
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;

	var alarm_level = $("#alarm_level").val();
	var alarm_status = $("#alarm_status").val();
	var alarm_content = $("#alarm_content").val();
	var alarm_start_time = $("#alarm_start_time").val();
	var alarm_end_time = $("#alarm_end_time").val();
	
	var id = $("#assetId").val();
	dataJson["nmsAssetId"] = id;
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
            console.log("[ERROR] textStatus: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
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
		
		var assetId = $("#assetId").val();
		var typeId = $("#typeId").val();
		if (json.list[i].nmsAlarm.dstatus == "0") {
			status = '<font color="red">待处理</font>';
			action = '<a href="#" onclick="alarmDeal(' + assetId + ',' + typeId + ',' + json.list[i].nmsAlarm.id + ',' + json.list[i].nmsAlarm.dstatus + ')"><i class="my-material-icons" style="color:#22B37A;">编辑&nbsp;</i></a>';
			action += '<a href="#" onclick="alarmDelete(' + json.list[i].nmsAlarm.id + ')"><i class="my-material-icons" style="color:#22B37A;">删除</i></a>';
		} else if(json.list[i].nmsAlarm.dstatus == "1") {
			status = '<font color="yellow">处理中</font>';
			action = '<a href="#" onclick="alarmDeal(' + assetId + ',' + typeId + ',' + json.list[i].nmsAlarm.id + ',' + json.list[i].nmsAlarm.dstatus + ')"><i class="my-material-icons" style="color:#22B37A;">编辑&nbsp;</i></a>';
			action += '<a href="#" onclick="alarmDelete(' + json.list[i].nmsAlarm.id + ')"><i class="my-material-icons" style="color:#22B37A;">删除</i></a>';
		} else if (json.list[i].nmsAlarm.dstatus == "2") {
			status = '<font color="green">已处理</font>';
			action = '<a href="#" onclick="alarmDeal(' + assetId + ',' + typeId + ',' + json.list[i].nmsAlarm.id + ',' + json.list[i].nmsAlarm.dstatus + ')"><i class="my-material-icons" style="color:#22B37A;">查看</i></a>';
			action += '<a href="#" onclick="alarmDelete(' + json.list[i].nmsAlarm.id + ')"><i class="my-material-icons" style="color:#22B37A;">删除</i></a>';
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
		
		$("#tableBodyID").append(
			'<tr class="odd gradeX">' +
				'<td class="center">' + n + '</td>' +
				'<td>' + htmlEscape(json.list[i].nmsAlarm.nmsAsset.aip) + '</td>' +
				'<td>' + level + '</td>' +
				'<td>' + htmlEscape(json.list[i].nmsAlarm.acontent) + '</td>' +
				'<td>' + json.list[i].nmsAlarm.stime + '</td>' +
				'<td>' + json.list[i].nmsAlarm.atime + '</td>' +
				'<td>' + json.list[i].nmsAlarm.acount + '</td>' +
				'<td>' + dealTime + '</td>'+
		 		'<td>' + status + '</td>'+
				'<td class="center">' + action + '</td>'+
			'</tr>'
		)
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function alarmDeal(assetId, typeId, alarmId, status) {
	if (status == 0) {
		var url = "performancePreDeal.html?assetId=" + assetId + "&typeId=" + typeId + "&alarmId=" + alarmId;
		window.location = url;
	} else if (status == 1) {
		var url = "performanceDeal.html?assetId=" + assetId + "&typeId=" + typeId + "&alarmId=" + alarmId;
		window.location = url;
	} else if (status == 2) {
		var url = "performanceDeal.html?assetId=" + assetId + "&typeId=" + typeId + "&alarmId=" + alarmId;
		window.location = url;
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
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] textStatus: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#alarm_content').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
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
