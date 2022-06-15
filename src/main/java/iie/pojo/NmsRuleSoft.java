package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/10/17 - 12:49
 */
public class NmsRuleSoft implements Serializable {
    private Integer id;
    private NmsAssetType nmsAssetType;
    private NmsSoft nmsSoft;
    private Integer DType;
    private String RName;
    private String RContent;
    private String RUnit;
    private Integer RSeq;
    private Integer REnable;
    private Integer RValue1;
    private Integer RValue2;
    private Integer RValue3;
    private Timestamp itime;

    public NmsRuleSoft() {
    }

    public NmsRuleSoft(Integer id, NmsAssetType nmsAssetType, NmsSoft nmsSoft, Integer DType, String RName, String RContent, String RUnit, Integer RSeq, Integer REnable, Integer RValue1, Integer RValue2, Integer RValue3, Timestamp itime) {
        this.id = id;
        this.nmsAssetType = nmsAssetType;
        this.nmsSoft = nmsSoft;
        this.DType = DType;
        this.RName = RName;
        this.RContent = RContent;
        this.RUnit = RUnit;
        this.RSeq = RSeq;
        this.REnable = REnable;
        this.RValue1 = RValue1;
        this.RValue2 = RValue2;
        this.RValue3 = RValue3;
        this.itime = itime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NmsAssetType getNmsAssetType() {
        return nmsAssetType;
    }

    public void setNmsAssetType(NmsAssetType nmsAssetType) {
        this.nmsAssetType = nmsAssetType;
    }

    public NmsSoft getNmsSoft() {
        return nmsSoft;
    }

    public void setNmsSoft(NmsSoft nmsSoft) {
        this.nmsSoft = nmsSoft;
    }

    public Integer getDType() {
        return DType;
    }

    public void setDType(Integer DType) {
        this.DType = DType;
    }

    public String getRName() {
        return RName;
    }

    public void setRName(String RName) {
        this.RName = RName;
    }

    public String getRContent() {
        return RContent;
    }

    public void setRContent(String RContent) {
        this.RContent = RContent;
    }

    public String getRUnit() {
        return RUnit;
    }

    public void setRUnit(String RUnit) {
        this.RUnit = RUnit;
    }

    public Integer getRSeq() {
        return RSeq;
    }

    public void setRSeq(Integer RSeq) {
        this.RSeq = RSeq;
    }

    public Integer getREnable() {
        return REnable;
    }

    public void setREnable(Integer REnable) {
        this.REnable = REnable;
    }

    public Integer getRValue1() {
        return RValue1;
    }

    public void setRValue1(Integer RValue1) {
        this.RValue1 = RValue1;
    }

    public Integer getRValue2() {
        return RValue2;
    }

    public void setRValue2(Integer RValue2) {
        this.RValue2 = RValue2;
    }

    public Integer getRValue3() {
        return RValue3;
    }

    public void setRValue3(Integer RValue3) {
        this.RValue3 = RValue3;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }
}
