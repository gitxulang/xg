package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsRole entity. @author MyEclipse Persistence Tools
 */

public class NmsRole implements java.io.Serializable {

	// Fields

	private Integer id;
	private String role;
	private Integer deled;
	private Timestamp itime;
	private Set nmsAdmins = new HashSet(0);
	private Set nmsRoleFunctions = new HashSet(0);

	// Constructors

	/** default constructor */
	public NmsRole() {
	}

	/** minimal constructor */
	public NmsRole(String role, Timestamp itime) {
		this.role = role;
		this.itime = itime;
	}

	/** full constructor */
	public NmsRole(String role, Integer deled, Timestamp itime, Set nmsAdmins,
			Set nmsRoleFunctions) {
		this.role = role;
		this.deled = deled;
		this.itime = itime;
		this.nmsAdmins = nmsAdmins;
		this.nmsRoleFunctions = nmsRoleFunctions;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public Set getNmsAdmins() {
		return this.nmsAdmins;
	}

	public void setNmsAdmins(Set nmsAdmins) {
		this.nmsAdmins = nmsAdmins;
	}

	public Set getNmsRoleFunctions() {
		return this.nmsRoleFunctions;
	}

	public void setNmsRoleFunctions(Set nmsRoleFunctions) {
		this.nmsRoleFunctions = nmsRoleFunctions;
	}

}