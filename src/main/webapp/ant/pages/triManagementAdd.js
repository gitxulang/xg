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
	// loadTable();
	// ipBlur();
})

function loadTable(){
	// loadDepartment();
	// loadAssetType();
	// loadLastAsset();
}

function managementAdd(){
	var aNo = $("#aNo").val();
	var manageName = $("#manageName").val();
	var manageUrl = $("#manageUrl").val();
	
	if (aNo == "") {
		alert("ASPID为必填项！");
		return;
	}

	if (manageName == "") {
		alert("管理端名称为必填项！");
		return;
	}

	if (manageUrl == "") {
		alert("管理端地址为必填项！");
		return;
	}
	
	var dataJson = {};
	dataJson["ANo"] = aNo;
	dataJson["manageName"] = manageName;
	dataJson["manageUrl"] = manageUrl;
	
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/triManagement/addManagement",
		data: dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			// alert(json);
			// closeDialog();
			layer.msg(json.info, {time: 1000},function(){
				if(json.info == '添加成功!') {
					window.parent.location.reload();
				}
			});			
		},
		
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
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
	});
}

var closeDialog = function() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}

$("#aNo").blur(function(){
	var aNo = $("#aNo").val();

	if (aNo == null || aNo == "") {
		layer.msg("ASP卡ID不能为空");
		return;
	}

	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/triManagement/findByAspId",
		data: {
			aNo: aNo
		},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json.ano == null || json.ano == "") {
				$("#aType").empty();
				$("#aName").attr({"value" : ""});
				layer.msg("ASP卡ID无效，请重新输入！");
			} else {
				$("#aType").append('<option value="' + json.nmsAssetType.id + '" selected>' + json.nmsAssetType.chType + '/' + json.nmsAssetType.chSubtype + '</option>');
				$("#aName").attr({"value" : json.aname});
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
});