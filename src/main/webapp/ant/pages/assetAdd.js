
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
	loadTable();
	// ipBlur();
})

function loadTable(){
	loadDepartment();
	loadAssetType();
	// loadLastAsset();
}

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

function assetAdd(){
	if ($("#ip_alarm").html() == "IP地址已存在") {
		alert("已存在相同IP的设备，无法添加！");
		return;
	}
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
	var colled = $("#colled").val();
	var colled_mode = $("#colled_mode").val();
	var r_comm = $("#r_comm").val();
	var w_comm = $("#w_comm").val();
	var auth_pass = $("#auth_pass").val();
	var username = $("#username").val();
	var password = $("#password").val();
	var sshport = $("#sshport").val();
	var a_date = $("#a_date").val();
	
	if(a_ip == ""){
		alert("IP地址为必填项！");
		return;
	}
	
	if (a_name == "") {
		alert("设备名称为必填项！");
		return;		
	}
	
	if (a_no == "") {
		alert("ASPID为必填项！");
		return;		
	}
	
	var dataJson = {};
	dataJson["AIp"] = a_ip;
	dataJson["BmIp"] = bm_ip;
	dataJson["YwIp"] = yw_ip;
	dataJson["nmsAssetTypeId"] = a_type;
	dataJson["nmsAssetDepartmentId"] = a_dept;
	dataJson["colled"] = colled;
	
	if (a_name != ""){
		dataJson["AName"] = a_name;
	}
	
	if(a_no != ""){
		dataJson["ANo"] = a_no;
	}
	
	if(a_pos != ""){
		dataJson["APos"] = a_pos;
	}
	
	if (a_manu != ""){
		dataJson["AManu"] = a_manu;
	}
	
	if (a_user != ""){
		dataJson["AUser"] = a_user;
	}
	
	if (auth_pass != ""){
		dataJson["authPass"] = auth_pass;
	}
	
	if(a_date!=""){
		dataJson["ADate"] = a_date;
	}
	
	if (colled == "0") {
		dataJson["collMode"] = colled_mode;
		if (colled_mode=="2" || colled_mode=="3" || colled_mode=="4") {
			if (r_comm != "") {
				dataJson["RComm"] = r_comm;
			}
			
			if (w_comm != "") {
				dataJson["WComm"] = w_comm;
			}
			
			if (username != null){
				dataJson["username"] = username;
			}
			
			if (password != null) {
				dataJson["password"] = password;
			}
			
			if (sshport != null) {
				dataJson["sshport"] = sshport;
			}				
		}
	}
	
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Asset/addAsset",
		data: dataJson,
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			// alert(json);
			// closeDialog();
			layer.msg(json.info, {time: 1000},function(){
				if(json.info == '添加成功!') {
					window.parent.location.reload();
				}
			});			
		},
		
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
        
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.responseText);
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
	});
}

function loadDepartment() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Department/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			for(var i in json){
				$("#a_dept").append(
					'<option value="'+json[i].id+'">'+json[i].dname+'</option>'
				)
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

function loadAssetType() {
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/assetType/all",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			for(var i in json){
				if (json[i].nodeTag == "net") {
					$("#a_type").append(
						'<option value="'+json[i].id+'">'+json[i].chType+' / '+json[i].chSubtype+'</option>'
					)
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

function loadLastAsset(){
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Asset/loadLastAsset",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				ipBlur(json.aip);
				$("#a_ip").val(json.aip);
				$("#a_name").val(json.aname);
				$("#a_no").val(json.ano);
				$("#a_pos").val(json.apos);
				$("#a_manu").val(json.amanu);
				$("#a_user").val(json.auser);
				$("#r_comm").val("public");
				$("#w_comm").val("public");
				$("#auth_pass").val("6000");
				$("#a_date").val(json.adate);
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
	});
}

$("#a_ip").blur(function(){
	ipBlur($("#a_ip").val());
})
var closeDialog = function() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}

function ipBlur(ip) {
	if (ip == null) {
		var a_ip = $("#a_ip").val().trim();
	} else {
		if(isValidIP(ip)) {
			a_ip = ip;
		} else {
			$("#ip_alarm").html("IP地址无效");
			$("#ip_alarm").css("color", "red");
			a_ip = null
		}
		
	}
	var dataJson = {};
	dataJson["AIp"] = a_ip;
	if(dataJson["AIp"]){
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : "/Asset/ifIp",
			data : dataJson,
			contentType : 'application/x-www-form-urlencoded;charset=utf-8',
			timeout : 5000,
			cache : true,
			async : true,
			success : function(json) {
				if (json) {
					$("#ip_alarm").html("IP地址已存在");
					$("#ip_alarm").css("color", "red");
				} else {
					$("#ip_alarm").html("IP地址有效");
					$("#ip_alarm").css("color", "green");
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
		});
	}
	
}


function importTable(){
    var importFile = $('#importFile')[0].files[0];
	if (null==importFile){
		alert("您尚未选择导入的xls文件")
		return;
	}
    var formData = new FormData();
    formData.append("importFile", importFile);
    $.ajax({
        url:'/deviceimport/input',
        dataType:'json',
        type:'POST',
        async: false,
		timout:60*1000,
        data: formData,
        processData : false,
        contentType : false,
        success: function(data){
        	alert(data);
            parent.location.reload(true);
        },
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
        error:function(response){
            alert(response.message);
        }
    });
}

$("#a_date").datetimepicker({
	language: "zh-cn",
	format: "yyyy-mm-dd",
	minView: "month",
	autoclose: true,
	todayBtn: true
});

/*
function retPage() {
	var request = GetRequest();
	var begin = request.begin;
	var offset = request.offset;
	var orderKey = request.orderKey;
	var orderValue = request.orderValue;
	var url = "hardWare.html?begin=" + begin + "&offset=" + offset + "&orderKey=" + orderKey + "&orderValue=" + orderValue;
	jumpPage(url);
}
*/



