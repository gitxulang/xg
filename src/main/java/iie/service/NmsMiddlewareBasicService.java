package iie.service;

import iie.pojo.*;
import iie.tools.NmsMiddlewareDetail;
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
 * @date 2020/7/12 - 15:31
 */
@Service("NmsMiddlewareBasicService")
public class NmsMiddlewareBasicService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @Autowired
    NmsPingInfoService pService;

    @Autowired
    NmsAssetService nmsAssetService;

    public PageBean<NmsMiddlewareBasic> getPageByDate(String orderKey, String orderValue, String startDate, String endDate, int begin, int offset, String assetId) throws Exception {
        Session session = hibernateTemplate.getSessionFactory().openSession();

        String sqlCount = "select count(1) from nms_middleware_basic as mb where mb.soft_id ='" + assetId + "'";

        if (startDate != null && endDate != null) {
            sqlCount += " and mb.itime between '" + startDate + "' and '" + endDate + "'";
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

        String sqlList = " select * from nms_middleware_basic as mb where mb.soft_id = '" + assetId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and mb.itime between '" + startDate + "' and '" + endDate + "'";
        }

        sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
        SQLQuery queryList = session.createSQLQuery(sqlList);
        queryList.addEntity("NmsMiddlewareBasic", NmsMiddlewareBasic.class);
        List<NmsMiddlewareBasic> list = queryList.list();
        session.close();

        PageBean<NmsMiddlewareBasic> page = new PageBean<NmsMiddlewareBasic>();
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

    public List<NmsMiddlewareBasic> getPageByDateExportExcel(String orderKey, String orderValue, String startDate, String endDate, String assetId) throws Exception {
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
        String sqlList = "select * from nms_middleware_basic as mb where mb.soft_id = '" + assetId + "'";
        if (startDate != null && endDate != null) {
            sqlList += " and mb.itime between '" + startDate + "' and '" + endDate + "'";
        }
        // 所有原始数据报表最多导出最新的10000条记录
        sqlList += " order by itime desc limit 10000";

        SQLQuery queryList = session.createSQLQuery(sqlList);

        // 序列化数据
        queryList.addEntity("NmsMiddlewareBasic", NmsMiddlewareBasic.class);
        List<NmsMiddlewareBasic> list = queryList.list();
        session.close();

        return list;
    }

    public NmsMiddlewareDetail assetMiddlewareInfoOverview(Integer softId) throws Exception {
        NmsSoft nmsSoft = hibernateTemplate.get(NmsSoft.class, softId);

        if (nmsSoft == null) {
            return new NmsMiddlewareDetail();
        }

        DetachedCriteria nmsMiddlewareBasicInfoCriteria = DetachedCriteria
                .forClass(NmsMiddlewareBasic.class);
        nmsMiddlewareBasicInfoCriteria.add(Restrictions.eq("nmsSoft", nmsSoft));
        nmsMiddlewareBasicInfoCriteria.addOrder(Order.desc("id"));

        List<NmsMiddlewareBasic> nmsMiddlewareBasicInfoList = (List<NmsMiddlewareBasic>) hibernateTemplate
                .findByCriteria(nmsMiddlewareBasicInfoCriteria);

        DetachedCriteria nmsMiddlewareConfigInfoCriteria = DetachedCriteria
                .forClass(NmsMiddlewareConfig.class);
        nmsMiddlewareConfigInfoCriteria.add(Restrictions.eq("nmsSoft", nmsSoft));
        nmsMiddlewareConfigInfoCriteria.addOrder(Order.desc("id"));

        List<NmsMiddlewareConfig> nmsMiddlewareConfigInfoList = (List<NmsMiddlewareConfig>) hibernateTemplate
                .findByCriteria(nmsMiddlewareConfigInfoCriteria);

        DetachedCriteria nmsMiddlewareStatusInfoCriteria = DetachedCriteria
                .forClass(NmsMiddlewareStatus.class);
        nmsMiddlewareStatusInfoCriteria.add(Restrictions.eq("nmsSoft", nmsSoft));
        nmsMiddlewareStatusInfoCriteria.addOrder(Order.desc("id"));

        List<NmsMiddlewareStatus> nmsMiddlewareStatusInfoList = (List<NmsMiddlewareStatus>) hibernateTemplate
                .findByCriteria(nmsMiddlewareStatusInfoCriteria);

        String nmsAlarmSql = "select d_status,count(1) from nms_alarm_soft where d_status in (0,1) and soft_id="
                + softId + " group by d_status";

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query nmsAlarmQuery = session.createSQLQuery(nmsAlarmSql);
        List nmsAlarmList = nmsAlarmQuery.list();
        session.close();

        NmsMiddlewareDetail nmsMiddlewareDetail = new NmsMiddlewareDetail();
        nmsMiddlewareDetail.setSoftId(nmsSoft.getId());
        nmsMiddlewareDetail.setSoftName(nmsSoft.getAName());

        if (nmsMiddlewareStatusInfoList != null && nmsMiddlewareStatusInfoList.size() > 0) {
            NmsMiddlewareStatus nmsMiddlewareStatusInfo = nmsMiddlewareStatusInfoList.get(0);
            nmsMiddlewareDetail.setMemTotal(nmsMiddlewareStatusInfo.getMemTotal());
            nmsMiddlewareDetail.setMemUsed(nmsMiddlewareStatusInfo.getMemUsed());
            nmsMiddlewareDetail.setHeapTotal(nmsMiddlewareStatusInfo.getHeapTotal());
            nmsMiddlewareDetail.setHeapUsed(nmsMiddlewareStatusInfo.getHeapUsed());
            nmsMiddlewareDetail.setNonHeapTotal(nmsMiddlewareStatusInfo.getNonHeapTotal());
            nmsMiddlewareDetail.setNonHeapUsed(nmsMiddlewareStatusInfo.getNonHeapUsed());
            nmsMiddlewareDetail.setConnNum(nmsMiddlewareStatusInfo.getConnNum());
        }

        if (nmsMiddlewareStatusInfoList != null && nmsMiddlewareStatusInfoList.size() >= 2) {
            NmsMiddlewareStatus nmsMiddlewareStatusInfo1 = nmsMiddlewareStatusInfoList.get(0);
            NmsMiddlewareStatus nmsMiddlewareStatusInfo2 = nmsMiddlewareStatusInfoList.get(1);

            Timestamp itime1 = nmsMiddlewareStatusInfo1.getItime();
            Long rps1 = nmsMiddlewareStatusInfo1.getRps();

            Timestamp itime2 = nmsMiddlewareStatusInfo2.getItime();
            Long rps2 = nmsMiddlewareStatusInfo2.getRps();

            if (rps2 == null || rps1 == null || itime1 == null || itime2 == null ||
                    rps1 == rps2 || itime1.equals(itime2) || rps1 < rps2 || itime1.before(itime2)) {
                nmsMiddlewareDetail.setNumPerSecond(Long.valueOf(0));
            } else {
                long rpsDiff = rps1 - rps2;
                long timeDiff = (itime1.getTime() - itime2.getTime()) / 1000;
                nmsMiddlewareDetail.setNumPerSecond(rpsDiff / timeDiff);
            }
        } else {
            nmsMiddlewareDetail.setNumPerSecond(null);
        }

        if (nmsMiddlewareConfigInfoList != null && nmsMiddlewareConfigInfoList.size() > 0) {
            NmsMiddlewareConfig nmsMiddlewareConfigInfo = nmsMiddlewareConfigInfoList.get(0);
            nmsMiddlewareDetail.setStartTime(nmsMiddlewareConfigInfo.getStartTime());
            nmsMiddlewareDetail.setHttpsProtocol(nmsMiddlewareConfigInfo.getHttpsProtocol());
            nmsMiddlewareDetail.setMaxConnNum(nmsMiddlewareConfigInfo.getMaxConnNum());
            nmsMiddlewareDetail.setMaxHreadNum(nmsMiddlewareConfigInfo.getMaxHreadNum());

            DetachedCriteria nmsMiddlewareInstanceInfoCriteria = DetachedCriteria
                    .forClass(NmsMiddlewareInstance.class);
            nmsMiddlewareInstanceInfoCriteria.add(Restrictions.eq("nmsMiddlewareConfig", nmsMiddlewareConfigInfo));
            nmsMiddlewareInstanceInfoCriteria.addOrder(Order.desc("id"));

            List<NmsMiddlewareInstance> nmsMiddlewareInstanceInfoList = (List<NmsMiddlewareInstance>) hibernateTemplate
                    .findByCriteria(nmsMiddlewareInstanceInfoCriteria);

            if (nmsMiddlewareInstanceInfoList != null && nmsMiddlewareInstanceInfoList.size() > 0) {
                nmsMiddlewareDetail.setConfigInstances(nmsMiddlewareInstanceInfoList);
            }
        }

        if (nmsMiddlewareBasicInfoList != null && nmsMiddlewareBasicInfoList.size() > 0) {
            NmsMiddlewareBasic nmsMiddlewareBasicInfo = nmsMiddlewareBasicInfoList.get(0);
            nmsMiddlewareDetail.setName(nmsMiddlewareBasicInfo.getName());
            nmsMiddlewareDetail.setVersion(nmsMiddlewareBasicInfo.getVersion());
            nmsMiddlewareDetail.setJdkVersion(nmsMiddlewareBasicInfo.getJdkVersion());
            nmsMiddlewareDetail.setInstallTime(nmsMiddlewareBasicInfo.getInstallTime());
        }

        StringBuilder builder = new StringBuilder("存在告警：");
        if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
            nmsMiddlewareDetail.setStatusCode(0);
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
            nmsMiddlewareDetail.setStatus(builder.toString());
        } else {
            nmsMiddlewareDetail.setStatus("正常");
            nmsMiddlewareDetail.setStatusCode(1);
        }

        NmsAsset nmsAsset = nmsAssetService.findByIp(nmsSoft.getAIp());

        Integer assetId = -1;

        if (nmsAsset != null) {
            assetId = nmsAsset.getId();
        }

        NmsPingStaticsInfo nmsPingStaticsInfo = pService.pingInfoServiceDetailPerformanceInfoV02(assetId, null, null);

        nmsMiddlewareDetail.setNmsPing(nmsPingStaticsInfo);

        return nmsMiddlewareDetail;
    }
}
