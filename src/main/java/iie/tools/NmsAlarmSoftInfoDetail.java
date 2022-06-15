package iie.tools;

import iie.pojo.NmsAlarmSoft;

/**
 * @author xczhao
 * @date 2020/10/18 - 11:04
 */
public class NmsAlarmSoftInfoDetail {
    private String ruleTableName;

    private Integer ruleId;

    private NmsAlarmSoft nmsAlarmSoft;

    private String action;

    public String getRuleTableName() {
        return ruleTableName;
    }

    public void setRuleTableName(String ruleTableName) {
        this.ruleTableName = ruleTableName;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public NmsAlarmSoft getNmsAlarmSoft() {
        return nmsAlarmSoft;
    }

    public void setNmsAlarmSoft(NmsAlarmSoft nmsAlarmSoft) {
        this.nmsAlarmSoft = nmsAlarmSoft;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
