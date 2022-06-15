package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsDynamicInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsDynamicInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Long sysUptime;
	private Long sysUpdateTime;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsDynamicInfo() {
	}

	/** minimal constructor */
	public NmsDynamicInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsDynamicInfo(NmsAsset nmsAsset, Long sysUptime,
			Long sysUpdateTime, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.sysUptime = sysUptime;
		this.sysUpdateTime = sysUpdateTime;
		this.itime = itime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NmsAsset getNmsAsset() {
		return this.nmsAsset;
	}

	public void setNmsAsset(NmsAsset nmsAsset) {
		this.nmsAsset = nmsAsset;
	}

	public Long getSysUptime() {
		return this.sysUptime;
	}

	public void setSysUptime(Long sysUptime) {
		this.sysUptime = sysUptime;
	}

	public Long getSysUpdateTime() {
		return this.sysUpdateTime;
	}

	public void setSysUpdateTime(Long sysUpdateTime) {
		this.sysUpdateTime = sysUpdateTime;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}