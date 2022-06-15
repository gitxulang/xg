package iie.tools;

import java.sql.Timestamp;

public class NmsRuleItem {
	private Integer id;
	private Integer ATypeId;
	private String chtype;
	private String chSubType;
	private String rContent;
	private String rUnit;
	private Integer rEnable;
	private Long rValue1;
	private Long rValue2;
	private Long rValue3;
	private Integer rSeq;
	private Timestamp itime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getATypeId() {
		return ATypeId;
	}

	public void setATypeId(Integer aTypeId) {
		ATypeId = aTypeId;
	}

	public String getChtype() {
		return chtype;
	}

	public void setChtype(String chtype) {
		this.chtype = chtype;
	}

	public String getChSubType() {
		return chSubType;
	}

	public void setChSubType(String chSubType) {
		this.chSubType = chSubType;
	}

	public String getrContent() {
		return rContent;
	}

	public void setrContent(String rContent) {
		this.rContent = rContent;
	}

	public String getrUnit() {
		return rUnit;
	}

	public void setrUnit(String rUnit) {
		this.rUnit = rUnit;
	}

	public Integer getrEnable() {
		return rEnable;
	}

	public void setrEnable(Integer rEnable) {
		this.rEnable = rEnable;
	}

	public Long getrValue1() {
		return rValue1;
	}

	public void setrValue1(Long rValue1) {
		this.rValue1 = rValue1;
	}

	public Long getrValue2() {
		return rValue2;
	}

	public void setrValue2(Long rValue2) {
		this.rValue2 = rValue2;
	}

	public Long getrValue3() {
		return rValue3;
	}

	public void setrValue3(Long rValue3) {
		this.rValue3 = rValue3;
	}

	public Timestamp getItime() {
		return itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public Integer getrSeq() {
		return rSeq;
	}

	public void setrSeq(Integer rSeq) {
		this.rSeq = rSeq;
	}
}
