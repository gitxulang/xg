package iie.pojo;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class NmsConfig implements java.io.Serializable {

	private Integer id;
	private Integer jmsSwitch;
	private Integer jmsInterval;
	private String jmsAddress;
	private String jmsPort;
	private String jmsAgptIp;
	private String jmsSfrzIp;
	private String jmsPtglIp;
	private Integer dbDeleteInterval;
	private Integer dbOnlineInterval;
	private Integer alarmPingInterval;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	
	public NmsConfig(Integer jmsSwitch, Integer jmsInterval, String jmsAddress, 
			String jmsPort, String jmsAgptIp, String jmsSfrzIp, String jmsPtglIp,
			Integer dbDeleteInterval, Integer dbOnlineInterval, Integer alarmPingInterval, 
			String reserve1, String reserve2, String reserve3, String reserve4, Timestamp itime) {
		this.jmsSwitch = jmsSwitch;
		this.jmsInterval = jmsInterval;
		this.jmsAddress = jmsAddress;
		this.jmsPort = jmsPort;
		this.jmsAgptIp = jmsAgptIp;
		this.jmsSfrzIp = jmsSfrzIp;
		this.jmsPtglIp = jmsPtglIp;
		this.dbDeleteInterval = dbDeleteInterval;
		this.dbOnlineInterval = dbOnlineInterval;
		this.alarmPingInterval = alarmPingInterval;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.itime = itime;
	}	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJmsSwitch() {
		return jmsSwitch;
	}

	public void setJmsSwitch(Integer jmsSwitch) {
		this.jmsSwitch = jmsSwitch;
	}

	public Integer getJmsInterval() {
		return jmsInterval;
	}

	public void setJmsInterval(Integer jmsInterval) {
		this.jmsInterval = jmsInterval;
	}

	public String getJmsAddress() {
		return jmsAddress;
	}

	public void setJmsAddress(String jmsAddress) {
		this.jmsAddress = jmsAddress;
	}

	public String getJmsPort() {
		return jmsPort;
	}

	public void setJmsPort(String jmsPort) {
		this.jmsPort = jmsPort;
	}

	public String getJmsAgptIp() {
		return jmsAgptIp;
	}

	public void setJmsAgptIp(String jmsAgptIp) {
		this.jmsAgptIp = jmsAgptIp;
	}

	public String getJmsSfrzIp() {
		return jmsSfrzIp;
	}

	public void setJmsSfrzIp(String jmsSfrzIp) {
		this.jmsSfrzIp = jmsSfrzIp;
	}

	public String getJmsPtglIp() {
		return jmsPtglIp;
	}

	public void setJmsPtglIp(String jmsPtglIp) {
		this.jmsPtglIp = jmsPtglIp;
	}

	public Integer getDbDeleteInterval() {
		return dbDeleteInterval;
	}

	public void setDbDeleteInterval(Integer dbDeleteInterval) {
		this.dbDeleteInterval = dbDeleteInterval;
	}

	public Integer getDbOnlineInterval() {
		return dbOnlineInterval;
	}

	public void setDbOnlineInterval(Integer dbOnlineInterval) {
		this.dbOnlineInterval = dbOnlineInterval;
	}

	public Integer getAlarmPingInterval() {
		return alarmPingInterval;
	}

	public void setAlarmPingInterval(Integer alarmPingInterval) {
		this.alarmPingInterval = alarmPingInterval;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public Timestamp getItime() {
		return itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	private Timestamp itime;

	public NmsConfig() {

	}
}