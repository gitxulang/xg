package iie.service;

import com.alibaba.druid.util.StringUtils;
import iie.pojo.NmsRoomPage;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NmsRoomPageService")
public class NmsRoomPageService {
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public List<NmsRoomPage> getAll() {
		String hsql = "from NmsRoomPage nrp where 1 = 1";
		return (List<NmsRoomPage>) hibernateTemplate.find(hsql);
	}
	
	public boolean addRoomPage(NmsRoomPage nr){
		//判断roomId是否已经存在
		int roomId = nr.getRoomId();
		String hsql = "from NmsRoomPage nrp where nrp.roomId = "+roomId;
		List<NmsRoomPage> rooms = (List<NmsRoomPage>) hibernateTemplate.find(hsql);
		if (null!=rooms && rooms.size()>0){
			return false;
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into nms_room_page (room_id,room_code,room_desc,room_location) values ("+nr.getRoomId());
		//
		if (!StringUtils.isEmpty(nr.getRoomCode())){
			sqlBuilder.append(",'"+nr.getRoomCode()+"'");
		}else {
			sqlBuilder.append(","+"null");
		}
		if (!StringUtils.isEmpty(nr.getRoomDesc())){
			sqlBuilder.append(",'"+nr.getRoomDesc()+"'");
		}else {
			sqlBuilder.append(","+"null");
		}
		if (!StringUtils.isEmpty(nr.getRoomLocation())){
			sqlBuilder.append(",'"+nr.getRoomLocation()+"'");
		}else {
			sqlBuilder.append(","+"null");
		}
		sqlBuilder.append(")");
		
		Session session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sqlBuilder.toString());
		int i = sqlQuery.executeUpdate();
		session.close();
		
		return i==1;
	}

	public boolean deleteRoom(Integer[] ids){
		SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction ts = session.beginTransaction();
		//设定占位符的个数
		String sql = "";
		for (int i = 0; i < ids.length; i++) {
			if (i == 0) {
				sql = sql + "?";
			} else {
				sql = sql + "," + "?";
			}
		}
		Query query = session.createQuery("DELETE FROM NmsRoomPage WHERE roomId in(" + sql + ")");
		//设置占位符中的参数
		Integer[] ints = new Integer[ids.length];
		for(int i=0; i<ids.length; i++){
			ints[i] = ids[i];
			query.setParameter(i, ints[i]);
		}
		int k = query.executeUpdate();
		ts.commit();
		session.close();
		return k>0;
	}

	public boolean updateRoomPage(Integer id,String webJson){
		try {
			NmsRoomPage roomPage = findById(id);
			roomPage.setWebJson(webJson);
			hibernateTemplate.update(roomPage);

		}catch (Exception e){
			return false;
		}
		return true;
	}

	public boolean deleteRoomPage(Integer id){
		try {
			NmsRoomPage roomPage = findById(id);
			hibernateTemplate.delete(roomPage);
		}catch (Exception e){
			return false;
		}
		return true;
	}

	public NmsRoomPage findById(int id) {
		String hsql = "from NmsRoomPage na where na.roomId = ?";
		try{
			List<NmsRoomPage> list = (List<NmsRoomPage>) hibernateTemplate.find(hsql,id);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}catch (Exception e){
			return null;
		}
		return null;
	}

	public void setDefault(int id){
		SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction ts = session.beginTransaction();
		String sql1 = "update nms_room_page nrp set nrp.is_default='0'";
		SQLQuery sqlQuery1 = session.createSQLQuery(sql1);
		sqlQuery1.executeUpdate();
		String sql2 = "update nms_room_page nrp set nrp.is_default='1' where nrp.room_id="+id;
		SQLQuery sqlQuery2 = session.createSQLQuery(sql2);
		sqlQuery2.executeUpdate();
		ts.commit();
		session.close();
	}

	/**
	 * 返回默认展示的机房ID
	 * @return
	 */
	public Integer getDefaultShowRoomId() {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_room_page where is_default='1'";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.addEntity(NmsRoomPage.class);
		List list = sqlQuery.list();
		session.close();
		
		if (null!=list&& list.size()>0){
			NmsRoomPage roomPage = (NmsRoomPage) list.get(0);
			return roomPage.getRoomId();
		}else {
			return 0;
		}
	}
}
