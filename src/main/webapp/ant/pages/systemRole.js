
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
		'<div class="orderRole_count" style="display:none;">0</div>' +
		'<div class="orderItime_count" style="display:none;">0</div>'
	);
	loadTable();
})

function queryData() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData, begin, offset);
}

function loadTable() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	query(dataJson);
}

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
	if (order == "orderRole") {
		$(".orderRole_count").html(parseInt($(".orderRole_count").html()) + 1);
		orderKey = "role";
		orderValue = parseInt($(".orderRole_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderItime") {
		$(".orderItime_count").html(parseInt($(".orderItime_count").html()) + 1);
		orderKey = "itime";
		orderValue = parseInt($(".orderItime_count").html()) % 2;
		
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
	
	var role = $("#role").val();
	var functions = $("#functions").val();
	if (role != null && role != "") {
		dataJson["role"] = role;
	}
	if (functions != null && functions != "") {
		dataJson["functions"] = functions;
	}

	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url : "/NmsRoleFunction/loadRoleList",
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

function loadList(json) {
	$("#tableBodyID").empty();
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	
	var n = 0;
	for (var i in json.list) {
		// var n = parseInt(i) + 1 + (begin - 1) * offset;
		var action = '<a href="#" onclick="doUpdate(' + json.list[i].id + ')"><i class="my-material-icons">编辑&nbsp;</i></a>';
		    action += '<a href="#" onclick="doDelete(' + json.list[i].id + ')"><i class="my-material-icons">删除</i></a>';
		
		n++;
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'
					+ '<td align="center">' + n + '</td>'
					+ '<td>' + htmlEscape(json.list[i].role) + '</td>'
					+ '<td>' + htmlEscape(json.list[i].functions) + '</td>'
					+ '<td>' + format(json.list[i].itime) + '</td>'
					+ '<td class="center">' + action + '</td>'
			+ '</tr>'
		)
		$('.my-material-icons').css('color', '#22B37A')
	}

	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function doAdd() {
	var url = "systemRoleAdd.html";
	jumpPage(url);
//	window.location.href = url;
}

function doUpdate(id) {
	var url = "systemRoleUpdate.html?id=" + id;
	jumpPage(url);
//	window.location.href = url;
}

function doDelete(roleId) {
	if(!confirm("确认要删除？")){ 
		return;
	}
	if (roleId == null || roleId == "") {
		alert("您删除的角色为空！");
		return;
	}
	var dataJson = {
		"roleId" : roleId
	};
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/NmsRoleFunction/deleteRoleFunction",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 6000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				if (json.state == 0) {
					jumpPage("role.html");
				} else {
					alert(json.info);
				}
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
	$('#role').keydown(function(event) {
		if (event.keyCode == 13) {
			queryData();
		}
	});
});

