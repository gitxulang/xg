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

$(document).ready(function(){
	$("body").append(
		'<div class="current_orderkey" style="display:none;"></div>'+
		'<div class="current_ordervalue" style="display:none;"></div>'+
		'<div class="orderType_count" style="display:none;">0</div>'+
		'<div class="orderDesc_count" style="display:none;">0</div>'+
		'<div class="orderUnit_count" style="display:none;">0</div>'+
		'<div class="orderEnable_count" style="display:none;">0</div>'+
		'<div class="orderValue1_count" style="display:none;">0</div>'+
		'<div class="orderValue2_count" style="display:none;">0</div>'+
		'<div class="orderValue3_count" style="display:none;">0</div>'+
		'<div class="orderItime_count" style="display:none;">0</div>'
	);
	
	loadAssetType();
	loadTable();
})

function loadTable() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	query(dataJson);
}

function selectPage() {
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

function setOrderKey(obj){
	var order = $(obj).attr("id");
	var orderKey = "";
	var orderValue = 0;
	
	if (order == "orderType") {
		$(".orderType_count").html(parseInt($(".orderType_count").html()) +1 );
		orderKey = "atype_id";
		orderValue = parseInt($(".orderType_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderDesc") {
		$(".orderDesc_count").html(parseInt($(".orderDesc_count").html()) + 1);
		orderKey = "r_content";
		orderValue = parseInt($(".orderDesc_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderUnit") {
		$(".orderUnit_count").html(parseInt($(".orderUnit_count").html()) + 1);
		orderKey = "r_unit";
		orderValue = parseInt($(".orderUnit_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderEnable") {
		$(".orderEnable_count").html(parseInt($(".orderEnable_count").html()) + 1);
		orderKey = "r_enable";
		orderValue = parseInt($(".orderEnable_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderValue1") {
		$(".orderValue1_count").html(parseInt($(".orderValue1_count").html()) + 1);
		orderKey = "r_value1";
		orderValue = parseInt($(".orderValue1_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderValue2") {
		$(".orderValue2_count").html(parseInt($(".orderValue2_count").html()) + 1);
		orderKey = "r_value2";
		orderValue = parseInt($(".orderValue2_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderValue3") {
		$(".orderValue3_count").html(parseInt($(".orderValue3_count").html()) + 1);
		orderKey = "r_value3";
		orderValue = parseInt($(".orderValue3_count").html()) % 2;
		
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

function query(dataJson){
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;		
	
	var type = $("#a_type").val();
	var content = $("#a_content").val();
	
	dataJson["assetTypeId"] = type;
	if (content != "" && content != null && content != undefined) {
		dataJson["ruleContent"] = content;
	}
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/Rule/findNmsRules",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("#page-inner").html(showNoFunction(100, 600));
			} else {
				loadList(json);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Rule/findNmsRules: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Rule/findNmsRules: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function loadAssetType() {
	$.ajax({
		type: 'POST',
		dataType : "json",
		url: "/assetType/all",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			for (var i in json) {
				$("#a_type").append(
					'<option value="' + json[i].id + '">' + json[i].chType + ' / ' + json[i].chSubtype + '</option>'
				)
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /assetType/all: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /assetType/all: no function");
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
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
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
function updateAlarmRule(id) {
	openDialog("alarmRuleUpdate.html?id=" + id, "阈值规则修改", 800, 600)
}
function loadList(json) {
	$("#tableBodyID").empty();
	var rEnable = '';
	var begin = parseInt(json.page);
	var offset = parseInt($("#selectPage").val());
	var rStart = '';
	for (var i in json.list) {
		var n = parseInt(i) + 1 + (begin - 1) * offset;
		
		if (json.list[i].rEnable == 1) {
			rEnable = '是';
			rStart = '<a href="#" onclick="enableAlarm('+json.list[i].id+',0);"><i class="my-material-icons">禁用</i></a>';
		}
		
		if (json.list[i].rEnable == 0) {
			rEnable = '否';
			rStart = '<a href="#" onclick="enableAlarm('+json.list[i].id+',1);"><i class="my-material-icons">启用</i></a>';
		}
		
		$("#tableBodyID").append(
			'<tr>'+
				'<td class="center">'+ n + '</td>'+
				'<td>' + json.list[i].chtype+' / ' + json.list[i].chSubType + '</td>' +
				'<td>' + htmlEscape(json.list[i].rContent) + '</td>'+
				'<td>' + rEnable + '</td>' +
				'<td>' + json.list[i].rValue1 + '</td>' +
				'<td>' + json.list[i].rValue2 + '</td>' +
		 		'<td>' + json.list[i].rValue3 + '</td>' +
		 		'<td>' + htmlEscape(json.list[i].rUnit) + '</td>'+
				'<td>' + format(json.list[i].itime) + '</td>' +
				'<td class="center"><a href="#" onclick="updateAlarmRule('+json.list[i].id+');"><i class="my-material-icons">编辑</i></a> | '+
				rStart +
				'</td>' +
			'</tr>'
		)
		$('.my-material-icons').css('color', '#22B37A')
	}
	
	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

var enableAlarm = function(id, renable) {
	var dataJson = {};
	dataJson["id"] = id;
	dataJson["rEnable"] = renable;
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Rule/updateNmsRuleMonitorById",
		data: dataJson,
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				if (json) {	
					layer.msg('修改成功！', {time: 2000},function(){
						loadTable();
					});
				} else {
					alert("修改失败！");
				}
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Rule/updateNmsRuleMonitorById: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Rule/findNmsRuleById: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#a_content').keydown(function(event) {
		if (event.keyCode == 13) {
			selectPage();
		}
	});
});

function changeType() {
	selectPage();
}

var reset = function(){
	$("#a_type").val('-1');
	$("#a_content").val('');
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	}
	loadTable()
}

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}