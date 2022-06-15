package iie.tools;

import java.util.List;

public class NmsMiddlewareDetail {
    private Integer softId;
    private String softName;
    private String status;
    private Integer statusCode;

    private String name;
    private String version;
    private String jdkVersion;
    private Long installTime;

    private Long startTime;
    private Integer httpsProtocol;
    private Long maxConnNum;
    private Long maxHreadNum;

    private Long memTotal;
    private Long memUsed;
    private Long heapTotal;
    private Long heapUsed;
    private Long nonHeapTotal;
    private Long nonHeapUsed;
    private Long connNum;

    private Long numPerSecond;
    @SuppressWarnings("rawtypes")
	private List configInstances;
    private NmsPingStaticsInfo nmsPing;

    public Integer getSoftId() {
        return softId;
    }

    public void setSoftId(Integer softId) {
        this.softId = softId;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    public Long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Long installTime) {
        this.installTime = installTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getHttpsProtocol() {
        return httpsProtocol;
    }

    public void setHttpsProtocol(Integer httpsProtocol) {
        this.httpsProtocol = httpsProtocol;
    }

    public Long getMaxConnNum() {
        return maxConnNum;
    }

    public void setMaxConnNum(Long maxConnNum) {
        this.maxConnNum = maxConnNum;
    }

    public Long getMaxHreadNum() {
        return maxHreadNum;
    }

    public void setMaxHreadNum(Long maxHreadNum) {
        this.maxHreadNum = maxHreadNum;
    }

    public Long getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Long memTotal) {
        this.memTotal = memTotal;
    }

    public Long getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Long memUsed) {
        this.memUsed = memUsed;
    }

    public Long getHeapTotal() {
        return heapTotal;
    }

    public void setHeapTotal(Long heapTotal) {
        this.heapTotal = heapTotal;
    }

    public Long getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(Long heapUsed) {
        this.heapUsed = heapUsed;
    }

    public Long getNonHeapTotal() {
        return nonHeapTotal;
    }

    public void setNonHeapTotal(Long nonHeapTotal) {
        this.nonHeapTotal = nonHeapTotal;
    }

    public Long getNonHeapUsed() {
        return nonHeapUsed;
    }

    public void setNonHeapUsed(Long nonHeapUsed) {
        this.nonHeapUsed = nonHeapUsed;
    }

    @SuppressWarnings("rawtypes")
	public List getConfigInstances() {
        return configInstances;
    }

    @SuppressWarnings("rawtypes")
	public void setConfigInstances(List configInstances) {
        this.configInstances = configInstances;
    }

    public NmsPingStaticsInfo getNmsPing() {
        return nmsPing;
    }

    public void setNmsPing(NmsPingStaticsInfo nmsPing) {
        this.nmsPing = nmsPing;
    }

    public Long getConnNum() {
        return connNum;
    }

    public void setConnNum(Long connNum) {
        this.connNum = connNum;
    }

    public Long getNumPerSecond() {
        return numPerSecond;
    }

    public void setNumPerSecond(Long numPerSecond) {
        this.numPerSecond = numPerSecond;
    }
}
