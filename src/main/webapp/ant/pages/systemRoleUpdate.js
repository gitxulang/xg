
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
/*
function GetRequest() {
	var url = location.search;
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}
*/

$(document).ready(function() {
	var request = GetSelfRequest();
	var id = request.id;
	$("#roleId").val(id);

	// 加载默认的权限列表
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/NmsFunction/all",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("body").html(showNoData(100, 600));
			} else {
				loadPage(json, id);
			}
		},
		beforeSend : function(xhr) {
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
	

	// 加载角色名称
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {
			id: id
		},
		url: "/role/getRoleById",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			$("#role").val(json.role);
		},
		beforeSend : function(xhr) {
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
})

function loadPage(json, id) {
	var len = json.length;
	var row = 0;
	if (len % 4 == 0) {
		row = parseInt(len / 4);
	} else {
		row = parseInt(len / 4) + 1;
	}
	var str = '';
	$("#functions").empty();
	for (var i = 0; i < row;) {
		if (parseInt(i) == row -1) {
			str +=	'<div class="row">';
			if (4*parseInt(i) < len) {
				str += 
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)].id + '">' +
							'<label for="' + json[4*parseInt(i)].id + '">' + json[4*parseInt(i)].chineseDesc + '</label>' +
						'</p>' +
					'</div>';
			}
			if (4*parseInt(i)+1 < len) {
				str += 
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)+1].id + '">' +
							'<label for="' + json[4*parseInt(i)+1].id + '">' + json[4*parseInt(i)+1].chineseDesc + '</label>' +
						'</p>' +
					'</div>';
			}	
			if (4*parseInt(i)+2 < len) {
				str += 
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)+2].id + '">' +
							'<label for="' + json[4*parseInt(i)+2].id + '">' + json[4*parseInt(i)+2].chineseDesc + '</label>' +
						'</p>' +
					'</div>';
			}
			if (4*parseInt(i)+3 < len) {
				str += 
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)+3].id + '">' +
							'<label for="' + json[4*parseInt(i)+3].id + '">' + json[4*parseInt(i)+3].chineseDesc + '</label>' +
						'</p>' +
					'</div>';
			}
			str += '</div>';
		} else {
			str +=	
				'<div class="row">' +
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)].id + '">' +
							'<label for="' + json[4*parseInt(i)].id + '">' + json[4*parseInt(i)].chineseDesc + '</label>' +
						'</p>' +
					'</div>' +
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)+1].id + '">' +
							'<label for="' + json[4*parseInt(i)+1].id + '">' + json[4*parseInt(i)+1].chineseDesc + '</label>' +
						'</p>' +
					'</div>' +
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)+2].id + '">' +
							'<label for="' + json[4*parseInt(i)+2].id + '">' + json[4*parseInt(i)+2].chineseDesc + '</label>' +
						'</p>' +
					'</div>' +
					'<div class="input-field col s3">' +
						'<p>' +
							'<input type="checkbox" id="' + json[4*parseInt(i)+3].id + '">' +
							'<label for="' + json[4*parseInt(i)+3].id + '">' + json[4*parseInt(i)+3].chineseDesc + '</label>' +
						'</p>' +
					'</div>' +
				'</div>';
		}
		i = parseInt(i) + 1;
	}
	$("#functions").append(str);
	
	// 加载用户原来的权限信息
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {
			id: id
		},
		url: "/NmsRoleFunction/findByRoleId",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if(json != null) {
				loadFunction(json);
			}
		},
		beforeSend : function(xhr) {
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

function loadFunction(json) {
	var checkedObjects = $("input[type='checkbox']");
	for (i in json) {
		$("[id=" + json[i].nmsFunction.id + "]:checkbox").prop("checked", true);
	}
}

function doUpdate() {
	var checkedObjects = $("input[type='checkbox']:checked");
	var roleId = $("#roleId").val();
	var role = $("#role").val();

	if (checkedObjects.length == 0) {
		alert("至少选择一个权限！");
		return;
	}
	if (role == "" || role == undefined) {
		alert("角色名不能为空！");
		return;
	}
	var functions = "";
	for ( var i = 0; i < checkedObjects.length; i++) {
		functions += checkedObjects.eq(i).attr('id') + ",";
	}

	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {
			roleId: roleId,
			role: role
		},
		url: "/role/updateRole",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(data) {
			if (data != null && data.state == 0) {
				$.ajax({
					type: 'POST',
					dataType: "json",
					data: {
						roleId: roleId,
						functions: functions
					},
					url : "/NmsRoleFunction/updateRoleFunction",
					contentType : 'application/x-www-form-urlencoded;charset=utf-8',
					timeout : 5000,
					cache : true,
					async : true,
					success : function(json) {
						if (json != null && json.state == 0) {
							alert("修改成功!");
							jumpPage("role.html");
						} else {
							alert("修改失败!");
						}
					},
					beforeSend : function(xhr) {
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
			} else if (data != null && data.state == 1) {
				alert(data.info);
			}
		},
		beforeSend : function(xhr) {
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


