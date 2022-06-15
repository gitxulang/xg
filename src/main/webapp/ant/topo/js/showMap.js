var stage, scene;
var viewFlag = 0;
var relNodeId = null;
var updNodeId = null;
var updNodeAlias = null;
var updLinkId = null;
var updLinkWidth = null;
var START_INIT_X = 10;
var START_INIT_Y = 60;
var INFO_WIDTH = 200;
var INFO_HEIGHT = 210;
var LINE_INFO_WIDTH = 200;
var LINE_INFO_HEIGHT = 400;
var MENU_WIDTH = 110;
var MENU_HEIGHT = 160;
var nodeObjectArray = new Array();
var edgeObjectArray = new Array();

var containers = [];

var topoNodesTable = function(id, map_id, node_id, node_subtype, img, x, y, width, height, ip,
		alias, info, relation_map, container_id) {
	this.id = id;
	this.map_id = map_id;
	this.nodeId = node_id;
	this.node_subtype = node_subtype;
	this.img = img;
	this.x = x;
	this.y = y;
	this.width = width,
	this.height = height,
	this.ip = ip;
	this.alias = alias;
	this.info = info;
	this.relation_map = relation_map;
	this.containerId = container_id;
}

var topoLinksTable = function(id, map_id, start_node_id, start_index, start_ip,
		start_descr, start_mac, end_node_id, end_index, end_ip, end_descr, end_mac,
		max_speed, link_type, link_name, link_info, link_dash, link_width, link_offset, link_col1, link_col2) {
	this.id = id;
	this.map_id = map_id;
	this.start_node_id = start_node_id;
	this.start_index = start_index;
	this.start_ip = start_ip;
	this.start_descr = start_descr;
	this.start_mac = start_mac;
	this.end_node_id = end_node_id;
	this.end_index = end_index;
	this.end_ip = end_ip;
	this.end_descr = end_descr;
	this.end_mac = end_mac;
	this.max_speed = max_speed;
	this.link_type = link_type;
	this.link_name = link_name;
	this.link_info = link_info;
	this.link_dash = link_dash;
	this.link_width = link_width;
	this.link_offset = link_offset;
	this.link_col1 = link_col1;
	this.link_col2 = link_col2;
}

var nodeRefreshObject = function(node_id, node_ip, image_src, font_color, node_subtype) {
	this.node_id = node_id;
	this.node_ip = node_ip;
	this.image_src = image_src;
	this.font_color = font_color;
	this.node_subtype = node_subtype;
}

var linkRefreshObject = function(id, start_node_id, start_index, end_node_id, end_index, link_color, link_dash, link_type) {
	this.id = id;
	this.start_node_id = start_node_id;
	this.start_index = start_index;
	this.end_node_id = end_node_id;
	this.end_index = end_index;	
	this.link_color = link_color;
	this.link_dash = link_dash;
	this.link_type = link_type;
}

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

function replaceNodeObject(data) {
    if (data != null && data.length > 0) {
    	for (var i = 0; i < data.length; i++) {
    		var obj = data[i];
    		if (obj.node_id.indexOf("hin") < 0) {
    			for (var i = 0; i < nodeObjectArray.length; i++) {
    				var node = nodeObjectArray[i];
    				if (node.node_id == obj.node_id) {
    					break;
    				}
    			}
	    		if(obj.image_src == "deleted") {
	    			node.fontColor = "190,190,190";
	    		} else {
	    			node.setImage(obj.image_src);
	    			if (obj.font_color == "green") {
	    				node.fontColor = "0,255,127";
	    			}
	    			if (obj.font_color == "yellow") {
	    				node.fontColor = "255,255,0";
	    			}
	    			if (obj.font_color == "orange") {
	    				node.fontColor = "250,128,10";
	    			}
	    			if (obj.font_color == "red") {
	    				node.fontColor = "255,0,0";
	    			}
	    		}
    		}
    	}
    }
}

function replaceLinkObject(data) {
    if (data != null && data.length > 0) {
    	for (var i = 0; i < data.length; i++) {
	    	var obj = data[i];
			for (var i = 0; i < edgeObjectArray.length; i++) {
				var edge = edgeObjectArray[i];
				if (edge.id == obj.id) {
					break;
				}
			}
			// 如果不是真实链路则跳过
			if (obj.link_type == null || obj.link_type != "0") {
				continue;
			}

			if (obj.link_color == "deleted") {
				edge.strokeColor = "190,190,190";
    		} else if (obj.link_color == "green") {
				edge.strokeColor = "0,255,127";
			} else if (obj.link_color == "red") {
				edge.strokeColor = "220,20,60";
			} else if (obj.link_color == "yellow") {
				edge.strokeColor = "255,255,113";
			} else {
				// 初始化默认的颜色(服务器绿实线,服务器黄实线,终端机绿实线,终端机黄虚线)
				edge.strokeColor = obj.link_color;
			}
    	}
    }
}

function autoRefresh() {
	var current_map_name = $("#centertitle").text();
	var nodeArray = new Array();
	for (var i = 0; i < nodeObjectArray.length; i++) {
		var node = nodeObjectArray[i];
		nodeArray[nodeArray.length] = new nodeRefreshObject(node.node_id, node.node_ip, node.image_src, '', node.node_subtype);
	}
	
	$.ajax({
		type : "POST",
		data: {
			nodeArray: JSON.stringify(nodeArray),
			current_map_name: current_map_name
		},
		dataType : "json",
		url : "/Topo/refreshImage",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			if (data != null) {
				replaceNodeObject(data.value);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/refreshImage: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/refreshImage: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
    
    var linkArray = new Array();
	for (var i = 0; i < edgeObjectArray.length; i++) {
		var edge = edgeObjectArray[i];
		linkArray[linkArray.length] = new linkRefreshObject(edge.id, edge.start_node_id, edge.start_index,
					edge.end_node_id, edge.end_index, "", edge.link_dash, edge.link_type);
	}   
	$.ajax({
		type : "POST",
		data: {
			linkArray: JSON.stringify(linkArray),
			current_map_name: current_map_name
		},
		dataType : "json",
		url : "/Topo/refreshLink",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			replaceLinkObject(data.value);
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/refreshLink: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/refreshLink: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function findNodeObjByNodeId(node_id) {
	for ( var i = 0; i < nodeObjectArray.length; i++) {
		var node = nodeObjectArray[i];
		if (node_id == node.node_id) {
			return node;
		}
	}
	return null;
}

function createNodeInfoDiv(id, x, y, width, height) {
	var divInfo = document.createElement("div");
	divInfo.id = "info_" + id;
	divInfo.style.position = "absolute";
	divInfo.style.border = "solid #0f4b4d 1px";
	divInfo.style.width = INFO_WIDTH;
	divInfo.style.height = INFO_HEIGHT;
	divInfo.style.color = '#4083FF' //"#1fffff";
	divInfo.style.padding = "6px";
	divInfo.style.display = "block";
	divInfo.style.lineHeight = "145%";
	divInfo.style.zIndex = 2;
	divInfo.style.filter = "alpha(opacity=80)";
	divInfo.style.backgroundColor = "#001a1c";
	divInfo.style.left = parseInt(x) > (document.body.clientWidth- INFO_WIDTH - parseInt(width)) ? 
			(parseInt(x) - INFO_WIDTH) + "px"  : (parseInt(x) + parseInt(width)) + "px" ;
	divInfo.style.top =  parseInt(y) > (document.body.clientHeight - INFO_HEIGHT) ? 
			(parseInt(y) - INFO_HEIGHT) + "px"  : parseInt(y)  + "px" ;
	divInfo.style.display = "none";
	divInfo.style.fontSize = "14px";
	divInfo.style.fontFamily = "黑体";
	divInfo.innerHTML = "";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divInfo);
}

function createNodeMenuDiv(id, node_id, node_subtype, x, y, width, height) {
	var divMenu = document.createElement("div");
	divMenu.id = "menu_" + id;
	divMenu.style.position = "absolute";
	divMenu.style.width = MENU_WIDTH;
	divMenu.style.height = "auto";
	divMenu.style.zIndex = 2;
	divMenu.style.left = parseInt(x) > (document.body.clientWidth- MENU_WIDTH - parseInt(width)) ? 
			(parseInt(x) - MENU_WIDTH) + "px" : (parseInt(x) + parseInt(width) + "px");
	divMenu.style.top =  parseInt(y) > (document.body.clientHeight - MENU_HEIGHT) ? 
			(parseInt(y) - MENU_HEIGHT) + "px" : parseInt(y) + "px";
	divMenu.style.border = "solid #0f4b4d 0px";
	divMenu.style.filter = "alpha(opacity=90)";
	divMenu.style.backgroundColor = "#ffffff";
	divMenu.style.display = "none";
	divMenu.innerHTML = createRightMenu(node_id, node_subtype);
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divMenu);	
}

function updateNodeCoor(node_id, x, y) {
	if(node_id != null && x != null && y != null) {
		var current_topo_map = $("#centertitle").html();
		var x = parseInt(x);
		var y = parseInt(y);
		$.ajax({
			type : "POST",
			dataType : "json",
			url : "/Topo/updateEntityNodeToTable",
			contentType : "application/x-www-form-urlencoded;charset=utf-8",
			data: {
				current_topo_map: current_topo_map,
				node_id: node_id,
				x: x,
				y: y
			},
			cache : true,
			async : false,
			success : function(data) {
				if (data.state === "3"){
					alert(data.info);
				}
			},
			beforeSend: function(xhr) {
	             xhr.setRequestHeader("Authorization", getCookie("token"));
	        },
			error: function(jqXHR, textStatus, errorThrown) {
	            console.log("[ERROR] /Topo/updateEntityNodeToTable: " + textStatus);
			},
			complete: function(xhr) {
				if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
					var win = window;
					while (win != win.top) {
						win = win.top;
					}
					win.location.href = xhr.getResponseHeader("CONTENTPATH");
				} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
	            	console.log("[DEBUG] /Topo/updateEntityNodeToTable: no function");
	            	$("body").html(showNoFunction(100, 600));
				}
			}
		});
	}
}

function updateSelectedNodeCoor() {
	for ( var i = 0; i < nodeObjectArray.length; i++) {
		var node = nodeObjectArray[i];
		if (node.selected) {
			updateNodeCoor(node.node_id, node.x, node.y);
		}
	}
}
//渲染节点
function addJTopoNode(stage, scene, id, node_id, node_subtype, img_src, 
		x, y, width, height, ip, alias, relation_map, containerId) {
	id = parseInt(id);
	x = parseInt(x);
	y = parseInt(y);
	width = parseInt(width);
	height = parseInt(height);

	// 通过传递来的viewFlag标志判断显示的是名称还是ip地址
	var text = "";
	if (viewFlag == 0) {
		text = alias;
	} else {
		text = ip;
	}
	
	// 创建JTopo节点对象Node并设置属性值
	var node = new JTopo.Node();
	node.id = "node_" + id;
	node.node_id = node_id;
	node.setBound(x, y, width, height);
	node.setImage(img_src);
	node.text = text;
	node.fontColor = "0,255,127";
	node.font = "17px 黑体";
	node.relation_map = relation_map;
	node.node_ip = ip;
	node.image_src = img_src;
	node.node_subtype = node_subtype;
	node.containerId = containerId;
//	node.zIndex = 2;

	// 创建鼠标左键点击显示设备Tip信息div
	createNodeInfoDiv(node.id, x, y, width, height);
	
	// 添加鼠标左键单击事件
	node.click(function(event) {
		if (this.node_id.substring(0, 3) != "hin") {
			$.ajax({
				type : "POST",
				data: {
					current_map_name: $("centertitle").text(),	
					node_id: node_id
				},
				dataType : "json",
				url : "/Topo/getShowNodeMessage",
				contentType : "application/x-www-form-urlencoded;charset=utf-8",
				cache : true,
				async : false,
				success : function(data) {
					if (data.state == 0) {
						var infoObj = document.getElementById("cTopo").contentWindow.document.getElementById("info_" + node.id);
						infoObj.innerHTML = data.info;
						
						// 更新dataSoft信息
						// var softObj = document.getElementById("cTopo").contentWindow.document.getElementById("_software_");
						// if (data.soft == null || data.soft == "") {
						// 	softObj.innerHTML = "";
						// } else {
						// 	softObj.innerHTML = data.soft;
						// }
						
						// 更新dataAccount信息
						// var accountObj = document.getElementById("cTopo").contentWindow.document.getElementById("_accountware_");
						// if (data.account == null || data.account == "") {
						// 	accountObj.innerHTML = "";
						// } else {
						// 	accountObj.innerHTML = data.account;
						// }
						
						// 更新softTitle信息
						// var softTitleObj = document.getElementById("cTopo").contentWindow.document.getElementById("_softtitle_");
						// if (data.soft == null || data.soft == "") {
						// 	softTitleObj.innerHTML = "";
						// } else {
						// 	softTitleObj.innerHTML = "（二）软件信息列表";
						// }
						
						// 更新accountTitle信息
						// var accountTitleObj = document.getElementById("cTopo").contentWindow.document.getElementById("_accounttitle_");
						// if (data.account == null || data.account == "" || data.account == undefined) {
						// 	accountTitleObj.innerHTML = "";
						// } else {
						// 	accountTitleObj.innerHTML = data.title;
						// }
					}
				},
				beforeSend: function(xhr) {
		             xhr.setRequestHeader("Authorization", getCookie("token"));
		        },
				error: function(jqXHR, textStatus, errorThrown) {
		            console.log("[ERROR] /Topo/getShowNodeMessage: " + textStatus);
				},
				complete: function(xhr) {
					if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
						var win = window;
						while (win != win.top) {
							win = win.top;
						}
						win.location.href = xhr.getResponseHeader("CONTENTPATH");
					} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
		            	console.log("[DEBUG] /Topo/getShowNodeMessage: no function");
		            	$("body").html(showNoFunction(100, 600));
					}
				}
			});
		} else {
			var infoObj = document.getElementById("cTopo").contentWindow.document.getElementById("info_" + this.id);
			infoObj.style.width = "182px";
			if (infoObj != null) {
				infoObj.innerHTML="无状态信息";
				// var softObj = document.getElementById("cTopo").contentWindow.document.getElementById("_software_");
				// softObj.innerHTML = "";
				
				// var accountObj = document.getElementById("cTopo").contentWindow.document.getElementById("_accountware_");
				// accountObj.innerHTML = "";
			}
		}
		
		var infoObj = document.getElementById("cTopo").contentWindow.document.getElementById("info_" + this.id);
		if (infoObj != null) {
			infoObj.style.display = "block";
		}

		var menuObj = document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + this.id);
		if (menuObj != null) {
			menuObj.style.display = "none";
		}
	});

	node.mouseout(function(event) {
		if(document.getElementById("cTopo").contentWindow.document.getElementById("info_" + this.id)){
			document.getElementById("cTopo").contentWindow.document.getElementById("info_" + this.id).style.display = "none";
			document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + this.id).style.display = "none";
		}
	});
	
	// 添加鼠标拖拽事件
	node.mousedrag(function(event) {
		var x = parseInt(this.x);
		var y = parseInt(this.y);
		// 在鼠标拖拽时修改info坐标
		document.getElementById("cTopo").contentWindow.document.getElementById("info_" + this.id).style.left = 
			parseInt(x) > (document.body.clientWidth- INFO_WIDTH - parseInt(width)) ? 
					(parseInt(x) - INFO_WIDTH) + "px" : (parseInt(x) + parseInt(width)) + "px" ;
		document.getElementById("cTopo").contentWindow.document.getElementById("info_" + this.id).style.top =  
			parseInt(y) > (document.body.clientHeight - INFO_HEIGHT) ? 
					(parseInt(y) - INFO_HEIGHT)  + "px"  : parseInt(y)  + "px" ;
		// 在鼠标拖拽时修改menu坐标
		document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + this.id).style.left = 
			parseInt(x) > (document.body.clientWidth- MENU_WIDTH - parseInt(width)) ? 
					(parseInt(x) - MENU_WIDTH)  + "px" : (parseInt(x) + parseInt(width)) + "px" ;
		document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + this.id).style.top =  
			parseInt(y) > (document.body.clientHeight - MENU_HEIGHT) ? 
					(parseInt(y) - MENU_HEIGHT)  + "px" : parseInt(y) + "px" ;
	});
	
	// 添加鼠标双击事件
	node.dbclick(function(event) {
		if (this.relation_map != "") {
			$.ajax({
				type : "POST",
				dataType : "json",
				url : "/Topo/selectName",
				contentType : "application/x-www-form-urlencoded;charset=utf-8",
				data: {
					tname: relation_map
				},
				cache : true,
				async : false,
				beforeSend: function(xhr) {
		             xhr.setRequestHeader("Authorization", getCookie("token"));
		        },
				error: function(jqXHR, textStatus, errorThrown) {
		            console.log("[ERROR] /Topo/selectName: " + textStatus);
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
			location.reload();
		} else {
			if (this.node_id.substring(0, 3) != "hin") {
				showNodeDetail(this.node_id.substr(3), this.node_subtype)
				// window.open("/ant/main.html?redirect=performanceDetail.html&id=" + this.node_id.substr(3) + "&typeid=" + this.node_subtype, 'newwindow');					
			} else {
				alert("该节点为虚拟节点！");
			}
		}
	});	
	
	// 创建菜单menu框
	createNodeMenuDiv(node.id, node.node_id, node_subtype, x, y, width, height);
	// 添加鼠标右键单击事件弹出菜单
	node.addEventListener('mouseup', function(event) {  
        // 当鼠标左键按起来时更新所有节点的坐标实现自动保存移动后的坐标功能
        if (event.button == 0) {   	
        	updateSelectedNodeCoor();      	
        }
        // 当鼠标右键按起来时实现弹出右键菜单功能
        if (event.button == 2) {
        	document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + this.id).style.display = "block";
        } 
    });
	
	nodeObjectArray[nodeObjectArray.length] = node;
	//initialContainer(containerId);
	var container;
	for(var j=0;j<containers.length;j++){
		if(containers[j].id==containerId){
			container = containers[j].jcontainer;
		}
	}
	if(container) {
		container.add(node);
		scene.add(node);
	}    
}

function createLineInfoDiv(line_id) {
	var divInfo = document.createElement("div");
	divInfo.id = "info_" + line_id;
	divInfo.style.position = "absolute";
	divInfo.style.border = "solid #0f4b4d 1px";
	divInfo.style.width = 200;
	divInfo.style.height = LINE_INFO_HEIGHT;
	divInfo.style.color = '#4083FF' //"#1fffff";
	divInfo.style.padding = "6px";
	divInfo.style.lineHeight = "130%";
	divInfo.style.zIndex = 2;
	divInfo.style.filter = "alpha(opacity=80)";
	divInfo.style.backgroundColor = "#001a1c";
	divInfo.style.display = "none";
	divInfo.style.fontSize = "14px";
	divInfo.style.fontFamily = "黑体";
	divInfo.innerHTML = "";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divInfo);
}

function createLineMenuDiv(line_id) {
	var divMenu = document.createElement("div");
	divMenu.id = "menu_" + line_id;
	divMenu.style.position = "absolute";
	divMenu.style.width = MENU_WIDTH;
	divMenu.style.height = "auto";
	divMenu.style.zIndex = 2;
	divMenu.style.display = "none";
	divMenu.style.border = "solid #0f4b4d 0px";
	divMenu.style.backgroundColor = "#ffffff";
	divMenu.innerHTML = createLinkRightMenu(line_id);
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divMenu); 
}

function getMousePos(canvas, evt, scene) {   
      var rect = canvas.getBoundingClientRect();
      return {   
          x: evt.clientX - rect.left * (canvas.width / rect.width)- ( scene ? scene.translateX : 0),  
          y: evt.clientY - rect.top * (canvas.height / rect.height)- ( scene ? scene.translateY : 0)  
      }  
}  

function addJTopoEdge(stage, scene, link_id, start_node_id, start_node_obj, start_index, 
		end_node_id, end_node_obj, end_index, link_dash, link_type, link_width, link_offset, link_col1, link_col2) {
	link_id = parseInt(link_id);
	link_width = parseInt(link_width);
	link_offset = parseInt(link_offset);
	
	// 二次折线
	var edge = new JTopo.Link(start_node_obj, end_node_obj);
	// 曲线
	// var edge = new JTopo.CurveLink(start_node_obj, end_node_obj);
	// 折线
	// var edge = new JTopo.FoldLink(start_node_obj, end_node_obj);
	edge.id = "line_" + link_id;
	edge.line_id = "line_" + start_node_id + "_"+ end_node_id;
	edge.start_node_id = start_node_id;
	edge.end_node_id = end_node_id;
	edge.fontColor = "255,0,0";
	
/*
	1不加管道绿实线, 2不加管道黄实线, 3加管道绿实线, 4加管道黄虚线, 5灰色管道
	后端数据库nms_topo_link表中
	l_width   对应    edge.lineWidth     (线的宽度), 
	l_offset  对应    edge.bundleGap     (线的间距), 
	col1                对应    edge.dashedPattern (实线虚线), 
	col2                对应    edge.strokeColor   (线初始化颜色)
*/
	edge.lineWidth = link_width;
	edge.bundleGap = link_offset;
	edge.strokeColor = link_col2;
	if (parseInt(link_col1) >  0) {
		edge.dashedPattern = link_col1;
	}
/*	
	if (link_type != null && link_type == "1") {
		edge.strokeColor = "0,255,127";
		edge.bundleGap = 10;
	} else if (link_type != null && link_type == "2") {
		edge.strokeColor = "255,255,113";
		edge.bundleGap = 10;
	} else if (link_type != null && link_type == "3") {
		edge.strokeColor = "0,255,127";
		edge.bundleGap = 5;
	} else if (link_type != null && link_type == "4") {
		edge.strokeColor = "255,255,113";
		edge.dashedPattern = "10";
		edge.bundleGap = 5;
	}  else if (link_type != null && link_type == "5") {
		edge.strokeColor = "128,128,105";
		edge.lineWidth = 15;
		edge.bundleGap = -2;
	} else {
		edge.bundleGap = 10;
	}
*/
	edge.bundleOffset = 20;
	edge.link_dash = link_dash;
	edge.link_type = link_type;
	edge.start_index = start_index;
	edge.end_index = end_index;
	edge.textOffsetX = 0;
	edge.textOffsetY = 0;
	edge.text = name;
	edge.font = "bold 15px 黑体";

	// 创建线信息div框
	createLineInfoDiv(edge.id);
	// 添加鼠标左键单击事件
	edge.click(function(event) {	
		var mousePos = getMousePos(stage.canvas, event, scene);        
		var x = parseInt(mousePos.x);
		var y = parseInt(mousePos.y);
		$.ajax({
			type: "POST",
			data: {
				id:this.id.replace("line_", ""),
				line_id: this.line_id.replace("line_", ""),
				current_map_name: $("#centertitle").text()
			},
			dataType: "json",
			url: "/Topo/getShowLinkMessage",
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			cache: true,
			async: false,
			success: function(data) {
				document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id).style.width = "200px"; 
				document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id).innerHTML = data;
			},
			beforeSend: function(xhr) {
	             xhr.setRequestHeader("Authorization", getCookie("token"));
	        },
			error: function(jqXHR, textStatus, errorThrown) {
	            console.log("[ERROR] /Topo/getShowLinkMessage: " + textStatus);
			},
			complete: function(xhr) {
				if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
					var win = window;
					while (win != win.top) {
						win = win.top;
					}
					win.location.href = xhr.getResponseHeader("CONTENTPATH");
				} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
	            	console.log("[DEBUG] /Topo/getShowLinkMessage: no function");
	            	$("body").html(showNoFunction(100, 600));
				}
			}
		});
		
        document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id).style.left = 
        	parseInt(x) > (document.body.clientWidth- LINE_INFO_WIDTH) ? (parseInt(x) - LINE_INFO_WIDTH) + "px": parseInt(x) + "px";
        document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id).style.top = 
        	parseInt(y) > (document.body.clientHeight - LINE_INFO_HEIGHT) ? (parseInt(y) - LINE_INFO_HEIGHT) + "px": parseInt(y) + "px";
		var mousePos = getMousePos(stage.canvas, event, scene);        
        document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id).style.display = "block";
	});    

	// 添加鼠标移除框事件将info和menu对应的框隐藏
	edge.mouseout(function(event) {
		if(document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id)
				&&document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id)){
			document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id).style.display = "none";
			document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id).style.display = "none";
		}
	});
	
	createLineMenuDiv(edge.id);
	edge.addEventListener('mouseup', function(event){
        if(event.button == 2) {
    		var mousePos = getMousePos(stage.canvas, event, scene);        
    		var x = parseInt(mousePos.x);
    		var y = parseInt(mousePos.y);
            document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id).style.left = 
            	parseInt(x) > (document.body.clientWidth- MENU_WIDTH) ? (parseInt(x) - MENU_WIDTH ) + "px": parseInt(x)  + "px";
            document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id).style.top = parseInt(y)  + "px";
        	document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id).style.display = "block";
        }
    });

	edgeObjectArray[edgeObjectArray.length] = edge;
	scene.add(edge);
}

function getNewNodeDefaultCoordinate(count) {
	var height = 50;
	var x = parseInt(START_INIT_X) + 100 + count % 50 * 50;
	var y = parseInt(START_INIT_Y) + (parseInt(height) + 20) + count / 50 * 50;
	return {'x':x, 'y':y};
}

function addNodes(node_array) {
	if (node_array == null) {
		alert("添加设备数组对象为空, 请检查！");
		return null;
	}
	var addNodeArray = new Array();
	for (var i = 0; i < node_array.length; i++) {
		var tmp = node_array[i].split(",");
		if (tmp != null && tmp.length == 5) {
			var node_id = tmp[0];
			var node_ip = tmp[1];
			var node_subtype = tmp[2];
			var node_alias = tmp[3];
			var container_id = tmp[4];
			var node_image = "";
			
			/*var obj = getNewNodeDefaultCoordinate(i);
			var x = obj.x;
			var y = obj.y;*/
			var x = 100;
			var y = 100;
			//initialContainer(container_id);
			for(var j=0;j<containers.length;j++){
				if(containers[j].id==container_id){
					container = containers[j].jcontainer;
					 x = container.x;
					 y = container.y;
				}
			}
			addNodeArray[addNodeArray.length] = new topoNodesTable('', '', node_id, node_subtype, node_image,
															 x, y, '', '', node_ip, node_alias, '', '',container_id);
		}
	}
	$.ajax({
		type : "POST",
		data: {
			current_topo_map: $("#centertitle").text(),
			node_object_Array: JSON.stringify(addNodeArray)
		},
		dataType : "json",
		url : "/Topo/addNode",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			if(data.state === "0"){
				for (var i = 0; i < data.data.length; i++) {
					var obj = data.data[i];
					addJTopoNode(stage, scene, obj.id, obj.nodeId, obj.nmsAssetType.id, obj.img, 
							obj.x, obj.y, obj.width, obj.height, obj.ip, obj.alias, obj.relMap,obj.containerId);
		    	}
			} else if(data.state === "3"){
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/addNode: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/addNode: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
    return "Success";
}


function addEntityLink(current_topo_map, 
		start_node_id, start_node_index, start_node_ip, start_node_name, start_node_mac, start_node_speed,
		end_node_id, end_node_index, end_node_ip, end_node_name, end_node_mac, end_node_speed, link_name, link_type) {
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/addEntityLink",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data: {
			current_topo_map, 
			link_name, 
			start_node_id, 
			start_node_name, 
			start_node_ip,
   			start_node_index, 
   			start_node_mac, 
   			start_node_speed, 
   			end_node_id, 
   			end_node_name, 
   			end_node_ip, 
   			end_node_index, 
   			end_node_mac, 
   			end_node_speed,
   			link_type
		},
		cache : true,
		async : false,
		success : function(data) {
			if (data.state === "0") {
				var start_node_obj = findNodeObjByNodeId(start_node_id);
				if (start_node_obj == null) {
					alert("添加真实链路开始端节点已删除！");
					return;
				}		    		
				var end_node_obj = findNodeObjByNodeId(end_node_id);
				if (end_node_obj == null) {
					alert("添加真实链路结束端节点已删除！");
					return;
				}
				
				// 根据link_type来画线
				var width = "2";          // 线的宽度
				var offset = "10";        // 多条线之间的间距
				var col1 = "0";           // 实线还是虚线的间距
				var col2 = "0,255,127";   // 线的颜色
				if (parseInt(link_type) == 1) {
					width = "2";
					offset = "10";
					col1 = "0";
					col2 = "0,255,127";
				} else if (parseInt(link_type) == 2) {
					width = "2";
					offset = "10";
					col1 = "0";
					col2 = "255,255,113";
				} else if (parseInt(link_type) == 3) {
					width = "2";           
					offset = "5";          
					col1 = "0";            
					col2 = "0,255,127";    
				} else if (parseInt(link_type) == 4) {
					width = "2";
					offset = "5";
					col1 = "10";
					col2 = "255,255,113";
				} else if (parseInt(link_type) == 5) {
					width = "15";
					offset = "-2";
					col1 = "0";
					col2 = "128,128,105";
				}

		    	addJTopoEdge(stage, scene, data.id, start_node_id, start_node_obj, start_node_index, 
		    			end_node_id, end_node_obj, end_node_index, "Solid", "0", width, offset, col1, col2);
			} else if(data.state === "3") {
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/addEntityLink: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/addEntityLink: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function addHintMeta(current_topo_map, meta_name, icon_name, icon_path, icon_width, icon_height,avalable_zone) {
	$.ajax({
		type : "POST",
		data: {
			current_topo_name: current_topo_map,
			icon_path: icon_path,
			meta_name: meta_name,
			icon_name: icon_name,
			icon_width: icon_width,
			icon_height: icon_height,
			container_id:avalable_zone
		},
		dataType : "json",
		url : "/Topo/addHinNode",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(json) {
			if (json.state === "0"){
				var ip = "设备-" + icon_name;
				addJTopoNode(stage, scene, json.nodeId.substring(3), json.nodeId, '0', icon_path, 
						'100', '100', icon_width, icon_height, ip, meta_name, '', avalable_zone);
			} else if(json.state === "3") {
				alert(json.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/addHinNode: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/addHinNode: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function addHintLink(current_topo_map, start_node_id, start_node_type, start_node_name, start_node_desc,
				end_node_id, end_node_type, end_node_name, end_node_desc, link_type) { 
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/addHintLink",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data: {
			current_topo_map, 
			start_node_id, 
			start_node_type, 
			start_node_name, 
			start_node_desc,
			end_node_id, 
			end_node_type, 
			end_node_name, 
			end_node_desc,
			link_type
		},
		cache : true,
		async : false,
		success : function(data) {
			if(data.state === "0"){
		    		var start_node_obj = findNodeObjByNodeId(start_node_id);
		    		if (start_node_obj == null) {
		    			alert("添加虚拟链路开始端节点已删除！");
		    			return;
		    		}
		        			    		
		    		var end_node_obj = findNodeObjByNodeId(end_node_id);
		    		if (end_node_obj == null) {
		    			alert("添加虚拟链路结束端节点已删除！");
		    			return;
		    		}
		    		
					// 根据link_type来画线
					var width = "2";          // 线的宽度
					var offset = "10";        // 多条线之间的间距
					var col1 = "0";           // 实线还是虚线的间距
					var col2 = "0,255,127";   // 线的颜色
					if (parseInt(link_type) == 1) {
						width = "2";
						offset = "10";
						col1 = "0";
						col2 = "0,255,127";
					} else if (parseInt(link_type) == 2) {
						width = "2";
						offset = "10";
						col1 = "0";
						col2 = "255,255,113";
					} else if (parseInt(link_type) == 3) {
						width = "2";           
						offset = "5";          
						col1 = "0";            
						col2 = "0,255,127";    
					} else if (parseInt(link_type) == 4) {
						width = "2";
						offset = "5";
						col1 = "10";
						col2 = "255,255,113";
					} else if (parseInt(link_type) == 5) {
						width = "15";
						offset = "-2";
						col1 = "0";
						col2 = "128,128,105";
					}
					
		        	addJTopoEdge(stage, scene, data.id, start_node_id, start_node_obj, "0", 
		        			end_node_id, end_node_obj, "0", "Solid", "0", width, offset, col1, col2);		  
			}	
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/addHintLink: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/addHintLink: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function saveEditMap(topo_name, region_ids) {
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/Edittopomap",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data: {
			current_topo_map: $("#centertitle").html(),
			topo_name: topo_name,
			region_ids: region_ids
		},
		cache : true,
		async : false,
		success : function(data) {
			if (data.state == "0") {
				alert("修改拓扑图属性成功!");
				window.location.reload();
				window.closeDialog();
			} else {
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/Edittopomap: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/Edittopomap: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function saveSubMap(topo_name, region_ids, topo_type) {
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/saveSubMap",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data: {
			current_topo_map: $("#centertitle").html(),
			topo_name: topo_name,
			region_ids: region_ids,
			topo_type: topo_type
		},
		cache : true,
		async : false,
		success : function(data) {
			if (data.state === "0") {
				alert(data.info);
				window.location.reload();
				window.closeDialog();
			} else if (data.state === "1") {
				alert(data.info);
				window.closeDialog();
			} else {
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/saveSubMap: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/saveSubMap: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}


function deleteNode(node_id) {
	// 根据node_id查找到对应的JTopo对象
	var node = null;
	for ( var i = 0; i < nodeObjectArray.length; i++) {
		if(node_id == nodeObjectArray[i].node_id) {
			var node = nodeObjectArray[i];
			break;
		}
	}
	// 从画图中删除对应的node对象节点
	var container;
	for(var j=0;j<containers.length;j++){
		if(containers[j].id==node.containerId){
			container = containers[j].jcontainer;
			container.remove(node);
		}
	}
   
	scene.remove(node);
	// 同时在内存数据中删除对应的元素
	nodeObjectArray.splice(i, 1);
	// 删除节点对应的info提示框
	if(document.getElementById("cTopo").contentWindow.document.getElementById("info_" + node.id)) {
		document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").removeChild(document.getElementById("cTopo").contentWindow.document.getElementById("info_" + node.id));
	}
	// 删除节点对应的menu右键菜单
	if(document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + node.id)) {
		document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").removeChild(document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + node.id));
	}
	// 删除节点连接的链路对应的info提示框
	for (var i = 0; i < edgeObjectArray.length; i++) {
		var edge = edgeObjectArray[i];
		if (node_id == edge.start_node_id || node_id == edge.end_node_id) {
			scene.remove(edge);
			edgeObjectArray.splice(i, 1);
			if (document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id)) {
				document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").removeChild(document.getElementById("cTopo").contentWindow.document.getElementById("info_" + edge.id));
			}
			if (document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id)) {
				document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").removeChild(document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + edge.id));
			}
		}
	}
}

function saveRelationSubMap(node_id, submap_name) {
	$.ajax({
		type : "POST",
		data: {
			current_topo_map: $("#centertitle").text(),
			node_id: node_id,
			submap_name: submap_name
		},
		dataType : "json",
		url : "/Topo/saveRelationSubMap",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			if(data.state === "0"){
				for ( var i = 0; i < nodeObjectArray.length; i++) {
					var node = nodeObjectArray[i];
					if (node_id == node.node_id) {
						node.relation_map = submap_name;
					}
				}
				alert("关联子拓扑图成功!");
				location.reload();	
			} else {
				alert("关联子拓扑图失败!");
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/saveRelationSubMap: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/saveRelationSubMap: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function deleteLink(line_id) {
	for (var i = 0; i < edgeObjectArray.length; i++) {
		var edge = edgeObjectArray[i];	
		if (edge.id == line_id) {
			scene.remove(edge);
			break;
		}
	}
	edgeObjectArray.splice(i, 1);
	if (document.getElementById("cTopo").contentWindow.document.getElementById("info_" + line_id)) {
		document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").removeChild(document.getElementById("cTopo").contentWindow.document.getElementById("info_" + line_id));
	}
	if (document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + line_id)) {
		document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").removeChild(document.getElementById("cTopo").contentWindow.document.getElementById("menu_" + line_id));
	}
}

function updateLinkWidth(link_id, link_width) {
	var current_topo_name = $("#centertitle").text();
	if (link_id == null || link_width == null) {
		return;
	}
	$.ajax({
		type : "POST",
		data: {
			current_topo_name: current_topo_name,
			link_id: link_id,
			link_width: link_width
		},
		dataType : "json",
		url : "/Topo/updateLinkWidth",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			if(data.state === "0"){
				var edge = null;
				for ( var i = 0; i < edgeObjectArray.length; i++) {
					var edge = edgeObjectArray[i];
					if (link_id == edge.id) {
						break;
					}
				}
				if (edge) {
					edge.lineWidth = parseInt(link_width);
				}
				window.closeDialog();
			} else {
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/updateLinkWidth: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/updateLinkWidth: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}

function updateNodeAlias(node_id, node_alias, current_map_name) {
	if (node_id == null || node_alias == null || current_map_name == null) {
		return;
	}
	$.ajax({
		type : "POST",
		data: {
			current_topo_name: current_map_name,
			node_id: node_id,
			node_alias: node_alias
		},
		dataType : "json",
		url : "/Topo/updateNodeAlias",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			if(data.state === "0"){
				var node = null;
				for ( var i = 0; i < nodeObjectArray.length; i++) {
					var node = nodeObjectArray[i];
					if (node_id == node.node_id) {
						break;
					}
				}
				if (node) {
					node.text = node_alias;
				}
				window.closeDialog();
			} else {
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/updateNodeAlias: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/updateNodeAlias: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}


function updateNodeBindings(node_id, containerId) {
	$.ajax({
		type : "POST",
		data: {
			id: node_id,
			containerId: containerId
		},
		dataType : "json",
		url : "/Topo/updateNodeBinding",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			if(data.state === "0"){
				window.closeDialog();
			} else {
				alert(data.info);
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/updateNodeAlias: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/updateNodeAlias: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
}


//判断container是否已经初始化，没有初始化则返回false，初始化过，则返回yes
var initialContainer=function(containerId){
	for(var i=0;i<containers.length;i++){
		if(containerId==containers[i].id){
			if(containers[i].isActive===true){
				return;
			}else{
				containers[i].isActive===true;
				var container = new JTopo.Container();
				 container.textPosition = 'Middle_Center';
			     container.fontColor = '100,255,0';
			     container.font = '18pt 微软雅黑';
			     container.borderColor = '255,0,0';
			     container.borderRadius = 30; // 圆角
			     container.fillColor='255,255,255';
			     container.mode = "nodrag";
			     container.text=containers[i].name;
			     containers[i].jcontainer=container;
			     scene.add(container);
			     return;
			}
		}
	}
}
//渲染节点和连线
function createTopo(topoData) {
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
	containers = retData.list;	
	for(var i=0;i<containers.length;i++){
		var container = new JTopo.Container();
		 container.textPosition = 'Middle_Center';
	     container.fontColor = '100,255,0';
	     container.font = '18pt 微软雅黑';
	     container.borderColor = '255,0,0';
	     container.borderRadius = 30; // 圆角
	     container.fillColor='255,255,255';
	     container.mode = "nodrag";
	     container.text=containers[i].divName;
	     containers[i].jcontainer=container;
	     scene.add(container);
	}
	
	    
	var topoNodesTableArray = topoData.node ? topoData.node : [];
	var topoLinksTableArray = topoData.link ? topoData.link : [];
	for (var i = 0; i < topoNodesTableArray.length; i++) {
		var node = topoNodesTableArray[i];
		var id = node.id;
		var node_id = node.nodeId;
		var node_subtype = node.nmsAssetType.id;
		var img_src = node.img;
		var x = node.x;
		var y = node.y;
		var ip = node.ip;
		var alias = node.alias;
		var relation_map = node.relMap;
		var width = node.width;
		var height = node.height;
		var container_id = node.containerId;
		
		if(id != null && node_id != null && node_subtype != null && img_src != null && x != null && y != null && 
				ip != null && alias != null && relation_map != null && width != null && height != null && container_id != null) {
			addJTopoNode(stage, scene, id, node_id, node_subtype, img_src, x, y, width, height, ip, alias, relation_map, container_id);
		}
	}

	for (var i = 0; i < topoLinksTableArray.length; i++) {
		var link = topoLinksTableArray[i];
		var link_id = link.id;
		var start_node_id = link.SNodeId;
		var start_node_obj = findNodeObjByNodeId(start_node_id);
		if (start_node_obj == null) {
			continue;
		}
		var start_index = link.SIndex;
		var end_node_id = link.ENodeId;
		var end_node_obj = findNodeObjByNodeId(end_node_id);
		if (end_node_obj == null) {
			continue;
		}		
		var end_index = link.EIndex;
		var link_dash = link.LDash;
		var link_type = link.LType;	
		
		var link_width = link.LWidth;   // width
		var link_offset = link.LOffset; // bundleGap
		var link_col1 = link.col1;      // dashedPattern
		var link_col2 = link.col2;      // strokeColor
		
		if(link_id != null && start_node_id != null && start_index != null && end_node_id != null && end_index != null 
				&& link_dash != null && link_width != null && link_offset != null && start_node_obj != null && end_node_obj != null
				&& link_col1 != null && link_col2 != null ) {
			
			addJTopoEdge(stage, scene, link_id, start_node_id, start_node_obj, start_index, end_node_id, 
					end_node_obj, end_index, link_dash, link_type, link_width, link_offset, link_col1, link_col2);
		}
	}

	zoomProcDlg("out");	
}


function doInit(val) {
	//获取Iframe对象
	var obj = document.getElementById("cTopo");
	if (obj == null) {
		return;
	}
	//获取topo真正的对象
	var topo = obj.contentWindow.document.getElementById("topo");
	topo.width = 1920;
	topo.height = 1080;

	stage = new JTopo.Stage(topo);
	scene = new JTopo.Scene(stage);
	stage.mode = "select";
	var topoData = postTopoData(val);
	

	// 首页不需要自动刷新
	var auto = setInterval(autoRefresh, 5 * 1000);
	if (refresh != 1) {
		autoRefresh();
	}

	createSoftInfoDiv();
	createAccountInfoDiv();
//	createAdminDiv();
}

//通过拓扑平台获取其下的node和link
function postTopoData(val){
	var retData = "";

	$.ajax({
		type: "POST",
		data: {
			mapId: val	
		},
		dataType: "json",
		url: "/Topo/findByIdMap",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		success: function(data) {
			if (data == null){
				$("body").html(showNoData(100,600));
			} else{
				//$.ajaxSettings.async = false; 
				retData = JSON.parse(data);
				createTopo(retData)
			    //$.ajaxSettings.async = true; // 销毁同步，否则会挂死
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/findByIdMap: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/findByIdMap: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});

	return retData;
}

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

function createSoftInfoDiv() {
	// 创建显示软件表头DIV
	var divTitle = document.createElement("div");
	divTitle.id = "_softtitle_";
	divTitle.style.position = "absolute";
	divTitle.style.border = "solid #001a1c 0px";
	divTitle.style.borderBottom = "solid #001a1c 0px";
	divTitle.style.width = '220px';
	divTitle.style.height = '16px';
	divTitle.style.color = "#ffeb3b";
	divTitle.style.padding = "5px";
	divTitle.style.display = "block";
	divTitle.style.lineHeight = "100%";
	divTitle.style.zIndex = 2;
	var x = parseInt(document.body.clientWidth) - 240;
	divTitle.style.left = x + "px" ;
	divTitle.style.top =  "10px" ;
	divTitle.style.fontSize = "17px";
	divTitle.style.fontFamily = "黑体";
	// divTitle.innerHTML = "（二）软件信息列表 ";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divTitle);	
	
	// 创建显示软件列表DIV
	var divInfo = document.createElement("div");
	divInfo.id = "_software_";
	divInfo.style.position = "absolute";
	divInfo.style.border = "solid #001a1c 0px";
	divInfo.style.borderTop = "solid #001a1c 0px";
	divInfo.style.width = '220px';
	divInfo.style.height = '00px';
	divInfo.style.color = "#2fffff";
	divInfo.style.padding = "0px";
	divInfo.style.display = "block";
	divInfo.style.lineHeight = "180%";
	divInfo.style.zIndex = 2;
	divInfo.style.filter = "alpha(opacity=80)";
	divInfo.style.backgroundColor = "#001a1c";
	var x = parseInt(document.body.clientWidth) - 240;
	divInfo.style.left = x + "px" ;
	divInfo.style.top =  "40px";
	divInfo.style.fontSize = "15px";
	divInfo.style.fontFamily = "黑体";
	divInfo.innerHTML = "";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divInfo);
}

function createAccountInfoDiv() {
	// 创建显示软件表头DIV
	var divTitle = document.createElement("div");
	divTitle.id = "_accounttitle_";
	divTitle.style.position = "absolute";
	divTitle.style.border = "solid #001a1c 0px";
	divTitle.style.borderBottom = "solid #001a1c 0px";
	divTitle.style.width = '240px';
	divTitle.style.height = '16px';
	divTitle.style.color = "#ffeb3b";
	divTitle.style.padding = "5px";
	divTitle.style.display = "block";
	divTitle.style.lineHeight = "100%";
	divTitle.style.zIndex = 2;
	var x = parseInt(document.body.clientWidth) - 500;
	divTitle.style.left = x + "px" ;
	divTitle.style.top =  "10px" ;
	divTitle.style.fontSize = "17px";
	divTitle.style.fontFamily = "黑体";
	// divTitle.innerHTML = "（一）应用授权列表 ";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divTitle);	
	
	// 创建显示软件列表DIV
	var divInfo = document.createElement("div");
	divInfo.id = "_accountware_";
	divInfo.style.position = "absolute";
	divInfo.style.border = "solid #001a1c 0px";
	divInfo.style.borderTop = "solid #001a1c 0px";
	divInfo.style.width = '240px';
	divInfo.style.height = '00px';
	divInfo.style.color = "#2fffff";
	divInfo.style.padding = "0px";
	divInfo.style.display = "block";
	divInfo.style.lineHeight = "180%";
	divInfo.style.zIndex = 2;
	divInfo.style.backgroundColor = "#001a1c";
	var x = parseInt(document.body.clientWidth) - 500;
	divInfo.style.left = x + "px" ;
	divInfo.style.top =  "40px";
	divInfo.style.fontSize = "15px";
	divInfo.style.fontFamily = "黑体";
	divInfo.innerHTML = "";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divInfo);
}

function createAdminDiv() {
	var divInfo = document.createElement("div");
	divInfo.id = "_admin_";
	divInfo.style.class="drsElement";
	divInfo.style.position = "absolute";
	divInfo.style.border = "solid #001a1c 1px";
	divInfo.style.width = '200px';
	divInfo.style.height = '60px';
	divInfo.style.color = "#ffff0f";
	divInfo.style.padding = "10px";
	divInfo.style.lineHeight = "150%";
	divInfo.style.zIndex = -1;
	divInfo.style.left = "10px" ;
	divInfo.style.top =  "60px" ;
	divInfo.style.fontSize = "17px";
	divInfo.style.fontFamily = "黑体";
	divInfo.innerHTML = "(1)全网终端机数：6台<br>(2)全网服务器数：6台";
	document.getElementById("cTopo").contentWindow.document.getElementById("divLayer").appendChild(divInfo);	
}

