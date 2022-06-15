var index = null;

function open_dialog(url, title, width, height) {
	index = layer.open({
		type : 2,
		title : title,
		area : [ width + "px", height + "px" ],
		fixed : false,
		maxmin : false,
		content : url
	});
}

function closeDialog() {
	layer.close(index);
}

function buttonAddNodesOver() {
	window.event.srcElement.className = "button_addnodes_over";
}

function buttonAddNodesOut() {
	window.event.srcElement.className = "button_addnodes_out";
}

function readyAddNodes(current_topo_map) {
	if (current_topo_map != null) {
		open_dialog("./addNodes.html", "添加真实设备", 1000, 600);
	}
}

function buttonCreateEntityLinkOver() {
	window.event.srcElement.className = "button_create_entity_link_over";
}

function buttonCreateEntityLinkOut() {
	window.event.srcElement.className = "button_create_entity_link_out";
}

function createEntityLink() {
	var nodeSelectedArray = new Array();
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

	open_dialog("./addEntityLink.html", "添加真实链路", 600, 410);

}

function buttonManageTopoAreaOver() {
	// window.event.srcElement.className = "button_create_topo_area_over";
}

function buttonManageTopoAreaOut() {
	// window.event.srcElement.className = "button_create_topo_area_out";
}

function manageTopoArea() {
	open_dialog("./manageTopoArea.html", "管理可用域", 1000, 600);
}

function buttonCreateHintMetaOver() {
	window.event.srcElement.className = "button_create_hint_meta_over";
}

function buttonCreateHintMetaOut() {
	window.event.srcElement.className = "button_create_hint_meta_out";
}

function createHintMeta() {
	open_dialog("./createHintMeta.html", "添加虚拟设备", 500, 300);
}

function buttonCreateHintLinkOver() {
	window.event.srcElement.className = "button_create_hint_link_over";
}

function buttonCreateHintLinkOut() {
	window.event.srcElement.className = "button_create_hint_link_out";
}

function createHintLink() {
	var nodeSelectedArray = new Array();
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

	open_dialog("./addHintLink.html", "添加虚拟链路", 550, 370);
}

function buttonViewOver() {
	window.event.srcElement.className = "button_view_over";
}

function buttonViewOut() {
	window.event.srcElement.className = "button_view_out";
}

function view() {
	window.location.href = "../main.html";
}

function buttonEditMapOver() {
	window.event.srcElement.className = "button_editmap_over";
}

function buttonEditMapOut() {
	window.event.srcElement.className = "button_editmap_out";
}

function editMap() {
	open_dialog("./editMap.html", "修改拓扑名称", 550, 260);
}

function buttonCreateSubMapOver() {
	window.event.srcElement.className = "button_create_submap_over";
}

function buttonCreateSubMapOut() {
	window.event.srcElement.className = "button_create_submap_out";
}

function createSubMap() {
	open_dialog("./createSubMap.html", "创建拓扑视图", 550, 260);
}

function buttonDeleteSubMapOver() {
	window.event.srcElement.className = "button_delete_map_over";
}

function buttonDeleteSubMapOut() {
	window.event.srcElement.className = "button_delete_map_out";
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

function deleteSubMap() {
	if (window.confirm("确定删除该拓扑图吗？")) {
		$.ajax({
			type : "POST",
			data : {
				current_topo_map : $("#centertitle").text()
			},
			dataType : "json",
			url : "/Topo/deleteSubMap",
			contentType : "application/x-www-form-urlencoded;charset=utf-8",
			cache : true,
			async : false,
			success : function(json) {
				if (json.state === "0") {
					alert(json.info);
					window.location.reload();
				} else if (json.state === "3") {
					alert(json.info);
					window.location.reload();
				}
			},
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Authorization", getCookie("token"));
			},
			error: function(jqXHR, textStatus, errorThrown) {
	            console.log("[ERROR] /Topo/deleteSubMap: " + textStatus);
			},
			complete: function(xhr) {
				if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
					var win = window;
					while (win != win.top) {
						win = win.top;
					}
					win.location.href = xhr.getResponseHeader("CONTENTPATH");
				} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
	            	console.log("[DEBUG] /Topo/deleteSubMap: no function");
	            	$("body").html(showNoFunction(100, 600));
				}
			}
		});
	}

}

function buttonRefreshOver() {
	window.event.srcElement.className = "button_refresh_over";
}

function buttonRefreshOut() {
	window.event.srcElement.className = "button_refresh_out";
}

function refreshTopo() {
	window.location.reload();
}

function buttonHorizonArrangeOver() {
	window.event.srcElement.className = "button_horizon_arrange_over";
}

function buttonHorizonArrangeOut() {
	window.event.srcElement.className = "button_horizon_arrange_out";
}

function horizonArrange() {
	var nodeSelectedArray = new Array();
	for ( var i = 0; i < nodeObjectArray.length; i++) {
		var node = nodeObjectArray[i];
		if (node.selected) {
			nodeSelectedArray[nodeSelectedArray.length] = node;
		}
	}

	if (nodeSelectedArray.length < 2) {
		alert("请至少选择两个节点单元进行水平对其排列！");
		return;
	}

	nodeSelectedArray.sort(function(a, b) {
		return a.x > b.x ? 1 : -1;
	});

	var _xWidth = (nodeSelectedArray[nodeSelectedArray.length - 1].x - nodeSelectedArray[0].x)
			/ (nodeSelectedArray.length - 1);
	var _y = nodeSelectedArray[0].y;
	var _x = nodeSelectedArray[0].x;

	for ( var i = 0; i < nodeSelectedArray.length; i++) {
		var node = nodeSelectedArray[i];

		// 更改节点坐标
		node.x = _x + _xWidth * i;
		node.y = _y;

		// 获取节点宽度和高度
		var width = node.width;
		var height = node.height;

		// 更改info坐标
		var divInfo = document.getElementById("cTopo").contentWindow.document
				.getElementById("info_" + node.id);
		divInfo.style.left = parseInt(node.x) > (document.body.clientWidth
				- INFO_WIDTH - parseInt(width)) ? (parseInt(node.x)
				- INFO_WIDTH - parseInt(width))
				: (parseInt(node.x) + parseInt(width));
		divInfo.style.top = parseInt(node.y) > (document.body.clientHeight - INFO_HEIGHT) ? (parseInt(node.y) - INFO_HEIGHT)
				: parseInt(node.y);

		// 更改menu坐标
		var divMenu = document.getElementById("cTopo").contentWindow.document
				.getElementById("menu_" + node.id);
		divMenu.style.left = parseInt(node.x) > (document.body.clientWidth
				- MENU_WIDTH - parseInt(width)) ? (parseInt(node.x)
				- MENU_WIDTH - parseInt(width))
				: (parseInt(node.x) + parseInt(width));
		divMenu.style.top = parseInt(node.y) > (document.body.clientHeight - MENU_HEIGHT) ? (parseInt(node.y) - MENU_HEIGHT)
				: parseInt(node.y);

		// 保存到数据库
		updateSelectedNodeCoor();
	}
}

function buttonVerticalArrangeOver() {
	window.event.srcElement.className = "button_vertical_arrange_over";
}

function buttonVerticalArrangeOut() {
	window.event.srcElement.className = "button_vertical_arrange_out";
}

function verticalArrange() {
	var nodeSelectedArray = new Array();
	for ( var i = 0; i < nodeObjectArray.length; i++) {
		var node = nodeObjectArray[i];
		if (node.selected) {
			nodeSelectedArray[nodeSelectedArray.length] = node;
		}
	}

	if (nodeSelectedArray.length < 2) {
		alert("请至少选择两个节点单元进行垂直对其排列！");
		return;
	}

	nodeSelectedArray.sort(function(a, b) {
		return a.y > b.y ? 1 : -1;
	});

	var _yHeight = (nodeSelectedArray[nodeSelectedArray.length - 1].y - nodeSelectedArray[0].y)
			/ (nodeSelectedArray.length - 1);
	var _x = nodeSelectedArray[0].x;
	var _y = nodeSelectedArray[0].y;

	for ( var i = 0; i < nodeSelectedArray.length; i++) {
		var node = nodeSelectedArray[i];

		// 更改节点坐标
		node.x = _x;
		node.y = _y + _yHeight * i;

		// 获取节点宽度和高度
		var width = node.width;
		var height = node.height;

		// 更改info坐标
		var divInfo = document.getElementById("cTopo").contentWindow.document
				.getElementById("info_" + node.id);
		divInfo.style.left = parseInt(node.x) > (document.body.clientWidth
				- INFO_WIDTH - parseInt(width)) ? (parseInt(node.x)
				- INFO_WIDTH - parseInt(width))
				: (parseInt(node.x) + parseInt(width));
		divInfo.style.top = parseInt(node.y) > (document.body.clientHeight - INFO_HEIGHT) ? (parseInt(node.y) - INFO_HEIGHT)
				: parseInt(node.y);

		// 更改menu坐标
		var divMenu = document.getElementById("cTopo").contentWindow.document
				.getElementById("menu_" + node.id);
		divMenu.style.left = parseInt(node.x) > (document.body.clientWidth
				- MENU_WIDTH - parseInt(width)) ? (parseInt(node.x)
				- MENU_WIDTH - parseInt(width))
				: (parseInt(node.x) + parseInt(width));
		divMenu.style.top = parseInt(node.y) > (document.body.clientHeight - MENU_HEIGHT) ? (parseInt(node.y) - MENU_HEIGHT)
				: parseInt(node.y);

		//保存到数据库
		updateSelectedNodeCoor();
	}
}


function buttonEditAreaOpenOver() {
	window.event.srcElement.className = "button_edit_area_open_over";
}

function buttonEditAreaOpenOut() {
	window.event.srcElement.className = "button_edit_area_open_out";
}

function openEditArea() {
	var obj1 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_1");	
	if (obj1 != null) {
		obj1.style.zIndex = 0;
	}
	
	var obj2 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_2");	
	if (obj2 != null) {
		obj2.style.zIndex = 0;
	}
	
	var obj3 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_3");	
	if (obj3 != null) {
		obj3.style.zIndex = 0;
	}
	
	var obj4 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_4");	
	if (obj4 != null) {
		obj4.style.zIndex = 0;
	}
	
	var obj5 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_5");	
	if (obj5 != null) {
		obj5.style.zIndex = 0;
	}
	
	var obj6 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_6");	
	if (obj6 != null) {
		obj6.style.zIndex = 0;
	}
	
	alert("开启编辑业务域功能开启，请拖动或拉伸区域框进行调整！");
}

function buttonEditAreaCloseOver() {
	window.event.srcElement.className = "button_edit_area_close_over";
}

function buttonEditAreaCloseOut() {
	window.event.srcElement.className = "button_edit_area_close_out";
}

function closeEditArea() {
	var obj1 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_1");	
	if (obj1 != null) {
		obj1.style.zIndex = -1;
	}
	
	var obj2 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_2");	
	if (obj2 != null) {
		obj2.style.zIndex = -1;
	}
	
	var obj3 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_3");	
	if (obj3 != null) {
		obj3.style.zIndex = -1;
	}
	
	var obj4 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_4");	
	if (obj4 != null) {
		obj4.style.zIndex = -1;
	}
	
	var obj5 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_5");	
	if (obj5 != null) {
		obj5.style.zIndex = -1;
	}
	
	var obj6 = document.getElementById("cTopo").contentWindow.document.getElementById("drsElement_6");	
	if (obj6 != null) {
		obj6.style.zIndex = -1;
	}
	
	alert("关闭编辑业务域功能成功！");
}

