
function showNoData(width, height) {
	return "<div style='width:"
			+ width
			+ "%;height:"
			+ height
			+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
			+ height + "px;' >这里暂时没有数据</div>";
}

function showNoFunction(width, height) {
	return "<div style='width:"
			+ width
			+ "%;height:"
			+ height
			+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
			+ height + "px;' >抱歉，您没有此处权限！</div>";
}

$(document).ready(function() {
	var request = GetSelfRequest();
	var id = request.id;
	loadTable(id);
})

function changeColledMode(){
	var colled_value = $("#colled_mode").val()
	if (colled_value == "0") {
		$("#snmp_attr").css("display", "none");
		$("#ssh_attr").css("display", "none");
		$("#serviceport_attr").css("display", "true");
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		var str = '<div class="input-field col s12">'+
					  '<input id="auth_pass" type="text" class="validate" value="6000">'+
			 		  '<label id="port" for="auth_pass" class="myactive">专用代理服务端口（UDP）</label>'+
				  '</div>';
		$("#serviceport_attr").html(str);
	}
	
	if (colled_value == "1") {
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		$("#snmp_attr").css("display", "none");
		$("#ssh_attr").css("display", "none");
		$("#serviceport_attr").css("display", "none");
	}
	
	if (colled_value == "2") {
		$("#snmp_attr").css("display", "true");
		$("#ssh_attr").css("display", "none");
		$("#serviceport_attr").css("display", "true");
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		var strSnmp = '<div class="input-field col s6">'+
				      		'<input id="r_comm" type="password" class="validate" value="">'+
							'<label id="user_show_snmp" for="r_comm" class="myactive">SNMPv1&v2c读团体字</label>'+
					  '</div>'+
							'<div class="input-field col s6">'+
							'<input id="w_comm" type="password" class="validate" value="">'+
							'<label id="pass_show_snmp" for="w_comm" class="myactive">SNMPv1&v2c写团体字</label>'+
					  '</div>';
		var strService = '<div class="input-field col s12">'+
							 '<input id="auth_pass" type="text" class="validate" value="161">'+
							 '<label id="port" for="auth_pass" class="myactive">SNMPv1&v2c服务端口</label>'+
						 '</div>';	
		$("#snmp_attr").html(strSnmp);
		$("#serviceport_attr").html(strService);
	}
	
	if (colled_value == "3") {
		$("#snmp_attr").css("display", "true");
		$("#ssh_attr").css("display", "true");
		$("#serviceport_attr").css("display", "true");
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		
		var strSnmp = '<div class="input-field col s6">'+
		  		        	'<select id="r_comm" class="my-form-select">'+
						        '<option value="MD5">MD5</option>'+
						        '<option value="SHA">SHA</option>'+
					        '</select>'+
						    '<label for="r_comm" class="myactive">SNMPv3认证方式</label>'+
					  '</div>'+
					  '<div class="input-field col s6">'+
					        '<select id="w_comm" class="my-form-select">'+
						        '<option value="DES">DES</option>'+
						        '<option value="AES">AES</option>'+
					        '</select>'+
							'<label for="w_comm" class="myactive">SNMPv3加密方式</label>'+
					  '</div>';
		var strSsh = '<div class="input-field col s6">'+
							'<input id="username" type="password" class="validate" value="">'+
							'<label id="user_show_ssh" for="username" class="myactive">SNMPv3用户名称</label>'+
						'</div>'+
						'<div class="input-field col s6">'+
							'<input id="password" type="password" class="validate" value="">'+
							'<label id="pass_show_ssh" for="password" class="myactive">SNMPv3认证密码</label>'+
						'</div>';
		
		var strServiceport = '<div class="input-field col s6">'+
								  '<input id="sshport" type="password" class="validate" value="">'+
								  '<label id="port_show_ssh" for="sshport" class="myactive">SNMPv3加密密码</label>'+
							 '</div>'+
							 '<div class="input-field col s6">'+
								  '<input id="auth_pass" type="text" class="validate" value="161">'+
								  '<label id="port" for="auth_pass" class="myactive">SNMPv3服务端口</label>'+
							 '</div>';
		$("#snmp_attr").html(strSnmp);
		$("#ssh_attr").html(strSsh);
		$("#serviceport_attr").html(strServiceport);
	}
}
function assetUpdate() {
	var id = $("#device_id").html() + "";
	var a_ip = $("#a_ip").val();
	var bm_ip = $("#bm_ip").val();
	var yw_ip = $("#yw_ip").val();
	var a_name = $("#a_name").val();
	var a_no = $("#a_no").val();
	var a_type = $("#a_type").val();
	var a_pos = $("#a_pos").val();
	var a_manu = $("#a_manu").val();
	var a_user = $("#a_user").val();
	var a_dept = $("#a_dept").val();
	var a_date = $("#a_date").val();
	var colled = $("#colled").val();
	var colled_mode = $("#colled_mode").val();
	var r_comm = $("#r_comm").val();
	var w_comm = $("#w_comm").val();
	var auth_pass = $("#auth_pass").val();
	var username = $("#username").val();
	var password = $("#password").val();
	var sshport = $("#sshport").val();	

	if (a_ip == null || a_ip == "") {
		alert("IP为必选项");
		return;
	}
	if (a_no == null || a_no == "") {
		alert("ASPID（aspId）为必填项！");
		return;		
	}

	var data_json = {};
	data_json["id"] = id;
	data_json["AIp"] = a_ip;
	data_json["BmIp"] = bm_ip;
	data_json["YwIp"] = yw_ip;
	data_json["nmsAssetTypeId"] = a_type;
	data_json["nmsAssetDepartmentId"] = a_dept;
	data_json["colled"] = colled;

	if (a_name != "") {
		data_json["AName"] = a_name;
	}
	if (a_no != "") {
		data_json["ANo"] = a_no;
	}
	if (a_pos != "") {
		data_json["APos"] = a_pos;
	}
	if (a_manu != "") {
		data_json["AManu"] = a_manu;
	}
	if (a_user != "") {
		data_json["AUser"] = a_user;
	}
	if (auth_pass != "") {
		data_json["authPass"] = auth_pass;
	}
	if (a_date != "") {
		data_json["ADate"] = a_date;
	}
	if (colled != "") {
		data_json["colled"] = colled;
	}
	if (colled_mode != null && colled_mode != "") {
		data_json["collMode"] = colled_mode;	
	}
	if (r_comm != null && r_comm != "") {
		data_json["RComm"] = r_comm;
	}
	if (w_comm != null && w_comm != "") {
		data_json["WComm"] = w_comm;
	}
	if (username != null && username != ""){
		data_json["username"] = username;
	}
	if (password != null && password != "") {
		data_json["password"] = password;
	}
	if (sshport != null && sshport != "") {
		data_json["sshport"] = sshport;
	}

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Asset/updateAsset",
		data : data_json,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			// alert(json);
			// jumpPage('hardWare.html');
			layer.msg(json.info, {time: 1000},function(){
				if(json.info == '修改成功！') {
					window.parent.location.reload();
				}
			});	
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
}

function loadTable(id) {
	$("#device_id").html(id);
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Asset/findByIdToUpdate",
		data: {
			id: id
		},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("body").html(showNoFunction(100, 600))
			} else {
				loadDepartment(json.nmsDepartment.id)
				loadAssetType(json.nmsAssetType.id)
				loadColled(json.colled, json.collMode);
				
				$("#a_ip").attr({"value" : json.aip});
				$("#a_name").attr({"value" : json.aname});
				$("#a_no").attr({"value" : json.ano});
				$("#a_ip").attr({"value" : json.aip});
				$("#bm_ip").attr({"value" : json.bmIp});
				$("#yw_ip").attr({"value" : json.ywIp});
				$("#a_pos").attr({"value" : json.apos});
				$("#a_manu").attr({"value" : json.amanu});
				$("#a_user").attr({"value" : json.auser});
				$("#a_date").attr({"value" : json.adate});
				if (json.collMode == "0") {		
					$("#auth_pass").attr({"value" : json.authPass});
				} else if (json.collMode == "2") {
					$("#r_comm").attr({"value" : json.rcomm});
					$("#w_comm").attr({"value" : json.wcomm});
					$("#auth_pass").attr({"value" : json.authPass});
				} else if (json.collMode == "3") {
					$("#r_comm").children().each(function() {
						if ($(this).val() == json.rcomm) {
							$(this).attr("selected", true)
						}
					})
					
					$("#w_comm").children().each(function() {
						if ($(this).val() == json.wcomm) {
							$(this).attr("selected", true)
						}
					})
			//		$("#r_comm").attr({"value" : json.rcomm});	
			//		$("#w_comm").attr({"value" : json.wcomm});
					$("#auth_pass").attr({"value" : json.authPass});
					$("#username").attr({"value" : json.username});		
					$("#password").attr({"value" : json.password});			
					$("#sshport").attr({"value" : json.sshport});
				}
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
}

$("#a_ip").blur(function(){
	ipBlur($("#a_ip").val())
})

function ipBlur(ip){
	let a_ip
	if (ip == null) {
		a_ip = $("#a_ip").val().trim()
	} else {
		if (isValidIP(ip)) {
			a_ip = ip
		} else {
			$("#a_ip_label").html("IP地址无效")
			$("#a_ip_label").css("color", "red")
			a_ip = null
		}
	}

	let dataJson = {}
	dataJson["AIp"] = a_ip
	if(dataJson.AIp) {
		$.ajax({
			type: "POST",
			dataType: "json",
			url: "/Asset/ifIp",
			data: dataJson,
			contentType: 'application/x-www-form-urlencoded;charset=utf-8',
			timeout: 5000,
			cache: true,
			async: true,
			success: function(json) {
				if (json) {
					$("#a_ip_label").html("IP地址已存在")
					$("#a_ip_label").css("color", "red")
				} else {
					$("#a_ip_label").html("IP地址有效")
					$("#a_ip_label").css("color", "green")
				}
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", getCookie("token"));
			},
			error : function() {
				$("#page-inner").html(showNoFunction(100, 600));
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
		})
	}
}

function loadDepartment(dept_id) {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Department/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			for ( var i in json) {
				if (json[i].id == dept_id) {
					$("#a_dept").append('<option value="' + json[i].id + '" selected>' + json[i].dname + '</option>');
				} else {
					$("#a_dept").append('<option value="' + json[i].id + '">' + json[i].dname + '</option>');
				}
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
}

function loadAssetType(type_id) {
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/assetType/all",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			for (var i in json) {
				if (json[i].nodeTag == "net") {
					if (json[i].id == type_id) {
						$("#a_type").append('<option value="' + json[i].id + '" selected>' + json[i].chType + '/' + json[i].chSubtype + '</option>');
					} else { 
						$("#a_type").append( '<option value="'+json[i].id+'">'+json[i].chType+'/'+json[i].chSubtype+'</option>' );
					}
				}
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
}

function loadColled(colled, collMode) {
	var colledObj = $("#colled").children();
	var collModeObj = $("#colled_mode").children();
	colledObj.each(function() {
		if ($(this).val() == colled) {
			$(this).attr("selected", true)
		}
	})
	collModeObj.each(function() {
		if ($(this).val() == collMode) {
			$(this).attr("selected", true);
		}
	})

	if (collMode == "0") {
		$("#snmp_attr").css("display", "none");
		$("#ssh_attr").css("display", "none");
		$("#serviceport_attr").css("display", "true");
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		var str = '<div class="input-field col s12">'+
					  '<input id="auth_pass" type="text" class="validate" value="6000">'+
			 		  '<label id="port" for="auth_pass" class="myactive">专用代理服务端口（UDP）</label>'+
				  '</div>';
		$("#serviceport_attr").html(str);
	}
	
	if (collMode == "1") {
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		$("#snmp_attr").css("display", "none");
		$("#ssh_attr").css("display", "none");
		$("#serviceport_attr").css("display", "none");
	}
	
	if (collMode == "2") {
		$("#snmp_attr").css("display", "true");
		$("#ssh_attr").css("display", "none");
		$("#serviceport_attr").css("display", "true");
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		var strSnmp = '<div class="input-field col s6">'+
				      		'<input id="r_comm" type="password" class="validate" value="">'+
							'<label id="user_show_snmp" for="r_comm" class="myactive">SNMPv1&v2c读团体字</label>'+
					  '</div>'+
							'<div class="input-field col s6">'+
							'<input id="w_comm" type="password" class="validate" value="">'+
							'<label id="pass_show_snmp" for="w_comm" class="myactive">SNMPv1&v2c写团体字</label>'+
					  '</div>';
		var strService = '<div class="input-field col s12">'+
							 '<input id="auth_pass" type="text" class="validate" value="161">'+
							 '<label id="port" for="auth_pass" class="myactive">SNMPv1&v2c服务端口</label>'+
						 '</div>';	
		$("#snmp_attr").html(strSnmp);
		$("#serviceport_attr").html(strService);
	}
	
	if (collMode == "3") {
		$("#snmp_attr").css("display", "true");
		$("#ssh_attr").css("display", "true");
		$("#serviceport_attr").css("display", "true");
		$("#snmp_attr").empty();
		$("#ssh_attr").empty();
		$("#serviceport_attr").empty();
		
		var strSnmp = '<div class="input-field col s6">'+
		  		        	'<select id="r_comm" class="my-form-select">'+
						        '<option value="MD5">MD5</option>'+
						        '<option value="SHA">SHA</option>'+
					        '</select>'+
						    '<label for="r_comm" class="myactive">SNMPv3认证方式</label>'+
					  '</div>'+
					  '<div class="input-field col s6">'+
					        '<select id="w_comm" class="my-form-select">'+
						        '<option value="DES">DES</option>'+
						        '<option value="AES">AES</option>'+
					        '</select>'+
							'<label for="w_comm" class="myactive">SNMPv3加密方式</label>'+
					  '</div>';
		var strSsh = '<div class="input-field col s6">'+
							'<input id="username" type="password" class="validate" value="">'+
							'<label id="user_show_ssh" for="username" class="myactive">SNMPv3用户名称</label>'+
						'</div>'+
						'<div class="input-field col s6">'+
							'<input id="password" type="password" class="validate" value="">'+
							'<label id="pass_show_ssh" for="password" class="myactive">SNMPv3认证密码</label>'+
						'</div>';
		
		var strServiceport = '<div class="input-field col s6">'+
								  '<input id="sshport" type="password" class="validate" value="">'+
								  '<label id="port_show_ssh" for="sshport" class="myactive">SNMPv3加密密码</label>'+
							 '</div>'+
							 '<div class="input-field col s6">'+
								  '<input id="auth_pass" type="text" class="validate" value="161">'+
								  '<label id="port" for="auth_pass" class="myactive">SNMPv3服务端口</label>'+
							 '</div>';
		$("#snmp_attr").html(strSnmp);
		$("#ssh_attr").html(strSsh);
		$("#serviceport_attr").html(strServiceport);
	}
}

$("#a_date").datetimepicker({
	language: "zh-cn",
	format: "yyyy-mm-dd",
	minView: "month",
	autoclose: true,
	todayBtn: true
});


