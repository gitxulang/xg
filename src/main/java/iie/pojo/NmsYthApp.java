package iie.pojo;


import java.sql.Timestamp;

public class NmsYthApp  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	Long id;
	private NmsAsset nmsAsset;
	private String appName;
	private String appPort;
	private Integer freq;
	private Timestamp itime;

    public NmsYthApp() {
    }

    public NmsYthApp(Timestamp itime) {
        this.itime = itime;
    }
    
    public NmsYthApp(NmsAsset nmsAsset, String appName, String appPort, Integer freq, Timestamp itime) {
        this.nmsAsset = nmsAsset;
        this.setAppName(appName);
        this.setAppPort(appPort);
        this.freq = freq;
        this.itime = itime;
    }

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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPort() {
		return appPort;
	}

	public void setAppPort(String appPort) {
		this.appPort = appPort;
	}
   








}