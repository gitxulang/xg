
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
		url : "/User/getUserById",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				$("#id").val(json.id);
				$("#name").attr({"value":json.name});
				$("#card").attr({"value":json.card});
				$("#sex").val(json.sex);
				$("#birthDate").attr({"value":json.birthDate});
				$("#education").attr({"value":json.education});
				$("#nationality").attr({"value":json.nationality});
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
	var name = $("#name").val();
	var sex = $("#sex").val();
	var dept = $("#dept").val();
	var education = $("#education").val();
	var nationality = $("#nationality").val();
	var birthDate = $("#birthDate").val();

	if (name == "" || name == undefined) {
		alert("姓名不能为空！");
		return;
	}
	if (sex == "" || sex == undefined) {
		alert("请选择性别！");
		return;
	}
	if (dept == "0" || dept == "-1" || dept == null) {
		alert("请选择单位部门！");
		return;
	}
	
	var dataJson = {};
	dataJson["id"] = id;
	dataJson["name"] = name;
	dataJson["sex"] = sex;
	dataJson["dept"] = dept;
	if (education != null && education != "") {
		dataJson["education"] = education;
	}
	if(nationality != null && nationality != "") {
		dataJson["nationality"] = nationality;
	}
	if (birthDate != "" && birthDate != null) {
		dataJson["birthDate"] = birthDate;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/User/updateUser",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				alert(json.info);
				if (json.state == "0") {
					jumpPage("commonUser.html");
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

$("#birthDate").datetimepicker({
	language: "zh-cn",
	format: "yyyy-mm-dd",
	minView: "month",
	autoclose: true,
	todayBtn: true
});