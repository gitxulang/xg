package iie.domain;

/**
 * 单击显示设备信息的页面bean
 */
public class AssetMessageBean {
    private Integer id;
    private Integer assetId;
    private String assetIp;
    private String assetPos;
    private String stateAlarm;
    private String weiguiStatus;
    private String pingStatus;
    private String cpuStatus;
    private String menStatus;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssetPos() {
        return assetPos;
    }

    public void setAssetPos(String assetPos) {
        this.assetPos = assetPos;
    }

    public String getStateAlarm() {
        return stateAlarm;
    }

    public void setStateAlarm(String stateAlarm) {
        this.stateAlarm = stateAlarm;
    }

    public String getWeiguiStatus() {
        return weiguiStatus;
    }

    public void setWeiguiStatus(String weiguiStatus) {
        this.weiguiStatus = weiguiStatus;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    public String getCpuStatus() {
        return cpuStatus;
    }

    public String getAssetIp() {
        return assetIp;
    }

    public void setAssetIp(String assetIp) {
        this.assetIp = assetIp;
    }

    public void setCpuStatus(String cpuStatus) {
        this.cpuStatus = cpuStatus;
    }

    public String getMenStatus() {
        return menStatus;
    }

    public void setMenStatus(String menStatus) {
        this.menStatus = menStatus;
    }
}
