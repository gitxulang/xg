function createRightMenu(node_id, node_subtype, containerId) {
	var type = node_id.substring(0, 3);
	if (type == "hin") {
		var menuItem = "<div class=\"detail_check_node_out\" "
				+ "onmouseover=\"detailDeleteNodeMenuOver();\" "
				+ "onmouseout=\"detailDeleteNodeMenuOut();\" "
				+ "onclick=\"detailDeleteNodeClick();\">"
				+ "&nbsp;&nbsp;删除节点</div>" +

				"<div class=\"detail_relation_node_out\" "
				+ "onmouseover=\"detailRelationNodeMenuOver();\" "
				+ "onmouseout=\"detailRelationNodeMenuOut();\" "
				+ "onclick=\"detailRelationNodeClick('" + node_id + "');\">"
				+ "&nbsp;&nbsp;关联子图</div>" +

				"<div class=\"detail_cancel_relation_node_out\" "
				+ "onmouseover=\"detailCancelRelationNodeMenuOver();\" "
				+ "onmouseout=\"detailCancelRelationNodeMenuOut();\" "
				+ "onclick=\"detailCancelRelationNodeClick('" + node_id
				+ "');\">" + "&nbsp;&nbsp;取消关联</div>"
				+ "<div class=\"detail_update_node_property_out\" "
				+ "onmouseover=\"detailUpdateNodePropertyMenuOver();\" "
				+ "onmouseout=\"detailUpdateNodePropertyMenuOut();\" "
				+ "onclick=\"detailUpdateNodePropertyClick('" + node_id
				+ "');\">" + "&nbsp;&nbsp;修改名称</div>";
	} else {
		var menuItem = "<div class=\"detail_check_node_out\" "
				+ "onmouseover=\"detailCheckNodeMenuOver();\" "
				+ "onmouseout=\"detailCheckNodeMenuOut();\" "
				+ "onclick=\"detailCheckNodeClick('"
				+ node_id
				+ "','"
				+ node_subtype
				+ "');\">"
				+ "&nbsp;&nbsp;查看详情</div>"
				+ "<div class=\"detail_delete_node_out\" "
				+ "onmouseover=\"detailDeleteNodeMenuOver();\" "
				+ "onmouseout=\"detailDeleteNodeMenuOut();\" "
				+ "onclick=\"detailDeleteNodeClick();\">"
				+ "&nbsp;&nbsp;删除节点</div>"
				+ "<div class=\"detail_relation_node_out\" "
				+ "onmouseover=\"detailRelationNodeMenuOver();\" "
				+ "onmouseout=\"detailRelationNodeMenuOut();\" "
				+ "onclick=\"detailRelationNodeClick('"
				+ node_id
				+ "');\">"
				+ "&nbsp;&nbsp;关联子图</div>"
				+ "<div class=\"detail_cancel_relation_node_out\" "
				+ "onmouseover=\"detailCancelRelationNodeMenuOver();\" "
				+ "onmouseout=\"detailCancelRelationNodeMenuOut();\" "
				+ "onclick=\"detailCancelRelationNodeClick('"
				+ node_id
				+ "');\">"
				+ "&nbsp;&nbsp;取消关联</div>"
				+ "<div class=\"detail_update_node_property_out\" "
				+ "onmouseover=\"detailUpdateNodePropertyMenuOver();\" "
				+ "onmouseout=\"detailUpdateNodePropertyMenuOut();\" "
				+ "onclick=\"detailUpdateNodePropertyClick('"
				+ node_id
				+ "');\">" + "&nbsp;&nbsp;修改名称</div>"

				+ "<div class=\"detail_update_node_property_out\" "
				+ "onmouseover=\"detailUpdateNodePropertyMenuOver();\" "
				+ "onmouseout=\"detailUpdateNodePropertyMenuOut();\" "
				+ "onclick=\"detailUpdateNodeBindingClick('"
				+ node_id
				+ "');\">" + "&nbsp;&nbsp;变更可用域</div>";
	}
	return menuItem;
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

function detailCheckNodeClick(node_id, node_subtype) {
	if (node_id.substring(0, 3) != "hin") {
		showNodeDetail(node_id.substr(3), node_subtype)
		// window.open("/ant/main.html?redirect=performanceDetail.html&id=" + node_id.substr(3) + "&typeid=" + node_subtype, 'newwindow');
	} else {
		alert("该节点为虚拟节点！");
	}

}

function detailDeleteNodeClick() {
	var nodeObjectArray = parent.nodeObjectArray;
	if (window.confirm("确定删除被选中的所有节点吗？")) {
		var nodeIdArray = new Array();

		for ( var i = 0; i < nodeObjectArray.length; i++) {
			var node = nodeObjectArray[i];
			console.log("[DEBUG] node.node_id = " + node.node_id
					+ ", node.selected = " + node.selected);
			if (node.selected) {
				nodeIdArray[nodeIdArray.length] = node.node_id;
			}
		}

		if (nodeIdArray.length == 0) {
			alert("请选择要删除的节点");
			return;
		}

		var nodeIds = nodeIdArray.join(";");
		if (nodeIdArray.length > 0) {
			$.ajax({
				type : "POST",
				data : {
					current_map_name : parent.document.getElementById("centertitle").innerHTML,
					node_ids : nodeIds
				},
				dataType : "json",
				url : "/Topo/deleteNode",
				contentType : "application/x-www-form-urlencoded;charset=utf-8",
				cache : true,
				async : false,
				success : function(data) {
					if (data.state === "0") {
						if (nodeIdArray.length > 0) {
							for ( var i = 0; i < nodeIdArray.length; i++) {
								if (nodeIdArray[i] != null && nodeIdArray[i] != "") {
									parent.deleteNode(nodeIdArray[i]);
								}
							}
						}
					}
				},
				beforeSend: function(xhr) {
					xhr.setRequestHeader("Authorization", getCookie("token"));
				},
				error: function(jqXHR, textStatus, errorThrown) {
		            console.log("[ERROR] /Topo/deleteNode: " + textStatus);
				},
				complete: function(xhr) {
					if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
						var win = window;
						while (win != win.top) {
							win = win.top;
						}
						win.location.href = xhr.getResponseHeader("CONTENTPATH");
					} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
		            	console.log("[DEBUG] /Topo/selectName: no function");
		            	$("body").html(showNoFunction(100, 600));
					}
				}
			});
		}
	}
}

function detailRelationNodeClick(node_id) {
	var nodeObjectArray = parent.nodeObjectArray;
	var current_topo_map = parent.document.getElementById("centertitle").innerHTML;
	parent.relNodeId = node_id;
	if (node_id != null && current_topo_map != null) {
		var relation_map = "";
		for ( var i = 0; i < nodeObjectArray.length; i++) {
			var node = nodeObjectArray[i];

			if (node_id == node.node_id) {
				relation_map = node.relation_map;
				break;
			}
		}
		parent.open_dialog("./realtionSubMap.html?node_id=" + node_id
						+ "&relation_map=" + encodeURI(relation_map), "关联拓扑图", 600, 400);
	}
}

function detailCancelRelationNodeClick(node_id) {
	var current_topo_map = parent.document.getElementById("centertitle").innerHTML;
	if (window.confirm("确定取消关联子图吗？")) {
		if (node_id != null && current_topo_map != null) {
			$.ajax({
				type: "POST",
				dataType: "json",
				url: "/Topo/cancelRelationMap",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data: {
					current_topo_map : current_topo_map,
					node_id : node_id
				},
				cache: true,
				async: false,
				success: function(data) {
					if (data.state === "0") {
						/*for ( var i = 0; i < nodeObjectArray.length; i++) {
							var node = nodeObjectArray[i];
							if (node_id == node.node_id) {
								node.relation_map = "";
								break;
							}
						}
						alert(data.info);*/
						parent.window.location.reload();
					} else {
						alert(data.info);
					}
				},
				beforeSend: function(xhr) {
					xhr.setRequestHeader("Authorization", getCookie("token"));
				},
				error: function(jqXHR, textStatus, errorThrown) {
		            console.log("[ERROR] /Topo/cancelRelationMap: " + textStatus);
				},
				complete: function(xhr) {
					if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
						var win = window;
						while (win != win.top) {
							win = win.top;
						}
						win.location.href = xhr.getResponseHeader("CONTENTPATH");
					} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
		            	console.log("[DEBUG] /Topo/cancelRelationMap: no function");
		            	$("body").html(showNoFunction(100, 600));
					}
				}
			});
		}
	}
}

function detailSearchAlarmNodeClick(node_id) {

}

function detailUpdateNodePropertyClick(node_id) {
	var nodeObjectArray = parent.nodeObjectArray;
	if (node_id != null) {
		for ( var i = 0; i < nodeObjectArray.length; i++) {
			var node = nodeObjectArray[i];
			if (node_id == node.node_id) {
				parent.updNodeAlias = node.text;
				parent.updNodeId = node_id;
				break;
			}
		}
		parent.open_dialog("./updateNodeProperty.html", "修改节点名称", 450, 220);
	}
}


function detailUpdateNodeBindingClick(node_id) {
	var nodeObjectArray = parent.nodeObjectArray;
	if (node_id != null) {
		for ( var i = 0; i < nodeObjectArray.length; i++) {
			var node = nodeObjectArray[i];
			if (node_id == node.node_id) {
				parent.updNodeId = node_id;
				parent.bindNodeId = node.id;
				parent.containerId = node.containerId;
				parent.mapId = $("#select_topo",parent.document).val();
				break;
			}
		}
		parent.open_dialog("./updateBindingNodeProperty.html", "变更可用域", 450, 220);
	}
}
function createLinkRightMenu(link_id) {
	var menuItem = "<div class=\"detail_delete_link_out\" "
			+ "onmouseover=\"detailDeleteLinkMenuOver();\" "
			+ "onmouseout=\"detailDeleteLinkMenuOut();\" "
			+ "onclick=\"detailDeleteLinkClick('" + link_id + "');\">"
			+ "&nbsp;&nbsp;删除链路</div>"
			+ "<div class=\"detail_property_link_out\" "
			+ "onmouseover=\"detailPropertyLinkMenuOver();\" "
			+ "onmouseout=\"detailPropertyLinkMenuOut();\" "
			+ "onclick=\"detailPropertyLinkClick('" + link_id + "');\">"
			+ "&nbsp;&nbsp;链路属性</div>";

	return menuItem;
}

function detailDeleteLinkClick(line_id) {
	var current_topo_map = parent.document.getElementById("centertitle").innerHTML;
	if (window.confirm("确定要删除该链路吗？")) {
		if (line_id != null) {
			$.ajax({
				type : "POST",
				dataType : "json",
				url : "/Topo/deleteLink",
				contentType : "application/x-www-form-urlencoded;charset=utf-8",
				data : {
					current_topo_map : current_topo_map,
					linkId : line_id
				},
				cache : true,
				async : false,
				success : function(data) {
					if (data.state === "0") {
						parent.deleteLink(line_id);
					} else {
						alert(data.info);
					}
				},
				beforeSend: function(xhr) {
					xhr.setRequestHeader("Authorization",
							getCookie("token"));
				},
				error: function(jqXHR, textStatus, errorThrown) {
		            console.log("[ERROR] /Topo/deleteLink: " + textStatus);
				},
				complete: function(xhr) {
					if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
						var win = window;
						while (win != win.top) {
							win = win.top;
						}
						win.location.href = xhr.getResponseHeader("CONTENTPATH");
					} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
		            	console.log("[DEBUG] /Topo/deleteLink: no function");
		            	$("body").html(showNoFunction(100, 600));
					}
				}
			});
		}
	}
}

function detailPropertyLinkClick(link_id) {
	var edgeObjectArray = parent.edgeObjectArray;
	if (link_id != null) {
		for ( var i = 0; i < edgeObjectArray.length; i++) {
			var edge = edgeObjectArray[i];
			if (link_id == edge.id) {
				parent.updLinkId = link_id;
				parent.updLinkWidth = edge.lineWidth;
				break;
			}
		}
		parent.open_dialog("./updateLinkProperty.html", "修改链路属性", 400, 220);
	}
}

function detailCheckNodeMenuOver() {
	window.event.target.className = "detail_check_node_over";
}

function detailCheckNodeMenuOut() {
	window.event.target.className = "detail_check_node_out";
}

function detailDeleteNodeMenuOver() {
	window.event.target.className = "detail_delete_node_over";
}

function detailDeleteNodeMenuOut() {
	window.event.target.className = "detail_delete_node_out";
}

function detailRelationNodeMenuOver() {
	window.event.target.className = "detail_relation_node_over";
}

function detailRelationNodeMenuOut() {
	window.event.target.className = "detail_relation_node_out";
}

function detailCancelRelationNodeMenuOver() {
	window.event.target.className = "detail_cancel_relation_node_over";
}

function detailCancelRelationNodeMenuOut() {
	window.event.target.className = "detail_cancel_relation_node_out";
}

function detailSearchAlarmNodeMenuOver() {
	window.event.target.className = "detail_search_alarm_node_over";
}

function detailSearchAlarmNodeMenuOut() {
	window.event.target.className = "detail_search_alarm_node_out";
}

function detailUpdateNodePropertyMenuOver() {
	window.event.target.className = "detail_update_node_property_over";
}

function detailUpdateNodePropertyMenuOut() {
	window.event.target.className = "detail_update_node_property_out";
}

function detailDeleteLinkMenuOver() {
	window.event.target.className = "detail_delete_link_over";
}

function detailDeleteLinkMenuOut() {
	window.event.target.className = "detail_delete_link_out";
}

function detailPropertyLinkMenuOver() {
	window.event.target.className = "detail_property_link_over";
}

function detailPropertyLinkMenuOut() {
	window.event.target.className = "detail_property_link_out";
}

function showNodeDetail(id, typeid) {
	if (id == null || id == "") {
        alert("您查询的设备ID为空！");
        return;
    }
    if (typeid == null && typeid < 0) {
        alert("您查询的设备typeid为空！");
        return;
    }
    
    let search = '?id=' + id + '&typeid=' + typeid
    let urls = {
        'detail': '../performanceDetail.html',
        'interface': '../performanceInterface.html',
        'soft': '../performanceSoft.html',
        'account': '../performanceAccount.html',
        'alarm': '../performanceAlarm.html',
        'process': '../performanceProcess.html',
        'app': '../performanceApp.html',
        'middleware': '../performanceMiddleware.html',
        'database': '../performanceDatabase.html'
    }
    const WIDTH = 1400, HEIGHT = 600
    let offset = [100, 50]
    
    if(typeid == 22) { // 安全数据库
        $('.performw').remove()
        openDialogPerform(urls.database + search, '安全数据库', WIDTH, HEIGHT, 'auto')
        openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, 'auto', 'performw1')
        $('.performw1').hide()
    } else if(typeid == 23) { // 安全中间件
        $('.performw').remove()
        openDialogPerform(urls.middleware + search, '安全中间件', WIDTH, HEIGHT, 'auto')
        openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, 'auto', 'performw1')
        $('.performw1').hide()
    } else {
        // 其他设备
        if(typeid == 0) {
            $('.performw').remove()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, 'auto')
        } else if(typeid >= 1 && typeid <= 4) { // 专用终端、专用工作站
            $('.performw').hide()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, 'auto')
            openDialogPerform(urls.interface + search, '接口信息', WIDTH, HEIGHT, 'auto', 'performw1')
            openDialogPerform(urls.soft + search, '软件信息', WIDTH, HEIGHT, 'auto', 'performw2')
            //openDialogPerform(urls.account + search, '账号信息', WIDTH, HEIGHT, 'auto', 'performw3')
            openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, 'auto', 'performw3')

            $('.performw1').hide()
            $('.performw2').hide()
            $('.performw3').hide()
            $('.performw4').hide()
        } else if(typeid >= 5 && typeid <= 10) { // 专用服务器、管理服务器、通用服务器
            $('.performw').remove()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, 'auto')
            openDialogPerform(urls.interface + search, '接口信息', WIDTH, HEIGHT, 'auto', 'performw1')
            openDialogPerform(urls.process + search, '进程信息', WIDTH, HEIGHT, 'auto', 'performw2')
            openDialogPerform(urls.soft + search, '软件信息', WIDTH, HEIGHT, 'auto', 'performw3')
            openDialogPerform(urls.app + search, '应用信息', WIDTH, HEIGHT, 'auto', 'performw4')
            openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, 'auto', 'performw5')

            $('.performw1').hide()
            $('.performw2').hide()
            $('.performw3').hide()
            $('.performw4').hide()
            $('.performw5').hide()
        } else if(typeid >= 11 && typeid <= 21) {
            $('.performw').remove()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, 'auto')
            openDialogPerform(urls.interface + search, '接口信息', WIDTH, HEIGHT, 'auto', 'performw1')
            openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, 'auto', 'performw2')

            $('.performw1').hide()
            $('.performw2').hide()
        }
    }
}
