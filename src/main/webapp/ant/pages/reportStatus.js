
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

$(document).ready(function(){
    loadTable();
    $("body").append(
        '<div class="current_orderkey" style="display:none;"></div>'+
        '<div class="current_ordervalue" style="display:none;"></div>'+
        '<div class="orderName_count" style="display:none;">0</div>'+
        '<div class="orderIp_count" style="display:none;">0</div>'
    );
})

function loadTable(){
    var data_json={};
    data_json["begin"]=1;
    data_json["offset"]=10;
    loadAssetType();
    loadDepartment();
    query(data_json);
}

function selectPage(){
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
    let request_data={};
    request_data["begin"]=begin;
    request_data["offset"]=offset;
    query(request_data);
}

function changeOffset(){
    const begin = 1;
    const offset = $("#selectPage").val();
    let request_data={};
    request_data["begin"]=begin;
    request_data["offset"]=offset;
    query(request_data);
}

function firstPage(){
    if($("#page").html()!="1"){
        const begin = 1;
        const offset = $("#selectPage").val();
        let request_data={};
        request_data["begin"]=begin;
        request_data["offset"]=offset;
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

function setOrderKey(obj){
    var order = $(obj).attr("id");
    var orderKey = "";
    var orderValue = 0;

    if(order=="orderName"){
        $(".orderName_count").html(parseInt($(".orderName_count").html())+1);
        orderKey = "a_name";
        orderValue = parseInt($(".orderName_count").html())%2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }else if(order=="orderIp"){
        $(".orderIp_count").html(parseInt($(".orderIp_count").html())+1);
        orderKey = "a_ip";
        orderValue = parseInt($(".orderIp_count").html())%2;

        $(".current_orderkey").html(orderKey);
        $(".current_ordervalue").html(orderValue);
    }

    var begin = $("#page").html();
    var offset = $("#selectPage").val();
    var request_data={};
    request_data["begin"]=begin;
    request_data["offset"]=offset;

    if(orderKey!=""){
        request_data["orderKey"]=orderKey;
        request_data["orderValue"]=orderValue;
    }

    query(request_data, begin, offset);
}

/* 点击按钮，下拉菜单在 显示/隐藏 之间切换 */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

var reset = function(){
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
}

function loadAssetType(){
    $.ajax({
        type : 'POST',
        dataType : "json",
        url : "/assetType/all",
        contentType : 'application/x-www-form-urlencoded;charset=utf-8',
        timeout : 5000,
        cache : true,
        async : true,
        success : function(json) {
            for(var i in json){
                $("#a_type").append(
                    '<option value="'+json[i].id+'">'+json[i].chType+'/'+json[i].chSubtype+'</option>'
                )
            }
        },
        beforeSend: function(xhr) {
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

function loadDepartment(){

    $.ajax({
        type : 'POST',
        dataType : "json",
        url : "/Department/all",
        contentType : 'application/x-www-form-urlencoded;charset=utf-8',
        timeout : 5000,
        cache : true,
        async : true,
        success : function(json) {
            for(var i in json){
                $("#a_dept").append(
                    '<option value="'+json[i].id+'">'+json[i].dname+'</option>'
                )
            }
        },
        beforeSend: function(xhr) {
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

function query(data_json){
    // 每次查询均获取current_orderkey和current_ordervalue值
    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();
    data_json["orderKey"] = orderKey;
    data_json["orderValue"] = orderValue;

    var name = $("#name").val();
    var ip = $("#ip").val();
    var type = $("#a_type").val();
    var dept = $("#a_dept").val();
    if(name!=""){
        data_json["AName"]=name;
    }
    if(ip!=""){
        data_json["AIp"]=ip;
    }
    if(type!="-1"){
        data_json["nmsAssetType"]=type;
    }
    if(dept!="-1"){
        data_json["ADept"]=dept;
    }

    $.ajax({
        type : 'POST',
        dataType : "json",
        url : "/performance/list/page/condition/realtime",
        data: data_json,
        contentType : 'application/x-www-form-urlencoded;charset=utf-8',
        timeout : 5000,
        cache : true,
        async : true,
        success : function(json) {
            if (json != null) {
            	loadList(json);
            }
        },
        beforeSend: function(xhr) {
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
    return text.replace(/[<>"&]/g, function(match, pos, originalText) {
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

function loadList(json){
    $("#tableBodyID").empty();
    var begin = parseInt(json.page);
    var offset = parseInt($("#selectPage").val());
    for(var i in json.list){
        var n= (begin-1)*offset + parseInt(i)+1;

        var pingRate = Number(json.list[i].online) == 1 ? parseInt(Number(json.list[i].pingRate)) * 100 : '--';
        let cpuRate = Number(json.list[i].online) == 1 ? Number(Number(json.list[i].cpuRate).toFixed(2)) : '--'
        let memTotal = Number(json.list[i].online) == 1 ? (Number(json.list[i].memTotal) / (1024 * 1024)).toFixed(2) : '--'
        let memRate = Number(json.list[i].online) == 1 ? Number((Number(json.list[i].memRate) * 100).toFixed(2)) : '--'
        let swapTotal = Number(json.list[i].online) == 1 ? ((Number(json.list[i].swapTotal) / (1024 * 1024)).toFixed(2)) : '--'
        let swapRate = Number(json.list[i].online) == 1 ? Number((Number(json.list[i].swapRate) * 100).toFixed(2)) : '--'
        let netInSpeed = Number(json.list[i].online) == 1 ? Number(Number(json.list[i].netInSpeed)) : '--'
        let netOutSpeed = Number(json.list[i].online) == 1 ? Number(Number(json.list[i].netOutSpeed)) : '--'
        let online = Number(json.list[i].online) == 1 ? '在线' : '离线'
        let textColor = Number(json.list[i].online) == 1 ? 'green' : 'red'

        $("#tableBodyID").append(
            '<tr class="odd gradeX">'+
            '<td class="center">'+n+'</td>'+
            '<td>'+htmlEscape(json.list[i].aip)+'</td>'+
            '<td>'+htmlEscape(json.list[i].aname)+'</td>'+
            '<td>'+json.list[i].chType+'/'+json.list[i].subChType+'</td>'+
            '<td style="color:'+textColor+'";>'+online+'</td>'+
            '<td>'+pingRate+'</td>'+
            '<td>'+cpuRate+'</td>'+
            '<td>'+memTotal+'</td>'+
            '<td>'+memRate+'</td>'+
            '<td>'+swapTotal+'</td>'+
            '<td>'+swapRate+'</td>'+
            '<td>'+netInSpeed+'</td>'+
            '<td>'+netOutSpeed+'</td>'+
            // '<td>'+json.list[i].cpuRate.toFixed(2)+'</td>'+
            // '<td>'+(json.list[i].memTotal/(1024*1024)).toFixed(2)+'</td>'+
            // '<td>'+(json.list[i].memRate*100).toFixed(2)+'</td>'+
            // '<td>'+(json.list[i].swapTotal/(1024*1024)).toFixed(2)+'</td>'+
            // '<td>'+(json.list[i].swapRate*100).toFixed(2)+'</td>'+
            // '<td>'+json.list[i].netInSpeed+'</td>'+
            // '<td>'+json.list[i].netOutSpeed+'</td>'+
            '</tr>'
        )
    }

    $("#page").html(json.page);
    $("#totalPage").html(json.totalPage);
    $("#totalCount").html(json.totalCount);
}

function exportTable(){
    var url = "/performance/list/page/condition/realtime/ExportExcel?"
    var name = $("#name").val();
    var ip = $("#ip").val();
    var type = $("#a_type").val();
    var dept = $("#a_dept").val();

    var orderKey = $(".current_orderkey").html();
    var orderValue = $(".current_ordervalue").html();

    if(name!='' && name != undefined && name != null){
        url+="AName="+name+"&";
    }

    if(ip!='' && ip != undefined && ip != null){
        url+="AIp="+ip+"&";
    }

    if(type!="-1" && type != undefined && type != null){
        url+="nmsAssetType="+type+"&";
    }

    if(dept!="-1" && dept != undefined && dept != null){
        url+="ADept="+dept+"&";
    }

    if(orderKey!="0" && orderKey != undefined && orderKey != null){
        url+="orderKey="+orderKey+"&";
    }

    if(orderValue!="0" && orderValue != undefined && orderValue != null){
        url+="orderValue="+orderValue+"&";
    }

    window.location.href = url;
}

$(function(){
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#name').keydown(function(event) {
        if (event.keyCode == 13) {
            selectPage();
        }
    });
});

$(function(){
    var event = arguments.callee.caller.arguments[0] || window.event;
    $('#ip').keydown(function(event) {
        if (event.keyCode == 13) {
            selectPage();
        }
    });
});

