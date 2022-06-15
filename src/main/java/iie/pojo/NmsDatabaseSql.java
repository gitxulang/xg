package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/23 - 17:30
 */
public class NmsDatabaseSql implements Serializable {
    private Long id;
    private NmsDatabaseStatus nmsDatabaseStatus;
    private String slowSql;
    private Long execTime;
    private Timestamp itime;

    public NmsDatabaseSql() {
    }

    public NmsDatabaseSql(Timestamp itime) {
        this.itime = itime;
    }

    public NmsDatabaseSql(Long id, NmsDatabaseStatus nmsDatabaseStatus, String slowSql, Long execTime, Timestamp itime) {
        this.id = id;
        this.nmsDatabaseStatus = nmsDatabaseStatus;
        this.slowSql = slowSql;
        this.execTime = execTime;
        this.itime = itime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NmsDatabaseStatus getNmsDatabaseStatus() {
        return nmsDatabaseStatus;
    }

    public void setNmsDatabaseStatus(NmsDatabaseStatus nmsDatabaseStatus) {
        this.nmsDatabaseStatus = nmsDatabaseStatus;
    }

    public String getSlowSql() {
        return slowSql;
    }

    public void setSlowSql(String slowSql) {
        this.slowSql = slowSql;
    }

    public Long getExecTime() {
        return execTime;
    }

    public void setExecTime(Long execTime) {
        this.execTime = execTime;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
