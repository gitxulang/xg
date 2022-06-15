
function showNoData(width, height) {
	return "<div style='width:"
			+ width
			+ "%;height:"
			+ height
			+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
			+ height + "px;' >这里暂时没有数据</div>";
}

function showNoFunction(width, height) {
	return "<div style='width:"
			+ width
			+ "%;height:"
			+ height
			+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
			+ height + "px;' >抱歉，您没有此处权限！</div>";
}

$(document).ready(function() {
	$("body").append(
		'<div class="current_orderkey" style="display:none;"></div>' + 
		'<div class="current_ordervalue" style="display:none;"></div>' + 
		'<div class="orderUser_count" style="display:none;">0</div>' + 
		'<div class="orderIp_count" style="display:none;">0</div>' + 
		'<div class="orderContent_count" style="display:none;">0</div>' + 
		'<div class="orderModname_count" style="display:none;">0</div>' + 
		'<div class="orderLogtype_count" style="display:none;">0</div>' + 
		'<div class="orderLogrest_count" style="display:none;">0</div>' + 
		'<div class="orderTime_count" style="display:none;">0</div>'
	);

	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	query(dataJson);
})

function selectPage(){
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
	if (order == "orderUser") {
		$(".orderUser_count").html(parseInt($(".orderUser_count").html()) + 1);
		orderKey = "username";
		orderValue = parseInt($(".orderUser_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderIp") {
		$(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
		orderKey = "ip";
		orderValue = parseInt($(".orderIp_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderContent") {
		$(".orderContent_count").html(
				parseInt($(".orderContent_count").html()) + 1);
		orderKey = "content";
		orderValue = parseInt($(".orderContent_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderModname") {
		$(".orderModname_count").html(
				parseInt($(".orderModname_count").html()) + 1);
		orderKey = "modname";
		orderValue = parseInt($(".orderModname_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderLogtype") {
		$(".orderLogtype_count").html(
				parseInt($(".orderLogtype_count").html()) + 1);
		orderKey = "logtype";
		orderValue = parseInt($(".orderLogtype_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderLogrest") {
		$(".orderLogrest_count").html(
				parseInt($(".orderLogrest_count").html()) + 1);
		orderKey = "logrest";
		orderValue = parseInt($(".orderLogrest_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderTime") {
		$(".orderTime_count").html(parseInt($(".orderTime_count").html()) + 1);
		orderKey = "itime";
		orderValue = parseInt($(".orderTime_count").html()) % 2;

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

	var name = $("#username").val();
	var ip = $("#ip").val();
	var content = $("#content").val();
	var modname = $("#modname").val();
	var logtype = $("#logtype").val();
	var logrest = $("#logrest").val();
	var itime_start = $("#itime_start").val();
	var itime_end = $("#itime_end").val();
	var begin = begin;
	var offset = offset;
	
	if (name != '' && name != undefined && name != null) {
		dataJson["username"] = name;
	}

	if (ip != '' && ip != undefined && ip != null) {
		dataJson["ip"] = ip;
	}

	if (content != '' && content != undefined && content != null) {
		dataJson["content"] = content;
	}

	if (modname != '' && modname != undefined && modname != null) {
		dataJson["modname"] = modname;
	}

	if (logtype != '' && logtype != undefined && logtype != null) {
		dataJson["logtype"] = logtype;
	}

	if (logrest != '' && logrest != undefined && logrest != null) {
		dataJson["logrest"] = logrest;
	}

	if (itime_start != '' && itime_start != undefined && itime_start != null) {
		dataJson["itime_start"] = itime_start;
	}

	if (itime_end != '' && itime_end != undefined && itime_end != null) {
		dataJson["itime_end"] = itime_end;
	}

	$.ajax({
		type : 'POST',
		data : dataJson,
		dataType : "json",
		url : "/AuditLog/list/find",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : false,
		success : function(json) {
			if (json != null) {
				loadList(json);
			}
		},
		beforeSend : function(xhr) {
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
function add0(m) {
	return m < 10 ? '0' + m : m
}

function format(time_str) {
	var time = new Date(time_str);
	var y = time.getFullYear();
	var m = time.getMonth() + 1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm)
			+ ':' + add0(s);
}

function loadList(json) {
	$("#tableBodyID").empty();
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	
	for ( var item in json.list) {
		var n = (begin - 1) * offset + parseInt(item) + 1;
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'
				+ '<td class="center">' + n + '</td>'
				+ '<td>' + htmlEscape(json.list[item].username) + '</td>'
				+ '<td>' + htmlEscape(json.list[item].ip) + '</td>'
				+ '<td>' + htmlEscape(json.list[item].modname) + '</td>'
				+ '<td title="' + json.list[item].content + '">' + htmlEscape(json.list[item].content) + '</td>'
				+ '<td>' + htmlEscape(json.list[item].logtype) + '</td>'
				+ '<td>' + htmlEscape(json.list[item].logrest) + '</td>'
				+ '<td class="center">' + format(json.list[item].itime) + '</td>'
			+ '</tr>'
		)
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function add0(m) {
	return m < 10 ? '0' + m : m;
}

function format(time_str) {
	var time = new Date(time_str);
	var y = time.getFullYear();
	var m = time.getMonth() + 1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}

function exportTable() {
	var url = "/AuditLog/exportExcel?"
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	var name = $("#username").val();
	var ip = $("#ip").val();
	var content = $("#content").val();
	var modname = $("#modname").val();
	var logtype = $("#logtype").val();
	var logrest = $("#logrest").val();
	var itime_start = $("#itime_start").val();
	var itime_end = $("#itime_end").val();

	if (name != '' && name != undefined && name != null) {
		url += "username=" + name + "&";
	}
	if (ip != '' && ip != undefined && ip != null) {
		url += "ip=" + ip + "&";
	}
	if (content != '' && content != undefined && content != null) {
		url += "content=" + content + "&";
	}
	if (modname != '' && modname != undefined && modname != null) {
		url += "modname=" + modname + "&";
	}
	if (logtype != '' && logtype != undefined && logtype != null) {
		url += "logtype=" + logtype + "&";
	}
	if (logrest != '' && logrest != undefined && logrest != null) {
		url += "logrest=" + logrest + "&";
	}
	if (orderKey != '' && orderKey != undefined && orderKey != null) {
		url += "orderKey=" + orderKey + "&";
	}
	if (orderValue != '' && orderValue != undefined && orderValue != null) {
		url += "orderValue=" + orderValue + "&";
	}
	if (itime_start != '' && itime_start != undefined && itime_start != null) {
		url += "itime_start=" + itime_start + "&";
	}
	if (itime_end != '' && itime_end != undefined && itime_end != null) {
		url += "itime_end=" + itime_end + "&";
	}

	window.location.href = url;
}

function importTable() {
	var url = "/AuditLog/importExcel"
	var importFile = $('#importFile')[0].files[0];
	if (null == importFile) {
		alert("您尚未选择导入的xls文件")
		return;
	}
	var formData = new FormData();
	formData.append("importFile", importFile);
	$.ajax({
		url : '/AuditLog/importExcel',
		dataType : 'json',
		type : 'POST',
		async : false,
		timout : 60 * 1000,
		data : formData,
		processData : false,
		contentType : false,
		success : function(data) {
			var code = data.code;
			if (code == 200) {
				alert("导入成功!");
				window.location.reload(true);
			} else {
				alert(data.message);
			}
		},
		beforeSend : function(xhr) {
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

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#username').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#ip').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#content').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#modname').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#logtype').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#logrest').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

// $("#itime_start").datetimepicker({
// 	language : "zh-cn",
// 	// format: "yyyy-mm-dd hh:ii:ss",
// 	format: "yyyy-mm-dd hh:ii:00",
// 	// minView: "month",
// 	minView : "hour",
// 	autoclose : true,
// 	todayBtn : true
// });

// $("#itime_end").datetimepicker({
// 	language : "zh-cn",
// 	// format: "yyyy-mm-dd hh:ii:ss",
// 	format: "yyyy-mm-dd hh:ii:59",
// 	// minView: "month",
// 	minView : "hour",
// 	autoclose : true,
// 	todayBtn : true
// });
jeDate("#itime_start",{
	theme:{bgcolor:"#00A1CB",pnColor:"#00CCFF"},
	format: "YYYY-MM-DD hh:mm:ss"
});

jeDate("#itime_end",{
	theme:{bgcolor:"#00A1CB",pnColor:"#00CCFF"},
	format: "YYYY-MM-DD hh:mm:ss"
});

var reset = function(){
	$("#username").val('');
	$("#ip").val('');
	$("#content").val('');
	$("#modname").val('');
	$("#logtype").val('');
	$("#logrest").val('');
	$('#itime_start').val("");
	$('#itime_start').datetimepicker('update');
	$('#itime_end').val("");
	$('#itime_end').datetimepicker('update');
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	}

	query({"begin": 1, "offset": 10});
}

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}