package iie.service;

import iie.pojo.NmsLicense;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NmsLicenseService")
public class NmsLicenseService {

    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public NmsLicense getLastLicense() {
        String sql = "select * from nms_license order by id desc limit 1";
        Session session = hibernateTemplate.getSessionFactory().openSession();

        SQLQuery queryList = session.createSQLQuery(sql);
        queryList.addEntity("NmsLicense", NmsLicense.class);
        List<NmsLicense> list = queryList.list();
        session.close();

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public boolean save(NmsLicense nl) {
        Session session = null;
        try {
            session = hibernateTemplate.getSessionFactory().openSession();
            String sql = "insert into nms_license(license, regtime) values('" + nl.getLicense() + "','" + nl.getRegtime() + "')";
            SQLQuery queryList = session.createSQLQuery(sql);

            if (queryList.executeUpdate() == 0) {
                session.close();
                return false;
            }
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (session != null && session.isOpen()) {
                session.close();
            }
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public int findByLicense(String license) {
        String hsql = " from NmsLicense where license = ?";
        List<NmsLicense> list = (List<NmsLicense>) hibernateTemplate.find(hsql, license);

        if (list != null && list.size() > 0) {
            return 1;
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public List<NmsLicense> getAll() {
        String hsql = "from NmsLicense nrp where 1=1";
        return (List<NmsLicense>) hibernateTemplate.find(hsql);
    }

    @SuppressWarnings("unchecked")
    public NmsLicense findById(int id) {
        String hsql = "from NmsLicense na where na.id = ?";
        List<NmsLicense> list = (List<NmsLicense>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsLicense();
    }

}
