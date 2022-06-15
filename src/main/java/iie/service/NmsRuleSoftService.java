package iie.service;

import iie.tools.NmsRuleSoftItem;
import iie.tools.PageBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xczhao
 * @date 2020/10/17 - 13:12
 */
@Service("NmsRuleSoftService")
public class NmsRuleSoftService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    public boolean deleteBySoftId(String softId) {
        boolean res = true;
        String sql = "delete from nms_rule_soft where soft_id = " + softId;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        try {
            session.createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            res = false;
        }
        session.close();
        return res;
    }

    public PageBean<NmsRuleSoftItem> findNmsRuleSoft(String ip, String port,
                                                     Integer assetTypeId, String ruleContent, int begin, int offset, String orderKey, String orderValue) {

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

        // 获取count总数
        String countSql = "select count(1)"
                + " from nms_rule_soft ra left join nms_asset_type t on ra.atype_id = t.id left join nms_soft a on ra.soft_id = a.id"
                + " where 1 = 1";
        String querySql = "select ra.id,ra.atype_id,ra.soft_id,a.a_name,a.a_ip,a.a_port,t.ch_type,t.ch_subtype,ra.r_content,ra.r_unit,ra.r_enable,ra.r_value1,ra.r_value2,ra.r_value3,ra.itime"
                + " from nms_rule_soft ra left join nms_asset_type t on ra.atype_id = t.id left join nms_soft a on ra.soft_id = a.id"
                + " where 1 = 1";

        StringBuilder builderCountSql = new StringBuilder(countSql);
        StringBuilder builderQuerySql = new StringBuilder(querySql);
        if (assetTypeId >= 0) {
            builderCountSql.append(" and ra.atype_id = '" + assetTypeId + "' ");
            builderQuerySql.append(" and ra.atype_id = '" + assetTypeId + "' ");
        }
        if (ruleContent != null) {
            builderCountSql.append(" and ra.r_content like '%" + ruleContent + "%' ");
            builderQuerySql.append(" and ra.r_content like '%" + ruleContent + "%' ");
        }
        if (ip != null) {
            builderCountSql.append(" and a.a_ip like '%" + ip + "%' ");
            builderQuerySql.append(" and a.a_ip like '%" + ip + "%' ");
        }
        if (port != null) {
            builderCountSql.append(" and a.a_port like '%" + port + "%' ");
            builderQuerySql.append(" and a.a_port like '%" + port + "%' ");
        }
        builderQuerySql.append(" order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset);

        //获取session
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();
        Query countQuery = session.createSQLQuery(builderCountSql.toString());
        Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
        session.close();

        List<NmsRuleSoftItem> nmsRuleAssetItemList = new ArrayList<NmsRuleSoftItem>();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                NmsRuleSoftItem ruleAssetItem = new NmsRuleSoftItem();
                ruleAssetItem.setId((Integer) objects[0]);
                ruleAssetItem.setATypeId((Integer) objects[1]);
                ruleAssetItem.setSoftId((Integer) objects[2]);
                ruleAssetItem.setAName((String) objects[3]);
                ruleAssetItem.setAIp((String) objects[4]);
                ruleAssetItem.setAPort((String) objects[5]);
                ruleAssetItem.setChtype((String) objects[6]);
                ruleAssetItem.setChSubType((String) objects[7]);
                ruleAssetItem.setrContent((String) objects[8]);
                if (objects[8] == null) {
                    ruleAssetItem.setrUnit("无");
                } else {
                    ruleAssetItem.setrUnit((String) objects[9]);
                }
                ruleAssetItem.setrEnable((Integer) objects[10]);
                ruleAssetItem.setrValue1(Long.valueOf(objects[11].toString()));
                ruleAssetItem.setrValue2(Long.valueOf(objects[12].toString()));
                ruleAssetItem.setrValue3(Long.valueOf(objects[13].toString()));
                ruleAssetItem.setItime((Timestamp) objects[14]);
                nmsRuleAssetItemList.add(ruleAssetItem);
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
        PageBean<NmsRuleSoftItem> pageBean = new PageBean<NmsRuleSoftItem>();
        pageBean.setTotalPage(totalPage);
        pageBean.setList(nmsRuleAssetItemList);
        pageBean.setTotalCount(totalCount.intValue());
        pageBean.setLimit(offset);
        pageBean.setPage(begin);
        return pageBean;
    }

    public NmsRuleSoftItem findNmsRuleSoftById(Integer id) {

        String querySql = "select ra.id,ra.atype_id,ra.soft_id,a.a_name,a.a_ip,a.a_port,t.ch_type,t.ch_subtype,ra.r_content,ra.r_unit,ra.r_enable,ra.r_value1,ra.r_value2,ra.r_value3,ra.itime,ra.r_seq"
                + " from nms_rule_soft ra left join nms_asset_type t on ra.atype_id = t.id left join nms_soft a on ra.soft_id = a.id"
                + " where 1 = 1 and ra.id = " + id;
        StringBuilder builderQuerySql = new StringBuilder(querySql);

        //获取session
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createSQLQuery(builderQuerySql.toString());
        List list = query.list();
        session.close();

        Object[] objects = (Object[]) list.get(0);
        NmsRuleSoftItem ruleAssetItem = new NmsRuleSoftItem();
        ruleAssetItem.setId((Integer) objects[0]);
        ruleAssetItem.setATypeId((Integer) objects[1]);
        ruleAssetItem.setSoftId((Integer) objects[2]);
        ruleAssetItem.setAName((String) objects[3]);
        ruleAssetItem.setAIp((String) objects[4]);
        ruleAssetItem.setAPort((String) objects[5]);
        ruleAssetItem.setChtype((String) objects[6]);
        ruleAssetItem.setChSubType((String) objects[7]);
        ruleAssetItem.setrContent((String) objects[8]);
        if (objects[9] == null) {
            ruleAssetItem.setrUnit("无");
        } else {
            ruleAssetItem.setrUnit((String) objects[9]);
        }
        ruleAssetItem.setrEnable((Integer) objects[10]);
        ruleAssetItem.setrValue1(Long.valueOf(objects[11].toString()));
        ruleAssetItem.setrValue2(Long.valueOf(objects[12].toString()));
        ruleAssetItem.setrValue3(Long.valueOf(objects[13].toString()));
        ruleAssetItem.setItime((Timestamp) objects[14]);
        ruleAssetItem.setrSeq((Integer) objects[15]);

        return ruleAssetItem;
    }

    public boolean updateNmsRuleSoftById(Integer id, Integer rEnable, Integer rSeq,
                                          Integer rValue1, Integer rValue2, Integer rValue3) {
        boolean res = true;
        String sql = "UPDATE nms_rule_soft SET r_enable = " + rEnable + ", r_seq = "
                + rSeq + ", r_value1 = " + rValue1 + ", r_value2 = " + rValue2
                + ", r_value3 = " + rValue3 + " WHERE id = " + id;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction tran = session.beginTransaction();
        try {
            session.createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            res = false;
        }
        tran.commit();
        session.close();
        return res;
    }
}
