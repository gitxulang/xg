package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsBmcInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsBmcInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Integer cpuNum;
	private String cpuTemp;
	private Integer dimmNum;
	private String dimmTemp;
	private Float raidTemp;
	private Float inletTemp;
	private Float outletTemp;
	private Integer fanNum;
	private String fanSpeed;
	private Integer psuNum;
	private String psuTemp;
	private String psuPower;
	private Float voltage12v;
	private Float voltage5v;
	private Float voltage3v3;
	private Float voltageBat;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsBmcInfo() {
	}

	/** minimal constructor */
	public NmsBmcInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsBmcInfo(NmsAsset nmsAsset, Integer cpuNum, String cpuTemp,
			Integer dimmNum, String dimmTemp, Float raidTemp, Float inletTemp,
			Float outletTemp, Integer fanNum, String fanSpeed, Integer psuNum,
			String psuTemp, String psuPower, Float voltage12v, Float voltage5v,
			Float voltage3v3, Float voltageBat, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.cpuNum = cpuNum;
		this.cpuTemp = cpuTemp;
		this.dimmNum = dimmNum;
		this.dimmTemp = dimmTemp;
		this.raidTemp = raidTemp;
		this.inletTemp = inletTemp;
		this.outletTemp = outletTemp;
		this.fanNum = fanNum;
		this.fanSpeed = fanSpeed;
		this.psuNum = psuNum;
		this.psuTemp = psuTemp;
		this.psuPower = psuPower;
		this.voltage12v = voltage12v;
		this.voltage5v = voltage5v;
		this.voltage3v3 = voltage3v3;
		this.voltageBat = voltageBat;
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

	public Integer getCpuNum() {
		return this.cpuNum;
	}

	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}

	public String getCpuTemp() {
		return this.cpuTemp;
	}

	public void setCpuTemp(String cpuTemp) {
		this.cpuTemp = cpuTemp;
	}

	public Integer getDimmNum() {
		return this.dimmNum;
	}

	public void setDimmNum(Integer dimmNum) {
		this.dimmNum = dimmNum;
	}

	public String getDimmTemp() {
		return this.dimmTemp;
	}

	public void setDimmTemp(String dimmTemp) {
		this.dimmTemp = dimmTemp;
	}

	public Float getRaidTemp() {
		return this.raidTemp;
	}

	public void setRaidTemp(Float raidTemp) {
		this.raidTemp = raidTemp;
	}

	public Float getInletTemp() {
		return this.inletTemp;
	}

	public void setInletTemp(Float inletTemp) {
		this.inletTemp = inletTemp;
	}

	public Float getOutletTemp() {
		return this.outletTemp;
	}

	public void setOutletTemp(Float outletTemp) {
		this.outletTemp = outletTemp;
	}

	public Integer getFanNum() {
		return this.fanNum;
	}

	public void setFanNum(Integer fanNum) {
		this.fanNum = fanNum;
	}

	public String getFanSpeed() {
		return this.fanSpeed;
	}

	public void setFanSpeed(String fanSpeed) {
		this.fanSpeed = fanSpeed;
	}

	public Integer getPsuNum() {
		return this.psuNum;
	}

	public void setPsuNum(Integer psuNum) {
		this.psuNum = psuNum;
	}

	public String getPsuTemp() {
		return this.psuTemp;
	}

	public void setPsuTemp(String psuTemp) {
		this.psuTemp = psuTemp;
	}

	public String getPsuPower() {
		return this.psuPower;
	}

	public void setPsuPower(String psuPower) {
		this.psuPower = psuPower;
	}

	public Float getVoltage12v() {
		return this.voltage12v;
	}

	public void setVoltage12v(Float voltage12v) {
		this.voltage12v = voltage12v;
	}

	public Float getVoltage5v() {
		return this.voltage5v;
	}

	public void setVoltage5v(Float voltage5v) {
		this.voltage5v = voltage5v;
	}

	public Float getVoltage3v3() {
		return this.voltage3v3;
	}

	public void setVoltage3v3(Float voltage3v3) {
		this.voltage3v3 = voltage3v3;
	}

	public Float getVoltageBat() {
		return this.voltageBat;
	}

	public void setVoltageBat(Float voltageBat) {
		this.voltageBat = voltageBat;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}