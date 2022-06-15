package iie.service;

import iie.pojo.NmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NmsConfigService")
public class NmsConfigService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public NmsConfig getConfig() {
        String hsql = "from NmsConfig nm order by id desc";
        List<NmsConfig> list = (List<NmsConfig>) hibernateTemplate.find(hsql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public boolean updateConfig(NmsConfig nsl) {
        try {
            hibernateTemplate.saveOrUpdate(nsl);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<NmsConfig> getAll() {
        String hsql = "from NmsConfig nrp where 1 = 1";
        return (List<NmsConfig>) hibernateTemplate.find(hsql);
    }

    @SuppressWarnings("unchecked")
    public NmsConfig findById(int id) {
        String hsql = "from NmsConfig na where na.id = ?";
        List<NmsConfig> list = (List<NmsConfig>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsConfig();
    }
}
