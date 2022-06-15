
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

$(document).ready(function() {
	loadRole();
	loadDepartment();
})

function loadRole() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/role/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				for (var i in json) {
					if (json[i].id != 4) {
    					continue;
    				}
					$("#role").append('<option value="' + json[i].id + '" >'+ json[i].role + '</option>');
				}
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

function loadDepartment() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Department/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			for (var i in json) {
				$("#dept").append('<option value="' + json[i].id + '" >' + json[i].dname + '</option>');
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

function addUser() {
	if ($("#idUser").html() != "*") {
		alert("账号名称重复！")
		return;
	}
	var username = $("#username").val().trim();
	var realname = $("#realname").val().trim();
	var sex = $("#sex").val();
	var role = $("#role").val().trim();
	var dept = $("#dept").val().trim();;
	var email = $("#email").val().trim();
	var mobile = $("#mobile").val().trim();
	
	if (username == null || username == undefined || username == "") {
		alert("请填写账号名称！");
		return;
	}
	if (realname == null || realname == undefined || realname == "") {
		alert("描述不能为空！");
		return;
	}	
	if (sex == null || sex == undefined || sex == "") {
		alert("请选择性别！");
		return;
	}
	if (role == null || role == undefined || role == "") {
		alert("请选择角色！");
		return;
	}
	if (dept == null || dept == undefined || dept == "") {
		alert("请选择部门！");
		return;
	}
	var dataJson = {};
	dataJson["username"] = username;
	dataJson["realname"] = realname;
	dataJson["sex"] = sex;
	dataJson["role"] = role;
	dataJson["dept"] = dept;
	if (email != "") {
		dataJson["email"] = email;
	}
	if (mobile != "") {
		dataJson["mobile"] = mobile;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Admin/add",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				alert(json.info);
				if (json.state == 0) {
				//	window.location = "adminManage.html";
					jumpPage("adminManage.html");
				}
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

$("#username").blur(function() {
	var username = $("#username").val().trim();
	var dataJson = {};
	dataJson["username"] = username;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Admin/ifUser",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json) {
				$("#idUser").html("已存在该账号名称，请重新输入！");
			} else {
				$("#idUser").html("*");
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
})

