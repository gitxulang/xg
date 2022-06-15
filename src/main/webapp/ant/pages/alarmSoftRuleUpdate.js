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
    var request = GetRequest();
    var id = request.id;
    $("#ruleId").val(id);
    loadRule(id);
})

function loadRule(id) {
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/RuleSoft/findNmsRuleSoftById",
        data: {"id": id},
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                $("#name").attr("value", json.aname);
                $("#ip").attr("value", json.aip + ":" + json.aport);
                $("#content").attr("value", json.rContent);
                $("#enabled").attr("value", json.rEnable);
                $("#unit").attr("value", json.rUnit);
                $("#seq").attr("value", json.rSeq);
                $("#r_value1").attr("value", json.rValue1);
                $("#r_value2").attr("value", json.rValue2);
                $("#r_value3").attr("value", json.rValue3);
                $("#a_type").attr("value", json.chtype + ' / ' + json.chSubType);
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

function updateRule() {
    var dataJson = {};
    dataJson["id"] = $("#ruleId").val();
    dataJson["ip"] = $("#ip").val();
    dataJson["name"] = $("#name").val();
    dataJson["content"] = $("#content").val();
    dataJson["aType"] = $("#a_type").val();
    dataJson["rEnable"] = $("#enabled").val();
    dataJson["rSeq"] = $("#seq").val();
    dataJson["rValue1"] = $("#r_value1").val();
    dataJson["rValue2"] = $("#r_value2").val();
    dataJson["rValue3"] = $("#r_value3").val();

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/RuleSoft/updateNmsRuleSoftById",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                if (json) {
                    jumpPage("alarmSoftRule.html");
                } else {
                    alert("修改失败！");
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

