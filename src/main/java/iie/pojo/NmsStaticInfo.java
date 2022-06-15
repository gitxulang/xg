package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsStaticInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsStaticInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private String uniqueIdent;
	private String productName;
	private String manufacturer;
	private String cpuInfo;
	private String diskSn;
	private String sysName;
	private String sysArch;
	private Integer sysBits;
	private String sysVersion;
	private String coreVersion;
	private Integer netNum;
	private Integer cpuNum;
	private String socVersion;
	private String ioVersion;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsStaticInfo() {
	}

	/** minimal constructor */
	public NmsStaticInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsStaticInfo(NmsAsset nmsAsset, String uniqueIdent,
			String productName, String manufacturer, String cpuInfo,
			String diskSn, String sysName, String sysArch, Integer sysBits,
			String sysVersion, String coreVersion, Integer netNum,
			Integer cpuNum, String socVersion, String ioVersion, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.uniqueIdent = uniqueIdent;
		this.productName = productName;
		this.manufacturer = manufacturer;
		this.cpuInfo = cpuInfo;
		this.diskSn = diskSn;
		this.sysName = sysName;
		this.sysArch = sysArch;
		this.sysBits = sysBits;
		this.sysVersion = sysVersion;
		this.coreVersion = coreVersion;
		this.netNum = netNum;
		this.cpuNum = cpuNum;
		this.socVersion = socVersion;
		this.ioVersion = ioVersion;
		this.itime = itime;
	}
	
	// Property accessors

	public NmsStaticInfo(String uniqueIdent, String productName,
			String manufacturer, String cpuInfo, String diskSn, String sysName,
			String sysArch, Integer sysBits, String sysVersion,
			String coreVersion, Integer netNum, Integer cpuNum,
			String socVersion, String ioVersion) {
		super();
		this.uniqueIdent = uniqueIdent;
		this.productName = productName;
		this.manufacturer = manufacturer;
		this.cpuInfo = cpuInfo;
		this.diskSn = diskSn;
		this.sysName = sysName;
		this.sysArch = sysArch;
		this.sysBits = sysBits;
		this.sysVersion = sysVersion;
		this.coreVersion = coreVersion;
		this.netNum = netNum;
		this.cpuNum = cpuNum;
		this.socVersion = socVersion;
		this.ioVersion = ioVersion;
	}

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

	public String getUniqueIdent() {
		return this.uniqueIdent;
	}

	public void setUniqueIdent(String uniqueIdent) {
		this.uniqueIdent = uniqueIdent;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCpuInfo() {
		return this.cpuInfo;
	}

	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	public String getDiskSn() {
		return this.diskSn;
	}

	public void setDiskSn(String diskSn) {
		this.diskSn = diskSn;
	}

	public String getSysName() {
		return this.sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysArch() {
		return this.sysArch;
	}

	public void setSysArch(String sysArch) {
		this.sysArch = sysArch;
	}

	public Integer getSysBits() {
		return this.sysBits;
	}

	public void setSysBits(Integer sysBits) {
		this.sysBits = sysBits;
	}

	public String getSysVersion() {
		return this.sysVersion;
	}

	public void setSysVersion(String sysVersion) {
		this.sysVersion = sysVersion;
	}

	public String getCoreVersion() {
		return this.coreVersion;
	}

	public void setCoreVersion(String coreVersion) {
		this.coreVersion = coreVersion;
	}

	public Integer getNetNum() {
		return this.netNum;
	}

	public void setNetNum(Integer netNum) {
		this.netNum = netNum;
	}

	public Integer getCpuNum() {
		return this.cpuNum;
	}

	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}

	public String getSocVersion() {
		return this.socVersion;
	}

	public void setSocVersion(String socVersion) {
		this.socVersion = socVersion;
	}

	public String getIoVersion() {
		return this.ioVersion;
	}

	public void setIoVersion(String ioVersion) {
		this.ioVersion = ioVersion;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}