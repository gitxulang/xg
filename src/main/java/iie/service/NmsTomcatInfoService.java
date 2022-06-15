package iie.service;

import iie.pojo.NmsTomcatInfo;
import iie.tools.PageBean;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsTomcatInfoService")
public class NmsTomcatInfoService {
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public NmsTomcatInfo getTomcatById(Integer id) {
		String hsql = "from NmsTomcatInfo nm where nm.nmsAsset.id = " + id;
		List<NmsTomcatInfo> list = (List<NmsTomcatInfo>) hibernateTemplate.find(hsql);
		if(list!=null&&list.size()>0){
			return list.get(list.size()-1);
		}
		else{
			return new NmsTomcatInfo();
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageBean<NmsTomcatInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_tomcat_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

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
		

		// 获取list数据
		String sqlList = "select * from nms_tomcat_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsTomcatInfo", NmsTomcatInfo.class);
		List<NmsTomcatInfo> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsTomcatInfo> page = new PageBean<NmsTomcatInfo>();
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
	public List<NmsTomcatInfo> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "itime";
		}
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " desc";
		} else {
			orderKey = orderKey + "asc";
		}

		// 获取list数据
		String sqlList = "select * from nms_tomcat_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}

		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";	
		
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsTomcatInfo", NmsTomcatInfo.class);
		List<NmsTomcatInfo> list = queryList.list();
		session.close();

		return list;
	}	
		
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_tomcat_info where asset_id = " + assetId;
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
