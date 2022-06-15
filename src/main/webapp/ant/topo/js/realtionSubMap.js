window.onload = function(){
	createList();
}

function UrlSearch(key) {
	var name, value;
	var str = location.href;
	var num = str.indexOf("?")
	str = str.substr(num + 1);
	var arr = str.split("&");
	for(var i = 0; i < arr.length; i++){
		num = arr[i].indexOf("=");
		if(num > 0){
			name = arr[i].substring(0, num);
			value = arr[i].substring(num + 1);
			
			if (name == key) {
				return value;
			}
		}
	}
	return null;
}

function save() {
	var current_topo_map = document.getElementById("current_topo_map").value;
	var node_id = document.getElementById("node_id").value;
	var checkList = document.getElementsByName("radio");
	var new_relation_map = '';
	var flag = 0;
	for (var i = 0; i < checkList.length; i++) {
		if (checkList[i].checked) {
			new_relation_map = checkList[i].value;
			flag = 1;
		}
	}
	if (flag == 0) {
		alert("请选择关联的拓扑图！");
		return;
	}
	var submapName = null;
	var radioArray = document.getElementsByName("radio");
	for (var i = 0; i < radioArray.length; i++) {
		if (radioArray[i].checked) {
			submapName = radioArray[i].getAttribute("data-name");
		}
	}
	parent.window.saveRelationSubMap(parent.relNodeId, submapName);
	closeDialog();			 
}

function closeDialog() {
	parent.window.closeDialog();
}

function createList(){
	var node_id = UrlSearch("node_id");
	var relation_map = decodeURI(UrlSearch("relation_map"));
	var listData = parent.postSelectData();
	var tableHTML = "";
	for (var i = 0; i < listData.length; i++){
		if (listData[i].tname == relation_map) {
			tableHTML += `
			<tr height="40">
				<td align="center" width="50%">
					<input type="radio" name="radio" checked value=${listData[i].id} data-name=${listData[i].tname}>
				</td>    	 	
				<td align="left">${listData[i].tname}</td>			
			</tr>`;			
		} else {
			tableHTML += `
			<tr height="40">
				<td align="center" width="50%">
					<input type="radio" name="radio" value=${listData[i].id} data-name=${listData[i].tname}>
				</td>    	 	
				<td align="left">${listData[i].tname}</td>			
			</tr>`;			
		}
	};
	$("#listname").after(tableHTML);
}

