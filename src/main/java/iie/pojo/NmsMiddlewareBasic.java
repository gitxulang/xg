package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/12 - 15:19
 */
public class NmsMiddlewareBasic implements Serializable {
    private Long id;
    private NmsSoft nmsSoft;
    private String name;
    private String version;
    private String jdkVersion;
    private Long installTime;
    private String processName;
    private Timestamp itime;

    public NmsMiddlewareBasic() {
    }

    public NmsMiddlewareBasic(Timestamp itime) {
        this.itime = itime;
    }

    public NmsMiddlewareBasic(Long id, NmsSoft nmsSoft, String name, String version, String jdkVersion, Long installTime, String processName, Timestamp itime) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.name = name;
        this.version = version;
        this.jdkVersion = jdkVersion;
        this.installTime = installTime;
        this.processName = processName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    public Long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Long installTime) {
        this.installTime = installTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
