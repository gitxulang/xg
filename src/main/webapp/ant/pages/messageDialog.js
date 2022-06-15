

$(document).ready(function(){
	getAlarm();
})


var closeDialog = function() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}
function getAlarm() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		data : {
			id : "1"
		},		
		url : "/AuditConfig/alarm",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : false,
		success : function(json) {
            if (json != null) {
				if (json.state == 1) {
					$("#alarm").html(json.info);
				} else {
					$("#alarm").html("没有告警日志");
				}
            }
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /AuditConfig/alarm: " + textStatus);
		},
		complete: function(xhr) {
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