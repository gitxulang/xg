package iie.service;

import iie.pojo.NmsAssetType;
import iie.pojo.NmsNetifInfo;
import iie.pojo.NmsRule;
import iie.tools.NmsPerformanceRecord;
import iie.tools.NmsRuleItem;
import iie.tools.PageBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsRuleService")
public class NmsRuleService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsRule> findNmsRulesByAssetTypeId(Integer assetTypeId) {
		String sql = "from NmsRule where nmsAssetType = ?";
		NmsAssetType assetType = new NmsAssetType();
		assetType.setId(assetTypeId);
		List<NmsRule> list = (List<NmsRule>) hibernateTemplate.find(sql,
				new Object[] { assetType });
		return list;
	}

	public PageBean<NmsRuleItem> findNmsRules(Integer assetTypeId,
			String ruleContent, int begin, int offset, String orderKey, String orderValue) {

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
				+ " from nms_rule r left join nms_asset_type t on r.atype_id = t.id"
				+ " where 1 = 1";
		
		String querySql = "select r.id,r.atype_id,t.ch_type,t.ch_subtype,r.r_content,r.r_unit,r.r_enable,r.r_value1,r.r_value2,r.r_value3,r.itime"
				+ " from nms_rule r left join nms_asset_type t on r.atype_id = t.id"
				+ " where 1 = 1";
		StringBuilder builderCountSql = new StringBuilder(countSql);
		StringBuilder builderQuerySql = new StringBuilder(querySql);
		if (assetTypeId >= 0) {
			builderCountSql.append(" and r.atype_id = '" + assetTypeId + "' ");
			builderQuerySql.append(" and r.atype_id = '" + assetTypeId + "' ");
		}
		if (ruleContent != null) {
			builderCountSql.append(" and r.r_content like '%" + ruleContent
					+ "%' ");
			builderQuerySql.append(" and r.r_content like '%" + ruleContent
					+ "%' ");
		}

		builderQuerySql.append(" order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset);

		//获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderQuerySql.toString());
		List list = query.list();
		
		Query countQuery = session.createSQLQuery(builderCountSql.toString());
		Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
		session.close();
		
		
		List<NmsRuleItem> nmsRuleItemList = new ArrayList<NmsRuleItem>();

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				NmsRuleItem ruleItem = new NmsRuleItem();
				ruleItem.setId((Integer) objects[0]);
				ruleItem.setATypeId((Integer) objects[1]);
				ruleItem.setChtype((String) objects[2]);
				ruleItem.setChSubType((String) objects[3]);
				ruleItem.setrContent((String) objects[4]);
				if (objects[5] == null) {
					ruleItem.setrUnit("无");
				} else {
					ruleItem.setrUnit((String) objects[5]);
				}
				ruleItem.setrEnable((Integer) objects[6]);
				ruleItem.setrValue1(Long.valueOf(objects[7].toString()));
				ruleItem.setrValue2(Long.valueOf(objects[8].toString()));
				ruleItem.setrValue3(Long.valueOf(objects[9].toString()));
				ruleItem.setItime((Timestamp) objects[10]);
				nmsRuleItemList.add(ruleItem);
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
		PageBean<NmsRuleItem> pageBean = new PageBean<NmsRuleItem>();
		//pageBean.setOrderKey(orderKey);
		//pageBean.setOrderValue(orderValue);
		pageBean.setTotalPage(totalPage);
		pageBean.setList(nmsRuleItemList);
		pageBean.setTotalCount(totalCount.intValue());
		pageBean.setLimit(offset);
		pageBean.setPage(begin);
		return pageBean;
	}

	public NmsRuleItem findNmsRuleById(Integer id) {
		// 获取count总数
		String querySql = "select r.id,r.atype_id,t.ch_type,t.ch_subtype,r.r_content,r.r_unit,r.r_enable,r.r_value1,r.r_value2,r.r_value3,r.itime,r.r_seq"
				+ " from nms_rule r left join nms_asset_type t on r.atype_id = t.id"
				+ " where 1 = 1 and r.id = " + id;
		StringBuilder builderQuerySql = new StringBuilder(querySql);

		//获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderQuerySql.toString());
		List list = query.list();
		session.close();
		
		Object[] objects = (Object[]) list.get(0);
		NmsRuleItem ruleItem = new NmsRuleItem();
		ruleItem.setId((Integer) objects[0]);
		ruleItem.setATypeId((Integer) objects[1]);
		ruleItem.setChtype((String) objects[2]);
		ruleItem.setChSubType((String) objects[3]);
		ruleItem.setrContent((String) objects[4]);
		if (objects[5] == null) {
			ruleItem.setrUnit("无");
		} else {
			ruleItem.setrUnit((String) objects[5]);
		}
		ruleItem.setrEnable((Integer) objects[6]);
		ruleItem.setrValue1(Long.valueOf(objects[7].toString()));
		ruleItem.setrValue2(Long.valueOf(objects[8].toString()));
		ruleItem.setrValue3(Long.valueOf(objects[9].toString()));
		ruleItem.setItime((Timestamp) objects[10]);
		ruleItem.setrSeq((Integer) objects[11]);


		return ruleItem;
	}

	public boolean updateNmsRuleById(Integer id, Integer rEnable, Integer rSeq,
			Integer rValue1, Integer rValue2, Integer rValue3) {
		boolean res = true;
		String sql = "UPDATE nms_rule SET r_enable = " + rEnable + ", r_seq = "
				+ rSeq + ", r_value1 = " + rValue1 + ", r_value2 = " + rValue2
				+ ", r_value3 = " + rValue3 + " WHERE id = " + id;
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

	public boolean add(NmsRule nmsRule) {
		try {
			nmsRule.setId((int) 0);
			hibernateTemplate.save(nmsRule);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean update(NmsRule nmsRule) {
		try {
			hibernateTemplate.saveOrUpdate(nmsRule);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean deleteNmsRuleByAssetId(int asset_id) {
		boolean res = true;
		String sql = "delete from nms_rule_asset where asset_id = " + asset_id;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		try {
			int num = session.createSQLQuery(sql).executeUpdate();
			System.out.println("[DEBUG] delete from nms_rule_asset where asset_id = " + asset_id + "共 " + num + " 条记录");
		} catch (Exception e) {
			res = false;
		}
		tran.commit();
		session.close();
		return res;
	}	
}
