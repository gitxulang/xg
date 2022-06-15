package iie.tools;

public class NmsPerformanceItem {

    private Integer id;

    private String AName;

    private String AIp;

	private Integer online;

    private String DName;

    private String chType;

    private String subChType;

    private Double cpuRate;

    private Float pingRate;

    private Double memRate;

    private Long memTotal;

    private Double swapRate;

    private Long swapTotal;

    private double netInSpeed;

    private double netOutSpeed;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAName() {
		return AName;
	}

	public void setAName(String aName) {
		AName = aName;
	}

	public String getAIp() {
		return AIp;
	}

	public void setAIp(String aIp) {
		AIp = aIp;
	}

	public String getDName() {
		return DName;
	}

	public void setDName(String dName) {
		DName = dName;
	}

	public String getChType() {
		return chType;
	}

	public void setChType(String chType) {
		this.chType = chType;
	}

	public String getSubChType() {
		return subChType;
	}

	public void setSubChType(String subChType) {
		this.subChType = subChType;
	}

	public Double getCpuRate() {
		return cpuRate;
	}

	public void setCpuRate(Double cpuRate) {
		this.cpuRate = cpuRate;
	}

	public Float getPingRate() {
		return pingRate;
	}

	public void setPingRate(Float pingRate) {
		this.pingRate = pingRate;
	}

	public Double getMemRate() {
		return memRate;
	}

	public void setMemRate(Double memRate) {
		this.memRate = memRate;
	}

	public Long getMemTotal() {
		return memTotal;
	}

	public void setMemTotal(Long memTotal) {
		this.memTotal = memTotal;
	}

	public Double getSwapRate() {
		return swapRate;
	}

	public void setSwapRate(Double swapRate) {
		this.swapRate = swapRate;
	}

	public Long getSwapTotal() {
		return swapTotal;
	}

	public void setSwapTotal(Long swapTotal) {
		this.swapTotal = swapTotal;
	}

	public double getNetInSpeed() {
		return netInSpeed;
	}

	public void setNetInSpeed(double netInSpeed) {
		this.netInSpeed = netInSpeed;
	}

	public double getNetOutSpeed() {
		return netOutSpeed;
	}

	public void setNetOutSpeed(double netOutSpeed) {
		this.netOutSpeed = netOutSpeed;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}
}
