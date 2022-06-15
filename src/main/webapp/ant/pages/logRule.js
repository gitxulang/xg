
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
	loadinfo(1);
	loadpart(1, "--");
})

function loadinfo(id) {
	if (id == null || id == "" || id == undefined) {
		return;
	}
	$.ajax({
		type : 'POST',
		dataType : "json",
		data : {
			id : id
		},
		url : "/AuditConfig/loadinfo",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : false,
		success : function(json) {
			if (json != null) {
				$("#id").html(json.id);
				$("#dbbasedir").attr({
					"value" : json.dbbasedir
				});
				$("#dbdatadir").attr({
					"value" : json.dbdatadir
				});
				$("#partsize").attr({
					"value" : json.partsize
				});
				$("#partused").attr({
					"value" : json.partused
				});
				$("#rule").attr({
					"value" : json.rule
				});
				partdir = json.partdir;
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

function loadpart(id, partdir) {
	if (id == null || id == undefined) {
		return;
	}
	if (partdir == null || partdir == undefined) {
		return;
	}
	$.ajax({
		type : 'POST',
		dataType : "json",
		data : {
			id : id
		},
		url : "/AuditConfig/loadpart",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : false,
		success : function(json) {
			if (json != null) {
				$("#partdir").empty();
				if (partdir == "") {
					$("#partdir").append('<option value="--" selected>请选择</option>');
				}
				for ( var i in json) {
					if (json[i].partdir == null || json[i].partdir == "") {
						continue;
					}
					if (json[i].partdir == partdir) {
						$("#partdir").append('<option value="' + json[i].partdir + '" selected>' + json[i].partdir + '</option>');
						$("#partsize").attr({"value" : json[i].partsize});
						$("#partused").attr({"value" : json[i].partused});
					} else {
						$("#partdir").append('<option value="' + json[i].partdir + '">' + json[i].partdir + '</option>');
					}
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

function update() {
	var id = $("#id").html();
	var dbbasedir = $("#dbbasedir").val();
	var dbdatadir = $("#dbdatadir").val();
	var partdir = $("#partdir").val();
	var partsize = $("#partsize").val();
	var partused = $("#partused").val();
	var rule = $("#rule").val();

	var dataJson = {};
	dataJson["id"] = id;
	if (dbbasedir != null && dbbasedir != "") {
		dataJson["dbbasedir"] = dbbasedir;
	} else {
		alert("数据库安装目录不能为空！");
		return;
	}
	if (dbdatadir != null && dbdatadir != "") {
		dataJson["dbdatadir"] = dbdatadir;
	} else {
		alert("数据库数据目录不能为空！");
		return;
	}
	if (partdir != null && partdir != "") {
		dataJson["partdir"] = partdir;
	} else {
		alert("数据库所在磁盘分区不能为空！");
		return;
	}
	if (partsize != null && partsize != "") {
		dataJson["partsize"] = partsize;
	} else {
		alert("数据库所在磁盘分区总容量不能为空并且应该为数字！");
		return;
	}
	if (partused != null && partused != "") {
		dataJson["partused"] = partused;
	} else {
		alert("数据库所在磁盘分区剩余容量不能为空并且应该为数字！");
		return;
	}
	if (rule != null && rule != "") {
		if (rule < 0 || rule > 100) {
			alert("数据库日志存储空间阈值必须在0到100之间（%）！");
			return;
		}
		dataJson["rule"] = rule;
	} else {
		alert("数据库日志存储空间阈值不能为空并且应该为数字！");
		return;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/AuditConfig/update",
		data : dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			alert(json.info);
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

function changeSelect() {
	var value = $("#partdir").val();
	loadpart(id, value);
}

function resetValue() {
	$("#rule").val(90);
}

