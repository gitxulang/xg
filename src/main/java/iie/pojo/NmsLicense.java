package iie.pojo;

import java.sql.Timestamp;


public class NmsLicense implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String license;
	private Long regtime;
	private Timestamp itime;

	public NmsLicense() {
		
	}


	public NmsLicense(Timestamp itime) {
		this.itime = itime;
	}

	public NmsLicense(String license, Long regtime, Timestamp itime) {
		this.license = license;
		this.regtime = regtime;
		this.itime = itime;
	}
	
	public NmsLicense(Integer id, String license, Long regtime, Timestamp itime) {
		this.id = id;
		this.license = license;
		this.regtime = regtime;
		this.itime = itime;
	}

	public NmsLicense(Integer id, String license, Long regtime) {
		this.id = id;
		this.license = license;
		this.regtime = regtime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicense() {
		return this.license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Long getRegtime() {
		return this.regtime;
	}

	public void setRegtime(Long regtime) {
		this.regtime = regtime;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}