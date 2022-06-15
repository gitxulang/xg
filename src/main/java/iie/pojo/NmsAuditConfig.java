package iie.pojo;

import java.sql.Timestamp;

public class NmsAuditConfig implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String dbbasedir;
	private String dbdatadir;
	private long dbdatasize;
	private String partdir;
	private long partsize;
	private long partused;
	private Integer rule;
	private String alarm;
	private Timestamp itime;
	
	public NmsAuditConfig() {
		
	}
	
	
	public NmsAuditConfig(int id, String dbbasedir, String dbdatadir, 
			long dbdatasize, String partdir, long partsize, long partused, 
			Integer rule, String alarm, Timestamp itime) {
		this.id = id;
		this.dbbasedir = dbbasedir;
		this.dbdatadir = dbdatadir;
		this.dbdatasize = dbdatasize;
		this.partdir = partdir;
		this.partsize = partsize;
		this.partused = partused;
		this.rule = rule;
		this.alarm = alarm;
		this.itime = itime;
	}	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDbbasedir() {
		return dbbasedir;
	}
	
	public void setDbbasedir(String dbbasedir) {
		this.dbbasedir = dbbasedir;
	}
	
	public String getDbdatadir() {
		return dbdatadir;
	}
	
	public void setDbdatadir(String dbdatadir) {
		this.dbdatadir = dbdatadir;
	}
	
	public long getDbdatasize() {
		return dbdatasize;
	}
	
	public void setDbdatasize(long dbdatasize) {
		this.dbdatasize = dbdatasize;
	}
	
	public String getPartdir() {
		return partdir;
	}
	
	public void setPartdir(String partdir) {
		this.partdir = partdir;
	}
	
	public long getPartsize() {
		return partsize;
	}
	
	public void setPartsize(long partsize) {
		this.partsize = partsize;
	}
	
	public Integer getRule() {
		return rule;
	}
	
	public void setRule(Integer rule) {
		this.rule = rule;
	}
	
	public String getAlarm() {
		return alarm;
	}
	
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	
	public Timestamp getItime() {
		return itime;
	}
	
	public void setItime(Timestamp itime) {
		this.itime = itime;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getPartused() {
		return partused;
	}

	public void setPartused(long partused) {
		this.partused = partused;
	}
}
