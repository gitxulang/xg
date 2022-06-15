package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/16 - 20:06
 */
public class NmsMiddlewareInstance implements Serializable {
    private Long id;
    private NmsMiddlewareConfig nmsMiddlewareConfig;
    private String name;
    private String ip;
    private String domain;
    private String listenPorts;
    private Timestamp itime;

    public NmsMiddlewareInstance() {
    }

    public NmsMiddlewareInstance(Timestamp itime) {
        this.itime = itime;
    }

    public NmsMiddlewareInstance(Long id, NmsMiddlewareConfig nmsMiddlewareConfig, String name, String ip, String domain, String listenPorts, Timestamp itime) {
        this.id = id;
        this.nmsMiddlewareConfig = nmsMiddlewareConfig;
        this.name = name;
        this.ip = ip;
        this.domain = domain;
        this.listenPorts = listenPorts;
        this.itime = itime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NmsMiddlewareConfig getNmsMiddlewareConfig() {
        return nmsMiddlewareConfig;
    }

    public void setNmsMiddlewareConfig(NmsMiddlewareConfig nmsMiddlewareConfig) {
        this.nmsMiddlewareConfig = nmsMiddlewareConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getListenPorts() {
        return listenPorts;
    }

    public void setListenPorts(String listenPorts) {
        this.listenPorts = listenPorts;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
