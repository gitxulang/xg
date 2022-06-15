package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsYthSoft;
import iie.tools.PageBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("NmsSoftInfoService")
public class NmsSoftInfoService {

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	HibernateTemplate hibernateTemplate;

	public int getSoftNum(int assetId) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sqlCount = "select count(1) from nms_yth_soft as rt where rt.asset_id = '"
				+ assetId + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<NmsYthSoft> getAll() {
		String hsql = "from NmsYthSoft";
		return (List<NmsYthSoft>) hibernateTemplate.find(hsql);
	}

	public boolean saveSoft(NmsYthSoft np) {
		try {
			np.setId((long) 0);
			hibernateTemplate.save(np);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public NmsAsset findById(int id) {
		String hsql = "from NmsAsset na where na.id = ?";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAsset();
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsYthSoft> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_yth_soft as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
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
		String sqlList = "select * from nms_yth_soft as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthSoft", NmsYthSoft.class);
		List<NmsYthSoft> list = queryList.list();
		session.close();
		
		// 创建PageBean对象返回数据
		PageBean<NmsYthSoft> page = new PageBean<NmsYthSoft>();
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
	public PageBean<NmsYthSoft> getPageByAssetId(String orderKey,
			String orderValue, int begin, int offset, String assetId, String soft_name, String product_type, String unique_ident) {

		if (!nmsAssetService.findOnlineById(Integer.valueOf(assetId))) {
			return new PageBean<NmsYthSoft>();
		}

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

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		// 获取count总数
		StringBuilder sqlCountBuilder = new StringBuilder(
				"select count(1) from nms_yth_soft  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_yth_soft) as b where asset_id= "
						+ assetId + ")");
		sqlCountBuilder.append(" and npi.asset_id = ").append(assetId);

		if (soft_name != null) {
			sqlCountBuilder.append(" and npi.softName like '%").append(soft_name).append("%' "); 
		}
		
		if (product_type != null) {
			sqlCountBuilder.append(" and npi.productType like '%").append(product_type).append("%' "); 
		}
		
		if (unique_ident != null) {
			sqlCountBuilder.append(" and npi.uniqueIdent like '%").append(unique_ident).append("%' "); 
		}
		
		
		SQLQuery queryCount = session
				.createSQLQuery(sqlCountBuilder.toString());
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		// 获取list数据
		String sqlList = "select * from nms_yth_soft  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_yth_soft) as b where asset_id= "
				+ assetId + ") and npi.asset_id = " + assetId;
		
		if (soft_name != null) {
			sqlList += " and npi.softName like '%" + soft_name + "%' "; 
		}
		
		if (product_type != null) {
			sqlList += " and npi.productType = '" + product_type + "' "; 
		}
		
		if (unique_ident != null) {
			sqlList += " and npi.uniqueIdent like '%" + unique_ident + "%' "; 
		}			
		
		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthSoft", NmsYthSoft.class);
		List<NmsYthSoft> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsYthSoft> page = new PageBean<NmsYthSoft>();
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
	public List<NmsYthSoft> getListByAssetId(String assetId, int limit) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sqlList = "select * from nms_yth_soft  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_yth_soft) as b where asset_id= "
				+ assetId + ") and npi.asset_id = " + assetId + " order by id desc limit " + limit;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthSoft", NmsYthSoft.class);
		List<NmsYthSoft> list = queryList.list();
		session.close();
		if (list == null) {
			list = new ArrayList<NmsYthSoft>();
		}
		return list;
	}	
	
	
	@SuppressWarnings("unchecked")
	public List<NmsYthSoft> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId)
			throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "itime";
		}
		if (orderValue == null || orderValue.length() == 0
				|| orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " asc";
		} else {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from nms_yth_soft as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";	
		
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthSoft", NmsYthSoft.class);
		List<NmsYthSoft> list = queryList.list();
		session.close();

		return list;
	}
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_yth_soft where asset_id = " + assetId;
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
