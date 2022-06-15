package iie.service;

import iie.pojo.*;
import iie.tools.NmsDatabaseDetail;
import iie.tools.NmsPingStaticsInfo;
import iie.tools.PageBean;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author xczhao
 * @date 2020/7/6 - 19:44
 */
@Service("NmsDatabaseBasicService")
public class NmsDatabaseBasicService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @Autowired
    NmsPingInfoService pService;

    @Autowired
    NmsAssetService nmsAssetService;

    public PageBean<NmsDatabaseBasic> getPageByDate(String orderKey, String orderValue, String startDate, String endDate, int begin, int offset, String assetId) throws Exception {

        Session session = hibernateTemplate.getSessionFactory().openSession();

        String sqlCount = "select count(1) from nms_database_basic as db where db.soft_id ='" + assetId + "'";

        if (startDate != null && endDate != null) {
            sqlCount += " and db.itime between '" + startDate + "' and '" + endDate + "'";
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

        String sqlList = "select * from nms_database_basic as db where db.soft_id = '" + assetId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and db.itime between '" + startDate + "' and '" + endDate + "'";
        }

        sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
        SQLQuery queryList = session.createSQLQuery(sqlList);
        queryList.addEntity("NmsDatabaseBasic", NmsDatabaseBasic.class);
        List<NmsDatabaseBasic> list = queryList.list();
        session.close();

        PageBean<NmsDatabaseBasic> page = new PageBean<NmsDatabaseBasic>();
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

    public List<NmsDatabaseBasic> getPageByDateExportExcel(String orderKey, String orderValue, String startDate, String endDate, String assetId) throws Exception {
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
        String sqlList = "select * from nms_database_basic as db where db.soft_id = '" + assetId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and db.itime between '" + startDate + "' and '" + endDate + "'";
        }
        // 所有原始数据报表最多导出最新的10000条记录
        sqlList += " order by itime desc limit 10000";

        SQLQuery queryList = session.createSQLQuery(sqlList);

        // 序列化数据
        queryList.addEntity("NmsDatabaseBasic", NmsDatabaseBasic.class);
        List<NmsDatabaseBasic> list = queryList.list();
        session.close();

        return list;
    }

    public NmsDatabaseDetail assetDatabaseInfoOverview(Integer softId) throws Exception {
        NmsSoft nmsSoft = hibernateTemplate.get(NmsSoft.class, softId);

        if (nmsSoft == null) {
            return new NmsDatabaseDetail();
        }

        DetachedCriteria nmsDatabaseBasicInfoCriteria = DetachedCriteria
                .forClass(NmsDatabaseBasic.class);
        nmsDatabaseBasicInfoCriteria.add(Restrictions.eq("nmsSoft", nmsSoft));
        nmsDatabaseBasicInfoCriteria.addOrder(Order.desc("id"));

        List<NmsDatabaseBasic> nmsDatabaseBasicInfoList = (List<NmsDatabaseBasic>) hibernateTemplate
                .findByCriteria(nmsDatabaseBasicInfoCriteria);

        DetachedCriteria nmsDatabaseConfigInfoCriteria = DetachedCriteria
                .forClass(NmsDatabaseConfig.class);
        nmsDatabaseConfigInfoCriteria.add(Restrictions.eq("nmsSoft", nmsSoft));
        nmsDatabaseConfigInfoCriteria.addOrder(Order.desc("id"));

        List<NmsDatabaseConfig> nmsDatabaseConfigInfoList = (List<NmsDatabaseConfig>) hibernateTemplate
                .findByCriteria(nmsDatabaseConfigInfoCriteria);

        DetachedCriteria nmsDatabaseStatusInfoCriteria = DetachedCriteria
                .forClass(NmsDatabaseStatus.class);
        nmsDatabaseStatusInfoCriteria.add(Restrictions.eq("nmsSoft", nmsSoft));
        nmsDatabaseStatusInfoCriteria.addOrder(Order.desc("id"));

        List<NmsDatabaseStatus> nmsDatabaseStatusInfoList = (List<NmsDatabaseStatus>) hibernateTemplate
                .findByCriteria(nmsDatabaseStatusInfoCriteria);

        String nmsAlarmSql = "select d_status,count(1) from nms_alarm_soft where d_status in (0,1) and soft_id="
                + softId + " group by d_status";

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query nmsAlarmQuery = session.createSQLQuery(nmsAlarmSql);
        List nmsAlarmList = nmsAlarmQuery.list();
        session.close();

        NmsDatabaseDetail nmsDatabaseDetail = new NmsDatabaseDetail();
        nmsDatabaseDetail.setSoftId(nmsSoft.getId());
        nmsDatabaseDetail.setSoftName(nmsSoft.getAName());

        if (nmsDatabaseStatusInfoList != null && nmsDatabaseStatusInfoList.size() > 0) {
            NmsDatabaseStatus nmsDatabaseStatusInfo = nmsDatabaseStatusInfoList.get(0);
            nmsDatabaseDetail.setStatusTotalSize(nmsDatabaseStatusInfo.getTotalSize());
            nmsDatabaseDetail.setMemSize(nmsDatabaseStatusInfo.getMemSize());
            nmsDatabaseDetail.setTps(nmsDatabaseStatusInfo.getTps());
            nmsDatabaseDetail.setUserList(nmsDatabaseStatusInfo.getUserList());
            nmsDatabaseDetail.setConnNum(nmsDatabaseStatusInfo.getConnNum());
            nmsDatabaseDetail.setDeadLockNum(nmsDatabaseStatusInfo.getDeadLockNum());

            DetachedCriteria nmsDatabaseSqlInfoCriteria = DetachedCriteria.forClass(NmsDatabaseSql.class);
            nmsDatabaseSqlInfoCriteria.add(Restrictions.eq("nmsDatabaseStatus", nmsDatabaseStatusInfo));
            nmsDatabaseSqlInfoCriteria.addOrder(Order.desc("execTime"));

            List<NmsDatabaseSql> nmsDatabaseSqlInfoList = (List<NmsDatabaseSql>) hibernateTemplate
                    .findByCriteria(nmsDatabaseSqlInfoCriteria, 0, 5);

            if (nmsDatabaseSqlInfoList != null && nmsDatabaseSqlInfoList.size() > 0) {
                nmsDatabaseDetail.setSqls(nmsDatabaseSqlInfoList);
            }

            DetachedCriteria nmsDatabaseStorageInfoCriteria = DetachedCriteria.forClass(NmsDatabaseStorage.class);
            nmsDatabaseStorageInfoCriteria.add(Restrictions.eq("nmsDatabaseStatus", nmsDatabaseStatusInfo));
            nmsDatabaseStorageInfoCriteria.addOrder(Order.desc("id"));

            List<NmsDatabaseStorage> nmsDatabaseStorageInfoList = (List<NmsDatabaseStorage>) hibernateTemplate
                    .findByCriteria(nmsDatabaseStorageInfoCriteria);

            if (nmsDatabaseStorageInfoList != null && nmsDatabaseStorageInfoList.size() > 0) {
                nmsDatabaseDetail.setStorages(nmsDatabaseStorageInfoList);
            }
        }

        if (nmsDatabaseStatusInfoList != null && nmsDatabaseStatusInfoList.size() >= 2) {
            NmsDatabaseStatus nmsDatabaseStatusInfo1 = nmsDatabaseStatusInfoList.get(0);
            NmsDatabaseStatus nmsDatabaseStatusInfo2 = nmsDatabaseStatusInfoList.get(1);

            Timestamp itime1 = nmsDatabaseStatusInfo1.getItime();
            Long tps1 = nmsDatabaseStatusInfo1.getTps();

            Timestamp itime2 = nmsDatabaseStatusInfo2.getItime();
            Long tps2 = nmsDatabaseStatusInfo2.getTps();

            if (tps2 == null || tps1 == null || itime1 == null || itime2 == null ||
                    tps1 == tps2 || itime1.equals(itime2) || tps1 < tps2 || itime1.before(itime2)) {
                nmsDatabaseDetail.setNumPerSecond(Long.valueOf(0));
            } else {
                long tpsDiff = tps1 - tps2;
                long timeDiff = (itime1.getTime() - itime2.getTime()) / 1000;
                nmsDatabaseDetail.setNumPerSecond(tpsDiff / timeDiff);
            }
        } else {
            nmsDatabaseDetail.setNumPerSecond(null);
        }

        if (nmsDatabaseConfigInfoList != null && nmsDatabaseConfigInfoList.size() > 0) {
            NmsDatabaseConfig nmsDatabaseConfigInfo = nmsDatabaseConfigInfoList.get(0);
            nmsDatabaseDetail.setStartTime(nmsDatabaseConfigInfo.getStartTime());
            nmsDatabaseDetail.setTotalSize(nmsDatabaseConfigInfo.getTotalSize());
            nmsDatabaseDetail.setMaxConnNum(nmsDatabaseConfigInfo.getMaxConnNum());
            nmsDatabaseDetail.setMaxMemSize(nmsDatabaseConfigInfo.getMaxMemSize());
        }

        if (nmsDatabaseBasicInfoList != null && nmsDatabaseBasicInfoList.size() > 0) {
            NmsDatabaseBasic nmsDatabaseBasicInfo = nmsDatabaseBasicInfoList.get(0);
            nmsDatabaseDetail.setName(nmsDatabaseBasicInfo.getName());
            nmsDatabaseDetail.setVersion(nmsDatabaseBasicInfo.getVersion());
            nmsDatabaseDetail.setInstallTime(nmsDatabaseBasicInfo.getInstallTime());
            nmsDatabaseDetail.setProcessName(nmsDatabaseBasicInfo.getProcessName());
        }

        StringBuilder builder = new StringBuilder("存在告警：");
        if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
            nmsDatabaseDetail.setStatusCode(0);
            for (int i = 0; i < nmsAlarmList.size(); i++) {
                Object[] objects = (Object[]) nmsAlarmList.get(i);
                Integer status = (Integer) objects[0];
                Integer count = ((BigInteger) objects[1]).intValue();
                if (status == 0) {
                    builder.append("待处理状态" + count + "条");
                }
                if (status == 1) {
                    builder.append("，处理中状态" + count + "条");
                }
            }
            nmsDatabaseDetail.setStatus(builder.toString());
        } else {
            nmsDatabaseDetail.setStatus("正常");
            nmsDatabaseDetail.setStatusCode(1);
        }

        NmsAsset nmsAsset = nmsAssetService.findByIp(nmsSoft.getAIp());

        Integer assetId = -1;

        if (nmsAsset != null) {
            assetId = nmsAsset.getId();
        }

        NmsPingStaticsInfo nmsPingStaticsInfo = pService.pingInfoServiceDetailPerformanceInfoV02(assetId, null, null);

        nmsDatabaseDetail.setNmsPing(nmsPingStaticsInfo);

        return nmsDatabaseDetail;
    }
}
