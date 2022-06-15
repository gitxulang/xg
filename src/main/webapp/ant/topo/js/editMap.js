function save() {
	var topoName = document.getElementById("topo_name").value;
	var checkList = document.getElementsByName("checkbox");
	if (topoName == null || topoName == '') {
		alert("子图名称不能为空！");
		return;
	}
	var flag = 0;
	for ( var i = 0; i < checkList.length; i++) {
		if (checkList[i].checked == true) {
			flag = 1;
			break;
		}
	}
	if (flag == 0) {
		alert("所属区域至少选一个！");
		return;
	}
	var regionIds = "";
	for ( var i = 0; i < checkList.length; i++) {
		if (checkList[i].checked == true) {
			regionIds += checkList[i].value + ",";
		}
	}
	parent.window.saveEditMap(topoName, regionIds);
}

function closeDialog() {
	parent.window.closeDialog();
}

window.onload = function() {
	$("#topo_name").val(parent.document.getElementById("centertitle").innerText)
}


