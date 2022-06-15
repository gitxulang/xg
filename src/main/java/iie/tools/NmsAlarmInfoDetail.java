package iie.tools;

import iie.pojo.NmsAlarm;

public class NmsAlarmInfoDetail {

    private String ruleTableName;

    private Integer ruleId;

    private NmsAlarm nmsAlarm;

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

    public NmsAlarm getNmsAlarm() {
        return nmsAlarm;
    }

    public void setNmsAlarm(NmsAlarm nmsAlarm) {
        this.nmsAlarm = nmsAlarm;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
