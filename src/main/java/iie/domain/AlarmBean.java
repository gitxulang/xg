package iie.domain;

public class AlarmBean {
    private Integer id;
    private Integer assetId;
    private String AName;
    private Integer ALevel;
    private Integer DStatus;
    private String ATime;
    private String ip;

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

    public String getAName() {
        return AName;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public Integer getALevel() {
        return ALevel;
    }

    public void setALevel(Integer ALevel) {
        this.ALevel = ALevel;
    }

    public Integer getDStatus() {
        return DStatus;
    }

    public void setDStatus(Integer DStatus) {
        this.DStatus = DStatus;
    }

    public String getATime() {
        return ATime;
    }

    public void setATime(String ATime) {
        this.ATime = ATime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
