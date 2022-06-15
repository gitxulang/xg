package iie.tools;

import java.util.List;

public class NmsSwapData {

    private Long currentSwapTotal;

    private Double currentSwapRate;

    private Double averageSwapRateOfToday;

    private Double maxSwapRateOfToday;

    private Double averageSwapRateOfTheLastThreeDays;

    private Double maxSwapRateOfTheLastThreeDays;

    private List<NmsSwapDataDetail> data;

    public Long getCurrentSwapTotal() {
        return currentSwapTotal;
    }

    public void setCurrentSwapTotal(Long currentSwapTotal) {
        this.currentSwapTotal = currentSwapTotal;
    }

    public Double getCurrentSwapRate() {
        return currentSwapRate;
    }

    public void setCurrentSwapRate(Double currentSwapRate) {
        this.currentSwapRate = currentSwapRate;
    }

    public Double getAverageSwapRateOfToday() {
        return averageSwapRateOfToday;
    }

    public void setAverageSwapRateOfToday(Double averageSwapRateOfToday) {
        this.averageSwapRateOfToday = averageSwapRateOfToday;
    }

    public Double getMaxSwapRateOfToday() {
        return maxSwapRateOfToday;
    }

    public void setMaxSwapRateOfToday(Double maxSwapRateOfToday) {
        this.maxSwapRateOfToday = maxSwapRateOfToday;
    }

    public Double getAverageSwapRateOfTheLastThreeDays() {
        return averageSwapRateOfTheLastThreeDays;
    }

    public void setAverageSwapRateOfTheLastThreeDays(Double averageSwapRateOfTheLastThreeDays) {
        this.averageSwapRateOfTheLastThreeDays = averageSwapRateOfTheLastThreeDays;
    }

    public Double getMaxSwapRateOfTheLastThreeDays() {
        return maxSwapRateOfTheLastThreeDays;
    }

    public void setMaxSwapRateOfTheLastThreeDays(Double maxSwapRateOfTheLastThreeDays) {
        this.maxSwapRateOfTheLastThreeDays = maxSwapRateOfTheLastThreeDays;
    }

    public List<NmsSwapDataDetail> getData() {
        return data;
    }

    public void setData(List<NmsSwapDataDetail> data) {
        this.data = data;
    }
}
