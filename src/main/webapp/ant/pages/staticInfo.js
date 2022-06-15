
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

$(document).ready(
	function() {
		$("body").append(
			'<div class="current_orderkey" style="display:none;"></div>'
					+ '<div class="current_ordervalue" style="display:none;"></div>'
					+ '<div class="orderId_count" style="display:none;">0</div>'
					+ '<div class="orderName_count" style="display:none;">0</div>'
					+ '<div class="orderDesc_count" style="display:none;">0</div>'
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
	}
)

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
	if (order == "orderId") {
		$(".orderId_count").html(parseInt($(".orderId_count").html()) + 1);
		orderKey = "unique_ident";
		orderValue = parseInt($(".orderId_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderName") {
		$(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
		orderKey = "product_name";
		orderValue = parseInt($(".orderName_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderDesc") {
		$(".orderDesc_count").html(parseInt($(".orderDesc_count").html()) + 1);
		orderKey = "sys_name";
		orderValue = parseInt($(".orderDesc_count").html()) % 2;
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

	begin = $("#page").html();
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
		url : "/StaticInfo/list/date",
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

		var showName = (json.list[i].sysName).length > 80 ? (json.list[i].sysName)
				.substring(0, 80)
				: json.list[i].sysName;

		if (showName == null || showName == "") {
			showName = "--";
		}

		if (json.list[i].productName == null || json.list[i].productName == "") {
			json.list[i].productName = "--";
		}

		if (json.list[i].uniqueIdent == null || json.list[i].uniqueIdent == "") {
			json.list[i].uniqueIdent = "--";
		}

		$("#tableBodyID")
				.append(
						'<tr class="odd gradeX">'
								+ '<td class="center">'
								+ n
								+ '</td>'
								+ '<td>'
								+ json.list[i].uniqueIdent
								+ '</td>'
								+ '<td>'
								+ json.list[i].productName
								+ '</td>'
								+ '<td align="center" class="table_tr_body" width="38%" title="'
								+ json.list[i].sysName
								+ '">'
								+ showName
								+ '</td>'
								+ '<td>'
								+ format(json.list[i].itime)
								+ '</td>'
								+ '<td class="center"><a href="#" value="'
								+ json.list[i].id
								+ '" onclick="openDetail(this)"><i class="my-material-icons" style="color:#22B37A;">查看</i></a></td>'
								+ '</tr>')
	}
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function openDetail(obj) {
	var id = $(obj).attr("value");
	var url = "staticInfoDetail.html?id=" + id;
	open_dialog(url, "静态信息详情", 650, 650);
}

function open_dialog(url, title, width, height) {
	index = layer.open({
		type : 2,
		title : title,
		area : [ width + 'px', height + 'px' ],
		fixed : true,
		maxmin : false,
		scrollbar : false,
		content : url
	});
}

function exportTable() {
	var url = "/StaticInfo/list/date/ExportExcel?"
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

