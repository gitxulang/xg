package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsCpuInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsCpuInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Integer freq;
	private String cpuName;
	private String cpuCores;
	private String cpuFreq;
	private Float cpuRate;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsCpuInfo() {
	}

	/** minimal constructor */
	public NmsCpuInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsCpuInfo(NmsAsset nmsAsset, Integer freq, String cpuName,
			String cpuCores, String cpuFreq, Float cpuRate, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.freq = freq;
		this.cpuName = cpuName;
		this.cpuCores = cpuCores;
		this.cpuFreq = cpuFreq;
		this.cpuRate = cpuRate;
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

	public Integer getFreq() {
		return this.freq;
	}

	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	public String getCpuName() {
		return this.cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public String getCpuCores() {
		return this.cpuCores;
	}

	public void setCpuCores(String cpuCores) {
		this.cpuCores = cpuCores;
	}

	public String getCpuFreq() {
		return this.cpuFreq;
	}

	public void setCpuFreq(String cpuFreq) {
		this.cpuFreq = cpuFreq;
	}

	public Float getCpuRate() {
		return this.cpuRate;
	}

	public void setCpuRate(Float cpuRate) {
		this.cpuRate = cpuRate;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}