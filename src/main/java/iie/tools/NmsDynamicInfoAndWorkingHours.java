package iie.tools;

import iie.pojo.NmsAsset;

import java.sql.Timestamp;

public class NmsDynamicInfoAndWorkingHours {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Long sysUptime;
	private String workingHours;
	private Long sysUpdateTime;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsDynamicInfoAndWorkingHours() {
	}

	/** minimal constructor */
	public NmsDynamicInfoAndWorkingHours(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsDynamicInfoAndWorkingHours(NmsAsset nmsAsset, Long sysUptime,
			String workingHours, Long sysUpdateTime, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.sysUptime = sysUptime;
		this.workingHours = workingHours;
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

	public String getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}

}