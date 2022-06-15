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
	var request = GetSelfRequest();
	var id = request.id;
	$("#ruleId").val(id);
	loadRule(id);
})

function loadRule(id){
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Rule/findNmsRuleById",
		data: {"id":id},
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success : function(json) {
			if (json == null) {
				$("body").html(showNoFunction(100, 600));
			} else {
				$("#content").attr("value",json.rContent);
				$("#enabled").attr("value",json.rEnable);
				$("#unit").attr("value",json.rUnit);
				$.each($('#seq option'), function(i, n) {
				    if ($(n).val() == json.rSeq) {
				        $(n).attr("selected", true);
				    }
				});
				$("#r_value1").attr("value",json.rValue1);
				$("#r_value2").attr("value",json.rValue2);
				$("#r_value3").attr("value",json.rValue3);
				$("#a_type").attr("value",json.chtype+' / '+json.chSubType);
			}
		},
		
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Rule/findNmsRuleById: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Rule/findNmsRuleById: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}


function updateRule() {
	var dataJson = {};
	
	dataJson["id"] = $("#ruleId").val();
	dataJson["content"] = $("#content").val();
	dataJson["aType"] = $("#a_type").val();
	dataJson["rEnable"] = $("#enabled").val();
	dataJson["rSeq"] = $("#seq").val();
	dataJson["rValue1"] = $("#r_value1").val();
	dataJson["rValue2"] = $("#r_value2").val();
	dataJson["rValue3"] = $("#r_value3").val();

	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Rule/updateNmsRuleById",
		data: dataJson,
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				if (json) {	
					layer.msg("修改成功", {time: 2000},function(){
    					parent.location.reload(true);
    				});
				} else {
					alert("修改失败！");
				}
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Rule/findNmsRuleById: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Rule/findNmsRuleById: no function");
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}
var closeDialog = function() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}
