package iie.tools;

public class NmsPerformanceRecord {

    private Integer id;

    private String AName;

    private String AIp;
    
    private Integer online;

    private String APort;

    private String AUser;

    private Byte colled;

    private String DName;
    
    private Integer typeId;

    private String chType;

    private String subChType;

    private Double cpuRate;

    private Float pingRate;

    private Long memFree;

    private Long memTotal;

    private Long swapFree;

    private Long swapTotal;

    private Integer netNum;
    
    private Integer ifAlarm;//此设备是否存在告警，是为1，否为0

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAName() {
        return AName;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public String getAIp() {
        return AIp;
    }

    public void setAIp(String AIp) {
        this.AIp = AIp;
    }

    public String getAPort() {
        return APort;
    }

    public void setAPort(String APort) {
        this.APort = APort;
    }

    public String getAUser() {
        return AUser;
    }

    public void setAUser(String AUser) {
        this.AUser = AUser;
    }

    public Byte getColled() {
        return colled;
    }

    public void setColled(Byte colled) {
        this.colled = colled;
    }

    public String getDName() {
        return DName;
    }

    public void setDName(String DName) {
        this.DName = DName;
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

    public Long getMemFree() {
        return memFree;
    }

    public void setMemFree(Long memFree) {
        this.memFree = memFree;
    }

    public Long getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Long memTotal) {
        this.memTotal = memTotal;
    }

    public Long getSwapFree() {
        return swapFree;
    }

    public void setSwapFree(Long swapFree) {
        this.swapFree = swapFree;
    }

    public Long getSwapTotal() {
        return swapTotal;
    }

    public void setSwapTotal(Long swapTotal) {
        this.swapTotal = swapTotal;
    }

    public Integer getNetNum() {
        return netNum;
    }

    public void setNetNum(Integer netNum) {
        this.netNum = netNum;
    }

	public Integer getIfAlarm() {
		return ifAlarm;
	}

	public void setIfAlarm(Integer ifAlarm) {
		this.ifAlarm = ifAlarm;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}
}
