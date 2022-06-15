package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsTopoMeta entity. @author MyEclipse Persistence Tools
 */

public class NmsTopoMeta implements java.io.Serializable {

	// Fields

	private Integer id;
	private String MName;
	private String MUrl;
	private Integer MWid;
	private Integer MHei;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsTopoMeta() {
	}

	/** minimal constructor */
	public NmsTopoMeta(String MName, Timestamp itime) {
		this.MName = MName;
		this.itime = itime;
	}

	/** full constructor */
	public NmsTopoMeta(String MName, String MUrl, Integer MWid, Integer MHei,
			Timestamp itime) {
		this.MName = MName;
		this.MUrl = MUrl;
		this.MWid = MWid;
		this.MHei = MHei;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMName() {
		return this.MName;
	}

	public void setMName(String MName) {
		this.MName = MName;
	}

	public String getMUrl() {
		return this.MUrl;
	}

	public void setMUrl(String MUrl) {
		this.MUrl = MUrl;
	}

	public Integer getMWid() {
		return this.MWid;
	}

	public void setMWid(Integer MWid) {
		this.MWid = MWid;
	}

	public Integer getMHei() {
		return this.MHei;
	}

	public void setMHei(Integer MHei) {
		this.MHei = MHei;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}