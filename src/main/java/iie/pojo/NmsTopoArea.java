package iie.pojo;

import java.sql.Timestamp;

public class NmsTopoArea implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer mapId;
//	private String divId;
	private String divLeft;
	private String divTop;
	private String divWidth;
	private String divHeight;
	private String divName;
	private String lineDashed;
	private String lineWidth;
	private String lineColor;
//	private String zIndex;
	private Integer deled;
	private Timestamp itime;

	
	public NmsTopoArea() {
		
	}
	
	public NmsTopoArea(Integer id, Integer mapId, String divLeft, String divTop, String divWidth, String divHeight, String divName,
			 String lineDashed, String lineWidth, String lineColor, Integer deled, Timestamp itime) {
		this.id = id;
		this.mapId = mapId;
		this.divLeft = divLeft;
		this.divTop = divTop;
		this.divWidth = divWidth;
		this.divHeight = divHeight;
		this.divName = divName;
		this.lineDashed = lineDashed;
		this.lineWidth = lineWidth;
		this.lineColor = lineColor;
		this.deled = deled;
		this.itime = itime;
	}

	public NmsTopoArea(Integer mapId,/* String divId, */String divLeft, String divTop, String divWidth, String divHeight, String divName,
			 String lineDashed, String lineWidth, String lineColor,/* String zIndex, */Integer deled, Timestamp itime) {
		this.mapId = mapId;
//		this.divId = divId;
		this.divLeft = divLeft;
		this.divTop = divTop;
		this.divWidth = divWidth;
		this.divHeight = divHeight;
		this.divName = divName;
		this.lineDashed = lineDashed;
		this.lineWidth = lineWidth;
		this.lineColor = lineColor;
//		this.zIndex = zIndex;
		this.deled = deled;
		this.itime = itime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}
/*
	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}
*/
	public String getDivLeft() {
		return divLeft;
	}

	public void setDivLeft(String divLeft) {
		this.divLeft = divLeft;
	}

	public String getDivTop() {
		return divTop;
	}

	public void setDivTop(String divTop) {
		this.divTop = divTop;
	}

	public String getDivWidth() {
		return divWidth;
	}

	public void setDivWidth(String divWidth) {
		this.divWidth = divWidth;
	}

	public String getDivHeight() {
		return divHeight;
	}

	public void setDivHeight(String divHeight) {
		this.divHeight = divHeight;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public String getLineDashed() {
		return lineDashed;
	}

	public void setLineDashed(String lineDashed) {
		this.lineDashed = lineDashed;
	}

	public String getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(String lineWidth) {
		this.lineWidth = lineWidth;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
/*
	public String getzIndex() {
		return zIndex;
	}

	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
	}
*/

	public Integer getDeled() {
		return deled;
	}

	public void setDeled(Integer deled) {
		this.deled = deled;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}