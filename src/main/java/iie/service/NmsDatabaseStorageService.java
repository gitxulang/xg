package iie.service;

import iie.pojo.NmsDatabaseStorage;
import iie.tools.PageBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xczhao
 * @date 2020/7/23 - 18:35
 */
@Service("NmsDatabaseStorageService")
public class NmsDatabaseStorageService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    public PageBean<NmsDatabaseStorage> getPageByDate(String orderKey, String orderValue, String startDate, String endDate, int begin, int offset, String statusId) throws Exception {
        Session session = hibernateTemplate.getSessionFactory().openSession();

        String sqlCount = "select count(1) from nms_database_storage as ds where ds.status_id ='" + statusId + "'";

        if (startDate != null && endDate != null) {
            sqlCount += " and ds.itime between '" + startDate + "' and '" + endDate + "'";
        }
        SQLQuery queryCount = session.createSQLQuery(sqlCount);
        Integer count = Integer.valueOf(queryCount.list().get(0).toString());

        if (orderKey == null || orderValue == null) {
            orderKey = "id";
            orderValue = "1";
        }

        if (orderKey != null && !orderKey.equals("")) {
            if (orderValue != null && orderValue.equals("0")) {
                orderKey = orderKey + " asc";
            } else {
                orderKey = orderKey + " desc";
            }
        }

        String sqlList = "select * from nms_database_storage as ds where ds.status_id = '" + statusId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and ds.itime between '" + startDate + "' and '" + endDate + "'";
        }

        sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
        SQLQuery queryList = session.createSQLQuery(sqlList);
        queryList.addEntity("NmsDatabaseStorage", NmsDatabaseStorage.class);
        List<NmsDatabaseStorage> list = queryList.list();
        session.close();

        PageBean<NmsDatabaseStorage> page = new PageBean<NmsDatabaseStorage>();
        page.setOrderKey(orderKey);
        page.setOrderValue(Integer.valueOf(orderValue));
        page.setKey("");
        page.setValue("");

        page.setTotalCount(count);
        page.setPage(begin);
        page.setLimit(offset);
        if (count == 0) {
            page.setTotalPage(count / offset + 1);
        } else if (count % offset == 0) {
            page.setTotalPage(count / offset);
        } else {
            page.setTotalPage(count / offset + 1);
        }

        page.setList(list);

        return page;
    }

    public List<NmsDatabaseStorage> getPageByDateExportExcel(String orderKey, String orderValue, String startDate, String endDate, String statusId) throws Exception {
        Session session = hibernateTemplate.getSessionFactory().openSession();

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "itime";
        }
        if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
            orderValue = "1";
            orderKey = orderKey + " desc";
        } else {
            orderKey = orderKey + " asc";
        }

        // 获取list数据
        String sqlList = "select * from nms_database_storage as ds where ds.status_id = '" + statusId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and ds.itime between '" + startDate + "' and '" + endDate + "'";
        }
        // 所有原始数据报表最多导出最新的10000条记录
        sqlList += " order by itime desc limit 10000";

        SQLQuery queryList = session.createSQLQuery(sqlList);

        // 序列化数据
        queryList.addEntity("NmsDatabaseStorage", NmsDatabaseStorage.class);
        List<NmsDatabaseStorage> list = queryList.list();
        session.close();

        return list;
    }
}
