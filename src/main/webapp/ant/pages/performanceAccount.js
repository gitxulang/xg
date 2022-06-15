
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

function showNoException(width, height) {
	return "<td colspan='7'><div style='width:"
		+ width
		+ "%;height:"
		+ height
		+ "px;color: #ff0000;text-align:center; font-size: 24px; line-height: "
		+ height
		+ "px;' >当前ActiveMQ连接异常，待连接正常后更新相关数据</div></td>";			
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
		+ '<div class="orderBiosName_count" style="display:none;">0</div>'
		+ '<div class="orderUserName_count" style="display:none;">0</div>'
		+ '<div class="orderUserRealName_count" style="display:none;">0</div>'
		+ '<div class="orderType_count" style="display:none;">0</div>'
		+ '<div class="orderUniqueIdent_count" style="display:none;">0</div>'
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
	if (order == "orderBiosName") {
		$(".orderBiosName_count").html(parseInt($(".orderBiosName_count").html()) + 1);
		orderKey = "biosName";
		orderValue = parseInt($(".orderBiosName_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderUserName") {
		$(".orderUserName_count").html(parseInt($(".orderUserName_count").html()) + 1);
		orderKey = "userName";
		orderValue = parseInt($(".orderUserName_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderUserRealName") {
		$(".orderUserRealName_count").html(parseInt($(".orderUserRealName_count").html()) + 1);
		orderKey = "userRealName";
		orderValue = parseInt($(".orderUserRealName_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderType") {
		$(".orderType_count").html(parseInt($(".orderType_count").html()) + 1);
		orderKey = "type";
		orderValue = parseInt($(".orderType_count").html()) % 2;
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "orderUniqueIdent") {
		$(".orderUniqueIdent_count").html(parseInt($(".orderUniqueIdent_count").html()) + 1);
		orderKey = "uniqueIdent";
		orderValue = parseInt($(".orderUniqueIdent_count").html()) % 2;
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

function query1(dataJson){
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;
	var id = $("#assetId").val();
	dataJson["assetId"] = id;
	
	var biosName = $("#biosName").val();
	if (biosName != null && biosName != "") {
		dataJson["biosName"] = biosName;
	}
	
	var userName = $("#userName").val();
	if (userName != null && userName != "") {
		dataJson["userName"] = userName;
	}
	
	var userRealName = $("#userRealName").val();
	if (userRealName != null && userRealName != "") {
		dataJson["userRealName"] = userRealName;
	}

	// var type = $("#type").val();
	// if (type != null && type != "") {
	// 	dataJson["type"] = type;
	// }
	
	var uniqueIdent = $("#uniqueIdent").val();
	if (uniqueIdent != null && uniqueIdent != "") {
		dataJson["unique_ident"] = uniqueIdent;
	}
	
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/AccountInfo/ServerDetail/AccountInfo",
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

function loadList1(json){
	$("#tableBodyID").empty();
	
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	
	for (var i in json.list) {
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + 
					'<td class="center">' + n + '</td>' +
					'<td>' + json.list[i].biosName + '</td>' + 
					'<td>' + json.list[i].userName + '</td>' + 
					'<td>' + json.list[i].userRealName + '</td>' + 
					// '<td>' + json.list[i].type + '</td>' + 
					'<td>' + json.list[i].uniqueIdent + '</td>' + 
					'<td>' + format(json.list[i].itime) + '</td>'
			 + '</tr>');
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function query(dataJson){
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {"id":"1"},
		url: "/SecRule/getSSORulesById",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		timeout: 5000,
		async: false,
		success: function(json) {
			// console.log(json.jmsStatus);
			var jmsStatus = json.jmsStatus;
			var orderKey = $(".current_orderkey").html();
			var orderValue = $(".current_ordervalue").html();
			dataJson["orderKey"] = orderKey;
			dataJson["orderValue"] = orderValue;
			var id = $("#assetId").val();
			dataJson["assetId"] = id;
			
			var biosName = $("#biosName").val();
			if (biosName != null && biosName != "") {
				dataJson["biosName"] = biosName;
			}
			
			var userName = $("#userName").val();
			if (userName != null && userName != "") {
				dataJson["userName"] = userName;
			}
			
			var userRealName = $("#userRealName").val();
			if (userRealName != null && userRealName != "") {
				dataJson["userRealName"] = userRealName;
			}
		
			// var type = $("#type").val();
			// if (type != null && type != "") {
			// 	dataJson["type"] = type;
			// }
			
			var uniqueIdent = $("#uniqueIdent").val();
			if (uniqueIdent != null && uniqueIdent != "") {
				dataJson["unique_ident"] = uniqueIdent;
			}
			
			$.ajax({
				type: 'POST',
				data: dataJson,
				dataType: "json",
				url: "/AccountInfo/ServerDetail/AccountInfo",
				contentType: 'application/x-www-form-urlencoded;charset=utf-8',
				timeout: 5000,
				cache: true,
				async: true,
				success: function(json) {
					if (json != null) {
						loadList(json, jmsStatus);
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
		},
		beforeSend: function(xhr) {
	        xhr.setRequestHeader("Authorization", getCookie("token"));
	    },
		error : function() {
			
		}
	})
}

function loadList(json, jmsStatus){
	$("#tableBodyID").empty();
	
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());

	if (jmsStatus == 0) {
		$("#tableBodyID").append("<tr class='odd gradeX'><td colspan='7' class='center'><font color='red'>当前ActiveMQ连接异常，待连接正常后更新相关数据</font></td></tr>");
	}
	
	for (var i in json.list) {
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + 
					'<td class="center">' + n + '</td>' +
					'<td>' + json.list[i].biosName + '</td>' + 
					'<td>' + json.list[i].userName + '</td>' + 
					'<td>' + json.list[i].userRealName + '</td>' + 
					// '<td>' + json.list[i].type + '</td>' + 
					'<td>' + json.list[i].uniqueIdent + '</td>' + 
					'<td>' + format(json.list[i].itime) + '</td>'
			 + '</tr>');
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}


$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#biosName').keydown(function(event) {
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
	$('#userName').keydown(function(event) {
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
	$('#userRealName').keydown(function(event) {
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
	$('#type').keydown(function(event) {
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

