package iie.service;

import iie.pojo.NmsAuditConfig;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("NmsAuditConfigService")
public class NmsAuditConfigService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings({"unchecked", "rawtypes", "unused"})
    public List<NmsAuditConfig> getAll() {
        String hsql = " from NmsAuditConfig ";
        List<NmsAuditConfig> list = (List<NmsAuditConfig>) hibernateTemplate.find(hsql);
        if (list != null && list.size() > 0) {
            return list;
        }
        return new <NmsAuditConfig>ArrayList();
    }

    @SuppressWarnings({"unchecked"})
    public List<NmsAuditConfig> findById(int id) {
        String hsql = " from NmsAuditConfig where id = ?";
        List<NmsAuditConfig> list = (List<NmsAuditConfig>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public String getDbBaseDir() {
        String sqlbasedir = "select @@basedir as basePath from dual";

        Session session = hibernateTemplate.getSessionFactory().openSession();
        SQLQuery queryList = session.createSQLQuery(sqlbasedir);

        List<String> list = queryList.list();
        String basedir = "";
        if (list != null && list.size() > 0) {
            basedir = list.get(0).toString();
        }
        session.close();

        return basedir;
    }

    @SuppressWarnings({"unchecked"})
    public String getDbDataDir() {
        String sqldatadir = "select @@datadir as dataPath from dual";

        Session session = hibernateTemplate.getSessionFactory().openSession();
        SQLQuery queryList = session.createSQLQuery(sqldatadir);

        List<String> list = queryList.list();
        String datadir = "";
        if (list != null && list.size() > 0) {
            datadir = list.get(0).toString();
        }
        session.close();

        return datadir;
    }

    public boolean update(NmsAuditConfig na) {
        try {
            hibernateTemplate.saveOrUpdate(na);
        } catch (Exception e) {
            e.toString();
            return false;
        }
        return true;
    }

}
