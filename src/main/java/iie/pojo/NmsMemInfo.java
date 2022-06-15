package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsMemInfo entity. @author MyEclipse Persistence Tools
 */

public class NmsMemInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private NmsAsset nmsAsset;
	private Long memTotal;
	private Long memFree;
	private Long memAvailable;
	private Long buffers;
	private Long cached;
	private Long swapTotal;
	private Long swapFree;
	private Long swapCached;
	private Timestamp itime;

	// Constructors

	/** default constructor */
	public NmsMemInfo() {
	}

	/** minimal constructor */
	public NmsMemInfo(Timestamp itime) {
		this.itime = itime;
	}

	/** full constructor */
	public NmsMemInfo(NmsAsset nmsAsset, Long memTotal, Long memFree,
			Long memAvailable, Long buffers, Long cached, Long swapTotal,
			Long swapFree, Long swapCached, Timestamp itime) {
		this.nmsAsset = nmsAsset;
		this.memTotal = memTotal;
		this.memFree = memFree;
		this.memAvailable = memAvailable;
		this.buffers = buffers;
		this.cached = cached;
		this.swapTotal = swapTotal;
		this.swapFree = swapFree;
		this.swapCached = swapCached;
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

	public Long getMemTotal() {
		return this.memTotal;
	}

	public void setMemTotal(Long memTotal) {
		this.memTotal = memTotal;
	}

	public Long getMemFree() {
		return this.memFree;
	}

	public void setMemFree(Long memFree) {
		this.memFree = memFree;
	}

	public Long getMemAvailable() {
		return this.memAvailable;
	}

	public void setMemAvailable(Long memAvailable) {
		this.memAvailable = memAvailable;
	}

	public Long getBuffers() {
		return this.buffers;
	}

	public void setBuffers(Long buffers) {
		this.buffers = buffers;
	}

	public Long getCached() {
		return this.cached;
	}

	public void setCached(Long cached) {
		this.cached = cached;
	}

	public Long getSwapTotal() {
		return this.swapTotal;
	}

	public void setSwapTotal(Long swapTotal) {
		this.swapTotal = swapTotal;
	}

	public Long getSwapFree() {
		return this.swapFree;
	}

	public void setSwapFree(Long swapFree) {
		this.swapFree = swapFree;
	}

	public Long getSwapCached() {
		return this.swapCached;
	}

	public void setSwapCached(Long swapCached) {
		this.swapCached = swapCached;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

}