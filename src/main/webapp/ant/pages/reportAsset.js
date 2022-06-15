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
        title: '设备详情报表',
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
        '<div class="orderBmIp_count" style="display:none;">0</div>' +
        '<div class="orderYwIp_count" style="display:none;">0</div>' +
        '<div class="orderType_count" style="display:none;">0</div>' +
        '<div class="orderPos_count" style="display:none;">0</div>' +
        '<div class="orderNumber_count" style="display:none;">0</div>' +
        '<div class="orderDep_count" style="display:none;">0</div>' +
        '<div class="orderColl_count" style="display:none;">0</div>' +
        '<div class="orderMonitor_count" style="display:none;">0</div>' +
        '<div class="orderTime_count" style="display:none;">0</div>'
    );
    $('.table-condensed').css("background-color","#d9edf7");
    loadTable();
	jeDate("#itime_start",{
		theme:{bgcolor:"#00A1CB",pnColor:"#00CCFF"},
        format: "YYYY-MM-DD hh:mm:ss"
    });
	
	jeDate("#itime_end",{
		theme:{bgcolor:"#00A1CB",pnColor:"#00CCFF"},
        format: "YYYY-MM-DD hh:mm:ss"
    });
})

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

var reset = function(){
	$('#itime_start').val("");
	$('#itime_end').val("");
	$("#name").val('');
	$("#ip").val('');
	$("#a_type").val('-1');
	$("#a_dept").val('-1');
	
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
    }
    loadTable()
    selectPage();
}

function loadTable() {
    var dataJson = {};
    dataJson["begin"] = 1;
    dataJson["offset"] = 10;
    loadAssetType();
    loadDepartment();
    query(dataJson);
}

function selectPage() {
	var dropdowns = document.getElementsByClassName("drop1down-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
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
    } else if (order == "orderBmIp") {
        $(".orderBmIp_count").html(parseInt($(".orderBmIp_count").html()) + 1);
        orderKey = "BmIp";
        orderValue = parseInt($(".orderBmIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderYwIp") {
        $(".orderYwIp_count").html(parseInt($(".orderYwIp_count").html()) + 1);
        orderKey = "YwIp";
        orderValue = parseInt($(".orderYwIp_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }else if (order == "orderType") {
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
    } else if (order == "orderNumber") {
        $(".orderNumber_count").html(parseInt($(".orderNumber_count").html()) + 1);
        orderKey = "ANo";
        orderValue = parseInt($(".orderNumber_count").html()) % 2;

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
        url: "/Asset/list/date/reportSelect",
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
	if(text==null){
		return "";
	}
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
            collMode = "SNMPv1&v2c协议";
        } else if (json.list[i].collMode == "3") {
            collMode = "SNMPv3c协议";
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
            '<td>' + htmlEscape(json.list[i].ano) + '</td>' +
            '<td>' + json.list[i].aip + '</td>' +
           /* '<td>' + htmlEscape(json.list[i].bmIp) + '</td>' +
            '<td>' + htmlEscape(json.list[i].ywIp) + '</td>' +*/
            '<td>' + htmlEscape(json.list[i].aname) + '</td>' +
            '<td>' + json.list[i].nmsAssetType.chType + '/' + json.list[i].nmsAssetType.chSubtype + '</td>' +
            '<td>' + department + '</td>' +
            '<td>' + htmlEscape(json.list[i].apos) + '</td>' +
            '<td>' + collMode + '</td>' +
            '<td>' + colled + '</td>' +
            '<td>' + date + '</td>' +
            '<td class="center"><a href="#" onclick="showDetail(' + json.list[i].id + ')"><i class="my-material-icons">查看</i></a></td>' +
            '</tr>'
        )
        $('.my-material-icons').css('color', '#22B37A')
    }

    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function showDetail(id, typeid) {
    if (id == null || id == "") {
        alert("您查询的设备ID为空！");
        return;
    }
//  window.location.href = "pingInfo.html?id=" + id;
    jumpPage("pingInfo.html?id=" + id);
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
                if (json[i].nodeTag == "net") {
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
    var url = "/Asset/list/date/reportSelect/exportExcel?"
    var name = $("#name").val();
    var ip = $("#ip").val();
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


