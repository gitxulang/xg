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

$(document).ready(function () {
    loadDepartment();
})

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
                $("#dept").append('<option value="' + json[i].id + '" >' + json[i].dname + '</option>');
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

function addUser() {
    if ($("#idCard").html() != "*") {
        alert("唯一标识重复！")
        return;
    }
    var name = $("#name").val().trim();
    var card = $("#card").val().trim();
    var sex = $("#sex").val();
    var dept = $("#dept").val();
    var education = $("#education").val();
    var nationality = $("#nationality").val();
    var birthDate = $("#birthDate").val();

    if (name == "" || name == undefined) {
        alert("请填写姓名！");
        return;
    }

    if (card == "" || card == undefined) {
        alert("请填唯一标识！");
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
    dataJson["name"] = name;
    dataJson["card"] = card;
    dataJson["dept"] = dept;

    if (sex != "") {
        dataJson["sex"] = sex;
    }
    if (education != "") {
        dataJson["education"] = education;
    }
    if (nationality != "") {
        dataJson["nationality"] = nationality;
    }
    if (birthDate != "" && birthDate != null) {
        dataJson["birthDate"] = birthDate;
    }

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/User/addUser",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                alert(json.info);
                if (json.state == 0) {
                	jumpPage("commonUser.html");
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

$("#card").blur(function () {
    var card = $("#card").val().trim();
    var dataJson = {};
    dataJson["card"] = card;
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/User/ifUser",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json) {
                $("#idCard").html("已存在该唯一标识，请重新输入！");
            } else {
                $("#idCard").html("*");
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
})

$("#birthDate").datetimepicker({
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    todayBtn: true
});

