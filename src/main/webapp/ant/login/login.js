
$(document).ready(function() {
	$.ajax({
		type : 'POST',
		url : "/Admin/getErrorMsg",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		async : true,
		success : function(errorMsg) {
			if (errorMsg != null && errorMsg != "") {
				if (errorMsg == "password") {
					var url = "./passwordUpdate.html";
					openDialog(url, "用户密码管理", 700, 400);
				} else if (errorMsg == "license") {
					var url = "./loginRegister.html";
					openDialog(url, "请输入激活码激活系统", 700, 350);
				} else {
					$("#errorMsg").html(errorMsg);
				}
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        }
	})	
})

function onInput() {
	if (getExplorerInfo().type != "Chrome") {
		// 获取密码框对象obj
		var obj = document.getElementById("password");
		// 获取真实存储的密码值
		var value0 = obj.getAttribute("myvalue");
		// 获取显示的密文的密码"•"
		var value1 = obj.value;
		// 获取两者的长度
		var len0 = value0.length;
		var len1 = value1.length;
		// 获取光标的位置
		var start1 = obj.selectionStart;
		// 如果输入的字符串值大于隐藏的密码真实值则在value0基础上加上刚输入的不是"•"的字符
		if (len1 > len0) {
			var x = "";
			for (var i = 0; i < len1; i++) {
				if (value1[i] != "•") {
					x = value1[i];
					break;
				}
			}
			if (x != "") {
				value0 += x;
			}
		// 如果两者长度相等说明是选中一个字符后同时输入了1个字符则需要找到选中的字符并替换刚输入的字符
		} else if (len1 == len0) {
			var x = "";
			for (var i = 0; i < len1; i++) {
				if (value1[i] != "•") {
					x = value1[i];
					break;
				}
			}
			var value = "";
			if (x != "") {
				if ((start1 - 1) < len0) {
					
					for (var i = 0; i < len0 ; i++) {
						if (i != (start1 - 1)) {
							value += value0[i];
						} else {
							value += x;
						}
					}
				}
				value0 = value;
			}
		// 如果输入的value值长度小于myvalue说明删除了1个或者多个字符则需要根据具体情况判断删除了哪些对应的删除myvalue
		} else {
			var x = "";
			for (var i = 0; i < len1; i++) {
				if (value1[i] != "•") {
					x = value1[i];
					break;
				}
			}
			var value = "";
			// 证明是删除的操作,并不是select后输了1个字符这种情况,start1就是删除的下标
			if (x == "") {
				var len = len0 - len1;
				for (var i = 0; i < len0; i++) {
				//	if (i < start1 || i > (start1 + len - 1)) {
						if (i < parseInt(startl) || i > (startl + len -1)) {
						value += value0[i];
					}
				}
			// 说明是选中1个或者多个字符后输入了1个新的字符则需要删除选中的并替换原来的
			} else {
				var len = len0 - len1;
				for (var i = 0; i < len0; i++) {
					if (i == (start1 - 1)) {
						value += x;
					}
					
					if (i < (start1 - 1) || i > (start1 + len - 1)) {
						value += value0[i];
					}
				}
			}
			value0 = value;
		}
		// 把刚输入的重新都置成"•"
	 	var len = (obj.value).length;
		var value = "";
		for (i = 0; i < len ; i++) {
			value += "•";
		}
		// 每一次输入动作后都要把刚输入的字符变成"•"并赋值给value
		obj.value = value;
		// 每一次输入动作后同步替换原来myvalue值并保存至属性myvalue中
		obj.setAttribute("myvalue", value0);
	}
}

function onLogin() {
	var username = document.getElementById("username").value;
	var password = null;
	data = {};
	data["username"] = $.base64.encode(username);
	if (getExplorerInfo().type == "Chrome") {
		password = document.getElementById("password").value;
		data["password"] = $.base64.encode(password);
	} else {
		password = document.getElementById("password").getAttribute("myvalue");		
		data["password"] = $.base64.encode(password);
	}
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: data, 
		url: "/Admin/loginAjax",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		// timeout: 5000,
		async: true,
		success: function(json) {
			location.href = json;
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error : function() {
			location.href = json;
		}
	})
}

$(function(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	$('#password').keydown(function(event) {
		if (event.keyCode == 13) {
			onLogin();
		}
	});
});

function isIE() {
    if (!!window.ActiveXObject || "ActiveXObject" in window) {
      return true;
    } else {
      return false;
	}
}

function getExplorerInfo() { 
	var explorer = window.navigator.userAgent.toLowerCase();
	if (explorer.indexOf("net") >= 0) { 
		if (!!window.ActiveXObject || "ActiveXObject" in window) {
			return { type: "IE", version: "1.0.0" }; 
		}
		return { type: "Others", version: "1.0.0" }; 
	} else if (explorer.indexOf("firefox") >= 0) { 
		var ver = explorer.match(/firefox\/([\d.]+)/)[1]; 
		return { type: "Firefox", version: ver }; 
	} else if (explorer.indexOf("chrome") >= 0) { 
		var ver = explorer.match(/chrome\/([\d.]+)/)[1]; 
		return { type: "Chrome", version: ver }; 
	} else if (explorer.indexOf("opera") >= 0) { 
		var ver = explorer.match(/opera.([\d.]+)/)[1]; 
		return { type: "Opera", version: ver }; 
	} else if (explorer.indexOf("Safari") >= 0) { 
		var ver = explorer.match(/version\/([\d.]+)/)[1]; 
		return { type: "Safari", version: ver }; 
	} else {
		return { type: "Others", version: "1.0.0" }; 
	}
}	

var index = null;
function openDialog(url, title, width, height) {
	index = layer.open({
	  type: 2,
	  title: title,
	  area: [width+'px', height+'px'],
	  fixed: true,
	  maxmin: false,
	  content: url
	});
}

function closeDialog() {
	layer.close(index);
}

