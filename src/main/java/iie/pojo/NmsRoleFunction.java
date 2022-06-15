package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsRoleFunction entity. @author MyEclipse Persistence Tools
 */

public class NmsRoleFunction implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsFunction nmsFunction;
	private NmsRole nmsRole;
	private Integer deled;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsRoleFunction() {
	}

	/** minimal constructor */
	public NmsRoleFunction(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsRoleFunction(NmsFunction nmsFunction, NmsRole nmsRole,
			Integer deled, Timestamp itime) {
		this.nmsFunction = nmsFunction;
		this.nmsRole = nmsRole;
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

	public NmsFunction getNmsFunction() {
		return this.nmsFunction;
	}

	public void setNmsFunction(NmsFunction nmsFunction) {
		this.nmsFunction = nmsFunction;
	}

	public NmsRole getNmsRole() {
		return this.nmsRole;
	}

	public void setNmsRole(NmsRole nmsRole) {
		this.nmsRole = nmsRole;
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

}