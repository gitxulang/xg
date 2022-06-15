
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
	var request = GetRequest();
	var id = request.id;
	var typeid = request.typeid;
	var redirect = request.redirect;
	if (id == null || id == "" || typeid == null || typeid == "" || redirect == null || redirect == "") {
		return;
	}
	$("#assetId").val(id);
	$("#typeId").val(typeid);
	
	if (typeid != null && parseInt(typeid) == 10) {
		$("#memTitle").html("虚拟内存信息");
	}

	loadTab(id, typeid, redirect);
	showPingInfo();
	showCpuInfo();
	showMemInfo(typeid);

	if (parseInt(typeid) >= 1 && parseInt(typeid) <= 10) {
		showFileSysInfo();
	} else {
		$("#otherMemID").empty();
		$("#fileSysID").empty();
	}
})

function pingRateBar(current_ping_rate, current_day_ping_rate_avg, current_day_ping_rate_max) {
	current_ping_rate += "";
	current_day_ping_rate_avg += "";
	current_day_ping_rate_max += "";
	if (current_ping_rate == null || current_ping_rate == "" ||
		current_day_ping_rate_avg == null || current_day_ping_rate_avg == "" ||
		current_day_ping_rate_max == null || current_day_ping_rate_max == "") {
		
		document.getElementById("pingRateBar").innerHTML =  showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('pingRateBar'));
	myChart.title = '当日连通率信息';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
		    left: '50',
		    right: '20'
		},
		
		title: {
			text: '当日连通率统计（%）',
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
	            data : ['当日连通率'],
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
	        formatter: "{a}<br/>当日连通率率:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	      //      barWidth: 80,
	            data: [current_ping_rate],
	            
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
	       //     barWidth: 80,
	            data: [current_day_ping_rate_avg],
	            
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
	       //     barWidth: 80,
	            data: [current_day_ping_rate_max],
	            
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

function showCpuBar(current_cpu_rate, current_day_cpu_avg, current_day_cpu_max) {
	if (current_cpu_rate == null || current_cpu_rate == "" ||
		current_day_cpu_avg == null || current_day_cpu_avg == "" ||
		current_day_cpu_max == null || current_day_cpu_max == "") {
		
		document.getElementById("cpuBar").innerHTML = showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('cpuBar'));
	myChart.title = '当日CPU利用率';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
		    left: '50',
		    right: '20'
		},
		
		title: {
			text: '当日CPU利用率（%）',
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
	            data : ['当日CPU利用率'],
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
	        formatter: "{a}<br/>当日CPU利用率:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	      //      barWidth: 80,
	            data: [current_cpu_rate],
	            
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
	       //     barWidth: 80,
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
	       //     barWidth: 80,
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

function showPhyMemBar(current_mem_rate, current_day_mem_avg, current_day_mem_max) {
	if (current_mem_rate == null || current_mem_rate == "" ||
		current_day_mem_avg == null || current_day_mem_avg == "" ||
		current_day_mem_max == null || current_day_mem_max == "") {
		
		document.getElementById("phyMemBar").innerHTML = showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('phyMemBar'));
	myChart.title = '当日物理内存利用率';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
		    left: '50',
		    right: '20'
		},
		
		title: {
			text: '当日物理内存利用率（%）',
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
	            data : ['当日物理内存利用率'],
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
	        formatter: "{a}<br/>当日物理内存利用率:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	      //      barWidth: 80,
	            data: [current_mem_rate],
	            
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
	       //     barWidth: 80,
	            data: [current_day_mem_avg],
	            
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
	       //     barWidth: 80,
	            data: [current_day_mem_max],
	            
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

function showOtherMemBar(titleMem, current_mem_rate, current_day_mem_avg, current_day_mem_max) {
	if (current_mem_rate == null || current_mem_rate == "" ||
		current_day_mem_avg == null || current_day_mem_avg == "" ||
		current_day_mem_max == null || current_day_mem_max == "") {
		
		document.getElementById("otherMemBar").innerHTML = showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('otherMemBar'));
	myChart.title = '当日' + titleMem + '内存利用率';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
		    left: '50',
		    right: '20'
		},
		
		title: {
			text: '当日' + titleMem + '内存利用率（%）',
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
	            data : ['当日' + titleMem + '内存利用率'],
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
	        formatter: "{a}<br/>当日' + titleMem + '内存利用率:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	      //      barWidth: 80,
	            data: [current_mem_rate],
	            
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
	       //     barWidth: 80,
	            data: [current_day_mem_avg],
	            
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
	       //     barWidth: 80,
	            data: [current_day_mem_max],
	            
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

function showFileSysBar(disk_object_array) {
	if (disk_object_array == null || disk_object_array.length == 0) {
		document.getElementById("fileSysBar").innerHTML =  showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('fileSysBar'));
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
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '30%',
		    bottom: '10%'
		},
				
		title: {
			text: '实时文件分区使用率统计（%）',
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
	        data: desc_data,	        
	    },
	    
	    toolbox: {
	        show : false,
	    },
	    
	    xAxis: [
	        {	
	        	show: true,
	            data : ['文件系统分区使用率'],
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

function pingRttBar(current_ping_rtt, current_day_ping_rtt_avg, current_day_ping_rtt_max) {
	current_ping_rtt += "";
	current_day_ping_rtt_avg += "";
	current_day_ping_rtt_max += "";
	if (current_ping_rtt == null || current_ping_rtt == "" ||
		current_day_ping_rtt_avg == null || current_day_ping_rtt_avg == "" ||
		current_day_ping_rtt_max == null || current_day_ping_rtt_max == "") {
		
		document.getElementById("pingRttBar").innerHTML =  showNoData(100, 250); 

		return;
	}
	
	var myChart = echarts.init(document.getElementById('pingRttBar'));
	myChart.title = '当日连通响应时间信息';

	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
		    left: '50',
		    right: '20'
		},
		
		title: {
			text: '当日连通响应时间统计（μs）',
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
	            data : ['连通响应时间'],
	        }
	    ],
	    
	    yAxis: [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
			//	max: 100,
				axisLabel: {
					formatter: '{value} μs',
				},
				boundaryGap: false,
				splitLine: {
					show: true, 
				},
			}
		],
	    
	    tooltip: {
	        trigger: 'bar',
	        formatter: "{a}<br/>连通响应时间:{c}%",
	    },		    
	        
	    series: [ 
	        {
	            name: '实时',
	            type: 'bar',
	       //     barWidth: 80,
	            data: [current_ping_rtt],
	            
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
	        //    barWidth: 80,
	            data: [current_day_ping_rtt_avg],
	            
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
	        //    barWidth: 80,
	            data: [current_day_ping_rtt_max],
	            
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

function showPingInfo() {
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
				$("#pingRateBar").append(showNoData(100, 250));
				$("#pingRateLine").append(showNoData(100, 250));
				$("#pingRttBar").append(showNoData(100, 250));
				$("#pingRttLine").append(showNoData(100, 250));
			} else {
				if (json.currentPingRate == null) {
					$("#pingRateBar").append(showNoData(100, 250));
					$("#pingRateLine").append(showNoData(100, 250));
					$("#pingRttBar").append(showNoData(100, 250));
					$("#pingRttLine").append(showNoData(100, 250));
				} else {
					pingRateBar(json.currentPingRate * 100, (json.averagePingRateOfToday * 100).toFixed(0), json.maxPingRateOfToday * 100);
					pingRttBar(json.currentPingRtt, parseInt(json.averagePingRttOfToday), json.maxPingRttOfToday);
					
					var rate_object_array = [];
					var rtt_object_array = [];
					for(var i = 0;i < json.nmsPingInfoList.length; i++) {
						var rate_tmp = new Array();
						rate_tmp[0] = json.nmsPingInfoList[i].itime;
						rate_tmp[1] = json.nmsPingInfoList[i].pingRate * 100;
						rate_object_array[rate_object_array.length] = rate_tmp;
						var rtt_tmp = new Array();
						rtt_tmp[0] = json.nmsPingInfoList[i].itime;
						rtt_tmp[1] = json.nmsPingInfoList[i].pingRtt;
						rtt_object_array[rtt_object_array.length] = rtt_tmp;
					}
					ping_line(rate_object_array.reverse());
					rtt_line(rtt_object_array.reverse());
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
				$("#pingRateBar").append(showNoFunction(100, 250));
				$("#pingRateLine").append(showNoFunction(100, 250));
				$("#pingRttBar").append(showNoFunction(100, 250));
				$("#pingRttLine").append(showNoFunction(100, 250));
			}
		}
	});
}

function ping_line(object_array) {
	if (object_array == null || object_array.length == 0) {
		document.getElementById("pingRateLine").innerHTML = showNoData(100, 250);
		return;
	}
	
	var myChart = echarts.init(document.getElementById('pingRateLine'));
	myChart.title = '连通率信息';
	
	var dates = object_array.map(function (item) {
	    return item[0];
	});
	
	var data = object_array.map(function (item) {
	    return item[1];
	});
	
	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '80',
	        right: '40'
		},
		
		grid: [{
	        left: 80,
	        right: 40,
	        height: '50%',
	    }],
	    
		title: {
			text: '最近4日连通率趋势统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
			
	    tooltip : {
	        trigger: 'axis',
	        formatter: '{b}<br/>连通率：{c}%',
	    },
	    
	    legend: {
	        x: 'left',
	        data: ['连通率'],
	        textStyle: {
	     //     fontSize: 14,
	     //     color: '#cff',
	        }
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    
	    dataZoom : {
	        show : true,
	        realtime : true,
	        start : 20,
	        end : 80,
	        textStyle: {
				fontSize: 14,
		//		color: '#ff0',
			}	
	    },
	    
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : dates,
	            
	            axisLabel: {
					textStyle: {
			//			fontSize: 12,
			//		    color: '#aff',
					}	
				},
				boundaryGap: false,
	        }
	    ],
	    
	    yAxis : [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value} %',
					textStyle: {
				//		fontSize: 12,
				//	    color: '#aff',
					}	
				},
				boundaryGap: false,
				splitLine: {
					show: true,
               		lineStyle: {
               	//		type: 'solid',
                //		color: '#6E6E6E',
                	}  
				},
			}
	    ],
	    
	    series : [
	        {
	            name:'连通率',
	            type:'line',
	            data: data,
	        },
	    ]
	};

	myChart.setOption(option);		

}

function rtt_line(object_array) {
	if (object_array == null || object_array.length == 0) {
		document.getElementById("pingRttLine").innerHTML = showNoData(100, 250);
		return;
	}
	
	var myChart = echarts.init(document.getElementById('pingRttLine'));
	myChart.title = '响应时间';
	
	var dates = object_array.map(function (item) {
	    return item[0];
	});
	
	var data = object_array.map(function (item) {
	    return item[1];
	});
	
	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '80',
	        right: '40'
		},
		
		grid: [{
	        left: 80,
	        right: 40,
	        height: '50%',
	    }],
		
		title: {
			text: '最近4日响应时间趋势统计（μs）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
			
	    tooltip : {
	        trigger: 'axis',
	        formatter: '{b}<br/>响应时间：{c}μs',
	    },
	    
	    legend: {
	        x: 'left',
	        data: ['响应时间',],
	        textStyle: {
	  //	    fontSize: 14,
	  //		color: '#cff',
	        }
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    
	    dataZoom : {
	        show : true,
	        realtime : true,
	        start : 20,
	        end : 80,
	        textStyle: {
		//		fontSize: 14,
		//		color: '#ff0',
			}	
	    },
	    
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : dates,
	            
	            axisLabel: {
					textStyle: {
		//				fontSize: 12,
		///			    color: '#aff',
					}	
				},
				boundaryGap: false,
	        }
	    ],
	    
	    yAxis : [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
			//	max: 100,
				axisLabel: {
					formatter: '{value} μs',
					textStyle: {
			//			fontSize: 12,
			//		    color: '#aff',
					}	
				},
				boundaryGap: false,
				splitLine: {
					show: true,
               		lineStyle: {
             //			type: 'solid',
             //   		color: '#6E6E6E',
                	}  
				},
			}
	    ],
	    
	    series : [
	        {
	            name:'响应时间',
	            type:'line',
	            data: data,
	        },
	    ]
	};

	myChart.setOption(option);		

}

function showCpuInfo() {
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
				$("#cpuBar").append(showNoData(100, 250));
				$("#cpuLine").append(showNoData(100, 250));
			} else {
				if (json.cpuData.currentCpuRate == null) {
					$("#cpuBar").append(showNoData(100, 250));
					$("#cpuLine").append(showNoData(100, 250));
				} else {
					var currentCpuRate = json.cpuData.currentCpuRate.toFixed(2) + "";
					var averageCpuRateOfToday = json.cpuData.averageCpuRateOfToday.toFixed(2) + "";
					var maxCpuRateOfToday = json.cpuData.maxCpuRateOfToday.toFixed(2) + "";
					showCpuBar(currentCpuRate, averageCpuRateOfToday, maxCpuRateOfToday);
					
					object_array = [];	
					for (var i = 0; i < json.cpuData.data.length; i++) {
						var tmp = new Array();
						tmp[0] = json.cpuData.data[i].itime;
						tmp[1] = json.cpuData.data[i].cpuRate;
						object_array[object_array.length] = tmp;
					}
					showCpuLine(object_array);
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
				$("#cpuBar").append(showNoFunction(100, 250));
				$("#cpuLine").append(showNoFunction(100, 250));
			}
		}
	});
}

function showCpuLine(object_array) {
	if (object_array == null || object_array.length == 0) {
		document.getElementById("cpuLine").innerHTML =  showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('cpuLine'));
	myChart.title = '历史CPU利用率';
	
	var dates = object_array.map(function (item) {
	    return item[0];
	});
	
	var data = object_array.map(function (item) {
	    return item[1];
	});
	
	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '80',
	        right: '40'
		},
		
		grid: [{
	        left: 80,
	        right: 40,
	        height: '50%',
	    }],
	    
		title: {
			text: '最近4日CPU利用率趋势统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
		
	    tooltip : {
	        trigger: 'axis',
	        formatter: '{b}<br/>CPU利用率：{c} %',
	    },
	    
	    legend: {
	        x: 'left',
	        data: ['CPU利用率',],
	        textStyle: {
	     //     fontSize: 14,
	     //     color: '#cff',
	        }
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    
	    dataZoom : {
	        show : true,
	        realtime : true,
	        start : 20,
	        end : 80,
	        textStyle: {
		//		fontSize: 14,
		//		color: '#ff0',
			}	
	    },

	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : dates,
	            
	            axisLabel: {
					textStyle: {
			//			fontSize: 12,
			//		    color: '#aff',
					}	
				},
				boundaryGap: false,
	        }
	    ],
	    
	    yAxis : [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value} %',
					textStyle: {
			//			fontSize: 12,
			//		    color: '#aff',
					}	
				},
				boundaryGap: false,
				splitLine: {
					show: true,
               		lineStyle: {
            //    		type: 'solid',
            //    		color: '#6E6E6E',
                	}  
				},
			}
	    ],
	    
	    series : [
	        {
	            name:'CPU利用率',
	            type:'line',
	            data: data,
	        },
	    ]
	};
	myChart.setOption(option);
}

function showMemInfo(type) {
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
				$("#phyMemBar").append(showNoData(100, 250));
				$("#phyMemLine").append(showNoData(100, 250));
				$("#otherMemBar").append(showNoData(100, 250));
				$("#otherMemLine").append(showNoData(100, 250));
			} else {
				if (json.memData.currentMemRate == null) {
					$("#phyMemBar").append(showNoData(100, 250));
					$("#phyMemLine").append(showNoData(100, 250));
					$("#otherMemBar").append(showNoData(100, 250));
					$("#otherMemLine").append(showNoData(100, 250));
				} else {
					var currentMemTotal = (parseFloat(json.memData.currentMemTotal) / 1024 / 1024).toFixed(2);
					var currentMemRate = (json.memData.currentMemRate*100).toFixed(2);
					var averageMemRateOfToday = (json.memData.averageMemRateOfToday*100).toFixed(2);
					var maxMemRateOfToday = (json.memData.maxMemRateOfToday*100).toFixed(2);	
					showPhyMemBar(currentMemRate, averageMemRateOfToday, maxMemRateOfToday);
					object_array = [];	
					for (var i = 0; i < json.memData.data.length; i++) {
						var tmp = new Array();
						tmp[0] = json.memData.data[i].itime;
						tmp[1] = json.memData.data[i].memRate;
						object_array[object_array.length] = tmp;
					}
					showPhyMemLine(object_array.reverse());
					
					var currentSwapTotal = (parseFloat(json.nmsSwapData.currentSwapTotal) / 1024 / 1024).toFixed(2);
					var currentSwapRate = (json.nmsSwapData.currentSwapRate*100).toFixed(2);
					var averageSwapRateOfToday = (json.nmsSwapData.averageSwapRateOfToday*100).toFixed(2);
					var maxSwapRateOfToday = (json.nmsSwapData.maxSwapRateOfToday*100).toFixed(2);
					if (type != null && type == 5) {
						showOtherMemBar("虚拟", currentSwapRate, averageSwapRateOfToday, maxSwapRateOfToday);
					} else {
						showOtherMemBar("交换", currentSwapRate, averageSwapRateOfToday, maxSwapRateOfToday);
					}
					
					object_array = [];	
					for(var i=0;i<json.nmsSwapData.data.length;i++){
						var tmp = new Array();
						tmp[0] = json.nmsSwapData.data[i].itime;
						tmp[1] = json.nmsSwapData.data[i].swapRate;
						object_array[object_array.length] = tmp;
					}
					
					if (type != null && type == 5) {
						showOtherMemLine("虚拟", object_array.reverse());
					} else {
						showOtherMemLine("交换", object_array.reverse());
					}
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
				$("#phyMemBar").append(showNoFunction(100, 250));
				$("#phyMemLine").append(showNoFunction(100, 250));
				$("#otherMemBar").append(showNoFunction(100, 250));
				$("#otherMemLine").append(showNoFunction(100, 250));
			}
		}
	});
}

function showPhyMemLine(object_array) {
	if (object_array == null || object_array.length == 0) {
		document.getElementById("phyMemLine").innerHTML =  showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('phyMemLine'));
	myChart.title = '最近4日物理内存利用率';
	
	var dates = object_array.map(function (item) {
	    return item[0];
	});
	
	var data = object_array.map(function (item) {
	    return item[1];
	});
	
	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '80',
	        right: '40'
		},
		
		grid: [{
	        left: 80,
	        right: 40,
	        height: '50%',
	    }],
	    
		title: {
			text: '最近4日物理内存利用率趋势统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
		
	    tooltip : {
	        trigger: 'axis',
	        formatter: '{b}<br/>物理内存利用率：{c} %',
	    },
	    
	    legend: {
	        x: 'left',
	        data: ['物理内存利用率',],
	        textStyle: {
	      //    fontSize: 14,
	      //    color: '#cff',
	        }
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    
	    dataZoom : {
	        show : true,
	        realtime : true,
	        start : 20,
	        end : 80,
	        textStyle: {
		//		fontSize: 14,
		//		color: '#ff0',
			}	
	    },
	    
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : dates,
	            
	            axisLabel: {
					textStyle: {
		//				fontSize: 12,
		//			    color: '#aff',
					}	
				},
				boundaryGap: false,
	        }
	    ],
	    
	    yAxis : [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value} %',
					textStyle: {
		//				fontSize: 12,
		//			    color: '#aff',
					}	
				},
				boundaryGap: false,
				splitLine: {
					show: true,
               		lineStyle: {
      //      			type: 'solid',
      //          		color: '#6E6E6E',
                	}  
				},
			}
	    ],
	    
	    series : [
	        {
	            name:'物理内存利用率',
	            type:'line',
	            data: data,
	        },
	    ]
	};
	myChart.setOption(option);	
}


function showOtherMemLine(memTitle, object_array) {

	if (object_array == null || object_array.length == 0) {
		document.getElementById("otherMemLine").innerHTML =  showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('otherMemLine'));
	myChart.title = '最近4日' + memTitle + '内存利用率';
	
	var dates = object_array.map(function (item) {
	    return item[0];
	});
	
	var data = object_array.map(function (item) {
	    return item[1];
	});
	
	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '80',
	        right: '40'
		},
		
		grid: [{
	        left: 80,
	        right: 40,
	        height: '50%',
	    }],
	    
		title: {
			text: '最近4日' + memTitle + '内存利用率趋势统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
			
	    tooltip : {
	        trigger: 'axis',
	        formatter: '{b}<br/>' + memTitle + '内存利用率：{c} %',
	    },
	    
	    legend: {
	        x: 'left',
	        data: ['最近4日' + memTitle + '内存利用率',],
	        textStyle: {
	//          fontSize: 14,
	//          color: '#cff',
	        }
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    
	    dataZoom : {
	        show : true,
	        realtime : true,
	        start : 20,
	        end : 80,
	        textStyle: {
	//			fontSize: 14,
	//			color: '#ff0',
			}	
	    },
	    
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : dates,
	            
	            axisLabel: {
					textStyle: {
	//					fontSize: 12,
	//				    color: '#aff',
					}	
				},
				boundaryGap: false,
	        }
	    ],
	    
	    yAxis : [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value} %',
					textStyle: {
	//					fontSize: 12,
	//				    color: '#aff',
					}	
				},
				boundaryGap: false,
				splitLine: {
					show: true,
               		lineStyle: {
       //     			type: 'solid',
       //         		color: '#6E6E6E',
                	}  
				},
			}
	    ],
	    
	    series : [
	        {
	            name:'最近4日' + memTitle + '内存利用率',
	            type:'line',
	            data: data,
	        },
	    ]
	};
	myChart.setOption(option);		
}


function showFileSysInfo() {
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
				$("#fileSysBar").append(showNoData(100, 250));
				$("#fileSysLine").append(showNoData(100, 250));
			} else {
				if (json.partsDates.length == 0 || json.partsDates == null) {
					$("#fileSysBar").append(showNoData(100, 250));
					$("#fileSysLine").append(showNoData(100, 250));
				} else {
					var data = json.partsDates;
					data.sort(sortCurrentPartTotal);
					
					var object_array = [];
					var date_array = [];
					var line_name = []
					for (var i = 0; i < data[0].diskUtilizationData.length; i++){
						date_array[i] = data[0].diskUtilizationData[i].itime;
					}
					
					date_array = date_array.reverse();
					var disk_object_array = new Array();
					var disk_object = function(desc, value) {
						this.desc = desc;
						this.value = value;
					}
					
					for (var i = 0; i < data.length && i < 5; i++) {
						if(data[i].currentPartTotal != 0){
							disk_object_array[disk_object_array.length] = new disk_object(data[i].filesys, (data[i].currentDiskUtilization * 100).toFixed(2));
						}
						
						line_name[line_name.length] = data[i].filesys;
						var diskUtilization_object = [];
						for (var j = 0; j < data[i].diskUtilizationData.length; j++) {
							diskUtilization_object[j] = (data[i].diskUtilizationData[j].diskUtilization * 100).toFixed(2);
						}
						object_array[i] = diskUtilization_object.reverse();
					}
					
					showFileSysBar(disk_object_array);
					showFileSysLine(date_array, object_array, line_name);
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
				$("#fileSysBar").append(showNoData(100, 250));
				$("#fileSysLine").append(showNoData(100, 250));
			}
		}
	});
}

function sortCurrentPartTotal(a,b){
	return b.currentPartTotal - a.currentPartTotal;
}

function showFileSysLine(date_array, object_array, line_name) {
	if (date_array == null || date_array.length == 0 || object_array == null || object_array.length == 0) {
		document.getElementById("fileSysLine").innerHTML = showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('fileSysLine'));
	myChart.title = '文件系统分区利用率';
	
	option = {
		backgroundColor: '#f0f0f0',
		
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '80',
	        right: '40'
		},
		
		grid: [{
	        left: 80,
	        right: 40,
	        height: '50%',
	    }],
	    
		title: {
			text: '最近4日文件系统分区趋势统计（%）',
			left: 'center',
			padding: 25,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},	
			
	    tooltip : {
	        trigger: 'axis',
	        formatter: '{b}<br/>文件系统分区利用率：{c} %',
	    },
	    
	    legend: {
	        x: 'left',
	        data: line_name,
	        textStyle: {
	    //      fontSize: 14,
	    //      color: '#cff',
	        }
	    },
	    
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    calculable : true,
	    
	    dataZoom : {
	        show : true,
	        realtime : true,
	        start : 20,
	        end : 80,
	        textStyle: {
		//		fontSize: 14,
		//		color: '#ff0',
			}	
	    },
	    
	    xAxis : [
	        {
	        	show: true,
	            type : 'category',
	            boundaryGap : false,
	            data : date_array,
	            
	            axisLabel: {
					textStyle: {
			//			fontSize: 12,
			//		    color: '#aff',
					}	
				},
				boundaryGap: false,
	        }
	    ],
	    
	    yAxis : [
	        {
	        	show: true,
				type: 'value',
				
				splitLine: {
					show: true,
               		lineStyle: {
           //  			type: 'solid',
           //     		color: '#6E6E6E',
                	}  
				},
				
				splitArea: {show: false},
				axisLine: {onZero: false},
				min: 0,
				max: 100,
				axisLabel: {
					formatter: '{value} %',
					textStyle: {
			//			fontSize: 12,
			//		    color: '#aff',
					}	
				},
				boundaryGap: false,
			}
	    ],
	    
	    series : [

	    ]
	};
	
	for (i = 0; i < object_array.length; i++) {
		var itme = {};
		itme.name = line_name[i];
		itme.type = 'line';
		itme.data = object_array[i];
		option.series.push(itme);
	}
	myChart.setOption(option);
}

