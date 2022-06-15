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
/*
function GetRequest() {
    var url = location.search;
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}
*/
$(document).ready(function () {
    var request = GetRequest();
    var id = request.id;
    loadTable(id);
})

function softUpdate() {
    var id = $("#device_id").html();
    var a_ip = $("#a_ip").val();
    var a_name = $("#a_name").val();
    var a_port = $("#a_port").val();
    var a_type = $("#a_type").val();
    var a_pos = $("#a_pos").val();
    var a_manu = $("#a_manu").val();
    var a_user = $("#a_user").val();
    var a_dept = $("#a_dept").val();
    var a_date = $("#a_date").val();
    var colled = $("#colled").val();
    var colled_mode = $("#colled_mode").val();

    if (a_ip == "") {
        alert("IP地址为必选项！");
        return;
    } else if (a_port == "") {
        alert("端口号为必填项！");
        return;
    }

    var data_json = {};
    data_json["id"] = id;
    data_json["AIp"] = a_ip;
    data_json["APort"] = a_port;
    data_json["nmsAssetTypeId"] = a_type;
    data_json["nmsAssetDepartmentId"] = a_dept;
    data_json["colled"] = colled;
    if (a_name != "") {
        data_json["AName"] = a_name;
    }
    if (a_pos != "") {
        data_json["APos"] = a_pos;
    }
    if (a_manu != "") {
        data_json["AManu"] = a_manu;
    }
    if (a_user != "") {
        data_json["AUser"] = a_user;
    }
    if (a_date != "") {
        data_json["ADate"] = a_date;
    }
    if (colled != "") {
        data_json["colled"] = colled;
    }
    if (colled_mode == "-1") {
        alert("启用监控必须设置监控方式");
        return;
    } else {
        data_json["collMode"] = colled_mode;
    }

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Soft/updateSoft",
        data: data_json,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            alert(json);
            jumpPage('softWare.html');
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

function loadTable(id) {
    $("#device_id").html(id);
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Soft/findByIdToUpdate",
        data: {
            id: id
        },
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json == null) {
                $("body").html(showNoFunction(100, 600))
            } else {
                loadDepartment(json.nmsDepartment.id)
                loadAssetType(json.nmsAssetType.id)
                loadColled(json.colled, json.collMode);

                $("#a_ip").attr({
                    "value": json.aip
                });

                $("#a_name").attr({
                    "value": json.aname
                });

                $("#a_port").attr({
                    "value": json.aport
                });

                $("#a_ip").attr({
                    "value": json.aip
                });

                $("#a_pos").attr({
                    "value": json.apos
                });

                $("#a_manu").attr({
                    "value": json.amanu
                });

                $("#a_user").attr({
                    "value": json.auser
                });
                $("#a_date").attr({
                    "value": json.adate
                });
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

function loadDepartment(dept_id) {
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
                if (json[i].id == dept_id) {
                    $("#a_dept").append('<option value="' + json[i].id + '" selected>' + json[i].dname + '</option>');
                } else {
                    $("#a_dept").append('<option value="' + json[i].id + '">' + json[i].dname + '</option>');
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

function loadAssetType(type_id) {
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
                    if (json[i].id == type_id) {
                        $("#a_type").append('<option value="' + json[i].id + '" selected>' + json[i].chType + '/' + json[i].chSubtype + '</option>');
                    } else {
                        $("#a_type").append('<option value="' + json[i].id + '">' + json[i].chType + '/' + json[i].chSubtype + '</option>');
                    }
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

function loadColled(colled, collMode) {
    var colledObj = $("#colled").children();
    var collModeObj = $("#colled_mode").children();
    colledObj.each(function () {
        if ($(this).val() == colled) {
            $(this).attr("selected", true)
        }
    })
    collModeObj.each(function () {
        if ($(this).val() == collMode) {
            $(this).attr("selected", true);
        }
    })
}

