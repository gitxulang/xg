package iie.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * NmsAsset entity. @author MyEclipse Persistence Tools
 */

public class NmsAsset implements java.io.Serializable {

	// Fields

	private Integer id;
	private NmsAssetType nmsAssetType;
	private String AIp;
	private String BmIp;
	private String YwIp;
	private String AName;
	private String ANo;
	private Integer online;
	private String APos;
	private String AManu;
	private String ADate;
	private String AUser;
	private NmsDepartment nmsDepartment;
	private Short colled;
	private Integer collMode;
	private String authPass;
	private String RComm;
	private String WComm;
	private String username;
	private String password;
	private String sshport;
	
	private Integer deled;
	private Timestamp itime;
	private Set nmsMemInfos = new HashSet(0);
	private Set nmsBmcInfos = new HashSet(0);
	private Set nmsFilesysInfos = new HashSet(0);
	private Set nmsStaticInfos = new HashSet(0);
	private Set nmsRuleAssets = new HashSet(0);
	private Set nmsNetifInfos = new HashSet(0);
	private Set nmsAlarms = new HashSet(0);
	private Set nmsDynamicInfos = new HashSet(0);
	private Set nmsCpuInfos = new HashSet(0);
	private Set nmsDiskioInfos = new HashSet(0);
	private Set nmsProcessInfos = new HashSet(0);
	private Set nmsRuleAssetIndexes = new HashSet(0);
	private Set nmsPingInfos = new HashSet(0);
	private Set nmsMysqlInfos = new HashSet(0);
	private Set nmsTomcatInfos = new HashSet(0);
	private Set nmsTriManagements = new HashSet(0);
	// Constructors

	public Set getNmsTriManagements() {
		return nmsTriManagements;
	}

	public void setNmsTriManagements(Set nmsTriManagements) {
		this.nmsTriManagements = nmsTriManagements;
	}

	public Set getNmsMysqlInfos() {
		return nmsMysqlInfos;
	}

	public void setNmsMysqlInfos(Set nmsMysqlInfos) {
		this.nmsMysqlInfos = nmsMysqlInfos;
	}

	public Set getNmsTomcatInfos() {
		return nmsTomcatInfos;
	}

	public void setNmsTomcatInfos(Set nmsTomcatInfos) {
		this.nmsTomcatInfos = nmsTomcatInfos;
	}

	/** default constructor */
	public NmsAsset() {
	}

	/** full constructor */
	public NmsAsset(NmsAssetType nmsAssetType, String AIp, String BmIp, String YwIp, String AName,
			String ANo, String APos, String AManu, String ADate, String AUser,
			NmsDepartment nmsDepartment, Short colled, Integer collMode, String authPass,
			String RComm, String WComm, Integer deled, Timestamp itime,
			Set nmsMemInfos, Set nmsBmcInfos, Set nmsFilesysInfos,
			Set nmsStaticInfos, Set nmsRuleAssets, Set nmsNetifInfos,
			Set nmsAlarms, Set nmsDynamicInfos, Set nmsCpuInfos,
			Set nmsDiskioInfos, Set nmsProcessInfos, Set nmsRuleAssetIndexes, Set nmsPingInfos, Set nmsTriManagements) {
		this.nmsAssetType = nmsAssetType;
		this.AIp = AIp;
		this.BmIp = BmIp;
		this.YwIp = YwIp;
		this.AName = AName;
		this.ANo = ANo;
		this.APos = APos;
		this.AManu = AManu;
		this.ADate = ADate;
		this.AUser = AUser;
		this.nmsDepartment = nmsDepartment;
		this.colled = colled;
		this.collMode = collMode;
		this.authPass = authPass;
		this.RComm = RComm;
		this.WComm = WComm;
		this.deled = deled;
		this.itime = itime;
		this.nmsMemInfos = nmsMemInfos;
		this.nmsBmcInfos = nmsBmcInfos;
		this.nmsFilesysInfos = nmsFilesysInfos;
		this.nmsStaticInfos = nmsStaticInfos;
		this.nmsRuleAssets = nmsRuleAssets;
		this.nmsNetifInfos = nmsNetifInfos;
		this.nmsAlarms = nmsAlarms;
		this.nmsDynamicInfos = nmsDynamicInfos;
		this.nmsCpuInfos = nmsCpuInfos;
		this.nmsDiskioInfos = nmsDiskioInfos;
		this.nmsProcessInfos = nmsProcessInfos;
		this.nmsRuleAssetIndexes = nmsRuleAssetIndexes;
		this.nmsPingInfos = nmsPingInfos;
		this.nmsTriManagements = nmsTriManagements;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NmsAssetType getNmsAssetType() {
		return this.nmsAssetType;
	}

	public void setNmsAssetType(NmsAssetType nmsAssetType) {
		this.nmsAssetType = nmsAssetType;
	}

	public String getAIp() {
		return this.AIp;
	}

	public void setAIp(String AIp) {
		this.AIp = AIp;
	}

	public String getAName() {
		return this.AName;
	}

	public void setAName(String AName) {
		this.AName = AName;
	}

	public String getANo() {
		return this.ANo;
	}

	public void setANo(String ANo) {
		this.ANo = ANo;
	}

	public String getAPos() {
		return this.APos;
	}

	public void setAPos(String APos) {
		this.APos = APos;
	}

	public String getAManu() {
		return this.AManu;
	}

	public void setAManu(String AManu) {
		this.AManu = AManu;
	}

	public String getADate() {
		return this.ADate;
	}

	public void setADate(String ADate) {
		this.ADate = ADate;
	}

	public String getAUser() {
		return this.AUser;
	}

	public void setAUser(String AUser) {
		this.AUser = AUser;
	}

	public NmsDepartment getNmsDepartment() {
		return nmsDepartment;
	}

	public void setNmsDepartment(NmsDepartment nmsDepartment) {
		this.nmsDepartment = nmsDepartment;
	}

	public Short getColled() {
		return this.colled;
	}

	public void setColled(Short colled) {
		this.colled = colled;
	}

	public Integer getCollMode() {
		return this.collMode;
	}

	public void setCollMode(Integer collMode) {
		this.collMode = collMode;
	}

	public String getAuthPass() {
		return this.authPass;
	}

	public void setAuthPass(String authPass) {
		this.authPass = authPass;
	}

	public String getRComm() {
		return this.RComm;
	}

	public void setRComm(String RComm) {
		this.RComm = RComm;
	}

	public String getWComm() {
		return this.WComm;
	}

	public void setWComm(String WComm) {
		this.WComm = WComm;
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

	public Set getNmsMemInfos() {
		return this.nmsMemInfos;
	}

	public void setNmsMemInfos(Set nmsMemInfos) {
		this.nmsMemInfos = nmsMemInfos;
	}

	public Set getNmsBmcInfos() {
		return this.nmsBmcInfos;
	}

	public void setNmsBmcInfos(Set nmsBmcInfos) {
		this.nmsBmcInfos = nmsBmcInfos;
	}

	public Set getNmsFilesysInfos() {
		return this.nmsFilesysInfos;
	}

	public void setNmsFilesysInfos(Set nmsFilesysInfos) {
		this.nmsFilesysInfos = nmsFilesysInfos;
	}

	public Set getNmsStaticInfos() {
		return this.nmsStaticInfos;
	}

	public void setNmsStaticInfos(Set nmsStaticInfos) {
		this.nmsStaticInfos = nmsStaticInfos;
	}

	public Set getNmsRuleAssets() {
		return this.nmsRuleAssets;
	}

	public void setNmsRuleAssets(Set nmsRuleAssets) {
		this.nmsRuleAssets = nmsRuleAssets;
	}

	public Set getNmsNetifInfos() {
		return this.nmsNetifInfos;
	}

	public void setNmsNetifInfos(Set nmsNetifInfos) {
		this.nmsNetifInfos = nmsNetifInfos;
	}

	public Set getNmsAlarms() {
		return this.nmsAlarms;
	}

	public void setNmsAlarms(Set nmsAlarms) {
		this.nmsAlarms = nmsAlarms;
	}

	public Set getNmsDynamicInfos() {
		return this.nmsDynamicInfos;
	}

	public void setNmsDynamicInfos(Set nmsDynamicInfos) {
		this.nmsDynamicInfos = nmsDynamicInfos;
	}

	public Set getNmsCpuInfos() {
		return this.nmsCpuInfos;
	}

	public void setNmsCpuInfos(Set nmsCpuInfos) {
		this.nmsCpuInfos = nmsCpuInfos;
	}

	public Set getNmsDiskioInfos() {
		return this.nmsDiskioInfos;
	}

	public void setNmsDiskioInfos(Set nmsDiskioInfos) {
		this.nmsDiskioInfos = nmsDiskioInfos;
	}

	public Set getNmsProcessInfos() {
		return this.nmsProcessInfos;
	}

	public void setNmsProcessInfos(Set nmsProcessInfos) {
		this.nmsProcessInfos = nmsProcessInfos;
	}

	public Set getNmsRuleAssetIndexes() {
		return this.nmsRuleAssetIndexes;
	}

	public void setNmsRuleAssetIndexes(Set nmsRuleAssetIndexes) {
		this.nmsRuleAssetIndexes = nmsRuleAssetIndexes;
	}

	public Set getNmsPingInfos() {
		return nmsPingInfos;
	}

	public void setNmsPingInfos(Set nmsPingInfos) {
		this.nmsPingInfos = nmsPingInfos;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSshport() {
		return sshport;
	}

	public void setSshport(String sshport) {
		this.sshport = sshport;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public String getBmIp() {
		return BmIp;
	}

	public void setBmIp(String bmIp) {
		BmIp = bmIp;
	}

	public String getYwIp() {
		return YwIp;
	}

	public void setYwIp(String ywIp) {
		YwIp = ywIp;
	}

}