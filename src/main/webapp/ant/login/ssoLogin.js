$(document).ready(function() {
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {"id":"1"},
		url: "/SecRule/getSSORulesById",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		timeout: 5000,
		async: false,
		success: function(json) {
			console.log(json)
			if (json.ssoSwitch == 1) {
				ssoLogin(json.appId, json.appName);
			} else {
				alert('单点登录未开启')
				location.href = "login.html";
			}
		},
		beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getCookie("token"));
        },
		error : function() {
			alert('获取UserName请求失败');
		}
	})
})

function ssoLogin(appId, appName){
	console.log("[DEBUG] start ssoLogin...");
	var wsObj = new WebSocket("wss://127.0.0.1:2355");   //建立连接
	console.log("[DEBUG] new WebSocket...");
    wsObj.onopen = function(){  //发送请求
        var messageObj = {};
        messageObj['appId'] = appId;
        messageObj['appName'] = appName;
        messageObj['action'] = "getTokenByAppInfo";
        console.log("[DEBUG] ssoLogin: appId=" + appId + ", appName=" + appName + ", action=getTokenByAppInfo");
        var messageJson = JSON.stringify(messageObj);
        console.log("messageJson=" + messageJson);
        wsObj.send(messageJson);
    };
    
    wsObj.onmessage = function(ev) {  //获取后端响应
    	var json_data = JSON.parse(ev.data)
        console.log(json_data)
        if (json_data.stateCode == '001') {
            console.log('success');
            console.log('token' + json_data.token);
            data_forUserName = {}
            data_forUserName['token'] = json_data['token'];
            data_forUserName['appId'] = appId;
            data_forUserName['appName'] = appName;
            $.ajax({
				type: 'POST',
				dataType: "json",
				data: data_forUserName,
				url: "/Admin/ssoLogin",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				timeout: 5000,
				async: false,
				success: function(data) {
					console.log("[DEBUG] data...");
					location.href = data;
				},
				beforeSend: function(xhr) {
		            xhr.setRequestHeader("Authorization", getCookie("token"));
		        },
				error : function() {
					console.log('ssoLogin请求出错');
					location.href = "login.html";
				}
			})
        } else {
            alert('获取token失败');
        }
    };
    
    wsObj.onclose = function(ev){
        console.log("close");
    };
    
    wsObj.onerror = function(ev){
        console.log("error");
        location.href = "login.html";
    };
}

