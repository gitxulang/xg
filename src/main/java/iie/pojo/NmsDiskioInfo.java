package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsDiskioInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsDiskioInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Integer freq;
	private String diskName;
	private String diskSn;
	private Float readNum;
	private Float writeNum;
	private Float kbRead;
	private Float kbWrtn;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsDiskioInfo() {
	}

	/** minimal constructor */
	public NmsDiskioInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsDiskioInfo(NmsAsset nmsAsset, Integer freq, String diskName,
			String diskSn, Float readNum, Float writeNum, Float kbRead,
			Float kbWrtn, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.freq = freq;
		this.diskName = diskName;
		this.diskSn = diskSn;
		this.readNum = readNum;
		this.writeNum = writeNum;
		this.kbRead = kbRead;
		this.kbWrtn = kbWrtn;
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

	public String getDiskName() {
		return this.diskName;
	}

	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}

	public String getDiskSn() {
		return this.diskSn;
	}

	public void setDiskSn(String diskSn) {
		this.diskSn = diskSn;
	}

	public Float getReadNum() {
		return this.readNum;
	}

	public void setReadNum(Float readNum) {
		this.readNum = readNum;
	}

	public Float getWriteNum() {
		return this.writeNum;
	}

	public void setWriteNum(Float writeNum) {
		this.writeNum = writeNum;
	}

	public Float getKbRead() {
		return this.kbRead;
	}

	public void setKbRead(Float kbRead) {
		this.kbRead = kbRead;
	}

	public Float getKbWrtn() {
		return this.kbWrtn;
	}

	public void setKbWrtn(Float kbWrtn) {
		this.kbWrtn = kbWrtn;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}