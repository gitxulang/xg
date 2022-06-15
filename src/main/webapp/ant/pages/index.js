
(function($) {
	"use strict";
	var mainApp = {
		initFunction : function() {
			$('#main-menu').metisMenu();
			$(window).bind("load resize", function() {
				if ($(this).width() < 768) {
					$('div.sidebar-collapse').addClass('collapse')
				} else {
					$('div.sidebar-collapse').removeClass('collapse')
				}
			});
			$('.bar-chart').cssCharts({
				type : "bar"
			});
			$('.donut-chart').cssCharts({
				type : "donut"
			}).trigger('show-donut-chart');
			$('.line-chart').cssCharts({
				type : "line"
			});
			$('.pie-thychart').cssCharts({
				type : "pie"
			});
		},
		initialization : function() {
			mainApp.initFunction();
		}
	}
	
	$(document).ready(function() {
		$(".dropdown-button").dropdown();
		$("#sideNav").click(function() {
			if ($(this).hasClass('closed')) {
				$('.navbar-side').animate({
					left : '0px'
				});
				$(this).removeClass('closed');
				var iframe = document.getElementById('main');
				var iframeWindow = iframe.contentWindow;
				var $c = iframeWindow.$;
				$c('#page-wrapper').animate({
					'margin-left' : '260px'
				});
			} else {
				$(this).addClass('closed');
				$('.navbar-side').animate({
					left : '-260px'
				});
				var iframe = document.getElementById('main');
				var iframeWindow = iframe.contentWindow;
				var $c = iframeWindow.$;
				$c('#page-wrapper').animate({
					'margin-left' : '0px'
				});
			}
		});
		mainApp.initFunction();
		ssoLoginStatus();
	});
	$(".dropdown-button").dropdown();
}(jQuery));

function ssoLoginSwitch() {
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {"id":"1"},
		url: "/SecRule/getSSORulesById",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		timeout: 5000,
		async: false,
		success: function(json) {
			if (json.ssoSwitch == "1") {
				$("#userProfile").remove();
				$("#userPassword").remove();
			} 
		},
		beforeSend: function(xhr) {
	        xhr.setRequestHeader("Authorization", getCookie("token"));
	    },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /SecRule/getSSORulesById: " + textStatus);
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
}

function ssoLoginStatus() {
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: {"id":"1"},
		url: "/Admin/ifsso",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		timeout: 5000,
		async: false,
		success: function(data) {
			if (data != null) {
				if (data == 1) {	
					// 预留接口,当前登录1表示单点登录0表示用户名密码登录
				}
			} 
		},
		beforeSend: function(xhr) {
	        xhr.setRequestHeader("Authorization", getCookie("token"));
	    },
		error: function(jqXHR, textStatus, errorThrown) {
            console.log("[ERROR] /Admin/ifsso: " + textStatus);
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
}

function changeFrameHeight() {
	var ifm = document.getElementById("main");
	var subWeb = document.frames ? document.frames["iframepage"].document
			: ifm.contentDocument;
	if (ifm != null && subWeb != null) {
		ifm.height = subWeb.body.scrollHeight;
		ifm.width = subWeb.body.scrollWidth;
	}
	
	// 如果选项卡页面中已经选择收缩菜单栏，那么点击选项卡跳转，加载时也应该是缩放
	if ($('#sideNav').hasClass('closed')) {
		var iframe = document.getElementById('main');
		var iframeWindow = iframe.contentWindow;
		var $c = iframeWindow.$;
		$c('#page-wrapper').css('margin-left', '0px');
	}
}

window.οnresize = function() {
	changeFrameHeight();
}

function jump(object, url) {
	// 更新浏览器URL地址栏信息, 为了按F5刷新当前页面而不是首页,并去掉URL中#特殊字符
	var address = location + "";
	if (address != null) {
		var strs = address.split("?");
		var newUrl = strs[0] + "?redirect=" + url;
		var stateObject = {};
	    history.pushState(stateObject, '', newUrl.replace("#", ""));
	}
	
	// 更改菜单被选中样式
	$("#main-menu li a").each(function() {
		$(this).removeClass("active-menu");
	});
	$(object).addClass("active-menu");

	// 页面IFRAME地址跳转
	$("#main").attr('src', url);
}

$(document).ready(function() {
	// 如果页面按F5刷新，则自动从URL中取出当前页面地址进行重定向
	// var request = GetSelfRequest();
	// var redirect = request.redirect;
	let redirect = location.search.split('=')[1]
	if (redirect != null && redirect != "") {
		$("#main").attr('src', redirect);
	}
	
	// 清除上一次点击后被选中菜单的样式
	$("#main-menu li a").each(function() {
		$(this).removeClass("active-menu");
	});
})

