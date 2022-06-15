package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/12 - 21:01
 */
public class NmsMiddlewareConfig implements Serializable {
    private Long id;
    private NmsSoft nmsSoft;
    private Long startTime;
    private Integer httpsProtocol;
    private String protocols;
    private String listenPorts;
    private Long maxConnNum;
    private Long maxHreadNum;
    private Timestamp itime;

    public NmsMiddlewareConfig() {
    }

    public NmsMiddlewareConfig(Timestamp itime) {
        this.itime = itime;
    }

    public NmsMiddlewareConfig(Long id, NmsSoft nmsSoft, Long startTime, Integer httpsProtocol, String protocols, String listenPorts, Long maxConnNum, Long maxHreadNum, Timestamp itime) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.startTime = startTime;
        this.httpsProtocol = httpsProtocol;
        this.protocols = protocols;
        this.listenPorts = listenPorts;
        this.maxConnNum = maxConnNum;
        this.maxHreadNum = maxHreadNum;
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

    public Integer getHttpsProtocol() {
        return httpsProtocol;
    }

    public void setHttpsProtocol(Integer httpsProtocol) {
        this.httpsProtocol = httpsProtocol;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }

    public String getListenPorts() {
        return listenPorts;
    }

    public void setListenPorts(String listenPorts) {
        this.listenPorts = listenPorts;
    }

    public Long getMaxConnNum() {
        return maxConnNum;
    }

    public void setMaxConnNum(Long maxConnNum) {
        this.maxConnNum = maxConnNum;
    }

    public Long getMaxHreadNum() {
        return maxHreadNum;
    }

    public void setMaxHreadNum(Long maxHreadNum) {
        this.maxHreadNum = maxHreadNum;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
