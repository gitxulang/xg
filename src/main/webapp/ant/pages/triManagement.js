
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
			+ '<div class="type_name" style="display:none;">all</div>'
			+ '<div class="manageName_count" style="display:none;">0</div>'
			+ '<div class="manageUrl_count" style="display:none;">0</div>'
			+ '<div class="createTime_count" style="display:none;">0</div>'
			+ '<div class="assetAno_count" style="display:none;">0</div>'
			+ '<div class="assetAtype_count" style="display:none;">0</div>'
		);
		requestData = {};
		requestData["begin"] = 1;
		requestData["offset"] = 10;

		loadAssetType();
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
	if (order == "manageName") {
		$(".manageName_count").html(parseInt($(".manageName_count").html()) + 1);
		orderKey = "manageName";
		orderValue = parseInt($(".manageName_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "manageUrl") {
		$(".manageUrl_count").html(parseInt($(".manageUrl_count").html()) + 1);
		orderKey = "manageUrl";
		orderValue = parseInt($(".manageUrl_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "createTime") {
		$(".createTime_count").html(parseInt($(".createTime_count").html()) + 1);
		orderKey = "createTime";
		orderValue = parseInt($(".createTime_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} /*else if (order == "assetAno") {
		$(".assetAno_count").html(parseInt($(".assetAno_count").html()) + 1);
		orderKey = "nmsAsset.aNo";
		orderValue = parseInt($(".assetAno_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	} else if (order == "assetName") {
		$(".assetName_count").html(parseInt($(".assetName_count").html()) + 1);
		orderKey = "nmsAsset.AName";
		orderValue = parseInt($(".assetName_count").html()) % 2;

		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);
	} else if (order == "assetAtype") {
		$(".assetAtype_count").html(parseInt($(".assetAtype_count").html()) + 1);
		orderKey = "nmsAsset.nmsAssetType.id";
		orderValue = parseInt($(".assetAtype_count").html()) % 2;
		
		$(".current_orderkey").html(orderKey);
		$(".current_ordervalue").html(orderValue);			
	}*/

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
	requestData["nmsAssetAspid"] = $("#value-aspid").val();
	requestData["nmsAssetName"] = $("#value-name").val();
	requestData["nmsAssetType"] = $("#value_type").val();
	requestData["nmsManageName"] = $("#value-management-name").val();
	requestData["nmsManageUrl"] = $("#value-management-url").val();
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
		url: "/triManagement/list/date/condition",
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
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'		
					+ '<td class="center">'
					+ n
					+ '</td><td>'
					+ htmlEscape(json.list[i].nmsAsset.ano)
					+ '</td><td>'
					+ htmlEscape(json.list[i].nmsAsset.aname)
					+ '</td><td>'
					+ htmlEscape(json.list[i].nmsAsset.nmsAssetType.chType)
					+ '/'
					+ htmlEscape(json.list[i].nmsAsset.nmsAssetType.chSubtype)
					+ '</td><td>'
					+ htmlEscape(json.list[i].manageName)
					// + '</td><td><a target="_blank" href="//'+json.list[i].manageUrl+'">'
					+ '</td><td><a href="#" onclick="urlJump(' + "'" + json.list[i].manageUrl + "'" + ')">'
					+ htmlEscape(json.list[i].manageUrl)
					+ '</a></td><td>'
					+ formatDate(new Date(parseInt(json.list[i].createTime)))
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

function urlJump(url) {
	window.open(url);
}

function doAdd() {
	openDialog('triManagementAdd.html', "新增三方管理端", 770, 600);
}
function doUpdate(id) {
	openDialog('triManagementUpdate.html?id=' + id, "更新三方管理端", 770, 600);
}

function doDelete(id) {
	layer.confirm('确认要删除?', function(index) {
		if (id == null || id == "") {
			alert("您删除的三方管理端为空！");
			return;
		}
		var dataJson = {
			"id" : id
		};
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : "/triManagement/list/delete",
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
	$("#value-aspid").val('');
	$("#value-name").val('') ;
	$("#value_type").val('-1');
	$("#value-management-name").val('') ;
	$("#value-management-url").val('') ;
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

function loadAssetType() {
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/assetType/all",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			for (var i in json) {
				if (json[i].nodeTag == "net") {
					$("#value_type").append( '<option value="'+json[i].id+'">'+json[i].chType+'/'+json[i].chSubtype+'</option>' );
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