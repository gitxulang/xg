package iie.service;

import iie.pojo.*;
import iie.tools.DateSqlUtils;
import iie.tools.Menu;
import iie.tools.MenuNode;
import iie.tools.NmsSystemDetail;
import iie.tools.PageBean;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("NmsAssetService")
@Transactional
public class NmsAssetService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Autowired
	NmsRuleService nmsRuleService;

	public boolean saveAssetAndRuleAsset(NmsAsset asset) {
		boolean res = true;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		try {
			asset.setId(0);
			asset.setDeled(0);
			asset.setItime(new Timestamp(System.currentTimeMillis()));
			session.save(asset);

			// 生成资产告警规则
			NmsAssetType type = asset.getNmsAssetType();
			List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(type
					.getId());
			for (NmsRule rule : rules) {
				NmsRuleAsset ruleAsset = new NmsRuleAsset();
				ruleAsset.setId(0);
				ruleAsset.setItime(new Timestamp(System.currentTimeMillis()));
				ruleAsset.setNmsAsset(asset);
				ruleAsset.setNmsAssetType(type);
				ruleAsset.setDType(rule.getDType());
				ruleAsset.setRName(rule.getRName());
				ruleAsset.setRContent(rule.getRContent());
				ruleAsset.setRUnit(rule.getRUnit());
				ruleAsset.setRSeq(rule.getRSeq());
				ruleAsset.setREnable(rule.getREnable());
				ruleAsset.setRValue1(rule.getRValue1().intValue());
				ruleAsset.setRValue2(rule.getRValue2().intValue());
				ruleAsset.setRValue3(rule.getRValue3().intValue());
				session.save(ruleAsset);
			}
			tran.commit();
		} catch (Exception e) {
			res = false;
		} finally {
			session.close();
		}
		return res;
	}
	
	
	public boolean updateRuleAsset(NmsAsset asset) {
		boolean res = true;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		int asset_id = asset.getId();
		try {
			// 删除asset_id已经存在的规则记录
			if (!nmsRuleService.deleteNmsRuleByAssetId(asset_id)) {
				System.out.println("[ERROE] delete from nms_rule_asset where asset_id = " + asset_id + "失败");
				return false;
			}
			
			// 生成资产告警规则
			NmsAssetType type = asset.getNmsAssetType();
			List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(type.getId());
			for (NmsRule rule : rules) {
				NmsRuleAsset ruleAsset = new NmsRuleAsset();
				ruleAsset.setId(0);
				ruleAsset.setItime(new Timestamp(System.currentTimeMillis()));
				ruleAsset.setNmsAsset(asset);
				ruleAsset.setNmsAssetType(type);
				ruleAsset.setDType(rule.getDType());
				ruleAsset.setRName(rule.getRName());
				ruleAsset.setRContent(rule.getRContent());
				ruleAsset.setRUnit(rule.getRUnit());
				ruleAsset.setRSeq(rule.getRSeq());
				ruleAsset.setREnable(rule.getREnable());
				ruleAsset.setRValue1(rule.getRValue1().intValue());
				ruleAsset.setRValue2(rule.getRValue2().intValue());
				ruleAsset.setRValue3(rule.getRValue3().intValue());
				session.save(ruleAsset);
			}
			tran.commit();
		} catch (Exception e) {
			System.out.println("[ERROE] delete from nms_rule_asset where asset_id = " + asset_id + "异常");
			res = false;
		} finally {
			session.close();
		}
		return res;
	}	
	

	@SuppressWarnings("unchecked")
	public NmsDepartment findDepartmentByName(String name) {
		String hsql = "from NmsDepartment nd where nd.DName = ?";
		List<NmsDepartment> list = (List<NmsDepartment>) hibernateTemplate
				.find(hsql, name);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NmsDepartment findDepartmentAll() {
		String hsql = "from NmsDepartment nd where nd.deled = 0";
		List<NmsDepartment> list = (List<NmsDepartment>) hibernateTemplate.find(hsql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NmsAssetType findTypeByName(String type, String subType) {
		String hsql = "from NmsAssetType nat where nat.chType = ? and nat.chSubtype = ?";
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate.find(
				hsql, type, subType);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
	//	return new NmsAssetType();
		return null;
	}

	@SuppressWarnings("unchecked")
	public int findAll() {
		int count = 0;
		String hsql = "from NmsAsset na where na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql);
		if (list != null && list.size() > 0) {
			count = list.size();
		}
		return count;
	}
	
	
	@SuppressWarnings("unchecked")
	public NmsAsset findByIp(String ip) {
		String hsql = "from NmsAsset na where na.AIp = ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, ip);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NmsAsset findByIpAndType(String ip, String type) {
		String hsql = "from NmsAsset na where na.AIp = ? and na.nmsAssetType.chType != ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, ip, type);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NmsAsset findByANo(String no) {
		String hsql = "from NmsAsset na where na.ANo = ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, no);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}	

	@SuppressWarnings("unchecked")
	public NmsAsset findById(int id) {
		String hsql = "from NmsAsset na where na.id = ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NmsAsset findByIdAny(int id) {
		String hsql = "from NmsAsset na where na.id = ?";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAsset();
	}

	@SuppressWarnings("unchecked")
	public NmsDepartment findDepartmentById(String id) {
		String hsql = "from NmsDepartment nd where nd.id = ?";
		List<NmsDepartment> list = (List<NmsDepartment>) hibernateTemplate
				.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsDepartment();
	}

	@SuppressWarnings("unchecked")
	public NmsAssetType findTypeById(int id) {
		String hsql = "from NmsAssetType na where na.id = ?";
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate.find(
				hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAssetType();
	}

	public boolean saveAsset(NmsAsset asset) {

		try {
			asset.setId(0);
			asset.setDeled(0);
			asset.setItime(new Timestamp(System.currentTimeMillis()));
			hibernateTemplate.save(asset);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public boolean updateAsset(NmsAsset asset) {
		try {
			asset.setItime(new Timestamp(System.currentTimeMillis()));
			hibernateTemplate.saveOrUpdate(asset);
		} catch (Exception e) {
			e.toString();
			return false;
		}
		return true;
	}

	public boolean deleteAsset(int id) {
		NmsAsset asset = findById(id);
		if (asset != null) {
			asset.setDeled(1);
			return updateAsset(asset);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<NmsAsset> queryList(String depertmentName, String chType,
			Integer currentPage, Integer pageNum) {

		Integer pageStart = (currentPage - 1) * pageNum;
		// 查询部门ID
		List<NmsDepartment> nmsDepartments = new ArrayList<>();
		if (depertmentName != "") {
			nmsDepartments = (List<NmsDepartment>) hibernateTemplate.find(
					"from NmsDepartment where DName = ?",
					new Object[] { depertmentName });
		}
		// 查询设备类型ID
		List<NmsAssetType> nmsAssetTypes = new ArrayList<>();
		if (chType != "") {
			nmsAssetTypes = (List<NmsAssetType>) hibernateTemplate.find(
					"from NmsAssetType where chType= ?",
					new Object[] { chType });
		}
		// 根据条件查询设备列表(分页)
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);
		if (nmsDepartments != null && nmsDepartments.size() > 0) {
			criteria.add(Restrictions.in("nmsDepartment",
					nmsDepartments.toArray()));
		}
		if (nmsAssetTypes != null && nmsAssetTypes.size() > 0) {
			criteria.add(Restrictions.in("nmsAssetType",
					nmsAssetTypes.toArray()));
		}
		List result = hibernateTemplate.findByCriteria(criteria, pageStart,
				pageNum);
		return result;
	}

	public Map<String, Object> queryTotal(String depertmentName, String chType,
			Integer pageNum) {
		Map<String, Object> result = new HashMap<>(2);

		List<NmsDepartment> nmsDepartments = new ArrayList<>();
		if (depertmentName != "") {
			nmsDepartments = (List<NmsDepartment>) hibernateTemplate.find(
					"from NmsDepartment where DName = ?",
					new Object[] { depertmentName });
			if (nmsDepartments == null || nmsDepartments.size() == 0) {
				result.put("sumData", 0);
				result.put("sumPage", 0);
				return result;
			}
		}
		List<NmsAssetType> nmsAssetTypes = new ArrayList<>();
		if (chType != "") {
			nmsAssetTypes = (List<NmsAssetType>) hibernateTemplate.find(
					"from NmsAssetType where chType= ?",
					new Object[] { chType });
			if (nmsAssetTypes == null || nmsAssetTypes.size() == 0) {
				result.put("sumData", 0);
				result.put("sumPage", 0);
				return result;
			}
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);
		if (nmsDepartments != null && nmsDepartments.size() > 0) {
			criteria.add(Restrictions.in("nmsDepartment",
					nmsDepartments.toArray()));
		}
		if (nmsAssetTypes != null && nmsAssetTypes.size() > 0) {
			criteria.add(Restrictions.in("nmsAssetType",
					nmsAssetTypes.toArray()));
		}
		List list = hibernateTemplate.findByCriteria(criteria);
		if (nmsAssetTypes != null && nmsAssetTypes.size() > 0) {
			int totalCount = list.size();

			result.put("sumData", totalCount);
			result.put("sumPage", totalCount % pageNum == 0 ? totalCount
					/ pageNum : (totalCount / pageNum) + 1);
		}
		return result;
	}

	public List queryData() {
		String nmsDepartmentSQL = "from NmsDepartment";
		String nmsAssetTypeSQL = "from NmsAssetType";

		List<NmsDepartment> departments = (List<NmsDepartment>) hibernateTemplate
				.find(nmsDepartmentSQL);
		List<NmsAssetType> nmsAssetTypes = (List<NmsAssetType>) hibernateTemplate
				.find(nmsAssetTypeSQL);

		Map<String, List<NmsAssetType>> nmsAssetTypesMap = new HashMap<>(2);
		if (nmsAssetTypes != null && nmsAssetTypes.size() > 0) {
			for (NmsAssetType nmsAssetType : nmsAssetTypes) {
				String chType = nmsAssetType.getChType();
				List<NmsAssetType> nmsAssetTypeIds = nmsAssetTypesMap
						.get(chType);
				if (nmsAssetTypeIds == null) {
					nmsAssetTypeIds = new ArrayList<>();
				}
				nmsAssetTypeIds.add(nmsAssetType);
				nmsAssetTypesMap.put(chType, nmsAssetTypeIds);
			}
		}
		List list = new ArrayList();
		if (departments != null && departments.size() > 0) {
			for (NmsDepartment nmsDepartment : departments) {
				Map<String, Map<String, Object>> nmsDepartMap = new HashMap<>(2);
				Map<String, Object> assetTypeMap = new HashMap<>(2);
				for (Map.Entry<String, List<NmsAssetType>> entry : nmsAssetTypesMap
						.entrySet()) {
					List<NmsAssetType> nmsAssetTypesList = entry.getValue();
					DetachedCriteria criteria = DetachedCriteria
							.forClass(NmsAsset.class);
					criteria.add(
							Restrictions.in("nmsAssetType",
									nmsAssetTypesList.toArray())).add(
							Restrictions.eq("nmsDepartment", nmsDepartment));
					List assetList = hibernateTemplate.findByCriteria(criteria);
					String key = entry.getKey();
					assetTypeMap.put(key, assetList.size());
				}
				nmsDepartMap.put(nmsDepartment.getDName(), assetTypeMap);
				list.add(nmsDepartMap);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsAsset> getPageByConditionDate(String orderKey,
			int orderValue, String startDate, String endDate,
			String nmsAssetKey, String nmsAssetValue, String nmsDepartmentKey,
			String nmsDepartmentValue, String nmsAssetTypeKey,
			String nmsAssetTypeValue, int begin, int offset,String nmsAssetValueIp, String nmsAssetValueBmIp, String nmsAssetValueYwIp, String nmsAssetValueEquip,String nmsAssetValueDeveiceId) throws Exception {

		// 加载本类
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);
		DetachedCriteria criteriaForCount = DetachedCriteria.forClass(NmsAsset.class);

		// 设定基础条件（部分可选）
		criteria.add(Restrictions.eq("deled", 0));// 是否已删除
		criteriaForCount.add(Restrictions.eq("deled", 0));// 是否已删除
		if (startDate != null && endDate != null) {
			criteria.add(Restrictions.ge("itime", Date.valueOf(startDate)));// 入库日期限制（可选）
			criteria.add(Restrictions.le("itime", Date.valueOf(endDate)));// 入库日期限制（可选）
			criteriaForCount.add(Restrictions.ge("itime", Date.valueOf(startDate)));// 入库日期限制（可选）
			criteriaForCount.add(Restrictions.le("itime", Date.valueOf(endDate)));// 入库日期限制（可选）
		}

		// 排序条件限制（可选）
		if (orderKey == null) {
			orderKey = "id";
		}
		if (orderValue == 0) {
			criteria.addOrder(Order.desc(orderKey));
			criteriaForCount.addOrder(Order.desc(orderKey));
		} else {
			criteria.addOrder(Order.asc(orderKey));
			criteriaForCount.addOrder(Order.asc(orderKey));
		}

		// 设定本类条件（可选）
		if (nmsAssetKey != null) {
			if (nmsAssetKey.equals("colled") || nmsAssetKey.equals("colledMode")) {
				// 数字类型用eq方法加入
				criteria.add(Restrictions.eq(nmsAssetKey, nmsAssetValue));
				criteriaForCount.add(Restrictions
						.eq(nmsAssetKey, nmsAssetValue));
			} else { 
				// 字符串类型用like方法加入
				criteria.add(Restrictions.like(nmsAssetKey, "%" + nmsAssetValue + "%"));
				criteriaForCount.add(Restrictions.like(nmsAssetKey, "%" + nmsAssetValue + "%"));
			}
		}
		if (StringUtils.isNotBlank(nmsAssetValueIp)) {
        	nmsAssetValueIp = StringEscapeUtils.escapeSql(nmsAssetValueIp);
        	criteria.add(Restrictions.like("AIp", "%" + nmsAssetValueIp
					+ "%"));
			criteriaForCount.add(Restrictions.like("AIp", "%"
					+ nmsAssetValueIp + "%"));
        }

		if (StringUtils.isNotBlank(nmsAssetValueBmIp)) {
			nmsAssetValueBmIp = StringEscapeUtils.escapeSql(nmsAssetValueBmIp);
        	criteria.add(Restrictions.like("BmIp", "%" + nmsAssetValueBmIp
					+ "%"));
			criteriaForCount.add(Restrictions.like("BmIp", "%"
					+ nmsAssetValueBmIp + "%"));
        }
		
		if (StringUtils.isNotBlank(nmsAssetValueYwIp)) {
			nmsAssetValueYwIp = StringEscapeUtils.escapeSql(nmsAssetValueYwIp);
        	criteria.add(Restrictions.like("YwIp", "%" + nmsAssetValueYwIp
					+ "%"));
			criteriaForCount.add(Restrictions.like("YwIp", "%"
					+ nmsAssetValueYwIp + "%"));
        }
		
        if (StringUtils.isNotBlank(nmsAssetValueEquip)) {
        	nmsAssetValueEquip = StringEscapeUtils.escapeSql(nmsAssetValueEquip);
        	criteria.add(Restrictions.like("AName", "%" + nmsAssetValueEquip
					+ "%"));
			criteriaForCount.add(Restrictions.like("AName", "%"
					+ nmsAssetValueEquip + "%"));
        }
        
        if (StringUtils.isNotBlank(nmsAssetValueDeveiceId)) {
        	nmsAssetValueDeveiceId = StringEscapeUtils.escapeSql(nmsAssetValueDeveiceId);
        	criteria.add(Restrictions.like("ANo", "%" + nmsAssetValueDeveiceId
					+ "%"));
			criteriaForCount.add(Restrictions.like("ANo", "%"
					+ nmsAssetValueDeveiceId + "%"));
        }

		// 设定关联类NmsDepartment条件（可选）
		if (nmsDepartmentKey != null) {
			// 这个类中没有数字作为条件的字段
			DetachedCriteria criteriaDepartment = DetachedCriteria.forClass(NmsDepartment.class);
			criteriaDepartment.add(Restrictions.like(nmsDepartmentKey, "%" + nmsDepartmentValue + "%"));
			criteriaDepartment.add(Restrictions.eq("deled", 0));
			criteriaDepartment.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteria.add(Property.forName("nmsDepartment").in(criteriaDepartment));

			DetachedCriteria criteriaDepartmentForCount = DetachedCriteria.forClass(NmsDepartment.class);
			criteriaDepartmentForCount.add(Restrictions.like(nmsDepartmentKey, "%" + nmsDepartmentValue + "%"));
			criteriaDepartmentForCount.add(Restrictions.eq("deled", 0));
			criteriaDepartmentForCount.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteriaForCount.add(Property.forName("nmsDepartment").in(criteriaDepartmentForCount));
		}

		// 设定关联类NmsAssetType条件（可选）
		if (nmsAssetTypeKey != null) {
			// 这个类中没有数字作为条件的字段
			DetachedCriteria criteriaAssetType = DetachedCriteria
					.forClass(NmsAssetType.class);
			criteriaAssetType.add(Restrictions.like(nmsAssetTypeKey, "%"
					+ nmsAssetTypeValue + "%"));
			criteriaAssetType.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteria.add(Property.forName("nmsAssetType").in(criteriaAssetType));

			DetachedCriteria criteriaAssetTypeForCount = DetachedCriteria
					.forClass(NmsAssetType.class);
			criteriaAssetTypeForCount.add(Restrictions.like(nmsAssetTypeKey,
					"%" + nmsAssetTypeValue + "%"));
			criteriaAssetTypeForCount.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteriaForCount.add(Property.forName("nmsAssetType").in(
					criteriaAssetTypeForCount));

		}

		// 获取数据总数
		int totalCount = ((Long) criteriaForCount
				.setProjection(Projections.rowCount())
				.getExecutableCriteria(
						this.hibernateTemplate.getSessionFactory()
								.getCurrentSession()).uniqueResult())
				.intValue();

		// 分页查找所需数据
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate
				.findByCriteria(criteria, (begin - 1) * offset, offset);

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
		PageBean<NmsAsset> page = new PageBean<NmsAsset>();
		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
		page.setTotalCount(totalCount);
		page.setPage(begin);
		page.setTotalPage(totalPage);
		String key = null;
		String value = null;
		page.setKey(key);
		page.setValue(value);
		page.setList(list);

		return page;

	}
	
	@SuppressWarnings("unchecked")
	public NmsAsset loadLastAsset() throws Exception {

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_asset order by id desc limit 0,1";
		SQLQuery query = session.createSQLQuery(sql);
		List list  = query.list();
		session.close();
		
		if(list!=null&&list.size()>0){
			Object[] data = (Object[]) list.get(0);
			NmsAsset asset = new NmsAsset();
			asset.setId((Integer) data[0]);
			asset.setAIp((String) data[1]);
			asset.setBmIp((String) data[2]);
			asset.setYwIp((String) data[3]);
			asset.setAName((String) data[4]);
			asset.setANo((String) data[5]);
			asset.setOnline((Integer) data[7]);
			asset.setAPos((String) data[8]);
			asset.setAManu((String) data[9]);
			asset.setADate((String) data[10]);
			asset.setAUser((String) data[11]);
			asset.setAuthPass((String) data[12]);
			return asset;
		}
		else {
			return new NmsAsset();
		}
		
	}
	
	public Map<String, Object> getCount(String startDate, String endDate,
			String nmsAssetKey, String nmsAssetValue, String nmsDepartmentKey,
			String nmsDepartmentValue, String nmsAssetTypeKey,
			String nmsAssetTypeValue, int offset) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 加载本类
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);

		// 设定基础条件（部分可选）
		criteria.add(Restrictions.eq("deled", 0));// 是否已删除
		if (startDate != null && startDate.length() > 0 && endDate != null
				&& endDate.length() > 0) {
			criteria.add(Restrictions.ge("itime", Date.valueOf(startDate)));// 入库日期限制（可选）
			criteria.add(Restrictions.le("itime", Date.valueOf(endDate)));// 入库日期限制（可选）
		}

		// 设定本类条件（可选）
		if (nmsAssetKey != null && nmsAssetKey.length() > 0) {
			if (nmsAssetKey.equals("colled")
					|| nmsAssetKey.equals("colledMode")) {// 数字类型用eq()方法加入
				Integer nav = Integer.valueOf(nmsAssetValue);
				criteria.add(Restrictions.eq(nmsAssetKey, nav));
			} else { // 字符串类型用like()方法加入
				criteria.add(Restrictions.like(nmsAssetKey, "%" + nmsAssetValue
						+ "%"));
			}
		}

		// 设定关联类NmsDepartment条件（可选）
		if (nmsDepartmentKey != null && nmsDepartmentKey.length() > 0) {
			// 这个类中没有数字作为条件的字段
			criteria.createAlias("nmsDepartment", "nmsDepartment");
			nmsDepartmentKey = "nmsDepartment." + nmsDepartmentKey;
			criteria.add(Restrictions.like(nmsDepartmentKey, "%"
					+ nmsDepartmentValue + "%"));
			criteria.add(Restrictions.eq("nmsDepartment.deled", 0));
		}

		// 设定关联类NmsAssetType条件（可选）
		if (nmsAssetTypeKey != null && nmsAssetTypeKey.length() > 0) {
			// 这个类中没有数字作为条件的字段
			criteria.createAlias("nmsAssetType", "nmsAssetType");
			nmsAssetTypeKey = "nmsAssetType." + nmsAssetTypeKey;
			criteria.add(Restrictions.like(nmsAssetTypeKey, "%"
					+ nmsAssetTypeValue + "%"));
		}

		// 获取总条数
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria)
				.get(0);

		// 计算总页数
		int totalPage = 1;
		if (totalCount == 0) {
			totalPage = totalCount.intValue() / offset + 1;
		} else if (totalCount % offset == 0) {
			totalPage = totalCount.intValue() / offset;
		} else {
			totalPage = totalCount.intValue() / offset + 1;
		}

		result.put("sumData", totalCount);
		result.put("sumPage", totalPage);
		return result;
	}

	public List<Menu> getMenuList() throws Exception {

		List<Menu> menuList = new ArrayList<Menu>(0);

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String nmsDepartmentSQL = "select id,d_name from nms_department where deled=0";
		String nmsAssetTypeNameSQL = "select distinct ch_type from nms_asset_type";
		
		// 查询出全部部门的ID、DNAME
		SQLQuery nmsDepartmentSQLQuery = session.createSQLQuery(nmsDepartmentSQL);
		List nmsDepartmentList = nmsDepartmentSQLQuery.list();
		
		// 查询出所有资产类型
		SQLQuery nmsAssetTypeNameSQLQuery = session.createSQLQuery(nmsAssetTypeNameSQL);
		List<String> nmsAssetTypeNameList = nmsAssetTypeNameSQLQuery.list();
		
		

		if (nmsDepartmentList == null || nmsDepartmentList.size() == 0) {
			return menuList;
		}

		if (nmsAssetTypeNameList == null || nmsAssetTypeNameList.size() == 0) {
			return menuList;
		}

		// 遍历查询所有部门下的资产类型数量
		if (nmsDepartmentList != null && nmsDepartmentList.size() > 0) {
			for (int i = 0; i < nmsDepartmentList.size(); i++) {
				Object[] list = (Object[]) nmsDepartmentList.get(i);
				Integer dptId = (Integer) list[0];
				String dName = (String) list[1];
				Menu menu = new Menu(dName, getMenuNodeList(dptId,
						nmsAssetTypeNameList, session));
				menuList.add(menu);
			}
		}
		
		session.close();
		

		return menuList;
	}

	private List<MenuNode> getMenuNodeList(Integer deptId,
			List<String> chTypeList, Session session) {
		// 添加MAP校验不存在的资产类型
		Map<String, Object> map = new HashMap<String, Object>();

		List<MenuNode> nodeList = new ArrayList<MenuNode>();
		String nmsAssetSQL = "select nat.ch_type,count(na.id) from nms_asset_type nat"
				+ "  left join nms_asset na on nat.id = na.type_id where na.deled=0 and na.dept_id =:deptId"
				+ "  group by nat.ch_type ";
		// 统计部门所有资产类型数量
		SQLQuery nmsAssetSQLQuery = session.createSQLQuery(nmsAssetSQL);
		nmsAssetSQLQuery.setInteger("deptId", deptId);
		List nmsAssetList = nmsAssetSQLQuery.list();
		// 组装数据
		if (nmsAssetList != null && nmsAssetList.size() > 0) {
			for (int i = 0; i < nmsAssetList.size(); i++) {
				Object[] list = (Object[]) nmsAssetList.get(i);
				String chType = (String) list[0];
				int count = ((BigInteger) list[1]).intValue();
				map.put(chType, 1);
				MenuNode menuNode = new MenuNode(chType, count);
				nodeList.add(menuNode);
			}
		}
		// 组装资产类型数量为零的数据
		for (String chTypeKey : chTypeList) {
			if (!map.containsKey(chTypeKey)) {
				MenuNode menuNode = new MenuNode(chTypeKey, 0);
				nodeList.add(menuNode);
			}
		}
		List<MenuNode> nodeListSort = new ArrayList<MenuNode>();
		for (String chTypeKey : chTypeList) {
		//	System.out.println(chTypeKey + "________________________________");
			if (nodeList != null && nodeList.size() > 0) {
				for (int i = 0; i < nodeList.size(); i++) {
				//	System.out.println(nodeList.get(i).getTypeName());
					if (nodeList.get(i).getTypeName().equals(chTypeKey)) {
						nodeListSort.add(nodeList.get(i));
						break;
					}
				}
			}
		}
		return nodeListSort;
	}

	public List getTypeAssetAlarmList() {

		Session session = hibernateTemplate.getSessionFactory().openSession();

		String sql = "select type.ch_type, COUNT(DISTINCT asset.id), COUNT(DISTINCT alarm.asset_id)"
				+ " FROM (nms_asset_type AS type LEFT JOIN nms_asset AS asset ON type.id = asset.type_id AND asset.deled = 0) LEFT JOIN nms_alarm AS alarm ON asset.id = alarm.asset_id"
				+ " GROUP BY type.ch_type";

		// 查询出所有资产类型
		SQLQuery query = session.createSQLQuery(sql);
		List res = query.list();
		session.close();

		return res;
	}
	
	public List getTypeAssetAlarmDepartmentList(String deptId) {

		Session session = hibernateTemplate.getSessionFactory().openSession();

		String sql = "select type.ch_type, COUNT(DISTINCT asset.id), COUNT(DISTINCT alarm.asset_id)"
				+ " FROM (nms_asset_type AS type LEFT JOIN nms_asset AS asset ON type.id = asset.type_id AND asset.deled = 0) LEFT JOIN nms_department dept ON asset.dept_id = dept.id LEFT JOIN nms_alarm AS alarm ON asset.id = alarm.asset_id"
				+ " WHERE asset.dept_id = "+ deptId
				+ " GROUP BY type.ch_type";

		// 查询出所有资产类型
		SQLQuery query = session.createSQLQuery(sql);
		List res = query.list();
		session.close();
	
		if(res != null&&res.size() > 0){
			return res;
		} else{
			res.add(-1);
			return res;
		}
	}

	public List getTypeAssetList() {

		Session session = hibernateTemplate.getSessionFactory().openSession();

		String sql = "select type.ch_type, COUNT(DISTINCT asset.id)"
				+ " FROM nms_asset_type AS type LEFT JOIN nms_asset AS asset ON type.id = asset.type_id AND asset.deled = 0"
				+ " GROUP BY type.ch_type";

		// 查询出所有资产类型
		SQLQuery query = session.createSQLQuery(sql);
		List res = query.list();
		session.close();

		return res;
	}

	public PageBean<NmsAsset> getPage(String orderKey, int orderValue,
			String startDate, String endDate, String nmsAssetKey,
			String nmsAssetValue, String nmsDepartmentKey,
			String nmsDepartmentValue, String nmsAssetTypeKey,
			String nmsAssetTypeValue, int begin, int offset) throws Exception {

		// 加载本类
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);

		// 设定基础条件（部分可选）
		criteria.add(Restrictions.eq("deled", 0));// 是否已删除
		if (startDate != null && startDate.length() > 0 && endDate != null
				&& endDate.length() > 0) {
			criteria.add(Restrictions.ge("itime", Date.valueOf(startDate)));// 入库日期限制（可选）
			criteria.add(Restrictions.le("itime", Date.valueOf(endDate)));// 入库日期限制（可选）
		}

		// 排序条件限制（可选）
		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "id";
		}
		if (orderValue == 0) {
			criteria.addOrder(Order.asc(orderKey));
		} else {
			criteria.addOrder(Order.desc(orderKey));
		}

		// 设定本类条件（可选）
		if (nmsAssetKey != null && nmsAssetKey.length() > 0) {
			if (nmsAssetKey.equals("colled")
					|| nmsAssetKey.equals("colledMode")) {// 数字类型用eq()方法加入
				Integer nav = Integer.valueOf(nmsAssetValue);
				criteria.add(Restrictions.eq(nmsAssetKey, nav));
			} else { // 字符串类型用like()方法加入
				criteria.add(Restrictions.like(nmsAssetKey, "%" + nmsAssetValue
						+ "%"));
			}
		}

		// 设定关联类NmsDepartment条件（可选）
		if (nmsDepartmentKey != null && nmsDepartmentKey.length() > 0) {
			// 这个类中没有数字作为条件的字段
			criteria.createAlias("nmsDepartment", "nmsDepartment");
			nmsDepartmentKey = "nmsDepartment." + nmsDepartmentKey;
			criteria.add(Restrictions.like(nmsDepartmentKey, "%"
					+ nmsDepartmentValue + "%"));
			criteria.add(Restrictions.eq("nmsDepartment.deled", 0));
		}

		// 设定关联类NmsAssetType条件（可选）
		if (nmsAssetTypeKey != null && nmsAssetTypeKey.length() > 0) {
			// 这个类中没有数字作为条件的字段
			criteria.createAlias("nmsAssetType", "nmsAssetType");
			nmsAssetTypeKey = "nmsAssetType." + nmsAssetTypeKey;
			criteria.add(Restrictions.like(nmsAssetTypeKey, "%"
					+ nmsAssetTypeValue + "%"));
		}

		// 获取数据总数
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria)
				.get(0);

		// 分页查找所需数据
		criteria.setProjection(null);
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate
				.findByCriteria(criteria, (begin - 1) * offset, offset);

		// 计算总页数
		int totalPage = 1;
		if (totalCount == 0) {
			totalPage = totalCount.intValue() / offset + 1;
		} else if (totalCount % offset == 0) {
			totalPage = totalCount.intValue() / offset;
		} else {
			totalPage = totalCount.intValue() / offset + 1;
		}

		// 创建PageBean对象返回数据
		PageBean<NmsAsset> page = new PageBean<NmsAsset>();
		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
		page.setTotalCount(totalCount.intValue());
		page.setPage(begin);
		page.setTotalPage(totalPage);
		String key = null;
		String value = null;
		page.setKey(key);
		page.setValue(value);
		page.setList(list);

		return page;
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsAsset> reportSelect(String orderKey, int orderValue,
			String startDate, String endDate, String AName, String AIp,
			String AType, String ADept, int begin, int offset) throws Exception {

		// 加载本类
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);
		DetachedCriteria criteriaForCount = DetachedCriteria.forClass(NmsAsset.class);

		// 设定基础条件（部分可选）
		criteria.add(Restrictions.eq("deled", 0));
		criteriaForCount.add(Restrictions.eq("deled", 0));
		if (startDate != null) {
			criteria.add(Restrictions.ge("itime", DateSqlUtils.strToDate(startDate)));
			criteriaForCount.add(Restrictions.ge("itime", DateSqlUtils.strToDate(startDate)));
		}

		if (endDate != null) {
			criteria.add(Restrictions.le("itime", DateSqlUtils.strToDate(endDate)));
			criteriaForCount.add(Restrictions.le("itime", DateSqlUtils.strToDate(endDate)));
		}

		// 排序条件限制（可选）
		if (orderValue == 0) {
			criteria.addOrder(Order.asc(orderKey));
			criteriaForCount.addOrder(Order.asc(orderKey));
		} else {
			criteria.addOrder(Order.desc(orderKey));
			criteriaForCount.addOrder(Order.desc(orderKey));
		}

		if (AName != null) {
			criteria.add(Restrictions.like("AName", "%" + AName + "%"));
			criteriaForCount.add(Restrictions.like("AName", "%" + AName + "%"));
		}

		if (AIp != null) {
			criteria.add(Restrictions.like("AIp", "%" + AIp + "%"));
			criteriaForCount.add(Restrictions.like("AIp", "%" + AIp + "%"));
		}

		if (ADept != null && !ADept.equals("")) {
			DetachedCriteria criteriaDepartment = DetachedCriteria.forClass(NmsDepartment.class);
			criteriaDepartment.add(Restrictions.like("id", ADept));
			criteriaDepartment.add(Restrictions.eq("deled", 0));
			criteriaDepartment.setProjection(Property.forName("id"));
			criteria.add(Property.forName("nmsDepartment").in(criteriaDepartment));

			DetachedCriteria criteriaDepartmentForCount = DetachedCriteria.forClass(NmsDepartment.class);
			criteriaDepartmentForCount.add(Restrictions.like("id", ADept));
			criteriaDepartmentForCount.add(Restrictions.eq("deled", 0));
			criteriaDepartmentForCount.setProjection(Property.forName("id"));
			criteriaForCount.add(Property.forName("nmsDepartment").in(criteriaDepartmentForCount));
		}

		if (AType != null) {
			DetachedCriteria criteriaAssetType = DetachedCriteria.forClass(NmsAssetType.class);
			criteriaAssetType.add(Restrictions.eq("id", Integer.valueOf(AType)));
			criteriaAssetType.setProjection(Property.forName("id"));
			criteria.add(Property.forName("nmsAssetType").in(criteriaAssetType));

			DetachedCriteria criteriaAssetTypeForCount = DetachedCriteria.forClass(NmsAssetType.class);
			criteriaAssetTypeForCount.add(Restrictions.eq("id", Integer.valueOf(AType)));
			criteriaAssetTypeForCount.setProjection(Property.forName("id"));
			criteriaForCount.add(Property.forName("nmsAssetType").in(criteriaAssetTypeForCount));
		}

		// 获取数据总数
		int totalCount = ((Long)criteriaForCount.setProjection(Projections.rowCount()).getExecutableCriteria(this.hibernateTemplate.getSessionFactory().getCurrentSession()).uniqueResult()).intValue();

		// 分页查找所需数据
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.findByCriteria(criteria, (begin - 1) * offset, offset);
		
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
		PageBean<NmsAsset> page = new PageBean<NmsAsset>();
		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
		page.setTotalCount(totalCount);
		page.setPage(begin);
		page.setTotalPage(totalPage);
		String key = orderKey;
		String value = String.valueOf(orderValue);
		page.setKey(key);
		page.setValue(value);
		page.setList(list);

		return page;
	}

	public NmsSystemDetail assetSystermInfoOverview(Integer assetId) {

		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsSystemDetail();
		}

		NmsSystemDetail nmsSystemDetail = new NmsSystemDetail();

		DetachedCriteria nmsStaticInfoCriteria = DetachedCriteria
				.forClass(NmsStaticInfo.class);
		nmsStaticInfoCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		nmsStaticInfoCriteria.addOrder(Order.desc("id"));

		List<NmsStaticInfo> nmsStaticInfoList = (List<NmsStaticInfo>) hibernateTemplate
				.findByCriteria(nmsStaticInfoCriteria);

		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (nmsAsset.getOnline() != null && nmsAsset.getOnline().equals(1)){
			String nmsAlarmSql = "select d_status,count(1) from nms_alarm where d_status in (0,1) and asset_id="
					+ assetId + " group by d_status";

			Query nmsAlarmQuery = session.createSQLQuery(nmsAlarmSql);
			List nmsAlarmList = nmsAlarmQuery.list();
			session.close();

			StringBuilder builder = new StringBuilder("存在告警：");
			if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
				nmsSystemDetail.setStatusCode(0);
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
				nmsSystemDetail.setStatus(builder.toString());
			} else {
				nmsSystemDetail.setStatus("正常");
				nmsSystemDetail.setStatusCode(1);
			}
		} else {
			nmsSystemDetail.setStatus("正常");
			nmsSystemDetail.setStatusCode(1);
		}

		NmsNetifInfo nmsNetifInfo = new NmsNetifInfo();
		nmsNetifInfo.setNmsAsset(nmsAsset);
		nmsNetifInfo.setIfIp(nmsAsset.getAIp());
		List<NmsNetifInfo> nmsNetifInfoList = hibernateTemplate.findByExample(nmsNetifInfo);

		if (nmsNetifInfoList != null && nmsNetifInfoList.size() > 0) {
			nmsNetifInfo = nmsNetifInfoList.get(0);
		}

		DetachedCriteria dynamicDetachedCriteria = DetachedCriteria
				.forClass(NmsDynamicInfo.class);
		dynamicDetachedCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		dynamicDetachedCriteria.addOrder(Order.desc("id"));
		List<NmsDynamicInfo> nmsDynamicInfoList = (List<NmsDynamicInfo>) hibernateTemplate.findByCriteria(dynamicDetachedCriteria, 0, 1);

		if (nmsAsset.getOnline() != null && nmsAsset.getOnline().equals(1)) {
			String processSql = "select count(1) from nms_process_info a where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_process_info) as b where asset_id = "
					+ assetId + ") " + "and a.asset_id = " + assetId;
			Session session1 = hibernateTemplate.getSessionFactory().openSession();
			Query processQuery = session1.createSQLQuery(processSql);
			List processList = processQuery.list();
			session1.close();

			if (processList != null && processList.size() > 0) {
				Integer count = ((BigInteger) processList.get(0)).intValue();
				nmsSystemDetail.setProcessNum(count);
			} else {
				nmsSystemDetail.setProcessNum(0);
			}
		} else {
			nmsSystemDetail.setProcessNum(0);
		}

		nmsSystemDetail.setAssetId(nmsAsset.getId());
		nmsSystemDetail.setAssetName(nmsAsset.getAName());
		nmsSystemDetail.setAssetNo(nmsAsset.getANo());
		nmsSystemDetail.setAssetIP(nmsAsset.getAIp());
		nmsSystemDetail.setAssetType(nmsAsset.getNmsAssetType().getChType());
		nmsSystemDetail.setAssetSubtype(nmsAsset.getNmsAssetType()
				.getChSubtype());
		nmsSystemDetail.setAssetPosition(nmsAsset.getAPos());
		nmsSystemDetail.setColled(String.valueOf(nmsAsset.getColled()));

		if (nmsStaticInfoList != null && nmsStaticInfoList.size() > 0) {
			NmsStaticInfo nmsStaticInfo = nmsStaticInfoList.get(0);
			if (nmsAsset.getOnline() != null && nmsAsset.getOnline().equals(1)) {
				nmsSystemDetail.setSysName(nmsStaticInfo.getSysName());
				nmsSystemDetail.setSysInfo(nmsStaticInfo.getSysName() + " "
						+ nmsStaticInfo.getSysArch() + " "
						+ nmsStaticInfo.getSysBits() + " "
						+ nmsStaticInfo.getSocVersion() + " "
						+ nmsStaticInfo.getCoreVersion());
			}
			nmsSystemDetail.setProductName(nmsStaticInfo.getProductName());
			nmsSystemDetail.setUniqueIdent(nmsStaticInfo.getUniqueIdent());
		}

		nmsSystemDetail.setAssetIfSubmask(nmsNetifInfo.getIfSubmask());

		if (nmsDynamicInfoList != null && nmsDynamicInfoList.size() > 0) {
			NmsDynamicInfo nmsDynamicInfo = nmsDynamicInfoList.get(0);

			if (nmsAsset.getOnline() != null && nmsAsset.getOnline().equals(1)) {
				nmsSystemDetail.setSysUptime(String.valueOf(nmsDynamicInfo
						.getSysUptime()));
			}

			nmsSystemDetail.setItime(nmsDynamicInfo.getItime());
		}

		return nmsSystemDetail;
	}

	@SuppressWarnings("unchecked")
	public List<NmsAsset> reportSelectExportExcel(String orderKey,
			int orderValue, String startDate, String endDate, String AName,
			String AIp, String AType, String ADept) throws Exception {

		// 加载本类
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAsset.class);
		DetachedCriteria criteriaForCount = DetachedCriteria
				.forClass(NmsAsset.class);

		// 设定基础条件（部分可选）
		criteria.add(Restrictions.eq("deled", 0));// 是否已删除
		criteriaForCount.add(Restrictions.eq("deled", 0));// 是否已删除
		if (startDate != null) {
			criteria.add(Restrictions.ge("itime", DateSqlUtils.strToDate(startDate)));// 入库日期限制（可选）
			criteriaForCount.add(Restrictions.ge("itime",
					DateSqlUtils.strToDate(startDate)));// 入库日期限制（可选）
		}

		if (endDate != null) {
			criteria.add(Restrictions.le("itime", DateSqlUtils.strToDate(endDate)));// 入库日期限制（可选）
			criteriaForCount
					.add(Restrictions.le("itime", DateSqlUtils.strToDate(endDate)));// 入库日期限制（可选）
		}

		// 排序条件限制（可选）
		if (orderKey == null) {
			orderKey = "id";
		}
		if (orderValue == 0) {
			criteria.addOrder(Order.asc(orderKey));
			criteriaForCount.addOrder(Order.asc(orderKey));
		} else {
			criteria.addOrder(Order.desc(orderKey));
			criteriaForCount.addOrder(Order.desc(orderKey));
		}

		// 设定本类条件（可选）
		if (AName != null) {
			// 字符串类型用like()方法加入
			criteria.add(Restrictions.like("AName", "%" + AName + "%"));
			criteriaForCount.add(Restrictions.like("AName", "%" + AName + "%"));
		}
		// 设定本类条件（可选）
		if (AIp != null) {
			// 字符串类型用like()方法加入
			criteria.add(Restrictions.like("AIp", "%" + AIp + "%"));
			criteriaForCount.add(Restrictions.like("AIp", "%" + AIp + "%"));
		}
		// 设定关联类NmsDepartment条件（可选）
		if (ADept != null && !ADept.equals("")) {
			// 这个类中没有数字作为条件的字段
			DetachedCriteria criteriaDepartment = DetachedCriteria
					.forClass(NmsDepartment.class);
			criteriaDepartment.add(Restrictions.eq("id", ADept));
			criteriaDepartment.add(Restrictions.eq("deled", 0));
			criteriaDepartment.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteria.add(Property.forName("nmsDepartment").in(
					criteriaDepartment));

			DetachedCriteria criteriaDepartmentForCount = DetachedCriteria
					.forClass(NmsDepartment.class);
			criteriaDepartmentForCount.add(Restrictions.eq("id", ADept));
			criteriaDepartmentForCount.add(Restrictions.eq("deled", 0));
			criteriaDepartmentForCount.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteriaForCount.add(Property.forName("nmsDepartment").in(
					criteriaDepartmentForCount));

		}

		// 设定关联类NmsAssetType条件（可选）
		if (AType != null) {
			// 这个类中没有数字作为条件的字段
			DetachedCriteria criteriaAssetType = DetachedCriteria
					.forClass(NmsAssetType.class);
			criteriaAssetType
					.add(Restrictions.eq("id", Integer.valueOf(AType)));
			criteriaAssetType.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteria.add(Property.forName("nmsAssetType").in(criteriaAssetType));

			DetachedCriteria criteriaAssetTypeForCount = DetachedCriteria
					.forClass(NmsAssetType.class);
			criteriaAssetTypeForCount.add(Restrictions.eq("id",
					Integer.valueOf(AType)));
			criteriaAssetTypeForCount.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
			criteriaForCount.add(Property.forName("nmsAssetType").in(
					criteriaAssetTypeForCount));

		}

		// 查找所需数据
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate
				.findByCriteria(criteria);

		return list;

	}
	
	@SuppressWarnings("unchecked")
	public boolean ifIp(String ip) {
		String hsql = "from NmsAsset na where na.AIp = ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql,ip);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean ifIpAndType(String ip, String type) {
		String hsql = "from NmsAsset na where na.AIp = ? and na.nmsAssetType.chType != ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, ip, type);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean ifNo(String no) {
		String hsql = "from NmsAsset na where na.ANo = ? and na.deled = 0";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, no);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean findOnlineById(int id) {
		String hsql = "from NmsAsset na where na.id = ? and na.deled = 0 and online = 1";
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
}