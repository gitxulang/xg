package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xczhao
 * @date 2020/10/17 - 22:34
 */
public class NmsAlarmSoft implements Serializable {
    // Fields

    private Integer id;
    private NmsSoft nmsSoft;
    private String AName;
    private String AIndex;
    private String AType;
    private String AContent;
    private Integer ALevel;
    private Integer DStatus;
    private String DPeople;
    private String DTime;
	private String STime;
	private String ATime;
	private Integer ACount;
    private Timestamp itime;
    private Set nmsAlarmReports = new HashSet(0);

    public NmsAlarmSoft() {
    }

    public NmsAlarmSoft(Timestamp itime) {
        this.itime = itime;
    }

    public NmsAlarmSoft(Integer id, NmsSoft nmsSoft, String AName, String AIndex, 
    		String AType, String AContent, Integer ALevel, Integer DStatus, 
    		String DPeople, String DTime, String STime, String ATime, Integer ACount, Timestamp itime, Set nmsAlarmReports) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.AName = AName;
        this.AIndex = AIndex;
        this.AType = AType;
        this.AContent = AContent;
        this.ALevel = ALevel;
        this.DStatus = DStatus;
        this.DPeople = DPeople;
        this.DTime = DTime;
        this.STime = STime;
        this.ATime = ATime;
        this.ACount = ACount;
        this.itime = itime;
        this.nmsAlarmReports = nmsAlarmReports;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NmsSoft getNmsSoft() {
        return nmsSoft;
    }

    public void setNmsSoft(NmsSoft nmsSoft) {
        this.nmsSoft = nmsSoft;
    }

    public String getAName() {
        return AName;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public String getAIndex() {
        return AIndex;
    }

    public void setAIndex(String AIndex) {
        this.AIndex = AIndex;
    }

    public String getAType() {
        return AType;
    }

    public void setAType(String AType) {
        this.AType = AType;
    }

    public String getAContent() {
        return AContent;
    }

    public void setAContent(String AContent) {
        this.AContent = AContent;
    }

    public Integer getALevel() {
        return ALevel;
    }

    public void setALevel(Integer ALevel) {
        this.ALevel = ALevel;
    }

    public Integer getDStatus() {
        return DStatus;
    }

    public void setDStatus(Integer DStatus) {
        this.DStatus = DStatus;
    }

    public String getDPeople() {
        return DPeople;
    }

    public void setDPeople(String DPeople) {
        this.DPeople = DPeople;
    }

    public String getDTime() {
        return DTime;
    }

    public void setDTime(String DTime) {
        this.DTime = DTime;
    }

    public String getATime() {
        return ATime;
    }

    public void setATime(String ATime) {
        this.ATime = ATime;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }

    public Set getNmsAlarmReports() {
        return nmsAlarmReports;
    }

    public void setNmsAlarmReports(Set nmsAlarmReports) {
        this.nmsAlarmReports = nmsAlarmReports;
    }

	public String getSTime() {
		return STime;
	}

	public void setSTime(String sTime) {
		STime = sTime;
	}

	public Integer getACount() {
		return ACount;
	}

	public void setACount(Integer aCount) {
		ACount = aCount;
	}
}
