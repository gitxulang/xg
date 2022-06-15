var start_node_index = null;
var start_node_mac = null;
var start_node_speed = null;

var end_node_index = null;
var end_node_mac = null;
var end_node_speed = null;

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

function save() {
	var current_topo_map = document.getElementById("current_topo_map").value;
	var start_node_id = document.getElementById("start_node_id").value;
	var end_node_id = document.getElementById("end_node_id").value;
	var link_name = document.getElementById("link_name").value;
	var start_node_name = $(".start_node_name").text();
	var start_node_ip = $(".start_node_ip").text();
	var end_node_name = $(".end_node_name").text();
	var end_node_ip = $(".end_node_ip").text();

	var start_interface = $("#start_node_index").val();
	var end_interface = $("#end_node_index").val();

	var start_node_index = "";
	var start_node_name = "";
	var start_node_mac = "";
	var start_node_speed = "";

	var end_node_index = "";
	var end_node_name = "";
	var end_node_mac = "";
	var end_node_speed = "";

	var link_type = document.getElementById("link_type").value;
	if (start_interface != null && start_interface != "") {
		var arr = start_interface.split(";");
		if (arr[0] != null) {
			start_node_index = arr[0];
		}

		if (arr[1] != null) {
			start_node_name = arr[1];
		}

		if (arr[2] != null) {
			start_node_mac = arr[2];
		}

		if (arr[3] != null) {
			start_node_speed = arr[3];
		}
	}

	if (end_interface != null && end_interface != "") {
		var arr = end_interface.split(";");
		if (arr[0] != null) {
			end_node_index = arr[0];
		}

		if (arr[1] != null) {
			end_node_name = arr[1];
		}

		if (arr[2] != null) {
			end_node_mac = arr[2];
		}

		if (arr[3] != null) {
			end_node_speed = arr[3];
		}
	}

	if (start_node_id == null || start_node_id == "") {
		alert("开始节点接口为空禁止添加真实链路,请添加虚拟链路！");
		return;
	}

	if (start_node_index == null || start_node_index == "") {
		alert("开始节点接口为空禁止添加真实链路,请添加虚拟链路！");
		parent.window.closeDialog();
		return;
	}

	if (end_node_id == null || end_node_id == "") {
		alert("结束节点接口为空禁止添加真实链路,请添加虚拟链路！");
		parent.window.closeDialog();
		return;
	}

	if (end_node_index == null || end_node_index == "") {
		alert("结束节点接口为空禁止添加真实链路,请添加虚拟链路！");
		parent.window.closeDialog();
		return;
	}

	parent.addEntityLink(current_topo_map, start_node_id, start_node_index,
			start_node_ip, start_node_name, start_node_mac, start_node_speed,
			end_node_id, end_node_index, end_node_ip, end_node_name,
			end_node_mac, end_node_speed, link_name, link_type);

	parent.window.closeDialog();
}

function startPortChange(object) {
	var start_interface = $("#start_node_index").val();
	var start_node_name = "";

	if (start_interface != null && start_interface != "") {
		var arr = start_interface.split(";");
		if (arr[1] != null) {
			start_node_name = arr[1];
		}
	}

	var end_interface = $("#end_node_index").val();
	var end_node_name = "";

	if (end_interface != null && end_interface != "") {
		var arr = end_interface.split(";");
		if (arr[1] != null) {
			end_node_name = arr[1];
		}
	}

	$("#link_name").val(start_node_name + "/" + end_node_name);
}

function endPortChange(object) {
	var start_interface = $("#start_node_index").val();
	var start_node_name = "";

	if (start_interface != null && start_interface != "") {
		var arr = start_interface.split(";");
		if (arr[1] != null) {
			start_node_name = arr[1];
		}
	}

	var end_interface = $("#end_node_index").val();
	var end_node_name = "";

	if (end_interface != null && end_interface != "") {
		var arr = end_interface.split(";");
		if (arr[1] != null) {
			end_node_name = arr[1];
		}
	}

	$("#link_name").val(start_node_name + "/" + end_node_name);
}

function closeDialog() {
	parent.window.closeDialog();
}

window.onload = function() {
	var data = postListData();
	createDom(data);
}

function createDom(data) {
	$(".start_node_name").text(data.start_node_name);
	$(".start_node_ip").text(data.start_node_ip);
	$("#start_node_id").val(data.start_node_id);
	$(".end_node_name").text(data.end_node_name);
	$(".end_node_ip").text(data.end_node_ip);
	$("#end_node_id").val(data.end_node_id);
	$("#current_topo_map").val(data.current_topo_map);

	var startName = "";
	var endName = "";
	if (data.start_interface_string != null) {
		var arr = data.start_interface_string.split("|");
		for ( var i = 0; i < arr.length; i++) {
			if (arr[i] != null && arr[i] != "") {

				var obj = arr[i];
				var objArr = obj.split(";");
				if (objArr[1] != null && objArr[1] != "") {
					startName = objArr[1];
					$("#start_node_index").append(
							'<option value="' + arr[i] + '" selected>'
									+ objArr[1] + '</option>')
				}

			}
		}
	}

	if (data.end_interface_string != null) {
		var arr = data.end_interface_string.split("|");
		for ( var i = 0; i < arr.length; i++) {
			if (arr[i] != null && arr[i] != "") {

				var obj = arr[i];
				var objArr = obj.split(";");
				if (objArr[1] != null && objArr[1] != "") {
					endName = objArr[1];
					$("#end_node_index").append(
							'<option value="' + arr[i] + '" selected>'
									+ objArr[1] + '</option>')
				}

			}
		}
	}

	var linkName = startName + "/" + endName;
	$("#link_name").val(linkName);
}

function postListData() {
	var retData = "";
	var nodeSelectedArray = new Array();
	var nodeObjectArray = parent.nodeObjectArray;

	for ( var i = 0; i < nodeObjectArray.length; i++) {
		var node = nodeObjectArray[i];
		if (node.selected) {
			nodeSelectedArray[nodeSelectedArray.length] = node;
		}
	}

	if (nodeSelectedArray.length != 2) {
		alert("请按住 Ctrl键选择两个真实设备！");
		return;
	}

	if (nodeSelectedArray[0].node_id.substring(0, 3) == "hin") {
		alert("真实链路只能在真实设备上创建, 链路开始设备请选择真实设备！");
		return;
	}

	if (nodeSelectedArray[1].node_id.substring(0, 3) == "hin") {
		alert("真实链路只能在真实设备上创建, 链路结束设备请选择真实设备！");
		return;
	}

	var current_topo_map = parent.document.getElementById("centertitle").innerText;
	var start_node_id = nodeSelectedArray[0].node_id;
	var end_node_id = nodeSelectedArray[1].node_id;
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/readyAddEntityLink",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data : {
			current_topo_map : current_topo_map,
			start_node_id : start_node_id,
			end_node_id : end_node_id
		},
		cache : true,
		async : false,
		success : function(data) {
			retData = data;
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/readyAddEntityLink: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/readyAddEntityLink: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
	return retData;
}

