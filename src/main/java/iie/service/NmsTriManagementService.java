package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsTriManagement;
import iie.tools.PageBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service("NmsTriManagementService")
@Transactional
public class NmsTriManagementService {
    Logger logger = Logger.getLogger(NmsTriManagementService.class);

    @Autowired
    HibernateTemplate hibernateTemplate;

    @Autowired
    NmsAssetService nmsAssetService;

    /**
     * 获取三方管理端信息列表
     * @param paramMap
     * @return
     * @throws Exception
     */
    public PageBean<NmsTriManagement> getPageByConditionDate(Map<String,Object> paramMap) throws Exception {
        String orderKey = StringUtils.isNotBlank(MapUtils.getString(paramMap, "orderKey")) ? MapUtils.getString(paramMap, "orderKey") : "id";
        int orderValue =StringUtils.isNotBlank(MapUtils.getString(paramMap, "orderValue")) ? MapUtils.getIntValue(paramMap, "orderValue") : 0;
        String nmsAssetAspid = MapUtils.getString(paramMap, "nmsAssetAspid");
        String nmsAssetName = MapUtils.getString(paramMap, "nmsAssetName");
        String nmsAssetTypes = MapUtils.getString(paramMap, "nmsAssetType");
        String nmsManageName = MapUtils.getString(paramMap, "nmsManageName");
        String nmsManageUrl = MapUtils.getString(paramMap, "nmsManageUrl");
        int begin = MapUtils.getIntValue(paramMap, "begin");
        int offset = MapUtils.getIntValue(paramMap, "offset");

        // 加载本类
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsTriManagement.class);

        // 设定基础条件（部分可选）
        criteria.add(Restrictions.eq("deled", 0));// 是否已删除
        if (StringUtils.isNotBlank(nmsManageName)) {
            nmsManageName = StringEscapeUtils.escapeSql(nmsManageName);
            criteria.add(Restrictions.like("manageName", "%" + nmsManageName + "%"));
        }
        if (StringUtils.isNotBlank(nmsManageUrl)) {
            nmsManageUrl = StringEscapeUtils.escapeSql(nmsManageUrl);
            criteria.add(Restrictions.like("manageUrl", "%" + nmsManageUrl + "%"));
        }
        // 设定关联类NmsAsset条件（可选）
        if (StringUtils.isNotBlank(nmsAssetAspid) || StringUtils.isNotBlank(nmsAssetName) || StringUtils.isNotBlank(nmsAssetTypes)) {
            DetachedCriteria criteriaAsset = DetachedCriteria.forClass(NmsAsset.class);
            criteriaAsset.add(Restrictions.eq("deled", 0));
            if (StringUtils.isNotBlank(nmsAssetAspid)){
                criteriaAsset.add(Restrictions.like("ANo", "%" + nmsAssetAspid + "%"));
            }
            if (StringUtils.isNotBlank(nmsAssetName)){
                criteriaAsset.add(Restrictions.like("AName", "%" + nmsAssetName + "%"));
            }
            if (StringUtils.isNotBlank(nmsAssetTypes) && Integer.valueOf(nmsAssetTypes) >= 0){
                // 这个类中没有数字作为条件的字段
                DetachedCriteria criteriaAssetType = DetachedCriteria.forClass(NmsAssetType.class);
                criteriaAssetType.add(Restrictions.eq("id", Integer.valueOf(nmsAssetTypes)));
                criteriaAssetType.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
                criteriaAsset.add(Property.forName("nmsAssetType").in(criteriaAssetType));
            }
            criteriaAsset.setProjection(Property.forName("id"));// 声明关联NmsAsset类的字段
            criteria.add(Property.forName("nmsAsset").in(criteriaAsset));
        }

        // 获取数据总数
        int totalCount = hibernateTemplate.findByCriteria(criteria).size();

        // 排序条件限制（可选）
        if (orderValue == 0) {
            criteria.addOrder(Order.desc(orderKey));
        } else {
//            criteria.createAlias("nmsAsset", "a");
            criteria.addOrder(Order.asc(orderKey));
        }
        // 分页查找所需数据
        List<NmsTriManagement> list = (List<NmsTriManagement>) hibernateTemplate.findByCriteria(criteria, (begin - 1) * offset, offset);

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
        PageBean<NmsTriManagement> page = new PageBean<NmsTriManagement>();
        page.setOrderKey(orderKey);
        page.setOrderValue(orderValue);
        page.setTotalCount(totalCount);
        page.setPage(begin);
        page.setTotalPage(totalPage);
        page.setKey(null);
        page.setValue(null);
        page.setList(list);

        return page;
    }

	/**
	 * 根据id查询三方管理端信息
	 * @param id
	 * @return
	 */
	public NmsTriManagement findById(int id) {
        String hsql = "from NmsTriManagement na where na.id = ? and na.deled = 0";
        List<NmsTriManagement> list = (List<NmsTriManagement>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsTriManagement();
    }

	/**
	 * 新增三方管理端信息
	 * @param management
	 * @return
	 */
	public boolean saveManagement(NmsTriManagement management) {
        try {
			management.setId(0);
			management.setDeled(0);
			management.setCreateTime(new Timestamp(System.currentTimeMillis()));
            hibernateTemplate.save(management);
        } catch (Exception e) {
            System.out.println(e);
        	logger.error("新增三方管理端信息异常：",e);
            return false;
        }
        return true;
    }

	/**
	 * 更新三方管理端信息
	 * @param management
	 * @return
	 */
	public boolean updateManagement(NmsTriManagement management) {
        try {
			management.setModifyTime(new Timestamp(System.currentTimeMillis()));
            hibernateTemplate.saveOrUpdate(management);
        } catch (Exception e) {
           logger.error("更新三方管理端信息异常：", e);
           return false;
        }
        return true;
    }

	/**
	 * 删除三方管理端信息（逻辑删除）
	 * @param id
	 * @return
	 */
	public boolean deleteManagement(int id) {
		NmsTriManagement dto = findById(id);
        if (dto != null) {
			dto.setDeled(1);
            return updateManagement(dto);
        }
        return true;
    }

    /**
     * 根据aspid获取资产信息
     *
     * @param no
     * @return
     */
    public NmsAsset findByAspId(String no) {
        String hsql = "from NmsAsset na where na.ANo = ? and na.deled = 0";
        List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, no);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return new NmsAsset();
    }
}