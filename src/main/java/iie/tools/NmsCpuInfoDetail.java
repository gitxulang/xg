package iie.tools;

import iie.pojo.NmsCpuInfo;

import java.util.ArrayList;
import java.util.List;

public class NmsCpuInfoDetail {

    private Integer numTotal;

    private Double avg;

    private List<NmsCpuInfo> cpuInfoList;


    public NmsCpuInfoDetail(){
        cpuInfoList = new ArrayList<NmsCpuInfo>(0);
    }

    public Integer getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(Integer numTotal) {
        this.numTotal = numTotal;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public List<NmsCpuInfo> getCpuInfoList() {
        return cpuInfoList;
    }

    public void setCpuInfoList(List<NmsCpuInfo> cpuInfoList) {
        this.cpuInfoList = cpuInfoList;
    }
}
