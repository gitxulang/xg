package iie.pojo;

import iie.tools.MD5Tools;

import java.sql.Timestamp;

/**
 * NmsAdmin entity. @author MyEclipse Persistence Tools
 */

public class NmsAdmin implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsRole nmsRole;
	private NmsDepartment nmsDepartment;
	private String username;
	private String password;
	private String realname;
	private Integer sex;
	private String phone;
	private String email;
	private Integer deled;
	private Timestamp itime;
	private Timestamp lastErrorTime;
	private Integer loginCount;
	private Timestamp lastPasswordChangeTime;
	// Constructors

	/** default constructor */
	public NmsAdmin() {
	}

	/** minimal constructor */
	public NmsAdmin(Integer deled, Timestamp itime) {
		this.deled = deled;
		this.itime = itime;
	}

	/** full constructor */
	public NmsAdmin(NmsRole nmsRole, NmsDepartment nmsDepartment,
			String username, String password, String realname, Integer sex, String phone,
			String email, Integer deled, Timestamp itime) {
		this.nmsRole = nmsRole;
		this.nmsDepartment = nmsDepartment;
		this.username = username;
		this.password = MD5Tools.MD5(password);
		this.realname = realname;
		this.sex = sex;
		this.phone = phone;
		this.email = email;
		this.deled = deled;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsRole getNmsRole() {
		return this.nmsRole;
	}

	public void setNmsRole(NmsRole nmsRole) {
		this.nmsRole = nmsRole;
	}

	public NmsDepartment getNmsDepartment() {
		return this.nmsDepartment;
	}

	public void setNmsDepartment(NmsDepartment nmsDepartment) {
		this.nmsDepartment = nmsDepartment;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getDeled() {
		return this.deled;
	}

	public void setDeled(Integer deled) {
		this.deled = deled;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}
	
	public Timestamp getLastErrorTime() {
		return this.lastErrorTime;
	}

	public void setLastErrorTime(Timestamp lastErrorTime) {
		this.lastErrorTime = lastErrorTime;
	}

	public Integer getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	
	public Timestamp getLastPasswordChangeTime() {
		return this.lastPasswordChangeTime;
	}

	public void setLastPasswordChangeTime(Timestamp lastPasswordChangeTime) {
		this.lastPasswordChangeTime = lastPasswordChangeTime;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}	

}