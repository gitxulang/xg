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
            + '<div class="orderConnNum_count" style="display:none;">0</div>'
            + '<div class="orderRPS_count" style="display:none;">0</div>'
            + '<div class="orderMemTotal_count" style="display:none;">0</div>'
            + '<div class="orderMemUsed_count" style="display:none;">0</div>'
            + '<div class="orderHeapTotal_count" style="display:none;">0</div>'
            + '<div class="orderHeapUsed_count" style="display:none;">0</div>'
            + '<div class="orderNonHeapTotal_count" style="display:none;">0</div>'
            + '<div class="orderNonHeapUsed_count" style="display:none;">0</div>'
            + '<div class="orderJvmThreadNum_count" style="display:none;">0</div>'
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
    if (order == "orderConnNum") {
        $(".orderConnNum_count").html(parseInt($(".orderConnNum_count").html()) + 1);
        orderKey = "conn_num";
        orderValue = parseInt($(".orderConnNum_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderRPS") {
        $(".orderRPS_count").html(parseInt($(".orderRPS_count").html()) + 1);
        orderKey = "rps";
        orderValue = parseInt($(".orderRPS_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderMemTotal") {
        $(".orderMemTotal_count").html(parseInt($(".orderMemTotal_count").html()) + 1);
        orderKey = "mem_total";
        orderValue = parseInt($(".orderMemTotal_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderMemUsed") {
        $(".orderMemUsed_count").html(parseInt($(".orderMemUsed_count").html()) + 1);
        orderKey = "mem_used";
        orderValue = parseInt($(".orderMemUsed_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderHeapTotal") {
        $(".orderHeapTotal_count").html(parseInt($(".orderHeapTotal_count").html()) + 1);
        orderKey = "heap_total";
        orderValue = parseInt($(".orderHeapTotal_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderHeapUsed") {
        $(".orderHeapUsed_count").html(parseInt($(".orderHeapUsed_count").html()) + 1);
        orderKey = "heap_used";
        orderValue = parseInt($(".orderHeapUsed_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderNonHeapTotal") {
        $(".orderNonHeapTotal_count").html(parseInt($(".orderNonHeapTotal_count").html()) + 1);
        orderKey = "nonheap_total";
        orderValue = parseInt($(".orderNonHeapTotal_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderNonHeapUsed") {
        $(".orderNonHeapUsed_count").html(parseInt($(".orderNonHeapUsed_count").html()) + 1);
        orderKey = "nonheap_used";
        orderValue = parseInt($(".orderNonHeapUsed_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderJvmThreadNum") {
        $(".orderJvmThreadNum_count").html(parseInt($(".orderJvmThreadNum_count").html()) + 1);
        orderKey = "jvm_thread_num";
        orderValue = parseInt($(".orderJvmThreadNum_count").html()) % 2;

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
        url: "/MiddlewareStatus/list/date",
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
            '<td>' + json.list[i].connNum + '</td>' +
            '<td>' + json.list[i].rps + '</td>' +
            '<td>' + json.list[i].memTotal + '</td>' +
            '<td>' + json.list[i].memUsed + '</td>' +
            '<td>' + json.list[i].heapTotal + '</td>' +
            '<td>' + json.list[i].heapUsed + '</td>' +
            '<td>' + json.list[i].nonHeapTotal + '</td>' +
            '<td>' + json.list[i].nonHeapUsed + '</td>' +
            '<td>' + json.list[i].jvmThreadNum + '</td>' +
            '<td>' + format(json.list[i].itime) + '</td>' +
            '</tr>'
        );
    }
    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function exportTable() {
    var url = "/MiddlewareStatus/list/date/ExportExcel?"
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

