package iie.service;

import iie.pojo.NmsFunction;
import iie.pojo.NmsRole;
import iie.pojo.NmsRoleFunction;
import iie.tools.PageBean;
import iie.tools.RoleFunctionBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("NmsRoleFunctionService")
public class NmsRoleFunctionService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsRoleFunction> getAllReal() {
		String hsql = "from NmsRoleFunction nr where nr.deled = 0";
		return (List<NmsRoleFunction>) hibernateTemplate.find(hsql);
	}
	
	@SuppressWarnings("unchecked")
	public List<NmsRoleFunction> getAll() {
		String hsql = "from NmsRoleFunction nr where nr.deled = 0";
		List<NmsRoleFunction> list = (List<NmsRoleFunction>) hibernateTemplate.find(hsql);
		for(int i=0;i<list.size();i++){
			if(list.get(i).getNmsRole().getRole().equals("网络系统管理员")){
				list.remove(i);
				i--;//ArrayList底层删除时所有元素前移，需要将指针回调一次
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<NmsRoleFunction> findByRoleId(int id) {
		String hsql = "from NmsRoleFunction nr where nr.nmsRole.id = ? and nr.deled = 0";
		return (List<NmsRoleFunction>) hibernateTemplate.find(hsql, id);
	}
	
	@SuppressWarnings("unchecked")
	public NmsRole findRoleByName(String name) {
		String hsql = "from NmsRole nr where nr.role = ?";
		List<NmsRole> list = (List<NmsRole>) hibernateTemplate.find(hsql, name);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsRole();
	}
	
	@SuppressWarnings("unchecked")
	public NmsFunction findFunctionById(int id) {
		String hsql = "from NmsFunction nf where nf.id = ?";
		List<NmsFunction> list = (List<NmsFunction>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsFunction();
	}
	
	public boolean add(NmsRoleFunction roleFunction) {
		try {
			roleFunction.setId(0);
			roleFunction.setDeled(0);
			roleFunction.setItime(new Timestamp(System.currentTimeMillis()));
			hibernateTemplate.save(roleFunction);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public boolean delete(NmsRoleFunction nmsRoleFunction) {
		try {
			nmsRoleFunction.setDeled(1);
			hibernateTemplate.saveOrUpdate(nmsRoleFunction);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public boolean deleteByRoleId(int roleId) {
		boolean res = true;
		String sql = "delete from nms_role_function where role_id = " + roleId;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		try {
			int num = session.createSQLQuery(sql).executeUpdate();
			//System.out.println("[DEBUG] delete from nms_role_function where role_id = " + roleId + "共 " + num + " 条记录");
		} catch (Exception e) {
			res = false;
		}
		tran.commit();
		session.close();
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public List<NmsRoleFunction> findRoleFunctionByRoleId(int id) {
		String hsql = "from NmsRoleFunction nr where nr.nmsRole.id = ? and nr.deled = 0";
		return (List<NmsRoleFunction>) hibernateTemplate.find(hsql, id);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public PageBean<RoleFunctionBean> getRoleFunctionPage(String role, int begin, int offset, String orderKey, String orderValue) {
		String sql = "select id, role, itime from nms_role where 1=1 and deled = 0 ";
		String sqlCount = "select id, role, itime from nms_role where 1=1 and deled = 0 ";
		if (role != null && role.length() > 0) {
			sql = sql + "and role like '%" + role + "%' ";
			sqlCount = sqlCount + "and role like '%" + role + "%' ";
		}
		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "id";
		}
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " desc";
		} else {
			orderKey = orderKey + " asc";
		}		
		sql += " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;

		List list = null;
		Integer count = 0;
		
		// 执行SQL语句
		try {
			Session session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.list();
			
			SQLQuery sqlCountQuery = session.createSQLQuery(sqlCount);
			List listCount = sqlCountQuery.list();
			count = listCount.size();
			session.close();
		} catch (Exception e) {
			e.toString();
		}
		
		List<NmsRoleFunction> res = this.getAllReal();
		
		// 获取FUNCTIONS值
		List<RoleFunctionBean> result = new ArrayList<RoleFunctionBean>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				RoleFunctionBean bean = new RoleFunctionBean();
				int roleId = (Integer)objects[0];
				bean.setId(roleId);
				bean.setRole((String)objects[1]);
				bean.setItime((Timestamp)objects[2]);

				// 初始化functions字符串
				bean.setFunctions("");
				
				// 获取functions字符串
				if (res != null && res.size() > 0) {
					for (int j = 0; j < res.size(); j++) {
						if (roleId == res.get(j).getNmsRole().getId()) {
							String descString = res.get(j).getNmsFunction().getChineseDesc();
							String functions = "";
							if (bean.getFunctions().equals("")) {
								functions = descString;
							} else {
								functions = bean.getFunctions() + ", " + descString;
							}
							bean.setFunctions(functions);
						}
					}
				}
				result.add(bean);
			}
		}
		
		// 创建PageBean对象返回数据
		PageBean<RoleFunctionBean> page = new PageBean<RoleFunctionBean>();
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
	
}
