package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsAlarm entity. @author MyEclipse Persistence Tools
 */

public class NmsAlarm implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsAsset nmsAsset;
	private String AName;
	private String AIndex;
	private String AType;
	private String AContent;
	private Integer ALevel;
	private Integer DStatus;
	private String DPeople;
	private String DTime;
	private String STime;
	private String ATime;
	private Integer ACount;
	private Timestamp itime;
	private Set nmsAlarmReports = new HashSet(0);

	// Constructors

	/** default constructor */
	public NmsAlarm() {
	}

	/** minimal constructor */
	public NmsAlarm(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsAlarm(NmsAsset nmsAsset, String AName, String AIndex,
			String AType, String AContent, Integer ALevel, Integer DStatus,
			String DPeople, String DTime, String STime, String ATime, Integer ACount, Timestamp itime,
			Set nmsAlarmReports) {
		this.nmsAsset = nmsAsset;
		this.AName = AName;
		this.AIndex = AIndex;
		this.AType = AType;
		this.AContent = AContent;
		this.ALevel = ALevel;
		this.DStatus = DStatus;
		this.DPeople = DPeople;
		this.DTime = DTime;
		this.STime = STime;
		this.ATime = ATime;
		this.ACount = ACount;
		this.itime = itime;
		this.nmsAlarmReports = nmsAlarmReports;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsAsset getNmsAsset() {
		return this.nmsAsset;
	}

	public void setNmsAsset(NmsAsset nmsAsset) {
		this.nmsAsset = nmsAsset;
	}

	public String getAName() {
		return this.AName;
	}

	public void setAName(String AName) {
		this.AName = AName;
	}

	public String getAIndex() {
		return this.AIndex;
	}

	public void setAIndex(String AIndex) {
		this.AIndex = AIndex;
	}

	public String getAType() {
		return this.AType;
	}

	public void setAType(String AType) {
		this.AType = AType;
	}

	public String getAContent() {
		return this.AContent;
	}

	public void setAContent(String AContent) {
		this.AContent = AContent;
	}

	public Integer getALevel() {
		return this.ALevel;
	}

	public void setALevel(Integer ALevel) {
		this.ALevel = ALevel;
	}

	public Integer getDStatus() {
		return this.DStatus;
	}

	public void setDStatus(Integer DStatus) {
		this.DStatus = DStatus;
	}

	public String getDPeople() {
		return this.DPeople;
	}

	public void setDPeople(String DPeople) {
		this.DPeople = DPeople;
	}

	public String getDTime() {
		return this.DTime;
	}

	public void setDTime(String DTime) {
		this.DTime = DTime;
	}

	public String getATime() {
		return this.ATime;
	}

	public void setATime(String ATime) {
		this.ATime = ATime;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public Set getNmsAlarmReports() {
		return this.nmsAlarmReports;
	}

	public void setNmsAlarmReports(Set nmsAlarmReports) {
		this.nmsAlarmReports = nmsAlarmReports;
	}
	public String getSTime() {
		return STime;
	}

	public void setSTime(String sTime) {
		STime = sTime;
	}

	public Integer getACount() {
		return ACount;
	}

	public void setACount(Integer aCount) {
		ACount = aCount;
	}
}