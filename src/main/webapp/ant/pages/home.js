
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
	$.ajax({
		type : 'POST',
		url : "/Admin/getuser",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(user) {
			if(user != null && (user == "sysadm" || user == "secadm" || user == "auditadm" || user =="root")) {
				// 先暂时保留, 等修改完其它页面再完成此部分
				initLog(user);
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
	})
	
	initDeviceChart();
	//loadNewAlarm();
});

/**
 * 初始化日志
 */
var initLog = function(user){
	var begin = $("#page").html()
	var offset = $("#selectPage").val();
	var requestData = {};
	requestData["begin"] = 1;
	requestData["offset"] = 10;
	requestData["username"] = user;
	$.ajax({
		type : 'POST',
		data : requestData,
		dataType : "json",
		url : "/AuditLog/list/find",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		success : function(json) {
			if (json != null) {
				loadList(json);
			}
		},
		beforeSend : function(xhr) {
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

/**
 * 初始化设备饼图
 */
var initDeviceChart= function(){
	$.ajax({
		type : 'POST',
		dataType : "json",
		url : "/Asset/typeAsset",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
		    var myChart = echarts.init(document.getElementById('main'));
			var data = genData(6);
			var legendData = [];
		    var seriesData = [];
		     for (var i = 0; i < json.length; i++) {
				seriesData.push({
					name : json[i][0],
					value : json[i][1]
				});
				legendData.push(json[i][0]);
			}

			option = {
			    title: {
			        text: '设备统计图',
			        left: 'left',
			        textStyle:{
			        	color : '#fff'
			        }
			    },
			    tooltip: {
			        trigger: 'item',
			        formatter: '{a} <br/>{b} : {c} ({d}%)'
			    },
			    legend: {
			        type: 'scroll',
			        orient: 'vertical',
			        right: 10,
			        top: 20,
			        bottom: 20,
			        data: legendData,
			    },
			    series: [
			        {
			            name: '设备',
			            type: 'pie',
			            radius: '55%',
			            center: ['40%', '50%'],
			            data: seriesData,
			            emphasis: {
			                itemStyle: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
			};
			// 使用刚指定的配置项和数据显示图表。
		    myChart.setOption(option);
			
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
	
	
	function genData(count) {
	    var nameList = [
	        '赵', '钱', '孙', '李', '周', '吴', '郑', '王', '冯', '陈', '褚', '卫', '蒋', '沈', '韩', '杨', '朱', '秦', '尤', '许', '何', '吕', '施', '张', '孔', '曹', '严', '华', '金', '魏', '陶', '姜', '戚', '谢', '邹', '喻', '柏', '水', '窦', '章', '云', '苏', '潘', '葛', '奚', '范', '彭', '郎', '鲁', '韦', '昌', '马', '苗', '凤', '花', '方', '俞', '任', '袁', '柳', '酆', '鲍', '史', '唐', '费', '廉', '岑', '薛', '雷', '贺', '倪', '汤', '滕', '殷', '罗', '毕', '郝', '邬', '安', '常', '乐', '于', '时', '傅', '皮', '卞', '齐', '康', '伍', '余', '元', '卜', '顾', '孟', '平', '黄', '和', '穆', '萧', '尹', '姚', '邵', '湛', '汪', '祁', '毛', '禹', '狄', '米', '贝', '明', '臧', '计', '伏', '成', '戴', '谈', '宋', '茅', '庞', '熊', '纪', '舒', '屈', '项', '祝', '董', '梁', '杜', '阮', '蓝', '闵', '席', '季', '麻', '强', '贾', '路', '娄', '危'
	    ];
	    var legendData = [];
	    var seriesData = [];
	    var selected = {};
	    for (var i = 0; i < count; i++) {
	        name = Math.random() > 0.65
	            ? makeWord(4, 1) + '·' + makeWord(3, 0)
	            : makeWord(2, 1);
	        legendData.push(name);
	        seriesData.push({
	            name: name,
	            value: Math.round(Math.random() * 100000)
	        });
	        selected[name] = i < 6;
	    }

	    return {
	        legendData: legendData,
	        seriesData: seriesData,
	        selected: selected
	    };

	    function makeWord(max, min) {
	        var nameLen = Math.ceil(Math.random() * max + min);
	        var name = [];
	        for (var i = 0; i < nameLen; i++) {
	            name.push(nameList[Math.round(Math.random() * nameList.length - 1)]);
	        }
	        return name.join('');
	    }
	}


    
	
}

var  loadList=function(json) {
	$("#tableBodyID").empty();
	for ( var item in json.list) {
		$("#tableBodyID").append(
			'<tr class="odd gradeX">'
				+ '<td>' + htmlEscape(json.list[item].modname) + '</td>'
				+ '<td title="' + json.list[item].content + '">' + htmlEscape(json.list[item].content) + '</td>'
				+ '<td>' + htmlEscape(json.list[item].logrest) + '</td>'
				+ '<td class="center">' + json.list[item].atime + '</td>' 
			+ '</tr>'
		)
	}
}


function loadNewAlarm() {
	var dataJson = {};
	dataJson["begin"] = 1;
	dataJson["offset"] = 10;
	dataJson["DStatus"] = 0;
	dataJson["orderKey"] = "itime";
	dataJson["orderValue"] = "0";
	
	$.ajax({
		type : 'POST',
		data: dataJson,
		dataType : "json",
		url : "/alarm/list/page/condition",
		contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		timeout : 5000,
		cache : true,
		async : true,
		success : function(json) {
			if (json != null) {
				alarmInfo = '<ul class="collection">';
				var length = json.list.length < 10 ? json.list.length: 10;
				
				for (var i = 0; i < length; i++) {
					alarmInfo += '<li class="collection-item avatar home-card-ul-li">';
					let infoLevel = '<span style="color:yellow;">告警  </span>'
					if (json.list[i].nmsAlarm.alevel == 1) {
// alarmInfo += '<i class="material-icons circle yellow">volume_up</i>';
						infoLevel = '<span style="color:yellow;">告警  </span>';
					} else if(json.list[i].nmsAlarm.alevel == 2) {
// alarmInfo += '<i class="material-icons circle orange">volume_up</i>';
						infoLevel = '<span style="color:#F6BD00;">告警  </span>';
					} else if (json.list[i].nmsAlarm.alevel == 3) {
// alarmInfo += '<i class="material-icons circle red">volume_up</i>';
						infoLevel = '<span style="color:#C22A4E;">严重  </span>';
					}
					
					alarmInfo += '<span class="title">' + json.list[i].nmsAlarm.nmsAsset.aip + '</span>';
					alarmInfo += '<p>'+infoLevel+'<font face="微软雅黑">告警时间：<font color="green">' + 
										json.list[i].nmsAlarm.atime + '</font>，网元名称：<font color="green">' + 
										json.list[i].nmsAlarm.nmsAsset.aname + '</font>，告警内容：<font color="red">' + 
										json.list[i].nmsAlarm.acontent + '</font></font></p>';
					
// alarmInfo += '<a href="#" class="secondary-content"><i
// class="material-icons">grade</i></a>';
					alarmInfo += '</li>';
				}
				
				alarmInfo += '</ul>';
				$("#alarmUL").append(alarmInfo);
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

function htmlEscape(text) {
	return text.replace(/[<>"&]/g, function(match, pos, originalText) {
		switch (match) {
		case "<":
			return "&lt;";
		case ">":
			return "&gt;";
		case "&":
			return "&amp;";
		case "\"":
			return "&quot;";
		}
	});
}

