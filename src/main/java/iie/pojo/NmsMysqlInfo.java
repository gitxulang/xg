package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsMysqlInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsMysqlInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private String dbVersion;
	private Integer maxConnections;
	private Integer threadsConnected;
	private Integer threadsRunning;
	private String dbReadOnly;
	private Integer qps;
	private Integer tps;
	private Integer abortedClients;
	private Integer questions;
	private Integer processlist;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsMysqlInfo() {
	}

	/** minimal constructor */
	public NmsMysqlInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsMysqlInfo(NmsAsset nmsAsset, String dbVersion, Integer maxConnections, Integer threadsConnected,
			Integer threadsRunning, String dbReadOnly, Integer qps, Integer tps, Integer abortedClients,
			Integer questions, Integer processlist, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.dbVersion = dbVersion;
		this.maxConnections = maxConnections;
		this.threadsConnected = threadsConnected;
		this.threadsRunning = threadsRunning;
		this.dbReadOnly = dbReadOnly;
		this.qps = qps;
		this.tps = tps;
		this.abortedClients = abortedClients;
		this.questions = questions;
		this.processlist = processlist;
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

	public String getDbVersion() {
		return this.dbVersion;
	}

	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}

	public Integer getMaxConnections() {
		return this.maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

	public Integer getThreadsConnected() {
		return this.threadsConnected;
	}

	public void setThreadsConnected(Integer threadsConnected) {
		this.threadsConnected = threadsConnected;
	}

	public Integer getThreadsRunning() {
		return this.threadsRunning;
	}

	public void setThreadsRunning(Integer threadsRunning) {
		this.threadsRunning = threadsRunning;
	}

	public String getDbReadOnly() {
		return this.dbReadOnly;
	}

	public void setDbReadOnly(String dbReadOnly) {
		this.dbReadOnly = dbReadOnly;
	}

	public Integer getQps() {
		return this.qps;
	}

	public void setQps(Integer qps) {
		this.qps = qps;
	}

	public Integer getTps() {
		return this.tps;
	}

	public void setTps(Integer tps) {
		this.tps = tps;
	}

	public Integer getAbortedClients() {
		return this.abortedClients;
	}

	public void setAbortedClients(Integer abortedClients) {
		this.abortedClients = abortedClients;
	}

	public Integer getQuestions() {
		return this.questions;
	}

	public void setQuestions(Integer questions) {
		this.questions = questions;
	}

	public Integer getProcesslist() {
		return this.processlist;
	}

	public void setProcesslist(Integer processlist) {
		this.processlist = processlist;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}