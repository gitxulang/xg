package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsTomcatInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsTomcatInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private String vmName;
	private String vmVersion;
	private String vmVendor;
	private String jitName;
	private String startTime;
	private String classPath;
	private Integer maxHeapMemory;
	private Integer commitHeapMemory;
	private Integer usedHeapMemory;
	private Integer commitNonHeapMemory;
	private Integer usedNonHeapMemory;
	private Integer threadCount;
	private Integer loadedClassCount;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsTomcatInfo() {
	}

	/** minimal constructor */
	public NmsTomcatInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsTomcatInfo(NmsAsset nmsAsset, String vmName, String vmVersion, String vmVendor, String jitName,
			String startTime, String classPath, Integer maxHeapMemory, Integer commitHeapMemory, Integer usedHeapMemory,
			Integer commitNonHeapMemory, Integer usedNonHeapMemory, Integer threadCount, Integer loadedClassCount,
			Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.vmName = vmName;
		this.vmVersion = vmVersion;
		this.vmVendor = vmVendor;
		this.jitName = jitName;
		this.startTime = startTime;
		this.classPath = classPath;
		this.maxHeapMemory = maxHeapMemory;
		this.commitHeapMemory = commitHeapMemory;
		this.usedHeapMemory = usedHeapMemory;
		this.commitNonHeapMemory = commitNonHeapMemory;
		this.usedNonHeapMemory = usedNonHeapMemory;
		this.threadCount = threadCount;
		this.loadedClassCount = loadedClassCount;
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

	public String getVmName() {
		return this.vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmVersion() {
		return this.vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public String getVmVendor() {
		return this.vmVendor;
	}

	public void setVmVendor(String vmVendor) {
		this.vmVendor = vmVendor;
	}

	public String getJitName() {
		return this.jitName;
	}

	public void setJitName(String jitName) {
		this.jitName = jitName;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public Integer getMaxHeapMemory() {
		return this.maxHeapMemory;
	}

	public void setMaxHeapMemory(Integer maxHeapMemory) {
		this.maxHeapMemory = maxHeapMemory;
	}

	public Integer getCommitHeapMemory() {
		return this.commitHeapMemory;
	}

	public void setCommitHeapMemory(Integer commitHeapMemory) {
		this.commitHeapMemory = commitHeapMemory;
	}

	public Integer getUsedHeapMemory() {
		return this.usedHeapMemory;
	}

	public void setUsedHeapMemory(Integer usedHeapMemory) {
		this.usedHeapMemory = usedHeapMemory;
	}

	public Integer getCommitNonHeapMemory() {
		return this.commitNonHeapMemory;
	}

	public void setCommitNonHeapMemory(Integer commitNonHeapMemory) {
		this.commitNonHeapMemory = commitNonHeapMemory;
	}

	public Integer getUsedNonHeapMemory() {
		return this.usedNonHeapMemory;
	}

	public void setUsedNonHeapMemory(Integer usedNonHeapMemory) {
		this.usedNonHeapMemory = usedNonHeapMemory;
	}

	public Integer getThreadCount() {
		return this.threadCount;
	}

	public void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}

	public Integer getLoadedClassCount() {
		return this.loadedClassCount;
	}

	public void setLoadedClassCount(Integer loadedClassCount) {
		this.loadedClassCount = loadedClassCount;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}