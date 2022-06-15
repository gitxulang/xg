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

function openInfo(obj) {
    const index = layer.open({
        type: 2,
        title: '软件详情报表',
        move: false,
        content: './infoIndex.html?id=' + $(obj).attr('id')
    });

    layer.full(index);
}

$(document).ready(function () {
    $("body").append(
        '<div class="current_orderkey" style="display:none;"></div>' +
        '<div class="current_ordervalue" style="display:none;"></div>' +
        '<div class="orderName_count" style="display:none;">0</div>' +
        '<div class="orderIp_count" style="display:none;">0</div>' +
        '<div class="orderPort_count" style="display:none;">0</div>' +
        '<div class="orderType_count" style="display:none;">0</div>' +
        '<div class="orderPos_count" style="display:none;">0</div>' +
        '<div class="orderDep_count" style="display:none;">0</div>' +
        '<div class="orderColl_count" style="display:none;">0</div>' +
        '<div class="orderMonitor_count" style="display:none;">0</div>' +
        '<div class="orderTime_count" style="display:none;">0</div>'
    );

    loadTable();
})

function loadTable() {
    var dataJson = {};
    dataJson["begin"] = 1;
    dataJson["offset"] = 10;
    loadAssetType();
    loadDepartment();
    query(dataJson);
}

function selectPage() {
    const begin = 1;
    const offset = $("#selectPage").val();
    let request_data = {};
    request_data["begin"] = begin;
    request_data["offset"] = offset;
    query(request_data);
}

function changeOffset() {
    const begin = 1;
    const offset = $("#selectPage").val();
    let request_data = {};
    request_data["begin"] = begin;
    request_data["offset"] = offset;
    query(request_data);
}

function firstPage() {
    if ($("#page").html() != "1") {
        const begin = 1;
        const offset = $("#selectPage").val();
        let request_data = {};
        request_data["begin"] = begin;
        request_data["offset"] = offset;
        query(request_data);
    }
}

function beforePage() {
    if ($("#page").html() != "1") {
        var begin = parseInt($("#page").html()) - 1;
        var offset = $("#selectPage").val();
        var requestData = {};
        requestData["begin"] = begin;
        requestData["offset"] = offset;
        query(requestData);
    }
}


function nextPage() {
    if ($("#page").html() != $("#totalPage").html()) {
        var begin = parseInt($("#page").html()) + 1;
        var offset = $("#selectPage").val();
        var requestData = {};
        requestData["begin"] = begin;
        requestData["offset"] = offset;
        query(requestData);
    }
}

function lastPage() {
    if ($("#page").html() != $("#totalPage").html()) {
        var begin = parseInt($("#totalPage").html())
        var offset = $("#selectPage").val();
        var requestData = {};
        requestData["begin"] = begin;
        requestData["offset"] = offset;
        query(requestData);
    }
}

function setOrderKey(obj) {
    const order = $(obj).attr("id");
    let orderKey = "";
    let orderValue = 0;
    if (order == "orderName") {
        $(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
        orderKey = "AName";
        orderValue = parseInt($(".orderName_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderIp") {
        $(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
        orderKey = "AIp";
        orderValue = parseInt($(".orderIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderType") {
        $(".orderType_count").html(parseInt($(".orderType_count").html()) + 1);
        orderKey = "nmsAssetType.id";
        orderValue = parseInt($(".orderType_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderPos") {
        $(".orderPos_count").html(parseInt($(".orderPos_count").html()) + 1);
        orderKey = "APos";
        orderValue = parseInt($(".orderPos_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderPort") {
        $(".orderPort_count").html(parseInt($(".orderPort_count").html()) + 1);
        orderKey = "APort";
        orderValue = parseInt($(".orderPort_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderDep") {
        $(".orderNumber_count").html(parseInt($(".orderNumber_count").html()) + 1);
        orderKey = "nmsDepartment.id";
        orderValue = parseInt($(".orderNumber_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderColl") {
        $(".orderColl_count").html(parseInt($(".orderColl_count").html()) + 1);
        orderKey = "collMode";
        orderValue = parseInt($(".orderColl_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderMonitor") {
        $(".orderMonitor_count").html(parseInt($(".orderMonitor_count").html()) + 1);
        orderKey = "colled";
        orderValue = parseInt($(".orderMonitor_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderTime") {
        $(".orderTime_count").html(parseInt($(".orderTime_count").html()) + 1);
        orderKey = "itime";
        orderValue = parseInt($(".orderTime_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }

    const begin = $("#page").html();
    const offset = $("#selectPage").val();

    let requestData = {};
    requestData["begin"] = begin;
    requestData["offset"] = offset;

    if (orderKey != "") {
        requestData["orderKey"] = orderKey;
        requestData["orderValue"] = orderValue;
    }

    query(requestData);
}

function query(data_json) {
    // 每次查询均获取current_orderkey和current_ordervalue值
    const orderKey = $(".current_orderkey").html();
    const orderValue = $(".current_ordervalue").html();
    data_json["orderKey"] = orderKey;
    data_json["orderValue"] = orderValue;

    const name = $("#name").val();
    const ip = $("#ip").val();
    const port = $("#port").val();
    const type = $("#a_type").val();
    const dept = $("#a_dept").val();
    const itime_start = $("#itime_start").val();
    const itime_end = $("#itime_end").val();
    if (name != "") {
        data_json["a_name"] = name;
    }
    if (ip != "") {
        data_json["a_ip"] = ip;
    }
    if (port != "") {
        data_json["a_port"] = port;
    }
    if (type != "-1") {
        data_json["a_type"] = type;
    }
    if (dept != "-1") {
        data_json["a_dept"] = dept;
    }
    if (itime_start != "" && itime_start != null) {
        data_json["itime_start"] = itime_start;
    }
    if (itime_end != "" && itime_end != null) {
        data_json["itime_end"] = itime_end;
    }

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Soft/list/date/reportSelect",
        data: data_json,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
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
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());
    for (var i in json.list) {
        var n = parseInt(i) + 1 + (begin - 1) * offset;
        var colled;
        var collMode;
        var date;
        if (json.list[i].colled == "0") {
            colled = "已监控";
        } else {
            colled = "未监控";
        }

        if (json.list[i].collMode == null) {
            collMode = "无";
        } else if (json.list[i].collMode == "0") {
            collMode = "专用代理";
        } else if (json.list[i].collMode == "1") {
            collMode = "ICMP协议";
        } else if (json.list[i].collMode == "2") {
            collMode = "SNMP协议";
        } else if (json.list[i].collMode == "3") {
            collMode = "JDBC协议";
        } else if (json.list[i].collMode == "4") {
            collMode = "JMX协议";
        } else {
            collMode = "其它";
        }

        var department = "--";
        if (json.list[i].nmsDepartment != null) {
            department = json.list[i].nmsDepartment.dname;
        }

        var date = format(json.list[i].itime);
        $("#tableBodyID").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td>' + htmlEscape(json.list[i].aname) + '</td>' +
            '<td><a href="#" onclick="showDetail(' + json.list[i].id + ',' + json.list[i].nmsAssetType.id + ')">' + json.list[i].aip + '</td>' +
            '<td>' + htmlEscape(json.list[i].aport) + '</td>' +
            '<td>' + json.list[i].nmsAssetType.chType + '/' + json.list[i].nmsAssetType.chSubtype + '</td>' +
            '<td>' + htmlEscape(json.list[i].apos) + '</td>' +
            '<td>' + department + '</td>' +
            '<td>' + collMode + '</td>' +
            '<td>' + colled + '</td>' +
            '<td>' + date + '</td>' +
            '<td class="center"><a href="#" onclick="showDetail(' + json.list[i].id + ',' + json.list[i].nmsAssetType.id + ')"><i class="my-material-icons">search</i></a></td>' +
            '</tr>'
        )
    }

    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function showDetail(id, typeid) {
    if (id == null || id == "") {
        alert("您查询的软件ID为空！");
        return;
    }
    switch (typeid) {
        case 22: //专用数据库
        //  window.location.href = "databaseBasicInfo.html?id=" + id;
            jumpPage("databaseBasicInfo.html?id=" + id);
            break;
        case 23: //专用中间件
        //  window.location.href = "middlewareBasicInfo.html?id=" + id;
            jumpPage("middlewareBasicInfo.html?id=" + id);
            break;
    }
}

function add0(m) {
    return m < 10 ? '0' + m : m
}

function format(time_str) {
    var time = new Date(time_str);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
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
                        '<option value="' + json[i].id + '">' + json[i].chType + '/' + json[i].chSubtype + '</option>'
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

function exportTable() {
    var url = "/Soft/list/date/reportSelect/exportExcel?"
    var name = $("#name").val();
    var ip = $("#ip").val();
    var port = $("#port").val();
    var type = $("#a_type").val();
    var dept = $("#a_dept").val();
    var itime_start = $("#itime_start").val();
    var itime_end = $("#itime_end").val();

    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();

    if (name != "") {
        url += "a_name=" + name + "&";
    }
    if (ip != "") {
        url += "a_ip=" + ip + "&";
    }
    if (port != "") {
        url += "a_port=" + port + "&";
    }
    if (type != "0" && type != "-1") {
        url += "a_type=" + type + "&";
    }
    if (dept != "0" && dept != "-1") {
        url += "a_dept=" + dept + "&";
    }
    if (itime_start != "" && itime_start != null) {
        url += "itime_start=" + itime_start + "&";
    }
    if (itime_end != "" && itime_end != null) {
        url += "itime_end=" + itime_end + "&";
    }

    if (orderKey != "0" && orderKey != undefined && orderKey != null) {
        url += "orderKey=" + orderKey + "&";
    }

    if (orderValue != "0" && orderValue != undefined && orderValue != null) {
        url += "orderValue=" + orderValue + "&";
    }

    window.location.href = url;
}

$(function () {
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#name').keydown(function (event) {
        if (event.keyCode == 13) {
            selectPage();
        }
    });
});

$(function () {
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#ip').keydown(function (event) {
        if (event.keyCode == 13) {
            selectPage();
        }
    });
});

$("#itime_start").datetimepicker({
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    todayBtn: true
});

$("#itime_end").datetimepicker({
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    todayBtn: true
});

