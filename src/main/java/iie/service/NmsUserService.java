package iie.service;

import iie.pojo.NmsUser;
import iie.tools.PageBean;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xczhao
 * @date 2020/10/25 - 17:38
 */
@Service("NmsUserService")
public class NmsUserService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public PageBean<NmsUser> getPageByCondition(String orderKey,
                                                int orderValue, String name, String card, int begin, int offset)
            throws Exception {
        // 加载本类
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsUser.class);
        DetachedCriteria criteriaForCount = DetachedCriteria
                .forClass(NmsUser.class);

        // 设定基础条件（部分可选）
        criteria.add(Restrictions.eq("deled", 0));// 是否已删除
        criteriaForCount.add(Restrictions.eq("deled", 0));// 是否已删除

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

        // 设定查询条件（可选）
        if (name != null && name != "") {
            criteria.add(Restrictions.like("name", "%" + name + "%"));
            criteriaForCount.add(Restrictions.like("name", "%" + name + "%"));
        }

        // 设定card条件（可选）
        if (card != null && card != "") {
            criteria.add(Restrictions.like("card", "%" + card + "%"));
            criteriaForCount.add(Restrictions.like("card", "%" + card + "%"));
        }

        // 获取数据总数
        int totalCount = ((Long) criteriaForCount
                .setProjection(Projections.rowCount())
                .getExecutableCriteria(
                        this.hibernateTemplate.getSessionFactory()
                                .getCurrentSession()).uniqueResult())
                .intValue();

        // 分页查找所需数据
        List<NmsUser> list = (List<NmsUser>) hibernateTemplate
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
        PageBean<NmsUser> page = new PageBean<NmsUser>();
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

    public boolean saveUser(NmsUser nu) {
        try {
            nu.setId((int) 0);
            hibernateTemplate.save(nu);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateUser(NmsUser nu) {
        try {
            hibernateTemplate.saveOrUpdate(nu);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public NmsUser findByIdForUpdate(int id) {
        String hsql = "from NmsUser nu where nu.id = ? and nu.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsUser> list = (List<NmsUser>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            NmsUser obj = list.get(0);
            return obj;
        }
        return new NmsUser();
    }

    public NmsUser findById(int id) {
        String hsql = "from NmsUser nu where nu.id = ? and nu.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsUser> list = (List<NmsUser>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            NmsUser obj = list.get(0);
            return obj;
        }
        return new NmsUser();
    }

    public NmsUser findByIdAny(int id) {
        String hsql = "from NmsUser nu where nu.id = ?";
        @SuppressWarnings("unchecked")
        List<NmsUser> list = (List<NmsUser>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsUser();
    }

    public boolean deleteUser(int id) {
        NmsUser user = findById(id);
        user.setDeled(1);
        return updateUser(user);
    }

    @SuppressWarnings("unchecked")
    public boolean ifUser(String card) {
        String hsql = "from NmsUser nu where nu.card = ? and nu.deled = 0";
        List<NmsUser> list = (List<NmsUser>) hibernateTemplate.find(hsql, card);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
