function save() {
	var current_map_name = parent.document.getElementById("centertitle").innerHTML;
	var link_width = document.getElementById("link_width").value;
	parent.window.updateLinkWidth(parent.updLinkId, link_width);
}

function closeDialog() {
	parent.window.closeDialog();
}

window.onload = function() {
	document.getElementById("link_width").value = parent.updLinkWidth;
}