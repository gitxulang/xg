
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

function licenseCreate() {
	var type = $("#type").val();
	var day = $("#day").val();
	var computer = $("#computer").val();
	var uniquecode = $("#uniquecode").val();
	if (type == "0") {
		if (day == "") {
			alert("证书时间有效天数不能为空！");
			return;
		}
	}
	if (computer == "" || computer <= 0) {
		alert("证书限制终端数量必须为正整数！");
		return;
	}
	if (uniquecode == "") {
		alert("安装服务器唯一码不能为空！");
		return;
	}
	var dataJson = {};
	dataJson["type"] = type;
	dataJson["day"] = day;
	dataJson["computer"] = computer;
	dataJson["uniquecode"] = uniquecode;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/License/Licensecreate",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json.state == 0) {
				$("#license").val(json.info);
			} else {
				alert(json.info);
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

function resetValue() {
	$("#day").val("");
	$("#computer").val("");
	$("#uniquecode").val("");
	$("#license").val("");
}

