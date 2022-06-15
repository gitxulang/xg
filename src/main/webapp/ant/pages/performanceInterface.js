
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
		+ '<div class="orderIfname_count" style="display:none;">0</div>'
		+ '<div class="orderIfspeed_count" style="display:none;">0</div>'
		+ '<div class="orderIfmac_count" style="display:none;">0</div>'
		+ '<div class="orderIfip_count" style="display:none;">0</div>'
		+ '<div class="orderIfstatus_count" style="display:none;">0</div>'
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
	
	if (order == "orderIfname") {
		$(".orderIfname_count").html(parseInt($(".orderIfname_count").html()) + 1);
		orderKey = "if_descr";
		orderValue = parseInt($(".orderIfname_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIfspeed") {
		$(".orderIfspeed_count").html(parseInt($(".orderIfspeed_count").html()) + 1);
		orderKey = "if_speed";
		orderValue = parseInt($(".orderIfspeed_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIfmac") {
		$(".orderIfmac_count").html(parseInt($(".orderIfmac_count").html()) + 1);
		orderKey = "if_physaddr";
		orderValue = parseInt($(".orderIfmac_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIfip") {
		$(".orderIfip_count").html(parseInt($(".orderIfip_count").html()) + 1);
		orderKey = "if_ip";
		orderValue = parseInt($(".orderIfip_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIfstatus") {
		$(".orderIfstatus_count").html(parseInt($(".orderIfstatus_count").html()) + 1);
		orderKey = "if_oper_status";
		orderValue = parseInt($(".orderIfstatus_count").html()) % 2;
		
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

function query(dataJson) {
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;		
	
	var name = $("#name").val();
	if (name != null && name != "") {
		dataJson["if_descr"] = name;
	}

	var ip = $("#ip").val();
	if (ip != null && ip != "") {
		dataJson["if_ip"] = ip;
	}
	
	var mac = $("#mac").val();
	if (mac != null && mac != "") {
		dataJson["if_physaddr"] = mac;
	}
	
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/NetifInfo/ServerDetail/NetifInfo",
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

function loadList(json) {
	$("#tableBodyID").empty();
	
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	
	for (var i in json.list) {
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		var status = "";
		if (json.list[i].status == 1) {
			status = "up";
		} else if (json.list[i].status == 2) {
			status = "down";
		} else if (json.list[i].status == 3) {
			status = "other";
		} else {
			procState = "未知";
		}
		
		var desc = json.list[i].description != "" ? json.list[i].description : "--";
		var mac = json.list[i].mac != "" ? json.list[i].mac : "--";
		var ip = json.list[i].ip != "" ? json.list[i].ip : "--";
		
		if (json.list[i].ifSpeed < 0) {
			json.list[i].ifSpeed = 0;
		}
		
		var discards = parseInt(json.list[i].inDiscards) + parseInt(json.list[i].outDiscards);
		var errors = parseInt(json.list[i].inErrors) + parseInt(json.list[i].outErrors);
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'+
			'<td class="center">' + n + '</td>'+	
			'<td>' + htmlEscape(desc) + '</td>'+
			'<td>' + json.list[i].ifSpeed + '</td>'+
			'<td>' + htmlEscape(mac) + '</td>'+
			'<td>' + htmlEscape(ip) + '</td>'+
			'<td>' + status + '</td>'+
			'<td>' + json.list[i].inletVelocity + '</td>'+
			'<td>' + json.list[i].inletSpeedRate + '</td>'+	
			'<td>' + json.list[i].outVelocity + '</td>'+
			'<td>' + json.list[i].outSpeedRate+'</td>'+		
			'<td>' + discards +'</td>'+		
			'<td>' + errors + '</td>'+		
			'</tr>'
		)
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
	$('#ip').keydown(function(event) {
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
	$('#mac').keydown(function(event) {
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

