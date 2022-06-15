package iie.tools;

public class NmsNetifItem {
	private String description;

	private Long ifSpeed;

	private String MAC;

	private String Ip;

	private Integer status;

	private double inletVelocity;

	private double inletSpeedRate;

	private double outVelocity;

	private double outSpeedRate;

	private Long inDiscards;

	private Long inErrors;

	private Long outDiscards;

	private Long outErrors;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getIfSpeed() {
		return ifSpeed;
	}

	public void setIfSpeed(Long ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public String getIp() {
		return Ip;
	}

	public void setIp(String ip) {
		Ip = ip;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public double getInletVelocity() {
		return inletVelocity;
	}

	public void setInletVelocity(double inletVelocity) {
		this.inletVelocity = inletVelocity;
	}

	public double getInletSpeedRate() {
		return inletSpeedRate;
	}

	public void setInletSpeedRate(double inletSpeedRate) {
		this.inletSpeedRate = inletSpeedRate;
	}

	public double getOutVelocity() {
		return outVelocity;
	}

	public void setOutVelocity(double outVelocity) {
		this.outVelocity = outVelocity;
	}

	public double getOutSpeedRate() {
		return outSpeedRate;
	}

	public void setOutSpeedRate(double outSpeedRate) {
		this.outSpeedRate = outSpeedRate;
	}

	public Long getInDiscards() {
		return inDiscards;
	}

	public void setInDiscards(Long inDiscards) {
		this.inDiscards = inDiscards;
	}

	public Long getInErrors() {
		return inErrors;
	}

	public void setInErrors(Long inErrors) {
		this.inErrors = inErrors;
	}

	public Long getOutDiscards() {
		return outDiscards;
	}

	public void setOutDiscards(Long outDiscards) {
		this.outDiscards = outDiscards;
	}

	public Long getOutErrors() {
		return outErrors;
	}

	public void setOutErrors(Long outErrors) {
		this.outErrors = outErrors;
	}
}