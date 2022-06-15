function deleteMenuOver() {
	window.event.srcElement.className = "deleteline_menu_over";
}

function pingMenuOut() {
	window.event.srcElement.className = "ping_menu_out";
}

function pingMenuOver() {
	window.event.srcElement.className = "ping_menu_over";
}

function showmenu(elmnt) {
	document.getElementById(elmnt).style.visibility = "visible"
}

function hidemenu(elmnt) {
	document.getElementById(elmnt).style.visibility = "hidden"
}

function toolMenuOut() {
	window.event.srcElement.className = "tool_menu_out";
}

function toolMenuOver() {
	window.event.srcElement.className = "tool_menu_over";
}

function deleteMenuOut() {
	window.event.srcElement.className = "deleteline_menu_out";
}

function detailMainMenuOut() {
	window.event.srcElement.className = "detail_mainmenu_out";
}

function detailMainMenuOver() {
	window.event.srcElement.className = "detail_mainmenu_over";
}

function detailMenuOut() {
	window.event.srcElement.className = "detail_menu_out";
}

function detailMenuOver() {
	window.event.srcElement.className = "detail_menu_over";
}

function setMenuOut() {
	window.event.srcElement.className = "set_menu_out";
}

function setMenuOver() {
	window.event.srcElement.className = "set_menu_over";
}

function manageMainMenuOut() {
	window.event.srcElement.className = "manage_mainmenu_out";
}

function manageMainMenuOver() {
	window.event.srcElement.className = "manage_mainmenu_over";
}

function reportMainMenuOut() {
	window.event.srcElement.className = "report_mainmenu_out";
}

function reportMainMenuOver() {
	window.event.srcElement.className = "report_mainmenu_over";
}

function alarmMenuOut() {
	window.event.srcElement.className = "alarm_menu_out";
}

function alarmMenuOver() {
	window.event.srcElement.className = "alarm_menu_over";
}

function sbmbMenuOut() {
	window.event.srcElement.className = "sbmb_menu_out";
}

function sbmbMenuOver() {
	window.event.srcElement.className = "sbmb_menu_over";
}

function reportMenuOut() {
	window.event.srcElement.className = "report_menu_out";
}

function reportMenuOver() {
	window.event.srcElement.className = "report_menu_over";
}

function deleteEquipRelatedApplicationsMenuOut() {
	window.event.srcElement.className = "equipRelatedApplications_menu_out";
}

function deleteEquipRelatedApplicationsMenuOver() {
	window.event.srcElement.className = "equipRelatedApplications_menu_over";
}

function panelmanageMenuOut() {
	window.event.srcElement.className = "panel_manage_menu_out";
}

function panelmanagelMenuOver() {
	window.event.srcElement.className = "panel_manage_menu_over";
}

function detailMenuOut1() {
	window.event.srcElement.className = "detail_menu_out1";
}

function detailMenuOver1() {
	window.event.srcElement.className = "detail_menu_over1";
}

function manageMenuOut() {
	window.event.srcElement.className = "manage_menu_out";
}

function manageMenuOver() {
	window.event.srcElement.className = "manage_menu_over";
}

function downloadMenuOut() {
	window.event.srcElement.className = "download_menu_out";
}

function downloadMenuOver() {
	window.event.srcElement.className = "download_menu_over";
}

function deleteEquipMenuOut() {
	window.event.srcElement.className = "deleteEquip_menu_out";
}

function deleteEquipMenuOver() {
	window.event.srcElement.className = "deleteEquip_menu_over";
}

function confirmAlarmMenuOut() {
	window.event.srcElement.className = "confirmAlarm_menu_out";
}

function confirmAlarmMenuOver() {
	window.event.srcElement.className = "confirmAlarm_menu_over";
}

function propertyMenuOut() {
	window.event.srcElement.className = "property_menu_out";
}

function propertyMenuOver() {
	window.event.srcElement.className = "property_menu_over";
}

function collectionMenuOut() {
	window.event.srcElement.className = "collection_menu_out";
}

function collectionMenuOver() {
	window.event.srcElement.className = "collection_menu_over";
}

function thresholdMenuOut() {
	window.event.srcElement.className = "threshold_menu_out";
}

function thresholdMenuOver() {
	window.event.srcElement.className = "threshold_menu_over";
}

function portMenuOut() {
	window.event.srcElement.className = "port_menu_out";
}

function portMenuOver() {
	window.event.srcElement.className = "port_menu_over";
}

function portthresholdMenuOut() {
	window.event.srcElement.className = "portthreshold_menu_out";
}

function portthresholdMenuOver() {
	window.event.srcElement.className = "portthreshold_menu_over";
}

function portscanMenuOut() {
	window.event.srcElement.className = "portscan_menu_out";
}

function portscanMenuOver() {
	window.event.srcElement.className = "portscan_menu_over";
}

function relationMapMenuOut() {
	window.event.srcElement.className = "relationmap_menu_out";
}

function relationMapMenuOver() {
	window.event.srcElement.className = "relationmap_menu_over";
}

function deleteLineMenuOut() {
	window.event.srcElement.className = "deleteline_menu_out";
}

function deleteLineMenuOver() {
	window.event.srcElement.className = "deleteline_menu_over";
}

function editLineMenuOut() {
	window.event.srcElement.className = "editline_menu_out";
}

function editLineMenuOver() {
	window.event.srcElement.className = "editline_menu_over";
}

function telnetMenuOut() {
	window.event.srcElement.className = "telnet_menu_out";
}

function telnetMenuOver() {
	window.event.srcElement.className = "telnet_menu_over";
}

function listMenuOut() {
	window.event.srcElement.className = "list_menu_out";
}

function listMenuOver() {
	window.event.srcElement.className = "list_menu_over";
}

function traceMenuOut() {
	window.event.srcElement.className = "trace_menu_out";
}

function traceMenuOver() {
	window.event.srcElement.className = "trace_menu_over";
}

function cloudMenuOut() {
	window.event.srcElement.className = "cloud_menu_out";
}

function cloudMenuOver() {
	window.event.srcElement.className = "cloud_menu_over";
}

function evtMenu2() {
	HideMenu();
}

function evtMenuOnmouseMove() {
	this.style.backgroundColor = '#8AAD77';
	this.style.paddingLeft = '30px';
}

function evtOnMouseOut() {
	this.style.backgroundColor = '#FAFFF8';
}

function IsIE() {
	if (navigator.appName == "Microsoft Internet Explorer") {
		return true;
	} else {
		return false;
	}
}

function ShowMenu() {
	if (IsIE()) {
		document.body.onclick = HideMenu;
		var redge = document.body.clientWidth - event.clientX;
		var bedge = document.body.clientHeight - event.clientY;
		var menu = $("div_RightMenu");
		if (redge < menu.offsetWidth) {
			menu.style.left = document.body.scrollLeft + event.clientX
					- menu.offsetWidth

		} else {
			menu.style.left = document.body.scrollLeft + event.clientX
			menu.style.display = "block";
		}
		if (bedge < menu.offsetHeight) {
			menu.style.top = document.body.scrollTop + event.clientY
					- menu.offsetHeight
		} else {
			menu.style.top = document.body.scrollTop + event.clientY
			menu.style.display = "block";
		}
	}
	return false;
}

function HideMenu() {
	if (IsIE())
		$("div_RightMenu").style.display = "none";
}

function $(gID) {
	return document.getElementById(gID);
}

