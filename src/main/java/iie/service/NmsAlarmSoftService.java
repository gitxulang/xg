package iie.service;

import iie.pojo.NmsAlarmSoft;
import iie.pojo.NmsAlarmSoftReport;
import iie.tools.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

/**
 * @author xczhao
 * @date 2020/10/17 - 22:38
 */
@Service
public class NmsAlarmSoftService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @SuppressWarnings("rawtypes")
	public PageBean<NmsAlarmSoftStaticsDetail> staticsNmsSoftAlarm(
            String nmsSoftName, String nmsSoftIp, String nmsSoftPort,
            Integer nmsSoftId, String nmsAssetTypeName, Integer nmsAssetTypeId,
            Integer alarmLevel, String alarmStartDate, String alarmEndDate,
            int begin, int offset, String orderKey, String orderValue) {

        String firstSql = "select a.id,a.a_name,"
                + "a.a_ip,a.a_port,b.totalCount,"
                + "b.undealTotalCount,b.dealIngTotalCount,"
                + "b.dealedTotalCount,b.autoTotalCount,b.deletedTotalCount,"
                + "k.undealTotalCountLevelOne,k.dealingTotalCountLevelOne,k.dealedTotalCountLevelOne,"
                + "k.autoTotalCountLevelOne,k.deletedTotalCountLevelOne,"
                + "k.undealTotalCountLevelTwo,k.dealingTotalCountLevelTwo,k.dealedCountLevelTwo,k.autoTotalCountLevelTwo,"
                + "k.deletedCountLevelTwo,"
                + "k.undealTotalCountLevelThree,k.dealingTotalCountLevelThree,k.dealedCountLevelThree,"
                + "k.autoTotalCountLevelThree,k.deletedCountLevelThree,t.ch_type,t.ch_subtype "
                + "from nms_soft a "
                + "left join nms_asset_type t on a.type_id= t.id "
                + "left join ( ";
        String firstSqlCount = "select count(*) " + "from nms_soft a "
                + "left join nms_asset_type t on a.type_id= t.id "
                + "left join ( ";
        String secondSql = "select soft_id,count(id) totalCount," + "sum("
                + "case when d_status = 0 then 1 " + "else 0 end"
                + ")  as undealTotalCount, " + "sum("
                + "case when d_status = 1 then 1 " + "else 0 end"
                + ") as dealIngTotalCount," + "sum("
                + "case when d_status = 2 then 1 " + "else 0 end"
                + ") as dealedTotalCount, " + "sum("
                + "case when d_status = 3 then 1 " + "else 0 end"
                + ") as autoTotalCount, " + "sum("
                + "case when d_status = 4 then 1 " + "else 0 end "
                + ") as deletedTotalCount " + "from nms_alarm_soft " + " where 1=1 ";

        String threeSql = " group by soft_id ) b on a.id = b.soft_id "
                + " left join ( ";
        String fourSql = "select soft_id,a_level," + "sum("
                + "case when d_status = 0  and a_level=1 then 1 "
                + "else 0 end" + ") as undealTotalCountLevelOne," + "sum("
                + "case when d_status = 1 and a_level=1 then 1 " + "else 0 end"
                + ") as dealingTotalCountLevelOne," + "sum("
                + "case when d_status = 2 and a_level=1 then 1 " + "else 0 end"
                + ") as dealedTotalCountLevelOne," + "sum("
                + "case when d_status = 3 and a_level=1 then 1 " + "else 0 end"
                + ") as autoTotalCountLevelOne," + "sum("
                + "case when d_status = 4 and a_level=1 then 1 " + "else 0 end"
                + ") as deletedTotalCountLevelOne," + "sum("
                + "case when d_status = 0 and a_level=2 then 1 " + "else 0 end"
                + ") as undealTotalCountLevelTwo," + "sum("
                + "case when d_status = 1 and a_level=2 then 1 " + "else 0 end"
                + ")  as dealingTotalCountLevelTwo," + "sum("
                + "case when d_status = 2 and a_level=2 then 1 " + "else 0 end"
                + ") as dealedCountLevelTwo," + "sum("
                + "case when d_status = 3 and a_level=2 then 1 " + "else 0 end"
                + ") as autoTotalCountLevelTwo," + "sum("
                + "case when d_status = 4 and a_level=2 then 1 " + "else 0 end"
                + ") as deletedCountLevelTwo," + "sum("
                + "case when d_status = 0 and a_level=3 then 1 " + "else 0 end"
                + ") as undealTotalCountLevelThree," + "sum("
                + "case when d_status = 1 and a_level=3 then 1 " + "else 0 end"
                + ")  as dealingTotalCountLevelThree," + "sum("
                + "case when d_status = 2 and a_level=3 then 1 " + "else 0 end"
                + ") as dealedCountLevelThree," + "sum("
                + "case when d_status = 3 and a_level=3 then 1 " + "else 0 end"
                + ") as autoTotalCountLevelThree," + "sum("
                + "case when d_status = 4 and a_level=3 then 1 " + "else 0 end"
                + ") as deletedCountLevelThree " + " from nms_alarm_soft "
                + " where 1=1 ";
        String endSql = " group by soft_id " + ") k on k.soft_id = a.id "
                + "where 1=1 and a.deled=0 ";

        if (nmsSoftName != null && nmsSoftName.length() > 0) {
            endSql = endSql + "and a.a_name like '%" + nmsSoftName + "%' ";
        }
        if (nmsSoftIp != null && nmsSoftIp.length() > 0) {
            endSql = endSql + "and a.a_ip like'%" + nmsSoftIp + "%' ";
        }
        if (nmsSoftPort != null && nmsSoftPort.length() > 0) {
            endSql = endSql + "and a.a_port like'%" + nmsSoftPort + "%' ";
        }
        if (nmsSoftId != null) {
            endSql = endSql + "and a.id =" + nmsSoftId + " ";
        }
        if (nmsAssetTypeName != null && nmsAssetTypeName.length() > 0) {
            endSql = endSql + " and t.ch_type like '%" + nmsAssetTypeName + "%' ";
        }
        if (nmsAssetTypeId != null) {
            endSql = endSql + " and t.id =" + nmsAssetTypeId + " ";
        }
        if (alarmLevel != null) {
            secondSql = secondSql + "and  a_level = " + alarmLevel + " ";
            fourSql = fourSql + " and a_level = " + alarmLevel + " ";
        }
        if (alarmStartDate != null && alarmStartDate.length() > 0) {
            secondSql = secondSql + " and a_time >= '" + alarmStartDate + "' ";
            fourSql = fourSql + " and a_time >= '" + alarmStartDate + "' ";
        }
        if (alarmEndDate != null && alarmEndDate.length() > 0) {
            secondSql = secondSql + " and a_time <= '" + alarmEndDate + "' ";
            fourSql = fourSql + " and a_time <= '" + alarmEndDate + "' ";
        }
        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == null || orderValue.length() == 0
                || orderValue.equals("1")) {
            orderValue = "1";
            orderKey = orderKey + " desc";
        } else {
            orderKey = orderKey + " asc";
        }

        String endSqlLimit = " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
        String sql = firstSql + secondSql + threeSql + fourSql + endSql + endSqlLimit;
        String sqlcount = firstSqlCount + secondSql + threeSql + fourSql + endSql;

        List list = null;
        Integer count = 0;

        try {
            Session session = hibernateTemplate.getSessionFactory().openSession();
            SQLQuery sqlQuery = session.createSQLQuery(sql);
            list = sqlQuery.list();

            SQLQuery sqlQueryCount = session.createSQLQuery(sqlcount);
            count = Integer.valueOf(sqlQueryCount.list().get(0).toString());
            session.close();
        } catch (Exception e) {
            e.toString();
        }

        List<NmsAlarmSoftStaticsDetail> result = new ArrayList<NmsAlarmSoftStaticsDetail>(0);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                NmsAlarmSoftStaticsDetail nmsAlarmStaticsDetail = new NmsAlarmSoftStaticsDetail();
                NmsAlarmLevelStaticsDetail levelOne = new NmsAlarmLevelStaticsDetail();
                NmsAlarmLevelStaticsDetail levelTwo = new NmsAlarmLevelStaticsDetail();
                NmsAlarmLevelStaticsDetail levelThree = new NmsAlarmLevelStaticsDetail();
                nmsAlarmStaticsDetail.setNmsSoftId((Integer) objects[0]);
                nmsAlarmStaticsDetail.setNmsSoftName((String) objects[1]);
                nmsAlarmStaticsDetail.setNmsSoftIp((String) objects[2]);
                nmsAlarmStaticsDetail.setNmsSoftPort((String) objects[3]);
                if (objects[4] == null) {
                    nmsAlarmStaticsDetail.setAlarmTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setAlarmTotalCount(Long
                            .valueOf(objects[4].toString()));
                }
                if (objects[5] == null) {
                    nmsAlarmStaticsDetail.setUnDealTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setUnDealTotalCount(Long
                            .valueOf(objects[5].toString()));
                }
                if (objects[6] == null) {
                    nmsAlarmStaticsDetail.setDealingTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setDealingTotalCount(Long
                            .valueOf(objects[6].toString()));
                }
                if (objects[7] == null) {
                    nmsAlarmStaticsDetail.setDealedTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setDealedTotalCount(Long
                            .valueOf(objects[7].toString()));
                }
                if (objects[8] == null) {
                    nmsAlarmStaticsDetail.setAutoRecoverTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setAutoRecoverTotalCount(Long
                            .valueOf(objects[8].toString()));
                }
                if (objects[9] == null) {
                    nmsAlarmStaticsDetail.setDeleteTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setDeleteTotalCount(Long
                            .valueOf(objects[9].toString()));
                }

                if (objects[10] == null) {
                    levelOne.setUnDealTotalCount(0L);
                } else {
                    levelOne.setUnDealTotalCount(Long.valueOf(objects[10]
                            .toString()));
                }
                if (objects[11] == null) {
                    levelOne.setDealingTotalCount(0L);
                } else {
                    levelOne.setDealingTotalCount(Long.valueOf(objects[11]
                            .toString()));
                }
                if (objects[12] == null) {
                    levelOne.setDealedTotalCount(0L);
                } else {
                    levelOne.setDealedTotalCount(Long.valueOf(objects[12]
                            .toString()));
                }
                if (objects[13] == null) {
                    levelOne.setAutoRecoverTotalCount(0L);
                } else {
                    levelOne.setAutoRecoverTotalCount(Long.valueOf(objects[13]
                            .toString()));
                }
                if (objects[14] == null) {
                    levelOne.setDeleteTotalCount(0L);
                } else {
                    levelOne.setDeleteTotalCount(Long.valueOf(objects[14]
                            .toString()));
                }

                if (objects[15] == null) {
                    levelTwo.setUnDealTotalCount(0L);
                } else {
                    levelTwo.setUnDealTotalCount(Long.valueOf(objects[15]
                            .toString()));
                }
                if (objects[16] == null) {
                    levelTwo.setDealingTotalCount(0L);
                } else {
                    levelTwo.setDealingTotalCount(Long.valueOf(objects[16]
                            .toString()));
                }
                if (objects[17] == null) {
                    levelTwo.setDealedTotalCount(0L);
                } else {
                    levelTwo.setDealedTotalCount(Long.valueOf(objects[17]
                            .toString()));
                }
                if (objects[18] == null) {
                    levelTwo.setAutoRecoverTotalCount(0L);
                } else {
                    levelTwo.setAutoRecoverTotalCount(Long.valueOf(objects[18]
                            .toString()));
                }
                if (objects[19] == null) {
                    levelTwo.setDeleteTotalCount(0L);
                } else {
                    levelTwo.setDeleteTotalCount(Long.valueOf(objects[19]
                            .toString()));
                }

                if (objects[20] == null) {
                    levelThree.setUnDealTotalCount(0L);
                } else {
                    levelThree.setUnDealTotalCount(Long.valueOf(objects[20]
                            .toString()));
                }
                if (objects[21] == null) {
                    levelThree.setDealingTotalCount(0L);
                } else {
                    levelThree.setDealingTotalCount(Long.valueOf(objects[21]
                            .toString()));
                }
                if (objects[22] == null) {
                    levelThree.setDealedTotalCount(0L);
                } else {
                    levelThree.setDealedTotalCount(Long.valueOf(objects[22]
                            .toString()));
                }
                if (objects[23] == null) {
                    levelThree.setAutoRecoverTotalCount(0L);
                } else {
                    levelThree.setAutoRecoverTotalCount(Long
                            .valueOf(objects[23].toString()));
                }
                if (objects[24] == null) {
                    levelThree.setDeleteTotalCount(0L);
                } else {
                    levelThree.setDeleteTotalCount(Long.valueOf(objects[24]
                            .toString()));
                }

                // 重置告警总数=待处理+处理中+已处理
                nmsAlarmStaticsDetail.setAlarmTotalCount(nmsAlarmStaticsDetail
                        .getUnDealTotalCount()
                        + nmsAlarmStaticsDetail.getDealingTotalCount()
                        + nmsAlarmStaticsDetail.getDealedTotalCount());

                nmsAlarmStaticsDetail.setNmsAssetType((String) objects[25]);
                nmsAlarmStaticsDetail.setNmsAssetSubType((String) objects[26]);
                nmsAlarmStaticsDetail.setLevelOne(levelOne);
                nmsAlarmStaticsDetail.setLevelTwo(levelTwo);
                nmsAlarmStaticsDetail.setLevelThree(levelThree);
                result.add(nmsAlarmStaticsDetail);
            }
        }

        // 创建PageBean对象返回数据
        PageBean<NmsAlarmSoftStaticsDetail> page = new PageBean<NmsAlarmSoftStaticsDetail>();
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
        page.setList(result);
        return page;
    }

    public List<NmsAlarmSoftStaticsDetail> staticsNmsSoftAlarmExportExcel(
            String nmsSoftName, String nmsSoftIp, String nmsSoftPort, Integer nmsSoftId,
            String nmsAssetTypeName, Integer nmsAssetTypeId, Integer alarmLevel,
            String alarmStartDate, String alarmEndDate, String orderKey, Integer orderValue) {

        String firstSql = "select a.id,a.a_name,"
                + "a.a_ip,a.a_port,b.totalCount,"
                + "b.undealTotalCount,b.dealIngTotalCount,"
                + "b.dealedTotalCount,b.autoTotalCount,b.deletedTotalCount,"
                + "k.undealTotalCountLevelOne,k.dealingTotalCountLevelOne,k.dealedTotalCountLevelOne,"
                + "k.autoTotalCountLevelOne,k.deletedTotalCountLevelOne,"
                + "k.undealTotalCountLevelTwo,k.dealingTotalCountLevelTwo,k.dealedCountLevelTwo,k.autoTotalCountLevelTwo,"
                + "k.deletedCountLevelTwo,"
                + "k.undealTotalCountLevelThree,k.dealingTotalCountLevelThree,k.dealedCountLevelThree,"
                + "k.autoTotalCountLevelThree,k.deletedCountLevelThree,t.ch_type,t.ch_subtype "
                + "from nms_soft a "
                + "left join nms_asset_type t on a.type_id= t.id "
                + "left join ( ";
        String firstSqlCount = "select count(*) " + "from nms_soft a "
                + "left join nms_asset_type t on a.type_id= t.id "
                + "left join ( ";
        String secondSql = "select soft_id,count(id) totalCount," + "sum("
                + "case when d_status = 0 then 1 " + "else 0 end"
                + ")  as undealTotalCount, " + "sum("
                + "case when d_status = 1 then 1 " + "else 0 end"
                + ") as dealIngTotalCount," + "sum("
                + "case when d_status = 2 then 1 " + "else 0 end"
                + ") as dealedTotalCount, " + "sum("
                + "case when d_status = 3 then 1 " + "else 0 end"
                + ") as autoTotalCount, " + "sum("
                + "case when d_status = 4 then 1 " + "else 0 end "
                + ") as deletedTotalCount " + "from nms_alarm_soft " + " where 1=1 ";

        String threeSql = " group by soft_id ) b on a.id = b.soft_id "
                + " left join ( ";
        String fourSql = "select soft_id,a_level," + "sum("
                + "case when d_status = 0  and a_level=1 then 1 "
                + "else 0 end" + ") as undealTotalCountLevelOne," + "sum("
                + "case when d_status = 1 and a_level=1 then 1 " + "else 0 end"
                + ") as dealingTotalCountLevelOne," + "sum("
                + "case when d_status = 2 and a_level=1 then 1 " + "else 0 end"
                + ") as dealedTotalCountLevelOne," + "sum("
                + "case when d_status = 3 and a_level=1 then 1 " + "else 0 end"
                + ") as autoTotalCountLevelOne," + "sum("
                + "case when d_status = 4 and a_level=1 then 1 " + "else 0 end"
                + ") as deletedTotalCountLevelOne," + "sum("
                + "case when d_status = 0 and a_level=2 then 1 " + "else 0 end"
                + ") as undealTotalCountLevelTwo," + "sum("
                + "case when d_status = 1 and a_level=2 then 1 " + "else 0 end"
                + ")  as dealingTotalCountLevelTwo," + "sum("
                + "case when d_status = 2 and a_level=2 then 1 " + "else 0 end"
                + ") as dealedCountLevelTwo," + "sum("
                + "case when d_status = 3 and a_level=2 then 1 " + "else 0 end"
                + ") as autoTotalCountLevelTwo," + "sum("
                + "case when d_status = 4 and a_level=2 then 1 " + "else 0 end"
                + ") as deletedCountLevelTwo," + "sum("
                + "case when d_status = 0 and a_level=3 then 1 " + "else 0 end"
                + ") as undealTotalCountLevelThree," + "sum("
                + "case when d_status = 1 and a_level=3 then 1 " + "else 0 end"
                + ")  as dealingTotalCountLevelThree," + "sum("
                + "case when d_status = 2 and a_level=3 then 1 " + "else 0 end"
                + ") as dealedCountLevelThree," + "sum("
                + "case when d_status = 3 and a_level=3 then 1 " + "else 0 end"
                + ") as autoTotalCountLevelThree," + "sum("
                + "case when d_status = 4 and a_level=3 then 1 " + "else 0 end"
                + ") as deletedCountLevelThree " + " from nms_alarm_soft "
                + " where 1=1 ";
        String endSql = " group by soft_id " + ") k on k.soft_id = a.id "
                + "where 1=1 and a.deled = 0 ";
        if (nmsSoftName != null && nmsSoftName.length() > 0) {
            endSql = endSql + "and a.a_name like '%" + nmsSoftName + "%' ";
        }
        if (nmsSoftIp != null && nmsSoftIp.length() > 0) {
            endSql = endSql + "and a.a_ip like'%" + nmsSoftIp + "%' ";
        }
        if (nmsSoftPort != null && nmsSoftPort.length() > 0) {
            endSql = endSql + "and a.a_port like'%" + nmsSoftPort + "%' ";
        }
        if (nmsSoftId != null) {
            endSql = endSql + "and a.id =" + nmsSoftId + " ";
        }
        if (nmsAssetTypeName != null && nmsAssetTypeName.length() > 0) {
            endSql = endSql + " and t.ch_type like '%" + nmsAssetTypeName + "%' ";
        }
        if (nmsAssetTypeId != null) {
            endSql = endSql + " and t.id =" + nmsAssetTypeId + " ";
        }
        if (alarmLevel != null) {
            secondSql = secondSql + "and a_level = " + alarmLevel + " ";
            fourSql = fourSql + " and a_level = " + alarmLevel + " ";
        }
        if (alarmStartDate != null && alarmStartDate.length() > 0) {
            secondSql = secondSql + " and a_time >= '" + alarmStartDate + "' ";
            fourSql = fourSql + " and a_time >= '" + alarmStartDate + "' ";
        }
        if (alarmEndDate != null && alarmEndDate.length() > 0) {
            secondSql = secondSql + " and a_time <= '" + alarmEndDate + "' ";
            fourSql = fourSql + " and a_time <= '" + alarmEndDate + "' ";
        }

        String sql = firstSql + secondSql + threeSql + fourSql + endSql;
        String sqlcount = firstSqlCount + secondSql + threeSql + fourSql + endSql;

        if (orderKey == null || orderKey.equals("")) {
            orderKey = "id";
            orderValue = 0;
        }

        if (orderValue == 0) {
            sql += " order by " + orderKey + " desc";
        } else {
            sql += " order by " + orderKey + " asc";
        }

        Session session = hibernateTemplate.getSessionFactory().openSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        List list = sqlQuery.list();
        SQLQuery sqlQueryCount = session.createSQLQuery(sqlcount);
        Integer count = Integer.valueOf(sqlQueryCount.list().get(0).toString());
        session.close();

        List<NmsAlarmSoftStaticsDetail> result = new ArrayList<NmsAlarmSoftStaticsDetail>(0);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                NmsAlarmSoftStaticsDetail nmsAlarmStaticsDetail = new NmsAlarmSoftStaticsDetail();
                NmsAlarmLevelStaticsDetail levelOne = new NmsAlarmLevelStaticsDetail();
                NmsAlarmLevelStaticsDetail levelTwo = new NmsAlarmLevelStaticsDetail();
                NmsAlarmLevelStaticsDetail levelThree = new NmsAlarmLevelStaticsDetail();
                nmsAlarmStaticsDetail.setNmsSoftId((Integer) objects[0]);
                nmsAlarmStaticsDetail.setNmsSoftName((String) objects[1]);
                nmsAlarmStaticsDetail.setNmsSoftIp((String) objects[2]);
                nmsAlarmStaticsDetail.setNmsSoftPort((String) objects[3]);
                if (objects[4] == null) {
                    nmsAlarmStaticsDetail.setAlarmTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setAlarmTotalCount(Long
                            .valueOf(objects[4].toString()));
                }
                if (objects[5] == null) {
                    nmsAlarmStaticsDetail.setUnDealTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setUnDealTotalCount(Long
                            .valueOf(objects[5].toString()));
                }
                if (objects[6] == null) {
                    nmsAlarmStaticsDetail.setDealingTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setDealingTotalCount(Long
                            .valueOf(objects[6].toString()));
                }
                if (objects[7] == null) {
                    nmsAlarmStaticsDetail.setDealedTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setDealedTotalCount(Long
                            .valueOf(objects[7].toString()));
                }
                if (objects[8] == null) {
                    nmsAlarmStaticsDetail.setAutoRecoverTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setAutoRecoverTotalCount(Long
                            .valueOf(objects[8].toString()));
                }
                if (objects[9] == null) {
                    nmsAlarmStaticsDetail.setDeleteTotalCount(0L);
                } else {
                    nmsAlarmStaticsDetail.setDeleteTotalCount(Long
                            .valueOf(objects[9].toString()));
                }

                if (objects[10] == null) {
                    levelOne.setUnDealTotalCount(0L);
                } else {
                    levelOne.setUnDealTotalCount(Long.valueOf(objects[10]
                            .toString()));
                }
                if (objects[11] == null) {
                    levelOne.setDealingTotalCount(0L);
                } else {
                    levelOne.setDealingTotalCount(Long.valueOf(objects[11]
                            .toString()));
                }
                if (objects[12] == null) {
                    levelOne.setDealedTotalCount(0L);
                } else {
                    levelOne.setDealedTotalCount(Long.valueOf(objects[12]
                            .toString()));
                }
                if (objects[13] == null) {
                    levelOne.setAutoRecoverTotalCount(0L);
                } else {
                    levelOne.setAutoRecoverTotalCount(Long.valueOf(objects[13]
                            .toString()));
                }
                if (objects[14] == null) {
                    levelOne.setDeleteTotalCount(0L);
                } else {
                    levelOne.setDeleteTotalCount(Long.valueOf(objects[14]
                            .toString()));
                }

                if (objects[15] == null) {
                    levelTwo.setUnDealTotalCount(0L);
                } else {
                    levelTwo.setUnDealTotalCount(Long.valueOf(objects[15]
                            .toString()));
                }
                if (objects[16] == null) {
                    levelTwo.setDealingTotalCount(0L);
                } else {
                    levelTwo.setDealingTotalCount(Long.valueOf(objects[16]
                            .toString()));
                }
                if (objects[17] == null) {
                    levelTwo.setDealedTotalCount(0L);
                } else {
                    levelTwo.setDealedTotalCount(Long.valueOf(objects[17]
                            .toString()));
                }
                if (objects[18] == null) {
                    levelTwo.setAutoRecoverTotalCount(0L);
                } else {
                    levelTwo.setAutoRecoverTotalCount(Long.valueOf(objects[18]
                            .toString()));
                }
                if (objects[19] == null) {
                    levelTwo.setDeleteTotalCount(0L);
                } else {
                    levelTwo.setDeleteTotalCount(Long.valueOf(objects[19]
                            .toString()));
                }

                if (objects[20] == null) {
                    levelThree.setUnDealTotalCount(0L);
                } else {
                    levelThree.setUnDealTotalCount(Long.valueOf(objects[20]
                            .toString()));
                }
                if (objects[21] == null) {
                    levelThree.setDealingTotalCount(0L);
                } else {
                    levelThree.setDealingTotalCount(Long.valueOf(objects[21]
                            .toString()));
                }
                if (objects[22] == null) {
                    levelThree.setDealedTotalCount(0L);
                } else {
                    levelThree.setDealedTotalCount(Long.valueOf(objects[22]
                            .toString()));
                }
                if (objects[23] == null) {
                    levelThree.setAutoRecoverTotalCount(0L);
                } else {
                    levelThree.setAutoRecoverTotalCount(Long
                            .valueOf(objects[23].toString()));
                }
                if (objects[24] == null) {
                    levelThree.setDeleteTotalCount(0L);
                } else {
                    levelThree.setDeleteTotalCount(Long.valueOf(objects[24]
                            .toString()));
                }

                // 重置告警总数=待处理+处理中+已处理
                nmsAlarmStaticsDetail.setAlarmTotalCount(nmsAlarmStaticsDetail
                        .getUnDealTotalCount()
                        + nmsAlarmStaticsDetail.getDealingTotalCount()
                        + nmsAlarmStaticsDetail.getDealedTotalCount());

                nmsAlarmStaticsDetail.setNmsAssetType((String) objects[25] + "/" + (String) objects[26]);
                nmsAlarmStaticsDetail.setNmsAssetSubType((String) objects[26]);
                nmsAlarmStaticsDetail.setLevelOne(levelOne);
                nmsAlarmStaticsDetail.setLevelTwo(levelTwo);
                nmsAlarmStaticsDetail.setLevelThree(levelThree);
                result.add(nmsAlarmStaticsDetail);
            }
        }

        return result;
    }

    public List<NmsAlarmSoftInfoDetail> listAlarmPageByConditionExportExcel(
            String orderKey, int orderValue, Integer ALevel, Integer nmsSoftId,
            String AIp, String APort, Integer DStatus, String AContent,
            String AName, String startDate, String endDate) {

        DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarmSoft.class);
        criteria.add(Restrictions.lt("DStatus", 3));

        if (ALevel != null) {
            criteria.add(Restrictions.eq("ALevel", ALevel));
        }
        criteria.createAlias("nmsSoft", "nmsSoft");
        if (nmsSoftId != null) {
            criteria.add(Restrictions.eq("nmsSoft.id", nmsSoftId));
        }
        if (AIp != null && AIp.length() > 0) {
            criteria.add(Restrictions.eq("nmsSoft.AIp", AIp));
        }
        if (APort != null && APort.length() > 0) {
            criteria.add(Restrictions.eq("nmsSoft.APort", APort));
        }
        if (AContent != null && AContent.length() > 0) {
            criteria.add(Restrictions.like("AContent", "%" + AContent + "%"));
        }
        if (AName != null && AName.length() > 0) {
            criteria.add(Restrictions.like("AName", "%" + AName + "%"));
        }
        if (DStatus != null) {
            criteria.add(Restrictions.gt("DStatus", DStatus));
        }
        if (startDate != null && startDate.length() > 0) {
            criteria.add(Restrictions.gt("ATime", startDate));
        }
        if (endDate != null && endDate.length() > 0) {
            criteria.add(Restrictions.lt("ATime", endDate));
        }
        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "ATime";
        }
        if (orderValue == 0) {
            criteria.addOrder(Order.desc(orderKey));
        } else {
            criteria.addOrder(Order.asc(orderKey));
        }
        criteria.addOrder(Order.desc("DStatus"));

        criteria.setProjection(Projections.rowCount());
        //Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria).get(0);

        criteria.setProjection(null);
        criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
        List<NmsAlarmSoft> nmsAlarmList = (List<NmsAlarmSoft>) hibernateTemplate.findByCriteria(criteria);

        List<NmsAlarmSoftInfoDetail> nmsAlarmInfoDetailList = new ArrayList<NmsAlarmSoftInfoDetail>(0);
        if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
            for (int i = 0; i < nmsAlarmList.size(); i++) {
                NmsAlarmSoft nmsAlarm = nmsAlarmList.get(i);
                int dStatus = nmsAlarm.getDStatus();
                Integer alarmId = nmsAlarm.getId();
                NmsAlarmSoftInfoDetail nmsAlarmInfoDetail = new NmsAlarmSoftInfoDetail();
                nmsAlarmInfoDetail.setNmsAlarmSoft(nmsAlarm);
                nmsAlarmInfoDetailList.add(nmsAlarmInfoDetail);
            }
        }
        return nmsAlarmInfoDetailList;
    }

    public List<NmsAlarmDetail> listPageByConditionExportExcel(
            String orderKey, int orderValue, String AIp, String APort,
            String AName, Integer ALevel, Integer DStatus, String AContent,
            String startDate, String endDate) {

        DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarmSoft.class);
        if (ALevel != null) {
            criteria.add(Restrictions.eq("ALevel", ALevel));
        }

        criteria.add(Restrictions.le("DStatus", 3));

        criteria.createAlias("nmsSoft", "nmsSoft");
        if (AIp != null && AIp.length() > 0) {
            criteria.add(Restrictions.like("nmsSoft.AIp", "%" + AIp + "%"));
        }

        if (APort != null && AIp.length() > 0) {
            criteria.add(Restrictions.like("nmsSoft.APort", "%" + APort + "%"));
        }

        if (AName != null && AName.length() > 0) {
            criteria.add(Restrictions.like("nmsSoft.AName", "%" + AName + "%"));
        }

        if (AContent != null && AContent.length() > 0) {
            criteria.add(Restrictions.like("AContent", "%" + AContent + "%"));
        }

        if (DStatus != null) {
            criteria.add(Restrictions.eq("DStatus", DStatus));
        }

        if (startDate != null && startDate.length() > 0) {
            criteria.add(Restrictions.gt("ATime", startDate));
        }

        if (endDate != null && endDate.length() > 0) {
            criteria.add(Restrictions.lt("ATime", endDate));
        }

        if (orderKey == null || orderKey.equals("")) {
            orderKey = "ATime";
            orderValue = 0;
        }

        if (orderValue == 0) {
            criteria.addOrder(Order.desc(orderKey));
        } else {
            criteria.addOrder(Order.asc(orderKey));
        }

        criteria.setProjection(Projections.rowCount());
        Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria).get(0);

        criteria.setProjection(null);
        criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);

        // 告警报表最多导出10000条记录
        List<NmsAlarmSoft> nmsAlarmList = (List<NmsAlarmSoft>) hibernateTemplate.findByCriteria(criteria, 0, 10000);

        List<NmsAlarmDetail> nmsAlarmDetailList = new ArrayList<NmsAlarmDetail>(0);
        if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
            for (int i = 0; i < nmsAlarmList.size(); i++) {
                NmsAlarmSoft nmsAlarm = nmsAlarmList.get(i);
                NmsAlarmDetail nmsAlarmDetail = new NmsAlarmDetail();

                int level = nmsAlarm.getALevel();
                int dStatus = nmsAlarm.getDStatus();

                if (level == 1) {
                    nmsAlarmDetail.setLevel("一级告警");
                } else if (level == 2) {
                    nmsAlarmDetail.setLevel("二级告警");
                } else if (level == 3) {
                    nmsAlarmDetail.setLevel("三级告警");
                } else {
                    nmsAlarmDetail.setLevel("其它");
                }

                if (dStatus == 0) {
                    nmsAlarmDetail.setStatus("待处理");
                } else if (dStatus == 1) {
                    nmsAlarmDetail.setStatus("处理中");
                } else if (dStatus == 2) {
                    nmsAlarmDetail.setStatus("已处理");
                } else {
                    nmsAlarmDetail.setStatus("其它");
                }

                if (nmsAlarm.getSTime() != null) {
					nmsAlarmDetail.setStime(nmsAlarm.getSTime());
				} else {
					nmsAlarmDetail.setStime("--");
				}
				
				if (nmsAlarm.getATime() != null) {
					nmsAlarmDetail.setAtime(nmsAlarm.getATime());
				} else {
					nmsAlarmDetail.setAtime("--");
				}
				
				if (nmsAlarm.getACount() != null) {
					nmsAlarmDetail.setAcount(String.valueOf(nmsAlarm.getACount()));
				} else {
					nmsAlarmDetail.setAcount("--");
				}

                if (nmsAlarm.getDTime() != null) {
                    nmsAlarmDetail.setDtime(nmsAlarm.getDTime());
                } else {
                    nmsAlarmDetail.setDtime("--");
                }

                if (nmsAlarm.getNmsSoft().getAIp() != null) {
                    nmsAlarmDetail.setIp(nmsAlarm.getNmsSoft().getAIp());
                } else {
                    nmsAlarmDetail.setIp("--");
                }
                
                if (nmsAlarm.getNmsSoft().getAPort() != null) {
                    nmsAlarmDetail.setPort(nmsAlarm.getNmsSoft().getAPort());
                } else {
                    nmsAlarmDetail.setPort("--");
                }
                
                if (nmsAlarm.getNmsSoft().getAName() != null) {
                    nmsAlarmDetail.setName(nmsAlarm.getNmsSoft().getAName());
                } else {
                    nmsAlarmDetail.setName("--");
                }

                if (nmsAlarm.getAContent() != null) {
                    nmsAlarmDetail.setContent(nmsAlarm.getAContent());
                } else {
                    nmsAlarmDetail.setContent("--");
                }

                nmsAlarmDetailList.add(nmsAlarmDetail);
            }
        }
        return nmsAlarmDetailList;
    }

    @SuppressWarnings("unchecked")
    public PageBean<NmsAlarmSoftInfoDetail> listAlarmPageByCondition(
            String orderKey, int orderValue, Integer ALevel,
            Integer nmsSoftId, String AIp, String APort, Integer DStatus, Short MStatus, String AContent,
            String AName, String startDate, String endDate, int begin,
            int offset) {

        DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarmSoft.class);
        criteria.add(Restrictions.ne("DStatus", 4));

        if (ALevel != null) {
            criteria.add(Restrictions.eq("ALevel", ALevel));
        }
        criteria.createAlias("nmsSoft", "nmsSoft");
        if (nmsSoftId != null) {
            criteria.add(Restrictions.eq("nmsSoft.id", nmsSoftId));
        }
        if (AIp != null && AIp.length() > 0) {
            criteria.add(Restrictions.like("nmsSoft.AIp", "%" + AIp + "%"));
        }
        if (APort != null && APort.length() > 0) {
            criteria.add(Restrictions.like("nmsSoft.APort", "%" + APort + "%"));
        }
        if (MStatus != null) {
            criteria.add(Restrictions.eq("nmsSoft.colled", MStatus));
        }
        if (AContent != null && AContent.length() > 0) {
            criteria.add(Restrictions.like("AContent", "%" + AContent + "%"));
        }
        if (AName != null && AName.length() > 0) {
            criteria.add(Restrictions.like("nmsSoft.AName", "%" + AName + "%"));
        }
        if (DStatus != null) {
            criteria.add(Restrictions.eq("DStatus", DStatus));
        }
        if (startDate != null && startDate.length() > 0) {
            criteria.add(Restrictions.gt("ATime", startDate));
        }
        if (endDate != null && endDate.length() > 0) {
            criteria.add(Restrictions.lt("ATime", endDate));
        }

        if (orderKey == null || orderKey.equals("")) {
            criteria.addOrder(Order.desc("itime"));
        } else {
            if (orderValue == 0) {
                criteria.addOrder(Order.desc(orderKey));
            } else {
                criteria.addOrder(Order.asc(orderKey));
            }
        }

        criteria.setProjection(Projections.rowCount());
        Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria).get(0);

        criteria.setProjection(null);
        criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
        List<NmsAlarmSoft> nmsAlarmList = (List<NmsAlarmSoft>) hibernateTemplate
                .findByCriteria(criteria, (begin - 1) * offset, offset);

        List<NmsAlarmSoftInfoDetail> nmsAlarmInfoDetailList = new ArrayList<NmsAlarmSoftInfoDetail>(0);
        if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
            for (int i = 0; i < nmsAlarmList.size(); i++) {
                NmsAlarmSoft nmsAlarm = nmsAlarmList.get(i);
                int dStatus = nmsAlarm.getDStatus();
                Integer alarmId = nmsAlarm.getId();

                NmsAlarmSoftInfoDetail nmsAlarmInfoDetail = new NmsAlarmSoftInfoDetail();
                nmsAlarmInfoDetail.setNmsAlarmSoft(nmsAlarm);

                String action;
                if (dStatus == 0) {
                    action = "接受处理";
                } else if (dStatus == 1) {
                    action = "填写报告";
                } else if (dStatus == 2) {
                    action = "查看报告";
                } else if (dStatus == 3) {
                    action = "无需处理";
                } else if (dStatus == 4) {
                    action = "无需处理";
                } else {
                    if (alarmReportExists(nmsAlarm)) {
                        action = "查看报告";
                    } else {
                        action = "填写报告";
                    }
                }
                nmsAlarmInfoDetail.setAction(action);
                nmsAlarmInfoDetailList.add(nmsAlarmInfoDetail);
            }
        }

        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount.intValue() / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount.intValue() / offset;
        } else {
            totalPage = totalCount.intValue() / offset + 1;
        }

        PageBean<NmsAlarmSoftInfoDetail> page = new PageBean<NmsAlarmSoftInfoDetail>();

        page.setOrderKey(orderKey);
        page.setOrderValue(orderValue);
        page.setTotalCount(totalCount.intValue());
        page.setPage(begin);
        page.setTotalPage(totalPage);
        page.setList(nmsAlarmInfoDetailList);
        return page;
    }

    public boolean deleteAlarm(List<String> list) {
        boolean res = true;
        String sql = "UPDATE nms_alarm_soft SET d_status = 4 WHERE id = " + list.get(0);
        if (list != null && list.size() > 0) {
            for (int i = 1; i < list.size(); i++) {
                sql += " OR id = " + list.get(i);
            }
        }

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction tran = session.beginTransaction();
        try {
            int num = session.createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            res = false;
        }
        tran.commit();
        session.close();
        return res;
    }

    private boolean alarmReportExists(NmsAlarmSoft nmsAlarm) {
        String sql = "from NmsAlarmSoftReport where nmsAlarmSoft = ?";
        List<NmsAlarmSoftReport> list = (List<NmsAlarmSoftReport>) hibernateTemplate
                .find(sql, new Object[]{nmsAlarm});
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public NmsAlarmSoftReport findByAlarmId(Integer alarmId) {
        DetachedCriteria detachedCriteria = DetachedCriteria
                .forClass(NmsAlarmSoftReport.class);
        if (alarmId != null) {
            detachedCriteria.createAlias("nmsAlarmSoft", "nmsAlarmSoft");
            detachedCriteria.add(Restrictions.eq("nmsAlarmSoft.id", alarmId));
        }
        detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);

        List<NmsAlarmSoftReport> nmsAlarmReports = new ArrayList<NmsAlarmSoftReport>();

        nmsAlarmReports = (List<NmsAlarmSoftReport>) hibernateTemplate
                .findByCriteria(detachedCriteria, 0, 1);

        if (nmsAlarmReports.size() > 0) {
            return nmsAlarmReports.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public NmsAlarmSoft findById(int id) {
        String hsql = "from NmsAlarmSoft ns where ns.id = ?";
        List<NmsAlarmSoft> list = (List<NmsAlarmSoft>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsAlarmSoft();
    }

    public boolean addAlarmReportAndUpdateAlarm(Integer id, String rPeople, String rContent, String dTime) {
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarmSoft.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("DStatus", 1));
        criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
        List<NmsAlarmSoft> nmsAlarmList = (List<NmsAlarmSoft>) hibernateTemplate.findByCriteria(criteria);
        if (nmsAlarmList == null || nmsAlarmList.size() == 0) {
            return false;
        }
        try {
            for (NmsAlarmSoft nmsAlarm : nmsAlarmList) {
                nmsAlarm.setId(id);
                nmsAlarm.setDStatus(2);
                nmsAlarm.setDPeople(rPeople);
                nmsAlarm.setDTime(dTime);
                hibernateTemplate.update(nmsAlarm);
                NmsAlarmSoftReport nmsAlarmReport = new NmsAlarmSoftReport();
                nmsAlarmReport.setNmsAlarmSoft(nmsAlarm);
                nmsAlarmReport.setDTime(dTime);
                nmsAlarmReport.setRContent(rContent);
                nmsAlarmReport.setRPeople(rPeople);
                nmsAlarmReport.setItime(nmsAlarm.getItime());
                nmsAlarmReport.setRTime(new Timestamp(System.currentTimeMillis()));
                hibernateTemplate.save(nmsAlarmReport);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAlarmDeal(Integer id, String dPeople) {
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarmSoft.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("DStatus", 0));
        criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
        List<NmsAlarmSoft> nmsAlarmList = (List<NmsAlarmSoft>) hibernateTemplate.findByCriteria(criteria);
        if (nmsAlarmList == null || nmsAlarmList.size() == 0) {
            return false;
        }
        try {
            for (NmsAlarmSoft nmsAlarm : nmsAlarmList) {
                nmsAlarm.setDPeople(dPeople);
                nmsAlarm.setDStatus(1);
                nmsAlarm.setDTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                hibernateTemplate.update(nmsAlarm);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
