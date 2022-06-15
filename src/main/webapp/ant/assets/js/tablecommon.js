
function autochange() {
	var tTD;
	var table = document.getElementById("tableID");
	for (j = 0; j < table.rows[0].cells.length; j++) {
		table.rows[0].cells[j].onmousedown = function () {
			tTD = this;
			if (event.offsetX > tTD.offsetWidth - 10) {
				tTD.mouseDown = true;
				tTD.oldX = event.x;
				tTD.oldWidth = tTD.offsetWidth;
			}

		};
		
		table.rows[0].cells[j].onmouseup = function () {
			if (tTD == undefined) tTD = this;
			tTD.mouseDown = false;
			tTD.style.cursor = 'default';
		};
		
		table.rows[0].cells[j].onmousemove = function () {

			if (event.offsetX > this.offsetWidth - 10)
				this.style.cursor = 'col-resize';
			else
				this.style.cursor = 'default';

			if (tTD == undefined) 
				tTD = this;

			if (tTD.mouseDown != null && tTD.mouseDown == true) {
				tTD.style.cursor = 'default';
				if (tTD.oldWidth + (event.x - tTD.oldX) > 0)
					tTD.width = tTD.oldWidth + (event.x - tTD.oldX);

				tTD.style.width = tTD.width;
				tTD.style.cursor = 'col-resize';

				table = tTD;
				while (table.tagName != 'TABLE') table = table.parentElement;
				for (j = 0; j < table.rows.length; j++) {
					table.rows[j].cells[tTD.cellIndex].width = tTD.width;
				}
			}
		};
	}
}

autochange();