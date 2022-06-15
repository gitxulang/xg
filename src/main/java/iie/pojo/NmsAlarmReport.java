package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsAlarmReport entity. @author MyEclipse Persistence Tools
 */

public class NmsAlarmReport implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsAlarm nmsAlarm;
	private String RPeople;
	private String RContent;
	private String DTime;
	private Timestamp RTime;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsAlarmReport() {
	}

	/** minimal constructor */
	public NmsAlarmReport(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsAlarmReport(NmsAlarm nmsAlarm, String RPeople, String RContent,
			String DTime, Timestamp RTime, Timestamp itime) {
		this.nmsAlarm = nmsAlarm;
		this.RPeople = RPeople;
		this.RContent = RContent;
		this.DTime = DTime;
		this.RTime = RTime;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsAlarm getNmsAlarm() {
		return this.nmsAlarm;
	}

	public void setNmsAlarm(NmsAlarm nmsAlarm) {
		this.nmsAlarm = nmsAlarm;
	}

	public String getRPeople() {
		return this.RPeople;
	}

	public void setRPeople(String RPeople) {
		this.RPeople = RPeople;
	}

	public String getRContent() {
		return this.RContent;
	}

	public void setRContent(String RContent) {
		this.RContent = RContent;
	}

	public String getDTime() {
		return this.DTime;
	}

	public void setDTime(String DTime) {
		this.DTime = DTime;
	}

	public Timestamp getRTime() {
		return this.RTime;
	}

	public void setRTime(Timestamp RTime) {
		this.RTime = RTime;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}