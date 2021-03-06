package iie.service;

import iie.pojo.NmsDatabaseStatus;
import iie.tools.PageBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xczhao
 * @date 2020/7/16 - 19:33
 */
@Service("NmsDatabaseStatusService")
public class NmsDatabaseStatusService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    public PageBean<NmsDatabaseStatus> getPageByDate(String orderKey, String orderValue, String startDate, String endDate, int begin, int offset, String assetId) throws Exception {
        Session session = hibernateTemplate.getSessionFactory().openSession();

        String sqlCount = "select count(1) from nms_database_status as ds where ds.soft_id ='" + assetId + "'";

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

        String sqlList = "select * from nms_database_status as ds where ds.soft_id = '" + assetId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and ds.itime between '" + startDate + "' and '" + endDate + "'";
        }

        sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
        SQLQuery queryList = session.createSQLQuery(sqlList);
        queryList.addEntity("NmsDatabaseStatus", NmsDatabaseStatus.class);
        List<NmsDatabaseStatus> list = queryList.list();
        session.close();

        PageBean<NmsDatabaseStatus> page = new PageBean<NmsDatabaseStatus>();
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

    public List<NmsDatabaseStatus> getPageByDateExportExcel(String orderKey, String orderValue, String startDate, String endDate, String assetId) throws Exception {
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

        // ??????list??????
        String sqlList = "select * from nms_database_status as ds where ds.soft_id = '" + assetId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and ds.itime between '" + startDate + "' and '" + endDate + "'";
        }
        // ?????????????????????????????????????????????10000?????????
        sqlList += " order by itime desc limit 10000";

        SQLQuery queryList = session.createSQLQuery(sqlList);

        // ???????????????
        queryList.addEntity("NmsDatabaseStatus", NmsDatabaseStatus.class);
        List<NmsDatabaseStatus> list = queryList.list();
        session.close();

        return list;
    }
}
