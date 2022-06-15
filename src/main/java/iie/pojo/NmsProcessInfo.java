package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsProcessInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsProcessInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Long freq;
	private Long procId;
	private String procName;
	private String procPath;
	private Integer procState;
	private Float procCpu;
	private Float procMem;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsProcessInfo() {
	}

	/** minimal constructor */
	public NmsProcessInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsProcessInfo(NmsAsset nmsAsset, Long freq, Long procId,
			String procName, String procPath, Integer procState, Float procCpu,
			Float procMem, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.freq = freq;
		this.procId = procId;
		this.procName = procName;
		this.procPath = procPath;
		this.procState = procState;
		this.procCpu = procCpu;
		this.procMem = procMem;
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

	public Long getFreq() {
		return this.freq;
	}

	public void setFreq(Long freq) {
		this.freq = freq;
	}

	public Long getProcId() {
		return this.procId;
	}

	public void setProcId(Long procId) {
		this.procId = procId;
	}

	public String getProcName() {
		return this.procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public String getProcPath() {
		return this.procPath;
	}

	public void setProcPath(String procPath) {
		this.procPath = procPath;
	}

	public Integer getProcState() {
		return this.procState;
	}

	public void setProcState(Integer procState) {
		this.procState = procState;
	}

	public Float getProcCpu() {
		return this.procCpu;
	}

	public void setProcCpu(Float procCpu) {
		this.procCpu = procCpu;
	}

	public Float getProcMem() {
		return this.procMem;
	}

	public void setProcMem(Float procMem) {
		this.procMem = procMem;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}