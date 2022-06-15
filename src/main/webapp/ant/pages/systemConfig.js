$(document).ready(function() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		data : {
			id : 1
		},		
		url : "/Config/getConfigById",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
            if (json != null) {
            	$("#id").html(json.id);
				$("#jmsInterval").attr({"value":json.jmsInterval});
				$("#jmsAddress").attr({"value":json.jmsAddress});
				$("#jmsPort").attr({"value":json.jmsPort});
				$("#jmsAgptIp").attr({"value":json.jmsAgptIp});
				$("#jmsSfrzIp").attr({"value":json.jmsSfrzIp});
				$("#jmsPtglIp").attr({"value":json.jmsPtglIp});	
				$("#dbDeleteInterval").attr({"value":json.dbDeleteInterval});	
				$("#dbOnlineInterval").attr({"value":json.dbOnlineInterval});
				$("#alarmPingInterval").attr({"value":json.alarmPingInterval});	
				
				if (json.jmsSwitch == "1") {
					document.getElementById("jmsSwitch").options.length=0;
					document.getElementById("jmsSwitch").options.add(new Option("开启", "1"));
					document.getElementById("jmsSwitch").options.add(new Option("关闭", "0"));
				} else {
					document.getElementById("jmsSwitch").options.length=0;
					document.getElementById("jmsSwitch").options.add(new Option("关闭", "0"));
					document.getElementById("jmsSwitch").options.add(new Option("开启", "1"));
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
				
function updateConfig() {
	var id = $("#id").html();
	var jmsSwitch = $("#jmsSwitch").val();
	var jmsInterval = $("#jmsInterval").val();
	var jmsAddress = $("#jmsAddress").val();
	var jmsPort = $("#jmsPort").val();
	var jmsAgptIp = $("#jmsAgptIp").val();
	var jmsSfrzIp = $("#jmsSfrzIp").val();
	var jmsPtglIp = $("#jmsPtglIp").val();
	var dbDeleteInterval = $("#dbDeleteInterval").val();
	var dbOnlineInterval = $("#dbOnlineInterval").val();
	var alarmPingInterval = $("#alarmPingInterval").val();

	var dataJson = {};
	dataJson["id"] = id;
	if (jmsSwitch != "") {
		dataJson["jmsSwitch"] = jmsSwitch;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsInterval != "") {
		dataJson["jmsInterval"] = jmsInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsAddress != "") {
		dataJson["jmsAddress"] = jmsAddress;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsPort != "") {
		dataJson["jmsPort"] = jmsPort;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsAgptIp != "") {
		dataJson["jmsAgptIp"] = jmsAgptIp;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsSfrzIp != "") {
		dataJson["jmsSfrzIp"] = jmsSfrzIp;
	} else {
		alert("所有信息不能为空！");
		return;
	}

	if (jmsPtglIp != "") {
		dataJson["jmsPtglIp"] = jmsPtglIp;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (dbDeleteInterval != "") {
		dataJson["dbDeleteInterval"] = dbDeleteInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (dbOnlineInterval != "") {
		dataJson["dbOnlineInterval"] = dbOnlineInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (alarmPingInterval != "") {
		dataJson["alarmPingInterval"] = alarmPingInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Config/updateConfig",
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

function showNoFunction(width, height) {
    return "<div style='width:"
        + width
        + "%;height:"
        + height
        + "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
        + height + "px;' >抱歉，您没有此处权限！</div>";
}

function syncConfig() {
	var id = $("#id").html();
	var jmsSwitch = $("#jmsSwitch").val();
	var jmsInterval = $("#jmsInterval").val();
	var jmsAddress = $("#jmsAddress").val();
	var jmsPort = $("#jmsPort").val();
	var jmsAgptIp = $("#jmsAgptIp").val();
	var jmsSfrzIp = $("#jmsSfrzIp").val();
	var jmsPtglIp = $("#jmsPtglIp").val();
	var dbDeleteInterval = $("#dbDeleteInterval").val();
	var dbOnlineInterval = $("#dbOnlineInterval").val();
	var alarmPingInterval = $("#alarmPingInterval").val();

	var dataJson = {};
	dataJson["id"] = id;
	if (jmsSwitch != "") {
		dataJson["jmsSwitch"] = jmsSwitch;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsInterval != "") {
		dataJson["jmsInterval"] = jmsInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsAddress != "") {
		dataJson["jmsAddress"] = jmsAddress;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsPort != "") {
		dataJson["jmsPort"] = jmsPort;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsAgptIp != "") {
		dataJson["jmsAgptIp"] = jmsAgptIp;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	if (jmsSfrzIp != "") {
		dataJson["jmsSfrzIp"] = jmsSfrzIp;
	} else {
		alert("所有信息不能为空！");
		return;
	}

	if (jmsPtglIp != "") {
		dataJson["jmsPtglIp"] = jmsPtglIp;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (dbDeleteInterval != "") {
		dataJson["dbDeleteInterval"] = dbDeleteInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (dbOnlineInterval != "") {
		dataJson["dbOnlineInterval"] = dbOnlineInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	if (alarmPingInterval != "") {
		dataJson["alarmPingInterval"] = alarmPingInterval;
	} else {
		alert("所有信息不能为空！");
		return;
	}
	
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Config/syncConfig",
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
