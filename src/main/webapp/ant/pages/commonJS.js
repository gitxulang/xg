// 删除置顶按钮
if(document.getElementsByClassName('click-to-toggle').length > 0){
	document.getElementsByClassName('click-to-toggle')[0].remove()
}
var index = null
function openDialog(url, title, width, height) {
	index = layer.open({
	  type: 2,
	  title: title,
	  area: [width+'px', height+'px'],
	  fixed: true,
	  maxmin: false,
	  content: url
	});
}
function closeDialog(){
	layer.close(layer.index);
}