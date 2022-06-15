window.onload = function() {
	init();
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

function createSelect() {
	//获取拓扑平台及子拓扑
	var selectOption = postSelectData();
	if (selectOption == null || selectOption == "") {
		return false;
	}
	if (selectOption) {
		var option = "";
		for ( var i = 0; i < selectOption.length; i++) {
			if (selectOption[i]["tid"] == "1") {
				option += "<option value=" + selectOption[i]["id"]
						+ " data-type=" + selectOption[i]["ttype"]
						+ " selected>" + selectOption[i]["tname"] + "</option>";
			} else {
				option += "<option value=" + selectOption[i]["id"]
						+ " data-type=" + selectOption[i]["ttype"] + ">"
						+ selectOption[i]["tname"] + "</option>";
			}
		}
        //下拉子拓扑
		$(".select_topo").html(option);
		var selectTopo = $(".select_topo option:selected");
		//中间标题名称
		$("#centertitle").text(selectTopo.text()).attr("data-value",
				selectTopo.val()).attr("data-type",
				selectTopo.attr("data-type"));
	}
	return true;
}

function changeView() {
	var name = $(".select_topo option:selected").text();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: "/Topo/selectName",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data: {
			tname: name
		},
		timeout: 5000,
		cache: true,
		async: false,
		success: function(data) {
			location.reload();
		},
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
}

function showToolBar() {
	var list = document.getElementById("toolbar_table");
	var checkbox = document.getElementById("checkbox");
	if (checkbox.checked) {
		list.style.marginLeft = "1px";
	} else {
		list.style.marginLeft = "-800px";
	}
}

function toolbarClick() {
	var currentTopoMap = $("#centertitle").text();
	$("#addNodes").click(function() {
		readyAddNodes(currentTopoMap);
	});

	$("#createEntityLink").click(function() {
		createEntityLink();
	});

	// 管理可用域
	$("#manageTopoArea").click(function() {
		manageTopoArea();
	});

	$("#createDemoObj").click(function() {
		createHintMeta();
	});

	$("#createHintLink").click(function() {
		createHintLink();
	});

	$("#view").click(function() {
		view();
	});

	$("#editMap").click(function() {
		editMap();
	});

	$("#createSubMap").click(function() {
		createSubMap();
	});

	$("#deleteSubMap").click(function() {
		deleteSubMap();
	});

	$("#refresh").click(function() {
		refreshTopo();
	});

	$("#horizonArrange").click(function() {
		horizonArrange();
	});

	$("#verticalArrange").click(function() {
		verticalArrange();
	});
	
	$("#openEditArea").click(function() {
		openEditArea();
	});
	
	$("#closeEditArea").click(function() {
		closeEditArea();
	});

}

function switchDisplay() {
	if ($("#centertitle").attr("data-type") === "0") {
		$("#deleteSubMap").closest("td").hide();
		$("#createSubMap").closest("td").show();
	} else {
		$("#deleteSubMap").closest("td").show();
		$("#createSubMap").closest("td").hide();
	}
}

function postSelectData() {
	var retData = "";
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "/Topo/all",
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
			console.log("[ERROR] /Topo/all: jqXHR.responseText=" + jqXHR.responseText);
            console.log("[ERROR] /Topo/all: jqXHR.status=" + jqXHR.status);
            console.log("[ERROR] /Topo/all: jqXHR.readyState=" + jqXHR.readyState);
            console.log("[ERROR] /Topo/all: jqXHR.statusText=" + jqXHR.statusText);
            console.log("[ERROR] /Topo/all: errorThrown=" + errorThrown);
            console.log("[ERROR] /Topo/all: textStatus=" + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/all: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
	return retData;
}

function init() {
	if (createSelect() == false) {
		return;
	}
	doInit($(".select_topo option:selected").val());
	toolbarClick();
	switchDisplay();
	
	var obj = document.getElementById("cTopo");
	if (obj == null) {
		return;
	}
	document.oncontextmenu = obj.contentWindow.document
			.getElementById("topo").oncontextmenu = function() {
		return false
	};
}

var index = null;
function open_dialog(url, title, width, height) {
	index = layer.open({
		type : 2,
		title : title,
		area : [ width + 'px', height + 'px' ],
		fixed : false,
		maxmin : false,
		content : url
	});
}

function closeDialog() {
	layer.close(index);
	window.location.reload();
}

