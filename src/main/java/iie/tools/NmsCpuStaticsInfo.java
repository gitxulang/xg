package iie.tools;

public class NmsCpuStaticsInfo {

    private Integer assetId;

    private String assetName;

    private String assetIp;

    private Integer online;

    private Integer cpuNum;

    private NmsCpuData cpuData;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetIp() {
        return assetIp;
    }

    public void setAssetIp(String assetIp) {
        this.assetIp = assetIp;
    }

    public Integer getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(Integer cpuNum) {
        this.cpuNum = cpuNum;
    }

    public NmsCpuData getCpuData() {
        return cpuData;
    }

    public void setCpuData(NmsCpuData cpuData) {
        this.cpuData = cpuData;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
