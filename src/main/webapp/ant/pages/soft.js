
function showNoFunction(width, height) {
	return "<div style='width:"
			+ width
			+ "%;height:"
			+ height
			+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
			+ height + "px;' >抱歉，您没有此处权限！</div>";
}

$(document).ready(
	function() {
		$("body").append(
			  '<div class="current_orderkey" style="display:none;"></div>'
			+ '<div class="current_ordervalue" style="display:none;"></div>'
			+ '<div class="department_name" style="display:none;">all</div>'
			+ '<div class="type_name" style="display:none;">all</div>'
			+ '<div class="orderName_count" style="display:none;">0</div>'
			+ '<div class="orderIp_count" style="display:none;">0</div>'
			+ '<div class="orderUser_count" style="display:none;">0</div>'
			+ '<div class="orderColled_count" style="display:none;">0</div>'
			+ '<div class="orderItime_count" style="display:none;">0</div>'
			+ '<div class="orderPort_count" style="display:none;">0</div>'
			+ '<div class="orderAtype_count" style="display:none;">0</div>'
		);
		
		requestData = {};
		requestData["begin"] = 1;
		requestData["offset"] = 10;
		loadData(requestData, 1, 10);
	}
)

function queryData() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData, begin, offset);
}

function changeOffset() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData, begin, offset);
}

function firstPage() {
	if ($("#page").html() != "1") {
		var begin = 1;
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function beforPage() {
	if ($("#page").html() != "1") {
		var begin = parseInt($("#page").html()) - 1
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function nextPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#page").html()) + 1
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function lastPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#totalPage").html())
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function setOrderKey(obj) {
	var order = $(obj).attr("id");
	var orderKey = "";
	var orderValue = 0;
	if (order == "orderName") {
		$(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
		orderKey = "AName";
		orderValue = parseInt($(".orderName_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIp") {
		$(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
		orderKey = "AIp";
		orderValue = parseInt($(".orderIp_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderUser") {
		$(".orderUser_count").html(parseInt($(".orderUser_count").html()) + 1);
		orderKey = "AUser";
		orderValue = parseInt($(".orderUser_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderColled") {
		$(".orderColled_count").html(
				parseInt($(".orderColled_count").html()) + 1);
		orderKey = "colled";
		orderValue = parseInt($(".orderColled_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderItime") {
		$(".orderItime_count")
				.html(parseInt($(".orderItime_count").html()) + 1);
		orderKey = "itime";
		orderValue = parseInt($(".orderItime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderPort") {
		$(".orderPort_count").html(parseInt($(".orderPort_count").html()) + 1);
		orderKey = "APort";
		orderValue = parseInt($(".orderPort_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderAtype") {
		$(".orderAtype_count")
				.html(parseInt($(".orderAtype_count").html()) + 1);
		orderKey = "nmsAssetType.id";
		orderValue = parseInt($(".orderAtype_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	}

	var requestData = {};
	var begin = $("#page").html()
	var offset = $("#selectPage").val();
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	
	if (orderKey != "" && orderValue != "") {
		requestData["orderKey"] = orderKey;
		requestData["orderValue"] = orderValue;
	}
	
	query(requestData, begin, offset);
}

function query(requestData, begin, offset) {
	if ($("#value").val() == "") {
		loadData(requestData, begin, offset);
	} else {
		var key = "";
		var value = $("#value").val();
		if ($("#key").val() == "name") {
			key = "AName";
		}
		
		if ($("#key").val() == "ip") {
			key = "AIp";
		}
		
		if ($("#key").val() == "user") {
			key = "AUser";
		}

		if ($("#key").val() == "port") {
			key = "APort";
		}
		
		requestData["nmsAssetKey"] = key;
		requestData["nmsAssetValue"] = value;
		loadData(requestData, begin, offset);
	}
}

function loadData(dataJson, begin, offset) {
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;		
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Soft/list/date/condition",
		data: dataJson,
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				loadList(json, begin, offset);
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

function formatDate(now) {
	var year = now.getFullYear();
	var month = '';
	if (parseInt((now.getMonth() + 1) / 10) == 0) {
		var tmp = now.getMonth() + 1;
		month = '0' + tmp;
	} else {
		month = now.getMonth() + 1;
	}

	var date = '';
	if (parseInt(now.getDate() / 10) == 0) {
		var tmp = now.getDate();
		date = '0' + tmp;

	} else {
		date = now.getDate();
	}

	var hour = '';
	if (parseInt(now.getHours() / 10) == 0) {
		var tmp = now.getHours();
		hour = '0' + tmp;
	} else {
		hour = now.getHours();
	}

	var minute = '';
	if (parseInt(now.getMinutes() / 10) == 0) {
		var tmp = now.getMinutes();
		minute = '0' + tmp;
	} else {
		minute = now.getMinutes();
	}

	var second = '';
	if (parseInt(now.getSeconds() / 10) == 0) {
		var tmp = now.getSeconds();
		second = '0' + tmp;
	} else {
		second = now.getSeconds();
	}

	return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}

function htmlEscape(text) {
	if (text == null) {
		return "";
	}
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

function loadList(json, begin, offset) {
	$("#tableBodyID").empty();
	for (var i in json.list) {
		var n = (begin - 1) * offset + parseInt(i) + 1;
		var colled;
		if (json.list[i].colled == "0") {
			colled = "已监控"
		} else {
			colled = "未监控"
		}
		
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'		
					+ '<td class="center">'
					+ n
					+ '</td><td>'
					+ htmlEscape(json.list[i].aname)
					+ '</td><td>'
					+ htmlEscape(json.list[i].aip)
					+ '</td><td>'
					+ htmlEscape(json.list[i].aport)
					+ '</td>'
					+ '</td><td>'
					+ htmlEscape(json.list[i].nmsAssetType.chType)
					+ '/'
					+ htmlEscape(json.list[i].nmsAssetType.chSubtype)
					+ '</td><td>'
					+ htmlEscape(json.list[i].auser)
					+ '</td><td>'
					+ colled
					+ '</td><td>'
					+ formatDate(new Date(parseInt(json.list[i].itime)))
					+ '</td><td class="center">'
					+ '<a href="#" onclick="doUpdate(' + json.list[i].id + ')"><i class="my-material-icons">mode_edit</i></a>'
					+ '<a href="#" onclick="doDelete(' + json.list[i].id + ')"><i class="my-material-icons">delete</i></a>'
					+ '</td>' 
			+ '</tr>'
		)
	}

	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function doAdd() {
//	var url = "softAdd.html";
//	window.location.href = url;
	jumpPage("softAdd.html");
}

function doUpdate(id) {
//	var url = "softUpdate.html?id=" + id;
//	window.location.href = url;
	jumpPage("softUpdate.html?id=" + id);
}

function doDelete(id) {
	if (!confirm("确认要删除？")) { 
		return;
	}
	if (id == null || id == "") {
		alert("您删除的软件为空！");
		return;
	}
	var dataJson = {
		"id" : id
	};
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Soft/list/delete",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 6000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
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
	$('#value').keydown(function(event) {
		if (event.keyCode == 13) {
			queryData();
		}
	});
});


