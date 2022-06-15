package iie.tools;

import java.util.List;

public class NmsFilePartsData {

    private String filesys;

    private Float currentPartTotal;

    private Float currentDiskUtilization;

    private List<NmsFileDiskData> diskUtilizationData;

    public String getFilesys() {
        return filesys;
    }

    public void setFilesys(String filesys) {
        this.filesys = filesys;
    }

    public Float getCurrentPartTotal() {
        return currentPartTotal;
    }

    public void setCurrentPartTotal(Float currentPartTotal) {
        this.currentPartTotal = currentPartTotal;
    }

    public List<NmsFileDiskData> getDiskUtilizationData() {
        return diskUtilizationData;
    }

    public void setDiskUtilizationData(List<NmsFileDiskData> diskUtilizationData) {
        this.diskUtilizationData = diskUtilizationData;
    }

    public Float getCurrentDiskUtilization() {
        return currentDiskUtilization;
    }

    public void setCurrentDiskUtilization(Float currentDiskUtilization) {
        this.currentDiskUtilization = currentDiskUtilization;
    }
}
