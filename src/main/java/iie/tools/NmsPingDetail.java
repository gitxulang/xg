package iie.tools;

public class NmsPingDetail {

    private String itime;

    private Float pingRate;
    private Integer pingRtt;

    public String getItime() {
        return itime;
    }

    public void setItime(String itime) {
        this.itime = itime;
    }

    public Float getPingRate() {
        return pingRate;
    }

    public void setPingRate(Float pingRate) {
        this.pingRate = pingRate;
    }

    public Integer getPingRtt() {
        return pingRtt;
    }

    public void setPingRtt(Integer pingRtt) {
        this.pingRtt = pingRtt;
    }
}
