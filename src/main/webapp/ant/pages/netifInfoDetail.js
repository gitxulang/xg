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
	var request = GetRequest();
	var id = request.id;

	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/NetifInfo/detail",
		data: {id:id},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json == null) {
				$("body").html(showNoData(100,600))
			} else {
				loadPage(json);
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

function GetRequest() {
	var url = location.search; 
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i ++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

function loadPage(json){
	var ifDescr = json.ifDescr==''?'--':json.ifDescr;
	var ifIp = json.ifIp==''?'--':json.ifIp;
	var ifSubmask = json.ifSubmask==''?'--':json.ifSubmask;
	var ifGateway = json.ifGateway==''?'--':json.ifGateway;
	var ifPhysaddr = json.ifPhysaddr==''?'--':json.ifPhysaddr;
	
	var ifType;
	if (json.ifType == 6) {
		ifType = "以太网";
	} else if (json.ifType == 24) {
		ifType = "本地回环";
	} else if (json.ifType == null) {
		ifType = "--";
	} else {
		ifType = "其它";
	}
	
	var ifAdminStatus;
	if (json.ifAdminStatus == 1) {
		ifAdminStatus = "up";
	} else if (json.ifAdminStatus == 2) {
		ifAdminStatus = "down";
	} else if (json.ifOperStatus == null) {
		ifOperStatus = "--";
	} else {
		ifAdminStatus = "其它";
	}

	var ifOperStatus;
	if (json.ifOperStatus == 1) {
		ifOperStatus = "up";
	} else if (json.ifOperStatus == 2) {
		ifOperStatus = "down";
	} else if (json.ifOperStatus == null) {
		ifOperStatus = "--";
	} else {
		ifOperStatus = "其它";
	}	
	
	if (json.ifSpeed < 0) {
		json.ifSpeed = 0;
	}
	
	var innerTable = '<tr>'+
						'<td>网口索引</td>'+
						'<td class="content_td">'+json.ifIndex+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>网口描述</td>'+
						'<td class="content_td">'+ifDescr+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>网口类型</td>'+
						'<td class="content_td">'+ifType+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>传输MTU</td>'+
						'<td class="content_td">'+json.ifMtu+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>最大传输速率（Mb）</td>'+
						'<td class="content_td">'+json.ifSpeed+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>IP地址</td>'+
						'<td class="content_td">'+ifIp+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>子网掩码</td>'+
						'<td class="content_td">'+ifSubmask+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>网关地址</td>'+
						'<td class="content_td">'+ifGateway+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>物理地址</td>'+
						'<td class="content_td">'+ifPhysaddr+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>管理状态</td>'+
						'<td class="content_td">'+ifAdminStatus+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>运行状态</td>'+
						'<td class="content_td">'+ifOperStatus+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>收到字节（bytes）</td>'+
						'<td class="content_td">'+json.ifInOctets+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>收到单播包（个）</td>'+
						'<td class="content_td">'+json.ifInUcastpkts+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>收到广播包（个）</td>'+
						'<td class="content_td">'+json.ifInNucastpkts+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>因接收资源紧张丢弃包（个）</td>'+
						'<td class="content_td">'+json.ifInDiscards+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>因接收出错丢弃包（个）</td>'+
						'<td class="content_td">'+json.ifInErrors+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>发送字节（bytes）</td>'+
						'<td class="content_td">'+json.ifOutOctets+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>发送单播包（个）</td>'+
						'<td class="content_td">'+json.ifOutUcastpkts+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>发送广播包（个）</td>'+
						'<td class="content_td">'+json.ifOutNucastpkts+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>因发送资源紧张丢弃包（个）</td>'+
						'<td class="content_td">'+json.ifOutDiscards+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>因发送出错丢弃包（个）</td>'+
						'<td class="content_td">'+json.ifOutErrors+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>设备接收ICMP报文（个）</td>'+
						'<td class="content_td">'+json.ifInIcmps+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>设备发送ICMP报文（个）</td>'+
						'<td class="content_td">'+json.ifOutIcmps+'</td>'+
					'</tr>'+
					'<tr>'+
						'<td>采集时间</td>'+
						'<td class="content_td">'+format(json.itime)+'</td>'+
					'</tr>'
	$(".detail_table").append(innerTable);
}

function add0(m) {
	return m < 10 ? '0' + m : m
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