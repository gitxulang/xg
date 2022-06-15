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
    $("body").append(
        '<div class="current_orderkey" style="display:none;"></div>' +
        '<div class="current_ordervalue" style="display:none;"></div>' +
        '<div class="orderName_count" style="display:none;">0</div>' +
        '<div class="orderType_count" style="display:none;">0</div>' +
        '<div class="orderIp_count" style="display:none;">0</div>' +
        '<div class="orderPort_count" style="display:none;">0</div>'
    );

    var dataJson = {};
    dataJson["begin"] = 1;
    dataJson["offset"] = 10;

    queryList(dataJson);
    loadAssetType();
})

function queryList(dataJson) {
    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();
    dataJson["orderKey"] = orderKey;
    dataJson["orderValue"] = orderValue;

    var name = $("#name").val();
    var ip = $("#ip").val();
    var port = $("#port").val();
    var type = $("#type").val();
    if (name != "") {
        dataJson["AName"] = name;
    }

    if (ip != "") {
        dataJson["AIp"] = ip;
    }

    if (port != "") {
        dataJson["APort"] = ip;
    }

    if (type != "-1") {
        dataJson["nmsAssetType"] = type;
    }

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/performance/soft/list/page/condition",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 10000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                loadList(json);
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

function htmlEscape(text) {
    return text.replace(/[<>"&]/g, function (match, pos, originalText) {
        switch (match) {
            case "<":
                return "&lt;";
            case ">":
                return "&gt;";
            case "&":
                return "&amp;";
            case "\"":
                return "&quot;";
        }
    });
}

function loadList(json) {
    $("#tableBodyID").empty();

    var n = 1;
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());

    for (i in json.list) {
        n = parseInt((begin - 1) * offset) + 1 + parseInt(i);

        var strHtml = '<tr class="odd gradeX"><td class="center">' + n + '</td>';
        if (json.list[i].ifAlarm) {
            strHtml += '<td><font color="red">' + json.list[i].aname + '</font></td>'
        } else {
            strHtml += '<td><font color="green">' + json.list[i].aname + '</font></td>'
        }

        var colledStr = '未监控';

        if (json.list[i].colled == 0) {
            colledStr = '已监控';
        }

        strHtml += '<td>' + htmlEscape(json.list[i].aip) + '</td>';
        strHtml += '<td>' + htmlEscape(json.list[i].aport) + '</td>';
        strHtml += '<td>' + json.list[i].chType + '/' + json.list[i].subChType + '</td>';
        strHtml += '<td>' + htmlEscape(json.list[i].auser) + '</td>';
        strHtml += '<td>' + colledStr + '</td>';
        //strHtml += '<td class="center"><a href="#" onclick="showDetail(' + json.list[i].id + ', ' + json.list[i].typeId + ')"><i class="my-material-icons">search</i></a></td>';

        $("#tableBodyID").append(strHtml);
    }

    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
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
                    $("#type").append(
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

function showDetail(id, typeid) {
    if (id == null || id == "") {
        alert("您查询的软件ID为空！");
        return;
    }
    if (typeid == null && typeid < 0) {
        alert("您查询的软件typeid为空！");
        return;
    }
    if (typeid == 22) {//专用数据库
    //  window.location.href = "performanceDatabase.html?id=" + id + "&typeid=" + typeid;
        var url = "performanceDatabase.html?id=" + id + "&typeid=" + typeid;
        jumpPage(url);
    } else if (typeid == 23) {//专用中间件
    //  window.location.href = "performanceMiddleware.html?id=" + id + "&typeid=" + typeid;
        var url = "performanceMiddleware.html?id=" + id + "&typeid=" + typeid;
        jumpPage(url);
    }
}

function changeOffset() {
    var dataJson = {};
    var offset = $("#selectPage").val();
    dataJson["begin"] = 1;
    dataJson["offset"] = offset;
    queryList(dataJson);
}

function firstPage() {
    if ($("#page").html() != "1") {
        var begin = 1;
        var offset = $("#selectPage").val();
        var dataJson = {};
        dataJson["begin"] = begin;
        dataJson["offset"] = offset;
        queryList(dataJson);
    }
}

function beforePage() {
    if ($("#page").html() != "1") {
        var begin = parseInt($("#page").html()) - 1;
        var offset = $("#selectPage").val();
        var dataJson = {};
        dataJson["begin"] = begin;
        dataJson["offset"] = offset;
        queryList(dataJson);
    }
}

function nextPage() {
    if ($("#page").html() != $("#totalPage").html()) {
        var begin = parseInt($("#page").html()) + 1;
        var offset = $("#selectPage").val();
        var dataJson = {};
        dataJson["begin"] = begin;
        dataJson["offset"] = offset;
        queryList(dataJson);
    }
}

function lastPage() {
    if ($("#page").html() != $("#totalPage").html()) {
        var begin = parseInt($("#totalPage").html())
        var offset = $("#selectPage").val();
        var dataJson = {};
        dataJson["begin"] = begin;
        dataJson["offset"] = offset;
        queryList(dataJson);
    }
}

function setOrderKey(obj) {
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;
    if (order == "a_name") {
        $(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
        orderKey = "a_name";
        orderValue = parseInt($(".orderName_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "a_type") {
        $(".orderType_count").html(parseInt($(".orderType_count").html()) + 1);
        orderKey = "type_id";
        orderValue = parseInt($(".orderType_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "a_ip") {
        $(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
        orderKey = "a_ip";
        orderValue = parseInt($(".orderIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "a_port") {
        $(".orderPort_count").html(parseInt($(".orderPort_count").html()) + 1);
        orderKey = "a_port";
        orderValue = parseInt($(".orderPort_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }

    var begin = $("#page").html()
    var offset = $("#selectPage").val();
    var requestData = {};
    requestData["begin"] = begin;
    requestData["offset"] = offset;

    if (orderKey != "") {
        requestData["orderKey"] = orderKey;
        requestData["orderValue"] = orderValue;
    }
    queryList(requestData);
}

function queryData() {
    var begin = 1;
    var offset = $("#selectPage").val();
    var dataJson = {};
    dataJson["begin"] = begin;
    dataJson["offset"] = offset;
    queryList(dataJson);
}

$(function () {
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#ip').keydown(function (event) {
        if (event.keyCode == 13) {
            queryData();
        }
    });
});

$(function () {
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#name').keydown(function (event) {
        if (event.keyCode == 13) {
            queryData();
        }
    });
});

$(function () {
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#port').keydown(function (event) {
        if (event.keyCode == 13) {
            queryData();
        }
    });
});

function changeType() {
    queryData();
}

