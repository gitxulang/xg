package iie.tools;

import java.sql.Timestamp;

public class NmsSystemDetail {

    private Integer assetId;

    private String productName;

    private String assetName;

    private String sysName;

    private String status;
    
    private Integer statusCode;

    private String assetIP;

    private String assetIfSubmask;

    private String assetType;

    private String assetSubtype;

    private String sysInfo;

    private String sysUptime;

    private String assetNo;

    private String assetPosition;

    private String colled;

    private Integer processNum;

    private String uniqueIdent;

    private Timestamp itime;
    
    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssetIP() {
        return assetIP;
    }

    public void setAssetIP(String assetIP) {
        this.assetIP = assetIP;
    }

    public String getAssetIfSubmask() {
        return assetIfSubmask;
    }

    public void setAssetIfSubmask(String assetIfSubmask) {
        this.assetIfSubmask = assetIfSubmask;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetSubtype() {
        return assetSubtype;
    }

    public void setAssetSubtype(String assetSubtype) {
        this.assetSubtype = assetSubtype;
    }

    public String getSysInfo() {
        return sysInfo;
    }

    public void setSysInfo(String sysInfo) {
        this.sysInfo = sysInfo;
    }

    public String getSysUptime() {
        return sysUptime;
    }

    public void setSysUptime(String sysUptime) {
        this.sysUptime = sysUptime;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getAssetPosition() {
        return assetPosition;
    }

    public void setAssetPosition(String assetPosition) {
        this.assetPosition = assetPosition;
    }

    public String getColled() {
        return colled;
    }

    public void setColled(String colled) {
        this.colled = colled;
    }

    public Integer getProcessNum() {
        return processNum;
    }

    public void setProcessNum(Integer processNum) {
        this.processNum = processNum;
    }

    public String getUniqueIdent() {
        return uniqueIdent;
    }

    public void setUniqueIdent(String uniqueIdent) {
        this.uniqueIdent = uniqueIdent;
    }

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Timestamp getItime() {
		return itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}
}

