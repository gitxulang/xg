
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
			+ '<div class="department_name" style="display:none;">all</div>'
			+ '<div class="type_name" style="display:none;">all</div>'
			+ '<div class="orderName_count" style="display:none;">0</div>'
			+ '<div class="orderIp_count" style="display:none;">0</div>'
			+ '<div class="orderBmIp_count" style="display:none;">0</div>'
			+ '<div class="orderYwIp_count" style="display:none;">0</div>'
			+ '<div class="orderUser_count" style="display:none;">0</div>'
			+ '<div class="orderColled_count" style="display:none;">0</div>'
			+ '<div class="orderItime_count" style="display:none;">0</div>'
			+ '<div class="orderAno_count" style="display:none;">0</div>'
			+ '<div class="orderAtype_count" style="display:none;">0</div>'
		);
/*		// 从浏览器地址栏获取begin,offset,orderKey,orderValue四个参数如果有值则初始化数据列表
		var request = GetRequest();
		var begin = request.begin;
		var offset = request.offset;
		var orderKey = request.orderKey;
		var orderValue = request.orderValue;
		if (!isRealNum(begin)) {
			begin = 1;
		}
		if (!isRealNum(offset)) {
			offset = 10;
		}
		if (isRealNum(orderKey)) {
			$(".current_orderkey").html(orderKey);
		}
		if (isRealNum(orderValue)) {
			$(".current_ordervalue").html(orderValue);
		}*/
		requestData = {};
		requestData["begin"] = 1;
		requestData["offset"] = 10;
		loadData(requestData, 1, 10);
	}
)

function queryData() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData, begin, offset);
}

function changeOffset() {
	var begin = 1;
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	query(requestData, begin, offset);
}

function firstPage() {
	if ($("#page").html() != "1") {
		var begin = 1;
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function beforPage() {
	if ($("#page").html() != "1") {
		var begin = parseInt($("#page").html()) - 1
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function nextPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#page").html()) + 1
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function lastPage() {
	if ($("#page").html() != $("#totalPage").html()) {
		var begin = parseInt($("#totalPage").html())
		var offset = $("#selectPage").val();
		var requestData = {};
		requestData["begin"] = begin;
		requestData["offset"] = offset;
		query(requestData, begin, offset);
	}
}

function setOrderKey(obj) {
	var order = $(obj).attr("id");
	var orderKey = "";
	var orderValue = 0;
	if (order == "orderName") {
		$(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
		orderKey = "AName";
		orderValue = parseInt($(".orderName_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderIp") {
		$(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
		orderKey = "AIp";
		orderValue = parseInt($(".orderIp_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderBmIp") {
		$(".orderBmIp_count").html(parseInt($(".orderBmIp_count").html()) + 1);
		orderKey = "BmIp";
		orderValue = parseInt($(".orderBmIp_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderYwIp") {
		$(".orderYwIp_count").html(parseInt($(".orderYwIp_count").html()) + 1);
		orderKey = "YwIp";
		orderValue = parseInt($(".orderYwIp_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderUser") {
		$(".orderUser_count").html(parseInt($(".orderUser_count").html()) + 1);
		orderKey = "AUser";
		orderValue = parseInt($(".orderUser_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderColled") {
		$(".orderColled_count").html(
				parseInt($(".orderColled_count").html()) + 1);
		orderKey = "colled";
		orderValue = parseInt($(".orderColled_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderItime") {
		$(".orderItime_count")
				.html(parseInt($(".orderItime_count").html()) + 1);
		orderKey = "itime";
		orderValue = parseInt($(".orderItime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderAno") {
		$(".orderAno_count").html(parseInt($(".orderAno_count").html()) + 1);
		orderKey = "ANo";
		orderValue = parseInt($(".orderAno_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "orderAtype") {
		$(".orderAtype_count")
				.html(parseInt($(".orderAtype_count").html()) + 1);
		orderKey = "nmsAssetType.id";
		orderValue = parseInt($(".orderAtype_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	}

	var requestData = {};
	var begin = $("#page").html()
	var offset = $("#selectPage").val();
	requestData["begin"] = begin;
	requestData["offset"] = offset;
	
	if (orderKey != "" && orderValue != "") {
		requestData["orderKey"] = orderKey;
		requestData["orderValue"] = orderValue;
	}
	
	query(requestData, begin, offset);
}

function query_bak(requestData, begin, offset) {
	if ($("#value").val() == "") {
		loadData(requestData, begin, offset);
	} else {
		var key = "";
		var value = $("#value").val();
		if ($("#key").val() == "name") {
			key = "AName";
		}
		
		if ($("#key").val() == "ip") {
			key = "AIp";
		}
		
		if ($("#key").val() == "user") {
			key = "AUser";
		}
		
		requestData["nmsAssetKey"] = key;
		requestData["nmsAssetValue"] = value;
		loadData(requestData, begin, offset);
	}
}
// 新查询方法
function query(requestData, begin, offset) {
		let valueIp = $("#value-ip").val();
		// 2021-02-23 新增保密IP和业务IP
		let valueBmIp = $("#value-bmip").val()
		let valueYwIp = $("#value-ywip").val()
		requestData["nmsAssetValueBmIp"] = valueBmIp
		requestData["nmsAssetValueYwIp"] = valueYwIp
		let valueEquip = $("#value-equip").val();
		let deveiceId = $("#deveice-id").val();
		requestData["nmsAssetValueIp"] = valueIp; 
		requestData["nmsAssetValueEquip"] = valueEquip; 
		requestData["nmsAssetValueDeveiceId"] = deveiceId;
		loadData(requestData, begin, offset);
}

function loadData(dataJson, begin, offset) {
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	dataJson["orderKey"] = orderKey;
	dataJson["orderValue"] = orderValue;		

	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Asset/list/date/condition",
		data: dataJson,
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				loadList(json, begin, offset);
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

function formatDate(now) {
	var year = now.getFullYear();
	var month = '';
	if (parseInt((now.getMonth() + 1) / 10) == 0) {
		var tmp = now.getMonth() + 1;
		month = '0' + tmp;
	} else {
		month = now.getMonth() + 1;
	}

	var date = '';
	if (parseInt(now.getDate() / 10) == 0) {
		var tmp = now.getDate();
		date = '0' + tmp;

	} else {
		date = now.getDate();
	}

	var hour = '';
	if (parseInt(now.getHours() / 10) == 0) {
		var tmp = now.getHours();
		hour = '0' + tmp;
	} else {
		hour = now.getHours();
	}

	var minute = '';
	if (parseInt(now.getMinutes() / 10) == 0) {
		var tmp = now.getMinutes();
		minute = '0' + tmp;
	} else {
		minute = now.getMinutes();
	}

	var second = '';
	if (parseInt(now.getSeconds() / 10) == 0) {
		var tmp = now.getSeconds();
		second = '0' + tmp;
	} else {
		second = now.getSeconds();
	}

	return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
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

function loadList(json, begin, offset) {
	$("#tableBodyID").empty();
	for (var i in json.list) {
		var n = (begin - 1) * offset + parseInt(i) + 1;
		var colled;
		if (json.list[i].colled == "0") {
			colled = "已监控"
		} else {
			colled = "未监控"
		}
		
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'		
					+ '<td class="center">'
					+ n
					+ '</td><td>'
					+ htmlEscape(json.list[i].ano)
					+ '</td><td>'
					+ htmlEscape(json.list[i].aname)
					+ '</td><td>'
					+ htmlEscape(json.list[i].nmsAssetType.chType)
					+ '/'
					+ htmlEscape(json.list[i].nmsAssetType.chSubtype)
					+ '</td><td style="display:none;">'
					+ htmlEscape(json.list[i].auser)
					+ '</td><td>'
					+ htmlEscape(json.list[i].aip)
					+ '</td>'
					/*+'<td>'
					+ htmlEscape(json.list[i].bmIp)
					+ '</td><td>'
					+ htmlEscape(json.list[i].ywIp)
					+ '</td>'*/
					+'<td>'
					+ colled
					+ '</td><td>'
					+ formatDate(new Date(parseInt(json.list[i].itime)))
					+ '</td><td class="center">'
					+ '<a href="#" onclick="doUpdate(' + json.list[i].id + ')"><i class="my-material-icons" style="color:#22B37A;">编辑&nbsp;</i></a>'
					+ '<a href="#" onclick="doDelete(' + json.list[i].id + ')"><i class="my-material-icons" style="color:#22B37A;">删除</i></a>'
					+ '</td>' 
			+ '</tr>'
		)
	}

	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
}

function doAdd() {
/*	var begin = parseInt($("#page").html()) - 1;
	var offset = $("#selectPage").val();
	var orderKey = $(".current_orderkey").html();
	var orderValue = $(".current_ordervalue").html();
	var url = "assetAdd.html?begin=" + begin + "&offset=" + offset + "&orderKey=" + orderKey + "&orderValue=" + orderValue;
*/
	// jumpPage("assetAdd.html");
	openDialog('assetAdd.html', "新增设备", 1050, 500)
//	window.location.href = url;
}
// 使用模板添加数据
function doAddTemplate() {
	openDialog('assetAddTemplate.html', "文件从模板导入", 800, 350)
}

function doUpdate(id) {
//	var url = "assetUpdate.html?id=" + id;
//	window.location.href = url;
	//  jumpPage("assetUpdate.html?id=" + id);
	openDialog('assetUpdate.html?id=' + id, "更新设备信息", 1050, 500)
}

function doDelete(id) {
	layer.confirm('确认要删除?', function(index) {
		if (id == null || id == "") {
			alert("您删除的设备为空！");
			return;
		}
		var dataJson = {
			"id" : id
		};
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : "/Asset/list/delete",
			data : dataJson,
			contentType : 'application/x-www-form-urlencoded;charset=utf-8',
			timeout : 6000,
			cache : true,
			async : true,
			success : function(json) {
				if (json != null) {
					layer.msg('删除成功！', {time: 1000},function(){
						window.location.reload();
					});
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
		layer.close(index);
	}); 
	
}

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	// $('#value').keydown(function(event) {
	// 	if (event.keyCode == 13) {
	// 		queryData();
	// 	}
	// });
	$('#value-ip').keydown(function(event) {
		if (event.keyCode == 13) {
			queryData();
		}
	});
	$('#value-equip').keydown(function(event) {
		if (event.keyCode == 13) {
			queryData();
		}
	});
});
var reset = function(){
	$("#value-ip").val('');
	$("#value-equip").val('') ;
	$("#deveice-id").val('') ;
	$("#value-bmip").val('') ;
	$("#value-ywip").val('') ;
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	}
	queryData();
}

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}