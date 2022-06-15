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
        '<div class="orderVmName_count" style="display:none;">0</div>' +
        '<div class="orderVmVersion_count" style="display:none;">0</div>' +
        '<div class="orderVmVendor_count" style="display:none;">0</div>' +
        '<div class="orderStartTime_count" style="display:none;">0</div>' +
        '<div class="orderMaxHeapMemory_count" style="display:none;">0</div>' +
        '<div class="orderCommitHeapMemory_count" style="display:none;">0</div>' +
        '<div class="orderUsedHeapMemory_count" style="display:none;">0</div>' +
        '<div class="orderCommitNonHeapMemory_count" style="display:none;">0</div>' +
        '<div class="orderUsedNonHeapMemory_count" style="display:none;">0</div>' +
        '<div class="orderThreadCount_count" style="display:none;">0</div>' +
        '<div class="orderLoadedClassCount_count" style="display:none;">0</div>' +
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
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm)
        + ':' + add0(s);
}

function setOrderKey(obj) {
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;
    if (order == "orderVmName") {
        $(".orderVmName_count").html(parseInt($(".orderVmName_count").html()) + 1);
        orderKey = "vm_name";
        orderValue = parseInt($(".orderVmName_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderVmVersion") {
        $(".orderVmVersion_count").html(parseInt($(".orderVmVersion_count").html()) + 1);
        orderKey = "vm_version";
        orderValue = parseInt($(".orderVmVersion_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderVmVendor") {
        $(".orderVmVendor_count").html(parseInt($(".orderVmVendor_count").html()) + 1);
        orderKey = "vm_vendor";
        orderValue = parseInt($(".orderVmVendor_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderStartTime") {
        $(".orderStartTime_count").html(parseInt($(".orderStartTime_count").html()) + 1);
        orderKey = "start_time";
        orderValue = parseInt($(".orderStartTime_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderMaxHeapMemory") {
        $(".max_heap_memory").html(parseInt($(".orderMaxHeapMemory_count").html()) + 1);
        orderKey = "max_heap_memory";
        orderValue = parseInt($(".orderMaxHeapMemory_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderCommitHeapMemory") {
        $(".orderCommitHeapMemory_count").html(parseInt($(".orderCommitHeapMemory_count").html()) + 1);
        orderKey = "commit_heap_memory";
        orderValue = parseInt($(".orderCommitHeapMemory_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderUsedHeapMemory") {
        $(".orderUsedHeapMemory_count").html(parseInt($(".orderUsedHeapMemory_count").html()) + 1);
        orderKey = "used_heap_memory";
        orderValue = parseInt($(".orderUsedHeapMemory_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderCommitNonHeapMemory") {
        $(".orderCommitNonHeapMemory_count").html(parseInt($(".orderCommitNonHeapMemory_count").html()) + 1);
        orderKey = "commit_non_heap_memory";
        orderValue = parseInt($(".orderCommitNonHeapMemory_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderUsedNonHeapMemory") {
        $(".orderUsedNonHeapMemory_count").html(parseInt($(".orderUsedNonHeapMemory_count").html()) + 1);
        orderKey = "used_non_heap_memory";
        orderValue = parseInt($(".orderUsedNonHeapMemory_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderThreadCount") {
        $(".orderThreadCount_count").html(parseInt($(".orderThreadCount_count").html()) + 1);
        orderKey = "thread_count";
        orderValue = parseInt($(".orderThreadCount_count").html()) % 2;
        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderLoadedClassCount") {
        $(".orderLoadedClassCount_count").html(parseInt($(".orderLoadedClassCount_count").html()) + 1);
        orderKey = "loaded_class_count";
        orderValue = parseInt($(".orderLoadedClassCount_count").html()) % 2;
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
        url: "/TomcatInfo/list/date",
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
        var starttime = json.list[i].startTime;

        var strTime = format(parseInt(starttime));

        $(".table_body_bg").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td>' + json.list[i].vmName + '</td>' +
            '<td>' + json.list[i].vmVersion + '</td>' +
            '<td>' + json.list[i].vmVendor + '</td>' +
            '<td>' + strTime + '</td>' +
            '<td>' + json.list[i].maxHeapMemory + '</td>' +
            '<td>' + json.list[i].commitHeapMemory + '</td>' +
            '<td>' + json.list[i].usedHeapMemory + '</td>' +
            '<td>' + json.list[i].commitNonHeapMemory + '</td>' +
            '<td>' + json.list[i].usedNonHeapMemory + '</td>' +
            '<td>' + json.list[i].threadCount + '</td>' +
            '<td>' + json.list[i].loadedClassCount + '</td>' +
            '<td>' + format(json.list[i].itime) + '</td>' +
            '</tr>'
        )
    }
    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function exportTable() {
    var url = "/TomcatInfo/list/date/ExportExcel?"
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
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    clearBtn: true,
    clearBtn: true,
    todayBtn: true
});

$("#end_time").datetimepicker({
    language: "zh-cn",
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true,
    clearBtn: true,
    clearBtn: true,
    todayBtn: true
});