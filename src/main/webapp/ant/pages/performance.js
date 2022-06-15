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
        '<div class="orderIp_count" style="display:none;">0</div>'
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
    var type = $("#a_type").val();
    if (name != "") {
        dataJson["AName"] = name;
    }

    if (ip != "") {
        dataJson["AIp"] = ip;
    }

    if (type != "-1") {
        dataJson["nmsAssetType"] = type;
    }

    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/performance/list/page/condition",
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

function NumberFloat(val) {
    if (parseFloat(val).toString() == "NaN") {
        return false;
    } else {
        return true;
    }
}

function NumberInt(val) {
    if (parseInt(val).toString() == "NaN") {
        return false;
    } else {
        return true;
    }
}

function htmlEscape(text) {
	if (text == null) {
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

    var n = 1;
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());

    for (i in json.list) {
        n = parseInt((begin - 1) * offset) + 1 + parseInt(i);

        var pingRate = Number(json.list[i].online) == 1 ? parseInt(json.list[i].pingRate) * 100 : '/';
        var cpuRate = Number(json.list[i].online) == 1 ? json.list[i].cpuRate.toFixed(2) : '/';
        var memRate = Number(json.list[i].online) == 1 ? ((1 - (json.list[i].memFree / json.list[i].memTotal)) * 100).toFixed(2) : '/';
        var swapRate = Number(json.list[i].online) == 1 ? ((1 - (json.list[i].swapFree / json.list[i].swapTotal)) * 100).toFixed(2) : '/';
        var ifNum = Number(json.list[i].online) == 1 ? json.list[i].netNum : '/';
        let online = Number(json.list[i].online) == 1 ? '在线' : '离线'

        if (NumberInt(pingRate) == false || pingRate==-1 ) {
            pingRate = '--';
        }

        if (NumberFloat(cpuRate) == false || cpuRate==-1) {
            cpuRate = '--';
        }

        if (NumberFloat(memRate) == false || memRate==-1) {
            memRate = '--';
        }

        if (NumberFloat(swapRate) == false || swapRate==-1) {
            swapRate = '--';
        }

        if (NumberInt(ifNum) == false || ifNum==-1) {
            ifNum = '--';
        }

        var strHtml = '<tr class="odd gradeX" status="'+ Number(json.list[i].online) +'"><td class="center">' + n + '</td>';
        if (json.list[i].ifAlarm) {
            strHtml += '<td><font color="red">' + htmlEscape(json.list[i].aname) + '</font></td>'
        } else {
            strHtml += '<td><font color="green">' + htmlEscape(json.list[i].aname) + '</font></td>'
        }
        strHtml += '<td>' + json.list[i].chType + '/' + json.list[i].subChType + '</td>';
        strHtml += '<td>' + htmlEscape(json.list[i].aip) + '</td>';
        Number(json.list[i].online) == 1
            ? strHtml += '<td style="color:green;">' + htmlEscape(online) + '</td>'
            : strHtml += '<td style="color:red;">' + htmlEscape(online) + '</td>';        
        strHtml += '<td>' + pingRate + '</td>';
        strHtml += '<td>' + cpuRate + '</td>';
        strHtml += '<td>' + memRate + '</td>';
        strHtml += '<td>' + swapRate + '</td>';
        strHtml += '<td>' + ifNum + '</td>';
        strHtml += '<td class="center"><a href="#" onclick="showDetail(' + json.list[i].id + ', ' + json.list[i].typeId + ')"><i class="my-material-icons" style="color:#22B37A;">查看</i></a></td>';

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
                if (json[i].nodeTag == "net") {
                    $("#a_type").append(
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
        alert("您查询的设备ID为空！");
        return;
    }
    if (typeid == null && typeid < 0) {
        alert("您查询的设备typeid为空！");
        return;
    }
    
    // var url = "performanceDetail.html?id=" + id + "&typeid=" + typeid;
    let search = '?id=' + id + '&typeid=' + typeid
    let urls = {
        'detail': 'performanceDetail.html',
        'interface': 'performanceInterface.html',
        'soft': 'performanceSoft.html',
        'account': 'performanceAccount.html',
        'alarm': 'performanceAlarm.html',
        'process': 'performanceProcess.html',
        'app': 'performanceApp.html',
        'middleware': 'performanceMiddleware.html',
        'database': 'performanceDatabase.html'
    }
    const WIDTH = 1400, HEIGHT = 600
     let offset = [200, 400]
//    let offset = 'auto'
    
    if(typeid == 22) { // 安全数据库
        parent.$('.performw').remove()
        openDialogPerform(urls.database + search, '安全数据库', WIDTH, HEIGHT, offset)
        openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, offset, 'performw1')
        parent.$('.performw1').hide()
    } else if(typeid == 23) { // 安全中间件
        parent.$('.performw').remove()
        openDialogPerform(urls.middleware + search, '安全中间件', WIDTH, HEIGHT, offset)
        openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, offset, 'performw1')
        parent.$('.performw1').hide()
    } else {
        // 其他设备
        if(typeid == 0) {
            parent.$('.performw').remove()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, offset)
        } else if(typeid >= 1 && typeid <= 4) { // 专用终端、专用工作站
            parent.$('.performw').hide()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, offset)
            openDialogPerform(urls.interface + search, '接口信息', WIDTH, HEIGHT, offset, 'performw1')
            openDialogPerform(urls.soft + search, '软件信息', WIDTH, HEIGHT, offset, 'performw2')
           // openDialogPerform(urls.account + search, '账号信息', WIDTH, HEIGHT, offset, 'performw3')
            openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, offset, 'performw3')

            parent.$('.performw1').hide()
            parent.$('.performw2').hide()
            parent.$('.performw3').hide()
            //parent.$('.performw4').hide()
        } else if(typeid >= 5 && typeid <= 10) { // 专用服务器、管理服务器、通用服务器
            $('.performw').remove()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, offset)
            openDialogPerform(urls.interface + search, '接口信息', WIDTH, HEIGHT, offset, 'performw1')
            openDialogPerform(urls.process + search, '进程信息', WIDTH, HEIGHT, offset, 'performw2')
            openDialogPerform(urls.soft + search, '软件信息', WIDTH, HEIGHT, offset, 'performw3')
            //openDialogPerform(urls.app + search, '应用信息', WIDTH, HEIGHT, offset, 'performw4')
            openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, offset, 'performw4')

            parent.$('.performw1').hide()
            parent.$('.performw2').hide()
            parent.$('.performw3').hide()
           // parent.$('.performw4').hide()
            parent.$('.performw4').hide()
        } else if(typeid >= 11 && typeid <= 21) {
            parent.$('.performw').remove()
            openDialogPerform(urls.detail + search, '基础信息', WIDTH, HEIGHT, offset)
            openDialogPerform(urls.interface + search, '接口信息', WIDTH, HEIGHT, offset, 'performw1')
            openDialogPerform(urls.alarm + search, '告警信息', WIDTH, HEIGHT, offset, 'performw2')

            parent.$('.performw1').hide()
            parent.$('.performw2').hide()
        }
    }
    // jumpPage(url);
    // openDialogPerform(url, "设备状态列表", )
    // openDialogPerform("performanceInterface.html?id=" + id + "&typeid=" + typeid, "测试", 1050, 500, ['100px', '50px'])
//  window.location.href = "performanceDetail.html?id=" + id + "&typeid=" + typeid;
}

function getIframeOffset(x = 0, y = 0) {
    let offset = [100, 50]
    return [offset[0] + x + 'px', offset[1] + y + 'px']
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

function changeType() {
    queryData();
}
var reset = function(){
	$("#name").val('');
	$("#ip").val('');
	$("#a_type").val('-1');
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
    }
    queryData()
}

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}
