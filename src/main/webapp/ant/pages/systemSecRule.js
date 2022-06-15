
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

$(document).ready(function() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		data : {
			id : 1
		},		
		url : "/SecRule/findByIdToUpdate",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
            if (json != null) {
            	$("#id").html(json.id);
				$("#sessionTimeout").attr({"value":json.sessionTimeout});
				$("#pwdMinSize").attr({"value":json.pwdMinSize});
				$("#pwdComplexity").attr({"value":json.pwdComplexity});
				$("#pwdPeriod").attr({"value":json.pwdPeriod});
				$("#loginAttempt").attr({"value":json.loginAttempt});
				$("#secInterval").attr({"value":json.secInterval});	
				
				$("#appId").attr({"value":json.appId});	
				$("#appName").attr({"value":json.appName});	
				
				if (json.ssoSwitch == "1") {
					document.getElementById("ssoSwitch").options.length=0;
					document.getElementById("ssoSwitch").options.add(new Option("开启", "1"));
					document.getElementById("ssoSwitch").options.add(new Option("关闭", "0"));
				} else {
					document.getElementById("ssoSwitch").options.length=0;
					document.getElementById("ssoSwitch").options.add(new Option("关闭", "0"));
					document.getElementById("ssoSwitch").options.add(new Option("开启", "1"));
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
})

function updateSecRule() {
	var id = $("#id").html();
	var sessionTimeout = $("#sessionTimeout").val();
	var pwdMinSize = $("#pwdMinSize").val();
	var pwdComplexity = $("#pwdComplexity").val();
	var pwdPeriod = $("#pwdPeriod").val();
	var loginAttempt = $("#loginAttempt").val();
	var secInterval = $("#secInterval").val();
	
	var appId = $("#appId").val();
	var appName = $("#appName").val();
	var ssoSwitch = $("#ssoSwitch").val();

	var dataJson = {};
	dataJson["id"] = id;
	if (sessionTimeout != "") {
		dataJson["sessionTimeout"] = sessionTimeout;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (pwdMinSize != "") {
		dataJson["pwdMinSize"] = pwdMinSize;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (pwdComplexity != "") {
		dataJson["pwdComplexity"] = pwdComplexity;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (pwdPeriod != "") {
		dataJson["pwdPeriod"] = pwdPeriod;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (loginAttempt != "") {
		dataJson["loginAttempt"] = loginAttempt;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (secInterval != "") {
		dataJson["secInterval"] = secInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}

	if (appId != "") {
		dataJson["appId"] = appId;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (appName != "") {
		dataJson["appName"] = appName;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (ssoSwitch != "") {
		dataJson["ssoSwitch"] = ssoSwitch;
	} else {
		alert("所有信息不能为空！");
		return;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/SecRule/updateRule",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			alert(json.info);
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

function isDigit(pwd) {
	var patrn = /(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{10,}/;
	var pa = new RegExp(patrn);
	if (pa.test(pwd)) {
        return true;
    }else{
       return false;
    }
}

function resetValue() {
	$("#sessionTimeout").val(300);
	$("#pwdMinSize").val(10);
	$("#pwdComplexity").val("11111111");
	$("#pwdPeriod").val(7);
	$("#loginAttempt").val(5);
	$("#secInterval").val(50);
	$("#appId").val("SYSMPT-000000");
	$("#appName").val("涉密专用系统管理基础平台");
}