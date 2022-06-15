function save() {
	let areaName = document.getElementById("area_name").value;
	if (!areaName) {
        layer.msg("域名称不能为空！", {time: 500})
	} else {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/Topo/addTopoArea",
            data: {
                divName: String(areaName),
                mapId: $("#select_topo",parent.parent.document).val()
            },
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            timeout: 5000,
            cache: true,
            async: true,
            success: function(json){
                layer.msg(json.info, {time: 1000}, function(){
                    if(json.info == "添加区域成功!"){
                        parent.window.location.reload()
                        closeDialog()                        
                    }
                })
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", getCookie("token"))
            },
            error : function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
            },
            complete : function(xhr) {
                if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
                    var win = window;
                    while (win != win.top) {
                        win = win.top;
                    }
                    win.location.href = xhr.getResponseHeader("CONTENTPATH");
                }
            }
        })
    }
}

function closeDialog() {
	parent.window.closeDialog();
}

window.onload = function() {
	$("#topo_name").val(parent.parent.document.getElementById("centertitle").innerText)
}


