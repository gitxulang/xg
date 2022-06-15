// 删除置顶按钮
if(document.getElementsByClassName('click-to-toggle').length > 0){
	document.getElementsByClassName('click-to-toggle')[0].remove()
}
function getCookie(key) {
	var cookieArr = document.cookie.split('; ');
	for ( var i = 0; i < cookieArr.length; i++) {
		var arr = cookieArr[i].split('=');
		if (arr[0] === key) {
			return arr[1];
		}
	}
	return false;
}

function jumpPage(url) {
	// 更新浏览器URL地址栏信息, 为了按F5刷新当前页面而不是跳转之前地址
	var address = window.parent.location + "";
	if (address != null) {
		var strs = address.split("?");
		var newUrl = strs[0] + "?redirect=" + url.replace("?", "&");
		var stateObject = {};
		window.parent.history.pushState(stateObject, '', newUrl);
	}
	//index.html的子页面互相跳转iframe重定向
	$("#main", window.parent.document).attr('src', url);
}

function GetRequest() {
	var url = parent.location.search;
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

function GetSelfRequest() {
	var url = location.search;
	let pathname = location.pathname.slice(5)
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
		theRequest.redirect = pathname
	}
	return theRequest;
}

function isRealNum(val) {
	// isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，    
　　if(val === "" || val ==null) {
        return false;
　　}
	if (!isNaN(val)) {　　　　
		// 对于空数组和只有一个数值成员的数组或全是数字组成的字符串,isNaN返回false,例如：'123',[],[2],['123'],isNaN返回false,   
		// 所以如果不需要val包含这些特殊情况，则这个判断改写为if (!isNaN(val) && typeof val === 'number')
		return true; 　　
	} else {
		return false; 　　
	}
}

function isValidIP(ip) {
    let reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    return reg.test(ip);
} 

