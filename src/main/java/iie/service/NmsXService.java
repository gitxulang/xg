package iie.service;

import iie.domain.AlarmBean;
import iie.pojo.NmsAlarm;
import iie.pojo.NmsAsset;
import iie.pojo.NmsMemInfo;
import iie.pojo.NmsPingInfo;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class NmsXService {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	/**
	 * 以assetid分组，查询每个asset的最大level未处理的报警信息
	 * @return
	 */
	public List<AlarmBean> getAlarms() {
/*		String sql ="select c.id,c.asset_id,c.a_level,c.d_status,c.a_name,c.a_time,nms_asset.a_ip FROM " +
				"(select * from nms_alarm as a " +
				"where a.a_level = (select max(b.a_level) " +
				"from nms_alarm as b " +
				"where a.asset_id= b.asset_id) " +
				"and a.d_status=0 " +
				"GROUP BY a.asset_id) c LEFT JOIN nms_asset on c.asset_id = nms_asset.id";*/
		
		String sql = "select c.id, c.asset_id, c.level, c.d_status, c.a_name, c.a_time, nms_asset.a_ip from  (select id, asset_id, a_level,d_status, a_name, a_time, max(a_level) as level from nms_alarm where d_status=0 group by asset_id) c LEFT JOIN nms_asset on c.asset_id = nms_asset.id";

		Session session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		session.close();
		
		List<AlarmBean> result = new ArrayList<AlarmBean>(0);
		if (list == null || list.size() == 0) {
			return result;
		}

		for (int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			AlarmBean nmsAlarmStaticsDetail = new AlarmBean();
			nmsAlarmStaticsDetail.setId((Integer) objects[0]);
			nmsAlarmStaticsDetail.setAssetId((Integer) objects[1]);
			nmsAlarmStaticsDetail.setALevel((Integer) objects[2]);
			nmsAlarmStaticsDetail.setDStatus((Integer) objects[3]);
			nmsAlarmStaticsDetail.setAName((String) objects[4]);
			nmsAlarmStaticsDetail.setATime((String) objects[5]);
			nmsAlarmStaticsDetail.setIp((String) objects[6]);
			result.add(nmsAlarmStaticsDetail);
		}
		return result;
	}

	/**
	 * 离线查询asset的告警信息
	 * @param nmsAssetId
	 * @param DStatus
	 * @return
	 */
	public List<NmsAlarm> listAlarmByAsset(
			Integer nmsAssetId,Integer DStatus) {
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarm.class);
		criteria.createAlias("nmsAsset", "nmsAsset");
		if (nmsAssetId != null) {
			criteria.add(Restrictions.eq("nmsAsset.id", nmsAssetId));
		}
		if (DStatus != null) {
			criteria.add(Restrictions.eq("DStatus", DStatus));
		}
		/*criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria)
				.get(0);
		criteria.setProjection(null);*/
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		List<NmsAlarm> nmsAlarmList = (List<NmsAlarm>) hibernateTemplate
				.findByCriteria(criteria);
		return nmsAlarmList;
	}

	/**
	 * 返回设备连通率信息
	 * @param assetId
	 * @return
	 */
	public String findPingById(Integer assetId) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Criteria criteria =	session.createCriteria(NmsPingInfo.class);
		criteria.add(Restrictions.eq("nmsAsset.id", assetId));
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		Order order = Order.desc("itime");
		criteria.addOrder(order);
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);
		List<NmsPingInfo> pingInfos = criteria.list();
		session.close();
		
		if (pingInfos == null || pingInfos.size() == 0) {
			return "--";
		}
		NmsPingInfo nmsPingInfo = pingInfos.get(0);
		Float pingRate = nmsPingInfo.getPingRate();

		return pingRate*100+"%";
	}

	/**
	 * 返回cpu使用率信息
	 * @param assetId
	 * @return
	 */
	public String findCpuByAssetId(String assetId){
		String sql ="select AVG(a.cpu_rate),COUNT(id) " +
				"from nms_cpu_info a where a.freq =(select max(freq) from nms_cpu_info where asset_id="+assetId+")" +
				" and a.asset_id="+assetId+"";
		Session session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		session.close();
		
		
		if (null==list||list.size()==0){
			return "--";
		}
		Object[] objects = (Object[]) list.get(0);
		if (null==objects && objects.length==0){
			return "--";
		}
		if (null==objects[0]){
			return "--";
		}
		double rate = (Double) objects[0];
		BigInteger cpuNum = (BigInteger) objects[1];
		double cpuRate = (double) Math.round(rate*100)/100;
		String s = cpuNum.intValue()+"核 "+ cpuRate+" %";
		return s;
	}

	/**
	 * 返回内存使用率信息
	 * @param assetId
	 * @return
	 */
	public String findMeninfoById(String assetId) {
		NmsAsset as=new NmsAsset();
		as.setId(Integer.parseInt(assetId));
		String hsql = "from NmsMemInfo na  where na.nmsAsset= ?  order by na.itime desc";
		@SuppressWarnings("unchecked")
		List<NmsMemInfo> list = (List<NmsMemInfo>) hibernateTemplate.find(hsql,as);
		if (null==list || list.size()==0){
			return "--";
		}
		NmsMemInfo nmsMemInfo = list.get(0);
		Long memFree = nmsMemInfo.getMemFree();
		Long memTotal = nmsMemInfo.getMemTotal();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		double freeMem = memFree * 100d / memTotal;
		String s = decimalFormat.format(100-freeMem );
		s = s + "%";
		return s;
	}

	/**
	 * 返回违规告警
	 * @param assetId
	 * @return
	 */
	public String findWGalarmById(Integer assetId) {

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(NmsAlarm.class);
		criteria.add(Restrictions.eq("nmsAsset.id", assetId));
		criteria.add(Restrictions.eq("AType", "违规告警"));
		criteria.add(Restrictions.eq("DStatus", 0));
		List<NmsAlarm> nmsAlarmList = criteria.list();
		session.close();
		
		
		String result="";
		if (null==nmsAlarmList || nmsAlarmList.size() == 0) {
			result = "正常";
		} else {
			int alarm_level = 0;
			for (int i = 0; i < nmsAlarmList.size(); i++) {
				NmsAlarm eventList = nmsAlarmList.get(i);
				if (eventList.getALevel() > alarm_level) {
					alarm_level = eventList.getALevel();
				}
			}
			if (alarm_level == 1) {
				result = "存在违规";
			} else if (alarm_level == 2) {
				result = "存在违规";
			} else if (alarm_level == 3) {
				result = "存在违规";
			} else {
			}
		}
		return result;
	}

}
