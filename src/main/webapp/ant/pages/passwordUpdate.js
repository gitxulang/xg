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
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Admin/getAdminById",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				$("#userId").val(json.id);
				$('#realname').val(json.realname);
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
            	$("#body").html(showNoFunction(100, 600));
			}
		}
	});
})

function updateUser(){
	var userId = $("#userId").val();
	var realname = $("#realname").val();
	var oldPassword = document.getElementById("oldPassword").value;
	var new1Password = document.getElementById("new1Password").value;
	var new2Password = document.getElementById("new2Password").value;
	
	var dataJson = {};
	if (realname == null || realname == undefined || realname == "") {
		alert("别名描述不能为空！");
		return;
	}
	if(oldPassword == null || oldPassword == undefined || oldPassword == "") {
		alert("原始密码不能为空！");
		return;
	}
	if (new1Password == null || new1Password == undefined || new1Password == "") {
		alert("更改新密码不能为空！");
		return;
	}
	if (new2Password == null ||  new2Password == undefined || new2Password == "") {
		alert("重复新密码不能为空！");
		return;
	}
	if (new1Password != new2Password) {
		alert("更改密码填写不一致！");
		return;
	}
	if(!isDigit(new1Password)){
		alert("密码复杂度不够，必须是包含数字、大小写字母、特殊字符的10位以上密码！");
		return;
	}
	
	oldPassword = $.base64.encode(oldPassword);
	new1Password = $.base64.encode(new1Password);
	dataJson["userId"] = userId;
	dataJson["realname"] = realname;
	dataJson["oldPwd"] = oldPassword;
	dataJson["newPwd"] = new1Password;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Admin/updatePwdOfLongTime",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : false,
		success : function(json) {
			if (json.state == 0) {
				alert(json.info);
				layout();
			} else {
				alert(json.info);
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
            	$("#body").html(showNoFunction(100, 600));
			}
		}
	});
}
function layout(){
	$.ajax({
		type : 'POST',
		dataType : "json",
		data: null,
		url : "/Admin/logoutAjax",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			parent.location.href = json;
		},
		beforeSend: function(xhr) {
	        xhr.setRequestHeader("Authorization", getCookie("token"));
	    },
		error: function(jqXHR, textStatus, errorThrown) {
	        console.log("[ERROR] /AuditConfig/logoutAjax: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			}
		}
	});
}

function isDigit(pwd){
	var patrn = /(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{10,}/;
	var pa = new RegExp(patrn);
	if (pa.test(pwd)) {
        return true;
    }else{
       return false;
    }
}


