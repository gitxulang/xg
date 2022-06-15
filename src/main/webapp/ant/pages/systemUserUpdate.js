
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

$(document).ready(function(){
	var request = GetRequest();
	var id = request.id;
	loadUserProfile(id);
})

function loadUserProfile(id) {
	var dataJson = {};
	dataJson["id"] = id;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Admin/getAdminById",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				$("#id").val(json.id);
				$("#username").attr({"value":json.username});
				$("#realname").attr({"value":json.realname});
				$("#sex").val(json.sex);
				$("#mobile").attr({"value":json.phone});
				$("#email").attr({"value":json.email});
				loadRole(json.nmsRole.id);
				loadDepartment(json.nmsDepartment.id);
			}
		},
		beforeSend: function(xhr) {
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

function loadRole(roleId){
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/role/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			for (var i in json){
				/*if (json[i].id != 4) {
					continue;
				}*/
				if (json[i].id == roleId) {
					$("#role").append('<option value="' + json[i].id + '" selected>' + json[i].role + '</option>');
				} else {
					$("#role").append('<option value="' + json[i].id+'" >' + json[i].role + '</option>');
				}
			}
		},
		beforeSend: function(xhr) {
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

function loadDepartment(deptId){
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Department/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			for (var i in json){
				if (json[i].id == deptId) {
					$("#dept").append('<option value="' + json[i].id + '" selected>' + json[i].dname + '</option>');
				} else {
					$("#dept").append('<option value="' + json[i].id + '" >' + json[i].dname + '</option>');
				}
			}
		},
		beforeSend: function(xhr) {
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

function updateUser(){
	var id = $("#id").val();
	var username = $("#username").val().trim();
	var realname = $("#realname").val().trim();
	var sex = $("#sex").val();
	var role = $("#role").val();
	var dept = $("#dept").val();
	var email = $("#email").val().trim();
	var mobile = $("#mobile").val().trim();

	if (username == null || username == undefined || username == "") {
		alert("账号名称不能为空！");
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
	dataJson["id"] = id;
	dataJson["username"] = username;
	dataJson["realname"] = realname;
	dataJson["sex"] = sex;
	dataJson["role"] = role;
	dataJson["dept"] = dept;
	if (email != null && email != "") {
		dataJson["email"] = email;
	}
	if(mobile != null && mobile != "") {
		dataJson["mobile"] = mobile;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Admin/updateUser",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				alert(json.info);
				if (json.state == "0") {
				//	window.location = "adminManage.html";
					jumpPage("adminManage.html");
				}
			}
		},
		beforeSend: function(xhr) {
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

