package iie.service;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAuditLog;
import iie.tools.DateSqlUtils;
import iie.tools.PageBean;
import iie.tools.excel.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("NmsAuditLogService")
public class NmsAuditLogService {

    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public List<NmsAuditLog> getAllexportExcel(String username, String ip, String content, String modname, String logtype, String logrest, String orderKey, String orderValue,
                                               String atimeStart, String atimeEnd, NmsAdmin admin) {
        if (admin.getUsername().equals("root")) {

            DetachedCriteria criteria = DetachedCriteria.forClass(NmsAuditLog.class);
            criteria.addOrder(Order.desc("itime"));

            if (username != null) {
                criteria.add(Restrictions.like("username", "%" + username + "%"));
            }

            if (ip != null) {
                criteria.add(Restrictions.like("ip", "%" + ip + "%"));
            }

            if (content != null) {
                criteria.add(Restrictions.like("content", "%" + content + "%"));
            }

            if (modname != null) {
                criteria.add(Restrictions.like("modname", "%" + modname + "%"));
            }

            if (logtype != null) {
                criteria.add(Restrictions.like("logtype", "%" + logtype + "%"));
            }

            if (logrest != null) {
                criteria.add(Restrictions.like("logrest", "%" + logrest + "%"));
            }

            if (atimeStart != null && atimeEnd != null) {
            	criteria.add(Restrictions.ge("itime", DateSqlUtils.strToDate(atimeStart)));
            	criteria.add(Restrictions.le("itime", DateSqlUtils.strToDate(atimeEnd)));
            }

            List<NmsAuditLog> list = (List<NmsAuditLog>) hibernateTemplate.findByCriteria(criteria);
            return list;
        } else {
            if (admin.getNmsRole().getRole().equals("审计管理员") || admin.getNmsRole().getRole().equals("安全保密员")) {
                return getAllexportExcelRole(username, ip, content, modname, logtype, logrest, orderKey, orderValue, atimeStart, atimeEnd, admin.getNmsRole().getRole());
            } else {
                return new ArrayList<NmsAuditLog>();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public List<NmsAuditLog> getAllexportExcelRole(String username, String ip, String content, String modname, String logtype, String logrest, String orderKey, String orderValue,
                                                   String atimeStart, String atimeEnd, String roleString) {
        String querySql = "";
        if (roleString.equals("审计管理员")) {
            querySql = "SELECT * FROM nms_audit_log as na WHERE username IN"
                    + " (SELECT username FROM nms_admin LEFT JOIN nms_role ON nms_admin.role_id = nms_role.id WHERE nms_role.role = '系统管理员' OR nms_role.role = '安全保密员')";
        } else {
            querySql = "SELECT * FROM nms_audit_log as na WHERE username IN"
                    + " (SELECT username FROM nms_admin LEFT JOIN nms_role ON nms_admin.role_id = nms_role.id WHERE nms_role.role = '审计管理员')";
        }
        StringBuilder builderQuerySql = new StringBuilder(querySql);
        if (username != null && username.length() > 0) {
            builderQuerySql.append(" and na.username like '%" + username
                    + "%' ");
        }

        if (ip != null && ip.length() > 0) {
            builderQuerySql.append(" and na.ip like '%" + ip + "%' ");
        }

        if (content != null && content.length() > 0) {
            builderQuerySql.append(" and na.content like '%" + content + "%' ");
        }

        if (modname != null && modname.length() > 0) {
            builderQuerySql.append(" and na.modname like '%" + modname + "%' ");
        }

        if (logtype != null && logtype.length() > 0) {
            builderQuerySql.append(" and na.logtype like '%" + logtype + "%' ");
        }

        if (logrest != null && logrest.length() > 0) {
            builderQuerySql.append(" and na.logrest like '%" + logrest + "%' ");
        }

        if (atimeStart != null && atimeStart.length() > 0) {
            builderQuerySql.append(" and na.a_time >= '" + atimeStart + "'");
        }

        if (atimeEnd != null && atimeEnd.length() > 0) {
            builderQuerySql.append(" and na.a_time <= '" + atimeEnd + "'");
        }


        if (orderKey != null) {
            if (orderValue != null && orderValue.equals("0")) {
                builderQuerySql.append(" ORDER BY " + orderKey + " DESC");
            } else {
                builderQuerySql.append(" ORDER BY " + orderKey + " ASC");
            }
        } else {
            builderQuerySql.append(" ORDER BY itime DESC");
        }

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();
        session.close();

        List<NmsAuditLog> auditLogs = new ArrayList<NmsAuditLog>();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                NmsAuditLog auditLog = new NmsAuditLog((String) objects[0],
                        (String) objects[1], (String) objects[2],
                        (String) objects[3], (String) objects[4], (String) objects[5], (String) objects[6], (String) objects[7],
                        (Timestamp) objects[8]);
                auditLogs.add(auditLog);
            }
        }
        return auditLogs;
    }

    @SuppressWarnings("unchecked")
    public PageBean<NmsAuditLog> getListByCondition(String username, String ip, String content, String modname, String logtype, String logrest,
                                                    String atimeStart, String atimeEnd, int begin, int offset, String orderKey, String orderValue,
                                                    NmsAdmin admin) throws Exception {

        if (admin.getUsername().equals("root")) {

            // 加载本类
            DetachedCriteria criteria = DetachedCriteria.forClass(NmsAuditLog.class);
            DetachedCriteria criteriaForCount = DetachedCriteria.forClass(NmsAuditLog.class);

            //排序条件默认按照id降序(时间降序)
            if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
                criteria.addOrder(Order.desc("itime"));
                criteriaForCount.addOrder(Order.desc("itime"));
            }

            if (orderKey != null && !orderKey.equals("")) {

                if (orderValue != null && orderValue.equals("0")) {
                    criteria.addOrder(Order.desc(orderKey));
                    criteriaForCount.addOrder(Order.desc(orderKey));
                } else {
                    criteria.addOrder(Order.asc(orderKey));
                    criteriaForCount.addOrder(Order.asc(orderKey));
                }
            }

            // 设定基础条件（部分可选）
            if (username != null) {
                criteria.add(Restrictions
                        .like("username", "%" + username + "%"));
                criteriaForCount.add(Restrictions.like("username", "%"
                        + username + "%"));
            }
            if (ip != null) {
                criteria.add(Restrictions.like("ip", "%" + ip + "%"));
                criteriaForCount.add(Restrictions.like("ip", "%" + ip + "%"));
            }

            if (content != null) {
                criteria.add(Restrictions.like("content", "%" + content + "%"));
                criteriaForCount.add(Restrictions.like("content", "%" + content + "%"));
            }

            if (modname != null) {
                criteria.add(Restrictions.like("modname", "%" + modname + "%"));
                criteriaForCount.add(Restrictions.like("modname", "%" + modname + "%"));
            }

            if (logtype != null) {
                criteria.add(Restrictions.like("logtype", "%" + logtype + "%"));
                criteriaForCount.add(Restrictions.like("logtype", "%" + logtype + "%"));
            }

            if (logrest != null) {
                criteria.add(Restrictions.like("logrest", "%" + logrest + "%"));
                criteriaForCount.add(Restrictions.like("logrest", "%" + logrest + "%"));
            }

            if (atimeStart != null && atimeEnd != null) {
                criteria.add(Restrictions.ge("ATime", atimeStart));
                criteria.add(Restrictions.le("ATime", atimeEnd));
                criteriaForCount.add(Restrictions.ge("ATime", atimeStart));
                criteriaForCount.add(Restrictions.le("ATime", atimeEnd));
            }

            int start = (begin - 1) * offset;

            // 获取数据总数
            int totalCount = ((Long) criteriaForCount
                    .setProjection(Projections.rowCount())
                    .getExecutableCriteria(
                            this.hibernateTemplate.getSessionFactory()
                                    .getCurrentSession()).uniqueResult())
                    .intValue();

            List<NmsAuditLog> list = (List<NmsAuditLog>) hibernateTemplate
                    .findByCriteria(criteria, start, offset);

            // 计算总页数
            int totalPage = 1;
            if (totalCount == 0) {
                totalPage = totalCount / offset + 1;
            } else if (totalCount % offset == 0) {
                totalPage = totalCount / offset;
            } else {
                totalPage = totalCount / offset + 1;
            }

            // 创建PageBean对象返回数据
            PageBean<NmsAuditLog> page = new PageBean<NmsAuditLog>();
            page.setOrderKey(orderKey);
            page.setOrderValue(1);
            page.setTotalCount(totalCount);
            page.setPage(begin);
            page.setTotalPage(totalPage);
            String key = null;
            String value = null;
            page.setKey(key);
            page.setValue(value);
            page.setList(list);

            return page;
        } else {
            System.out.println("admin.getNmsRole().getRole()=" + admin.getNmsRole().getRole());
            if (admin.getNmsRole().getRole().equals("审计管理员") || admin.getNmsRole().getRole().equals("安全保密员") || admin.getNmsRole().getRole().equals("网络系统管理员") || admin.getUsername().equals("root")) {
                return getListByConditionRole(username, ip, atimeStart,
                        atimeEnd, begin, offset, admin.getNmsRole().getRole(), content, modname, logtype, logrest, orderKey, orderValue);
            } else {
                return new PageBean<NmsAuditLog>();
            }
        }

    }

    public static String doSQLFilter(String str) {
        //   str=str.replaceAll("\\.","。");
        str = str.replaceAll(":", "：");
        str = str.replaceAll(";", "；");
        str = str.replaceAll("&", "＆");
        str = str.replaceAll("<", "＜");
        str = str.replaceAll(">", "＞");
        str = str.replaceAll("'", "＇");
        str = str.replaceAll("\"", "“");
        str = str.replaceAll("--", "－－");
        str = str.replaceAll("/", "／");
        str = str.replaceAll("%", "％");
        str = str.replaceAll("\\+", "＋");
        str = str.replaceAll("\\(", "（");
        str = str.replaceAll("\\)", "）");
        return str;
    }

    @SuppressWarnings({"rawtypes"})
    public PageBean<NmsAuditLog> getListByConditionRole(String username,
                                                        String ip, String atimeStart, String atimeEnd, int begin,
                                                        int offset, String roleString, String content, String modname, String logtype, String logrest, String orderKey, String orderValue) {

        String countSql = "";
        String querySql = "";

        if (roleString.equals("审计管理员")) {
            countSql = "SELECT count(*) FROM nms_audit_log as na WHERE username IN"
                    + " (SELECT username FROM nms_admin LEFT JOIN nms_role ON nms_admin.role_id = nms_role.id WHERE nms_role.role = '系统管理员' OR nms_role.role = '安全保密员')";

            querySql = "SELECT * FROM nms_audit_log as na WHERE username IN"
                    + " (SELECT username FROM nms_admin LEFT JOIN nms_role ON nms_admin.role_id = nms_role.id WHERE nms_role.role = '系统管理员' OR nms_role.role = '安全保密员')";
        } else if (roleString.equals("安全保密员")) {
            countSql = "SELECT count(*) FROM nms_audit_log as na WHERE username IN"
                    + " (SELECT username FROM nms_admin LEFT JOIN nms_role ON nms_admin.role_id = nms_role.id WHERE nms_role.role = '审计管理员')";

            querySql = "SELECT * FROM nms_audit_log as na WHERE username IN"
                    + " (SELECT username FROM nms_admin LEFT JOIN nms_role ON nms_admin.role_id = nms_role.id WHERE nms_role.role = '审计管理员')";
        } else if (roleString.equals("网络系统管理员") || username.equals("root")) {
            countSql = "SELECT count(*) FROM nms_audit_log";
            querySql = "SELECT * FROM nms_audit_log";
        }


        StringBuilder builderQuerySql = new StringBuilder(querySql);
        StringBuilder builderCountSql = new StringBuilder(countSql);
        if (username != null && username.length() > 0) {
            builderQuerySql.append(" and na.username like '%" + doSQLFilter(username) + "%' ");
            builderCountSql.append(" and na.username like '%" + doSQLFilter(username) + "%' ");
        }
        if (ip != null && ip.length() > 0) {
            builderQuerySql.append(" and na.ip like '%" + doSQLFilter(ip) + "%' ");
            builderCountSql.append(" and na.ip like '%" + doSQLFilter(ip) + "%' ");
        }

        if (content != null && content.length() > 0) {
            builderQuerySql.append(" and na.content like '%" + doSQLFilter(content) + "%' ");
            builderCountSql.append(" and na.content like '%" + doSQLFilter(content) + "%' ");
        }

        if (modname != null && modname.length() > 0) {
            builderQuerySql.append(" and na.modname like '%" + doSQLFilter(modname) + "%' ");
            builderCountSql.append(" and na.modname like '%" + doSQLFilter(modname) + "%' ");
        }

        if (logtype != null && logtype.length() > 0) {
            builderQuerySql.append(" and na.logtype like '%" + doSQLFilter(logtype) + "%' ");
            builderCountSql.append(" and na.logtype like '%" + doSQLFilter(logtype) + "%' ");
        }

        if (logrest != null && logrest.length() > 0) {
            builderQuerySql.append(" and na.logrest like '%" + doSQLFilter(logrest) + "%' ");
            builderCountSql.append(" and na.logrest like '%" + doSQLFilter(logrest) + "%' ");
        }

        if (atimeStart != null && atimeStart.length() > 0) {
            builderQuerySql.append(" and na.a_time >= '" + atimeStart + "'");
            builderCountSql.append(" and na.a_time >= '" + atimeStart + "'");
        }
        if (atimeEnd != null && atimeEnd.length() > 0) {
            builderQuerySql.append(" and na.a_time <= '" + atimeEnd + "'");
            builderCountSql.append(" and na.a_time <= '" + atimeEnd + "'");
        }

        if (StringUtils.isNotBlank(orderKey)) {
            if (StringUtils.isNotBlank(orderValue) && orderValue.equals("0")) {
                builderQuerySql.append(" ORDER BY " + orderKey + " DESC");
            } else {
                builderQuerySql.append(" ORDER BY " + orderKey + " ASC");
            }
        } else {
            builderQuerySql.append(" ORDER BY itime DESC");
        }

        builderQuerySql.append(" LIMIT " + (begin - 1) * offset + "," + offset);

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();

        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Integer totalCount = Integer.valueOf(countQuery.list().get(0).toString());

        session.close();

        List<NmsAuditLog> auditLogs = new ArrayList<NmsAuditLog>();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                NmsAuditLog auditLog = new NmsAuditLog((String) objects[0],
                        (String) objects[1], (String) objects[2],
                        (String) objects[3], (String) objects[4], (String) objects[5], (String) objects[6], (String) objects[7],
                        (Timestamp) objects[8]);
                auditLogs.add(auditLog);
            }
        }

        // 计算总页数
        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount / offset;
        } else {
            totalPage = totalCount / offset + 1;
        }

        // 创建PageBean对象返回数据
        PageBean<NmsAuditLog> page = new PageBean<NmsAuditLog>();
        page.setOrderKey(null);
        page.setOrderValue(0);
        page.setTotalCount(totalCount);
        page.setPage(begin);
        page.setTotalPage(totalPage);
        String key = null;
        String value = null;
        page.setKey(key);
        page.setValue(value);
        page.setList(auditLogs);

        return page;
    }

    public boolean add(String username, String ip, String content, String modname, String logtype, String logrest) {
        NmsAuditLog auditLog = new NmsAuditLog();
        auditLog.setUsername(username);
        auditLog.setIp(ip);
        auditLog.setContent(content);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        auditLog.setATime(dateFormat.format(date));
        String a_time = dateFormat.format(date);

        Session session = null;
        try {
            session = hibernateTemplate.getSessionFactory().openSession();

            String sql = "insert into nms_audit_log(id,ip, content, modname, logtype, logrest, a_time, username) values('" + UuidUtil.get32UUID() + "','" + ip + "','" + content + "','" + modname + "','" + logtype + "','" + logrest + "','" + a_time + "','" + username + "')";
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
}
