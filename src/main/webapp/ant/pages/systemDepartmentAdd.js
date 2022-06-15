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

var setting = {
    view: {
        dblClickExpand: false
    },
    data: {
        key: {
            title: "desc"
        },
        simpleData: {
            enable: true
        }
    },
    callback: {
        onClick: onClick
    }
};

function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("tree"),
        nodes = zTree.getSelectedNodes(),
        v = "",
        id = "";

    nodes.sort(function compare(a, b) {
        return a.id - b.id;
    });
    v = nodes[0].name;
    id = nodes[0].id;
    var parentObj = $("#parent_id");
    var parentIdObj = $("#parentId");
    parentObj.attr("value", v);
    parentIdObj.attr("value", id);
    hideMenu();
}

function showMenu() {
    var parentObj = $("#parent_id");
    var parentOffset = $("#parent_id").offset();
    $("#menuContent").css({
        left: parentOffset.left + "px",
        top: parentOffset.top + parentObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}

function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}

$(document).ready(function () {
    loadDepartment();
})

function loadDepartment() {
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Department/tree",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                $.fn.zTree.init($("#tree"), setting, json);
                var zTree = $.fn.zTree.getZTreeObj("tree");
                zTree.expandAll(true);
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

function addDepartment() {
    var dname = $("#d_name").val().trim();
    var ddesc = $("#d_desc").val();
    var parentId = $("#parentId").val();

    if (dname == "" || dname == undefined) {
    	layer.msg('请填写组织机构名称！', {time: 2000},function(){
			return;
		});
    }else{
    	var dataJson = {};
        dataJson["DName"] = dname;
        dataJson["DDesc"] = ddesc;

        if (parentId != "" && parentId != -1) {
            dataJson["ParentId"] = parentId;
        }

        $.ajax({
            type: 'POST',
            dataType: "json",
            url: "/Department/add",
            data: dataJson,
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            timeout: 5000,
            cache: true,
            async: true,
            success: function (json) {
                if (json != null) {
                    if (json.state == 0) {
                    	layer.msg(json.info, {time: 2000},function(){
        					parent.location.reload(true);
        				});
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
    
}
var closeDia = function() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}