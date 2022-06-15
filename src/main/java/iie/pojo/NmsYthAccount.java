package iie.pojo;
// default package

import java.sql.Timestamp;


/**
 * NmsYthAccount entity. @author MyEclipse Persistence Tools
 */

public class NmsYthAccount  implements java.io.Serializable {


    // Fields    

     private Long id;
     private NmsAsset nmsAsset;
     private String biosName;
     private String userName;
     private String userRealName;
     private String type;
     private String uniqueIdent;
     private Integer freq;
     private Timestamp itime;


    // Constructors

    /** default constructor */
    public NmsYthAccount() {
    }

	/** minimal constructor */
    public NmsYthAccount(Timestamp itime) {
        this.itime = itime;
    }
    
    /** full constructor */
    public NmsYthAccount(NmsAsset nmsAsset, String biosName, String userName, String userRealName, String type, String uniqueIdent, Integer freq, Timestamp itime) {
        this.nmsAsset = nmsAsset;
        this.biosName = biosName;
        this.userName = userName;
        this.userRealName = userRealName;
        this.type = type;
        this.uniqueIdent = uniqueIdent;
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

    public String getBiosName() {
		return biosName;
	}

	public void setBiosName(String biosName) {
		this.biosName = biosName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
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