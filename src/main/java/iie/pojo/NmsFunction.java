package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsFunction entity. @author MyEclipse Persistence Tools
 */

public class NmsFunction implements java.io.Serializable {

	// Fields

	private Integer id;
	private String functionDesc;
	private String chineseDesc;
	private String levelDesc;
	private String fatherNode;
	private String funUrl;
	private String imgUrl;
	private Timestamp itime;
	private Set nmsRoleFunctions = new HashSet(0);

	// Constructors

	/** default constructor */
	public NmsFunction() {
	}

	/** minimal constructor */
	public NmsFunction(Timestamp itime) {
		this.itime = itime;
	}
	
	public NmsFunction(String functionDesc, String chineseDesc,
			String levelDesc, String fatherNode, String funUrl, String imgUrl,
			Timestamp itime) {
		this.functionDesc = functionDesc;
		this.chineseDesc = chineseDesc;
		this.levelDesc = levelDesc;
		this.fatherNode = fatherNode;
		this.funUrl = funUrl;
		this.imgUrl = imgUrl;
		this.itime = itime;
	}

	/** full constructor */
	public NmsFunction(String functionDesc, String chineseDesc,
			String levelDesc, String fatherNode, String funUrl, String imgUrl,
			Timestamp itime, Set nmsRoleFunctions) {
		this.functionDesc = functionDesc;
		this.chineseDesc = chineseDesc;
		this.levelDesc = levelDesc;
		this.fatherNode = fatherNode;
		this.funUrl = funUrl;
		this.imgUrl = imgUrl;
		this.itime = itime;
		this.nmsRoleFunctions = nmsRoleFunctions;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFunctionDesc() {
		return this.functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}

	public String getChineseDesc() {
		return this.chineseDesc;
	}

	public void setChineseDesc(String chineseDesc) {
		this.chineseDesc = chineseDesc;
	}

	public String getLevelDesc() {
		return this.levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

	public String getFatherNode() {
		return this.fatherNode;
	}

	public void setFatherNode(String fatherNode) {
		this.fatherNode = fatherNode;
	}

	public String getFunUrl() {
		return this.funUrl;
	}

	public void setFunUrl(String funUrl) {
		this.funUrl = funUrl;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public Set getNmsRoleFunctions() {
		return this.nmsRoleFunctions;
	}

	public void setNmsRoleFunctions(Set nmsRoleFunctions) {
		this.nmsRoleFunctions = nmsRoleFunctions;
	}

}