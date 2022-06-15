package iie.service;

import iie.pojo.NmsYthAppAccount;
import iie.tools.PageBean;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsAppAccountInfoService")
public class NmsAppAccountInfoService {

	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public PageBean<NmsYthAppAccount> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String appId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_yth_app_account as rt where rt.appId = '" + appId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '" + endDate + "'";
		}
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());
		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "id";
		}
		
		String orderKey1 = "";
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {  
			orderKey1 = orderKey + " desc";
			orderValue = "1";
		} else {
			orderKey1 = orderKey + " asc";
			orderValue = "0";
		}

		// 获取list数据
		String sqlList = "select * from nms_yth_app_account as rt where rt.appId = '" + appId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '" + endDate + "'";
		}
		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthAppAccount", NmsYthAppAccount.class);
		List<NmsYthAppAccount> list = queryList.list();
		session.close();
		
		// 创建PageBean对象返回数据
		PageBean<NmsYthAppAccount> page = new PageBean<NmsYthAppAccount>();
		page.setOrderKey(orderKey);
		page.setOrderValue(Integer.valueOf(orderValue));
		page.setKey("");
		page.setValue("");
	
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

		page.setList(list);

		return page;
	}
	
	@SuppressWarnings("unchecked")
	public List<NmsYthAppAccount> getList(String appId) throws Exception {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_yth_app_account as rt where rt.appId = '" + appId + "'";
		SQLQuery queryList = session.createSQLQuery(sql);
		queryList.addEntity("NmsYthAppAccount", NmsYthAppAccount.class);
		List<NmsYthAppAccount> list = queryList.list();
		session.close();
		return list;
	}
}
