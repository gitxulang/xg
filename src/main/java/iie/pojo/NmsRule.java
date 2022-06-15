package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsRule entity. @author MyEclipse Persistence Tools
 */

public class NmsRule implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private NmsAssetType nmsAssetType;
	private Integer DType;
	private String RName;
	private String RContent;
	private String RUnit;
	private Integer RSeq;
	private Integer REnable;
	private Long RValue1;
	private Long RValue2;
	private Long RValue3;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsRule() {
	}

	/** minimal constructor */
	public NmsRule(Integer REnable, Timestamp itime) {
		this.REnable = REnable;
		this.itime = itime;
	}

	/** full constructor */
	public NmsRule(NmsAssetType nmsAssetType, Integer DType, String RName,
			String RContent, String RUnit, Integer RSeq, Integer REnable,
			Long RValue1, Long RValue2, Long RValue3, Timestamp itime) {
		this.nmsAssetType = nmsAssetType;
		this.DType = DType;
		this.RName = RName;
		this.RContent = RContent;
		this.RUnit = RUnit;
		this.RSeq = RSeq;
		this.REnable = REnable;
		this.RValue1 = RValue1;
		this.RValue2 = RValue2;
		this.RValue3 = RValue3;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsAssetType getNmsAssetType() {
		return this.nmsAssetType;
	}

	public void setNmsAssetType(NmsAssetType nmsAssetType) {
		this.nmsAssetType = nmsAssetType;
	}

	public Integer getDType() {
		return this.DType;
	}

	public void setDType(Integer DType) {
		this.DType = DType;
	}

	public String getRName() {
		return this.RName;
	}

	public void setRName(String RName) {
		this.RName = RName;
	}

	public String getRContent() {
		return this.RContent;
	}

	public void setRContent(String RContent) {
		this.RContent = RContent;
	}

	public String getRUnit() {
		return this.RUnit;
	}

	public void setRUnit(String RUnit) {
		this.RUnit = RUnit;
	}

	public Integer getRSeq() {
		return this.RSeq;
	}

	public void setRSeq(Integer RSeq) {
		this.RSeq = RSeq;
	}

	public Integer getREnable() {
		return this.REnable;
	}

	public void setREnable(Integer REnable) {
		this.REnable = REnable;
	}

	public Long getRValue1() {
		return this.RValue1;
	}

	public void setRValue1(Long RValue1) {
		this.RValue1 = RValue1;
	}

	public Long getRValue2() {
		return this.RValue2;
	}

	public void setRValue2(Long RValue2) {
		this.RValue2 = RValue2;
	}

	public Long getRValue3() {
		return this.RValue3;
	}

	public void setRValue3(Long RValue3) {
		this.RValue3 = RValue3;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}