package iie.service;

import iie.pojo.*;
import iie.tools.*;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NmsPerformanceService {

    @Autowired
    NmsAssetService nmsAssetService;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    // 使用反射机制获取最后一条记录的时间
    public Calendar getDateOfTheLastRecord(Class<?> clazz, NmsAsset nmsAsset) {
        Calendar calendar = Calendar.getInstance();
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        criteria.add(Restrictions.eq("nmsAsset", nmsAsset));
        criteria.addOrder(Order.desc("id"));
        List<?> list = hibernateTemplate.findByCriteria(criteria, 0, 1);
        Date dateOfTheLastRecord = new Date();
        Method method;
        try {
            method = clazz.getDeclaredMethod("getItime");
            method.setAccessible(true);
            if (list != null && list.size() > 0) {
                Timestamp timestampOfTheLastRecord = (Timestamp) method
                        .invoke(list.get(0));
                dateOfTheLastRecord = new Date(
                        timestampOfTheLastRecord.getTime());
            }
            calendar.setTime(dateOfTheLastRecord);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return calendar;
    }

    public NmsCpuInfoDetail listCpuInfoByCondition(String AIp, Integer id) {

        NmsCpuInfoDetail nmsCpuInfoDetail = new NmsCpuInfoDetail();

        DetachedCriteria detachedCriteria = DetachedCriteria
                .forClass(NmsStaticInfo.class);

        detachedCriteria.createAlias("nmsAsset", "nmsAsset");
        if (AIp != null && AIp.length() > 0) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.AIp", AIp));
        }
        if (id != null) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.id", id));
        }
        detachedCriteria.addOrder(Order.desc("itime"));
        List<NmsStaticInfo> nmsStaticInfoList = (List<NmsStaticInfo>) hibernateTemplate
                .findByCriteria(detachedCriteria, 0, 1);
        if (nmsStaticInfoList == null || nmsStaticInfoList.size() <= 0) {
            return nmsCpuInfoDetail;
        }

        NmsStaticInfo nmsStaticInfo = nmsStaticInfoList.get(0);
        String hql = "from NmsCpuInfo a where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM NmsCpuInfo) as b where nmsAsset = "
                + nmsStaticInfo.getNmsAsset()
                + ")"
                + "and  a.nmsAsset=? order by a.id desc";

        List<NmsCpuInfo> cpuInfoList = (List<NmsCpuInfo>) hibernateTemplate
                .find(hql, new Object[]{nmsStaticInfo.getNmsAsset()});

        String avgHql = "SELECT AVG(cpuRate) from NmsCpuInfo a where "
                + "a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM NmsCpuInfo) as b where nmsAsset = "
                + nmsStaticInfo.getNmsAsset() + ")" + "and  a.nmsAsset=?";
        List<Double> avgList = (List<Double>) hibernateTemplate.find(avgHql,
                new Object[]{nmsStaticInfo.getNmsAsset()});

        nmsCpuInfoDetail.setNumTotal(nmsStaticInfo.getCpuNum());
        nmsCpuInfoDetail.setAvg(avgList.get(0));
        nmsCpuInfoDetail.setCpuInfoList(cpuInfoList);

        return nmsCpuInfoDetail;
    }

    public NmsMemInfo listMemInfoByCondition(String AIp, Integer id) {

        NmsMemInfo nmsMemInfo = new NmsMemInfo();
        DetachedCriteria detachedCriteria = DetachedCriteria
                .forClass(NmsMemInfo.class);

        detachedCriteria.createAlias("nmsAsset", "nmsAsset");
        if (AIp != null && AIp.length() > 0) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.AIp", AIp));
        }
        if (id != null) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.id", id));
        }
        detachedCriteria.addOrder(Order.desc("itime"));
        List<NmsMemInfo> nmsMemInfoList = (List<NmsMemInfo>) hibernateTemplate
                .findByCriteria(detachedCriteria, 0, 1);
        if (nmsMemInfoList == null || nmsMemInfoList.size() <= 0) {
            return nmsMemInfo;
        }
        return nmsMemInfoList.get(0);
    }

    public NmsNetInfoDetail listNetInfoByCondition(String AIp, Integer id) {
        NmsNetInfoDetail nmsNetInfoDetail = new NmsNetInfoDetail();

        DetachedCriteria detachedCriteria = DetachedCriteria
                .forClass(NmsStaticInfo.class);

        detachedCriteria.createAlias("nmsAsset", "nmsAsset");
        if (AIp != null && AIp.length() > 0) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.AIp", AIp));
        }
        if (id != null) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.id", id));
        }
        detachedCriteria.addOrder(Order.desc("itime"));
        List<NmsStaticInfo> nmsStaticInfoList = (List<NmsStaticInfo>) hibernateTemplate
                .findByCriteria(detachedCriteria, 0, 1);
        if (nmsStaticInfoList == null || nmsStaticInfoList.size() <= 0) {
            return nmsNetInfoDetail;
        }

        NmsStaticInfo nmsStaticInfo = nmsStaticInfoList.get(0);
        String hql = "from NmsNetifInfo a where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM NmsNetifInfo) as b where nmsAsset = "
                + nmsStaticInfo.getNmsAsset()
                + ")"
                + "and  a.nmsAsset=? order by a.id desc";
        List<NmsNetifInfo> nmsNetifInfoList = (List<NmsNetifInfo>) hibernateTemplate
                .find(hql, new Object[]{nmsStaticInfo.getNmsAsset()});

        nmsNetInfoDetail.setNetNum(nmsStaticInfo.getNetNum());
        nmsNetInfoDetail.setNmsNetifInfoList(nmsNetifInfoList);

        return nmsNetInfoDetail;
    }

    public NmsPingInfo listPingInfoByCondition(String AIp, Integer id) {

        NmsPingInfo nmsPingInfo = new NmsPingInfo();
        DetachedCriteria detachedCriteria = DetachedCriteria
                .forClass(NmsPingInfo.class);

        detachedCriteria.createAlias("nmsAsset", "nmsAsset");
        if (AIp != null && AIp.length() > 0) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.AIp", AIp));
        }
        if (id != null) {
            detachedCriteria.add(Restrictions.eq("nmsAsset.id", id));
        }
        detachedCriteria.addOrder(Order.desc("id"));
        List<NmsPingInfo> nmsPingInfoList = (List<NmsPingInfo>) hibernateTemplate
                .findByCriteria(detachedCriteria, 0, 1);
        if (nmsPingInfoList == null || nmsPingInfoList.size() <= 0) {
            return nmsPingInfo;
        }
        return nmsPingInfoList.get(0);
    }

    // 这个方法被下面的方法替换，没有调用
    @Deprecated
    public PageBean<NmsPerformanceRecord> listNmsPerformanceByCondition(
            String orderKey, int orderValue, String AName, String AIp,
            String APos, String nmsAssetType, int begin, int offset) {
        String countSql = "select count(1) "
                + "from nms_asset a "
                + "left join (select * from nms_static_info order by itime desc limit 1 ) s on s.asset_id = a.id "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "left join (select a.asset_id,avg(a.cpu_rate) from nms_cpu_info a "
                + "where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b "
                + "where a.asset_id = asset_id) "
                + "group by a.asset_id) c on s.asset_id = c.asset_id "
                + "left join (select * from nms_ping_info order by id desc limit 1) p on s.asset_id = p.asset_id "
                + "left join (select * from nms_mem_info order by itime desc limit 1) m on s.asset_id = m.asset_id where 1=1 ";
        String querySql = "select a.id,a.a_name,"
                + "a.a_ip,"
                + "d.d_name,"
                + "at.ch_type,"
                + "at.ch_subtype,"
                + "cpu.cpu_rate,"
                + "ping.ping_rate,"
                + "mem.mem_free, "
                + "mem.mem_total, "
                + "mem.swap_free, "
                + "mem.swap_total, "
                + "s.net_num "
                + "from nms_asset a "
                + "left join (select * from nms_static_info order by itime desc limit 1) s on s.asset_id = a.id "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "left join (select a.asset_id as asset_id,avg(a.cpu_rate) as cpu_rate from nms_cpu_info a where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where a.asset_id = asset_id) "
                + "group by a.asset_id ) as cpu on cpu.asset_id = a.id "
                + "left join (select * from nms_ping_info order by id desc limit 1) as  ping on ping.asset_id = a.id "
                + "left join (select * from nms_mem_info order by itime desc limit 1) as mem on mem.asset_id = a.id where 1=1 ";
        String alarmListSql = "SELECT DISTINCT a.id "
                + "FROM nms_asset as a,nms_alarm as b "
                + "WHERE 1=1 and a.id = b.asset_id AND (b.d_status = 0 OR b.d_status = 1) ";
        StringBuilder builderCountSql = new StringBuilder(countSql);
        StringBuilder builderQuerySql = new StringBuilder(querySql);
        StringBuilder builderAlarmSql = new StringBuilder(alarmListSql);
        if (AName != null && AName.length() > 0) {
            builderCountSql.append(" and a.a_name like '%" + AName + "%' ");
            builderQuerySql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderCountSql.append(" and a.a_ip like '%" + AIp + "%' ");
            builderQuerySql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APos != null && APos.length() > 0) {
            builderCountSql.append(" and a.a_pos = '" + APos + "' ");
            builderQuerySql.append(" and a.a_pos = '" + APos + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderCountSql.append(" and at.id = '" + nmsAssetType + "' ");
            builderQuerySql.append(" and at.id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            builderQuerySql.append(" order by a." + orderKey);
        } else {
            builderQuerySql.append(" order by a." + orderKey + " desc");
        }
        builderQuerySql.append(" limit " + (begin - 1) * offset + "," + offset);


        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();


        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
        session.close();

        List<NmsPerformanceRecord> nmsPerformanceRecordList = new ArrayList<NmsPerformanceRecord>();
        int minId = (begin - 1) * offset;
        int maxId = minId + offset;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                if ((Integer) objects[0] > maxId) {
                    maxId = (Integer) objects[0];
                }
                if ((Integer) objects[0] < minId) {
                    minId = (Integer) objects[0];
                }
                NmsPerformanceRecord nmsPerformanceRecord = new NmsPerformanceRecord();
                nmsPerformanceRecord.setId((Integer) objects[0]);
                nmsPerformanceRecord.setAName((String) objects[1]);
                nmsPerformanceRecord.setAIp((String) objects[2]);
                nmsPerformanceRecord.setDName((String) objects[3]);
                nmsPerformanceRecord.setChType((String) objects[4]);
                nmsPerformanceRecord.setSubChType((String) objects[5]);
                nmsPerformanceRecord.setIfAlarm(0);
                if (objects[6] == null) {
                    nmsPerformanceRecord.setCpuRate(0.0);
                } else {
                    nmsPerformanceRecord.setCpuRate((Double) objects[6]);
                }
                if (objects[7] == null) {
                    nmsPerformanceRecord.setPingRate(0f);
                } else {
                    nmsPerformanceRecord.setPingRate((Float) objects[7]);
                }
                if (objects[8] == null) {
                    nmsPerformanceRecord.setMemFree(0L);
                } else {
                    nmsPerformanceRecord.setMemFree(Long.valueOf(objects[8]
                            .toString()));
                }
                if (objects[9] == null) {
                    nmsPerformanceRecord.setMemTotal(0L);
                } else {
                    nmsPerformanceRecord.setMemTotal(Long.valueOf(objects[9]
                            .toString()));
                }
                if (objects[10] == null) {
                    nmsPerformanceRecord.setSwapFree(0L);
                } else {
                    nmsPerformanceRecord.setSwapFree(Long.valueOf(objects[10]
                            .toString()));
                }
                if (objects[11] == null) {
                    nmsPerformanceRecord.setSwapTotal(0L);
                } else {
                    nmsPerformanceRecord.setSwapTotal(Long.valueOf(objects[11]
                            .toString()));
                }
                if (objects[12] == null) {
                    nmsPerformanceRecord.setNetNum(0);
                } else {
                    nmsPerformanceRecord.setNetNum((Integer) objects[12]);
                }
                nmsPerformanceRecordList.add(nmsPerformanceRecord);
            }
            builderAlarmSql.append(" and a.id <= " + maxId + " and a.id >= "
                    + minId);
            Query alarmquery = session.createSQLQuery(builderAlarmSql
                    .toString());
            List<Integer> ifAlarmAssetIdList = alarmquery.list();
            if (ifAlarmAssetIdList != null && ifAlarmAssetIdList.size() > 0) {
                for (int i = 0; i < nmsPerformanceRecordList.size(); i++) {
                    if (ifAlarmAssetIdList.contains(nmsPerformanceRecordList
                            .get(i).getId())) {
                        nmsPerformanceRecordList.get(i).setIfAlarm(1);
                    }
                }
            }
        }


        // 计算总页数
        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount.intValue() / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount.intValue() / offset;
        } else {
            totalPage = totalCount.intValue() / offset + 1;
        }
        PageBean<NmsPerformanceRecord> pageBean = new PageBean<NmsPerformanceRecord>();
        pageBean.setOrderKey(orderKey);
        pageBean.setOrderValue(orderValue);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(nmsPerformanceRecordList);
        pageBean.setTotalCount(totalCount.intValue());
        pageBean.setLimit(offset);
        pageBean.setPage(begin);
        return pageBean;
    }

    /**
     * 使用ehCache缓存数据，用参数作key
     */
    @Cacheable(value = "local5", key = "'key_listNmsPerformanceByConditionById'+#orderKey+#orderValue+#AIp+#AName+#APos+#nmsAssetType+#ADept+#begin+#offset")
    public PageBean<NmsPerformanceRecord> listNmsPerformanceByConditionById(
            String orderKey, int orderValue, String AName, String AIp,
            String APos, String nmsAssetType, String ADept, int begin,
            int offset) {

        String countSql = "select count(1) " + "from nms_asset a "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "where a.deled = 0 ";
        String idSql = "select a.id " + "from nms_asset a "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "where a.deled = 0 ";

        String alarmListSql = "SELECT DISTINCT a.id "
                + "FROM nms_asset as a,nms_alarm as b "
                + "WHERE 1=1 and a.id = b.asset_id AND (b.d_status = 0 OR b.d_status = 1) ";
        StringBuilder builderCountSql = new StringBuilder(countSql);
        // StringBuilder builderQuerySql = new StringBuilder(querySql);
        StringBuilder builderIdSql = new StringBuilder(idSql);
        StringBuilder builderAlarmSql = new StringBuilder(alarmListSql);
        if (AName != null && AName.length() > 0) {
            builderCountSql.append(" and a.a_name like '%" + AName + "%' ");
            // builderQuerySql.append(" and a.a_name like '%" + AName + "%' ");
            builderIdSql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderCountSql.append(" and a.a_ip like '%" + AIp + "%' ");
            // builderQuerySql.append(" and a.a_ip like '%" + AIp + "%' ");
            builderIdSql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APos != null && APos.length() > 0) {
            builderCountSql.append(" and a.a_pos = '" + APos + "' ");
            // builderQuerySql.append(" and a.a_pos = '" + APos + "' ");
            builderIdSql.append(" and a.a_pos = '" + APos + "' ");
        }
        if (ADept != null && ADept.length() > 0) {
            builderCountSql.append(" and a.dept_id = '" + ADept + "' ");
            // builderQuerySql.append(" and a.a_pos = '" + APos + "' ");
            builderIdSql.append(" and a.dept_id = '" + ADept + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderCountSql.append(" and at.id = '" + nmsAssetType + "' ");
            // builderQuerySql.append(" and at.id = '" + nmsAssetType + "' ");
            builderIdSql.append(" and at.id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            // builderQuerySql.append(" order by a." + orderKey);
            builderIdSql.append(" order by a." + orderKey);
        } else {
            // builderQuerySql.append(" order by a." + orderKey + " desc");
            builderIdSql.append(" order by a." + orderKey + " desc");
        }

        builderIdSql.append(" limit " + (begin - 1) * offset + "," + offset);
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query queryId = session.createSQLQuery(builderIdSql.toString());
        List idlist = queryId.list();
        session.close();

        session = hibernateTemplate.getSessionFactory().openSession();
        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
        session.close();

        List<NmsPerformanceRecord> nmsPerformanceRecordList = new ArrayList<NmsPerformanceRecord>();
        int minId = (begin - 1) * offset;
        int maxId = minId + offset;


        if (idlist != null && idlist.size() > 0) {

            session = hibernateTemplate.getSessionFactory().openSession();
            for (int i = 0; i < idlist.size(); i++) {
                int assetId = (Integer) idlist.get(i);

                if (assetId > maxId) {
                    maxId = assetId;
                }
                if (assetId < minId) {
                    minId = assetId;
                }

                // 按照某一个id循环查询
                String querySql = "select a.id, a.a_name,"
                        + "a.a_ip,"
                        + "d.d_name,"
                        + "at.ch_type,"
                        + "at.ch_subtype,"
                        + "cpu.cpu_rate,"
                        + "ping.ping_rate,"
                        + "mem.mem_free, "
                        + "mem.mem_total, "
                        + "mem.swap_free, "
                        + "mem.swap_total, "
                        + "s.net_num, "
                        + "at.id as typeid, "
                        + "a.online "
                        + "from nms_asset a "
                        + "left join (select * from nms_static_info where asset_id = "
                        + assetId
                        + " order by itime desc limit 1) s on s.asset_id = a.id "
                        + "left join nms_department d on a.dept_id = d.id "
                        + "left join nms_asset_type at on a.type_id = at.id "
                        + "left join (select a.asset_id as asset_id,avg(a.cpu_rate) as cpu_rate from nms_cpu_info a where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where b.asset_id = "
                        + assetId
                        + ") "
                        + "group by a.asset_id ) as cpu on cpu.asset_id = a.id "
                        + "left join (select * from nms_ping_info where asset_id = "
                        + assetId
                        + " order by id desc limit 1) as  ping on ping.asset_id = a.id "
                        + "left join (select * from nms_mem_info where asset_id = "
                        + assetId
                        + " order by itime desc limit 1) as mem on mem.asset_id = a.id where 1=1 and a.id = "
                        + assetId;

                StringBuilder builderQuerySql = new StringBuilder(querySql);
                Query query = session.createSQLQuery(builderQuerySql.toString());
                List list = query.list();
                Object[] objects = (Object[]) list.get(0);

                NmsPerformanceRecord nmsPerformanceRecord = new NmsPerformanceRecord();
                nmsPerformanceRecord.setId((Integer) objects[0]);
                nmsPerformanceRecord.setAName((String) objects[1]);
                nmsPerformanceRecord.setAIp((String) objects[2]);
                nmsPerformanceRecord.setDName((String) objects[3]);
                nmsPerformanceRecord.setChType((String) objects[4]);
                nmsPerformanceRecord.setSubChType((String) objects[5]);
                nmsPerformanceRecord.setOnline((Integer) objects[14]);
                nmsPerformanceRecord.setIfAlarm(0);

                if (nmsAssetService.findOnlineById(assetId)) {
                    if (objects[6] == null) {
                        nmsPerformanceRecord.setCpuRate(-1.0D);
                    } else {
                        nmsPerformanceRecord.setCpuRate((Double) objects[6]);
                    }
                    if (objects[7] == null) {
                        nmsPerformanceRecord.setPingRate(-1.0F);
                    } else {
                        nmsPerformanceRecord.setPingRate((Float) objects[7]);
                    }
                    if (objects[8] == null) {
                        nmsPerformanceRecord.setMemFree(-1L);
                    } else {
                        nmsPerformanceRecord.setMemFree(Long.valueOf(objects[8]
                                .toString()));
                    }
                    if (objects[9] == null) {
                        nmsPerformanceRecord.setMemTotal(-1L);
                    } else {
                        nmsPerformanceRecord.setMemTotal(Long.valueOf(objects[9]
                                .toString()));
                    }
                    if (objects[10] == null) {
                        nmsPerformanceRecord.setSwapFree(-1L);
                    } else {
                        nmsPerformanceRecord.setSwapFree(Long.valueOf(objects[10]
                                .toString()));
                    }
                    if (objects[11] == null) {
                        nmsPerformanceRecord.setSwapTotal(-1L);
                    } else {
                        nmsPerformanceRecord.setSwapTotal(Long.valueOf(objects[11]
                                .toString()));
                    }
                    if (objects[12] == null) {
                        nmsPerformanceRecord.setNetNum(-1);
                    } else {
                        if ((Integer) objects[12] == 0) {
                            nmsPerformanceRecord.setNetNum(-1);
                        } else {
                            nmsPerformanceRecord.setNetNum((Integer) objects[12]);
                        }
                    }
                }

                nmsPerformanceRecord.setTypeId((Integer) objects[13]);
                nmsPerformanceRecordList.add(nmsPerformanceRecord);
            }


            builderAlarmSql.append(" and a.id <= " + maxId + " and a.id >= " + minId);
            Query alarmquery = session.createSQLQuery(builderAlarmSql.toString());
            List<Integer> ifAlarmAssetIdList = alarmquery.list();
            if (ifAlarmAssetIdList != null && ifAlarmAssetIdList.size() > 0) {
                for (int i = 0; i < nmsPerformanceRecordList.size(); i++) {
                    if (ifAlarmAssetIdList.contains(nmsPerformanceRecordList
                            .get(i).getId())) {
                        nmsPerformanceRecordList.get(i).setIfAlarm(1);
                    }
                }
            }

            session.close();
        }

        // 计算总页数
        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount.intValue() / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount.intValue() / offset;
        } else {
            totalPage = totalCount.intValue() / offset + 1;
        }
        PageBean<NmsPerformanceRecord> pageBean = new PageBean<NmsPerformanceRecord>();
        pageBean.setOrderKey(orderKey);
        pageBean.setOrderValue(orderValue);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(nmsPerformanceRecordList);
        pageBean.setTotalCount(totalCount.intValue());
        pageBean.setLimit(offset);
        pageBean.setPage(begin);
        return pageBean;
    }

    @Cacheable(value = "local5", key = "'listNmsPerformanceSoftByConditionById'+#orderKey+#orderValue+#AIp+#APort+#AName+#APos+#nmsAssetType+#ADept+#begin+#offset")
    public PageBean<NmsPerformanceRecord> listNmsPerformanceSoftByConditionById(
            String orderKey, int orderValue, String AName, String AIp,
            String APort, String nmsAssetType, int begin, int offset) {

        String countSql = "select count(1) " + "from nms_soft a "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "where a.deled = 0 ";
        String querySql = "select a.id,a.a_name,a.a_ip,a.a_port,a.a_user,a.colled,a.type_id,at.ch_type,at.ch_subtype " + "from nms_soft a "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "where a.deled = 0 ";

        String alarmListSql = "SELECT DISTINCT a.id "
                + "FROM nms_soft as a,nms_alarm_soft as b "
                + "WHERE 1=1 and a.id = b.soft_id AND (b.d_status = 0 OR b.d_status = 1) ";
        StringBuilder builderCountSql = new StringBuilder(countSql);
        StringBuilder builderQuerySql = new StringBuilder(querySql);
        StringBuilder builderAlarmSql = new StringBuilder(alarmListSql);
        if (AName != null && AName.length() > 0) {
            builderCountSql.append(" and a.a_name like '%" + AName + "%' ");
            builderQuerySql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderCountSql.append(" and a.a_ip like '%" + AIp + "%' ");
            builderQuerySql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APort != null && APort.length() > 0) {
            builderCountSql.append(" and a.a_port = '" + APort + "' ");
            builderQuerySql.append(" and a.a_port = '" + APort + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderCountSql.append(" and at.id = '" + nmsAssetType + "' ");
            builderQuerySql.append(" and at.id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            builderQuerySql.append(" order by a." + orderKey);
        } else {
            builderQuerySql.append(" order by a." + orderKey + " desc");
        }

        builderQuerySql.append(" limit " + (begin - 1) * offset + "," + offset);
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query queryData = session.createSQLQuery(builderQuerySql.toString());
        List list = queryData.list();
        session.close();

        session = hibernateTemplate.getSessionFactory().openSession();
        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
        session.close();

        List<NmsPerformanceRecord> nmsPerformanceRecordList = new ArrayList<NmsPerformanceRecord>();
        int minId = (begin - 1) * offset;
        int maxId = minId + offset;

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] nmsSoft = (Object[]) list.get(i);
                int softId = (Integer) nmsSoft[0];
                if (softId > maxId) {
                    maxId = softId;
                }
                if (softId < minId) {
                    minId = softId;
                }
                NmsPerformanceRecord nmsPerformanceRecord = new NmsPerformanceRecord();
                nmsPerformanceRecord.setId((Integer) nmsSoft[0]);
                nmsPerformanceRecord.setAName((String) nmsSoft[1]);
                nmsPerformanceRecord.setAIp((String) nmsSoft[2]);
                nmsPerformanceRecord.setAPort((String) nmsSoft[3]);
                nmsPerformanceRecord.setAUser((String) nmsSoft[4]);
                nmsPerformanceRecord.setColled((Byte) nmsSoft[5]);
                nmsPerformanceRecord.setTypeId((Integer) nmsSoft[6]);
                nmsPerformanceRecord.setChType((String) nmsSoft[7]);
                nmsPerformanceRecord.setSubChType((String) nmsSoft[8]);
                nmsPerformanceRecord.setIfAlarm(0);
                nmsPerformanceRecordList.add(nmsPerformanceRecord);
            }

            session = hibernateTemplate.getSessionFactory().openSession();
            builderAlarmSql.append(" and a.id <= " + maxId + " and a.id >= " + minId);
            Query alarmquery = session.createSQLQuery(builderAlarmSql.toString());
            List<Integer> ifAlarmAssetIdList = alarmquery.list();
            if (ifAlarmAssetIdList != null && ifAlarmAssetIdList.size() > 0) {
                for (int i = 0; i < nmsPerformanceRecordList.size(); i++) {
                    if (ifAlarmAssetIdList.contains(nmsPerformanceRecordList.get(i).getId())) {
                        nmsPerformanceRecordList.get(i).setIfAlarm(1);
                    }
                }
            }
            session.close();
        }

        // 计算总页数
        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount.intValue() / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount.intValue() / offset;
        } else {
            totalPage = totalCount.intValue() / offset + 1;
        }

        PageBean<NmsPerformanceRecord> pageBean = new PageBean<NmsPerformanceRecord>();
        pageBean.setOrderKey(orderKey);
        pageBean.setOrderValue(orderValue);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(nmsPerformanceRecordList);
        pageBean.setTotalCount(totalCount.intValue());
        pageBean.setLimit(offset);
        pageBean.setPage(begin);

        return pageBean;
    }

    // 这个方法被下面的方法替换，没有调用
    @SuppressWarnings({"rawtypes"})
    @Deprecated
    public PageBean<NmsPerformanceItem> listNmsPerformanceByConditionRealtime(
            String orderKey, int orderValue, String AName, String AIp,
            String APos, String nmsAssetType, int begin, int offset) {
        String countSql = "select count(1) "
                + "from nms_asset a "
                + "left join (select * from nms_static_info order by itime desc limit 1 ) s on s.asset_id = a.id "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "left join (select a.asset_id,avg(a.cpu_rate) from nms_cpu_info a "
                + "where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b "
                + "where a.asset_id = asset_id) "
                + "group by a.asset_id) c on s.asset_id = c.asset_id "
                + "left join (select * from nms_ping_info order by id desc limit 1) p on s.asset_id = p.asset_id "
                + "left join (select * from nms_mem_info order by itime desc limit 1) m on s.asset_id = m.asset_id where 1=1 ";
        String querySql = "select a.id,a.a_name,"
                + "a.a_ip,"
                + "d.d_name,"
                + "at.ch_type,"
                + "at.ch_subtype,"
                + "cpu.cpu_rate,"
                + "ping.ping_rate,"
                + "mem.mem_free, "
                + "mem.mem_total, "
                + "mem.swap_free, "
                + "mem.swap_total, "
                + "s.net_num "
                + "from nms_asset a "
                + "left join (select * from nms_static_info order by itime desc limit 1) s on s.asset_id = a.id "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "left join (select a.asset_id as asset_id,avg(a.cpu_rate) as cpu_rate from nms_cpu_info a where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where a.asset_id = asset_id) "
                + "group by a.asset_id ) as cpu on cpu.asset_id = a.id "
                + "left join (select * from nms_ping_info order by id desc limit 1) as  ping on ping.asset_id = a.id "
                + "left join (select * from nms_mem_info order by itime desc limit 1) as mem on mem.asset_id = a.id where 1=1 ";
        StringBuilder builderCountSql = new StringBuilder(countSql);
        StringBuilder builderQuerySql = new StringBuilder(querySql);
        if (AName != null && AName.length() > 0) {
            builderCountSql.append(" and a.a_name like '%" + AName + "%' ");
            builderQuerySql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderCountSql.append(" and a.a_ip like '%" + AIp + "%' ");
            builderQuerySql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APos != null && APos.length() > 0) {
            builderCountSql.append(" and a.a_pos = '" + APos + "' ");
            builderQuerySql.append(" and a.a_pos = '" + APos + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderCountSql.append(" and at.id = '" + nmsAssetType + "' ");
            builderQuerySql.append(" and at.id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            builderQuerySql.append(" order by a." + orderKey);
        } else {
            builderQuerySql.append(" order by a." + orderKey + " desc");
        }
        builderQuerySql.append(" limit " + (begin - 1) * offset + "," + offset);

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();

        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
        session.close();


        List<NmsPerformanceItem> nmsPerformanceItemList = new ArrayList<NmsPerformanceItem>();
        int minId = (begin - 1) * offset;
        int maxId = minId + offset;
        if (list != null && list.size() > 0) {

            session = hibernateTemplate.getSessionFactory().openSession();
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                if ((Integer) objects[0] > maxId) {
                    maxId = (Integer) objects[0];
                }
                if ((Integer) objects[0] < minId) {
                    minId = (Integer) objects[0];
                }
                NmsPerformanceItem nmsPerformanceItem = new NmsPerformanceItem();
                nmsPerformanceItem.setId((Integer) objects[0]);
                nmsPerformanceItem.setAName((String) objects[1]);
                nmsPerformanceItem.setAIp((String) objects[2]);
                nmsPerformanceItem.setDName((String) objects[3]);
                nmsPerformanceItem.setChType((String) objects[4]);
                nmsPerformanceItem.setSubChType((String) objects[5]);

                if (objects[6] == null) {
                    nmsPerformanceItem.setCpuRate(0.0);
                } else {
                    nmsPerformanceItem.setCpuRate((Double) objects[6]);
                }
                if (objects[7] == null) {
                    nmsPerformanceItem.setPingRate(0f);
                } else {
                    nmsPerformanceItem.setPingRate((Float) objects[7]);
                }
                if (objects[9] == null) {
                    nmsPerformanceItem.setMemTotal(0L);
                } else {
                    nmsPerformanceItem.setMemTotal(Long.valueOf(objects[9]
                            .toString()));
                }

                if (objects[8] == null
                        || nmsPerformanceItem.getMemTotal() == 0L) {
                    nmsPerformanceItem.setMemRate(0.0);
                } else {
                    nmsPerformanceItem.setMemRate(Double.valueOf(objects[8]
                            .toString())
                            / Double.valueOf(objects[9].toString()));
                }
                if (objects[11] == null) {
                    nmsPerformanceItem.setSwapTotal(0L);
                } else {
                    nmsPerformanceItem.setSwapTotal(Long.valueOf(objects[11]
                            .toString()));
                }
                if (objects[10] == null
                        || nmsPerformanceItem.getSwapTotal() == 0L) {
                    nmsPerformanceItem.setSwapRate(0.0);
                } else {
                    nmsPerformanceItem.setSwapRate(Double.valueOf(objects[10]
                            .toString())
                            / Double.valueOf(objects[11].toString()));
                }

                // 获取freq最大值list数据
                String sqlList = "select sum(if_in_octets), sum(if_out_octets) from nms_netif_info as nni where nni.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                        + nmsPerformanceItem.getId()
                        + "') and nni.asset_id = '"
                        + nmsPerformanceItem.getId() + "'";
                SQLQuery queryList = session.createSQLQuery(sqlList);
                List listMaxFreq = queryList.list();
                Object[] objMaxFreq = (Object[]) listMaxFreq.get(0);

                // 获取freq最大值list数据
                String sqlList02 = "select sum(if_in_octets), sum(if_out_octets) from nms_netif_info as nni where nni.freq = (select MAX(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                        + nmsPerformanceItem.getId()
                        + "') - 1 and nni.asset_id = '"
                        + nmsPerformanceItem.getId() + "'";
                SQLQuery queryList02 = session.createSQLQuery(sqlList02);
                List listSecondFreq = queryList02.list();
                Object[] objSecondFreq = (Object[]) listSecondFreq.get(0);

                if (objMaxFreq[0] == null || objSecondFreq[0] == null) {
                    nmsPerformanceItem.setNetInSpeed(0L);
                } else {
                    nmsPerformanceItem.setNetInSpeed((Long
                            .valueOf(objMaxFreq[0].toString()) - Long
                            .valueOf(objSecondFreq[0].toString())) / 1024);
                }
                if (objMaxFreq[1] == null || objSecondFreq[1] == null) {
                    nmsPerformanceItem.setNetOutSpeed(0L);
                } else {
                    nmsPerformanceItem.setNetOutSpeed((Long
                            .valueOf(objMaxFreq[1].toString()) - Long
                            .valueOf(objSecondFreq[1].toString())) / 1024);
                }
                nmsPerformanceItemList.add(nmsPerformanceItem);
            }

            session.close();
        }


        // 计算总页数
        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount.intValue() / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount.intValue() / offset;
        } else {
            totalPage = totalCount.intValue() / offset + 1;
        }
        PageBean<NmsPerformanceItem> pageBean = new PageBean<NmsPerformanceItem>();
        pageBean.setOrderKey(orderKey);
        pageBean.setOrderValue(orderValue);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(nmsPerformanceItemList);
        pageBean.setTotalCount(totalCount.intValue());
        pageBean.setLimit(offset);
        pageBean.setPage(begin);
        return pageBean;
    }

    @SuppressWarnings({"rawtypes"})
    @Cacheable(value = "local5", key = "'key_listNmsPerformanceByConditionById'+#AIp+#AName+#APos+#nmsAssetType+#ADept+#begin+#offset+#orderKey+#orderValue")
    public PageBean<NmsPerformanceItem> listNmsPerformanceByConditionRealtimeById(
            String orderKey, int orderValue, String AName, String AIp,
            String APos, String nmsAssetType, String ADept, int begin,
            int offset) {

        String idSql = "select a.id " + "from nms_asset a "
                + "where a.deled = 0 ";

        String countSql = "select count(1) " + "from nms_asset a "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "where a.deled = 0 ";

        StringBuilder builderCountSql = new StringBuilder(countSql);

        StringBuilder builderIdSql = new StringBuilder(idSql);
        if (AName != null && AName.length() > 0) {
            builderCountSql.append(" and a.a_name like '%" + AName + "%' ");
            builderIdSql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderCountSql.append(" and a.a_ip like '%" + AIp + "%' ");
            builderIdSql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APos != null && APos.length() > 0) {
            builderCountSql.append(" and a.a_pos = '" + APos + "' ");
            builderIdSql.append(" and a.a_pos = '" + APos + "' ");
        }
        if (ADept != null && ADept.length() > 0) {
            builderCountSql.append(" and a.dept_id = '" + ADept + "' ");
            builderIdSql.append(" and a.dept_id = '" + ADept + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderCountSql.append(" and a.type_id = '" + nmsAssetType + "' ");
            builderIdSql.append(" and a.type_id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            builderIdSql.append(" order by " + orderKey);
        } else {
            builderIdSql.append(" order by " + orderKey + " desc");
        }

        builderIdSql.append(" limit " + (begin - 1) * offset + "," + offset);

        Session session = hibernateTemplate.getSessionFactory().openSession();

        Query queryId = session.createSQLQuery(builderIdSql.toString());
        List idlist = queryId.list();

        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
        session.close();

        List<NmsPerformanceItem> nmsPerformanceItemList = new ArrayList<NmsPerformanceItem>();
        int minId = (begin - 1) * offset;
        int maxId = minId + offset;
        if (idlist != null && idlist.size() > 0) {

            session = hibernateTemplate.getSessionFactory().openSession();
            session.createSQLQuery(builderCountSql.toString());
            for (int i = 0; i < idlist.size(); i++) {
                int assetId = (Integer) idlist.get(i);
                if (assetId > maxId) {
                    maxId = assetId;
                }
                if (assetId < minId) {
                    minId = assetId;
                }

                String querySql = "select a.id,a.a_name,"
                        + "a.a_ip,"
                        + "d.d_name,"
                        + "at.ch_type,"
                        + "at.ch_subtype,"
                        + "cpu.cpu_rate,"
                        + "ping.ping_rate,"
                        + "mem.mem_free, "
                        + "mem.mem_total, "
                        + "mem.swap_free, "
                        + "mem.swap_total, "
                        + "s.net_num, "
                        + "a.online "
                        + "from nms_asset a "
                        + "left join (select * from nms_static_info where asset_id = "
                        + assetId
                        + " order by itime desc limit 1) s on s.asset_id = a.id "
                        + "left join nms_department d on a.dept_id = d.id "
                        + "left join nms_asset_type at on a.type_id = at.id "
                        + "left join (select a.asset_id as asset_id,avg(a.cpu_rate) as cpu_rate from nms_cpu_info a where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where b.asset_id = "
                        + assetId
                        + ") "
                        + "group by a.asset_id ) as cpu on cpu.asset_id = a.id "
                        + "left join (select * from nms_ping_info where asset_id = "
                        + assetId
                        + " order by id desc limit 1) as  ping on ping.asset_id = a.id "
                        + "left join (select * from nms_mem_info where asset_id = "
                        + assetId
                        + " order by itime desc limit 1) as mem on mem.asset_id = a.id where 1=1 and a.id = "
                        + assetId;

                StringBuilder builderQuerySql = new StringBuilder(querySql);
                Query query = session.createSQLQuery(builderQuerySql.toString());
                List list = query.list();
                Object[] objects = (Object[]) list.get(0);

                NmsPerformanceItem nmsPerformanceItem = new NmsPerformanceItem();
                nmsPerformanceItem.setId((Integer) objects[0]);
                nmsPerformanceItem.setAName((String) objects[1]);
                nmsPerformanceItem.setAIp((String) objects[2]);
                nmsPerformanceItem.setDName((String) objects[3]);
                nmsPerformanceItem.setChType((String) objects[4]);
                nmsPerformanceItem.setSubChType((String) objects[5]);
                nmsPerformanceItem.setOnline((Integer) objects[13]);

                if (objects[13] != null && objects[13].equals(1)) {
                    if (objects[6] == null) {
                        nmsPerformanceItem.setCpuRate(0.0);
                    } else {
                        nmsPerformanceItem.setCpuRate((Double) objects[6]);
                    }
                    if (objects[7] == null) {
                        nmsPerformanceItem.setPingRate(0f);
                    } else {
                        nmsPerformanceItem.setPingRate((Float) objects[7]);
                    }
                    if (objects[9] == null) {
                        nmsPerformanceItem.setMemTotal(0L);
                    } else {
                        nmsPerformanceItem.setMemTotal(Long.valueOf(objects[9]
                                .toString()));
                    }

                    if (objects[8] == null
                            || nmsPerformanceItem.getMemTotal() == 0L) {
                        nmsPerformanceItem.setMemRate(0.0);
                    } else {
                        nmsPerformanceItem.setMemRate(1 - (Double
                                .valueOf(objects[8].toString()) / Double
                                .valueOf(objects[9].toString())));
                    }
                    if (objects[11] == null) {
                        nmsPerformanceItem.setSwapTotal(0L);
                    } else {
                        nmsPerformanceItem.setSwapTotal(Long.valueOf(objects[11]
                                .toString()));
                    }
                    if (objects[10] == null
                            || nmsPerformanceItem.getSwapTotal() == 0L) {
                        nmsPerformanceItem.setSwapRate(0.0);
                    } else {
                        nmsPerformanceItem.setSwapRate(1 - (Double
                                .valueOf(objects[10].toString()) / Double
                                .valueOf(objects[11].toString())));
                    }

                    // 获取freq最大值list数据
                    String sqlList = "select sum(if_in_octets), sum(if_out_octets) ,min(itime) from nms_netif_info as nni where nni.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                            + assetId + "') and nni.asset_id = '" + assetId + "' and nni.if_oper_status = 1 and nni.if_admin_status = 1";
                    SQLQuery queryList = session.createSQLQuery(sqlList);
                    List listMaxFreq = queryList.list();
                    Object[] objMaxFreq = (Object[]) listMaxFreq.get(0);

                    // 获取freq最大值list数据
                    String sqlList02 = "select sum(if_in_octets), sum(if_out_octets),min(itime) from nms_netif_info as nni where nni.freq = (select freq from nms_netif_info where asset_id = '" + assetId + "' and freq < (select MAX(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                            + assetId
                            + "') order by freq desc limit 1 ) and nni.asset_id = '"
                            + assetId
                            + "' and nni.if_oper_status = 1 and nni.if_admin_status = 1";

                    SQLQuery queryList02 = session.createSQLQuery(sqlList02);
                    List listSecondFreq = queryList02.list();
                    Object[] objSecondFreq = (Object[]) listSecondFreq.get(0);

                    if (objMaxFreq[0] == null || objSecondFreq[0] == null) {
                        nmsPerformanceItem.setNetInSpeed(0d);
                    } else {
                        long speed = 0;
                        long ts = 0;

                        try {
                            speed = (Long.valueOf(objMaxFreq[0].toString()) - Long.valueOf(objSecondFreq[0].toString())) / 1024;

                            // 获取第一次时间
                            String times = objMaxFreq[2].toString();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = simpleDateFormat.parse(times);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long ts01 = date.getTime() / 1000;

                            // 获取第二次时间
                            times = objSecondFreq[2].toString();
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date = null;
                            try {
                                date = simpleDateFormat.parse(times);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long ts02 = date.getTime() / 1000;

                            ts = ts01 - ts02;


                        } catch (NumberFormatException e) {
                            speed = 0;
                            ts = 0;
                        }

                        if (ts > 0 && speed > 0) {
                            double d = speed * 1.0 / ts;
                            d = (double) Math.round(d * 100) / 100;
                            nmsPerformanceItem.setNetInSpeed(d);
                        } else {
                            nmsPerformanceItem.setNetInSpeed(0.00);
                        }
                    }

                    if (objMaxFreq[1] == null || objSecondFreq[1] == null) {
                        nmsPerformanceItem.setNetOutSpeed(0L);
                    } else {
                        long speed = 0;
                        long ts = 0;

                        try {
                            speed = (Long.valueOf(objMaxFreq[1].toString()) - Long.valueOf(objSecondFreq[1].toString())) / 1024;

                            // 获取第一次时间
                            String times = objMaxFreq[2].toString();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = simpleDateFormat.parse(times);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long ts01 = date.getTime() / 1000;

                            // 获取第二次时间
                            times = objSecondFreq[2].toString();
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date = null;
                            try {
                                date = simpleDateFormat.parse(times);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long ts02 = date.getTime() / 1000;

                            ts = ts01 - ts02;

                        } catch (NumberFormatException e) {
                            speed = 0;
                            ts = 0;
                        }

                        if (ts > 0 && speed > 0) {
                            double d = speed * 1.0 / ts;
                            d = (double) Math.round(d * 100) / 100;
                            nmsPerformanceItem.setNetOutSpeed(d);
                        } else {
                            nmsPerformanceItem.setNetOutSpeed(0.00);
                        }
                    }
                }
                nmsPerformanceItemList.add(nmsPerformanceItem);
            }

            session.close();
        }

        // 计算总页数
        int totalPage = 1;
        if (totalCount == 0) {
            totalPage = totalCount.intValue() / offset + 1;
        } else if (totalCount % offset == 0) {
            totalPage = totalCount.intValue() / offset;
        } else {
            totalPage = totalCount.intValue() / offset + 1;
        }
        PageBean<NmsPerformanceItem> pageBean = new PageBean<NmsPerformanceItem>();
        pageBean.setOrderKey(orderKey);
        pageBean.setOrderValue(orderValue);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(nmsPerformanceItemList);
        pageBean.setTotalCount(totalCount.intValue());
        pageBean.setLimit(offset);
        pageBean.setPage(begin);
        return pageBean;
    }

    // 这个方法被下面的方法替换，没有调用
    @SuppressWarnings({"rawtypes"})
    public List<NmsPerformanceItem> listNmsPerformanceByConditionRealtimeExportExcel(
            String orderKey, int orderValue, String AName, String AIp,
            String APos, String nmsAssetType) {
        String querySql = "select a.id,a.a_name,"
                + "a.a_ip,"
                + "d.d_name,"
                + "at.ch_type,"
                + "at.ch_subtype,"
                + "cpu.cpu_rate,"
                + "ping.ping_rate,"
                + "mem.mem_free, "
                + "mem.mem_total, "
                + "mem.swap_free, "
                + "mem.swap_total, "
                + "s.net_num "
                + "from nms_asset a "
                + "left join (select * from nms_static_info order by itime desc limit 1) s on s.asset_id = a.id "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "left join (select a.asset_id as asset_id,avg(a.cpu_rate) as cpu_rate from nms_cpu_info a where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where a.asset_id = asset_id) "
                + "group by a.asset_id ) as cpu on cpu.asset_id = a.id "
                + "left join (select * from nms_ping_info order by id desc limit 1) as  ping on ping.asset_id = a.id "
                + "left join (select * from nms_mem_info order by itime desc limit 1) as mem on mem.asset_id = a.id where 1=1 ";
        StringBuilder builderQuerySql = new StringBuilder(querySql);
        if (AName != null && AName.length() > 0) {
            builderQuerySql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderQuerySql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APos != null && APos.length() > 0) {
            builderQuerySql.append(" and a.a_pos = '" + APos + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderQuerySql.append(" and at.id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            builderQuerySql.append(" order by a." + orderKey);
        } else {
            builderQuerySql.append(" order by a." + orderKey + " desc");
        }
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();
        session.close();

        List<NmsPerformanceItem> nmsPerformanceItemList = new ArrayList<NmsPerformanceItem>();
        if (list != null && list.size() > 0) {
            session = hibernateTemplate.getSessionFactory().openSession();

            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                NmsPerformanceItem nmsPerformanceItem = new NmsPerformanceItem();
                nmsPerformanceItem.setId((Integer) objects[0]);
                nmsPerformanceItem.setAName((String) objects[1]);
                nmsPerformanceItem.setAIp((String) objects[2]);
                nmsPerformanceItem.setDName((String) objects[3]);
                nmsPerformanceItem.setChType((String) objects[4]);
                nmsPerformanceItem.setSubChType((String) objects[5]);

                if (objects[6] == null) {
                    nmsPerformanceItem.setCpuRate(0.0);
                } else {
                    nmsPerformanceItem.setCpuRate((Double) objects[6]);
                }
                if (objects[7] == null) {
                    nmsPerformanceItem.setPingRate(0f);
                } else {
                    nmsPerformanceItem.setPingRate((Float) objects[7]);
                }
                if (objects[9] == null) {
                    nmsPerformanceItem.setMemTotal(0L);
                } else {
                    nmsPerformanceItem.setMemTotal(Long.valueOf(objects[9]
                            .toString()));
                }

                if (objects[8] == null
                        || nmsPerformanceItem.getMemTotal() == 0L) {
                    nmsPerformanceItem.setMemRate(0.0);
                } else {
                    nmsPerformanceItem.setMemRate(Double.valueOf(objects[8]
                            .toString())
                            / Double.valueOf(objects[9].toString()));
                }
                if (objects[11] == null) {
                    nmsPerformanceItem.setSwapTotal(0L);
                } else {
                    nmsPerformanceItem.setSwapTotal(Long.valueOf(objects[11]
                            .toString()));
                }
                if (objects[10] == null
                        || nmsPerformanceItem.getSwapTotal() == 0L) {
                    nmsPerformanceItem.setSwapRate(0.0);
                } else {
                    nmsPerformanceItem.setSwapRate(Double.valueOf(objects[10]
                            .toString())
                            / Double.valueOf(objects[11].toString()));
                }

                // 获取freq最大值list数据
                String sqlList = "select sum(if_in_octets), sum(if_out_octets) from nms_netif_info as nni where nni.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                        + nmsPerformanceItem.getId()
                        + "') and nni.asset_id = '"
                        + nmsPerformanceItem.getId() + "'";
                SQLQuery queryList = session.createSQLQuery(sqlList);
                List listMaxFreq = queryList.list();
                Object[] objMaxFreq = (Object[]) listMaxFreq.get(0);
                //		System.out.println(list.size());

                // 获取freq最大值list数据
                String sqlList02 = "select sum(if_in_octets), sum(if_out_octets) from nms_netif_info as nni where nni.freq = (select MAX(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                        + nmsPerformanceItem.getId()
                        + "') - 1 and nni.asset_id = '"
                        + nmsPerformanceItem.getId() + "'";
                SQLQuery queryList02 = session.createSQLQuery(sqlList02);
                List listSecondFreq = queryList02.list();
                Object[] objSecondFreq = (Object[]) listSecondFreq.get(0);

                if (objMaxFreq[0] == null || objSecondFreq[0] == null) {
                    nmsPerformanceItem.setNetInSpeed(0L);
                } else {
                    nmsPerformanceItem.setNetInSpeed((Long
                            .valueOf(objMaxFreq[0].toString()) - Long
                            .valueOf(objSecondFreq[0].toString())) / 1024);
                }
                if (objMaxFreq[1] == null || objSecondFreq[1] == null) {
                    nmsPerformanceItem.setNetOutSpeed(0L);
                } else {
                    nmsPerformanceItem.setNetOutSpeed((Long
                            .valueOf(objMaxFreq[1].toString()) - Long
                            .valueOf(objSecondFreq[1].toString())) / 1024);
                }
                nmsPerformanceItemList.add(nmsPerformanceItem);
            }

            session.close();
        }


        return nmsPerformanceItemList;
    }

    @SuppressWarnings({"rawtypes"})
    public List<NmsPerformanceItem> listNmsPerformanceByConditionRealtimeExportExcelById(
            String orderKey, int orderValue, String AName, String AIp,
            String APos, String nmsAssetType, String ADept) {

        String idSql = "select a.id " + "from nms_asset a "
                + "left join nms_department d on a.dept_id = d.id "
                + "left join nms_asset_type at on a.type_id = at.id "
                + "where a.deled = 0";

        StringBuilder builderIdSql = new StringBuilder(idSql);
        if (AName != null && AName.length() > 0) {
            builderIdSql.append(" and a.a_name like '%" + AName + "%' ");
        }
        if (AIp != null && AIp.length() > 0) {
            builderIdSql.append(" and a.a_ip like '%" + AIp + "%' ");
        }
        if (APos != null && APos.length() > 0) {
            builderIdSql.append(" and a.a_pos = '" + APos + "' ");
        }
        if (ADept != null && ADept.length() > 0) {
            builderIdSql.append(" and a.dept_id = '" + ADept + "' ");
        }
        if (nmsAssetType != null && nmsAssetType.length() > 0) {
            builderIdSql.append(" and at.id = '" + nmsAssetType + "' ");
        }

        if (orderKey == null || orderKey.length() == 0) {
            orderKey = "id";
        }
        if (orderValue == 0) {
            builderIdSql.append(" order by a." + orderKey);
        } else {
            builderIdSql.append(" order by a." + orderKey + " desc");
        }

        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query queryId = session.createSQLQuery(builderIdSql.toString());
        List idlist = queryId.list();
        session.close();

        List<NmsPerformanceItem> nmsPerformanceItemList = new ArrayList<NmsPerformanceItem>();
        if (idlist != null && idlist.size() > 0) {

            session = hibernateTemplate.getSessionFactory().openSession();
            for (int i = 0; i < idlist.size(); i++) {
                int assetId = (Integer) idlist.get(i);
                String querySql = "select a.id,a.a_name,"
                        + "a.a_ip,"
                        + "d.d_name,"
                        + "at.ch_type,"
                        + "at.ch_subtype,"
                        + "cpu.cpu_rate,"
                        + "ping.ping_rate,"
                        + "mem.mem_free, "
                        + "mem.mem_total, "
                        + "mem.swap_free, "
                        + "mem.swap_total, "
                        + "s.net_num, "
                        + "a.online "
                        + "from nms_asset a "
                        + "left join (select * from nms_static_info where asset_id = "
                        + assetId
                        + " order by itime desc limit 1) s on s.asset_id = a.id "
                        + "left join nms_department d on a.dept_id = d.id "
                        + "left join nms_asset_type at on a.type_id = at.id "
                        + "left join (select a.asset_id as asset_id,avg(a.cpu_rate) as cpu_rate from nms_cpu_info a where freq= (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where b.asset_id = "
                        + assetId
                        + ") "
                        + "group by a.asset_id ) as cpu on cpu.asset_id = a.id "
                        + "left join (select * from nms_ping_info where asset_id = "
                        + assetId
                        + " order by id desc limit 1) as  ping on ping.asset_id = a.id "
                        + "left join (select * from nms_mem_info where asset_id = "
                        + assetId
                        + " order by itime desc limit 1) as mem on mem.asset_id = a.id where 1=1 and a.id = "
                        + assetId;

                StringBuilder builderQuerySql = new StringBuilder(querySql);
                Query query = session
                        .createSQLQuery(builderQuerySql.toString());
                List list = query.list();
                if (list != null && list.size() > 0) {
                    Object[] objects = (Object[]) list.get(0);

                    NmsPerformanceItem nmsPerformanceItem = new NmsPerformanceItem();
                    nmsPerformanceItem.setId((Integer) objects[0]);
                    nmsPerformanceItem.setAName((String) objects[1]);
                    nmsPerformanceItem.setAIp((String) objects[2]);
                    nmsPerformanceItem.setDName((String) objects[3]);
                    nmsPerformanceItem.setChType((String) objects[4]);
                    nmsPerformanceItem.setSubChType((String) objects[5]);
                    nmsPerformanceItem.setOnline((Integer) objects[13]);

                    if (objects[13] != null && objects[13].equals(1)) {
                        if (objects[6] == null) {
                            nmsPerformanceItem.setCpuRate(0.0);
                        } else {
                            nmsPerformanceItem.setCpuRate((Double) objects[6]);
                        }
                        if (objects[7] == null) {
                            nmsPerformanceItem.setPingRate(0f);
                        } else {
                            nmsPerformanceItem.setPingRate((Float) objects[7]);
                        }
                        if (objects[9] == null) {
                            nmsPerformanceItem.setMemTotal(0L);
                        } else {
                            nmsPerformanceItem.setMemTotal(Long.valueOf(objects[9]
                                    .toString()));
                        }

                        if (objects[8] == null
                                || nmsPerformanceItem.getMemTotal() == 0L) {
                            nmsPerformanceItem.setMemRate(0.0);
                        } else {
                            nmsPerformanceItem.setMemRate(1 - (Double
                                    .valueOf(objects[8].toString()) / Double
                                    .valueOf(objects[9].toString())));
                        }
                        if (objects[11] == null) {
                            nmsPerformanceItem.setSwapTotal(0L);
                        } else {
                            nmsPerformanceItem.setSwapTotal(Long
                                    .valueOf(objects[11].toString()));
                        }
                        if (objects[10] == null
                                || nmsPerformanceItem.getSwapTotal() == 0L) {
                            nmsPerformanceItem.setSwapRate(0.0);
                        } else {
                            nmsPerformanceItem.setSwapRate(1 - (Double
                                    .valueOf(objects[10].toString()) / Double
                                    .valueOf(objects[11].toString())));
                        }

                        // 获取freq最大值list数据
                        String sqlList = "select sum(if_in_octets), sum(if_out_octets), min(itime) from nms_netif_info as nni where nni.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                                + nmsPerformanceItem.getId()
                                + "') and nni.asset_id = '"
                                + nmsPerformanceItem.getId() + "' and if_oper_status=1 and if_admin_status=1";
                        SQLQuery queryList = session.createSQLQuery(sqlList);
                        List listMaxFreq = queryList.list();
                        Object[] objMaxFreq = (Object[]) listMaxFreq.get(0);
                        //		System.out.println(list.size());

                        // 获取freq最大值list数据
                        String sqlList02 = "select sum(if_in_octets), sum(if_out_octets), min(itime) from nms_netif_info as nni where nni.freq = (select freq from nms_netif_info where asset_id = '" + assetId + "' and freq < (select MAX(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= '"
                                + assetId
                                + "') order by freq desc limit 1 ) and nni.asset_id = '"
                                + assetId
                                + "' and nni.if_oper_status = 1 and nni.if_admin_status = 1";
                        SQLQuery queryList02 = session.createSQLQuery(sqlList02);
                        List listSecondFreq = queryList02.list();
                        Object[] objSecondFreq = (Object[]) listSecondFreq.get(0);

                        if (objMaxFreq[0] == null || objSecondFreq[0] == null) {
                            nmsPerformanceItem.setNetInSpeed(0L);
                        } else {
                            long speed = 0;
                            long ts = 0;

                            try {
                                speed = (Long.valueOf(objMaxFreq[0].toString()) - Long.valueOf(objSecondFreq[0].toString())) / 1024;

                                // 获取第一次时间
                                String times = objMaxFreq[2].toString();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = null;
                                try {
                                    date = simpleDateFormat.parse(times);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long ts01 = date.getTime() / 1000;

                                // 获取第二次时间
                                times = objSecondFreq[2].toString();
                                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                date = null;
                                try {
                                    date = simpleDateFormat.parse(times);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long ts02 = date.getTime() / 1000;

                                ts = ts01 - ts02;

                            } catch (NumberFormatException e) {
                                speed = 0;
                                ts = 0;
                            }

                            if (ts > 0 && speed > 0) {
                                double d = speed * 1.0 / ts;
                                d = (double) Math.round(d * 100) / 100;
                                nmsPerformanceItem.setNetInSpeed(d);
                            } else {
                                nmsPerformanceItem.setNetInSpeed(0.00);
                            }
                        }
                        if (objMaxFreq[1] == null || objSecondFreq[1] == null) {
                            nmsPerformanceItem.setNetOutSpeed(0L);
                        } else {
                            long speed = 0;
                            long ts = 0;

                            try {
                                speed = (Long.valueOf(objMaxFreq[1].toString()) - Long.valueOf(objSecondFreq[1].toString())) / 1024;

                                // 获取第一次时间
                                String times = objMaxFreq[2].toString();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = null;
                                try {
                                    date = simpleDateFormat.parse(times);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long ts01 = date.getTime() / 1000;

                                // 获取第二次时间
                                times = objSecondFreq[2].toString();
                                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                date = null;
                                try {
                                    date = simpleDateFormat.parse(times);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long ts02 = date.getTime() / 1000;

                                ts = ts01 - ts02;

                            } catch (NumberFormatException e) {
                                speed = 0;
                                ts = 0;
                            }

                            if (ts > 0 && speed > 0) {
                                double d = speed * 1.0 / ts;
                                d = (double) Math.round(d * 100) / 100;
                                nmsPerformanceItem.setNetOutSpeed(d);
                            } else {
                                nmsPerformanceItem.setNetOutSpeed(0.00);
                            }
                        }
                    }

                    nmsPerformanceItemList.add(nmsPerformanceItem);
                }
            }

            session.close();
        }

        return nmsPerformanceItemList;
    }
}
