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

$(document).ready(function () {
    $("body").append(
        '<div class="current_orderkey" style="display:none;"></div>' +
        '<div class="current_ordervalue" style="display:none;"></div>' +
        '<div class="orderName_count" style="display:none;">0</div>' +
        '<div class="orderCard_count" style="display:none;">0</div>' +
        '<div class="orderSex_count" style="display:none;">0</div>' +
        '<div class="orderBirthDay_count" style="display:none;">0</div>' +
        '<div class="orderDept_count" style="display:none;">0</div>' +
        '<div class="orderEducation_count" style="display:none;">0</div>' +
        '<div class="orderNationality_count" style="display:none;">0</div>' +
        '<div class="orderStatus_count" style="display:none;">0</div>' +
        '<div class="orderCreateTime_count" style="display:none;">0</div>' +
        '<div class="orderItime_count" style="display:none;">0</div>'
    );
    loadTable();
})

function loadTable() {
    var dataJson = {};
    dataJson["begin"] = 1;
    dataJson["offset"] = 10;
    query(dataJson);
}

function selectPage() {
    var begin = 1;
    var offset = $("#selectPage").val();
    var requestData = {};
    requestData["begin"] = begin;
    requestData["offset"] = offset;
    query(requestData);
}

function changeOffset() {
    var begin = 1;
    var offset = $("#selectPage").val();
    var requestData = {};
    requestData["begin"] = begin;
    requestData["offset"] = offset;
    query(requestData);
}

function firstPage() {
    if ($("#page").html() != "1") {
        var begin = 1;
        var offset = $("#selectPage").val();
        var requestData = {};
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

function setOrderKey(obj) {
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;
    if (order == "orderName") {
        $(".orderName_count").html(parseInt($(".orderName_count").html()) + 1);
        orderKey = "name";
        orderValue = parseInt($(".orderName_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderSex") {
        $(".orderSex_count").html(parseInt($(".orderSex_count").html()) + 1);
        orderKey = "sex";
        orderValue = parseInt($(".orderSex_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderCard") {
        $(".orderCard_count").html(parseInt($(".orderCard_count").html()) + 1);
        orderKey = "card";
        orderValue = parseInt($(".orderCard_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderBirthDate") {
        $(".orderBirthDate_count").html(parseInt($(".orderBirthDate_count").html()) + 1);
        orderKey = "birthDate";
        orderValue = parseInt($(".orderBirthDate_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderDept") {
        $(".orderDept_count").html(parseInt($(".orderDept_count").html()) + 1);
        orderKey = "nmsDepartment.id";
        orderValue = parseInt($(".orderDept_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderEducation") {
        $(".orderEducation_count").html(parseInt($(".orderEducation_count").html()) + 1);
        orderKey = "education";
        orderValue = parseInt($(".orderEducation_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderNationality") {
        $(".orderNationality_count").html(parseInt($(".orderNationality_count").html()) + 1);
        orderKey = "nationality";
        orderValue = parseInt($(".orderNationality_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderStatus") {
        $(".orderStatus_count").html(parseInt($(".orderStatus_count").html()) + 1);
        orderKey = "deled";
        orderValue = parseInt($(".orderStatus_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderCreateTime") {
        $(".orderCreateTime_count").html(parseInt($(".orderCreateTime_count").html()) + 1);
        orderKey = "createTime";
        orderValue = parseInt($(".orderCreateTime_count").html()) % 2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    } else if (order == "orderItime") {
        $(".orderItime_count").html(parseInt($(".orderItime_count").html()) + 1);
        orderKey = "itime";
        orderValue = parseInt($(".orderItime_count").html()) % 2;

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

    query(requestData);
}

function query(dataJson) {
    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();
    dataJson["orderKey"] = orderKey;
    dataJson["orderValue"] = orderValue;

    var name = $("#name").val();
    var card = $("#card").val();
    if (name != null && name != "") {
        dataJson["name"] = name;
    }
    if (card != null && card != "") {
        dataJson["card"] = card;
    }

    $.ajax({
        type: 'POST',
        data: dataJson,
        dataType: "json",
        url: "/User/list/page/condition",
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

function add0(m) {
    return m < 10 ? '0' + m : m;
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
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());

    for (var i in json.list) {
        var n = parseInt(i) + 1 + (begin - 1) * offset;
        var deled = (json.list[i].deled == "0") ? "正常" : "已删除";
        var action = '<a href="#" onclick="doUpdate(' + json.list[i].id + ')"><i class="my-material-icons">编辑&nbsp;</i></a>';
        action += '<a href="#" onclick="doDelete(' + json.list[i].id + ')"><i class="my-material-icons">删除</i></a>';

        $("#tableBodyID").append(
            '<tr class="odd gradeX">'
            + '<td align="center">' + n + '</td>'
            + '<td>' + htmlEscape(json.list[i].name) + '</td>'
            + '<td>' + htmlEscape(json.list[i].card) + '</td>'
            + '<td>' + htmlEscape(json.list[i].sex) + '</td>'
            + '<td>' + htmlEscape(json.list[i].birthDate) + '</td>'
            + '<td>' + htmlEscape(json.list[i].nmsDepartment.dname) + '</td>'
            + '<td>' + htmlEscape(json.list[i].education) + '</td>'
            + '<td>' + htmlEscape(json.list[i].nationality) + '</td>'
            + '<td>' + htmlEscape(deled) + '</td>'
            + '<td>' + format(json.list[i].createtime) + '</td>'
            + '<td>' + format(json.list[i].itime) + '</td>'
            + '<td class="center">' + action + '</td>'
            + '</tr>'
        )
        $('.my-material-icons').css('color', '#22B37A')
    }

    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function doAdd() {
    var url = "commonUserAdd.html";
    jumpPage(url);
//  window.location.href = url;
}

function doUpdate(id) {
    var url = "commonUserUpdate.html?id=" + id;
    jumpPage(url);
//  window.location.href = url;
}

function doDelete(id) {
    if (!confirm("确认要删除？")) {
        return;
    }
    var dataJson = {
        id: id
    };
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/User/deleteUser",
        data: dataJson,
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                alert("删除成功！");
                jumpPage("commonUser.html");
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
    $('#card').keydown(function (event) {
        if (event.keyCode == 13) {
            selectPage();
        }
    });
});


var reset = function(){
	$("#name").val('');
    $("#card").val('');
	var dropdowns = document.getElementsByClassName("drop1down-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
    }
    loadTable()
}
/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}
