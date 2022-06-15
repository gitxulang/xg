package iie.service;


import iie.pojo.NmsAsset;
import iie.pojo.NmsBmcInfo;
import iie.pojo.NmsCpuInfo;
import iie.tools.PageBean;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsBmcInfoService")
public class NmsBmcInfoService {
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public List<NmsBmcInfo> getAll() {
		String hsql = "from NmsBmcInfo";
		return (List<NmsBmcInfo>) hibernateTemplate.find(hsql);
	}
	
	public boolean saveBmc(NmsBmcInfo nb){
		try {
			nb.setId((long) 0);
			hibernateTemplate.save(nb);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public NmsAsset findById(int id) {
		String hsql = "from NmsAsset na where na.id = ?";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql,id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAsset();
	}
	
	@SuppressWarnings("unchecked")
	public PageBean<NmsBmcInfo> getPageByDate(String orderKey, int orderValue,
			String startDate, String endDate, int begin, int offset) throws Exception{

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from (select * from nms_bmc_info order by itime asc) as rt where rt.itime between '"
				+ startDate + "' and '" + endDate + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());
		

		if (orderValue != 0) {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from (select * from nms_bmc_info order by itime asc) as rt where rt.itime between '"
				+ startDate
				+ "' and '"
				+ endDate
				+ "' order by "
				+ orderKey
				+ " limit " + (begin-1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		//序列化数据
		queryList.addEntity("NmsBmcInfo", NmsBmcInfo.class);
		List<NmsBmcInfo> list = queryList.list();
		
		//关闭session
		session.close();

		//创建PageBean对象返回数据
		PageBean<NmsBmcInfo> page = new PageBean<NmsBmcInfo>();
		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
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

}
