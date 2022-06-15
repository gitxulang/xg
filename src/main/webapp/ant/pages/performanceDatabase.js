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

$(document).ready(function () {
    // var request = GetRequest();
    var request = GetSelfRequest();
    var id = request.id;
    var typeid = request.typeid;
	var redirect = request.redirect;
	if (id == null || id == "" || typeid == null || typeid == "" || redirect == null || redirect == "") {
        return;
    }
    $("#softId").val(id);
    $("#typeId").val(typeid);

    loadTab(id, typeid, redirect);
    loadDevice();
})

function loadDevice() {
    var id = $("#softId").val();
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: "/DatabaseBasic/Overview",
        data: {softId: id},
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        timeout: 10000,
        cache: true,
        async: true,
        success: function (json) {
            if (json != null) {
                loadTable(json);
            } else {
                $("#tID").append(showNoData(100, 517));
            }

            if (json != null && json.sqls != null) {
                loadList(json.sqls);
            } else {
            //    $("#tableBodySQLID").append(showNoData(100, 517));
            }

            if (json != null && json.storages != null) {
                loadStorageList(json.storages);
            } else {
            //    $("#tableBodyID").append(showNoData(100, 517));
            }

            if (json == null || json.nmsPing == null) {
                $("#current_ping_rate").append(showNoData(100, 250));
                $("#current_ping_rtt").append(showNoData(100, 250));
            } else {
                if (json.nmsPing.currentPingRate == null) {
                    $("#current_ping_rate").append(showNoData(100, 250));
                    $("#current_ping_rtt").append(showNoData(100, 250));
                } else {
                    dash_board("current_ping_rate", "实时连通率", "连通率(%)", (json.nmsPing.currentPingRate * 100).toFixed(2));
                    $("#current_ping_rtt").append(rtt(json.nmsPing.currentPingRtt, "./"));
                }
            }

            if (json == null) {
                $("#current_con_rate").append(showNoData(100, 250));
            } else {
                if (json.connNum == null) {
                    $("#current_con_rate").append(showNoData(100, 250));
                } else {
                    dash_board_num("current_con_rate", "当前连接数", "连接数(个)", json.connNum.toFixed(2) + "", 500, 10);
                }
            }

            if (json == null) {
                $("#current_rps_rate").append(showNoData(100, 250));
            } else {
                if (json.numPerSecond == null) {
                    $("#current_rps_rate").append(showNoData(100, 250));
                } else {
                    dash_board_num("current_rps_rate", "每秒事务数", "事务数(个)", json.numPerSecond.toFixed(2) + "", 5000, 8);
                }
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
	            $("#tID").append(showNoFunction(100, 517));
	            $("#current_ping_rate").append(showNoFunction(100, 250));
	            $("#current_ping_rtt").append(showNoFunction(100, 250));
	            $("#current_con_rate").append(showNoFunction(100, 250));
	            $("#current_rps_rate").append(showNoFunction(100, 250));
	            $("#tableBodySQLID").append(showNoFunction(100, 250));
	            $("#tableBodyID").append(showNoFunction(100, 250));
			}
		}
    });
}

function loadTable(json) {
    var innerTable = '';
    innerTable += '<table class="table">';
    innerTable += '<tbody>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col" width="30%">软件/数据库名称</td>';
    innerTable += '<td class="success">' + (json.softName==null?'--':json.softName) + '/' + (json.name==null?'--':json.name) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">版本</td>';
    innerTable += '<td class="success">' + (json.version==null?'--':json.version) + '</td>';
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
    innerTable += '<td class="my-table-col">安装时间</td>';
    innerTable += '<td class="success">' + (json.installTime==null?'--':format(json.installTime)) + '</td>';
    innerTable += '</tr>';
    
    var processName = '--';
    if (json.processName != null) {
    	processName = json.processName.length > 50 ? json.processName.substring(0,50) + '...' : json.processName;
    } else {
    	json.processName = '--';
    } 

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">进程名称</td>';
    innerTable += '<td class="success" title="' + json.processName + '">' + processName + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">启动时间</td>';
    innerTable += '<td class="success">' + (json.startTime==null?'--':format(json.startTime)) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">存储总容量（KB）</td>';
    innerTable += '<td class="success">' + (json.totalSize==null?'--':json.totalSize) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">最大连接数</td>';
    innerTable += '<td class="success">' + (json.maxConnNum==null?'--':json.maxConnNum) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">已用/最大使用内存（KB）</td>';
    innerTable += '<td class="success">' + (json.memSize==null?'--':json.memSize) + '/' + (json.maxMemSize==null?'--':json.maxMemSize) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">已存数据量（KB）</td>';
    innerTable += '<td class="success">' + (json.statusTotalSize==null?'--':json.statusTotalSize) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">当前死锁数</td>';
    innerTable += '<td class="success">' + (json.deadLockNum==null?'--':json.deadLockNum) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">事务总数</td>';
    innerTable += '<td class="success">' + (json.tps==null?'--':json.tps) + '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">当前访问用户</td>';
    innerTable += '<td class="success">' + (json.userList==null?'--':json.userList)+ '</td>';
    innerTable += '</tr>';

    innerTable += '<tr>';
    innerTable += '<td class="my-table-col">本次查看时间</td>';
    innerTable += '<td class="success">' + format(new Date().getTime()) + '</td>';
    innerTable += '</tr>';

    innerTable += '</tbody>';
    innerTable += '</table>	';

    $("#tID").append(innerTable);
}

function loadList(list) {
    $("#tableBodySQLID").empty();
    for (var i in list) {
        var n = parseInt(i) + 1;
        $("#tableBodySQLID").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td>' + list[i].slowSql + '</td>' +
            '<td>' + list[i].execTime + '</td>' +
            '<td>' + format(list[i].itime) + '</td>' +
            '</tr>'
        );
    }
}

function loadStorageList(list) {
    $("#tableBodyID").empty();
    for (var i in list) {
        var n = parseInt(i) + 1;
        $("#tableBodyID").append(
            '<tr class="odd gradeX">' +
            '<td class="center">' + n + '</td>' +
            '<td style="word-break: break-all;">' + list[i].path + '</td>' +
            '<td>' + list[i].totalSize + '</td>' +
            '<td>' + list[i].usedSize + '</td>' +
            '<td>' + format(list[i].itime) + '</td>' +
            '</tr>'
        );
    }
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

function rtt(rtt, rootPath) {
    if (rtt == null || rtt == "" || rtt == 0) {
        return "<div style='color: #00ff00;text-align:center;font-size:24px;line-height:250px;'>这里暂时没有数据</div>";
    }

    rtt = Math.round(rtt, 0);
    var rttXml = "";

    if (rtt / 999999 > 1) {
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
    } else if (rtt / 100000 >= 1) {
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 100000, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10000, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 1000, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 100, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt % 10 + '.png" width="31" height="58"/></li>';
    } else if (rtt / 10000 >= 1) {
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10000, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 1000, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 100, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt % 10 + '.png" width="31" height="58"/></li>';
    } else if (rtt / 1000 >= 1) {
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 1000, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 100, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt % 10 + '.png" width="31" height="58"/></li>';
    } else if (rtt / 100 >= 1) {
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 100, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt % 10 + '.png" width="31" height="58"/></li>';
    } else if (rtt / 10 >= 1) {
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + parseInt(rtt / 10, 0) % 10 + '.png" width="31" height="58"/></li>';
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt % 10 + '.png" width="31" height="58"/></li>';
    } else if (rtt >= 1) {
        rttXml += '<li class="shu"><img src="./assets/img/shu_' + rtt % 10 + '.png" width="31" height="58"/></li>';
    }

    rttXml += '<li class="shu"><img src="./assets/img/shu_u.png" width="31" height="58" /></li>';
    rttXml += '<li class="shu"><img src="./assets/img/shu_s.png" width="31" height="58" /></li>';

    return rttXml;
}

function dash_board(show_id, show_name, show_unit, value) {
    if (value == null || value == "") {
        document.getElementById(show_id).innerHTML = showNoData(document.getElementById(show_id).offsetWidth, document.getElementById(show_id).offsetHeight);
        return;
    }

    var myChart = echarts.init(document.getElementById(show_id));
    myChart.title = '利用率';
    option = {
        backgroundColor: '#f0f0f0',

        tooltip: {
            formatter: "{b} : {c}%",
        },

        toolbox: {
            show: false,
            feature: {
                mark: {show: true},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },

        series: [
            {
                name: show_unit,
                type: 'gauge',
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

                data: [{value: value, name: show_name}]
            }
        ]
    };
    myChart.setOption(option);
}

function dash_board_num(show_id, show_name, show_unit, value, max_value, split_number) {
    if (value == null || value == "") {
        document.getElementById(show_id).innerHTML = showNoData(document.getElementById(show_id).offsetWidth, document.getElementById(show_id).offsetHeight);
        return;
    }

    var myChart = echarts.init(document.getElementById(show_id));
    myChart.title = show_name;
    option = {
        backgroundColor: '#f0f0f0',

        tooltip: {
            formatter: "{b} : {c}个",
        },

        toolbox: {
            show: false,
            feature: {
                mark: {show: true},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },

        series: [
            {
                name: show_unit,
                type: 'gauge',
                min: 0,
                max: max_value,
                splitNumber: split_number,
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

                data: [{value: parseInt(value), name: show_name}]
            }
        ]
    };
    myChart.setOption(option);
}
