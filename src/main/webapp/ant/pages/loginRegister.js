
$(document).ready(function(){
	licenseInfo();
})

function licenseInfo() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/License/Licenseinfo",
		data : {},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json.state == 0 || json.state == 2) {
				$("#info").html(json.info);
			} else {
				$("#info").html("获取证书信息失败！");
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

function licenseRegister(){
	var license = $("#license").val();
	var dataJson = {};
	if (license == undefined || license == ""){
		alert("请输入平台激活码！");
		return;
	}
	dataJson["license"] = license;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/License/Licenseregister",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json.state == 0) {
				alert(json.info);
				window.parent.closeDialog();
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
            	$("#page-inner").html(showNoFunction(100, 600));
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

