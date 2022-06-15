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
											+ '<div class="orderPid_count" style="display:none;">0</div>'
											+ '<div class="orderName_count" style="display:none;">0</div>'
											+ '<div class="orderPath_count" style="display:none;">0</div>'
											+ '<div class="orderStatus_count" style="display:none;">0</div>'
											+ '<div class="orderCpu_count" style="display:none;">0</div>'
											+ '<div class="orderMem_count" style="display:none;">0</div>'
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
	if (order == "orderPid") {
		$(".orderPid_count").html(parseInt($(".orderPid_count").html()) + 1);
		orderKey = "proc_id";
		orderValue = parseInt($(".orderPid_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderName") {
		$(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
		orderKey = "proc_name";
		orderValue = parseInt($(".orderName_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderPath") {
		$(".orderPath_count").html(parseInt($(".orderPath_count").html()) + 1);
		orderKey = "proc_path";
		orderValue = parseInt($(".orderPath_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderStatus") {
		$(".orderStatus_count").html(
				parseInt($(".orderStatus_count").html()) + 1);
		orderKey = "proc_state";
		orderValue = parseInt($(".orderStatus_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderCpu") {
		$(".orderCpu_count").html(parseInt($(".orderCpu_count").html()) + 1);
		orderKey = "proc_cpu";
		orderValue = parseInt($(".orderCpu_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderMem") {
		$(".orderMem_count").html(parseInt($(".orderMem_count").html()) + 1);
		orderKey = "proc_mem";
		orderValue = parseInt($(".orderMem_count").html()) % 2;
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
		url : "/ProcessInfo/list/date",
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
		var procState = "";
		if (json.list[i].procState == 0) {
			procState = "running";
		} else if (json.list[i].procState == 1) {
			procState = "stopped";
		} else if (json.list[i].procState == 2) {
			procState = "sleeping";
		} else if (json.list[i].procState == 3) {
			procState = "zombie";
		} else {
			procState = "未知";
		}

		var procName = json.list[i].procName == '' ? '--'
				: json.list[i].procName;
		var procPath = json.list[i].procPath == '' ? '--'
				: json.list[i].procPath;
		var showPath = procPath;
		if (procPath.length > 80) {
			showPath = procPath.substring(0, 80);
		}

		if (json.list[i].procCpu < 0) {
			json.list[i].procCpu = 0;
		}

		if (json.list[i].procMem < 0) {
			json.list[i].procMem = 0
		}

		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + '<td class="center">' + n + '</td>' + '<td>'
						+ json.list[i].procId + '</td>' + '<td>' + procName
						+ '</td>' + '<td>' + showPath + '</td>' + '<td>'
						+ procState + '</td>' + '<td>' + json.list[i].procCpu
						+ '</td>' + '<td>' + json.list[i].procMem + '</td>'
						+ '<td>' + format(json.list[i].itime) + '</td>'
						+ '</tr>')
	}
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function exportTable() {
	var url = "/ProcessInfo/list/date/ExportExcel?"
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
	clearBtn : true,
	todayBtn : true
});

$("#end_time").datetimepicker({
	language : "zh-cn",
	format : "yyyy-mm-dd",
	minView : "month",
	autoclose : true,
	clearBtn : true,
	clearBtn : true,
	todayBtn : true
});

