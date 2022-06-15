var zoom = null;
var display = null;
var refresh = null;

document.onreadystatechange = subSomething;
function subSomething() {
	if (document.readyState == "complete") {
		$('#sys-loading').delay(10).hide(0);
		$('.spinner').delay(10).fadeOut('slow');
	}
}

$(document).ready(function() {
	var request = GetSelfRequest();
	zoom = request.zoom;
	display = request.display;
	refresh = request.refresh;
	if (display == 1) {
		display_zoom(zoom)
	}
})

function display_zoom(zoom) {
	$("#head_table").css("background-color", "transparent");
	$(".display").css("display", "none");
}

