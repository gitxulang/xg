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
											+ '<div class="orderDesc_count" style="display:none;">0</div>'
											+ '<div class="orderOper_count" style="display:none;">0</div>'
											+ '<div class="orderType_count" style="display:none;">0</div>'
											+ '<div class="orderIp_count" style="display:none;">0</div>'
											+ '<div class="orderSubmask_count" style="display:none;">0</div>'
											+ '<div class="orderGateway_count" style="display:none;">0</div>'
											+ '<div class="orderMac_count" style="display:none;">0</div>'
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
	if (order == "orderDesc") {
		$(".orderDesc_count").html(parseInt($(".orderDesc_count").html()) + 1);
		orderKey = "if_descr";
		orderValue = parseInt($(".orderDesc_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderOper") {
		$(".orderOper_count").html(parseInt($(".orderOper_count").html()) + 1);
		orderKey = "if_oper_status";
		orderValue = parseInt($(".orderOper_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderType") {
		$(".orderType_count").html(parseInt($(".orderType_count").html()) + 1);
		orderKey = "if_type";
		orderValue = parseInt($(".orderType_count").html()) % 2;
	} else if (order == "orderIp") {
		$(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
		orderKey = "if_ip";
		orderValue = parseInt($(".orderIp_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderSubmask") {
		$(".orderSubmask_count").html(
				parseInt($(".orderSubmask_count").html()) + 1);
		orderKey = "if_submask";
		orderValue = parseInt($(".orderSubmask_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderGateway") {
		$(".orderGateway_count").html(
				parseInt($(".orderGateway_count").html()) + 1);
		orderKey = "if_gateway";
		orderValue = parseInt($(".orderGateway_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderMac") {
		$(".orderMac_count").html(parseInt($(".orderMac_count").html()) + 1);
		orderKey = "if_physaddr";
		orderValue = parseInt($(".orderMac_count").html()) % 2;
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
		url : "/NetifInfo/list/date",
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
		var ifType = "";
		var ifAdminStatus = "";
		var ifOperStatus = "";

		if (json.list[i].ifType == 6) {
			ifType = "以太网";
		} else if (json.list[i].ifType == 24) {
			ifType = "本地回环";
		} else {
			ifType = "其它";
		}

		if (json.list[i].ifAdminStatus == 1) {
			ifAdminStatus = "up";
		} else if (json.list[i].ifAdminStatus == 2) {
			ifAdminStatus = "down";
		} else if (json.list[i].ifOperStatus == null) {
			ifOperStatus = "--";
		} else {
			ifAdminStatus = "其它";
		}

		if (json.list[i].ifOperStatus == 1) {
			ifOperStatus = "up";
		} else if (json.list[i].ifOperStatus == 2) {
			ifOperStatus = "down";
		} else if (json.list[i].ifOperStatus == null) {
			ifOperStatus = "--";
		} else {
			ifOperStatus = "其它";
		}

		var ifDescr = json.list[i].ifDescr == '' ? '--' : json.list[i].ifDescr;

		var showDescr = ifDescr;
		if (ifDescr.length > 60) {
			showDescr = ifDescr.substring(0, 60);
		}

		var ifIp = json.list[i].ifIp == '' ? '--' : json.list[i].ifIp;
		var ifSubmask = json.list[i].ifSubmask == '' ? '--'
				: json.list[i].ifSubmask;
		var ifGateway = json.list[i].ifGateway == '' ? '--'
				: json.list[i].ifGateway;
		var ifPhysaddr = json.list[i].ifPhysaddr == '' ? '--'
				: json.list[i].ifPhysaddr;

		$("#tableBodyID")
				.append(
						'<tr class="odd gradeX">' + '<td class="center">'
								+ n
								+ '</td>'
								+ '<td>'
								+ showDescr
								+ '</td>'
								+ '<td>'
								+ ifOperStatus
								+ '</td>'
								+ '<td>'
								+ ifType
								+ '</td>'
								+ '<td>'
								+ ifIp
								+ '</td>'
								+ '<td>'
								+ ifSubmask
								+ '</td>'
								+ '<td>'
								+ ifGateway
								+ '</td>'
								+ '<td>'
								+ ifPhysaddr
								+ '</td>'
								+ '<td>'
								+ format(json.list[i].itime)
								+ '</td>'
								+ '<td class="center"><a href="#" value="'
								+ json.list[i].id
								+ '" onclick="openDetail(this)"><i class="my-material-icons" style="color:#22B37A">查看</i></a></td>'
								+ '</tr>')
	}
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function openDetail(obj) {
	var id = $(obj).attr("value");
	var url = "netifInfoDetail.html?id=" + id;
	open_dialog(url, "接口信息详情", 650, 800);
}

function open_dialog(url, title, width, height) {
	index = layer.open({
		type : 2,
		title : title,
		area : [ width + 'px', height + 'px' ],
		fixed : false,
		maxmin : false,
		scrollbar : false,
		content : url
	});
}

function exportTable() {
	var url = "/NetifInfo/list/date/ExportExcel?"
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

