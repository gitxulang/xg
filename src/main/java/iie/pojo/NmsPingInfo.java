package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsPingInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsPingInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Float pingRate;
	private Integer pingRtt;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsPingInfo() {
	}

	/** minimal constructor */
	public NmsPingInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsPingInfo(NmsAsset nmsAsset, Float pingRate, Integer pingRtt,
			Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.pingRate = pingRate;
		this.pingRtt = pingRtt;
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

	public Float getPingRate() {
		return this.pingRate;
	}

	public void setPingRate(Float pingRate) {
		this.pingRate = pingRate;
	}

	public Integer getPingRtt() {
		return this.pingRtt;
	}

	public void setPingRtt(Integer pingRtt) {
		this.pingRtt = pingRtt;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}