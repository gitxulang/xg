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
        '<div class="orderPort_count" style="display:none;">0</div>' +
        '<div class="orderUndo_count" style="display:none;">0</div>' +
        '<div class="orderDoing_count" style="display:none;">0</div>' +
        '<div class="orderDone_count" style="display:none;">0</div>' +
        '<div class="orderTotal_count" style="display:none;">0</div>'
    );

    loadTable();
})

function loadTable() {
    var data_json = {};
    data_json["begin"] = 1;
    data_json["offset"] = 10;
    query(data_json);
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
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;
    if (order == "orderName") {
        $(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
        orderKey = "a_name";
        orderValue = parseInt($(".orderName_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderType") {
        $(".orderType_count").html(parseInt($(".orderType_count").html()) + 1);
        orderKey = "ch_type";
        orderValue = parseInt($(".orderType_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderIp") {
        $(".orderIp_count").html(parseInt($(".orderIp_count").html()) + 1);
        orderKey = "a_ip";
        orderValue = parseInt($(".orderIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderPort") {
        $(".orderPort_count").html(parseInt($(".orderPort_count").html()) + 1);
        orderKey = "APort";
        orderValue = parseInt($(".orderPort_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderUndo") {
        $(".orderUndo_count").html(parseInt($(".orderUndo_count").html()) + 1);
        orderKey = "undealTotalCount";
        orderValue = parseInt($(".orderUndo_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderDoing") {
        $(".orderDoing_count").html(parseInt($(".orderDoing_count").html()) + 1);
        orderKey = "dealIngTotalCount";
        orderValue = parseInt($(".orderDoing_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderDone") {
        $(".orderDone_count").html(parseInt($(".orderDone_count").html()) + 1);
        orderKey = "dealedTotalCount";
        orderValue = parseInt($(".orderDone_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderTotal") {
        $(".orderTotal_count").html(parseInt($(".orderTotal_count").html()) + 1);
        orderKey = "totalCount";
        orderValue = parseInt($(".orderTotal_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }


    var begin = $("#page").html();
    var offset = $("#selectPage").val();
    var request_data = {};
    request_data["begin"] = begin;
    request_data["offset"] = offset;

    if (orderKey != "") {
        request_data["orderKey"] = orderKey;
        request_data["orderValue"] = orderValue;
    }

    query(request_data);
}

function query(data_json) {
    // 每次查询均获取current_orderkey和current_ordervalue值
    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();
    data_json["orderKey"] = orderKey;
    data_json["orderValue"] = orderValue;

    var assetName = $("#name").val();
    var ip = $("#ip").val();
    var port = $("#port").val();

    if (assetName != "") {
        data_json["name"] = assetName;
    }
    if (ip != "") {
        data_json["ip"] = ip;
    }
    if (port != "") {
        data_json["port"] = port;
    }

    $.ajax({
        type: 'POST',
        data: data_json,
        dataType: "json",
        url: "/alarm/list/statics/soft/condition",
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
    var level = '';
    var status = '';
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());
    for (var i in json.list) {
        var n = parseInt(i) + 1 + (begin - 1) * offset;
        $("#tableBodyID").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td>' + htmlEscape(json.list[i].nmsSoftName) + '</td>' +
            '<td>' + htmlEscape(json.list[i].nmsSoftIp) + '</td>' +
            '<td>' + htmlEscape(json.list[i].nmsSoftPort) + '</td>' +
            '<td>' + json.list[i].nmsAssetType + '/' + json.list[i].nmsAssetSubType + '</td>' +
            '<td>' + json.list[i].unDealTotalCount + '</td>' +
            '<td>' + json.list[i].dealingTotalCount + '</td>' +
            '<td>' + json.list[i].dealedTotalCount + '</td>' +
            '<td>' + json.list[i].alarmTotalCount + '</td>' +
            '<td class="center"><a href="/alarm/list/page/soft/condition/exportExcel?nmsSoftId=' + json.list[i].nmsSoftId + '"><i class="my-material-icons">input</i></a></td>' +
            '</tr>'
        )
    }
    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function exportTable() {
    var url = "/alarm/list/statics/soft/condition/exportExcel?"
    var softName = $("#name").val();
    var ip = $("#ip").val();
    var port = $("#port").val();

    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();

    if (softName != '' && softName != undefined && softName != null) {
        url += "name=" + softName + "&";
    }
    if (ip != '' && ip != undefined && ip != null) {
        url += "ip=" + ip + "&";
    }

    if (port != '' && port != undefined && port != null) {
        url += "port=" + port + "&";
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

