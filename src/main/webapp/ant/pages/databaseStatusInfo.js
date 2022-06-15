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

$(document).ready(
    function () {
        $("body").append(
            '<div class="current_orderkey" style="display:none;"></div>'
            + '<div class="current_ordervalue" style="display:none;"></div>'
            + '<div class="orderTotalSize_count" style="display:none;">0</div>'
            + '<div class="orderMemSize_count" style="display:none;">0</div>'
            + '<div class="orderTPS_count" style="display:none;">0</div>'
            + '<div class="orderIOBusy_count" style="display:none;">0</div>'
            + '<div class="orderConnNum_count" style="display:none;">0</div>'
            + '<div class="orderActiveConnNum_count" style="display:none;">0</div>'
            + '<div class="orderProcessNum_count" style="display:none;">0</div>'
            + '<div class="orderDeadLockNum_count" style="display:none;">0</div>'
            + '<div class="orderUserList_count" style="display:none;">0</div>'
            + '<div class="orderItime_count" style="display:none;">0</div>');

		var request = GetRequest();
		var id = request.id;
		var redirect = request.redirect;
		if (id == null || id == "" || redirect == null || redirect == "") {
			return;
		}
		$("#assetId").val(id);
		loadTab(id, 'sof', redirect);
		
        loadTable();
    }
)

function loadTable(id) {
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
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm)
        + ':' + add0(s);
}

function setOrderKey(obj) {
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;
    if (order == "orderTotalSize") {
        $(".orderTotalSize_count").html(parseInt($(".orderTotalSize_count").html()) + 1);
        orderKey = "total_size";
        orderValue = parseInt($(".orderTotalSize_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderMemSize") {
        $(".orderMemSize_count").html(parseInt($(".orderMemSize_count").html()) + 1);
        orderKey = "mem_size";
        orderValue = parseInt($(".orderMemSize_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderTPS") {
        $(".orderTPS_count").html(parseInt($(".orderTPS_count").html()) + 1);
        orderKey = "tps";
        orderValue = parseInt($(".orderTPS_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderIOBusy") {
        $(".orderIOBusy_count").html(parseInt($(".orderIOBusy_count").html()) + 1);
        orderKey = "io_busy";
        orderValue = parseInt($(".orderIOBusy_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderConnNum") {
        $(".orderConnNum_count").html(parseInt($(".orderConnNum_count").html()) + 1);
        orderKey = "conn_num";
        orderValue = parseInt($(".orderConnNum_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderActiveConnNum") {
        $(".orderActiveConnNum_count").html(parseInt($(".orderActiveConnNum_count").html()) + 1);
        orderKey = "active_conn_num";
        orderValue = parseInt($(".orderActiveConnNum_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderProcessNum") {
        $(".orderProcessNum_count").html(parseInt($(".orderProcessNum_count").html()) + 1);
        orderKey = "process_num";
        orderValue = parseInt($(".orderProcessNum_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderDeadLockNum") {
        $(".orderDeadLockNum_count").html(parseInt($(".orderDeadLockNum_count").html()) + 1);
        orderKey = "dead_lock_num";
        orderValue = parseInt($(".orderDeadLockNum_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderUserList") {
        $(".orderUserList_count").html(parseInt($(".orderUserList_count").html()) + 1);
        orderKey = "user_list";
        orderValue = parseInt($(".orderUserList_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderItime") {
        $(".orderItime_count").html(parseInt($(".orderItime_count").html()) + 1);
        orderKey = "itime";
        orderValue = parseInt($(".orderItime_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }

    begin = $("#page").html();
    offset = $("#selectPage").val();
    var requestData = {};

    requestData["begin"] = begin;
    requestData["offset"] = offset;

    if (orderKey != "") {
        requestData["orderKey"] = orderKey;
        requestData["orderValue"] = orderValue;
    }

    query(requestData);
}

function query(dataJson) {
    var id = $("#assetId").val();
    dataJson["assetId"] = id;

    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();
    dataJson["orderKey"] = orderKey;
    dataJson["orderValue"] = orderValue;

    var start_time = $("#start_time").val();
    var end_time = $("#end_time").val();
    if (start_time != "" && start_time != null) {
        dataJson["startDate"] = start_time;
    }

    if (end_time != "" && end_time != null) {
        dataJson["endDate"] = end_time;
    }

    $.ajax({
        type: 'POST',
        data: dataJson,
        dataType: "json",
        url: "/DatabaseStatus/list/date",
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

        $("#tableBodyID").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td>' + json.list[i].totalSize + '</td>' +
            '<td>' + json.list[i].memSize + '</td>' +
            '<td>' + json.list[i].tps + '</td>' +
            '<td>' + json.list[i].ioBusy + '</td>' +
            '<td>' + json.list[i].connNum + '</td>' +
            '<td>' + json.list[i].activeConnNum + '</td>' +
            '<td>' + json.list[i].processNum + '</td>' +
            '<td>' + json.list[i].deadLockNum + '</td>' +
            '<td>' + json.list[i].userList + '</td>' +
            '<td>' + format(json.list[i].itime) + '</td>' +
            '<td class="center">' +
            '<a href="#" value="' + json.list[i].id + '" onclick="openSlowSql(this)">慢</a>' +
            ' | ' +
            '<a href="#" value="' + json.list[i].id + '" onclick="openStorage(this)">储</a>' +
            '</td>' +
            '</tr>'
        );
    }
    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);

}

function openSlowSql(obj) {
    var id = $(obj).attr("value");
    var url = "databaseSqlInfo.html?id=" + id;
    open_dialog(url, "慢查询列表", 800, 800);
}

function openStorage(obj) {
    var id = $(obj).attr("value");
    var url = "databaseStorageInfo.html?id=" + id;
    open_dialog(url, "存储列表", 800, 800);
}

function open_dialog(url, title, width, height) {
    index = layer.open({
        type: 2,
        title: title,
        area: [width + 'px', height + 'px'],
        fixed: false,
        maxmin: false,
        scrollbar: false,
        content: url
    });
}

function exportTable() {
    var url = "/DatabaseStatus/list/date/ExportExcel?"
    var start_time = $("#start_time").val();
    var end_time = $("#end_time").val();
    var id = $("#assetId").val();

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
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    clearBtn: true,
    todayBtn: true
});

$("#end_time").datetimepicker({
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    clearBtn: true,
    todayBtn: true
});

