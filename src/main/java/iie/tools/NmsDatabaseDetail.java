package iie.tools;

import java.util.List;

public class NmsDatabaseDetail {
    private Integer softId;
    private String softName;
    private String status;
    private Integer statusCode;
    private String name;
    private String version;
    private Long installTime;
    private String processName;
    private Long startTime;
    private Long totalSize;
    private Long maxConnNum;
    private Long maxMemSize;
    private Long statusTotalSize;
    private Long memSize;
    private Long tps;
    private String userList;
    private Long connNum;
    private Long numPerSecond;
    private Long deadLockNum;

    @SuppressWarnings("rawtypes")
	private List sqls;
    @SuppressWarnings("rawtypes")
	private List storages;
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

    public Long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Long installTime) {
        this.installTime = installTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getMaxConnNum() {
        return maxConnNum;
    }

    public void setMaxConnNum(Long maxConnNum) {
        this.maxConnNum = maxConnNum;
    }

    public Long getMaxMemSize() {
        return maxMemSize;
    }

    public void setMaxMemSize(Long maxMemSize) {
        this.maxMemSize = maxMemSize;
    }

    public Long getStatusTotalSize() {
        return statusTotalSize;
    }

    public void setStatusTotalSize(Long statusTotalSize) {
        this.statusTotalSize = statusTotalSize;
    }

    public Long getMemSize() {
        return memSize;
    }

    public void setMemSize(Long memSize) {
        this.memSize = memSize;
    }

    public Long getTps() {
        return tps;
    }

    public void setTps(Long tps) {
        this.tps = tps;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
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

    @SuppressWarnings("rawtypes")
	public List getSqls() {
        return sqls;
    }

    @SuppressWarnings("rawtypes")
	public void setSqls(List sqls) {
        this.sqls = sqls;
    }

    @SuppressWarnings("rawtypes")
	public List getStorages() {
        return storages;
    }

    @SuppressWarnings("rawtypes")
	public void setStorages(List storages) {
        this.storages = storages;
    }

    public Long getDeadLockNum() {
        return deadLockNum;
    }

    public void setDeadLockNum(Long deadLockNum) {
        this.deadLockNum = deadLockNum;
    }
}
