package iie.tools;

import java.util.List;

public class NmsCpuData {
    private Float currentCpuRate;

    private Double averageCpuRateOfToday;

    private Float maxCpuRateOfToday;

    private Double averageCpuRateOfTheLastThreeDays;

    private Float maxCpuRateOfTheLastThreeDays;

    private List<NmsCpuDetail> data;

    public Float getCurrentCpuRate() {
        return currentCpuRate;
    }

    public void setCurrentCpuRate(Float currentCpuRate) {
        this.currentCpuRate = currentCpuRate;
    }

    public Double getAverageCpuRateOfToday() {
        return averageCpuRateOfToday;
    }

    public void setAverageCpuRateOfToday(Double averageCpuRateOfToday) {
        this.averageCpuRateOfToday = averageCpuRateOfToday;
    }

    public Float getMaxCpuRateOfToday() {
        return maxCpuRateOfToday;
    }

    public void setMaxCpuRateOfToday(Float maxCpuRateOfToday) {
        this.maxCpuRateOfToday = maxCpuRateOfToday;
    }

    public Double getAverageCpuRateOfTheLastThreeDays() {
        return averageCpuRateOfTheLastThreeDays;
    }

    public void setAverageCpuRateOfTheLastThreeDays(Double averageCpuRateOfTheLastThreeDays) {
        this.averageCpuRateOfTheLastThreeDays = averageCpuRateOfTheLastThreeDays;
    }

    public Float getMaxCpuRateOfTheLastThreeDays() {
        return maxCpuRateOfTheLastThreeDays;
    }

    public void setMaxCpuRateOfTheLastThreeDays(Float maxCpuRateOfTheLastThreeDays) {
        this.maxCpuRateOfTheLastThreeDays = maxCpuRateOfTheLastThreeDays;
    }

    public List<NmsCpuDetail> getData() {
        return data;
    }

    public void setData(List<NmsCpuDetail> data) {
        this.data = data;
    }
}
