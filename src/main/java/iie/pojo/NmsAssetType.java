package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsAssetType entity. @author MyEclipse Persistence Tools
 */

public class NmsAssetType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String chType;
	private String chSubtype;
	private String nodeTag;
	private String imgUrl;
	private String imgWid;
	private String imgHei;
	private Integer relId;
	private Timestamp itime;
	private Set nmsAssets = new HashSet(0);
	private Set nmsTopoNodes = new HashSet(0);
	private Set nmsRules = new HashSet(0);
	private Set nmsRuleAssetIndexes = new HashSet(0);
	private Set nmsRuleAssets = new HashSet(0);

	// Constructors

	/** default constructor */
	public NmsAssetType() {
	}

	/** minimal constructor */
	public NmsAssetType(String chType, String chSubtype, String nodeTag,
			Integer relId, Timestamp itime) {
		this.chType = chType;
		this.chSubtype = chSubtype;
		this.nodeTag = nodeTag;
		this.relId = relId;
		this.itime = itime;
	}

	/** full constructor */
	public NmsAssetType(String chType, String chSubtype, String nodeTag,
			String imgUrl, String imgWid, String imgHei, Integer relId,
			Timestamp itime, Set nmsAssets, Set nmsTopoNodes, Set nmsRules,
			Set nmsRuleAssetIndexes, Set nmsRuleAssets) {
		this.chType = chType;
		this.chSubtype = chSubtype;
		this.nodeTag = nodeTag;
		this.imgUrl = imgUrl;
		this.imgWid = imgWid;
		this.imgHei = imgHei;
		this.relId = relId;
		this.itime = itime;
		this.nmsAssets = nmsAssets;
		this.nmsTopoNodes = nmsTopoNodes;
		this.nmsRules = nmsRules;
		this.nmsRuleAssetIndexes = nmsRuleAssetIndexes;
		this.nmsRuleAssets = nmsRuleAssets;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChType() {
		return this.chType;
	}

	public void setChType(String chType) {
		this.chType = chType;
	}

	public String getChSubtype() {
		return this.chSubtype;
	}

	public void setChSubtype(String chSubtype) {
		this.chSubtype = chSubtype;
	}

	public String getNodeTag() {
		return this.nodeTag;
	}

	public void setNodeTag(String nodeTag) {
		this.nodeTag = nodeTag;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgWid() {
		return this.imgWid;
	}

	public void setImgWid(String imgWid) {
		this.imgWid = imgWid;
	}

	public String getImgHei() {
		return this.imgHei;
	}

	public void setImgHei(String imgHei) {
		this.imgHei = imgHei;
	}

	public Integer getRelId() {
		return this.relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public Set getNmsAssets() {
		return this.nmsAssets;
	}

	public void setNmsAssets(Set nmsAssets) {
		this.nmsAssets = nmsAssets;
	}

	public Set getNmsTopoNodes() {
		return this.nmsTopoNodes;
	}

	public void setNmsTopoNodes(Set nmsTopoNodes) {
		this.nmsTopoNodes = nmsTopoNodes;
	}

	public Set getNmsRules() {
		return this.nmsRules;
	}

	public void setNmsRules(Set nmsRules) {
		this.nmsRules = nmsRules;
	}

	public Set getNmsRuleAssetIndexes() {
		return this.nmsRuleAssetIndexes;
	}

	public void setNmsRuleAssetIndexes(Set nmsRuleAssetIndexes) {
		this.nmsRuleAssetIndexes = nmsRuleAssetIndexes;
	}

	public Set getNmsRuleAssets() {
		return this.nmsRuleAssets;
	}

	public void setNmsRuleAssets(Set nmsRuleAssets) {
		this.nmsRuleAssets = nmsRuleAssets;
	}

}