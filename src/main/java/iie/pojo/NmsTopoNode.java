package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsTopoNode entity. @author MyEclipse Persistence Tools
 */

public class NmsTopoNode implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsAssetType nmsAssetType;
	private NmsTopoMap nmsTopoMap;
	private String nodeId;
	private String img;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
	private String ip;
	private String alias;
	private String relMap;
	private String col1;
	private String col2;
	private Integer containerId;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsTopoNode() {
	}

	/** minimal constructor */
	public NmsTopoNode(NmsTopoMap nmsTopoMap, Timestamp itime) {
		this.nmsTopoMap = nmsTopoMap;
		this.itime = itime;
	}

	/** full constructor */
	public NmsTopoNode(NmsAssetType nmsAssetType, NmsTopoMap nmsTopoMap,
			String nodeId, String img, Integer x, Integer y, Integer width,
			Integer height, String ip, String alias, String relMap,
			String col1, String col2, Integer containerId,  Timestamp itime) {
		this.nmsAssetType = nmsAssetType;
		this.nmsTopoMap = nmsTopoMap;
		this.nodeId = nodeId;
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.ip = ip;
		this.alias = alias;
		this.relMap = relMap;
		this.col1 = col1;
		this.col2 = col2;
		this.containerId=containerId;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsAssetType getNmsAssetType() {
		return this.nmsAssetType;
	}

	public void setNmsAssetType(NmsAssetType nmsAssetType) {
		this.nmsAssetType = nmsAssetType;
	}

	public NmsTopoMap getNmsTopoMap() {
		return this.nmsTopoMap;
	}

	public void setNmsTopoMap(NmsTopoMap nmsTopoMap) {
		this.nmsTopoMap = nmsTopoMap;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getX() {
		return this.x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return this.y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getRelMap() {
		return this.relMap;
	}

	public void setRelMap(String relMap) {
		this.relMap = relMap;
	}

	public String getCol1() {
		return this.col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return this.col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}
	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

}