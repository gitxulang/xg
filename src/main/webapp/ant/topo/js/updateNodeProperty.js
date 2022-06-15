function save() {
	var current_map_name = parent.document.getElementById("centertitle").innerHTML;
	var node_alias = document.getElementById("node_alias").value;
	parent.window.updateNodeAlias(parent.updNodeId, node_alias,
			current_map_name);
}

function closeDialog() {
	parent.window.closeDialog();
}

window.onload = function() {
	console.log(parent.updNodeAlias)
	document.getElementById("node_alias").value = parent.updNodeAlias;
}