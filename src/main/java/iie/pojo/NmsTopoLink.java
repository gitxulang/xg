package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsTopoLink entity. @author MyEclipse Persistence Tools
 */

public class NmsTopoLink implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private NmsTopoMap nmsTopoMap;
	private String SNodeId;
	private String SIndex;
	private String SIp;
	private String SDesc;
	private String SMac;
	private String ENodeId;
	private String EIndex;
	private String EIp;
	private String EDesc;
	private String EMac;
	private Integer LType;
	private String LName;
	private String LDash;
	private Integer LWidth;
	private Integer LOffset;
	private String col1;
	private String col2;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsTopoLink() {
	}

	/** minimal constructor */
	public NmsTopoLink(NmsTopoMap nmsTopoMap, Timestamp itime) {
		this.nmsTopoMap = nmsTopoMap;
		this.itime = itime;
	}

	/** full constructor */
	public NmsTopoLink(NmsTopoMap nmsTopoMap, String SNodeId, String SIndex,
			String SIp, String SDesc, String SMac, String ENodeId,
			String EIndex, String EIp, String EDesc, String EMac,
			Integer LType, String LName, String LDash, Integer LWidth,
			Integer LOffset, String col1, String col2, Timestamp itime) {
		this.nmsTopoMap = nmsTopoMap;
		this.SNodeId = SNodeId;
		this.SIndex = SIndex;
		this.SIp = SIp;
		this.SDesc = SDesc;
		this.SMac = SMac;
		this.ENodeId = ENodeId;
		this.EIndex = EIndex;
		this.EIp = EIp;
		this.EDesc = EDesc;
		this.EMac = EMac;
		this.LType = LType;
		this.LName = LName;
		this.LDash = LDash;
		this.LWidth = LWidth;
		this.LOffset = LOffset;
		this.col1 = col1;
		this.col2 = col2;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsTopoMap getNmsTopoMap() {
		return this.nmsTopoMap;
	}

	public void setNmsTopoMap(NmsTopoMap nmsTopoMap) {
		this.nmsTopoMap = nmsTopoMap;
	}

	public String getSNodeId() {
		return this.SNodeId;
	}

	public void setSNodeId(String SNodeId) {
		this.SNodeId = SNodeId;
	}

	public String getSIndex() {
		return this.SIndex;
	}

	public void setSIndex(String SIndex) {
		this.SIndex = SIndex;
	}

	public String getSIp() {
		return this.SIp;
	}

	public void setSIp(String SIp) {
		this.SIp = SIp;
	}

	public String getSDesc() {
		return this.SDesc;
	}

	public void setSDesc(String SDesc) {
		this.SDesc = SDesc;
	}

	public String getSMac() {
		return this.SMac;
	}

	public void setSMac(String SMac) {
		this.SMac = SMac;
	}

	public String getENodeId() {
		return this.ENodeId;
	}

	public void setENodeId(String ENodeId) {
		this.ENodeId = ENodeId;
	}

	public String getEIndex() {
		return this.EIndex;
	}

	public void setEIndex(String EIndex) {
		this.EIndex = EIndex;
	}

	public String getEIp() {
		return this.EIp;
	}

	public void setEIp(String EIp) {
		this.EIp = EIp;
	}

	public String getEDesc() {
		return this.EDesc;
	}

	public void setEDesc(String EDesc) {
		this.EDesc = EDesc;
	}

	public String getEMac() {
		return this.EMac;
	}

	public void setEMac(String EMac) {
		this.EMac = EMac;
	}

	public Integer getLType() {
		return this.LType;
	}

	public void setLType(Integer LType) {
		this.LType = LType;
	}

	public String getLName() {
		return this.LName;
	}

	public void setLName(String LName) {
		this.LName = LName;
	}

	public String getLDash() {
		return this.LDash;
	}

	public void setLDash(String LDash) {
		this.LDash = LDash;
	}

	public Integer getLWidth() {
		return this.LWidth;
	}

	public void setLWidth(Integer LWidth) {
		this.LWidth = LWidth;
	}

	public Integer getLOffset() {
		return this.LOffset;
	}

	public void setLOffset(Integer LOffset) {
		this.LOffset = LOffset;
	}

	public String getCol1() {
		return this.col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return this.col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}