
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

$(document).ready(function(){
	var request = GetRequest();
	var alarmId = request.alarmId;

	if (alarmId == null || alarmId == "") {
		return;
	}

	$("#alarmId").val(alarmId);
	$('#dtime').val(currentTime());
	$('#fromType').val(request.fromType);
	$('#softId').val(request.softId);
	$('#typeId').val(request.typeId);
	
	loadReport();
})

function loadReport() {
	var id = $("#alarmId").val();
	$.ajax({
		type: 'POST',
		data: {alarmId: id},
		dataType: "json",
		url: "/alarm/soft/findByAlarmId",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				var time = new Date(json.rtime);
				var y = time.getFullYear();
				var m = time.getMonth()+1;
				var d = time.getDate();
				var h = time.getHours();
				var mm = time.getMinutes();
				var s = time.getSeconds();
				time = y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s); 
				
				$("#dtime").html(json.dtime);
				$("#content").html(json.rcontent);
				$("#people").val(json.rpeople);
				
				if (json.nmsAlarmSoft && json.nmsAlarmSoft.dstatus == 2) {
					$("#submitBtn").empty();
				}
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /alarm/soft/findByAlarmId: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /alarm/soft/findByAlarmId: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function add0(m) {
	return m < 10 ? '0' + m : m;
}

function format(time_str) {
	var time = new Date(time_str);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}

function submitReport() {
	var dataJson = {};
	var id = $("#alarmId").val();
	var deal_time = $("#dtime").val();
	var report_content = $("#content").val();
	var report_man = $("#people").val();
	
	if (deal_time == "") {
		alert("请填写解决日期");
		return;
	}
	
	if (report_content == "") {
		alert("请填写报告内容");
		return;
	}
	
	if(report_man == "") {
		alert("请填写报告填写人");
		return;
	}
	
	dataJson["id"] = id;
	dataJson["alarmId"] = id;
	dataJson["rContent"] = report_content;
	dataJson["rPeople"] = report_man;
	dataJson["dTime"] = deal_time;
	
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/alarm/soft/deal/report",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				backAlarm();
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /alarm/soft/deal/report: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /alarm/soft/deal/report: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function currentTime() {
	var today = new Date();
	var h = today.getFullYear();
	var m = today.getMonth() + 1;
	var d = today.getDate();
	var hh = today.getHours();
	var mm = today.getMinutes();
	var ss = today.getSeconds();
	m = m < 10 ? "0" + m : m;
	d = d < 10 ? "0" + d : d;
	hh = hh < 10 ? "0" + hh : hh;
	mm = mm < 10 ? "0" + mm : mm;
	ss = ss < 10 ? "0" + ss : ss;
	return h + "-" + m + "-" + d + " " + hh + ":" + mm + ":" + ss;
}

$("#dtime").datetimepicker({
	language: "zh-cn",
	format: "yyyy-mm-dd hh:ii:ss",
	minView: "month",
	autoclose: true,
	todayBtn: true
});

function backAlarm() {
	var url = "alarmSoftList.html";

	if($('#fromType').val() == 'performance') {
		var softId = $("#softId").val();
		var typeId = $("#typeId").val();
		url = "performanceSoftAlarm.html?id=" + softId + "&typeid=" + typeId;
	}

	window.location.href = url;
}

