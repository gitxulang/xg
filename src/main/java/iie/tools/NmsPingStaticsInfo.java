package iie.tools;

import java.math.BigDecimal;
import java.util.List;

public class NmsPingStaticsInfo {

    private Integer assetId;

    private String assetName;

    private String assetIp;

    private Integer online;

    private Float currentPingRate;

    private Double averagePingRateOfToday;

    private Float maxPingRateOfToday;

    private Double averagePingRateOfTheLastThreeDays;

    private Float maxPingRateOfTheLastThreeDays;

    private Integer currentPingRtt;

    private BigDecimal averagePingRttOfToday;

    private Integer maxPingRttOfToday;

    private BigDecimal averagePingRttOfTheLastThreeDays;

    private Integer maxPingRttOfTheLastThreeDays;

    private List<NmsPingDetail> nmsPingInfoList;

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

    public Float getCurrentPingRate() {
        return currentPingRate;
    }

    public void setCurrentPingRate(Float currentPingRate) {
        this.currentPingRate = currentPingRate;
    }

    public Double getAveragePingRateOfToday() {
        return averagePingRateOfToday;
    }

    public void setAveragePingRateOfToday(Double averagePingRateOfToday) {
        this.averagePingRateOfToday = averagePingRateOfToday;
    }

    public Float getMaxPingRateOfToday() {
        return maxPingRateOfToday;
    }

    public void setMaxPingRateOfToday(Float maxPingRateOfToday) {
        this.maxPingRateOfToday = maxPingRateOfToday;
    }

    public Double getAveragePingRateOfTheLastThreeDays() {
        return averagePingRateOfTheLastThreeDays;
    }

    public void setAveragePingRateOfTheLastThreeDays(Double averagePingRateOfTheLastThreeDays) {
        this.averagePingRateOfTheLastThreeDays = averagePingRateOfTheLastThreeDays;
    }

    public Float getMaxPingRateOfTheLastThreeDays() {
        return maxPingRateOfTheLastThreeDays;
    }

    public void setMaxPingRateOfTheLastThreeDays(Float maxPingRateOfTheLastThreeDays) {
        this.maxPingRateOfTheLastThreeDays = maxPingRateOfTheLastThreeDays;
    }

    public Integer getCurrentPingRtt() {
        return currentPingRtt;
    }

    public void setCurrentPingRtt(Integer currentPingRtt) {
        this.currentPingRtt = currentPingRtt;
    }

    public BigDecimal getAveragePingRttOfToday() {
        return averagePingRttOfToday;
    }

    public void setAveragePingRttOfToday(BigDecimal averagePingRttOfToday) {
        this.averagePingRttOfToday = averagePingRttOfToday;
    }

    public Integer getMaxPingRttOfToday() {
        return maxPingRttOfToday;
    }

    public void setMaxPingRttOfToday(Integer maxPingRttOfToday) {
        this.maxPingRttOfToday = maxPingRttOfToday;
    }

    public BigDecimal getAveragePingRttOfTheLastThreeDays() {
        return averagePingRttOfTheLastThreeDays;
    }

    public void setAveragePingRttOfTheLastThreeDays(BigDecimal averagePingRttOfTheLastThreeDays) {
        this.averagePingRttOfTheLastThreeDays = averagePingRttOfTheLastThreeDays;
    }

    public Integer getMaxPingRttOfTheLastThreeDays() {
        return maxPingRttOfTheLastThreeDays;
    }

    public void setMaxPingRttOfTheLastThreeDays(Integer maxPingRttOfTheLastThreeDays) {
        this.maxPingRttOfTheLastThreeDays = maxPingRttOfTheLastThreeDays;
    }

    public List<NmsPingDetail> getNmsPingInfoList() {
        return nmsPingInfoList;
    }

    public void setNmsPingInfoList(List<NmsPingDetail> nmsPingInfoList) {
        this.nmsPingInfoList = nmsPingInfoList;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
