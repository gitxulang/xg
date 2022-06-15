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
        '<div class="orderVersion_count" style="display:none;">0</div>' +
        '<div class="orderMaxConnections_count" style="display:none;">0</div>' +
        '<div class="orderThreadsConnected_count" style="display:none;">0</div>' +
        '<div class="orderThreadsRunning_count" style="display:none;">0</div>' +
        '<div class="orderDbReadOnly_count" style="display:none;">0</div>' +
        '<div class="orderQps_count" style="display:none;">0</div>' +
        '<div class="orderTps_count" style="display:none;">0</div>' +
        '<div class="orderAbortedClients_count" style="display:none;">0</div>' +
        '<div class="orderQuestions_count" style="display:none;">0</div>' +
        '<div class="orderProcesslist_count" style="display:none;">0</div>' +
        '<div class="orderItime_count" style="display:none;">0</div>'
    );

	var request = GetRequest();
	var id = request.id;
	var redirect = request.redirect;
	if (id == null || id == "" || redirect == null || redirect == "") {
		return;
	}
	$("#assetId").val(id);
	loadTab(id, 'sof', redirect);
	
    loadTable();
})

function loadTable() {
    var dataJson = {};
    dataJson["begin"] = 1;
    dataJson["offset"] = 10;
    query(dataJson);
}

function selectPage() {
    begin = 1;
    offset = $("#selectPage").val();
    requestData = {};
    requestData["begin"] = begin;
    requestData["offset"] = offset;
    query(requestData);
}

function changeOffset() {
    begin = 1;
    offset = $("#selectPage").val();
    requestData = {};
    requestData["begin"] = begin;
    requestData["offset"] = offset;
    query(requestData);
}

function firstPage() {
    if ($("#page").html() != "1") {
        begin = 1;
        offset = $("#selectPage").val();
        requestData = {};
        requestData["begin"] = begin;
        requestData["offset"] = offset;
        query(requestData);
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

function setOrderKey(obj) {
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;
    if (order == "orderVersion") {
        $(".orderVersion_count").html(parseInt($(".orderVersion_count").html()) + 1);
        orderKey = "db_version";
        orderValue = parseInt($(".orderVersion_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderMaxConnections") {
        $(".orderMaxConnections_count").html(parseInt($(".orderMaxConnections_count").html()) + 1);
        orderKey = "max_connections";
        orderValue = parseInt($(".orderMaxConnections_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderThreadsConnected") {
        $(".orderThreadsConnected_count").html(parseInt($(".orderThreadsConnected_count").html()) + 1);
        orderKey = "threads_connected";
        orderValue = parseInt($(".orderThreadsConnected_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderThreadsRunning") {
        $(".orderThreadsRunning_count").html(parseInt($(".orderThreadsRunning_count").html()) + 1);
        orderKey = "threads_running";
        orderValue = parseInt($(".orderThreadsRunning_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderDbReadOnly") {
        $(".orderDbReadOnly_count").html(parseInt($(".orderDbReadOnly_count").html()) + 1);
        orderKey = "db_read_only";
        orderValue = parseInt($(".orderDbReadOnly_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderQps") {
        $(".orderQps_count").html(parseInt($(".orderQps_count").html()) + 1);
        orderKey = "qps";
        orderValue = parseInt($(".orderQps_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderTps") {
        $(".orderTps_count").html(parseInt($(".orderTps_count").html()) + 1);
        orderKey = "tps";
        orderValue = parseInt($(".orderTps_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderAbortedClients") {
        $(".orderAbortedClients_count").html(parseInt($(".orderAbortedClients_count").html()) + 1);
        orderKey = "aborted_clients";
        orderValue = parseInt($(".orderAbortedClients_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderQuestions") {
        $(".orderQuestions_count").html(parseInt($(".orderQuestions_count").html()) + 1);
        orderKey = "questions";
        orderValue = parseInt($(".orderQuestions_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderProcesslist") {
        $(".orderProcesslist_count").html(parseInt($(".orderProcesslist_count").html()) + 1);
        orderKey = "processlist";
        orderValue = parseInt($(".orderProcesslist_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderItime") {
        $(".orderItime_count").html(parseInt($(".orderItime_count").html()) + 1);
        orderKey = "itime";
        orderValue = parseInt($(".orderItime_count").html()) % 2;
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
    var id = $("#assetId").val();
    data_json["assetId"] = id;

    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();
    data_json["orderKey"] = orderKey;
    data_json["orderValue"] = orderValue;

    var start_time = $("#start_time").val();
    var end_time = $("#end_time").val();
    if (start_time != "" && start_time != null) {
        data_json["startDate"] = start_time;
    }

    if (end_time != "" && end_time != null) {
        data_json["endDate"] = end_time;
    }

    $.ajax({
        type: 'POST',
        data: data_json,
        dataType: "json",
        url: "/MysqlInfo/list/date",
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

function loadList(json) {
    $("#tableBodyID").empty();
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());
    for (var i in json.list) {
        var n = parseInt(i) + 1 + (begin - 1) * offset;
        var pingRate = parseInt(json.list[i].pingRate);

        $("#tableBodyID").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td>' + json.list[i].dbVersion + '</td>' +
            '<td>' + json.list[i].maxConnections + '</td>' +
            '<td>' + json.list[i].threadsConnected + '</td>' +
            '<td>' + json.list[i].threadsRunning + '</td>' +
            '<td>' + json.list[i].dbReadOnly + '</td>' +
            '<td>' + json.list[i].qps + '</td>' +
            '<td>' + json.list[i].tps + '</td>' +
            '<td>' + json.list[i].abortedClients + '</td>' +
            '<td>' + json.list[i].questions + '</td>' +
            '<td>' + json.list[i].processlist + '</td>' +
            '<td>' + format(json.list[i].itime) + '</td>' +
            '</tr>'
        )
    }
    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function exportTable() {
    var url = "/MysqlInfo/list/date/ExportExcel?"
    var start_time = $("#start_time").val();
    var end_time = $("#end_time").val();
    var id = parent.$(".device_id").html();
    url += "assetId=" + id + "&";
    if (start_time != '' && start_time != undefined && start_time != null) {
        url += "startDate=" + start_time + "&";
    }
    if (end_time != '' && end_time != undefined && end_time != null) {
        url += "endDate=" + end_time + "&";
    }

    window.location.href = url;
}

$("#start_time").datetimepicker({
    language : "zh-cn",
    format : "yyyy-mm-dd",
    minView : "month",
    autoclose : true,
    clearBtn : true,
    clearBtn : true,
    todayBtn : true
});

$("#end_time").datetimepicker({
    language : "zh-cn",
    format : "yyyy-mm-dd",
    minView : "month",
    autoclose : true,
    clearBtn : true,
    clearBtn : true,
    todayBtn : true
});