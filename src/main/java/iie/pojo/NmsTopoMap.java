package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsTopoMap entity. @author MyEclipse Persistence Tools
 */

public class NmsTopoMap implements java.io.Serializable {

	// Fields

	private Integer id;
	private String TId;
	private String TName;
	private String TPicture;
	private Integer TType;
	private Float ZPercent;
	private Timestamp itime;
	private Set nmsTopoNodes = new HashSet(0);
	private Set nmsTopoLinks = new HashSet(0);

	// Constructors

	/** default constructor */
	public NmsTopoMap() {
	}

	/** minimal constructor */
	public NmsTopoMap(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsTopoMap(String TId, String TName, String TPicture, Integer TType,
			Float ZPercent, Timestamp itime, Set nmsTopoNodes, Set nmsTopoLinks) {
		this.TId = TId;
		this.TName = TName;
		this.TPicture = TPicture;
		this.TType = TType;
		this.ZPercent = ZPercent;
		this.itime = itime;
		this.nmsTopoNodes = nmsTopoNodes;
		this.nmsTopoLinks = nmsTopoLinks;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTId() {
		return this.TId;
	}

	public void setTId(String TId) {
		this.TId = TId;
	}

	public String getTName() {
		return this.TName;
	}

	public void setTName(String TName) {
		this.TName = TName;
	}

	public String getTPicture() {
		return this.TPicture;
	}

	public void setTPicture(String TPicture) {
		this.TPicture = TPicture;
	}

	public Integer getTType() {
		return this.TType;
	}

	public void setTType(Integer TType) {
		this.TType = TType;
	}

	public Float getZPercent() {
		return this.ZPercent;
	}

	public void setZPercent(Float ZPercent) {
		this.ZPercent = ZPercent;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public Set getNmsTopoNodes() {
		return this.nmsTopoNodes;
	}

	public void setNmsTopoNodes(Set nmsTopoNodes) {
		this.nmsTopoNodes = nmsTopoNodes;
	}

	public Set getNmsTopoLinks() {
		return this.nmsTopoLinks;
	}

	public void setNmsTopoLinks(Set nmsTopoLinks) {
		this.nmsTopoLinks = nmsTopoLinks;
	}

}