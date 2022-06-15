function changeSelectCss(object) {
    $("#tab li").find("a").removeClass("active");
    $(object).find("a").addClass("active");
}
const WIDTH = 1050, HEIGHT = 500
function loadTab(assetId, typeId, redirect) {
//	console.info(redirect)
    if (typeId == 22) {//安全数据库
        $("#tab").empty();
        // 添加安全数据库tab
	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceDatabase.html") {
    		tab += '<a target="_self" class="active" ';
    	} else {
    		tab += '<a target="_self" ';
    	}
		tab += 'style="text-decoration:none;">安全数据库</a></li>'
	    // 添加告警信息tab
	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceSoftAlarm.html") {
    		tab += '<a target="_self" class="active" ';
    	} else {
    		tab += '<a target="_self" ';
    	}
		tab += 'style="text-decoration:none;">告警信息</a></li>'
        $("#tab").append(tab);
    } else if (typeId == 23) {//安全中间件
        $("#tab").empty();
        // 添加安全中间件tab
	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceMiddleware.html") {
    		tab += '<a target="_self" class="active" ';
    	} else {
    		tab += '<a target="_self" ';
    	}
		
		tab += 'style="text-decoration:none;">安全中间件</a></li>'
	    // 添加告警信息tab
	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceSoftAlarm.html") {
    		tab += '<a target="_self" class="active" ';
    	} else {
    		tab += '<a target="_self" ';
    	}
		
		tab += 'style="text-decoration:none;">告警信息</a></li>'
        $("#tab").append(tab);
    } else {
        // 其它设备
        if (typeId == 0) {
        	$("#tab").empty();
        	// 添加基础信息tab
     	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
        	if (redirect == "performanceDetail.html") {
        		tab += '<a target="_self" class="active" ';
        	} else {
        		tab += '<a target="_self" ';
        	}
			tab += 'style="text-decoration:none;">基础信息</a></li>'
    	    $("#tab").append(tab);
        }

        // 专用终端,专用工作站
        if (typeId >= 1 && typeId <= 4) {
        	$("#tab").empty();
        	// 添加基础信息tab
			 var tab = '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceDetail.html") {
         		tab += '<a target="_self" class="active" ';
         	} else {
				tab += '<a target="_self" ';
         	}
			tab += 'style="text-decoration:none;">基础信息</a></li>'    	 
     	    // 添加接口信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceInterface.html") {
         		tab += '<a target="_self" class="active" ';
         	} else {
         		tab += '<a target="_self" ';
			 }
			 tab += 'style="text-decoration:none;">接口信息</a></li>'
     	    // 添加软件信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceSoft.html") {
         		tab += '<a target="_self" class="active" ';
         	} else {
         		tab += '<a target="_self" ';
			 }
			 tab += ' style="text-decoration:none;">软件信息</a></li>'   	 
     	    // 添加账号信息tab
//     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
//         	if (redirect == "performanceAccount.html") {
//         		tab += '<a target="_self" class="active" ';
//         	} else {
//         		tab += '<a target="_self" ';
//         	}
//			tab += ' style="text-decoration:none;">账号信息</a></li>'
     	    // 添加告警信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceAlarm.html") {
         		tab += '<a target="_self" class="active" ';
         	} else {
         		tab += '<a target="_self" ';
         	}
			 tab += ' style="text-decoration:none;">告警信息</a></li>'
     	    $("#tab").append(tab);
        }

        // 专用服务器,管理服务器,通用服务器
        if (typeId >= 5 && typeId <= 10) {
        	$("#tab").empty();
         	// 添加基础信息tab
      	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceDetail.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">基础信息</a></li>'
			// 添加接口信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceInterface.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">接口信息</a></li>'
      	    // 添加进程信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceProcess.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">进程信息</a></li>'
      	    // 添加软件信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceSoft.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">软件信息</a></li>'     	 
      	    // 添加应用信息tab
//      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
//          	if (redirect == "performanceApp.html") {
//          		tab += '<a target="_self" class="active" ';
//          	} else {
//          		tab += '<a target="_self" ';
//          	}
//			tab += ' style="text-decoration:none;">应用信息</a></li>'
      	    // 添加告警信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceAlarm.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">告警信息</a></li>'
      	    $("#tab").append(tab);
        }
    	// 专用数通设备,通用数通设备,专用网络安全设备
        if (typeId >= 11 && typeId <= 21) {
        	$("#tab").empty();
         	// 添加基础信息tab
      	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceDetail.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">基础信息</a></li>'    	 
      	    // 添加接口信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceInterface.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">接口信息</a></li>'
      	    // 添加告警信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceAlarm.html") {
          		tab += '<a target="_self" class="active" ';
          	} else {
          		tab += '<a target="_self" ';
          	}
			tab += ' style="text-decoration:none;">告警信息</a></li>'
      	    $("#tab").append(tab);
        }
    }

    $("li a").each(function (i, d) {
        var url = $(this).attr("href");
        var curUrl = location + "";
        if (curUrl != null && curUrl.indexOf(url) != -1) {
            $(this).addClass("active");
		}
		if($(d).attr('target') == '_self') {
			$(d).on('click', function(){
				window.parent.$('.performw').hide()
				window.parent.$('.performw' + [i - 3]).show()
			})
		}
	});
}

