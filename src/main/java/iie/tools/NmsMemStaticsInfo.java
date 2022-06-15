package iie.tools;

public class NmsMemStaticsInfo {

    private Integer assetId;

    private String assetName;

    private String assetIp;

    private Integer online;

    private NmsMemData memData;

    private NmsSwapData nmsSwapData;

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

    public NmsMemData getMemData() {
        return memData;
    }

    public void setMemData(NmsMemData memData) {
        this.memData = memData;
    }

    public NmsSwapData getNmsSwapData() {
        return nmsSwapData;
    }

    public void setNmsSwapData(NmsSwapData nmsSwapData) {
        this.nmsSwapData = nmsSwapData;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
