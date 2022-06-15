package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsProcessInfo;
import iie.tools.PageBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("NmsProcessInfoService")
public class NmsProcessInfoService {

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	HibernateTemplate hibernateTemplate;

	public int getProcessNum(int assetId) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sqlCount = "select count(1) from nms_process_info as rt where rt.asset_id = '"
				+ assetId + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<NmsProcessInfo> getAll() {
		String hsql = "from NmsProcessInfo";
		return (List<NmsProcessInfo>) hibernateTemplate.find(hsql);
	}

	public boolean saveProcess(NmsProcessInfo np) {
		try {
			np.setId((long) 0);
			hibernateTemplate.save(np);
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
	public PageBean<NmsProcessInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_process_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());


		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "proc_id";
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
		String sqlList = "select * from nms_process_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsProcessInfo", NmsProcessInfo.class);
		List<NmsProcessInfo> list = queryList.list();
		session.close();

		List<NmsProcessInfo> list01 = new ArrayList<NmsProcessInfo>();

		if (list != null && list.size() > 0) {
			for (NmsProcessInfo nmsProcessInfo : list) {
				nmsProcessInfo.setProcCpu(new BigDecimal(nmsProcessInfo
						.getProcCpu()).setScale(2, BigDecimal.ROUND_HALF_UP)
						.floatValue());
				nmsProcessInfo.setProcMem(new BigDecimal(nmsProcessInfo
						.getProcMem()).setScale(2, BigDecimal.ROUND_HALF_UP)
						.floatValue());
				list01.add(nmsProcessInfo);
			}
		}

		// 创建PageBean对象返回数据
		PageBean<NmsProcessInfo> page = new PageBean<NmsProcessInfo>();
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

		page.setList(list01);

		return page;
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsProcessInfo> getPageByAssetId(String orderKey,
			String orderValue, int begin, int offset, String assetId, String proc_name, String proc_id, String proc_path) {

		if (!nmsAssetService.findOnlineById(Integer.valueOf(assetId))) {
            return new PageBean<NmsProcessInfo>();
		}

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "proc_id";
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
				"select count(1) from nms_process_info  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_process_info) as b where asset_id= "
						+ assetId + ")");
		sqlCountBuilder.append(" and npi.asset_id = ").append(assetId);


		if (proc_name != null) {
			sqlCountBuilder.append(" and npi.proc_name like '%").append(proc_name).append("%' ");
		}

		if (proc_id != null) {
			sqlCountBuilder.append(" and npi.proc_id = '").append(proc_id).append("' ");
		}

		if (proc_path != null) {
			sqlCountBuilder.append(" and npi.proc_path like '%").append(proc_path).append("%' ");
		}

		SQLQuery queryCount = session
				.createSQLQuery(sqlCountBuilder.toString());
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		// 获取list数据
		String sqlList = "select * from nms_process_info  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_process_info) as b where asset_id= "
				+ assetId + ") and npi.asset_id = " + assetId;

		if (proc_name != null) {
			sqlList += " and npi.proc_name like '%" + proc_name + "%' ";
		}

		if (proc_id != null) {
			sqlList += " and npi.proc_id = '" + proc_id + "' ";
		}

		if (proc_path != null) {
			sqlList += " and npi.proc_path like '%" + proc_path + "%' ";
		}

		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsProcessInfo", NmsProcessInfo.class);
		List<NmsProcessInfo> list = queryList.list();
		session.close();

		List<NmsProcessInfo> list01 = new ArrayList<NmsProcessInfo>();

		if (list != null && list.size() > 0) {
			for (NmsProcessInfo nmsProcessInfo : list) {
				nmsProcessInfo.setProcCpu(new BigDecimal(nmsProcessInfo
						.getProcCpu()).setScale(2, BigDecimal.ROUND_HALF_UP)
						.floatValue());
				nmsProcessInfo.setProcMem(new BigDecimal(nmsProcessInfo
						.getProcMem()).setScale(2, BigDecimal.ROUND_HALF_UP)
						.floatValue());
				list01.add(nmsProcessInfo);
			}
		}

		// 创建PageBean对象返回数据
		PageBean<NmsProcessInfo> page = new PageBean<NmsProcessInfo>();
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

		page.setList(list01);

		return page;
	}

	@SuppressWarnings("unchecked")
	public List<NmsProcessInfo> getPageByDateExportExcel(String orderKey,
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
		String sqlList = "select * from nms_process_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}

		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";

		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsProcessInfo", NmsProcessInfo.class);
		List<NmsProcessInfo> list = queryList.list();
		session.close();

		return list;
	}

	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_process_info where asset_id = " + assetId;
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
