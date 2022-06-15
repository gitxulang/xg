package iie.pojo;

import java.sql.Timestamp;


/**
 * NmsYthSoft entity. @author MyEclipse Persistence Tools
 */

public class NmsYthSoft  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private NmsAsset nmsAsset;
	private String softName;
	private String softVersion;
	private String architecture;
	private String productType;
	private String sm3;
	private String jobId;
	private String decInfo;
	private String updateTime;
	private String uniqueIdent;
	private String platformType;
	private Integer freq;
	private Timestamp itime;

  
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSm3() {
		return sm3;
	}

	public void setSm3(String sm3) {
		this.sm3 = sm3;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getDecInfo() {
		return decInfo;
	}

	public void setDecInfo(String decInfo) {
		this.decInfo = decInfo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

    /** default constructor */
    public NmsYthSoft() {
    }

	/** minimal constructor */
    public NmsYthSoft(Timestamp itime) {
        this.itime = itime;
    }
    
    /** full constructor */
    public NmsYthSoft(NmsAsset nmsAsset, String softName, String softVersion, String architecture, String productType, String sm3, String jobId, String decInfo, String updateTime, String uniqueIdent, String platformType, Integer freq, Timestamp itime) {
        this.nmsAsset = nmsAsset;
        this.softName = softName;
        this.softVersion = softVersion;
        this.architecture = architecture;
        this.productType = productType;
        this.sm3 = sm3;
        this.jobId = jobId;
        this.decInfo = decInfo;
        this.updateTime = updateTime;
        this.uniqueIdent = uniqueIdent;
        this.platformType = platformType;
        this.freq = freq;
        this.itime = itime;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public NmsAsset getNmsAsset() {
        return this.nmsAsset;
    }
    
    public void setNmsAsset(NmsAsset nmsAsset) {
        this.nmsAsset = nmsAsset;
    }

    public String getSoftName() {
        return this.softName;
    }
    
    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getSoftVersion() {
        return this.softVersion;
    }
    
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getArchitecture() {
        return this.architecture;
    }
    
    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }
    
    public String getUniqueIdent() {
        return this.uniqueIdent;
    }
    
    public void setUniqueIdent(String uniqueIdent) {
        this.uniqueIdent = uniqueIdent;
    }

    public Integer getFreq() {
        return this.freq;
    }
    
    public void setFreq(Integer freq) {
        this.freq = freq;
    }

    public Timestamp getItime() {
        return this.itime;
    }
    
    public void setItime(Timestamp itime) {
        this.itime = itime;
    }

}