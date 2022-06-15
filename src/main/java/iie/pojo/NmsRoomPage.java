package iie.pojo;

import java.io.Serializable;

/**
 *机房3D展示前端数据
 */

public class NmsRoomPage implements Serializable {

	private Integer id;
	private Integer roomId;
	private String webJson;
	private String roomLocation;
	private String roomDesc;
	private String roomCode;
	private String isDefault;

	public NmsRoomPage() {
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public NmsRoomPage(Integer roomId, String webJson) {
		this.roomId = roomId;
		this.webJson = webJson;
	}

	public NmsRoomPage(Integer id, Integer roomId, String webJson) {
		this.id = id;
		this.roomId = roomId;
		this.webJson = webJson;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public String getWebJson() {
		return webJson;
	}

	public void setWebJson(String webJson) {
		this.webJson = webJson;
	}

	public String getRoomLocation() {
		return roomLocation;
	}

	public void setRoomLocation(String roomLocation) {
		this.roomLocation = roomLocation;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	@Override
	public String toString() {
		return "NmsRoomPage{" +
				"id=" + id +
				", roomId=" + roomId +
				", webJson='" + webJson + '\'' +
				", roomLocation='" + roomLocation + '\'' +
				", roomCode='" + roomCode + '\'' +
				'}';
	}
}