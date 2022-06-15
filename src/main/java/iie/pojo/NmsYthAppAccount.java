package iie.pojo;
import java.sql.Timestamp;

public class NmsYthAppAccount  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long appId;
	private String biosName;
    private String userName;
    private String userRealName;
    private String type;
    private Timestamp itime;

    public NmsYthAppAccount() {
    }

    public NmsYthAppAccount(Timestamp itime) {
        this.itime = itime;
    }
    
    public NmsYthAppAccount(Long appId, String biosName, String userName, String userRealName, String type, Timestamp itime) {
        this.appId = appId;
        this.biosName = biosName;
        this.userName = userName;
        this.userRealName = userRealName;
        this.type = type;
        this.itime = itime;
    }

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
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

    public Timestamp getItime() {
        return this.itime;
    }
    
    public void setItime(Timestamp itime) {
        this.itime = itime;
    }

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getBiosName() {
		return biosName;
	}

	public void setBiosName(String biosName) {
		this.biosName = biosName;
	}
}