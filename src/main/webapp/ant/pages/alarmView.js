
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
	loadTable();
	loadLine();
})

function loadTable(){
	$.ajax({
		type: 'POST',
		url: "/Asset/typeAssetAlarm",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(data_array) {
			if (data_array != null && data_array != "") {
				if (data_array.length == 0) {
					$("body").html(showNoData(100, 600));
				} else {
					loadList(data_array);
					loadBar(data_array);
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
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function loadList(data_array) {
	$("#tableBodyID").empty();
	var list = "";
	for (var i = 0; i < data_array.length; i++) {
		list +=	'<tr>'+
					'<td>' + data_array[i][0] + '</td>'+
					'<td>' + data_array[i][1] + '</td>'+			
					'<td>' + data_array[i][2] + '</td>'+
				'</tr>'
	}
	$("#tableBodyID").append(list);
}

function loadBar(data_array) {
	if (data_array == null) {
		document.getElementById("bar").innerHTML =  showNoData(100, 100); 
		return;
	}
	
	var name = [];
	var tnum = [];
	var anum = [];
	for (var i = 0; i < data_array.length; i++) {
		name.push(data_array[i][0]);
		tnum.push(data_array[i][1]);
		anum.push(data_array[i][2]);
	}
	
	var myChart = echarts.init(document.getElementById('bar'));
	myChart.title = '告警数量统计';	

	var option = {
	//	backgroundColor: '#f0f0f0',
		grid: {
		    top: '15%',
		    bottom: '5%',
		    left: '50',
		    right: '20'
		},
		
		title: {
			text: '告警数量统计',
			left: 'center',
			padding: 5,
			top:'5%',
			textStyle: {
				fontSize: 15,
				fontWeight: "bolder",
			}
		},				

		tooltip: {},
		
		legend: {
			x: 'left',
			data:['设备数量', '告警数量']
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
		
		xAxis: {
			data: name
		},
		
	    yAxis: [
	        {
	        	show: true,
				type: 'value',
				axisLine : {onZero: false},
				min: 0,
			//	max: 100,
				axisLabel: {
					formatter: '{value} 台',
				},
				boundaryGap: false,
				splitLine: {
					show: true, 
				},
			}
		],
		    
		
		series: [
		    {
				name: '设备数量',
				type: 'bar',
				data: tnum,
	            markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
	            barMinHeight:2,
			},
			{
				name: '告警数量',
				type: 'bar',
				data: anum,
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

function add0(m) {
	return m < 10 ? '0' + m : m;
}

function format(time_str) {
	var time = new Date(time_str);
	var y = time.getFullYear();
	var m = time.getMonth() + 1;
	var d = time.getDate();
	return y + '-' + add0(m) + '-' + add0(d);
}

function loadLine() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 1000000;
//	dataJson["DStatus"] = "0";
	var date = new Date();
	var endDate = format(date) + " 59:59:59";
	date.setDate(date.getDate() - 30);
	var startDate = format(date) + " 00:00:00";

	dataJson["startDate"] = startDate;
	dataJson["endDate"] = endDate;
	
	$.ajax({
		type: 'POST',
		data: dataJson,
		dataType: "json",
		url: "/alarm/list/page/condition",
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		timeout: 5000,
		cache: true,
		async: true,
		success: function(json) {
			if (json == null) {
				$("body").html(showNoFunction(100, 600));
			} else {
				createLine(json, startDate, endDate);
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
            	$("#page-inner").html(showNoFunction(100, 600));
			}
		}
	});
}

function createLine(json, startDate, endDate) {
	if (json == null || json.list.length == 0) {
		document.getElementById("line").innerHTML = showNoData(100, 250); 
		return;
	}
	
	var myChart = echarts.init(document.getElementById('line'));
	myChart.title = '历史告警数量统计';
	
	startDate = startDate.substring(0,10);    
	startDate = startDate.replace(/-/g,'/'); 
	var startTimeStamp = new Date(startDate).getTime();
	
	endDate = endDate.substring(0,10);    
	endDate = endDate.replace(/-/g,'/'); 
	var endTimeStamp = new Date(endDate).getTime();
	
	
	var date = [];
	var value = [];

	while (startTimeStamp <= endTimeStamp) {
		var dateStr = format(startTimeStamp);
		startTimeStamp += 60 * 60 * 24 * 1000;
		date.push(dateStr);
		value.push(0);
	}

	for (var i in json.list) {
		var obj = json.list[i];
		var itime = format(obj.nmsAlarm.itime);
		for (var j = 0; j < date.length; j++) {
			if (itime == date[j]) {
				value[j]++;
				break;
			}
		}
	}

	option = {
	//	backgroundColor: '#f0f0f0',
		grid: {
		    top: '35%',
		    bottom: '10%',
	        left: '45',
	        right: '20'
		},
		
		grid: [{
	        left: '45',
	        right: '20',
	        height: '50%',
	    }],
	    
		title: {
			text: '最近30日告警数量统计（TOP 30）',
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
	        formatter: '{b}<br/>告警数：{c} 个',
	    },
	    
	    legend: {
	        x: 'left',
	        data: ['告警数量',],
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
	    },

	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : date,
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
					formatter: '{value} 个',
				},
				boundaryGap: false,
				splitLine: {
					show: true,
				},
			}
	    ],
	    
	    series : [
	        {
	            name:'告警数量',
	            type:'line',
	            data: value,
	        },
	    ]
	};
	myChart.setOption(option);
}


