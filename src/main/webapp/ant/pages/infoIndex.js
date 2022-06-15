function changeSelectCss(object) {
    $("#tab li").find("a").removeClass("active");
    $(object).find("a").addClass("active");
}

function loadTab(id, type = 'net', redirect) {
    if (type == 'sof') {
        $.ajax({
            type: 'POST',
            dataType: "json",
            url: "/Soft/findById",
            data: {
                id: id
            },
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            timeout: 5000,
            cache: true,
            async: true,
            success: function (json) {
                $("#title").append(
                    '<span style="margin-right: 5px;color:gray">软件名称：'
                    + json.aname + '（'
                    + json.aip + ':' + json.aport + '）</span>');

                if (json.nmsAssetType.chType == "专用数据库") {
                    $("#tab").empty();
                    // 添加基础信息tab
            	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "databaseBasicInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'databaseBasicInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'databaseBasicInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">基础信息</a></li>';
            	    // 添加配置信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "databaseConfigInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'databaseConfigInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'databaseConfigInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">配置信息</a></li>';
            	    // 添加状态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "databaseStatusInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'databaseStatusInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'databaseStatusInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">状态信息</a></li>';
                    $("#tab").append(tab);
                }

                if (json.nmsAssetType.chType == "专用中间件") {
                    $("#tab").empty();
                    // 添加基础信息tab
            	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "middlewareBasicInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'middlewareBasicInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'middlewareBasicInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">基础信息</a></li>';
            	    // 添加配置信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "middlewareConfigInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'middlewareConfigInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'middlewareConfigInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">配置信息</a></li>';
            	    // 添加状态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "middlewareStatusInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'middlewareStatusInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'middlewareStatusInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">状态信息</a></li>';
                    $("#tab").append(tab);
                }

                $("li a").each(function () {
                    var url = $(this).attr("href");
                    var curUrl = location + "";
                    if (curUrl != null && curUrl.indexOf(url) != -1) {
                        $(this).addClass("active");
                    }
                });
            },
            beforeSend: function (xhr) {
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
    } else if (type == 'net') {
        $.ajax({
            type: 'POST',
            dataType: "json",
            url: "/Asset/findById",
            data: {
                id: id
            },
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            timeout: 5000,
            cache: true,
            async: true,
            success: function (json) {
                $("#title").append(
                    '<span style="margin-right: 5px;color:gray">设备名称：'
                    + json.aname + '（'
                    + json.aip + ' & ' + json.ano + '）</span>');

                if (json.nmsAssetType.chType == "专用数通设备"
                    || json.nmsAssetType.chType == "通用数通设备"
                    || json.nmsAssetType.chType == "专用网络安全设备") {
                    $("#tab").empty();
                    // 添加连通信息tab
            	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "pingInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'pingInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'pingInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">连通信息</a></li>';
            	    // 添加静态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "staticInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'staticInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'staticInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">静态信息</a></li>';
            	    // 添加动态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "dynamicInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'dynamicInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'dynamicInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">动态信息</a></li>';
            	    // 添加CPU信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "cpuInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'cpuInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'cpuInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">CPU信息</a></li>';
            	    // 添加内存信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "memInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'memInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'memInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">内存信息</a></li>';
            	    // 添加接口信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "netifInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'netifInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'netifInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">接口信息</a></li>';
                    $("#tab").append(tab);
                }

                if (json.nmsAssetType.chType == "专用终端" || json.nmsAssetType.chType == "专用工作站") {
                    $("#tab").empty();
                    // 添加连通信息tab
            	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "pingInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'pingInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'pingInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">连通信息</a></li>';
            	    // 添加静态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "staticInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'staticInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'staticInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">静态信息</a></li>';
            	    // 添加动态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "dynamicInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'dynamicInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'dynamicInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">动态信息</a></li>';
            	    // 添加CPU信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "cpuInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'cpuInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'cpuInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">CPU信息</a></li>';
            	    // 添加内存信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "memInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'memInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'memInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">内存信息</a></li>';
            	    // 添加文件分区信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "filesysInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'filesysInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'filesysInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">文件分区</a></li>';
            	    // 添加接口信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "netifInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'netifInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'netifInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">接口信息</a></li>';
            	    // 添加软件信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "softInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'softInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'softInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">软件信息</a></li>';
            	    // 添加账号信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "accountInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'accountInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'accountInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">账号信息</a></li>';
                    $("#tab").append(tab);
                }

                if (json.nmsAssetType.chType == "其它设备") {
                    $("#tab").empty();
                    // 添加连通信息tab
            	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "pingInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'pingInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'pingInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">连通信息</a></li>';
                    $("#tab").append(tab);
                }

                if (json.nmsAssetType.chType == "专用服务器"
                    || json.nmsAssetType.chType == "管理服务器"
                    || json.nmsAssetType.chType == "通用服务器") {
                    $("#tab").empty();
                    // 添加连通信息tab
            	    var tab = '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "pingInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'pingInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'pingInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">连通信息</a></li>';
            	    // 添加静态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "staticInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'staticInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'staticInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">静态信息</a></li>';
            	    // 添加动态信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "dynamicInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'dynamicInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'dynamicInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">动态信息</a></li>';
            	    // 添加CPU信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "cpuInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'cpuInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'cpuInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">CPU信息</a></li>';
            	    // 添加内存信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "memInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'memInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'memInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">内存信息</a></li>';
            	    // 添加文件分区信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "filesysInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'filesysInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'filesysInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">文件分区</a></li>';
            	    // 添加磁盘信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "diskioInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'diskioInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'diskioInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">磁盘信息</a></li>';
            	    // 添加接口信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "netifInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'netifInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'netifInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">接口信息</a></li>';
            	    // 添加进程信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "processInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'processInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'processInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">进程信息</a></li>';
            	    // 添加软件信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "softInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'softInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'softInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">软件信息</a></li>';
            	    // 添加账号信息tab
            	    tab += '<li class="tab col s3" style="margin-right: 5px;">';
                	if (redirect == "appInfo.html") {
                		tab += '<a target="_self" class="active" href="javascript:jumpPage(\'appInfo.html?id=';
                	} else {
                		tab += '<a target="_self" href="javascript:jumpPage(\'appInfo.html?id=';
                	}
            	    tab += json.id + '\')" style="text-decoration:none;">应用信息</a></li>';
                    $("#tab").append(tab);
                }
/*
                $("li a").each(function () {
                    var url = $(this).attr("href");
                    var curUrl = location + "";
                    if (curUrl != null && curUrl.indexOf(url) != -1) {
                        $(this).addClass("active");
                    }
                });
*/
            },
            beforeSend: function (xhr) {
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
}

