package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsNetifInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsNetifInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Integer freq;
	private Integer ifIndex;
	private String ifDescr;
	private Integer ifType;
	private Integer ifMtu;
	private Long ifSpeed;
	private String ifIp;
	private String ifSubmask;
	private String ifGateway;
	private String ifPhysaddr;
	private Integer ifAdminStatus;
	private Integer ifOperStatus;
	private Long ifInOctets;
	private Long ifInUcastpkts;
	private Long ifInNucastpkts;
	private Long ifInDiscards;
	private Long ifInErrors;
	private Long ifOutOctets;
	private Long ifOutUcastpkts;
	private Long ifOutNucastpkts;
	private Long ifOutDiscards;
	private Long ifOutErrors;
	private Long ifInIcmps;
	private Long ifOutIcmps;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsNetifInfo() {
	}

	/** minimal constructor */
	public NmsNetifInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsNetifInfo(NmsAsset nmsAsset, Integer freq, Integer ifIndex,
			String ifDescr, Integer ifType, Integer ifMtu, Long ifSpeed,
			String ifIp, String ifSubmask, String ifGateway, String ifPhysaddr,
			Integer ifAdminStatus, Integer ifOperStatus, Long ifInOctets,
			Long ifInUcastpkts, Long ifInNucastpkts, Long ifInDiscards,
			Long ifInErrors, Long ifOutOctets, Long ifOutUcastpkts,
			Long ifOutNucastpkts, Long ifOutDiscards, Long ifOutErrors,
			Long ifInIcmps, Long ifOutIcmps, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.freq = freq;
		this.ifIndex = ifIndex;
		this.ifDescr = ifDescr;
		this.ifType = ifType;
		this.ifMtu = ifMtu;
		this.ifSpeed = ifSpeed;
		this.ifIp = ifIp;
		this.ifSubmask = ifSubmask;
		this.ifGateway = ifGateway;
		this.ifPhysaddr = ifPhysaddr;
		this.ifAdminStatus = ifAdminStatus;
		this.ifOperStatus = ifOperStatus;
		this.ifInOctets = ifInOctets;
		this.ifInUcastpkts = ifInUcastpkts;
		this.ifInNucastpkts = ifInNucastpkts;
		this.ifInDiscards = ifInDiscards;
		this.ifInErrors = ifInErrors;
		this.ifOutOctets = ifOutOctets;
		this.ifOutUcastpkts = ifOutUcastpkts;
		this.ifOutNucastpkts = ifOutNucastpkts;
		this.ifOutDiscards = ifOutDiscards;
		this.ifOutErrors = ifOutErrors;
		this.ifInIcmps = ifInIcmps;
		this.ifOutIcmps = ifOutIcmps;
		this.itime = itime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NmsAsset getNmsAsset() {
		return this.nmsAsset;
	}

	public void setNmsAsset(NmsAsset nmsAsset) {
		this.nmsAsset = nmsAsset;
	}

	public Integer getFreq() {
		return this.freq;
	}

	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	public Integer getIfIndex() {
		return this.ifIndex;
	}

	public void setIfIndex(Integer ifIndex) {
		this.ifIndex = ifIndex;
	}

	public String getIfDescr() {
		return this.ifDescr;
	}

	public void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	public Integer getIfType() {
		return this.ifType;
	}

	public void setIfType(Integer ifType) {
		this.ifType = ifType;
	}

	public Integer getIfMtu() {
		return this.ifMtu;
	}

	public void setIfMtu(Integer ifMtu) {
		this.ifMtu = ifMtu;
	}

	public Long getIfSpeed() {
		return this.ifSpeed;
	}

	public void setIfSpeed(Long ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	public String getIfIp() {
		return this.ifIp;
	}

	public void setIfIp(String ifIp) {
		this.ifIp = ifIp;
	}

	public String getIfSubmask() {
		return this.ifSubmask;
	}

	public void setIfSubmask(String ifSubmask) {
		this.ifSubmask = ifSubmask;
	}

	public String getIfGateway() {
		return this.ifGateway;
	}

	public void setIfGateway(String ifGateway) {
		this.ifGateway = ifGateway;
	}

	public String getIfPhysaddr() {
		return this.ifPhysaddr;
	}

	public void setIfPhysaddr(String ifPhysaddr) {
		this.ifPhysaddr = ifPhysaddr;
	}

	public Integer getIfAdminStatus() {
		return this.ifAdminStatus;
	}

	public void setIfAdminStatus(Integer ifAdminStatus) {
		this.ifAdminStatus = ifAdminStatus;
	}

	public Integer getIfOperStatus() {
		return this.ifOperStatus;
	}

	public void setIfOperStatus(Integer ifOperStatus) {
		this.ifOperStatus = ifOperStatus;
	}

	public Long getIfInOctets() {
		return this.ifInOctets;
	}

	public void setIfInOctets(Long ifInOctets) {
		this.ifInOctets = ifInOctets;
	}

	public Long getIfInUcastpkts() {
		return this.ifInUcastpkts;
	}

	public void setIfInUcastpkts(Long ifInUcastpkts) {
		this.ifInUcastpkts = ifInUcastpkts;
	}

	public Long getIfInNucastpkts() {
		return this.ifInNucastpkts;
	}

	public void setIfInNucastpkts(Long ifInNucastpkts) {
		this.ifInNucastpkts = ifInNucastpkts;
	}

	public Long getIfInDiscards() {
		return this.ifInDiscards;
	}

	public void setIfInDiscards(Long ifInDiscards) {
		this.ifInDiscards = ifInDiscards;
	}

	public Long getIfInErrors() {
		return this.ifInErrors;
	}

	public void setIfInErrors(Long ifInErrors) {
		this.ifInErrors = ifInErrors;
	}

	public Long getIfOutOctets() {
		return this.ifOutOctets;
	}

	public void setIfOutOctets(Long ifOutOctets) {
		this.ifOutOctets = ifOutOctets;
	}

	public Long getIfOutUcastpkts() {
		return this.ifOutUcastpkts;
	}

	public void setIfOutUcastpkts(Long ifOutUcastpkts) {
		this.ifOutUcastpkts = ifOutUcastpkts;
	}

	public Long getIfOutNucastpkts() {
		return this.ifOutNucastpkts;
	}

	public void setIfOutNucastpkts(Long ifOutNucastpkts) {
		this.ifOutNucastpkts = ifOutNucastpkts;
	}

	public Long getIfOutDiscards() {
		return this.ifOutDiscards;
	}

	public void setIfOutDiscards(Long ifOutDiscards) {
		this.ifOutDiscards = ifOutDiscards;
	}

	public Long getIfOutErrors() {
		return this.ifOutErrors;
	}

	public void setIfOutErrors(Long ifOutErrors) {
		this.ifOutErrors = ifOutErrors;
	}

	public Long getIfInIcmps() {
		return this.ifInIcmps;
	}

	public void setIfInIcmps(Long ifInIcmps) {
		this.ifInIcmps = ifInIcmps;
	}

	public Long getIfOutIcmps() {
		return this.ifOutIcmps;
	}

	public void setIfOutIcmps(Long ifOutIcmps) {
		this.ifOutIcmps = ifOutIcmps;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}