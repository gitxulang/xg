package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsRule;
import iie.pojo.NmsRuleAsset;
import iie.tools.NmsRuleAssetItem;
import iie.tools.NmsRuleItem;
import iie.tools.PageBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsRuleAssetService")
public class NmsRuleAssetService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Autowired
	NmsRuleService nmsRuleService;

	public boolean add(Integer assetId, Integer assetTypeId) {
		NmsRuleAsset nra = new NmsRuleAsset();
		try {
			List<NmsRule> rules = nmsRuleService
					.findNmsRulesByAssetTypeId(assetTypeId);
			for (NmsRule r : rules) {
				nra.setId((int) 0);
				nra.setDType(r.getDType());
				nra.setItime(new Timestamp(System.currentTimeMillis()));
				NmsAsset nmsAsset = new NmsAsset();
				nmsAsset.setId(assetId);
				nra.setNmsAsset(nmsAsset);
				NmsAssetType nmsAssetType = new NmsAssetType();
				nmsAssetType.setId(assetTypeId);
				nra.setNmsAssetType(nmsAssetType);
				nra.setRContent(r.getRContent());
				nra.setREnable(r.getREnable());
				nra.setRName(r.getRName());
				nra.setRSeq(r.getRSeq());
				nra.setRUnit(r.getRUnit());
				nra.setRValue1(r.getRValue1().intValue());
				nra.setRValue2(r.getRValue2().intValue());
				nra.setRValue3(r.getRValue3().intValue());
				hibernateTemplate.save(nra);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public PageBean<NmsRuleAssetItem> findNmsRuleAsset(String ip,
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
				+ " from nms_rule_asset ra left join nms_asset_type t on ra.atype_id = t.id left join nms_asset a on ra.asset_id = a.id"
				+ " where 1 = 1";
		String querySql = "select ra.id,ra.atype_id,ra.asset_id,a.a_name,a.a_ip,t.ch_type,t.ch_subtype,ra.r_content,ra.r_unit,ra.r_enable,ra.r_value1,ra.r_value2,ra.r_value3,ra.itime"
				+ " from nms_rule_asset ra left join nms_asset_type t on ra.atype_id = t.id left join nms_asset a on ra.asset_id = a.id"
				+ " where 1 = 1";
		
		
		StringBuilder builderCountSql = new StringBuilder(countSql);
		StringBuilder builderQuerySql = new StringBuilder(querySql);
		if (assetTypeId >= 0) {
			builderCountSql.append(" and ra.atype_id = '" + assetTypeId + "' ");
			builderQuerySql.append(" and ra.atype_id = '" + assetTypeId + "' ");
		}
		if (ruleContent != null) {
			builderCountSql.append(" and ra.r_content like '%" + ruleContent
					+ "%' ");
			builderQuerySql.append(" and ra.r_content like '%" + ruleContent
					+ "%' ");
		}
		if (ip != null) {
			builderCountSql.append(" and a.a_ip like '%" + ip + "%' ");
			builderQuerySql.append(" and a.a_ip like '%" + ip + "%' ");
		}
		builderQuerySql.append(" order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset);

		//获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderQuerySql.toString());
		List list = query.list();
		Query countQuery = session.createSQLQuery(builderCountSql.toString());
		Long totalCount = Long.valueOf(countQuery.list().get(0).toString());
		session.close();
		
		List<NmsRuleAssetItem> nmsRuleAssetItemList = new ArrayList<NmsRuleAssetItem>();

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				NmsRuleAssetItem ruleAssetItem = new NmsRuleAssetItem();
				ruleAssetItem.setId((Integer) objects[0]);
				ruleAssetItem.setATypeId((Integer) objects[1]);
				ruleAssetItem.setAssetId((Integer) objects[2]);
				ruleAssetItem.setAName((String) objects[3]);
				ruleAssetItem.setAIp((String) objects[4]);
				ruleAssetItem.setChtype((String) objects[5]);
				ruleAssetItem.setChSubType((String) objects[6]);
				ruleAssetItem.setrContent((String) objects[7]);
				if (objects[8] == null) {
					ruleAssetItem.setrUnit("无");
				} else {
					ruleAssetItem.setrUnit((String) objects[8]);
				}
				ruleAssetItem.setrEnable((Integer) objects[9]);
				ruleAssetItem.setrValue1(Long.valueOf(objects[10].toString()));
				ruleAssetItem.setrValue2(Long.valueOf(objects[11].toString()));
				ruleAssetItem.setrValue3(Long.valueOf(objects[12].toString()));
				ruleAssetItem.setItime((Timestamp) objects[13]);
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
		PageBean<NmsRuleAssetItem> pageBean = new PageBean<NmsRuleAssetItem>();
		//pageBean.setOrderKey(orderKey);
		//pageBean.setOrderValue(orderValue);
		pageBean.setTotalPage(totalPage);
		pageBean.setList(nmsRuleAssetItemList);
		pageBean.setTotalCount(totalCount.intValue());
		pageBean.setLimit(offset);
		pageBean.setPage(begin);
		return pageBean;
	}

	public NmsRuleAssetItem findNmsRuleAssetById(Integer id) {

		String querySql = "select ra.id,ra.atype_id,ra.asset_id,a.a_name,a.a_ip,t.ch_type,t.ch_subtype,ra.r_content,ra.r_unit,ra.r_enable,ra.r_value1,ra.r_value2,ra.r_value3,ra.itime,ra.r_seq"
				+ " from nms_rule_asset ra left join nms_asset_type t on ra.atype_id = t.id left join nms_asset a on ra.asset_id = a.id"
				+ " where 1 = 1 and ra.id = " + id;
		StringBuilder builderQuerySql = new StringBuilder(querySql);

		//获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderQuerySql.toString());
		List list = query.list();
		session.close();

		Object[] objects = (Object[]) list.get(0);
		NmsRuleAssetItem ruleAssetItem = new NmsRuleAssetItem();
		ruleAssetItem.setId((Integer) objects[0]);
		ruleAssetItem.setATypeId((Integer) objects[1]);
		ruleAssetItem.setAssetId((Integer) objects[2]);
		ruleAssetItem.setAName((String) objects[3]);
		ruleAssetItem.setAIp((String) objects[4]);
		ruleAssetItem.setChtype((String) objects[5]);
		ruleAssetItem.setChSubType((String) objects[6]);
		ruleAssetItem.setrContent((String) objects[7]);
		if (objects[8] == null) {
			ruleAssetItem.setrUnit("无");
		} else {
			ruleAssetItem.setrUnit((String) objects[8]);
		}
		ruleAssetItem.setrEnable((Integer) objects[9]);
		ruleAssetItem.setrValue1(Long.valueOf(objects[10].toString()));
		ruleAssetItem.setrValue2(Long.valueOf(objects[11].toString()));
		ruleAssetItem.setrValue3(Long.valueOf(objects[12].toString()));
		ruleAssetItem.setItime((Timestamp) objects[13]);
		ruleAssetItem.setrSeq((Integer) objects[14]);


		return ruleAssetItem;
	}
	
	public boolean updateNmsRuleAssetById(Integer id, Integer rEnable, Integer rSeq,
			Integer rValue1, Integer rValue2, Integer rValue3) {
		boolean res = true;
		String sql = "UPDATE nms_rule_asset SET r_enable = " + rEnable + ", r_seq = "
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
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_rule_asset where asset_id = " + assetId;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		try {
			session.createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			res = false;
		}
		session.close();
		return res;	
	}	
	
}
