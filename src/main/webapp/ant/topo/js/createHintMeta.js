var icon_name = "";
var icon_path = "";
var icon_width = "";
var icon_height = "";

window.onload = function() {
	$("#current_topo_map").val(
			parent.document.getElementById("centertitle").innerText);
	var virtualData = postSelectHintNode();
	createSelect(virtualData);
	changeIcon();
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

function createSelect(virtualData) {
	var option = "";
	for ( var i = 0; i < virtualData.length; i++) {
		option += "<option value=" + JSON.stringify(virtualData[i]) + ">"
				+ virtualData[i].mname + "</option>";
	}
	$("#hint_meta_select").html(option);
}

function save() {
	var meta_name = document.getElementById("meta_name").value;
	var current_topo_map = document.getElementById("current_topo_map").value;
	var avalable_zone = document.getElementById("colled_mode").value;
	if (meta_name == null || meta_name == "") {
		alert("示意网元名称不能为空！");
		return;
	}
	if (current_topo_map == null || current_topo_map == "") {
		alert("当前拓扑名称为空或者不存在请查看！");
		return;
	}
	if (icon_name == "" || icon_path == "" || web_icon_path == ""
			|| icon_width == "" || icon_height == "") {
		alert("当前示意图标为空或者不存在请查看！");
		return;
	}
	parent.addHintMeta(current_topo_map, meta_name, icon_name, icon_path, icon_width, icon_height, avalable_zone);
	closeDialog();
}

function closeDialog() {
	parent.window.closeDialog();
}

function changeIcon() {
	var selectValueArray = new Array();
	if (document.getElementById("hint_meta_select").value != null) {
		selectValueArray = JSON.parse(document.getElementById("hint_meta_select").value);
		icon_name = selectValueArray.mname;
		web_icon_path = selectValueArray.murl;
		icon_path = selectValueArray.murl;
		icon_width = selectValueArray.mwid;
		icon_height = selectValueArray.mhei;
	}
}

function postSelectHintNode() {
	var current_topo_map = parent.document.getElementById("centertitle").innerText;
	var ret;
	$.ajax({
		type : "POST",
		data : {
			current_topo_map : current_topo_map
		},
		dataType : "json",
		url : "/Topo/createHintMeta",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			ret = data;
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/createHintMeta: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/createHintMeta: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
	return ret;
}

