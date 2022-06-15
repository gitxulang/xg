package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/10/18 - 21:15
 */
public class NmsAlarmSoftReport implements Serializable {
    private Integer id;
    private NmsAlarmSoft nmsAlarmSoft;
    private String RPeople;
    private String RContent;
    private String DTime;
    private Timestamp RTime;
    private Timestamp itime;

    public NmsAlarmSoftReport() {
    }

    public NmsAlarmSoftReport(Timestamp itime) {
        this.itime = itime;
    }

    public NmsAlarmSoftReport(Integer id, NmsAlarmSoft nmsAlarmSoft, String RPeople, String RContent, String DTime, Timestamp RTime, Timestamp itime) {
        this.id = id;
        this.nmsAlarmSoft = nmsAlarmSoft;
        this.RPeople = RPeople;
        this.RContent = RContent;
        this.DTime = DTime;
        this.RTime = RTime;
        this.itime = itime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NmsAlarmSoft getNmsAlarmSoft() {
        return nmsAlarmSoft;
    }

    public void setNmsAlarmSoft(NmsAlarmSoft nmsAlarmSoft) {
        this.nmsAlarmSoft = nmsAlarmSoft;
    }

    public String getRPeople() {
        return RPeople;
    }

    public void setRPeople(String RPeople) {
        this.RPeople = RPeople;
    }

    public String getRContent() {
        return RContent;
    }

    public void setRContent(String RContent) {
        this.RContent = RContent;
    }

    public String getDTime() {
        return DTime;
    }

    public void setDTime(String DTime) {
        this.DTime = DTime;
    }

    public Timestamp getRTime() {
        return RTime;
    }

    public void setRTime(Timestamp RTime) {
        this.RTime = RTime;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
