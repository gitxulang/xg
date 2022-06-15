package iie.tools;

import java.util.List;

public class NmsMemData {

    private Long currentMemTotal;

    private Double currentMemRate;

    private Double averageMemRateOfToday;

    private Double maxMemRateOfToday;

    private Double averageMemRateOfTheLastThreeDays;

    private Double maxMemRateOfTheLastThreeDays;

    private List<NmsMemDataDetail> data;

    public Long getCurrentMemTotal() {
        return currentMemTotal;
    }

    public void setCurrentMemTotal(Long currentMemTotal) {
        this.currentMemTotal = currentMemTotal;
    }

    public Double getCurrentMemRate() {
        return currentMemRate;
    }

    public void setCurrentMemRate(Double currentMemRate) {
        this.currentMemRate = currentMemRate;
    }

    public Double getAverageMemRateOfToday() {
        return averageMemRateOfToday;
    }

    public void setAverageMemRateOfToday(Double averageMemRateOfToday) {
        this.averageMemRateOfToday = averageMemRateOfToday;
    }

    public Double getMaxMemRateOfToday() {
        return maxMemRateOfToday;
    }

    public void setMaxMemRateOfToday(Double maxMemRateOfToday) {
        this.maxMemRateOfToday = maxMemRateOfToday;
    }

    public Double getAverageMemRateOfTheLastThreeDays() {
        return averageMemRateOfTheLastThreeDays;
    }

    public void setAverageMemRateOfTheLastThreeDays(Double averageMemRateOfTheLastThreeDays) {
        this.averageMemRateOfTheLastThreeDays = averageMemRateOfTheLastThreeDays;
    }

    public Double getMaxMemRateOfTheLastThreeDays() {
        return maxMemRateOfTheLastThreeDays;
    }

    public void setMaxMemRateOfTheLastThreeDays(Double maxMemRateOfTheLastThreeDays) {
        this.maxMemRateOfTheLastThreeDays = maxMemRateOfTheLastThreeDays;
    }

    public List<NmsMemDataDetail> getData() {
        return data;
    }

    public void setData(List<NmsMemDataDetail> data) {
        this.data = data;
    }
}
