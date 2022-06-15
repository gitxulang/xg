package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/16 - 17:18
 */
public class NmsDatabaseStatus implements Serializable {
    private Long id;
    private NmsSoft nmsSoft;
    private Long totalSize;
    private Long memSize;
    private Long tps;
    private Long ioBusy;
    private Long connNum;
    private Long activeConnNum;
    private Long processNum;
    private Long deadLockNum;
    private String userList;
    private Timestamp itime;

    public NmsDatabaseStatus() {
    }

    public NmsDatabaseStatus(Timestamp itime) {
        this.itime = itime;
    }

    public NmsDatabaseStatus(Long id, NmsSoft nmsSoft, Long totalSize, Long memSize, Long tps, Long ioBusy, Long connNum, Long activeConnNum, Long processNum, Long deadLockNum, String userList, Timestamp itime) {
        this.id = id;
        this.nmsSoft = nmsSoft;
        this.totalSize = totalSize;
        this.memSize = memSize;
        this.tps = tps;
        this.ioBusy = ioBusy;
        this.connNum = connNum;
        this.activeConnNum = activeConnNum;
        this.processNum = processNum;
        this.deadLockNum = deadLockNum;
        this.userList = userList;
        this.itime = itime;
    }

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

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getMemSize() {
        return memSize;
    }

    public void setMemSize(Long memSize) {
        this.memSize = memSize;
    }

    public Long getTps() {
        return tps;
    }

    public void setTps(Long tps) {
        this.tps = tps;
    }

    public Long getIoBusy() {
        return ioBusy;
    }

    public void setIoBusy(Long ioBusy) {
        this.ioBusy = ioBusy;
    }

    public Long getConnNum() {
        return connNum;
    }

    public void setConnNum(Long connNum) {
        this.connNum = connNum;
    }

    public Long getActiveConnNum() {
        return activeConnNum;
    }

    public void setActiveConnNum(Long activeConnNum) {
        this.activeConnNum = activeConnNum;
    }

    public Long getProcessNum() {
        return processNum;
    }

    public void setProcessNum(Long processNum) {
        this.processNum = processNum;
    }

    public Long getDeadLockNum() {
        return deadLockNum;
    }

    public void setDeadLockNum(Long deadLockNum) {
        this.deadLockNum = deadLockNum;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
