$(document).ready(function() {
	init();
})

var init = function() {
	this.listeners();
}
var listeners = function() {
	//文件上传事件
	$("#file").on("change", "input[type='file']", function() {
		var filePath = $(this).val();
		if (filePath.indexOf("xlsx") != -1 || filePath.indexOf("xls") != -1) {
			var arr = filePath.split('\\');
			var fileName = arr[arr.length - 1];
			$("#showFileName").html(fileName);
		} else {
			layer.msg('您未上传文件，或者您上传文件类型有误，只接收xls或xlsx类型文件！', {time: 2000},function(){
				return false;
			});
		}
	})
}

var closeDialog = function() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}


var importTable = function() {
	var importFile = $('#importFile')[0].files[0];
	if (null == importFile) {
		layer.msg('您尚未选择导入的xls文件！', {time: 2000},function(){
			return;
		});
	}else{
		var formData = new FormData();
		formData.append("importFile", importFile);
		$.ajax({
			url : '/deviceimport/input',
			dataType : 'json',
			type : 'POST',
			async : false,
			timout : 60 * 1000,
			data : formData,
			processData : false,
			contentType : false,
			success : function(data) {
				layer.msg(data.info, {time: 2000},function(){
					parent.location.reload(true);
				});
			},
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Authorization", getCookie("token"));
			},
			error : function(response) {
				alert(response.message);
			}
		});
	}
}
