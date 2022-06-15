package iie.pojo;

import java.sql.Timestamp;

/**
 * NmsSecRule entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class NmsSecRule implements java.io.Serializable {

	private Integer id;
	private Integer sessionTimeout;
	private Integer pwdMinSize;
	private String pwdComplexity;
	private Integer pwdPeriod;
	private Integer loginAttempt;
	private Integer secInterval;
	private String appId;
	private String appName;
	private Integer ssoSwitch;
	private Timestamp itime;
	private Integer jmsStatus;


	public NmsSecRule() {

	}

	public NmsSecRule(Integer sessionTimeout, Integer pwdMinSize, Integer pwdPeriod, Integer loginAttempt,
			Integer secInterval, Timestamp itime) {
		this.sessionTimeout = sessionTimeout;
		this.pwdMinSize = pwdMinSize;
		this.pwdPeriod = pwdPeriod;
		this.loginAttempt = loginAttempt;
		this.secInterval = secInterval;
		this.itime = itime;
	}

	public NmsSecRule(Integer sessionTimeout, Integer pwdMinSize, String pwdComplexity, Integer pwdPeriod,
			Integer loginAttempt, Integer secInterval, Timestamp itime) {
		this.sessionTimeout = sessionTimeout;
		this.pwdMinSize = pwdMinSize;
		this.pwdComplexity = pwdComplexity;
		this.pwdPeriod = pwdPeriod;
		this.loginAttempt = loginAttempt;
		this.secInterval = secInterval;
		this.itime = itime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSessionTimeout() {
		return this.sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public Integer getPwdMinSize() {
		return this.pwdMinSize;
	}

	public void setPwdMinSize(Integer pwdMinSize) {
		this.pwdMinSize = pwdMinSize;
	}

	public String getPwdComplexity() {
		return this.pwdComplexity;
	}

	public void setPwdComplexity(String pwdComplexity) {
		this.pwdComplexity = pwdComplexity;
	}

	public Integer getPwdPeriod() {
		return this.pwdPeriod;
	}

	public void setPwdPeriod(Integer pwdPeriod) {
		this.pwdPeriod = pwdPeriod;
	}

	public Integer getLoginAttempt() {
		return this.loginAttempt;
	}

	public void setLoginAttempt(Integer loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	public Integer getSecInterval() {
		return this.secInterval;
	}

	public void setSecInterval(Integer secInterval) {
		this.secInterval = secInterval;
	}

	public Timestamp getItime() {
		return this.itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getSsoSwitch() {
		return ssoSwitch;
	}

	public void setSsoSwitch(Integer ssoSwitch) {
		this.ssoSwitch = ssoSwitch;
	}

	public Integer getJmsStatus() {
		return jmsStatus;
	}

	public void setJmsStatus(Integer jmsStatus) {
		this.jmsStatus = jmsStatus;
	}

}