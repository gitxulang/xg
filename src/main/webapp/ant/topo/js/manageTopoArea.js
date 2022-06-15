window.onload = function() {
	var data = postListData($("#value").val());
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
	var data = postListData($("#value").val());
	createList(data);
}

function doAdd_bak() {
	/**
	 * dataJson:{
	 *   divName: 可用域名字
	 *   mapId: 拓扑ID
	 * }
	 */
	let dataJson = {
		divName: $("#value").val(),
		mapId: $("#select_topo",parent.document).val()
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: "/Topo/addTopoArea",
		data: dataJson,
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			layer.msg(json.info, {time: 1000},function(){
				if(json.info == '添加区域成功!') {
					window.top.location.reload();
				}
			});		
		},
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"))
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.responseText);
		},
		complete : function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			}
		}
	})
}

function doAdd(){
	open_dialog('./addTopoArea.html', '添加可用域', 600, 250)
}

function doUpdate(obj, id){
	let inputObj = $(obj).parent().parent().find('input')
	inputObj.removeAttr("readonly")
	inputObj.css('border', '1px solid #22B37A')

	
	inputObj.on('blur', function(){
		inputObj.css('border', '1px solid #5D6FB2')
		
		let dataJson = {
			id: id,
			divName: inputObj.val(),
			mapId: $("#select_topo",parent.document).val()
		}
		$.ajax({
			type: "POST",
			dataType: "json",
			url: "/Topo/updateTopoArea",
			data: dataJson,
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			timeout: 5000,
			cache: true,
			async: true,
			success: function(json) {
				layer.msg(json.info, {time: 1000},function(){
					inputObj.attr("readonly", "readonly")
					if(json.info == '修改成功!') {
						window.top.location.reload();
					}
				});		
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", getCookie("token"))
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.responseText);
			},
			complete : function(xhr) {
				if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
					var win = window;
					while (win != win.top) {
						win = win.top;
					}
					win.location.href = xhr.getResponseHeader("CONTENTPATH");
				}
			}
		})
	})
	
	
}

function doDelete(obj, id){
	// let inputObj = $(obj).parent().parent().find('input')
	let dataJson = {
		id: id,
		// divName: inputObj.val(),
		mapId: $("#select_topo",parent.document).val()
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: "/Topo/deleteTopoArea",
		data: dataJson,
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			layer.msg(json.info, {time: 1000},function(){
				if(json.info == '删除成功!') {
					window.top.location.reload();
				}
			});		
		},
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"))
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.responseText);
		},
		complete : function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			}
		}
	})
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

function createList(json) {
	var trBody = "", divName = "", id = "", n = 0, no = 0;
	$(".table_body_bg").empty();
	let begin = parseInt(json.page);
	let offset = parseInt($("#selectPage").val());
	let data = json.list
	for ( var i = 0; i < data.length; i++) {
		n = parseInt(i) + 1 + (begin - 1) * offset
		divName = htmlEscape(data[i].divName);
		id = data[i].id;
		no = i + 1
		trBody += '<tr>'
			+ '<td align="center" height="33" class="table_tr_body_start" width="6%">'			
			+ n		
			+ '</td>'
			+ '<td align="left" class="table_tr_body" width="20%">'
			+ '<input type="text" class="topo_area_name_input" readonly="readonly" value="'
			+ divName
			+ '">'
			+ '</td>'
			+ '<td align="left" class="table_tr_body" width="22%">'
			+ '<a href="#" onclick="doUpdate(this, ' + id + ')"><i class="my-material-icons" style="color:#22B37A;">编辑&nbsp;</i></a>'
			+ '<a href="#" onclick="doDelete(this, ' + id + ')"><i class="my-material-icons" style="color:#22B37A;">删除</i></a>'
			+ '</td>'
		+ '</tr>';
	}
	$(".table_body_bg").html(trBody);

	$("#page").html(json.page);
	$("#totalPage").html(json.totalPage);
	$("#totalCount").html(json.totalCount);
};

function postListData(keywords, begin, offset) {
	var retData = "";
	let mapId = $("#select_topo",parent.document).val()
	// let offset = $("#selectPage").val()
	$.ajax({
		type : "POST",
		data : {
			divName : String(keywords) ? String(keywords) : "",
			mapId: mapId,
			begin: begin ? begin : 1,
			offset: offset ? offset : 10
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
	return retData;
}

function changeOffset() {
	let keywords = $("#value").val()
	createList(
		postListData(keywords, 1, $("#selectPage").val())
	)
}

function firstPage() {
	let keywords = $("#value").val()
	if ($("#page").html() != "1") {
		createList(
			postListData(keywords, 1, $("#selectPage").val())
		)
	}
}

function beforePage() {
	let begin = parseInt($("#page").html())
	let keywords = $("#value").val()
	
	if (begin != 1) {
		createList(
			postListData(keywords, begin - 1, $("#selectPage").val())
		)
	}
}

function nextPage() {
	let keywords = $("#value").val()
	if ($("#page").html() != $("#totalPage").html()) {
		let begin = parseInt($("#page").html()) + 1
		let offset = $("#selectPage").val();
		createList(
			postListData(keywords, begin, offset)
		)
		
	}
}

function lastPage() {
	let keywords = $("#value").val()
	if ($("#page").html() != $("#totalPage").html()) {
		let begin = parseInt($("#totalPage").html())
		let offset = $("#selectPage").val();
		createList(
			postListData(keywords, begin, offset)
		)
		
	}
}

$(function() {
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#value').keydown(function(event) {
		if (event.keyCode == 13) {
			doQuery();
		}
	});
});





