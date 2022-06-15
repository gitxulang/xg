package iie.pojo;

import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/6 - 19:16
 */
public class NmsDatabaseBasic implements java.io.Serializable {

    // Fields
    private Long id;
    private NmsSoft nmsSoft;
    private String name;
    private String version;
    private Long installTime;
    private String processName;
    private Timestamp itime;

    /**
     * default constructor
     */
    public NmsDatabaseBasic() {
    }

    /**
     * minimal constructor
     */
    public NmsDatabaseBasic(Timestamp itime) {
        this.itime = itime;
    }

    /**
     * full constructor
     */
    public NmsDatabaseBasic(Long id, NmsSoft nmsSoft, String name, String version, Long installTime, String processName, Timestamp itime) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.name = name;
        this.version = version;
        this.installTime = installTime;
        this.processName = processName;
        this.itime = itime;
    }

    // Property accessors

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