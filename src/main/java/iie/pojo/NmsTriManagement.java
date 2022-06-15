package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsAsset entity. @author MyEclipse Persistence Tools
 */
public class NmsTriManagement implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsAsset nmsAsset;
	private String manageName;
	private String manageUrl;
	private Integer deled;
	private Timestamp createTime;
	private Timestamp modifyTime;
	/** default constructor */
	public NmsTriManagement() {
	}

	/** full constructor */
	public NmsTriManagement(NmsAsset nmsAsset, String manageName, String manageUrl,Integer deled, Timestamp createTime,Timestamp modifyTime) {
		this.nmsAsset = nmsAsset;
		this.manageName = manageName;
		this.manageUrl = manageUrl;
		this.deled = deled;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	// Property accessors

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsAsset getNmsAsset() {
		return nmsAsset;
	}

	public void setNmsAsset(NmsAsset nmsAsset) {
		this.nmsAsset = nmsAsset;
	}

	public String getManageName() {
		return manageName;
	}

	public void setManageName(String manageName) {
		this.manageName = manageName;
	}

	public String getManageUrl() {
		return manageUrl;
	}

	public void setManageUrl(String manageUrl) {
		this.manageUrl = manageUrl;
	}

	public Integer getDeled() {
		return deled;
	}

	public void setDeled(Integer deled) {
		this.deled = deled;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
}