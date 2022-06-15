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

$(document)
		.ready(
				function() {
					$("body")
							.append(
									'<div class="current_orderkey" style="display:none;"></div>'
											+ '<div class="current_ordervalue" style="display:none;"></div>'
											+ '<div class="orderMemTotal_count" style="display:none;">0</div>'
											+ '<div class="orderMemFree_count" style="display:none;">0</div>'
											+ '<div class="orderMemBuffer_count" style="display:none;">0</div>'
											+ '<div class="orderMemCache_count" style="display:none;">0</div>'
											+ '<div class="orderSwapTotal_count" style="display:none;">0</div>'
											+ '<div class="orderSwapFree_count" style="display:none;">0</div>'
											+ '<div class="orderSwapCache_count" style="display:none;">0</div>'
											+ '<div class="orderItime_count" style="display:none;">0</div>');

					var request = GetRequest();
					var id = request.id;
					var redirect = request.redirect;
					if (id == null || id == "" || redirect == null || redirect == "") {
						return;
					}
					$("#assetId").val(id);
					loadTab(id, 'net', redirect);
					
					loadTable();
				})

function loadTable() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	query(dataJson);
}

function selectPage() {
	begin = 1;
	offset = $("#selectPage").val();
	requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData);
}

function changeOffset() {
	begin = 1;
	offset = $("#selectPage").val();
	requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData);
}

function firstPage() {
	if ($("#page").html() != "1") {
		begin = 1;
		offset = $("#selectPage").val();
		requestData = {};
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

function setOrderKey(obj) {
	var order = $(obj).attr("id");
	var orderKey = "";
	var orderValue = 0;
	if (order == "orderMemTotal") {
		$(".orderMemTotal_count").html(
				parseInt($(".orderMemTotal_count").html()) + 1);
		orderKey = "mem_total";
		orderValue = parseInt($(".orderMemTotal_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderMemFree") {
		$(".orderMemFree_count").html(
				parseInt($(".orderMemFree_count").html()) + 1);
		orderKey = "mem_free";
		orderValue = parseInt($(".orderMemFree_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderMemBuffer") {
		$(".orderMemBuffer_count").html(
				parseInt($(".orderMemBuffer_count").html()) + 1);
		orderKey = "buffers";
		orderValue = parseInt($(".orderMemBuffer_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderMemCache") {
		$(".orderMemCache_count").html(
				parseInt($(".orderMemCache_count").html()) + 1);
		orderKey = "cached";
		orderValue = parseInt($(".orderMemCache_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderSwapTotal") {
		$(".orderSwapTotal_count").html(
				parseInt($(".orderSwapTotal_count").html()) + 1);
		orderKey = "swap_total";
		orderValue = parseInt($(".orderSwapTotal_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderSwapFree") {
		$(".orderSwapFree_count").html(
				parseInt($(".orderSwapFree_count").html()) + 1);
		orderKey = "swap_free";
		orderValue = parseInt($(".orderSwapFree_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderSwapCache") {
		$(".orderSwapCache_count").html(
				parseInt($(".orderSwapCache_count").html()) + 1);
		orderKey = "swap_cached";
		orderValue = parseInt($(".orderSwapCache_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderItime") {
		$(".orderItime_count")
				.html(parseInt($(".orderItime_count").html()) + 1);
		orderKey = "itime";
		orderValue = parseInt($(".orderItime_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	}

	const
	begin = $("#page").html();
	const
	offset = $("#selectPage").val();
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
	var id = $("#assetId").val();
	dataJson["assetId"] = id;
	
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;

	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();
	if (start_time != "" && start_time != null) {
		dataJson["startDate"] = start_time;
	}

	if (end_time != "" && end_time != null) {
		dataJson["endDate"] = end_time;
	}

	$.ajax({
		type : 'POST',
		data : dataJson,
		dataType : "json",
		url : "/MemInfo/list/date",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
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

function loadList(json) {
	$("#tableBodyID").empty();
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	for ( var i in json.list) {
		var n = parseInt(i) + 1 + (begin - 1) * offset;

		var memTotal = (parseFloat(json.list[i].memTotal) / 1024 / 1024)
				.toFixed(2);
		var memFree = (parseFloat(json.list[i].memFree) / 1024 / 1024)
				.toFixed(2);

		var buffers = (parseFloat(json.list[i].buffers) / 1024 / 1024)
				.toFixed(2);
		var cached = (parseFloat(json.list[i].cached) / 1024 / 1024).toFixed(2);
		var buffers = (parseFloat(json.list[i].buffers) / 1024 / 1024)
				.toFixed(2);
		var swapTotal = (parseFloat(json.list[i].swapTotal) / 1024 / 1024)
				.toFixed(2);
		var swapFree = (parseFloat(json.list[i].swapFree) / 1024 / 1024)
				.toFixed(2);
		var swapCached = (parseFloat(json.list[i].swapCached) / 1024 / 1024)
				.toFixed(2);

		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + '<td class="center">' + n + '</td>' + '<td>'
						+ memTotal + '</td>' + '<td>' + memFree + '</td>'
						+ '<td>' + buffers + '</td>' + '<td>' + cached
						+ '</td>' + '<td>' + swapTotal + '</td>' + '<td>'
						+ swapFree + '</td>' + '<td>'
						+ format(json.list[i].itime) + '</td>' + '</tr>')
	}
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function exportTable() {
	var url = "/MemInfo/list/date/ExportExcel?"
	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();
	var id = $("#assetId").val();
	url += "assetId=" + id + "&";
	if (start_time != '' && start_time != undefined && start_time != null) {
		url += "startDate=" + start_time + "&";
	}
	if (end_time != '' && end_time != undefined && end_time != null) {
		url += "endDate=" + end_time + "&";
	}

	window.location.href = url;
}

$("#start_time").datetimepicker({
	language : "zh-cn",
	format : "yyyy-mm-dd",
	minView : "month",
	autoclose : true,
	clearBtn : true,
	todayBtn : true
});

$("#end_time").datetimepicker({
	language : "zh-cn",
	format : "yyyy-mm-dd",
	minView : "month",
	autoclose : true,
	clearBtn : true,
	todayBtn : true
});

