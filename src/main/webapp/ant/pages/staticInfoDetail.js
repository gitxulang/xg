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

$(document).ready(function() {
	var request = GetSelfRequest();
	var id = request.id;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/StaticInfo/detail",
		data : {id : id},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json == null) {
				$("body").html(showNoData(100,600));
			} else {
				loadPage(json, id);
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
				$("#body").html(showNoFunction(100, 600));
			}
		}
	});
})

function loadPage(json) {
	var request = parent.GetRequest();
	var id = request.id;
	if (id == null || id == "") {
		return;
	}
	var uniqueIdent = json.uniqueIdent != null ? json.uniqueIdent : '--';
	var productName = json.productName != null ? json.productName : '--';
	var manufacturer = json.manufacturer != null ? json.manufacturer : '--';
	var cpuInfo = json.cpuInfo != null ? json.cpuInfo : '--';
	var cpuInfoView = cpuInfo;
	if (cpuInfo.length > 4096) {
		cpuInfoView = cpuInfo.substring(0, 4096);
	}
	var diskSn = json.diskSn != null ? json.diskSn : '--';
	var sysName = json.sysName != null ? json.sysName : '--';
	var sysArch = json.sysArch != null ? json.sysArch : '--';
	var sysBits = json.sysBits != null ? json.sysBits : '--';
	var sysVersion = json.sysVersion != null ? json.sysVersion : '--';
	var coreVersion = json.coreVersion != null ? json.coreVersion : '--';
	var netNum = json.netNum != null ? json.netNum : '--';
	var cpuNum = json.cpuNum != null ? json.cpuNum : '--';
	var socVersion = json.socVersion != null ? json.socVersion : '--';
	var ioVersion = json.ioVersion != null ? json.ioVersion : '--';
	var itime = (json.itime != null && json.itime != '') ? format(json.itime) : '--';

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
            if (json.nmsAssetType.chType == "专用数通设备" || json.nmsAssetType.chType == "通用数通设备") {
            	var innerTable = 
            		'<tr>'+
						'<td width="30%" class="title_td">ASPID</td>'+
						'<td class="content_td">' + uniqueIdent + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">设备序列号</td>'+
						'<td class="content_td">' + productName + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">设备型号</td>'+
						'<td class="content_td">' + manufacturer + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">系统描述</td>'+
						'<td class="content_td">' + cpuInfoView + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">设备厂商对象标识OID</td>'+
						'<td class="content_td">' + diskSn + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">系统名称</td>'+
						'<td class="content_td">' + sysName + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">设备位置</td>'+
						'<td class="content_td">' + sysArch + '</td>'+
					'</tr>'+
/*					'<tr>'+
						'<td class="title_td">操作系统位数</td>'+
						'<td class="content_td">' + sysBits + '</td>'+
					'</tr>'+*/
					'<tr>'+
						'<td class="title_td">软件版本号</td>'+
						'<td class="content_td">' + sysVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">固件版本号</td>'+
						'<td class="content_td">' + coreVersion + '</td>'+
					'</tr>'+
/*					'<tr>'+
						'<td class="title_td">物理网口数</td>'+
						'<td class="content_td">' + netNum + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">CPU数</td>'+
						'<td class="content_td">' + cpuNum + '</td>'+
					'</tr>'+*/
					'<tr>'+
						'<td class="title_td">设备联系人</td>'+
						'<td class="content_td">' + socVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">硬件版本号</td>'+
						'<td class="content_td">' + ioVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">指标采集时间</td>'+
						'<td class="content_td">' + itime + '</td>'+
					'</tr>';
				$(".detail_table").append(innerTable);
            } else {
            	var innerTable = 
            		'<tr>'+
						'<td class="title_td">ASPID</td>'+
						'<td class="content_td">' + uniqueIdent + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">设备名称</td>'+
						'<td class="content_td">' + productName + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">制造商</td>'+
						'<td class="content_td">' + manufacturer + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">CPU信息</td>'+
						'<td class="content_td">' + cpuInfo + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">硬盘序列号</td>'+
						'<td class="content_td">' + diskSn + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">操作系统名称</td>'+
						'<td class="content_td">' + sysName + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">操作系统处理器架构</td>'+
						'<td class="content_td">' + sysArch + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">操作系统位数</td>'+
						'<td class="content_td">' + sysBits + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">操作系统版本</td>'+
						'<td class="content_td">' + sysVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">操作系统内核版本</td>'+
						'<td class="content_td">' + coreVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">物理网口数</td>'+
						'<td class="content_td">' + netNum + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">CPU数</td>'+
						'<td class="content_td">' + cpuNum + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">soc安全卡版本</td>'+
						'<td class="content_td">' + socVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">三合一模块版本</td>'+
						'<td class="content_td">' + ioVersion + '</td>'+
					'</tr>'+
					'<tr>'+
						'<td class="title_td">指标采集时间</td>'+
						'<td class="content_td">' + itime + '</td>'+
					'</tr>';
				$(".detail_table").append(innerTable);
            }
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
				$("#body").html(showNoFunction(100, 600));
			}
		}
    });	

}

function add0(m) {
	return m < 10 ? '0' + m : m;
}

function format(time_str) {
	var time = new Date(time_str);
	var y = time.getFullYear();
	var m = time.getMonth() + 1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}

