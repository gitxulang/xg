
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
		+ '<div class="orderPid_count" style="display:none;">0</div>'
		+ '<div class="orderPname_count" style="display:none;">0</div>'
		+ '<div class="orderPpath_count" style="display:none;">0</div>'
		+ '<div class="orderPstate_count" style="display:none;">0</div>'
		+ '<div class="orderPcpu_count" style="display:none;">0</div>'
		+ '<div class="orderPmem_count" style="display:none;">0</div>'
		+ '<div class="orderPitime_count" style="display:none;">0</div>'
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
	if (order == "orderPid") {
		$(".orderPid_count").html(parseInt($(".orderPid_count").html()) + 1);
		orderKey = "proc_id";
		orderValue = parseInt($(".orderPid_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
		
	} else if (order == "orderPname") {
		$(".orderPname_count").html(parseInt($(".orderPname_count").html()) + 1);
		orderKey = "proc_name";
		orderValue = parseInt($(".orderPname_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);		
	} else if (order == "orderPpath") {
		$(".orderPpath_count").html(parseInt($(".orderPpath_count").html()) + 1);
		orderKey = "proc_path";
		orderValue = parseInt($(".orderPpath_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);		
	} else if (order == "orderPstate") {
		$(".orderPstate_count").html(parseInt($(".orderPstate_count").html()) + 1);
		orderKey = "proc_state";
		orderValue = parseInt($(".orderPstate_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);		
	} else if (order == "orderPcpu") {
		$(".orderPcpu_count").html(parseInt($(".orderPcpu_count").html()) + 1);
		orderKey = "proc_cpu";
		orderValue = parseInt($(".orderPcpu_count").html()) % 2;
	} else if (order == "orderPmem") {
		$(".orderPmem_count").html(parseInt($(".orderPmem_count").html()) + 1);
		orderKey = "proc_mem";
		orderValue = parseInt($(".orderPmem_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);		
	} else if (order == "orderPitime") {
		$(".orderPitime_count").html(parseInt($(".orderPitime_count").html()) + 1);
		orderKey = "itime";
		orderValue = parseInt($(".orderPitime_count").html()) % 2;
		
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

function query(dataJson){
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;		
	
	var name = $("#name").val();
	if (name != null && name != "") {
		dataJson["proc_name"] = name;
	}

	var pid = $("#pid").val();
	if (pid != null && pid != "") {
		dataJson["proc_id"] = pid;
	}
	
	var path = $("#path").val();
	if (path != null && path != "") {
		dataJson["proc_path"] = path;
	}
	
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/ProcessInfo/ServerDetail/ProcessInfo",
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
		
		var mem = 0;
		if (parseInt(json.list[i].procMem) < 0) {
			mem = 0;
		}else if (parseInt(json.list[i].procMem) > 100) {
			mem = 100;
		}else{
			mem = json.list[i].procMem;
		}
		
		var cpu = 0;
		if (parseInt(json.list[i].procCpu) < 0) {
			cpu = 0;
		}else if(parseInt(json.list[i].procCpu) > 100) {
			cpu = 100;
		}else{
			cpu = json.list[i].procCpu;
		}		
		
		var path = json.list[i].procPath == '' ? '--' : json.list[i].procPath;
		
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'+
			'<td class="center">' + n + '</td>' +	
			'<td>' + json.list[i].procId + '</td>' +
			'<td>' + htmlEscape(json.list[i].procName) + '</td>' +
			'<td>' + htmlEscape(path) + '</td>' +
			'<td>' + procState + '</td>' +
			'<td>' + cpu + '</td>' +
			'<td>' + mem + '</td>' +
			'<td>' + format(json.list[i].itime) + '</td>' +		
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
	$('#pid').keydown(function(event) {
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
	$('#path').keydown(function(event) {
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

