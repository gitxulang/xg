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
        + height + "px;' >抱歉，您没有此处权限！</div>";
}

var setting = {
    data: {
        key: {
            title: "desc"
        },
        simpleData: {
            enable: true
        }
    },
    view: {
        nodeClasses: getNodeClasses,
        fontCss: getFont
    },
    edit: {
        enable: true,
        showRemoveBtn: setRemoveBtn,
        showRenameBtn: true,
        removeTitle: "删除",
        renameTitle: "编辑"
    },
    callback: {
        beforeRemove: beforeRemove,
        beforeEditName: beforeEditName,
        onRemove: onRemove
    }
};
var key;
var lastValue = "", nodeList = [];

function setRemoveBtn(treeId, treeNode) {
    return !treeNode.isParent;
}

function getFont(treeId, node) {
	return node.font ? node.font : {};
}

function beforeRemove(treeId, treeNode) {
    if (!confirm("确认要删除组织机构:" + treeNode.name + "？")) {
        return false;
    }
    return true;
}

//使用模板添加数据
function beforeEditName(treeId, treeNode) {
	openDialog("systemDepartmentUpdate.html?id="+treeNode.id, "修改组织机构", 800, 500)
}

//使用模板添加数据
function departmentAdd() {
	openDialog("systemDepartmentAdd.html", "添加组织机构", 800, 500)
}

/*function beforeEditName(treeId, treeNode) {
    var url = "systemDepartmentUpdate.html?id=" + treeNode.id;
    jumpPage(url);
//  window.location.href = url;
    return false;
}*/

function onRemove(event, treeId, treeNode) {
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/Department/delete",
        data: {id: treeNode.id},
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 5000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
            	layer.msg('删除成功！', {time: 2000});
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

function getNodeClasses(treeId, treeNode) {
    var classes = $("#styleNodesByCSS").attr('checked') || !(!!treeNode.highlight || !!treeNode.hiddenNodes)
        ? {remove: ['highlight', 'highlight_alt', 'hiddennodes', 'highlight_hiddennodes']}
        : (!!treeNode.highlight
                ? ((!!treeNode.hiddenNodes)
                        ? {add: ['highlight', 'highlight_alt', 'hiddennodes']}
                        : {add: ['highlight', 'highlight_alt']}
                )
                : {add: ['hiddennodes', 'highlight_alt']}
        );
    return classes;
}

$(document).ready(function () {
    key = $("#key");
    query();
});

function searchNode(e) {
    var zTree = $.fn.zTree.getZTreeObj("tree");
    var value = $.trim(key.get(0).value);
    if (key.hasClass("empty")) {
        value = "";
    }
    if (lastValue === value) return;
    lastValue = value;
    updateNodes(false);
    if (value === "") return;
    nodeList = zTree.getNodesByParamFuzzy('name', value);
    updateNodes(true);
}

function updateNodes(highlight, node = null) {
    var zTree = $.fn.zTree.getZTreeObj("tree");
    var expanded = node && node.open;

    // If expanding a node then it MUST be a parent
    // in which case it cannot be hiding matched nodes
    if (expanded) {
        node.hiddenNodes = false;
        zTree.updateNode(node);
    }
    for (var i = 0, l = nodeList.length; i < l; i++) {
        nodeList[i].highlight = highlight;
        nodeList[i].hiddenNodes = false;
        if (highlight) {
            // Make parent nodes of matched nodes show the
            // existence of hidden nodes if the parent is closed.
            var node = nodeList[i];
            while (true) {
                if (!node.parentTId) break;
                var parentNode = zTree.getNodeByTId(node.parentTId);
                if (parentNode.isParent && parentNode.open) break;
                parentNode.hiddenNodes = true;
                zTree.updateNode(parentNode);
                node = parentNode;
            }
        }
        zTree.updateNode(nodeList[i]);
    }
}

function query() {
    $.ajax({
        type: 'POST',
        data: {},
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
            } else {
                $("#tree").html(showNoData(100, 600));
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

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#key').keydown(function(event) {
		if (event.keyCode == 13) {
			searchNode();
		}
	});
});


