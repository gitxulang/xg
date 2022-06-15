package iie.tools;

import java.util.List;

public class NmsFileSysStaticsInfo {

    private Integer assetId;

    private String assetName;

    private String assetIp;

    private Integer online;

    private Integer partNum;

    private List<NmsFilePartsData> partsDates;

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

    public Integer getPartNum() {
        return partNum;
    }

    public void setPartNum(Integer partNum) {
        this.partNum = partNum;
    }

    public List<NmsFilePartsData> getPartsDates() {
        return partsDates;
    }

    public void setPartsDates(List<NmsFilePartsData> partsDates) {
        this.partsDates = partsDates;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
