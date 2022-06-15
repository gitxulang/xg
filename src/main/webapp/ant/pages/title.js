
var index;
var realname;
$(document).ready(function(){
	$.ajax({
		type: 'POST',
		url: "/Admin/getuser",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(user) {
			if(user != null) {
				realname = user;
				$("#user").html(user);
				if (user == "secadm" || user == "secadm") {
					// 只有小系统管理员不能看到,小安全员、小审计员、运管系统管理员(单点登录用户)都能看到
					$("#bell").remove();
				}
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /AuditConfig/getuser: " + textStatus);
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
	})
})

function openMessageDialog(){
	openDialog("messageDialog.html", "告警消息日志", 800, 450);
}

function openDialog(url, title, width, height) {
	index = top.layer.open({
	  type: 2,
	  title: title,
	  area: [width+'px', height+'px'],
	  fixed: true,
	  maxmin: false,
	  content: url
	});
}

// 监控管理-弹出框
function openDialogPerform(url, title, width, height, offset = 'auto', skin = 'performw0') {
	let index = top.layer.open({
	  type: 2,
	  title: title,
	  area: [width+'px', height+'px'],
	  fixed: true,
	  maxmin: false,
	  content: url,
	  shade: 0,
	  offset: offset,
	  skin: skin
	});
	// $('.' + skin).addClass('performw')
	window.parent.document.getElementsByClassName(skin)[0].classList.add('performw')
	// $('.' + skin + ' .layui-layer-close1').off('click')
	// $('.' + skin + ' .layui-layer-close1').on('click', function(){
	// 	$('.performw').remove()
	// })
	parent.$('.' + skin + ' .layui-layer-close1').off('click')
	parent.$('.' + skin + ' .layui-layer-close1').on('click', function(){
		parent.$('.performw').remove()
	})
	
	return index
}


function closeDialog() {
	console.log("close");
	layer.close(index);
}

function logout(){
	$.ajax({
		type : 'POST',
		dataType : "json",
		data: null,
		url : "/Admin/logoutAjax",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			parent.location.href = json;
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /AuditConfig/logoutAjax: " + textStatus);
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


function updateProfile(){
	var url = "./profileUpdate.html";
	openDialog(url, "账号属性管理", 800, 550);
}

function updatePassword(){
	var url = "./passwordUpdate.html";
	openDialog(url, "账号密码管理", 800, 400);
}

