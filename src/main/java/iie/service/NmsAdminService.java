package iie.service;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsRole;
import iie.tools.MD5Tools;
import iie.tools.PageBean;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("NmsAdminService")
public class NmsAdminService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public List<NmsAdmin> getAll() {
        String hsql = "from NmsAdmin na where na.deled = 0 and na.username != 'root'";
        return (List<NmsAdmin>) hibernateTemplate.find(hsql);
    }

    public boolean saveAdmin(NmsAdmin na) {
        try {
            na.setId((int) 0);
            hibernateTemplate.save(na);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateAdmin(NmsAdmin na) {
        try {
            hibernateTemplate.saveOrUpdate(na);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public NmsAdmin findByLoginInfo(String username, String password) {
        String pwd = MD5Tools.MD5(password);
        String hsql = "from NmsAdmin na where na.username like ? and na.password like ? and na.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql,
                username, pwd);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    public NmsAdmin findById(int id) {
        String hsql = "from NmsAdmin na where na.id = ? and na.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            NmsAdmin obj = list.get(0);
            obj.setPassword("");
            return obj;
        }
        return new NmsAdmin();
    }

    public NmsAdmin findByIdForUpdate(int id) {
        String hsql = "from NmsAdmin na where na.id = ? and na.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            NmsAdmin obj = list.get(0);
            return obj;
        }
        return new NmsAdmin();
    }

    public NmsAdmin findByIdForUpdatePassword(int id) {
        String hsql = "from NmsAdmin na where na.id = ? and na.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            NmsAdmin obj = list.get(0);
            return obj;
        }
        return null;
    }

    public NmsAdmin findByIdAny(int id) {
        String hsql = "from NmsAdmin na where na.id = ?";
        @SuppressWarnings("unchecked")
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsAdmin();
    }

    public boolean deleteAdmin(int id) {
        NmsAdmin admin = findById(id);
        admin.setDeled(1);

        //将角色修改成系统默认角色root
        NmsRole role = new NmsRole();
        role.setId(4);
        role.setDeled(0);
        admin.setNmsRole(role);
        return updateAdmin(admin);
    }

    @SuppressWarnings("unchecked")
    public boolean ifUser(String username) {
        String hsql = "from NmsAdmin na where na.username = ? and na.deled = 0";
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql,
                username);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public NmsAdmin findByUsername(String username) {
        String hsql = "from NmsAdmin na where na.username = ? and na.deled = 0";
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql,
                username);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public int findByRoleId(int roleId) {
        String hsql = "from NmsAdmin na where na.nmsRole.id = ? and na.deled = 0";
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql, roleId);
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public boolean judgeOldPasswordByUsername(String username,
                                              String oldPassword) {
        String hsql = "from NmsAdmin na where na.username = ?";
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate.find(hsql,
                username);
        if (list != null && list.size() > 0) {
            if (list.get(0).getPassword().equals(oldPassword)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean getRegularAns(String regular, String str) {
        // 匹配到的话返回true 否则返回false
        String pattern = regular;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public PageBean<NmsAdmin> getPageByCondition(String orderKey,
                                                 int orderValue, String username, String phone, int begin, int offset)
            throws Exception {
        // 加载本类
        DetachedCriteria criteria = DetachedCriteria.forClass(NmsAdmin.class);
        DetachedCriteria criteriaForCount = DetachedCriteria
                .forClass(NmsAdmin.class);

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
        if (username != null && username != "") {
            criteria.add(Restrictions.like("username", "%" + username + "%"));
            criteriaForCount.add(Restrictions.like("username", "%" + username
                    + "%"));
        }

        // 设定phone条件（可选）
        if (phone != null && phone != "") {
            criteria.add(Restrictions.like("phone", "%" + phone + "%"));
            criteriaForCount.add(Restrictions.like("phone", "%" + phone + "%"));
        }

        // 获取数据总数
        int totalCount = ((Long) criteriaForCount
                .setProjection(Projections.rowCount())
                .getExecutableCriteria(
                        this.hibernateTemplate.getSessionFactory()
                                .getCurrentSession()).uniqueResult())
                .intValue();

        // 分页查找所需数据
        List<NmsAdmin> list = (List<NmsAdmin>) hibernateTemplate
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
        PageBean<NmsAdmin> page = new PageBean<NmsAdmin>();
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
}
