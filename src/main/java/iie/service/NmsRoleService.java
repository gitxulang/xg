package iie.service;

import iie.pojo.NmsRole;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NmsRoleService")
public class NmsRoleService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public List<NmsRole> getAll() {
        String hsql = "from NmsRole nr where nr.deled = 0";
        return (List<NmsRole>) hibernateTemplate.find(hsql);
    }

    public boolean saveRole(NmsRole nr) {
        try {
            nr.setId((int) 0);
            nr.setDeled(0);
            hibernateTemplate.save(nr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public NmsRole findById(int id) {
        String hsql = "from NmsRole na where na.id = ?";
        @SuppressWarnings("unchecked")
        List<NmsRole> list = (List<NmsRole>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsRole();
    }

    @SuppressWarnings("unchecked")
    public int countByName(String name) {
        String hsql = "from NmsRole nr where nr.role like ?";
        List<NmsRole> list = (List<NmsRole>) hibernateTemplate.find(hsql, name);
        int count = 0;
        if (list != null && list.size() > 0) {
            count = list.size();
        }
        return count;
    }

    public boolean updateRole(NmsRole role, String r) {
        try {
            role.setRole(r);
            hibernateTemplate.saveOrUpdate(role);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteById(int id) {
        boolean res = true;
        String sql = "delete from nms_role where id = " + id;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction tran = session.beginTransaction();
        try {
            int num = session.createSQLQuery(sql).executeUpdate();
            //System.out.println("[DEBUG] delete from nms_role where id = " + id + "共 " + num + " 条记录");
        } catch (Exception e) {
            res = false;
        }
        tran.commit();
        session.close();
        return res;
    }
}
