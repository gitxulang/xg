function save() {
	var containerId = document.getElementById("n_container").value;
	console.log("nodeId="+parent.bindNodeId.split('_')[1]);
	parent.window.updateNodeBindings(parent.bindNodeId.split('_')[1], containerId);
}

function closeDialog() {
	parent.window.closeDialog();
}

var getContainers = function(containerId, mapId){
	$.ajax({
		type : "POST",
		data : {
			mapId: mapId
		},
		dataType : "json",
		url : "/Topo/topoAreas",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		cache : true,
		async : false,
		success : function(data) {
			var json = data.list;
			for (var i in json) {
				if (json[i].id == containerId) {
					$("#n_container").append(
							'<option value="' + json[i].id + '" selected>'
									+ json[i].divName
									+ '</option>');
				} else {
					$("#n_container").append(
							'<option value="' + json[i].id + '">'
									+ json[i].divName
									+ '</option>');
				}
			}
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader("Authorization", getCookie("token"));
		},
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Topo/topoAreas: " + textStatus);
		},
		complete: function(xhr) {
			if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) {
				var win = window;
				while (win != win.top) {
					win = win.top;
				}
				win.location.href = xhr.getResponseHeader("CONTENTPATH");
			} else if ("FORBIDDEN" == xhr.getResponseHeader("REDIRECT")) {
            	console.log("[DEBUG] /Topo/topoAreas: no function");
            	$("body").html(showNoFunction(100, 600));
			}
		}
	});
} 
window.onload = function() {
	getContainers(parent.containerId, parent.mapId);
}