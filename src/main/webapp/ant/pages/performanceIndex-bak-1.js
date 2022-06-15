function changeSelectCss(object) {
    $("#tab li").find("a").removeClass("active");
    $(object).find("a").addClass("active");
}
const WIDTH = 1050, HEIGHT = 500
function loadTab(assetId, typeId, redirect) {
    if (typeId == 22) {//安全数据库
        $("#tab").empty();
        // 添加安全数据库tab
	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceDatabase.html") {
    		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceDatabase.html?id=';
    	} else {
    		tab += '<a target="_self" href="javascript:openDialog(\'performanceDatabase.html?id=';
    	}
		// tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">安全数据库</a></li>';
		// tab += ```${assetId}&typeid=${typeId}\', '安全数据库', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
		tab += assetId + '&typeid=' + typeId + '\', \'安全数据库\', 1050, 500)" style="text-decoration:none;">安全数据库</a></li>'
	    // 添加告警信息tab
	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceSoftAlarm.html") {
    		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceSoftAlarm.html?id=';
    	} else {
    		tab += '<a target="_self" href="javascript:openDialog(\'performanceSoftAlarm.html?id=';
    	}
		// tab += ```${assetId}&typeid=${typeId}\', '告警信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
		tab += assetId + '&typeid=' + typeId + '\', \'告警信息\', 1050, 500)" style="text-decoration:none;">告警信息</a></li>'
        $("#tab").append(tab);
    } else if (typeId == 23) {//安全中间件
        $("#tab").empty();
        // 添加安全中间件tab
	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceMiddleware.html") {
    		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceMiddleware.html?id=';
    	} else {
    		tab += '<a target="_self" href="javascript:openDialog(\'performanceMiddleware.html?id=';
    	}
		// tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">安全中间件</a></li>';
		// tab += ```${assetId}&typeid=${typeId}\', '安全中间件', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
		tab += assetId + '&typeid=' + typeId + '\', \'安全中间件\', 1050, 500)" style="text-decoration:none;">安全中间件</a></li>'
	    // 添加告警信息tab
	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
    	if (redirect == "performanceSoftAlarm.html") {
    		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceSoftAlarm.html?id=';
    	} else {
    		tab += '<a target="_self" href="javascript:openDialog(\'performanceSoftAlarm.html?id=';
    	}
		// tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">告警信息</a></li>';
		// tab += ```${assetId}&typeid=${typeId}\', '告警信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
		tab += assetId + '&typeid=' + typeId + '\', \'告警信息\', 1050, 500)" style="text-decoration:none;">告警信息</a></li>'
        $("#tab").append(tab);
    } else {
        // 其它设备
        if (typeId == 0) {
        	$("#tab").empty();
        	// 添加基础信息tab
     	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
        	if (redirect == "performanceDetail.html") {
        		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceDetail.html?id=';
        	} else {
        		tab += '<a target="_self" href="javascript:openDialog(\'performanceDetail.html?id=';
        	}
			// tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">基础信息</a></li>';
			// tab += ```${assetId}&typeid=${typeId}\', '基础信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'基础信息\', 1050, 500)" style="text-decoration:none;">基础信息</a></li>'
    	    $("#tab").append(tab);
        }

        // 专用终端,专用工作站
        if (typeId >= 1 && typeId <= 4) {
        	$("#tab").empty();
        	// 添加基础信息tab
			 var tab = '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceDetail.html") {
         		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceDetail.html?id=';
         	} else {
				tab += '<a target="_self" href="javascript:openDialog(\'performanceDetail.html?id=';
         	}
			 // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">基础信息</a></li>';
			//  tab += ```${assetId}&typeid=${typeId}\', 基础信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'基础信息\', 1050, 500)" style="text-decoration:none;">基础信息</a></li>'
//     	    // 添加性能信息tab
//     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
//         	if (redirect == "performanceStatus.html") {
//         		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceStatus.html?id=';
//         	} else {
//         		tab += '<a target="_self" href="javascript:openDialog(\'performanceStatus.html?id=';
//         	}
//     	    tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">性能信息</a></li>';        	 
     	    // 添加接口信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceInterface.html") {
         		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceInterface.html?id=';
         	} else {
         		tab += '<a target="_self" href="javascript:openDialog(\'performanceInterface.html?id=';
         	}
			 // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">接口信息</a></li>';   
			// tab += ```${assetId}&typeid=${typeId}\', '接口信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```        	 
			tab += assetId + '&typeid=' + typeId + '\', \'接口信息\', 1050, 500)" style="text-decoration:none;">接口信息</a></li>'
     	    // 添加软件信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceSoft.html") {
         		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceSoft.html?id=';
         	} else {
         		tab += '<a target="_self" href="javascript:openDialog(\'performanceSoft.html?id=';
         	}
			 // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">软件信息</a></li>';     
			//  tab += ```${assetId}&typeid=${typeId}\', '软件信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```  
			tab += assetId + '&typeid=' + typeId + '\', \'软件信息\', 1050, 500)" style="text-decoration:none;">软件信息</a></li>'   	 
     	    // 添加账号信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceAccount.html") {
         		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceAccount.html?id=';
         	} else {
         		tab += '<a target="_self" href="javascript:openDialog(\'performanceAccount.html?id=';
         	}
			 // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">账号信息</a></li>';  
			//  tab += ```${assetId}&typeid=${typeId}\', '账号信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'账号信息\', 1050, 500)" style="text-decoration:none;">账号信息</a></li>'
     	    // 添加告警信息tab
     	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
         	if (redirect == "performanceAlarm.html") {
         		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceAlarm.html?id=';
         	} else {
         		tab += '<a target="_self" href="javascript:openDialog(\'performanceAlarm.html?id=';
         	}
			 // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">告警信息</a></li>';
			//  tab += ```${assetId}&typeid=${typeId}\', '告警信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'告警信息\', 1050, 500)" style="text-decoration:none;">告警信息</a></li>'
     	    $("#tab").append(tab);
        }

        // 专用服务器,管理服务器,通用服务器
        if (typeId >= 5 && typeId <= 10) {
        	$("#tab").empty();
         	// 添加基础信息tab
      	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceDetail.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceDetail.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceDetail.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">基础信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '基础信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'基础信息\', 1050, 500)" style="text-decoration:none;">基础信息</a></li>'
//      	    // 添加性能信息tab
//      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
//          	if (redirect == "performanceStatus.html") {
//          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceStatus.html?id=';
//          	} else {
//          		tab += '<a target="_self" href="javascript:openDialog(\'performanceStatus.html?id=';
//          	}
//      	    tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">性能信息</a></li>';        	 
      	    // 添加接口信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceInterface.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceInterface.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceInterface.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">接口信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '接口信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'接口信息\', 1050, 500)" style="text-decoration:none;">接口信息</a></li>'
      	    // 添加进程信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceProcess.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceProcess.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceProcess.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">进程信息</a></li>';     
			//   tab += ```${assetId}&typeid=${typeId}\', '进程信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'进程信息\', 1050, 500)" style="text-decoration:none;">进程信息</a></li>'
      	    // 添加软件信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceSoft.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceSoft.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceSoft.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">软件信息</a></li>';   
			//   tab += ```${assetId}&typeid=${typeId}\', '软件信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```  
			tab += assetId + '&typeid=' + typeId + '\', \'软件信息\', 1050, 500)" style="text-decoration:none;">软件信息</a></li>'     	 
      	    // 添加应用信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceApp.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceApp.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceApp.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">应用信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '应用信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```  
			tab += assetId + '&typeid=' + typeId + '\', \'应用信息\', 1050, 500)" style="text-decoration:none;">应用信息</a></li>'
      	    // 添加告警信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceAlarm.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceAlarm.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceAlarm.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">告警信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '告警信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'告警信息\', 1050, 500)" style="text-decoration:none;">告警信息</a></li>'
      	    $("#tab").append(tab);
        }
    	// 专用数通设备,通用数通设备,专用网络安全设备
        if (typeId >= 11 && typeId <= 21) {
        	$("#tab").empty();
         	// 添加基础信息tab
      	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceDetail.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceDetail.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceDetail.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">基础信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '基础信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'基础信息\', 1050, 500)" style="text-decoration:none;">基础信息</a></li>'
//      	    // 添加性能信息tab
//      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
//          	if (redirect == "performanceStatus.html") {
//          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceStatus.html?id=';
//          	} else {
//          		tab += '<a target="_self" href="javascript:openDialog(\'performanceStatus.html?id=';
//          	}
//      	    tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">性能信息</a></li>';        	 
      	    // 添加接口信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceInterface.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceInterface.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceInterface.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">接口信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '接口信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			  tab += assetId + '&typeid=' + typeId + '\', \'接口信息\', 1050, 500)" style="text-decoration:none;">接口信息</a></li>'
      	    // 添加告警信息tab
      	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
          	if (redirect == "performanceAlarm.html") {
          		tab += '<a target="_self" class="active" href="javascript:openDialog(\'performanceAlarm.html?id=';
          	} else {
          		tab += '<a target="_self" href="javascript:openDialog(\'performanceAlarm.html?id=';
          	}
			  // tab += assetId + '&typeid=' + typeId + '\')" style="text-decoration:none;">告警信息</a></li>';
			//   tab += ```${assetId}&typeid=${typeId}\', '告警信息', ${WIDTH}, ${HEIGHT})" style="text-decoration:none;"></a></li>```
			tab += assetId + '&typeid=' + typeId + '\', \'告警信息\', 1050, 500)" style="text-decoration:none;">告警信息</a></li>'
      	    $("#tab").append(tab);
        }
    }

    $("li a").each(function () {
        var url = $(this).attr("href");
        var curUrl = location + "";
        if (curUrl != null && curUrl.indexOf(url) != -1) {
            $(this).addClass("active");
        }
    });
}

