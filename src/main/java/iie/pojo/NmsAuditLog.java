package iie.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * NmsAuditLog entity. @author MyEclipse Persistence Tools
 */

public class NmsAuditLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String ip;
	private String username;
	private String content;
	private String modname;
	private String logtype;
	private String logrest;
	private String ATime;
	
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsAuditLog() {
	}

	/** minimal constructor */
	public NmsAuditLog(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsAuditLog(String ip, String content, String ATime,
			String username, Timestamp itime) {
		this.ip = ip;
		this.content = content;
		this.ATime = ATime;
		this.username = username;
		this.itime = itime;
	}
	
	public NmsAuditLog(String id, String ip, String username, String content, String modname, String logtype, String ATime,
			String logrest, Timestamp itime) {
		this.id = id;
		this.ip = ip;
		this.username = username;
		this.content = content;
		this.modname = modname;
		this.logtype = logtype;
		this.ATime = ATime;
		this.logrest = logrest;
		this.itime = itime;
	}


	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getATime() {
		return this.ATime;
	}

	public void setATime(String ATime) {
		this.ATime = ATime;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public String getModname() {
		return modname;
	}

	public void setModname(String modname) {
		this.modname = modname;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getLogrest() {
		return logrest;
	}

	public void setLogrest(String logrest) {
		this.logrest = logrest;
	}

	@Override
	public String toString() {
		return "NmsAuditLog{" +
				"ip='" + ip + '\'' +
				", username='" + username + '\'' +
				", content='" + content + '\'' +
				", modname='" + modname + '\'' +
				", logtype='" + logtype + '\'' +
				", logrest='" + logrest + '\'' +
				", ATime='" + ATime + '\'' +
				'}';
	}
}