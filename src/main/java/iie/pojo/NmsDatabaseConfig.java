package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/12 - 19:54
 */
public class NmsDatabaseConfig implements Serializable {
    private Long id;
    private NmsSoft nmsSoft;
    private Long startTime;
    private Long totalSize;
    private String instanceNames;
    private Long maxConnNum;
    private Long maxMemSize;
    private Timestamp itime;

    public NmsDatabaseConfig() {
    }

    public NmsDatabaseConfig(Timestamp itime) {
        this.itime = itime;
    }

    public NmsDatabaseConfig(Long id, NmsSoft nmsSoft, Long startTime, Long totalSize, String instanceNames, Long maxConnNum, Long maxMemSize, Timestamp itime) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.startTime = startTime;
        this.totalSize = totalSize;
        this.instanceNames = instanceNames;
        this.maxConnNum = maxConnNum;
        this.maxMemSize = maxMemSize;
        this.itime = itime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NmsSoft getNmsSoft() {
        return nmsSoft;
    }

    public void setNmsSoft(NmsSoft nmsSoft) {
        this.nmsSoft = nmsSoft;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getInstanceNames() {
        return instanceNames;
    }

    public void setInstanceNames(String instanceNames) {
        this.instanceNames = instanceNames;
    }

    public Long getMaxConnNum() {
        return maxConnNum;
    }

    public void setMaxConnNum(Long maxConnNum) {
        this.maxConnNum = maxConnNum;
    }

    public Long getMaxMemSize() {
        return maxMemSize;
    }

    public void setMaxMemSize(Long maxMemSize) {
        this.maxMemSize = maxMemSize;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
