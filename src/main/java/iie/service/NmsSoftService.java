package iie.service;

import iie.pojo.*;
import iie.tools.PageBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author xczhao
 * @date 2020/10/13 - 20:49
 */
@Service("NmsSoftService")
@Transactional
public class NmsSoftService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @Autowired
    NmsRuleService nmsRuleService;

    public boolean updateSoft(NmsSoft soft) {
        try {
            soft.setItime(new Timestamp(System.currentTimeMillis()));
            hibernateTemplate.saveOrUpdate(soft);
        } catch (Exception e) {
            e.toString();
            return false;
        }
        return true;
    }

    public boolean updateRuleSoft(NmsSoft soft) {
        boolean res = true;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction tran = session.beginTransaction();
        int soft_id = soft.getId();
        try {
            // 删除soft_id已经存在的规则记录
            if (!nmsRuleService.deleteNmsRuleByAssetId(soft_id)) {
                System.out.println("[ERROE] delete from nms_rule_soft where soft_id = " + soft_id + "失败");
                return false;
            }

            // 生成资产告警规则
            NmsAssetType type = soft.getNmsAssetType();
            List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(type.getId());
            for (NmsRule rule : rules) {
                NmsRuleSoft ruleSoft = new NmsRuleSoft();
                ruleSoft.setId(0);
                ruleSoft.setItime(new Timestamp(System.currentTimeMillis()));
                ruleSoft.setNmsSoft(soft);
                ruleSoft.setNmsAssetType(type);
                ruleSoft.setDType(rule.getDType());
                ruleSoft.setRName(rule.getRName());
                ruleSoft.setRContent(rule.getRContent());
                ruleSoft.setRUnit(rule.getRUnit());
                ruleSoft.setRSeq(rule.getRSeq());
                ruleSoft.setREnable(rule.getREnable());
                ruleSoft.setRValue1(rule.getRValue1().intValue());
                ruleSoft.setRValue2(rule.getRValue2().intValue());
                ruleSoft.setRValue3(rule.getRValue3().intValue());
                session.save(ruleSoft);
            }
            tran.commit();
        } catch (Exception e) {
            System.out.println("[ERROE] delete from nms_rule_soft where soft_id = " + soft_id + "异常");
            res = false;
        } finally {
            session.close();
        }
        return res;
    }

	@SuppressWarnings("unchecked")
	public NmsAssetType findTypeByName(String type, String subType) {
		String hsql = "from NmsAssetType nat where nat.chType = ? and nat.chSubtype = ?";
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate.find(
				hsql, type, subType);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
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
	public NmsSoft findByIpOrPort(String ip, String port) {
		String hsql = "from NmsSoft na where na.AIp = ? and na.APort = ? and na.deled = 0";
		List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.find(hsql, ip, port);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
    @SuppressWarnings("unchecked")
    public NmsSoft findById(int id) {
        String hsql = "from NmsSoft ns where ns.id = ? and ns.deled = 0";
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public NmsSoft findByIdAny(int id) {
        String hsql = "from NmsSoft ns where ns.id = ?";
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsSoft();
    }

    public boolean deleteSoft(int id) {
        NmsSoft soft = findById(id);
        if (soft != null) {
            soft.setDeled(1);
            return updateSoft(soft);
        }
        return true;
    }

    public boolean saveSoftAndRuleSoft(NmsSoft soft) {
        boolean res = true;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction tran = session.beginTransaction();
        try {
            soft.setId(0);
            soft.setDeled(0);
            soft.setItime(new Timestamp(System.currentTimeMillis()));
            session.save(soft);

            // 生成资产告警规则
            NmsAssetType type = soft.getNmsAssetType();
            List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(type.getId());
            for (NmsRule rule : rules) {
                NmsRuleSoft ruleSoft = new NmsRuleSoft();
                ruleSoft.setId(0);
                ruleSoft.setItime(new Timestamp(System.currentTimeMillis()));
                ruleSoft.setNmsSoft(soft);
                ruleSoft.setNmsAssetType(type);
                ruleSoft.setDType(rule.getDType());
                ruleSoft.setRName(rule.getRName());
                ruleSoft.setRContent(rule.getRContent());
                ruleSoft.setRUnit(rule.getRUnit());
                ruleSoft.setRSeq(rule.getRSeq());
                ruleSoft.setREnable(rule.getREnable());
                ruleSoft.setRValue1(rule.getRValue1().intValue());
                ruleSoft.setRValue2(rule.getRValue2().intValue());
                ruleSoft.setRValue3(rule.getRValue3().intValue());
                session.save(ruleSoft);
            }
            tran.commit();
        } catch (Exception e) {
            res = false;
        } finally {
            session.close();
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public NmsSoft loadLastSoft() throws Exception {

        Session session = hibernateTemplate.getSessionFactory().openSession();
        String sql = "select * from nms_soft order by id desc limit 0,1";
        SQLQuery query = session.createSQLQuery(sql);
        List list = query.list();
        session.close();

        if (list != null && list.size() > 0) {
            Object[] data = (Object[]) list.get(0);
            NmsSoft soft = new NmsSoft();
            soft.setId((Integer) data[0]);
            soft.setAIp((String) data[1]);
            soft.setAPort((String) data[2]);
            soft.setAName((String) data[3]);
            soft.setAPos((String) data[5]);
            soft.setAManu((String) data[6]);
            soft.setADate((String) data[7]);
            soft.setAUser((String) data[8]);
            return soft;
        } else {
            return new NmsSoft();
        }

    }

    @SuppressWarnings("unchecked")
    public boolean ifIpOrPort(String ip, String port) {
        String hsql = "from NmsSoft ns where ns.AIp = ? and ns.APort = ? and ns.deled = 0";
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.find(hsql, ip, port);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public PageBean<NmsSoft> getPageByConditionDate(String orderKey,
                                                    int orderValue, String startDate, String endDate,
                                                    String nmsAssetKey, String nmsAssetValue, String nmsDepartmentKey,
                                                    String nmsDepartmentValue, String nmsAssetTypeKey,
                                                    String nmsAssetTypeValue, int begin, int offset) throws Exception {

        // 加载本类
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsSoft.class);
        DetachedCriteria criteriaForCount = DetachedCriteria.forClass(NmsSoft.class);

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
            criteria.addOrder(Order.asc(orderKey));
            criteriaForCount.addOrder(Order.desc(orderKey));
        } else {
            criteria.addOrder(Order.desc(orderKey));
            criteriaForCount.addOrder(Order.asc(orderKey));
        }

        // 设定本类条件（可选）
        if (nmsAssetKey != null) {
            if (nmsAssetKey.equals("colled") || nmsAssetKey.equals("colledMode")) {// 数字类型用eq()方法加入
                Integer nav = Integer.valueOf(nmsAssetValue);
                criteria.add(Restrictions.eq(nmsAssetKey, nmsAssetValue));
                criteriaForCount.add(Restrictions.eq(nmsAssetKey, nmsAssetValue));
            } else { // 字符串类型用like()方法加入
                criteria.add(Restrictions.like(nmsAssetKey, "%" + nmsAssetValue + "%"));
                criteriaForCount.add(Restrictions.like(nmsAssetKey, "%" + nmsAssetValue + "%"));
            }
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
            DetachedCriteria criteriaAssetType = DetachedCriteria.forClass(NmsAssetType.class);
            criteriaAssetType.add(Restrictions.like(nmsAssetTypeKey, "%" + nmsAssetTypeValue + "%"));
            criteriaAssetType.setProjection(Property.forName("id"));// 声明关联NmsSoft类的字段
            criteria.add(Property.forName("nmsAssetType").in(criteriaAssetType));

            DetachedCriteria criteriaAssetTypeForCount = DetachedCriteria.forClass(NmsAssetType.class);
            criteriaAssetTypeForCount.add(Restrictions.like(nmsAssetTypeKey, "%" + nmsAssetTypeValue + "%"));
            criteriaAssetTypeForCount.setProjection(Property.forName("id"));// 声明关联NmsSoft类的字段
            criteriaForCount.add(Property.forName("nmsAssetType").in(criteriaAssetTypeForCount));
        }

        // 获取数据总数
        int totalCount = ((Long) criteriaForCount
                .setProjection(Projections.rowCount())
                .getExecutableCriteria(
                        this.hibernateTemplate.getSessionFactory()
                                .getCurrentSession()).uniqueResult())
                .intValue();

        // 分页查找所需数据
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.findByCriteria(criteria, (begin - 1) * offset, offset);

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
        PageBean<NmsSoft> page = new PageBean<NmsSoft>();
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
    public int findAll() {
        int count = 0;
        String hsql = "from NmsSoft ns where ns.deled = 0";
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.find(hsql);
        if (list != null && list.size() > 0) {
            count = list.size();
        }
        return count;
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

    @SuppressWarnings("unchecked")
    public PageBean<NmsSoft> reportSelect(String orderKey, int orderValue,
                                          String startDate, String endDate, String AName, String AIp, String APort,
                                          String AType, String ADept, int begin, int offset) throws Exception {

        // 加载本类
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsSoft.class);
        DetachedCriteria criteriaForCount = DetachedCriteria.forClass(NmsSoft.class);

        // 设定基础条件（部分可选）
        criteria.add(Restrictions.eq("deled", 0));
        criteriaForCount.add(Restrictions.eq("deled", 0));
        if (startDate != null) {
            criteria.add(Restrictions.ge("itime", Date.valueOf(startDate)));
            criteriaForCount.add(Restrictions.ge("itime", Date.valueOf(startDate)));
        }

        if (endDate != null) {
            criteria.add(Restrictions.le("itime", Date.valueOf(endDate)));
            criteriaForCount.add(Restrictions.le("itime", Date.valueOf(endDate)));
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

        if (APort != null) {
            criteria.add(Restrictions.like("APort", "%" + APort + "%"));
            criteriaForCount.add(Restrictions.like("APort", "%" + APort + "%"));
        }

        if (ADept != null) {
            DetachedCriteria criteriaDepartment = DetachedCriteria.forClass(NmsDepartment.class);
            criteriaDepartment.add(Restrictions.eq("id", Integer.valueOf(ADept)));
            criteriaDepartment.add(Restrictions.eq("deled", 0));
            criteriaDepartment.setProjection(Property.forName("id"));
            criteria.add(Property.forName("nmsDepartment").in(criteriaDepartment));

            DetachedCriteria criteriaDepartmentForCount = DetachedCriteria.forClass(NmsDepartment.class);
            criteriaDepartmentForCount.add(Restrictions.eq("id", Integer.valueOf(ADept)));
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
        int totalCount = ((Long) criteriaForCount.setProjection(Projections.rowCount()).getExecutableCriteria(this.hibernateTemplate.getSessionFactory().getCurrentSession()).uniqueResult()).intValue();

        // 分页查找所需数据
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate.findByCriteria(criteria, (begin - 1) * offset, offset);

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
        PageBean<NmsSoft> page = new PageBean<NmsSoft>();
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

    @SuppressWarnings("unchecked")
    public List<NmsSoft> reportSelectExportExcel(String orderKey,
                                                 int orderValue, String startDate, String endDate, String AName,
                                                 String AIp, String APort, String AType, String ADept) throws Exception {
        // 加载本类
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsSoft.class);
        DetachedCriteria criteriaForCount = DetachedCriteria
                .forClass(NmsSoft.class);

        // 设定基础条件（部分可选）
        criteria.add(Restrictions.eq("deled", 0));// 是否已删除
        criteriaForCount.add(Restrictions.eq("deled", 0));// 是否已删除
        if (startDate != null) {
            criteria.add(Restrictions.ge("itime", Date.valueOf(startDate)));// 入库日期限制（可选）
            criteriaForCount.add(Restrictions.ge("itime",
                    Date.valueOf(startDate)));// 入库日期限制（可选）
        }

        if (endDate != null) {
            criteria.add(Restrictions.le("itime", Date.valueOf(endDate)));// 入库日期限制（可选）
            criteriaForCount
                    .add(Restrictions.le("itime", Date.valueOf(endDate)));// 入库日期限制（可选）
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

        if (APort != null) {
            // 字符串类型用like()方法加入
            criteria.add(Restrictions.like("APort", "%" + APort + "%"));
            criteriaForCount.add(Restrictions.like("APort", "%" + APort + "%"));
        }

        // 设定关联类NmsDepartment条件（可选）
        if (ADept != null) {
            // 这个类中没有数字作为条件的字段
            DetachedCriteria criteriaDepartment = DetachedCriteria
                    .forClass(NmsDepartment.class);
            criteriaDepartment
                    .add(Restrictions.eq("id", Integer.valueOf(ADept)));
            criteriaDepartment.add(Restrictions.eq("deled", 0));
            criteriaDepartment.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
            criteria.add(Property.forName("nmsDepartment").in(
                    criteriaDepartment));

            DetachedCriteria criteriaDepartmentForCount = DetachedCriteria
                    .forClass(NmsDepartment.class);
            criteriaDepartmentForCount.add(Restrictions.eq("id",
                    Integer.valueOf(ADept)));
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
        List<NmsSoft> list = (List<NmsSoft>) hibernateTemplate
                .findByCriteria(criteria);

        return list;
    }
}
