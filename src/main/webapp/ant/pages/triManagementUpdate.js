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

$(document).ready(function() {
	var request = GetSelfRequest();
	var id = request.id;
	loadTable(id);
})

function triManagementUpdate() {
	var id = $("#device_id").html() + "";
	var manageName = $("#manageName").val();
	var manageUrl = $("#manageUrl").val();

	if (manageName == null || manageName == "") {
		alert("管理端名称为必选项");
		return;
	}
	if (manageUrl == null || manageUrl == "") {
		alert("管理端地址为必填项！");
		return;		
	}

	var data_json = {};
	data_json["id"] = id;
	data_json["manageName"] = manageName;
	data_json["manageUrl"] = manageUrl;

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/triManagement/updateTriManageMent",
		data : data_json,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			layer.msg(json.info, {time: 1000},function(){
				if(json.info == '修改成功！') {
					window.parent.location.reload();
				}
			});	
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

function loadTable(id) {
	$("#device_id").html(id);
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/triManagement/findByIdToUpdate",
		data: {
			id: id
		},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("body").html(showNoFunction(100, 600))
			} else {
				$("#a_type").append('<option value="' + json.nmsAsset.nmsAssetType.id + '" selected>' + json.nmsAsset.nmsAssetType.chType + '/' + json.nmsAsset.nmsAssetType.chSubtype + '</option>');
				$("#a_no").attr({"value" : json.nmsAsset.ano});
				$("#a_name").attr({"value" : json.nmsAsset.aname});
				$("#manageName").attr({"value" : json.manageName});
				$("#manageUrl").attr({"value" : json.manageUrl});
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


