
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

$(document).ready(function() {
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
		'<div class="current_orderkey" style="display:none;"></div>'
		+ '<div class="current_ordervalue" style="display:none;"></div>'
		+ '<div class="orderSoftName_count" style="display:none;">0</div>'
		+ '<div class="orderSoftVersion_count" style="display:none;">0</div>'
		+ '<div class="orderArchitecture_count" style="display:none;">0</div>'
		+ '<div class="orderProductType_count" style="display:none;">0</div>'
		+ '<div class="orderSm3_count" style="display:none;">0</div>'
		+ '<div class="orderJobId_count" style="display:none;">0</div>'
		+ '<div class="orderDecInfo_count" style="display:none;">0</div>'
		+ '<div class="orderUpdateTime_count" style="display:none;">0</div>'
		+ '<div class="orderUniqueIdent_count" style="display:none;">0</div>'
		+ '<div class="orderPlatformType_count" style="display:none;">0</div>'
		+ '<div class="orderItime_count" style="display:none;">0</div>'
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
	if (order == "orderSoftName") {
		$(".orderSoftName_count").html(parseInt($(".orderSoftName_count").html()) + 1);
		orderKey = "softName";
		orderValue = parseInt($(".orderSoftName_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderSoftVersion") {
		$(".orderSoftVersion_count").html(parseInt($(".orderSoftVersion_count").html()) + 1);
		orderKey = "softVersion";
		orderValue = parseInt($(".orderSoftVersion_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderArchitecture") {
		$(".orderArchitecture_count").html(parseInt($(".orderArchitecture_count").html()) + 1);
		orderKey = "architecture";
		orderValue = parseInt($(".orderArchitecture_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderProductType") {
		$(".orderProductType_count").html(parseInt($(".orderProductType_count").html()) + 1);
		orderKey = "productType";
		orderValue = parseInt($(".orderProductType_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderSm3") {
		$(".orderSm3_count").html(parseInt($(".orderSm3_count").html()) + 1);
		orderKey = "sm3";
		orderValue = parseInt($(".orderSm3_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderJobId") {
		$(".orderJobId_count").html(parseInt($(".orderJobId_count").html()) + 1);
		orderKey = "jobId";
		orderValue = parseInt($(".orderJobId_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderDecInfo") {
		$(".orderDecInfo_count").html(parseInt($(".orderDecInfo_count").html()) + 1);
		orderKey = "decInfo";
		orderValue = parseInt($(".orderDecInfo_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	}  else if (order == "orderUpdateTime") {
		$(".orderUpdateTime_count").html(parseInt($(".orderUpdateTime_count").html()) + 1);
		orderKey = "updateTime";
		orderValue = parseInt($(".orderUpdateTime_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	}  else if (order == "orderUniqueIdent") {
		$(".orderUniqueIdent_count").html(parseInt($(".orderUniqueIdent_count").html()) + 1);
		orderKey = "uniqueIdent";
		orderValue = parseInt($(".orderUniqueIdent_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	}  else if (order == "orderPlatformType") {
		$(".orderPlatformType_count").html(parseInt($(".orderPlatformType_count").html()) + 1);
		orderKey = "platformType";
		orderValue = parseInt($(".orderPlatformType_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	}  else if (order == "orderItime") {
		$(".orderItime_count").html(parseInt($(".orderItime_count").html()) + 1);
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

function query(dataJson){
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;
	var id = $("#assetId").val();
	dataJson["assetId"] = id;
	
	var softName = $("#softName").val();
	if (softName != null && softName != "") {
		dataJson["soft_name"] = softName;
	}

	var productType = $("#productType").val();
	if (productType != null && productType != "") {
		dataJson["product_type"] = productType;
	}
	
	var uniqueIdent = $("#uniqueIdent").val();
	if (uniqueIdent != null && uniqueIdent != "") {
		dataJson["unique_ident"] = uniqueIdent;
	}
	
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/SoftInfo/ServerDetail/SoftInfo",
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
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + 
					'<td class="center">' + n + '</td>' +
					'<td>' + json.list[i].softName + '</td>' + 
					'<td>' + json.list[i].softVersion + '</td>' + 
				/*	'<td>' + json.list[i].architecture + '</td>' + 
					'<td>' + json.list[i].productType + '</td>' + 
					'<td>' + json.list[i].sm3 + '</td>' +
					'<td>' + json.list[i].jobId + '</td>' + 
					'<td>' + json.list[i].decInfo + '</td>' + 
					'<td>' + json.list[i].updateTime + '</td>' + 
					'<td>' + json.list[i].uniqueIdent + '</td>' +
					'<td>' + json.list[i].platformType + '</td>' + */
					'<td>' + format(json.list[i].itime) + '</td>'
			 + '</tr>');
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#name').keydown(function(event) {
		if (event.keyCode == 13) {
			var dataJson = {};
			dataJson["begin"] = 1;
			dataJson["offset"] = 10;
			var id = $("#assetId").val();
			dataJson["assetId"] = id;
			query(dataJson);
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#softName').keydown(function(event) {
		if (event.keyCode == 13) {
			var dataJson = {};
			dataJson["begin"] = 1;
			dataJson["offset"] = 10;
			var id = $("#assetId").val();
			dataJson["assetId"] = id;
			query(dataJson);
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#productType').keydown(function(event) {
		if (event.keyCode == 13) {
			var dataJson = {};
			dataJson["begin"] = 1;
			dataJson["offset"] = 10;
			var id = $("#assetId").val();
			dataJson["assetId"] = id;
			query(dataJson);
		}
	});
});

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#uniqueIdent').keydown(function(event) {
		if (event.keyCode == 13) {
			var dataJson = {};
			dataJson["begin"] = 1;
			dataJson["offset"] = 10;
			var id = $("#assetId").val();
			dataJson["assetId"] = id;
			query(dataJson);
		}
	});
});