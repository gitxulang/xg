package iie.tools;

public class NmsAlarmStaticsDetail {

    private Integer nmsAssetId;

    private String nmsAssetIp;

    private String nmsAssetName;
    
    private String nmsAssetType;
    
    private String nmsAssetSubType;

    private Long alarmTotalCount;

    private Long unDealTotalCount;

    private Long dealedTotalCount;

    private Long dealingTotalCount;

    private Long deleteTotalCount;

    private Long autoRecoverTotalCount;

    private NmsAlarmLevelStaticsDetail levelOne;

    private NmsAlarmLevelStaticsDetail levelTwo;

    private NmsAlarmLevelStaticsDetail levelThree;

    public Integer getNmsAssetId() {
        return nmsAssetId;
    }

    public void setNmsAssetId(Integer nmsAssetId) {
        this.nmsAssetId = nmsAssetId;
    }

    public String getNmsAssetIp() {
        return nmsAssetIp;
    }

    public void setNmsAssetIp(String nmsAssetIp) {
        this.nmsAssetIp = nmsAssetIp;
    }

    public String getNmsAssetName() {
        return nmsAssetName;
    }

    public void setNmsAssetName(String nmsAssetName) {
        this.nmsAssetName = nmsAssetName;
    }

    public Long getAlarmTotalCount() {
        return alarmTotalCount;
    }

    public void setAlarmTotalCount(Long alarmTotalCount) {
        this.alarmTotalCount = alarmTotalCount;
    }

    public Long getUnDealTotalCount() {
        return unDealTotalCount;
    }

    public void setUnDealTotalCount(Long unDealTotalCount) {
        this.unDealTotalCount = unDealTotalCount;
    }

    public Long getDealedTotalCount() {
        return dealedTotalCount;
    }

    public void setDealedTotalCount(Long dealedTotalCount) {
        this.dealedTotalCount = dealedTotalCount;
    }

    public Long getDealingTotalCount() {
        return dealingTotalCount;
    }

    public void setDealingTotalCount(Long dealingTotalCount) {
        this.dealingTotalCount = dealingTotalCount;
    }

    public Long getDeleteTotalCount() {
        return deleteTotalCount;
    }

    public void setDeleteTotalCount(Long deleteTotalCount) {
        this.deleteTotalCount = deleteTotalCount;
    }

    public Long getAutoRecoverTotalCount() {
        return autoRecoverTotalCount;
    }

    public void setAutoRecoverTotalCount(Long autoRecoverTotalCount) {
        this.autoRecoverTotalCount = autoRecoverTotalCount;
    }

    public NmsAlarmLevelStaticsDetail getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(NmsAlarmLevelStaticsDetail levelOne) {
        this.levelOne = levelOne;
    }

    public NmsAlarmLevelStaticsDetail getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(NmsAlarmLevelStaticsDetail levelTwo) {
        this.levelTwo = levelTwo;
    }

    public NmsAlarmLevelStaticsDetail getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(NmsAlarmLevelStaticsDetail levelThree) {
        this.levelThree = levelThree;
    }

	public String getNmsAssetType() {
		return nmsAssetType;
	}

	public void setNmsAssetType(String nmsAssetType) {
		this.nmsAssetType = nmsAssetType;
	}

	public String getNmsAssetSubType() {
		return nmsAssetSubType;
	}

	public void setNmsAssetSubType(String nmsAssetSubType) {
		this.nmsAssetSubType = nmsAssetSubType;
	}
}
