package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/13 - 22:01
 */
public class NmsMiddlewareStatus implements Serializable {
    private Long id;
    private NmsSoft nmsSoft;
    private Long connNum;
    private Long rps;
    private Long memTotal;
    private Long memUsed;
    private Long heapTotal;
    private Long heapUsed;
    private Long nonHeapTotal;
    private Long nonHeapUsed;
    private Long jvmThreadNum;
    private Timestamp itime;

    public NmsMiddlewareStatus() {
    }

    public NmsMiddlewareStatus(Timestamp itime) {
        this.itime = itime;
    }

    public NmsMiddlewareStatus(Long id, NmsSoft nmsSoft, Long connNum, Long rps, Long memTotal, Long memUsed, Long heapTotal, Long heapUsed, Long nonHeapTotal, Long nonHeapUsed, Long jvmThreadNum, Timestamp itime) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.connNum = connNum;
        this.rps = rps;
        this.memTotal = memTotal;
        this.memUsed = memUsed;
        this.heapTotal = heapTotal;
        this.heapUsed = heapUsed;
        this.nonHeapTotal = nonHeapTotal;
        this.nonHeapUsed = nonHeapUsed;
        this.jvmThreadNum = jvmThreadNum;
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

    public Long getConnNum() {
        return connNum;
    }

    public void setConnNum(Long connNum) {
        this.connNum = connNum;
    }

    public Long getRps() {
        return rps;
    }

    public void setRps(Long rps) {
        this.rps = rps;
    }

    public Long getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Long memTotal) {
        this.memTotal = memTotal;
    }

    public Long getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Long memUsed) {
        this.memUsed = memUsed;
    }

    public Long getHeapTotal() {
        return heapTotal;
    }

    public void setHeapTotal(Long heapTotal) {
        this.heapTotal = heapTotal;
    }

    public Long getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(Long heapUsed) {
        this.heapUsed = heapUsed;
    }

    public Long getNonHeapTotal() {
        return nonHeapTotal;
    }

    public void setNonHeapTotal(Long nonHeapTotal) {
        this.nonHeapTotal = nonHeapTotal;
    }

    public Long getNonHeapUsed() {
        return nonHeapUsed;
    }

    public void setNonHeapUsed(Long nonHeapUsed) {
        this.nonHeapUsed = nonHeapUsed;
    }

    public Long getJvmThreadNum() {
        return jvmThreadNum;
    }

    public void setJvmThreadNum(Long jvmThreadNum) {
        this.jvmThreadNum = jvmThreadNum;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
