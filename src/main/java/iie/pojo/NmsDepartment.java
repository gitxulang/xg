package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsDepartment entity. @author MyEclipse Persistence Tools
 */

public class NmsDepartment implements java.io.Serializable {

	private String id;
	private String ParentId;
	private String DName;
	private String DDesc;
	private Integer deled;
	private Timestamp itime;
	private Set nmsAdmins = new HashSet(0);
	private Set nmsAssets = new HashSet(0);

	/** default constructor */
	public NmsDepartment() {
	}

	/** minimal constructor */
	public NmsDepartment(String id) {
		this.id = id;
	}

	public NmsDepartment(String parentId, String DName, String DDesc, Integer deled, Timestamp itime) {
		ParentId = parentId;
		this.DName = DName;
		this.DDesc = DDesc;
		this.deled = deled;
		this.itime = itime;
	}

	public NmsDepartment(String ParentId, String DName, String DDesc, Timestamp itime) {
		this.setParentId(ParentId);
		this.DName = DName;
		this.DDesc = DDesc;
		this.itime = itime;
	}
	/** full constructor */
	public NmsDepartment(String ParentId, String DName, String DDesc, Integer deled,
			Timestamp itime, Set nmsAdmins) {
		this.setParentId(ParentId);
		this.DName = DName;
		this.DDesc = DDesc;
		this.deled = deled;
		this.itime = itime;
		this.nmsAdmins = nmsAdmins;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDName() {
		return this.DName;
	}

	public void setDName(String DName) {
		this.DName = DName;
	}

	public String getDDesc() {
		return this.DDesc;
	}

	public void setDDesc(String DDesc) {
		this.DDesc = DDesc;
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

	public Set getNmsAssets() {
		return nmsAssets;
	}

	public void setNmsAssets(Set nmsAssets) {
		this.nmsAssets = nmsAssets;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String parentId) {
		ParentId = parentId;
	}

}