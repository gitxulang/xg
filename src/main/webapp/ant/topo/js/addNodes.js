window.onload = function() {
	var data = postListData();
	createList(data);
};

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

function doQuery() {
	var data = postListData($("#key option:selected").val(), $("#value").val());
	createList(data);
}

function doAdd() {
	if ($("[name = checkbox]:checked").length == 0) {
		alert("请至少选择一个设备！");
		return;
	}

	var node_array = new Array();

	$("[name = checkbox]:checked").each(
		function() {
			var dataTemp = JSON.parse($(this).siblings("div").text());
			var container_id=$(this).parent().parent().find("select").val();
			node_array[node_array.length] = dataTemp.id + ","
					+ dataTemp.aip + "," + dataTemp.nmsAssetType.id + ","
					+ dataTemp.aname+ ","+ container_id;
		});

	if (parent.window.addNodes(node_array) == "Success") {
		parent.window.closeDialog();
	}
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

function createList(data) {
	
	var retData=[];
	let mapId = $("#select_topo",parent.document).val()
	$.ajax({
		type : "POST",
		data : {
			mapId: mapId
		},
		dataType : "json",
		url : "/Topo/topoAreas",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			retData = data;
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/topoAreas: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/topoAreas: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
	console.log(retData);
	var azone = '<select id="colled_mode_' + i + '" class="my-form-select" >';
	for (var i = 0; i < retData.list.length; i++) {
		azone = azone + '<option value=' + retData.list[i].id + '>'
				+ retData.list[i].divName + '</option>';
	}
	azone = azone + '</select> ';
	console.log(azone);
	var trBody = "";
	$(".table_body_bg").empty();
	for ( var i = 0; i < data.length; i++) {
		var aname = htmlEscape(data[i].aname);
		var value = JSON.stringify(data[i]);
		trBody += '<tr>'
			+ '<td align="center" height="33" class="table_tr_body_start" width="6%">'
			+ '<input type="checkbox" class="checkbox" name="checkbox" value="" class="noborder">'
			+ '<div style="display:none">'
			+ value
			+ '<div>'
			+ '</td>'
			+ '<td align="left" class="table_tr_body" width="20%">'
			+ aname
			+ '</td>'
			+ '<td align="left" class="table_tr_body" width="22%">'
			+ data[i].ano
			+ '</td>'
			+ '<td align="left" class="table_tr_body" width="15%">'
			+ data[i].aip
			+ '</td>'
			+ '<td align="left" class="table_tr_body" width="25%">'
			+ data[i].nmsAssetType.chSubtype
			+ '</td>'
		+ '<td align="left" class="table_tr_body_end" width="12%">'
				+ azone + '</td>'
		+ '</tr>';
	}
	$(".table_body_bg").html(trBody);
};

function postListData(type, keywords) {
	var retData = "";
	var currentTopoMap = parent.document.getElementById("centertitle").innerHTML;
	$.ajax({
		type : "POST",
		data : {
			current_topo_map : currentTopoMap,
			type : type ? type : "",
			keywords : keywords ? keywords : ""
		},
		dataType : "json",
		url : "/Topo/readyAddNodes",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			retData = data;
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/readyAddNodes: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/readyAddNodes: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
	return retData;
}

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#value').keydown(function(event) {
		if (event.keyCode == 13) {
			doQuery();
		}
	});
});

function doTopoFind() {
	if ($("[name = checkbox]:checked").length == 0) {
		alert("请至少选择一个交换机设备！");
		return;
	}
	var node_array = new Array();
	var flag = 0;
	var value = [];
	$("[name = checkbox]:checked").each(
		function() {
			var dataTemp = JSON.parse($(this).siblings("div").text());
			if (!(dataTemp.nmsAssetType.id == "6"
					|| dataTemp.nmsAssetType.id == "7" || dataTemp.nmsAssetType.id == "8")) {
				flag = 1;
			}
			var temp = {};
			temp["ip"] = dataTemp.aip;
			temp["readcomm"] = dataTemp.rcomm;
			temp["commport"] = dataTemp.authPass;
			temp["username"] = dataTemp.username;
			temp["password"] = dataTemp.password;
			temp["sshport"] = dataTemp.sshport;
			value[value.length] = temp;
		}
	);
	if (flag == 1) {
		alert("请选择交换机，不能选择其它类型设备！");
		return;
	}
	var json = JSON.stringify(value);
	var tname = parent.document.getElementById("centertitle").innerHTML;
	if (tname == null || tname == "") {
		alert("当前拓扑名称非法（未定义或者为空）！");
		return;
	}
	var data = {};
	data["json"] = json;
	data["tname"] = tname;
	$.ajax({
		type: 'POST',
		data: data,
		dataType: "json",
		url: "/Topo/send",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 60000,
		cache: true,
		async: false,
		success: function(json) {
			if (json == null) {
				alert("请求失败！");
			} else {
				alert(json.info);
			}
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/send: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/send: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}



