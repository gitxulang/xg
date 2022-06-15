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

function showNoException(width, height) {
	return "<td colspan='7'><div style='width:"
		+ width
		+ "%;height:"
		+ height
		+ "px;color: #ff0000;text-align:center; font-size: 24px; line-height: "
		+ height
		+ "px;' >当前ActiveMQ连接异常，待连接正常后更新相关数据</div></td>";			
}


$(document)
		.ready(
				function() {
					$("body")
							.append(
									'<div class="current_orderkey" style="display:none;"></div>'
											+ '<div class="current_ordervalue" style="display:none;"></div>'
											+ '<div class="orderBiosName_count" style="display:none;">0</div>'
											+ '<div class="orderUserName_count" style="display:none;">0</div>'
											+ '<div class="orderUserRealName_count" style="display:none;">0</div>'
											+ '<div class="orderType_count" style="display:none;">0</div>'
											+ '<div class="orderUniqueIdent_count" style="display:none;">0</div>'
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
	} else if (order == "orderItime") {
		$(".orderItime_count").html(parseInt($(".orderItime_count").html()) + 1);
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

function query1(dataJson) {
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
		url : "/AccountInfo/list/date",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				loadList(json, jmsStatus);
			}
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /AccountInfo/list/date: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /AccountInfo/list/date: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function query(dataJson) {
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
				url : "/AccountInfo/list/date",
				contentType : 'application/x-www-form-urlencoded;charset=utf-8',
				timeout : 5000,
				cache : true,
				async : true,
				success : function(json) {
					if (json != null) {
						loadList(json, jmsStatus);
					}
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Authorization", getCookie("token"));
				},
				error: function(jqXHR, textStatus, errorThrown) {
		            console.log("[ERROR] /AccountInfo/list/date: " + textStatus);
				},
				complete: function(xhr) {
					if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
						var win = window;
						while (win != win.top) {
							win = win.top;
						}
						win.location.href = xhr.getResponseHeader("CONTENTPATH");
					} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
		            	console.log("[DEBUG] /AccountInfo/list/date: no function");
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


function loadList1(json, jmsStatus) {
	$("#tableBodyID").empty();
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());

	if (jmsStatus == 0) {
		$("#tableBodyID").append("<tr class='odd gradeX'><td colspan='7' class='center'><font color='red'>ActiveMQ服务连接异常，数据不能完全保持同步，待ActiveMQ服务成功启动后，相关数据自动同步</font></td></tr>");
	}

	
	for ( var i in json.list) {
		var type = "";
		if (json.list[i].type == "000") {
			type = "系统管理员";
		} else if (json.list[i].type == "000") {
			type = "安全保密员";
		} else if (json.list[i].type == "000") {
			type = "安全审计员"
		} else if (json.list[i].type == "000") {
			type = "普通用户"
		}
		
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + 
					'<td class="center">' + n + '</td>' +
					'<td>' + json.list[i].biosName + '</td>' + 
					'<td>' + json.list[i].userName + '</td>' + 
					'<td>' + json.list[i].userRealName + '</td>' + 
					'<td>' + type + '</td>' + 
					'<td>' + json.list[i].uniqueIdent + '</td>' +
					'<td>' + format(json.list[i].itime) + '</td>'
			 + '</tr>');
	}
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function loadList(json, jmsStatus) {
	$("#tableBodyID").empty();
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	
	if (jmsStatus == 0) {
		$("#tableBodyID").append("<tr class='odd gradeX'><td colspan='7' class='center'><font color='red'>当前ActiveMQ连接异常，待连接正常后更新相关数据</font></td></tr>");
	}
	
	for ( var i in json.list) {
		var type = "";
		if (json.list[i].type == "000") {
			type = "普通用户"
		} else if (json.list[i].type == "001") {
			type = "系统管理员";
		} else if (json.list[i].type == "010") {
			type = "安全保密员";
		} else if (json.list[i].type == "100") {
			type = "安全审计员"
		} else if (json.list[i].type == "110") {
			type = "网络系统管理员"
		} else if (json.list[i].type == "101") {
			type = "网路安全保密员"
		} else if (json.list[i].type == "011") {
			type = "网络安全审计员"
		}
		
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		$("#tableBodyID").append(
				'<tr class="odd gradeX">' + 
					'<td class="center">' + n + '</td>' +
					'<td>' + json.list[i].biosName + '</td>' + 
					'<td>' + json.list[i].userName + '</td>' + 
					'<td>' + json.list[i].userRealName + '</td>' + 
					'<td>' + type + '</td>' + 
					'<td>' + json.list[i].uniqueIdent + '</td>' +
					'<td>' + format(json.list[i].itime) + '</td>'
			 + '</tr>');
	}
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}


function exportTable() {
	var url = "/AccountInfo/list/date/ExportExcel?"
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

