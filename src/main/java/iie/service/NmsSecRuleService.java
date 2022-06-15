package iie.service;

import iie.pojo.NmsSecRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NmsSecRuleService")
public class NmsSecRuleService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public NmsSecRule getTheLastRule() {
        String hsql = "from NmsSecRule nm order by id desc";
        List<NmsSecRule> list = (List<NmsSecRule>) hibernateTemplate.find(hsql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public boolean updateRule(NmsSecRule nsl) {
        try {
            hibernateTemplate.saveOrUpdate(nsl);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public String getThePwdComplexity() {
        NmsSecRule rule = getTheLastRule();
        return rule.getPwdComplexity();

    }

    @SuppressWarnings("unchecked")
    public List<NmsSecRule> getAll() {
        String hsql = "from NmsSecRule nrp where 1 = 1";
        return (List<NmsSecRule>) hibernateTemplate.find(hsql);
    }

    @SuppressWarnings("unchecked")
    public NmsSecRule findById(int id) {
        String hsql = "from NmsSecRule na where na.id = ?";
        List<NmsSecRule> list = (List<NmsSecRule>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return new NmsSecRule();
    }

}
