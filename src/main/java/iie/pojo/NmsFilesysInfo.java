package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsFilesysInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsFilesysInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Integer freq;
	private String fileSys;
	private String fileType;
	private Float partTotal;
	private Float partFree;
	private Integer partInodeNum;
	private Integer partInodeFree;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsFilesysInfo() {
	}

	/** minimal constructor */
	public NmsFilesysInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsFilesysInfo(NmsAsset nmsAsset, Integer freq, String fileSys,
			String fileType, Float partTotal, Float partFree,
			Integer partInodeNum, Integer partInodeFree, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.freq = freq;
		this.fileSys = fileSys;
		this.fileType = fileType;
		this.partTotal = partTotal;
		this.partFree = partFree;
		this.partInodeNum = partInodeNum;
		this.partInodeFree = partInodeFree;
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

	public String getFileSys() {
		return this.fileSys;
	}

	public void setFileSys(String fileSys) {
		this.fileSys = fileSys;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Float getPartTotal() {
		return this.partTotal;
	}

	public void setPartTotal(Float partTotal) {
		this.partTotal = partTotal;
	}

	public Float getPartFree() {
		return this.partFree;
	}

	public void setPartFree(Float partFree) {
		this.partFree = partFree;
	}

	public Integer getPartInodeNum() {
		return this.partInodeNum;
	}

	public void setPartInodeNum(Integer partInodeNum) {
		this.partInodeNum = partInodeNum;
	}

	public Integer getPartInodeFree() {
		return this.partInodeFree;
	}

	public void setPartInodeFree(Integer partInodeFree) {
		this.partInodeFree = partInodeFree;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}