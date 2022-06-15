package iie.tools;

import iie.pojo.NmsNetifInfo;

import java.util.List;

public class NmsNetInfoDetail {

    private Integer netNum;

    private List<NmsNetifInfo> nmsNetifInfoList;

    public Integer getNetNum() {
        return netNum;
    }

    public void setNetNum(Integer netNum) {
        this.netNum = netNum;
    }

    public List<NmsNetifInfo> getNmsNetifInfoList() {
        return nmsNetifInfoList;
    }

    public void setNmsNetifInfoList(List<NmsNetifInfo> nmsNetifInfoList) {
        this.nmsNetifInfoList = nmsNetifInfoList;
    }
}
