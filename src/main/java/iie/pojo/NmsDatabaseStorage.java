package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/7/23 - 18:29
 */
public class NmsDatabaseStorage implements Serializable {
    private Long id;
    private NmsDatabaseStatus nmsDatabaseStatus;
    private String path;
    private Long totalSize;
    private Long usedSize;
    private Timestamp itime;

    public NmsDatabaseStorage() {
    }

    public NmsDatabaseStorage(Timestamp itime) {
        this.itime = itime;
    }

    public NmsDatabaseStorage(Long id, NmsDatabaseStatus nmsDatabaseStatus, String path, Long totalSize, Long usedSize, Timestamp itime) {
        this.id = id;
        this.nmsDatabaseStatus = nmsDatabaseStatus;
        this.path = path;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.itime = itime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NmsDatabaseStatus getNmsDatabaseStatus() {
        return nmsDatabaseStatus;
    }

    public void setNmsDatabaseStatus(NmsDatabaseStatus nmsDatabaseStatus) {
        this.nmsDatabaseStatus = nmsDatabaseStatus;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(Long usedSize) {
        this.usedSize = usedSize;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
