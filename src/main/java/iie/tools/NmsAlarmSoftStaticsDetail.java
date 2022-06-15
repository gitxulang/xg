package iie.tools;

/**
 * @author xczhao
 * @date 2020/10/18 - 9:58
 */
public class NmsAlarmSoftStaticsDetail {
    private Integer nmsSoftId;

    private String nmsSoftIp;

    private String nmsSoftPort;

    private String nmsSoftName;

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

    public Integer getNmsSoftId() {
        return nmsSoftId;
    }

    public void setNmsSoftId(Integer nmsSoftId) {
        this.nmsSoftId = nmsSoftId;
    }

    public String getNmsSoftIp() {
        return nmsSoftIp;
    }

    public void setNmsSoftIp(String nmsSoftIp) {
        this.nmsSoftIp = nmsSoftIp;
    }

    public String getNmsSoftPort() {
        return nmsSoftPort;
    }

    public void setNmsSoftPort(String nmsSoftPort) {
        this.nmsSoftPort = nmsSoftPort;
    }

    public String getNmsSoftName() {
        return nmsSoftName;
    }

    public void setNmsSoftName(String nmsSoftName) {
        this.nmsSoftName = nmsSoftName;
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
}
