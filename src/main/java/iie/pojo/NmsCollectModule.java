package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsCollectModule entity. @author MyEclipse Persistence Tools
 */

public class NmsCollectModule implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer relId;
	private String collName;
	private Integer collCycle;
	private Integer collThread;
	private Integer collProcess;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsCollectModule() {
	}

	/** minimal constructor */
	public NmsCollectModule(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsCollectModule(Integer relId, String collName, Integer collCycle,
			Integer collThread, Integer collProcess, Timestamp itime) {
		this.relId = relId;
		this.collName = collName;
		this.collCycle = collCycle;
		this.collThread = collThread;
		this.collProcess = collProcess;
		this.itime = itime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRelId() {
		return this.relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}

	public String getCollName() {
		return this.collName;
	}

	public void setCollName(String collName) {
		this.collName = collName;
	}

	public Integer getCollCycle() {
		return this.collCycle;
	}

	public void setCollCycle(Integer collCycle) {
		this.collCycle = collCycle;
	}

	public Integer getCollThread() {
		return this.collThread;
	}

	public void setCollThread(Integer collThread) {
		this.collThread = collThread;
	}

	public Integer getCollProcess() {
		return this.collProcess;
	}

	public void setCollProcess(Integer collProcess) {
		this.collProcess = collProcess;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}