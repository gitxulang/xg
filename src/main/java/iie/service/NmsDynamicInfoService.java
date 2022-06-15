package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsDynamicInfo;
import iie.tools.NmsDynamicInfoAndWorkingHours;
import iie.tools.PageBean;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsDynamicInfoService")
public class NmsDynamicInfoService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsDynamicInfo> getAll() {
		String hsql = "from NmsDynamicInfo";
		return (List<NmsDynamicInfo>) hibernateTemplate.find(hsql);
	}

	public boolean saveDynamic(NmsDynamicInfo nd) {
		try {
			nd.setId((long) 0);
			hibernateTemplate.save(nd);
		} catch (Exception e) {
			// TODO: handle exception
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
	public PageBean<NmsDynamicInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_dynamic_info as rt where rt.asset_id = '"
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
		String sqlList = "select * from nms_dynamic_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		//序列化数据
		queryList.addEntity("NmsDynamicInfo", NmsDynamicInfo.class);
		List<NmsDynamicInfo> list = queryList.list();
	//	System.out.println(list);

		//关闭session
		session.close();

		//创建PageBean对象返回数据
		PageBean<NmsDynamicInfo> page = new PageBean<NmsDynamicInfo>();
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
	public List<NmsDynamicInfoAndWorkingHours> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "itime";
		}
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " asc";
		} else {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from nms_dynamic_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";	
		
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsDynamicInfo", NmsDynamicInfo.class);
		List<NmsDynamicInfo> list = queryList.list();
		session.close();
		
		
		List<NmsDynamicInfoAndWorkingHours> workingHours = new ArrayList<NmsDynamicInfoAndWorkingHours>();
	//	System.out.println(list);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				NmsDynamicInfoAndWorkingHours nmsDynamicInfoAndWorkingHours = new NmsDynamicInfoAndWorkingHours();
				nmsDynamicInfoAndWorkingHours.setId(list.get(i).getId());
				nmsDynamicInfoAndWorkingHours.setNmsAsset(list.get(i).getNmsAsset());
				
				if (list.get(i).getSysUpdateTime() != null) {
					nmsDynamicInfoAndWorkingHours.setSysUpdateTime(list.get(i).getSysUpdateTime());
				} else {
					nmsDynamicInfoAndWorkingHours.setSysUpdateTime(0L);
				}
				
				nmsDynamicInfoAndWorkingHours.setWorkingHours(formatInterval(list.get(i).getSysUptime()*1000));
				
				
				long date = list.get(i).getItime().getTime() - list.get(i).getSysUptime() * 1000;
				nmsDynamicInfoAndWorkingHours.setSysUptime(date);
				
				
				nmsDynamicInfoAndWorkingHours.setItime(list.get(i).getItime());
				workingHours.add(nmsDynamicInfoAndWorkingHours);
			}
		}


		return workingHours;
	}
	
	private String formatInterval(Long time) {
		StringBuilder formatedInterval = new StringBuilder();
		int day = (int) (time/ (1000 * 60 * 60 * 24));
		int h = (int) (time % (1000 * 60 * 60 * 24)/(1000 * 60 * 60));
		int m = (int) (time % (1000 * 60 * 60)/(1000 * 60));
		int s = (int) (time % (1000 * 60)/1000);
		
		return formatedInterval.append(day).append("天").append(h).append("小时").append(m).append("分钟").append(s).append("秒").toString();
	}
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_dynamic_info where asset_id = " + assetId;
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
