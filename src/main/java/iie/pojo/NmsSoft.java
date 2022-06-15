package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/10/13 - 20:33
 */
public class NmsSoft implements Serializable {

    private Integer id;
    private NmsAssetType nmsAssetType;
    private String AIp;
    private String AName;
    private String APort;
    private String APos;
    private String AManu;
    private String ADate;
    private String AUser;
    private NmsDepartment nmsDepartment;
    private Short colled;
    private Integer collMode;
    private Integer deled;
    private Timestamp itime;

    public NmsSoft() {
    }

    public NmsSoft(Integer id, NmsAssetType nmsAssetType, String AIp, String AName, String APort, String APos, String AManu, String ADate, String AUser, NmsDepartment nmsDepartment, Short colled, Integer collMode, Integer deled, Timestamp itime) {
        this.id = id;
        this.nmsAssetType = nmsAssetType;
        this.AIp = AIp;
        this.AName = AName;
        this.APort = APort;
        this.APos = APos;
        this.AManu = AManu;
        this.ADate = ADate;
        this.AUser = AUser;
        this.nmsDepartment = nmsDepartment;
        this.colled = colled;
        this.collMode = collMode;
        this.deled = deled;
        this.itime = itime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NmsAssetType getNmsAssetType() {
        return nmsAssetType;
    }

    public void setNmsAssetType(NmsAssetType nmsAssetType) {
        this.nmsAssetType = nmsAssetType;
    }

    public String getAIp() {
        return AIp;
    }

    public void setAIp(String AIp) {
        this.AIp = AIp;
    }

    public String getAName() {
        return AName;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public String getAPort() {
        return APort;
    }

    public void setAPort(String APort) {
        this.APort = APort;
    }

    public String getAPos() {
        return APos;
    }

    public void setAPos(String APos) {
        this.APos = APos;
    }

    public String getAManu() {
        return AManu;
    }

    public void setAManu(String AManu) {
        this.AManu = AManu;
    }

    public String getADate() {
        return ADate;
    }

    public void setADate(String ADate) {
        this.ADate = ADate;
    }

    public String getAUser() {
        return AUser;
    }

    public void setAUser(String AUser) {
        this.AUser = AUser;
    }

    public NmsDepartment getNmsDepartment() {
        return nmsDepartment;
    }

    public void setNmsDepartment(NmsDepartment nmsDepartment) {
        this.nmsDepartment = nmsDepartment;
    }

    public Short getColled() {
        return colled;
    }

    public void setColled(Short colled) {
        this.colled = colled;
    }

    public Integer getCollMode() {
        return collMode;
    }

    public void setCollMode(Integer collMode) {
        this.collMode = collMode;
    }

    public Integer getDeled() {
        return deled;
    }

    public void setDeled(Integer deled) {
        this.deled = deled;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
