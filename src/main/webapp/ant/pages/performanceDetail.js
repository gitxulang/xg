
function showNoData(width, height, msg) {
	if (msg == null ) {
		msg = "";
	}
	return "<div style='width:"
		+ width
		+ "%;height:"
		+ height
		+ "px;color: #00ff00;text-align:center; font-size: 24px; line-height: "
		+ height
		+ "px;' >这里暂时没有" + msg + "数据</div>";			
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
	// var request = GetRequest();
	let request = GetSelfRequest()
	var id = request.id;
	var typeid = request.typeid;
	var redirect = request.redirect;
	if (id == null || id == "" || typeid == null || typeid == "" || redirect == null || redirect == "") {
		return;
	}
	$("#assetId").val(id);
	$("#typeId").val(typeid);

	var onlineStatus = 0

	loadTab(id, typeid, redirect);
	loadDevice(typeid);
	// showCpuRate();
	// showMemRate();
	// showPingRate();
	
	// if (parseInt(typeid) >= 1 && parseInt(typeid) <= 9) {
	// 	if(onlineStatus) {
	// 		$("#disk_div").show()
	// 		showDiskBar()
	// 	} else {
	// 		$("#disk_div").hide()
	// 	}
	// 	// showDiskBar();
	// } else {
	// 	$("#disk_div").remove();
	// 	$("#cpu_div").attr('class', 'col-md-6');
	// 	$("#memory_div").attr('class', 'col-md-6');
	// }
})

function loadDevice(typeid) {
	var id = $("#assetId").val();
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/Asset/SystermInfo/Overview",
		data: {assetId:id},
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 10000,
		cache: true,
		async: true,
		success: function(json) {
			if (json != null) {
				loadTable(json, typeid);
			} else {
				$("#tID").append(showNoData(100,517));
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
				$("#tID").append(showNoFunction(100,517));
			}
		}
	});
}

function loadTable(resp, typeid) {
	var showInfo = '';
	var npr = resp.npr;
	var json = resp.nd;
	if (json.sysName == null) {
		showInfo = '--';
		json.sysName = '--';
	} else {
		showInfo = json.sysName.substring(0, 50);
	}	
	
	var showSysInfo = '';
	if (json.sysInfo == null) {
		showSysInfo = '--';
		json.sysInfo = '--';
	} else {
		showSysInfo = json.sysInfo.substring(0, 50);
	}	
	if (json.assetPosition == null) {
    	json.assetPosition = '--';
    }

	onlineStatus = Number(npr.online)

	var pingRate = Number(npr.online) == 1 ? parseInt(npr.pingRate) * 100 : '--';
    var cpuRate = Number(npr.online) == 1 ? Number(npr.cpuRate).toFixed(2) : '--';
    var memRate = Number(npr.online) == 1 ? ((1 - (Number(npr.memFree) / Number(npr.memTotal))) * 100).toFixed(2)=='NaN'?'0.00':((1 - (Number(npr.memFree) / Number(npr.memTotal))) * 100).toFixed(2) : '--';
	var innerTable = '';
	innerTable += '<table class="table">';
	innerTable += '<tbody>';
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col" width="16%">设备名称</td>';
	innerTable += '<td class="success">' + json.assetName + '</td>';
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">系统简介</td>';
	innerTable += '<td class="success" title="' + json.sysName + '">' + showInfo + '</td>';
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">告警状态</td>';
	if (json.statusCode == 1) {
		innerTable += '<td class="success"><font color="green">不存在告警记录</font></td>';
	} else {
		innerTable += '<td class="success"><font color="red">存在告警记录</font></td>';
	}
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">IP地址</td>';
	innerTable += '<td class="success">' + json.assetIP + '</td>';
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">子网掩码</td>';
	if (json.assetIfSubmask == null) {
		innerTable += '<td class="success">--</td>';
	} else {
		innerTable += '<td class="success">' + json.assetIfSubmask + '</td>';
	}
	innerTable += '</tr>';
	
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">类型/类别</td>';
	innerTable += '<td class="success">' + json.assetType + ' / ' + json.assetSubtype + '</td>';
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '	<td class="my-table-col">系统描述</td>';
	if (json.sysInfo == null) {
		innerTable += '<td class="success">--</td>';
	} else {
		innerTable += '<td class="success" title="' + json.sysInfo + '">' + showSysInfo + '</td>';
	}
	innerTable += '</tr>';
			
	var upTime = '';
	if (json.sysUptime != null) {
		if (parseInt(json.sysUptime).toString() != 'NaN') {
			var timestamp = json.itime;
			timestamp = timestamp - parseInt(json.sysUptime) * 1000;
			upTime = format(timestamp);
		}
	}
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">启动时间</td>';
	innerTable += '<td class="success">' + upTime + '</td>';
	innerTable += '</tr>';
		
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">ASPID</td>';
	innerTable += '<td class="success">' + json.assetNo + '</td>';
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">设备位置</td>';
	innerTable += '<td class="success">' + (json.assetPosition==null?"":json.assetPosition)+ '</td>';
	innerTable += '</tr>';
	
	var mstatus = '';
	if (json.colled == 0){
		mstatus = "已监控"
	} else {
		mstatus = "不监控"
	}
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">监控状态</td>';
	innerTable += '<td class="success">' + mstatus + '</td>';
	innerTable += '</tr>';
	
	
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">进程总数</td>';
	innerTable += '<td class="success">' + json.processNum + ' 个进程</td>';
	innerTable += '</tr>';
	
	innerTable += '<tr>';
	// innerTable += '<td class="my-table-col">系统唯一标识</td>';
	// if (json.uniqueIdent == null) {
	// 	innerTable += '<td class="success">--</td>';
	// } else {
	// 	innerTable += '<td class="success">' + json.uniqueIdent + '</td>';
	// }
	innerTable += '</tr>';
			
	innerTable += '<tr>';
	innerTable += '<td class="my-table-col">本次查看时间</td>';
	innerTable += '<td class="success">' + format(new Date().getTime()) + '</td>';
	innerTable += '</tr>';
			
	if (onlineStatus == 1) {
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">设备在线状态</td>';
		innerTable += '<td class="success" style="color:green;">在线</td>';
		innerTable += '</tr>';

		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">设备连通率(%)</td>';
		innerTable += '<td class="success">' + pingRate + '</td>';
		innerTable += '</tr>';
		
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">CPU利用率(%)</td>';
		innerTable += '<td class="success">' + cpuRate + '</td>';
		innerTable += '</tr>';
		
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">内存使用率(%)</td>';
		innerTable += '<td class="success">' + memRate + '</td>';
		innerTable += '</tr>';
	} else {
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">设备在线状态</td>';
		innerTable += '<td class="success" style="color:red;">离线</td>';
		innerTable += '</tr>';
	}
	
	
	
	innerTable += '</tbody>';
	innerTable += '</table>	';
	
	$("#tID").append(innerTable);

	if (parseInt(typeid) >= 1 && parseInt(typeid) <= 9) {
		if(onlineStatus) {
			$("#disk_div").show()
			showDiskBar()
		} else {
			$("#disk_div").hide()
		}
		// showDiskBar();
	} else {
		$("#disk_div").remove();
		$("#cpu_div").attr('class', 'col-md-6');
		$("#memory_div").attr('class', 'col-md-6');
	}
}
function loadTable1(json) {
    var showInfo = '';
    if (json.sysName == null) {
        showInfo = '--';
        json.sysName = '--';
    } else {
        showInfo = json.sysName.substring(0, 50);
    }

    var showSysInfo = '';
    if (json.sysInfo == null) {
        showSysInfo = '--';
        json.sysInfo = '--';
    } else {
        showSysInfo = json.sysInfo.substring(0, 50);
    }

	onlineStatus = json.online
	console.info(onlineStatus, json.online, json)

    var innerTable = '';
    innerTable += '<table class="table">';
    innerTable += '<tbody>';
    innerTable += '<tr>';
    innerTable += '<td class="my-table-col" width="16%">设备名称</td>';
    innerTable += '<td class="success">' + json.assetName + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">系统简介</td>';
    innerTable += '<td class="success" title="' + json.sysName + '">' + showInfo + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">告警状态</td>';
    if (json.statusCode == 1) {
        innerTable += '<td class="success"><font color="green">不存在告警记录</font></td>';
    } else {
        innerTable += '<td class="success"><font color="red">存在告警记录</font></td>';
    }
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">IP地址</td>';
    innerTable += '<td class="success">' + json.assetIP + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">子网掩码</td>';
    if (json.assetIfSubmask == null) {
        innerTable += '<td class="success">--</td>';
    } else {
        innerTable += '<td class="success">' + json.assetIfSubmask + '</td>';
    }
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">类型/类别</td>';
    innerTable += '<td class="success">' + json.assetType + ' / ' + json.assetSubtype + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '	<td class="my-table-col">系统描述</td>';
    if (json.sysInfo == null) {
        innerTable += '<td class="success">--</td>';
    } else {
        innerTable += '<td class="success" title="' + json.sysInfo + '">' + showSysInfo + '</td>';
    }
    innerTable += '</tr>';

    var upTime = '';
    if (json.sysUptime != null) {
        if (parseInt(json.sysUptime).toString() != 'NaN') {
            var timestamp = json.itime;
            timestamp = timestamp - parseInt(json.sysUptime) * 1000;
            upTime = format(timestamp);
        }
    }
    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">启动时间</td>';
    innerTable += '<td class="success">' + upTime + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">ASPID</td>';
    innerTable += '<td class="success">' + json.assetNo + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">设备位置</td>';
    innerTable += '<td class="success">' + json.assetPosition + '</td>';
    innerTable += '</tr>';

    var mstatus = '';
    if (json.colled == 0) {
        mstatus = "已监控"
    } else {
        mstatus = "不监控"
    }
    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">监控状态</td>';
    innerTable += '<td class="success">' + mstatus + '</td>';
    innerTable += '</tr>';


    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">进程总数</td>';
    innerTable += '<td class="success">' + json.processNum + ' 个进程</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    // innerTable += '<td class="my-table-col">系统唯一标识</td>';
    // if (json.uniqueIdent == null) {
    //     innerTable += '<td class="success">--</td>';
    // } else {
    //     innerTable += '<td class="success">' + json.uniqueIdent + '</td>';
    // }
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">本次查看时间</td>';
    innerTable += '<td class="success">' + format(new Date().getTime()) + '</td>';
	innerTable += '</tr>';
	
	if (Number(json.online) == 1) {
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">设备在线状态</td>';
		innerTable += '<td class="success" style="color:green;">在线</td>';
		innerTable += '</tr>';

		let pingRate = Number(json.online) == 1 ? parseInt(json.pintRate) * 100 : '--';
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">设备连通率(%)</td>';
		innerTable += '<td class="success">' + pingRate + '</td>';
		innerTable += '</tr>';
		
		let cpuRate = Number(json.online) == 1 ? json.cpuRate.toFixed(2) : '--'
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">CPU利用率(%)</td>';
		innerTable += '<td class="success">' + cpuRate + '</td>';
		innerTable += '</tr>';
		
		let memRate = Number(json.online) == 1 ? ((1 - (json.memFree / json.memTotal)) * 100).toFixed(2)=='NaN'?'0.00':((1 - (json.memFree / json.memTotal)) * 100).toFixed(2) : '--';
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">内存使用率(%)</td>';
		innerTable += '<td class="success">' + memRate + '</td>';
		innerTable += '</tr>';
	} else {
		innerTable += '<tr>';
		innerTable += '<td class="my-table-col">设备在线状态</td>';
		innerTable += '<td class="success" style="color:red;">离线</td>';
		innerTable += '</tr>';
	}
	

    innerTable += '</tbody>';
    innerTable += '</table>	';

    $("#tID").append(innerTable);
}

function add0(m) {
	return m < 10 ? '0' + m : m 
}

function format(time_str) {
	var time = new Date(time_str);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}

function rtt(rtt, rootPath) {
	rtt += "";
	if (rtt == null && rtt != 0 || rtt == "") {
		return "<div style='color: #00ff00;text-align:center;font-size:24px;line-height:250px;'>这里暂时没有数据</div>";
	}
	rtt = Math.round(rtt, 0);
	var rttXml = "";
	
	if (rtt/999999 > 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_y.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_9.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_9.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_9.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_9.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_9.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_9.png" width="31" height="58"/></li>';
	} else if (rtt == 0) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_x.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_1.png" width="31" height="58"/></li>';
	} else if (rtt/100000 >= 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/100000,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10000,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/1000,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/100,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt%10 + '.png" width="31" height="58"/></li>';
	} else if (rtt/10000 >= 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10000,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/1000,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/100,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt%10 + '.png" width="31" height="58"/></li>';
	} else if (rtt/1000 >= 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/1000,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/100,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt%10 + '.png" width="31" height="58"/></li>';
	} else if (rtt/100 >= 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/100,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt%10+'.png" width="31" height="58"/></li>';
	} else if (rtt/10 >= 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt/10,0)%10 + '.png" width="31" height="58"/></li>';
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt%10 + '.png" width="31" height="58"/></li>';
	} else if (rtt >= 1) {
		rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt%10 + '.png" width="31" height="58"/></li>';
	}
	
	rttXml += '<li class="shu"><img src="./assets/img/shu_u.png" width="31" height="58" /></li>';
	rttXml += '<li class="shu"><img src="./assets/img/shu_s.png" width="31" height="58" /></li>';
	
	return rttXml;
}

function dash_board(show_id, show_name, show_unit, value) {
	if (value == null || value == "") {
		document.getElementById(show_id).innerHTML =  showNoData(document.getElementById(show_id).offsetWidth, document.getElementById(show_id).offsetHeight); 
		return;
	}			
	
	var myChart = echarts.init(document.getElementById(show_id));
	myChart.title = '利用率';	
	option = {
		backgroundColor: '#f0f0f0',
		
	    tooltip : {
	        formatter: "{b} : {c}%",
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    series : [
	        {
	        	name: show_unit,
	            type:'gauge',
                min: 0,
                max: 100,
                splitNumber: 10,
                radius: '90%',
                
            	axisLine: {
                    lineStyle: {
                        color: [
                            [0.09, 'lime'],
                            [0.82, '#1090ff'],
                            [1, '#ff4500']
                        ],
                        width: 18,
                //      shadowColor: '#fff',
                        shadowBlur: 10
                    }
                },
                
                axisLabel: {
                    textStyle: {
                        fontWeight: 'bolder',
                 //     color: '#fff',
                 //     shadowColor: '#fff',
                        shadowBlur: 10
                    }
                },
                
                axisTick: {
               //   length: 15, 
                    lineStyle: {
                   // 	color: 'auto',
                   //   shadowColor: '#fff',
                   // 	shadowBlur: 10
                    }
                },
                
                splitLine: {
                 // length: 25,
                    lineStyle: {
                 //		 width: 3,
                 //     color: '#fff',
                 //     shadowColor: '#fff',
                        shadowBlur: 10
                    }
                },
                
                pointer: {
                    shadowColor: '#fff', 
                    shadowBlur: 5
                },
                
                title: {
                    textStyle: {
                        fontWeight: 'bolder',
                        fontSize: 16,
                    //  fontStyle: 'italic',
                    //  color: '#0000f0',
                    //  shadowColor: '#fff',
                        shadowBlur: 10
                    }
                },
                
                detail: {
                	backgroundColor: 'rgba(25,144,255,0.1)',
                	borderWidth: 1,
             //   	borderColor: '#fff',
             //   	shadowColor: '#fff',
                    shadowBlur: 5,
                    offsetCenter: [0, '70%'],
                    width: 90,
                    height: 30,
                    textStyle: {
                        fontWeight: 'bolder',
                        fontSize: 18,
                        textAlign: 'right',
                    }
                },		        
	        
	            data:[{value: value, name: show_name}]
	        }
	    ]
	};
	myChart.setOption(option);	
}

function cpu_bar_graph(current_avg_cpu, current_day_cpu_avg, current_day_cpu_max) {
	if (current_avg_cpu == null || current_avg_cpu == "" ||
		current_day_cpu_avg == null || current_day_cpu_avg == "" ||
		current_day_cpu_max == null || current_day_cpu_max == "") {
		
		document.getElementById("cpu").innerHTML =  showNoData(document.getElementById("cpu").offsetWidth, document.getElementById("cpu").offsetHeight); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('cpu'));
	myChart.title = '当日CPU信息';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '30%',
		    bottom: '10%'
		},
		
		title: {
			text: '当日CPU利用率统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
		
	    legend: {
	        x: 'left',
	        data:['实时', '平均', '最大'],	        
	    },
		
		tooltip: {
			trigger: 'axis'
		},
		
	    toolbox: {
			show: false,
			feature: {
				dataView: {show: true, readOnly: false},
				magicType: {show: true, type: ['line', 'bar']},
				restore: {show: true},
				saveAsImage: {show: true}
			}
		},
	    
	    calculable: true,
	    
	    xAxis: [
	        {	
	        	type: 'category',
	            data : ['CPU利用率'],
	        }
	    ],
	    
	    yAxis: [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value}%',
				},
				boundaryGap: false,
				splitLine: {
					show: true, 
				},
			}
		],
	    
	    tooltip: {
	        trigger: 'bar',
	        formatter: "{a}<br/>CPU使用率:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	            barWidth: 80,
	            data: [current_avg_cpu],
	            
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
                barMinHeight:2,
	        },
	        {
	            name: '平均',
	            type: 'bar',
	            barWidth: 80,
	            data: [current_day_cpu_avg],
	            
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
                barMinHeight:2,
	        },
	        {
	            name: '最大',
	            type: 'bar',
	            barWidth: 80,
	            data: [current_day_cpu_max],
	            
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
                barMinHeight:2,
	        }
	    ]
	};
	myChart.setOption(option);	
}

function mem_bar_graph(current_physical_mem, others_mem_label, 
						current_avg_physical_mem, current_avg_others_mem,
						current_day_physical_mem_avg, current_day_others_mem_avg,
						current_day_physical_mem_max, current_day_others_mem_max) {
	if (current_avg_physical_mem == null || current_avg_physical_mem == "" ||
		current_avg_others_mem == null || current_avg_others_mem == "" ||
		current_day_physical_mem_avg == null || current_day_physical_mem_avg == "" ||
		current_day_others_mem_avg == null || current_day_others_mem_avg == "" ||
		current_day_physical_mem_max == null || current_day_physical_mem_max == "" ||
		current_day_others_mem_max == null || current_day_others_mem_max == "") {
		
		document.getElementById("memory").innerHTML =  showNoData(document.getElementById("memory").offsetWidth, document.getElementById("memory").offsetHeight); 
		return;
	}	
	
	var myChart = echarts.init(document.getElementById('memory'));
	myChart.title = '当日内存信息';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '30%',
		    bottom: '10%'
		},
				
		title: {
			text: '当日内存利用率统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
		
	    legend: {
	        x: 'left',
	        data:['实时', '平均', '最大'],	        
	    },
	    
	    toolbox: {
	        show : false,
	    },
	    
	    xAxis: [
	        {	
	        	show: true,
	            data : ['物理内存利用率', '其它内存利用率'],
	        }
	    ],
	    
	    yAxis: [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value}%',
				},
				boundaryGap: false,
				splitLine: {
					show: true, 
				},
			}
		],
		
	    calculable: true,
	    
	    tooltip: {
	        trigger: 'bar',
	        formatter: "{a}<br/>内存使用率:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	            data: [current_day_physical_mem_avg, current_avg_others_mem],
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
	            barMinHeight:2,		
	        },
	        
	        {
	            name: '平均',
	            type: 'bar',
	            data: [current_avg_physical_mem, current_day_others_mem_avg],
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
                barMinHeight:2,		     
	        },
	        
	        {
	            name: '最大',
	            type: 'bar',
	            data: [current_day_physical_mem_max, current_day_others_mem_max],
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
                barMinHeight:2,		     
	        }
	    ]
	};
	myChart.setOption(option);	
}

function sortCurrentPartTotal(a,b) {
	return b.currentPartTotal - a.currentPartTotal;
}

function disk_bar_graph(disk_object_array, max) {
	if (disk_object_array == null || disk_object_array.length == 0) {
		document.getElementById("disk_chart").innerHTML =  showNoData(document.getElementById("disk_chart").offsetWidth, document.getElementById("disk_chart").offsetHeight); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('disk_chart'));
	myChart.title = '文件系统分区信息';

	var desc_data = [];
	var data_array = [];
	
	for (var i = 0; i < disk_object_array.length; i++) {
		var object = disk_object_array[i];
		desc_data[desc_data.length] = object.desc;
		var data = [];
		data[data.length] = disk_object_array[i].value;		
		data_array[data_array.length] = data;
	}

	option = {
		// backgroundColor: '#f0f0f0',
		backgroundColor: 'rgb(21,27,48)',
		
		grid: {
		    // top: '30%',
			// bottom: '10%'
			show: false
		},
				
		title: {
			text: '实时文件分区使用率统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
				color: "#fff"
			}
		},	
		
	    legend: {
	        x: 'left',
			data: desc_data,	
			textStyle: {
				color: "#fff"
			}        
	    },
	    
	    toolbox: {
	        show : false,
	    },
	    
	    yAxis: [
	        {	
	        	show: false,
	            data : ['文件系统分区使用率'],
	        }
	    ],
	    
	    xAxis: [
	        {
	        	show: true,
				type: 'value',
				axisLine : {
					onZero: false,
					lineStyle:{
						color:'#fff'
					}
				},
				min: 0,
				max: max,//100,
				axisLabel: {
					formatter: '{value}%',
				},
				boundaryGap: false,
				splitLine: {
					show: true, 
				}
			}
		],
		
	    calculable: true,
	    
	    tooltip: {
	        trigger: 'bar',
	        formatter: "{a}<br/>分区已用率:{c}%",
	    },		    
	        
	    series: [
	        {
	            name: desc_data[0],
	            type: 'bar',
	            data: data_array[0],
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
	            barMinHeight:2,		
	        },
	    ]
	};
	
	for (i = 1; i < disk_object_array.length; i++) {
		var itme = {};
		itme.name = desc_data[i];
		itme.type = 'bar';
		itme.data = data_array[i];
		
		itme.markPoint = {};
		var objMax = {};
		objMax.type = 'max';
		objMax.name = '最大值';
		var objMin = {};
		objMin.type = 'min';
		objMin.name = '最小值';		
		var data = [];
		data.push(objMax);
		data.push(objMin);
		itme.markPoint.data = data;
		itme.barMinHeight = 2;
		option.series.push(itme);
	}
	myChart.setOption(option);	
}

function showCpuRate() {
	var id = $("#assetId").val();
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/CpuInfo/ServerDetail/PerformanceInfoV02",
		data: {assetId:id},
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("#current_cpu_rate").append(showNoData(100, 250, "实时CPU"));
				$("#cpu").append(showNoData(100, 250, "当日CPU"));
			} else {
				if (json.cpuData.currentCpuRate == null) {
					$("#current_cpu_rate").append(showNoData(100, 250, "实时CPU"));
					$("#cpu").append(showNoData(100, 250, "当日CPU"));
				} else {
					dash_board("current_cpu_rate", "CPU使用率", "CPU(%)", json.cpuData.currentCpuRate.toFixed(2) + "");
					cpu_bar_graph(json.cpuData.currentCpuRate.toFixed(2), json.cpuData.averageCpuRateOfToday.toFixed(2), json.cpuData.maxCpuRateOfToday.toFixed(2));
				}
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
				$("#current_cpu_rate").append(showNoFunction(100, 250));
				$("#cpu").append(showNoFunction(100, 250));
			}
		}
	});
}

function showMemRate() {
	var id = $("#assetId").val();
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/MemInfo/ServerDetail/MemInfoV02",
		data: {assetId:id},
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("#current_mem_rate").append(showNoData(100, 250, "实时CPU"));
				$("#memory").append(showNoData(100, 250, "当日CPU"));
			} else {
				if (json.memData.currentMemRate == null) {
					$("#current_mem_rate").append(showNoData(100, 250, "实时CPU"));
					$("#memory").append(showNoData(100, 250, "当日CPU"));
				} else {
					dash_board("current_mem_rate", "内存使用率", "内存(%)", (json.memData.currentMemRate*100).toFixed(2));
					mem_bar_graph("物理内存", "交换内存", (json.memData.currentMemRate*100).toFixed(2), (json.nmsSwapData.currentSwapRate*100).toFixed(2), (json.memData.averageMemRateOfToday*100).toFixed(2), (json.nmsSwapData.averageSwapRateOfToday*100).toFixed(2), (json.memData.maxMemRateOfToday*100).toFixed(2), (json.nmsSwapData.maxSwapRateOfToday*100).toFixed(2));
				}
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
				$("#current_mem_rate").append(showNoFunction(100, 250));
				$("#memory").append(showNoFunction(100, 250));
			}
		}
	});
}

function showPingRate() {
	var id = $("#assetId").val();
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/PingInfo/ServerDetail/PerformanceInfoV02",
		data: {assetId:id},
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("#current_ping_rate").append(showNoData(100, 250));
				$("#current_ping_rtt").append(showNoData(100, 250));
			} else {
				if (json.currentPingRate == null) {
					$("#current_ping_rate").append(showNoData(100, 250));
					$("#current_ping_rtt").append(showNoData(100, 250));
				} else {
					dash_board("current_ping_rate", "实时连通率", "连通率(%)", (json.currentPingRate * 100).toFixed(2));
					$("#current_ping_rtt").append(rtt(json.currentPingRtt, "./"));
				}
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
				$("#current_ping_rate").append(showNoFunction(100, 250));
				$("#current_ping_rtt").append(showNoFunction(100, 250));
			}
		}
	});
}

function showDiskBar() {
	var id = $("#assetId").val();
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: "/FilesysInfo/ServerDetail/DiskUtilizationV02",
		data: {assetId:id},
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("#disk_chart").append(showNoData(100, 250, "文件分区"));
			} else {
				var data = json.partsDates;
				if (data.length == 0 || data == null){
					$("#disk_chart").append(showNoData(100, 250, "文件分区"));
				} else {
					// data.sort(sortCurrentPartTotal);
					data.sort((a, b) => b.currentPartTotal - a.currentPartTotal);
					let data1 = []
					data.forEach(d => {
						if(Number(d.currentDiskUtilization) * 100 > 0.1 ){
							data1.push(d)
						}
					})
					data1.sort((a, b) => b.currentDiskUtilization - a.currentDiskUtilization);
					let max = Math.ceil(Number(data1[0].currentDiskUtilization) * 100)
					data = JSON.parse(JSON.stringify(data1))
					
					var disk_object_array = new Array();
					var disk_object = function(desc, value) {
						this.desc = desc;
						this.value = value;
					}
					
					for (var i = 0;i < data.length && i < 5; i++){
						if (data[i].currentPartTotal != 0) {
							disk_object_array[disk_object_array.length] = new disk_object(data[i].filesys, (data[i].currentDiskUtilization * 100).toFixed(2));
						}
					}
					disk_bar_graph(disk_object_array, max);
				}
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
				$("#disk_chart").append(showNoFunction(100, 250));
			}
		}
	});
}

