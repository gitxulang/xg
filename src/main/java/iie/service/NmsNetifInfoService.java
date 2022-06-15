package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsNetifInfo;
import iie.tools.NmsNetifItem;
import iie.tools.PageBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service("NmsNetifInfoService")
public class NmsNetifInfoService {

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsNetifInfo> getAll() {
		String hsql = "from NmsNetifInfo";
		return (List<NmsNetifInfo>) hibernateTemplate.find(hsql);
	}

	public boolean saveMem(NmsNetifInfo nn) {
		try {
			nn.setId((long) 0);
			hibernateTemplate.save(nn);
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
	public PageBean<NmsNetifInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_netif_info as rt where rt.asset_id = '"
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
		String sqlList = "select * from nms_netif_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsNetifInfo", NmsNetifInfo.class);
		List<NmsNetifInfo> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsNetifInfo> page = new PageBean<NmsNetifInfo>();
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
	public NmsNetifInfo getNetifInfoDetail(int id) {
		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		// 获取list数据
		String sqlList = "select * from nms_netif_info as rt where rt.id = '" + id + "'";

		SQLQuery queryList = session.createSQLQuery(sqlList);
		// 序列化数据
		queryList.addEntity("NmsNetifInfo", NmsNetifInfo.class);
		List<NmsNetifInfo> list = queryList.list();
		session.close();
		
		if (list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsNetifItem> getPageByAssetId(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId, String if_descr, String if_ip, String if_physaddr) {

		if (!nmsAssetService.findOnlineById(Integer.valueOf(assetId))) {
			return new PageBean<NmsNetifItem>();
		}

		if (orderKey == null || orderKey == "") {
			orderKey = "id";
		}
		
		if (orderValue == null || orderValue.equals("") || orderValue.equals("0")) { 
			orderKey = orderKey + " asc";
			orderValue = "0";
		} else {
			orderKey = orderKey + " desc";
			orderValue = "1";
		}

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		// 获取count总数
		StringBuilder sqlCountBuilder = new StringBuilder(
				"select count(1) from nms_netif_info  nni where nni.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= "
						+ assetId + ")");
		sqlCountBuilder.append(" and nni.asset_id = ").append(assetId);
		
		if (if_descr != null) {
			sqlCountBuilder.append(" and nni.if_descr like '%").append(if_descr).append("%' "); 
		}
		
		if (if_ip != null) {
			sqlCountBuilder.append(" and nni.if_ip like '%").append(if_ip).append("%' "); 
		}
		
		if (if_physaddr != null) {
			sqlCountBuilder.append(" and nni.if_physaddr like '%").append(if_physaddr).append("%' "); 
		}
		
	//	System.out.println(sqlCountBuilder.toString());
		
		SQLQuery queryCount = session
				.createSQLQuery(sqlCountBuilder.toString());
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		// 获取freq最大值list数据
		String sqlList = "select * from nms_netif_info as nni where nni.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where asset_id= "
				+ assetId + ") and nni.asset_id = '" + assetId + "'";
		
		if (if_descr != null) {
			sqlList += " and nni.if_descr like '%" + if_descr + "%' "; 
		}
		
		if (if_ip != null) {
			sqlList += " and nni.if_ip like '%" + if_ip + "%' "; 
		}
		
		if (if_physaddr != null) {
			sqlList += " and nni.if_physaddr like '%" + if_physaddr + "%' "; 
		}			
		
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsNetifInfo", NmsNetifInfo.class);
		List<NmsNetifInfo> list = queryList.list();

		
		// 获取freq最大值list数据
		String sqlList02 = "select * from nms_netif_info as nni where nni.freq = (select MAX(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where b.asset_id= '"
				+ assetId
				+ "' and b.freq < (select MAX(freq) from (SELECT DISTINCT asset_id,freq FROM nms_netif_info) as b where b.asset_id= '"
				+ assetId + "')) and nni.asset_id = '" + assetId + "'";
		
		if (if_descr != null) {
			sqlList02 += " and nni.if_descr like '%" + if_descr + "%' "; 
		}
		
		if (if_ip != null) {
			sqlList02 += " and nni.if_ip like '%" + if_ip + "%' "; 
		}
		
		if (if_physaddr != null) {
			sqlList02 += " and nni.if_physaddr like '%" + if_physaddr + "%' "; 
		}		
		
		SQLQuery queryList02 = session.createSQLQuery(sqlList02);
		queryList02.addEntity("NmsNetifInfo", NmsNetifInfo.class);
		List<NmsNetifInfo> list02 = queryList02.list();

		// 关闭session
		session.close();

		List<NmsNetifItem> allList = new ArrayList<NmsNetifItem>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				NmsNetifInfo nmsNetifInfo = list.get(i);
				NmsNetifItem nmsNetifItem = new NmsNetifItem();
				nmsNetifItem.setDescription(nmsNetifInfo.getIfDescr());
				// 数据库中存的是Mb
				nmsNetifItem.setIfSpeed(nmsNetifInfo.getIfSpeed());
				if (nmsNetifInfo.getIfSpeed() < 0) {
					nmsNetifInfo.setIfSpeed(0L);
				}
				
				nmsNetifItem.setMAC(nmsNetifInfo.getIfPhysaddr());
				nmsNetifItem.setIp(nmsNetifInfo.getIfIp());
				if (nmsNetifInfo.getIfAdminStatus() == null
						|| nmsNetifInfo.getIfOperStatus() == null) {
					nmsNetifItem.setStatus(0);
				} else {
					if (nmsNetifInfo.getIfAdminStatus() == 3
							|| nmsNetifInfo.getIfOperStatus() == 3) {
						nmsNetifItem.setStatus(3);
					} else if (nmsNetifInfo.getIfAdminStatus() == 2
							|| nmsNetifInfo.getIfOperStatus() == 2) {
						nmsNetifItem.setStatus(2);
					} else if (nmsNetifInfo.getIfAdminStatus() == 1
							&& nmsNetifInfo.getIfOperStatus() == 1) {
						nmsNetifItem.setStatus(1);
					} else {
						nmsNetifItem.setStatus(0);
					}
				}
				double inletVelocity = 0.0;
				double outVelocity = 0.0;
				nmsNetifItem.setInletVelocity(inletVelocity);
				nmsNetifItem.setOutVelocity(outVelocity);
				nmsNetifItem.setInDiscards(0L);
				nmsNetifItem.setInErrors(0L);
				nmsNetifItem.setOutDiscards(0L);
				nmsNetifItem.setOutErrors(0L);
				
				DecimalFormat df = new DecimalFormat(".00");
				for (NmsNetifInfo nmsNetifInfo02 : list02) {
					if (nmsNetifInfo.getIfIndex() == nmsNetifInfo02.getIfIndex()) {
						
						if ((nmsNetifInfo.getItime().getTime() - nmsNetifInfo02.getItime().getTime()) > 0) {
							inletVelocity = (nmsNetifInfo.getIfInOctets() - nmsNetifInfo02.getIfInOctets())
									/ ((nmsNetifInfo.getItime().getTime() - nmsNetifInfo02.getItime().getTime()) / 1000) / 1024.0;
							nmsNetifItem.setInletVelocity(Double.valueOf(df.format(inletVelocity)));
							
							if (nmsNetifItem.getInletVelocity() < 0) {
								nmsNetifItem.setInletVelocity(0);
							}
							
						}
						
						if ((nmsNetifInfo.getItime().getTime() - nmsNetifInfo02.getItime().getTime()) > 0) {
							outVelocity = (nmsNetifInfo.getIfOutOctets() - nmsNetifInfo02.getIfOutOctets())
									/ ((nmsNetifInfo.getItime().getTime() - nmsNetifInfo02.getItime().getTime()) / 1000) / 1024.0;
						
							nmsNetifItem.setOutVelocity(Double.valueOf(df.format(outVelocity)));
							
							if (nmsNetifItem.getOutVelocity() < 0) {
								nmsNetifItem.setOutVelocity(0);
							}
						}
						
						nmsNetifItem.setInDiscards(nmsNetifInfo.getIfInDiscards() - nmsNetifInfo02.getIfInDiscards());
						if (nmsNetifItem.getInDiscards() < 0) {
							nmsNetifItem.setInDiscards(0L);
						}
						nmsNetifItem.setInErrors(nmsNetifInfo.getIfInErrors() - nmsNetifInfo02.getIfInErrors());
						if (nmsNetifItem.getInErrors() < 0) {
							nmsNetifItem.setInErrors(0L);
						}
						nmsNetifItem.setOutDiscards(nmsNetifInfo.getIfOutDiscards() - nmsNetifInfo02.getIfOutDiscards());
						if (nmsNetifItem.getOutDiscards() < 0) {
							nmsNetifItem.setOutDiscards(0L);
						}
						nmsNetifItem.setOutErrors(nmsNetifInfo.getIfOutErrors() - nmsNetifInfo02.getIfOutErrors());
						if (nmsNetifItem.getOutErrors() < 0) {
							nmsNetifItem.setOutErrors(0L);
						}
					}
				}
				if (nmsNetifInfo.getIfSpeed() > 0) {
					double d01 = 100 * ((inletVelocity * 1024 * 8) / (nmsNetifInfo.getIfSpeed() * 1000000));	
					d01 = new BigDecimal(d01).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					if (d01 < 0) {
						nmsNetifItem.setInletSpeedRate(0);
					} else {
						nmsNetifItem.setInletSpeedRate(d01);
					}
					
					if (d01 > 100) {
						nmsNetifItem.setInletSpeedRate(100);
					} else {
						nmsNetifItem.setInletSpeedRate(d01);
					}	
								
					double d02 = 100 * ((outVelocity * 1024 * 8) / (nmsNetifInfo.getIfSpeed() * 1000000));
					d02 = new BigDecimal(d02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					
					if (d02 < 0) {
						nmsNetifItem.setOutSpeedRate(0);
					} else {
						nmsNetifItem.setOutSpeedRate(d02);
					}
					
					if (d02 > 100) {
						nmsNetifItem.setOutSpeedRate(100);
					} else {
						nmsNetifItem.setOutSpeedRate(d02);
					}
					
				} else {
					nmsNetifItem.setInletSpeedRate(0);
					nmsNetifItem.setOutSpeedRate(0);
				}
				
				
				
				
				allList.add(nmsNetifItem);
			}
		}

		// 创建PageBean对象返回数据
		PageBean<NmsNetifItem> page = new PageBean<NmsNetifItem>();
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

		page.setList(allList);

		return page;
	}

	@SuppressWarnings("unchecked")
	public List<NmsNetifInfo> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId)
			throws Exception {

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
		String sqlList = "select * from nms_netif_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsNetifInfo", NmsNetifInfo.class);
		List<NmsNetifInfo> list = queryList.list();
		session.close();

		return list;
	}
	
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_netif_info where asset_id = " + assetId;
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
