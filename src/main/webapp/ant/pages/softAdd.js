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

$(document).ready(function () {
    loadTable();
    ipOrPortBlur();
})

function loadTable() {
    loadDepartment();
    loadAssetType();
    loadLastSoft();
}

function softAdd() {
    if ($("#ip_alarm").html() == "IP地址:端口号已存在" || $("#port_alarm").html() == "IP地址:端口号已存在") {
        alert("已存在相同IP地址:端口号的软件，无法添加！");
        return;
    }
    var a_ip = $("#a_ip").val();
    var a_name = $("#a_name").val();
    var a_port = $("#a_port").val();
    var a_type = $("#a_type").val();
    var a_pos = $("#a_pos").val();
    var a_manu = $("#a_manu").val();
    var a_user = $("#a_user").val();
    var a_dept = $("#a_dept").val();
    var colled = $("#colled").val();
    var colled_mode = $("#colled_mode").val();
    var a_date = $("#a_date").val();

    if (a_ip == "") {
        alert("IP地址为必填项！");
        return;
    }

    if (a_name == "") {
        alert("软件名称为必填项！");
        return;
    }

    if (a_port == "") {
        alert("端口号为必填项！");
        return;
    }

    var dataJson = {};
    dataJson["AIp"] = a_ip;
    dataJson["APort"] = a_port;
    dataJson["nmsAssetTypeId"] = a_type;
    dataJson["nmsAssetDepartmentId"] = a_dept;
    dataJson["colled"] = colled;

    if (a_name != "") {
        dataJson["AName"] = a_name;
    }

    if (a_pos != "") {
        dataJson["APos"] = a_pos;
    }

    if (a_manu != "") {
        dataJson["AManu"] = a_manu;
    }

    if (a_user != "") {
        dataJson["AUser"] = a_user;
    }

    if (a_date != "") {
        dataJson["ADate"] = a_date;
    }

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Soft/addSoft",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            alert(json);
        },
        beforeSend: function (xhr) {
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
        type: 'POST',
        dataType: "json",
        url: "/Department/all",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            for (var i in json) {
                $("#a_dept").append(
                    '<option value="' + json[i].id + '">' + json[i].dname + '</option>'
                )
            }
        },
        beforeSend: function (xhr) {
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

function loadAssetType() {
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/assetType/all",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            for (var i in json) {
                if (json[i].nodeTag == "sof") {
                    $("#a_type").append(
                        '<option value="' + json[i].id + '">' + json[i].chType + ' / ' + json[i].chSubtype + '</option>'
                    )
                }
            }
        },
        beforeSend: function (xhr) {
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

function loadLastSoft() {
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Soft/loadLastSoft",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                ipOrPortBlur(json.aip, json.aport);
                $("#a_ip").val(json.aip);
                $("#a_name").val(json.aname);
                $("#a_port").val(json.aport);
                $("#a_pos").val(json.apos);
                $("#a_manu").val(json.amanu);
                $("#a_user").val(json.auser);
                $("#a_date").val(json.adate);
            }
        },
        beforeSend: function (xhr) {
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

$("#a_ip").blur(function () {
    ipOrPortBlur(null, null);
})

$("#a_port").blur(function () {
    ipOrPortBlur(null, null);
})

function ipOrPortBlur(ip, port) {
    if (ip == null) {
        var a_ip = $("#a_ip").val().trim();
    } else {
        a_ip = ip;
    }

    if (port == null) {
        var a_port = $("#a_port").val().trim();
    } else {
        a_port = port;
    }

    var dataJson = {};
    dataJson["AIp"] = a_ip;
    dataJson["APort"] = a_port;
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Soft/ifIpOrPort",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json) {
                $("#ip_alarm").html("IP地址:端口号已存在");
                $("#ip_alarm").css("color", "red");
                $("#port_alarm").html("IP地址:端口号已存在");
                $("#port_alarm").css("color", "red");
            } else {
                $("#ip_alarm").html("IP地址:端口号有效");
                $("#ip_alarm").css("color", "green");
                $("#port_alarm").html("IP地址:端口号有效");
                $("#port_alarm").css("color", "green");
            }
        },
        beforeSend: function (xhr) {
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

function importTable() {
    var importFile = $('#importFile')[0].files[0];
    if (null == importFile) {
        alert("您尚未选择导入的xls文件")
        return;
    }
    var formData = new FormData();
    formData.append("importFile", importFile);
    $.ajax({
        url: '/softimport/input',
        dataType: 'json',
        type: 'POST',
        async: false,
        timout: 60 * 1000,
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            alert(data);
            parent.location.reload(true);
        },
        beforeSend: function (xhr) {
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

$("#a_date").datetimepicker({
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    todayBtn: true
});

