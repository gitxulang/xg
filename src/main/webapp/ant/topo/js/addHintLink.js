window.onload = function() {
	var data = postListData();
	$(".start_node_name").text(data.start_node_name);
	$(".start_node_desc").text(data.start_node_desc);
	$(".end_node_name").text(data.end_node_name);
	$(".end_node_desc").text(data.end_node_desc);
	$("#start_node_id").val(data.start_node_id);
	$("#start_node_name").val(data.start_node_name);
	$("#start_node_desc").val(data.start_node_desc);
	$("#end_node_id").val(data.end_node_id);
	$("#end_node_name").val(data.end_node_name);
	$("#end_node_desc").val(data.end_node_desc);
	$("#current_topo_map").val();
}

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
	var current_topo_map = parent.document.getElementById("centertitle").innerHTML;
	var start_node_id = document.getElementById("start_node_id").value;
	var start_node_type = document.getElementById("start_node_type").value;
	var start_node_name = document.getElementById("start_node_name").value;
	var start_node_desc = document.getElementById("start_node_desc").value;
	var end_node_id = document.getElementById("end_node_id").value;
	var end_node_type = document.getElementById("end_node_type").value;
	var end_node_name = document.getElementById("end_node_name").value;
	var end_node_desc = document.getElementById("end_node_desc").value;
	
	var link_type = document.getElementById("link_type").value;

	parent.addHintLink(current_topo_map, start_node_id, start_node_type,
			start_node_name, start_node_desc, end_node_id, end_node_type,
			end_node_name, end_node_desc, link_type);

	parent.window.closeDialog();
}

function stratNodeTypeChange() {
	var endNodeType = document.getElementById("end_node_type").value;
	if (endNodeType == "开始节点") {
		document.getElementById("end_node_type").value = "结束节点";
	} else {
		document.getElementById("end_node_type").value = "开始节点";
	}
}

function endNodeTypeChange() {
	var startNodeType = document.getElementById("start_node_type").value;
	if (startNodeType == "开始节点") {
		document.getElementById("start_node_type").value = "结束节点";
	} else {
		document.getElementById("start_node_type").value = "开始节点";
	}
}

function closeDialog() {
	parent.window.closeDialog();
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
		alert("请按住 Ctrl键选择两个网元节点！");
		return;
	}

	var start_node_id = nodeSelectedArray[0].node_id;
	var end_node_id = nodeSelectedArray[1].node_id;

	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/readyAddHintLink",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data : {
			current_topo_map : parent.document
					.getElementById("centertitle").innerText,
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
            console.log("[ERROR] /Topo/readyAddHintLink: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/readyAddHintLink: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
	return retData;
}

