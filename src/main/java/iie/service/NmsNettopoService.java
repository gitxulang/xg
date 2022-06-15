package iie.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import iie.pojo.NmsAlarm;
import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsCpuInfo;
import iie.pojo.NmsDiskioInfo;
import iie.pojo.NmsMemInfo;
import iie.pojo.NmsNetifInfo;
import iie.pojo.NmsPingInfo;
import iie.pojo.NmsTopoArea;
import iie.pojo.NmsTopoLink;
import iie.pojo.NmsTopoMap;
import iie.pojo.NmsTopoMeta;
import iie.pojo.NmsTopoNode;
import iie.tools.PageBean;

@Service("NmsNettopoService")
public class NmsNettopoService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	/**
	 * 查询下拉框内容
	 * 
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<NmsTopoMap> getAll() {
		String hsql = "    from NmsTopoMap na";
		return (List<NmsTopoMap>) hibernateTemplate.find(hsql);

	}

	public int getId(int map, String start_node_ip, String start_node_index, String end_node_ip, String end_node_index) {
		int id = 0;

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select id from nms_topo_link where map_id = " + map + " and s_ip = '" + start_node_ip + "' and s_index = " + start_node_index + " and e_ip = '" + end_node_ip + "' and e_index = " + end_node_index;
	
		SQLQuery queryList = session.createSQLQuery(sql);
		
		if (queryList.list() != null && queryList.list().size() > 0) {
			id = (int) queryList.list().get(0);
		}
		
		session.close();		
		
		return id;
	}
	
	
	/**
	 * 根据拓扑图查询节点信息
	 * 
	 * @param id
	 * @return
	 */
	public List<NmsTopoNode> findnodeById(int mapId) {

		String hsql = " from NmsTopoNode na where na.mapId = ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoNode> list = (List<NmsTopoNode>) hibernateTemplate.find(
				hsql, mapId);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;

	}

	/**
	 * 根据拓扑图查询节点信息和节点ID值查看是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean findnodeByMapIDAndNodeID(int mapId, String nodeId) {


		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_topo_node where map_id = '" + mapId
				+ "' and node_id = '" + nodeId + "' limit 1";
		
		SQLQuery queryList = session.createSQLQuery(sql);
		if (queryList.list() != null && queryList.list().size() > 0) {
			session.close();
			return true;
		}
		session.close();

		return false;
	}
	
	
	/**
	 * 查询链路信息
	 * 
	 * @param id
	 * @return
	 */
	public NmsTopoLink findlinkById(int id) {
		String hsql = " from NmsTopoLink na where na.id = ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoLink> list = (List<NmsTopoLink>) hibernateTemplate.find(
				hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 添加子拓扑
	 * 
	 * @param na
	 * @return
	 */

	public boolean addtopoMap(NmsTopoMap na) {
		try {
			na.setId((int) 0);
			hibernateTemplate.save(na);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 根据id查询拓扑图
	 * 
	 * @param id
	 * @return
	 */
	public NmsTopoMap findById(int id) {
		String hsql = "from NmsTopoMap na where na.id = ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据t_name查询拓扑图
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int findByName(String name) {
		int id = -1;
		String hsql = "  from NmsTopoMap where TName = ?";
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql, name);
		
		if (list != null && list.size() > 0) {
			id = list.get(0).getId();
		}
		return id;		
	}	
	
	/**
	 * 修改拓扑信息
	 * 
	 * @param na
	 * @return
	 */

	public boolean updateTopo(NmsTopoMap na) {
		try {
			hibernateTemplate.saveOrUpdate(na);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 查询虚拟节点
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NmsTopoMeta> findallMeta() {

		String hsql = "    from NmsTopoMeta na";
		return (List<NmsTopoMeta>) hibernateTemplate.find(hsql);

		// return null;
	}

	/**
	 * 保存虚拟节点
	 */
	public boolean addtopoNode(NmsTopoNode na) {
		try {

			na.setId((int) 0);
			hibernateTemplate.save(na);

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 根据mapid查询ip
	 */
	public List<NmsTopoNode> findBymapId(int mapId) {
		String hsql = "from NmsTopoNode na where na.mapId = ? and nodeId like % 'net' ";
		@SuppressWarnings("unchecked")
		List<NmsTopoNode> list = (List<NmsTopoNode>) hibernateTemplate.find(
				hsql, mapId);

		return list;
	}

	/**
	 * 查询资产ip
	 * 
	 * @param ip
	 * @return
	 */
	public List<NmsAsset> findByaIp(String ip) {

		String hsql = "from NmsAsset na where na.AIp not EXISTS( select AIp from  NmsAsset o where o.AIp = ? ";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, ip);

		return list;
	}

	public boolean deletelink(String id) {
		try {
			
			Session session = hibernateTemplate.getSessionFactory().openSession();
			String sql = "delete from nms_topo_link where id = " + id;
			SQLQuery queryList = session.createSQLQuery(sql);

			if (queryList.executeUpdate() == 0) {
				session.close();
				return false;
			}
			session.close();				

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 根据id查询node信息
	 * 
	 * @param id
	 * @return
	 */
	public List<NmsTopoNode> selectNodeById(String mapId) {

		List<NmsTopoNode> NmsTopoNodeRecordList = new ArrayList<NmsTopoNode>();

		Session session = hibernateTemplate.getSessionFactory().openSession();

		String SQL = "select nat.alias,nat.height,nat.id,nat.img,nat.ip,nat.map_id,nat.node_id,nat.type_id,nat.rel_map,nat.width,nat.x,nat.y,nat.container_id from nms_topo_node nat"
				+ "  left join nms_topo_map na on nat.map_id = na.id where  na.id =:id";

		SQLQuery nodeSQLQuery = session.createSQLQuery(SQL);

		nodeSQLQuery.setString("id", mapId);
		
		if (mapId == null || mapId.equals("")) {
			System.out.println("[ERROR] mapId == null or mapId == ''"); 
			session.close();
			return NmsTopoNodeRecordList;
		}
		
		NmsTopoMap map = new NmsTopoMap();
		
		try {
			map.setId(Integer.parseInt(mapId));
		} catch (NumberFormatException e) {
			System.out.println("[ERROR] " + e.toString());
			session.close();
			
			return NmsTopoNodeRecordList;
		}

		List nms = nodeSQLQuery.list();
		session.close();

		if (nms != null && nms.size() > 0) {
			for (int i = 0; i < nms.size(); i++) {
				Object[] objects = (Object[]) nms.get(i);

				NmsAssetType type = new NmsAssetType();

				type.setId((Integer) objects[7]);

				NmsTopoNode nmsTopoNode = new NmsTopoNode();
				nmsTopoNode.setAlias((String) objects[0]);
				nmsTopoNode.setHeight((Integer) objects[1]);
				nmsTopoNode.setId((Integer) objects[2]);
				nmsTopoNode.setImg((String) objects[3]);
				nmsTopoNode.setIp((String) objects[4]);
				nmsTopoNode.setNmsTopoMap(map);
				nmsTopoNode.setNodeId((String) objects[6]);
				nmsTopoNode.setNmsAssetType(type);
				nmsTopoNode.setRelMap((String) objects[8]);
				nmsTopoNode.setWidth((Integer) objects[9]);
				nmsTopoNode.setX((Integer) objects[10]);
				nmsTopoNode.setY((Integer) objects[11]);
				nmsTopoNode.setContainerId((Integer) objects[12]);
				NmsTopoNodeRecordList.add(nmsTopoNode);
			}
		}

		
		return NmsTopoNodeRecordList;
	}

	/**
	 * 根据ID查询链路信息
	 * 
	 * @param id
	 * @return
	 */
	public List<NmsTopoLink> selectlinkById(String mapId) {

		List<NmsTopoLink> NmsTopoLinkRecordList = new ArrayList<NmsTopoLink>();

		Session session = hibernateTemplate.getSessionFactory().openSession();

		String SQL = "select nat.e_desc,nat.e_index,nat.e_ip,nat.e_mac,nat.e_node_id,nat.id,nat.l_dash,nat.l_name,nat.l_offset,nat.l_type,nat.l_width,nat.map_id,nat.s_desc ,nat.s_index ,nat.s_ip,nat.s_mac ,nat.s_node_id, nat.col1, nat.col2 from nms_topo_link nat"
				+ "  left join nms_topo_map na on nat.map_id = na.id where  na.id =:id";

		SQLQuery nodeSQLQuery = session.createSQLQuery(SQL);

		nodeSQLQuery.setString("id", mapId);

		NmsTopoMap map = new NmsTopoMap();
		map.setId(Integer.parseInt(mapId));

		List link = nodeSQLQuery.list();
		session.close();

		if (link != null && link.size() > 0) {
			for (int i = 0; i < link.size(); i++) {
				Object[] objects = (Object[]) link.get(i);
				NmsTopoLink NmsTopoLink = new NmsTopoLink();

				NmsTopoLink.setEDesc((String) objects[0]);
				NmsTopoLink.setEIndex((String) objects[1]);
				NmsTopoLink.setEIp((String) objects[2]);
				NmsTopoLink.setEMac((String) objects[3]);
				NmsTopoLink.setENodeId((String) objects[4]);
				NmsTopoLink.setId((Integer) objects[5]);
				NmsTopoLink.setLDash((String) objects[6]);
				NmsTopoLink.setLName((String) objects[7]);
				NmsTopoLink.setLOffset((Integer) objects[8]);    // bundleGap       链路偏移量
				NmsTopoLink.setLType((Integer) objects[9]);
				NmsTopoLink.setLWidth((Integer) objects[10]);
				NmsTopoLink.setNmsTopoMap(map);
				NmsTopoLink.setSDesc((String) objects[12]);
				NmsTopoLink.setSIndex((String) objects[13]);
				NmsTopoLink.setSIp((String) objects[14]);
				NmsTopoLink.setSMac((String) objects[15]);
				NmsTopoLink.setSNodeId((String) objects[16]);
 
				NmsTopoLink.setCol1((String) objects[17]);       // dashedPattern   链路显示实线虚线: 0表示实线, 非0表示虚线
				NmsTopoLink.setCol2((String) objects[18]);       // strokeColor     链路初始化颜色

				NmsTopoLinkRecordList.add(NmsTopoLink);
			}
		}

		return NmsTopoLinkRecordList;
	}

	/**
	 * 根据name查询拓扑id
	 * 
	 * @param current_map_name
	 * @return
	 */
	public List<NmsTopoMap> findBymapName(String current_map_name) {
		String hsql = "from NmsTopoMap na where na.TName like ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				current_map_name);

		return list;
	}

	/**
	 * 删除node
	 * 
	 * @param current_map_id
	 */
	public boolean deletenode(Integer current_map_id, String nodeId) {
		// TODO Auto-generated method stub
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete from NmsTopoNode na where na.nmsTopoMap = :mapId and na.nodeId= :nodeId ");
			q.setInteger("mapId", current_map_id).setString("nodeId", nodeId);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	
	/**
	 * 删除node
	 * 
	 * @param current_map_id
	 */
	public boolean deleteallnode(String nodeId) {
		// TODO Auto-generated method stub
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete from NmsTopoNode na where na.nodeId= :nodeId ");
			q.setString("nodeId", nodeId);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}	
	
	
	public boolean deletelink(Integer current_map_id, String nodeId) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete from NmsTopoLink na where  na.nmsTopoMap = :mapId and ( na.SNodeId = :nodeId or na.ENodeId = :nodeId )");
			q.setInteger("mapId", current_map_id).setString("nodeId", nodeId);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	
	public boolean deletealllink(String nodeId) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete from NmsTopoLink na where ( na.SNodeId = :nodeId or na.ENodeId = :nodeId )");
			q.setString("nodeId", nodeId);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}	
	
	/**
	 * 获取ID最大值
	 * 
	 * @return
	 */
	public Integer getMaxId() {
		String hql = "select max(sa.id) from NmsTopoNode sa";
		List<Integer> lst = new ArrayList<Integer>();
		lst = (List<Integer>) hibernateTemplate.find(hql);

		if (lst.get(0) == null) {
			lst.set(0, 0);
		}

		return lst.get(0);

	}

	public NmsAssetType findByassettypeId(int typeId) {
		String hsql = "from NmsAssetType na where na.id = ? ";
		@SuppressWarnings("unchecked")
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate.find(
				hsql, typeId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Integer getMaxlinkId() {
		String hql = "select max(sa.id) from NmsTopoLink sa";
		List<Integer> lst = new ArrayList<Integer>();
		lst = (List<Integer>) hibernateTemplate.find(hql);
		if (lst.get(0) == null) {
			lst.set(0, 0);
		}
		return lst.get(0);
	}

	/**
	 * 添加虚拟链路
	 * 
	 * @param link
	 * @return
	 */
	public boolean addHintLink(NmsTopoLink link) {
		try {

			link.setId((int) 0);
			hibernateTemplate.save(link);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param current_topo_map
	 * @param node_id
	 * @return
	 */
	public List<NmsTopoMap> selectmapById(String current_topo_map) {
		String hsql = "from NmsTopoMap na where na.TName = ?  ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				current_topo_map);

		return list;
	}

	/**
	 * 更新关联子图
	 * 
	 * @param node_id
	 * @param submap_name
	 * @param mapId
	 * @return
	 */
	public boolean updateRelation(String node_id, String submap_name,
			String mapId) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createSQLQuery("update nms_topo_node t set t.rel_map = :submapName where t.node_id = :nodeId and t.map_id= :mapId ");
			q.setParameter("submapName", submap_name)
					.setParameter("nodeId", node_id)
					.setParameter("mapId", mapId).executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 取消关联
	 * 
	 * @param node_id
	 * @param submap_name
	 * @param mapId
	 * @return
	 */
	public boolean updateCencel(String node_id, String submap_name, String mapId) {
		try {

			String submapName = "";
			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createSQLQuery("update nms_topo_node t set t.rel_map = :submapName   where t.node_id = :nodeId and t.map_id= :mapId");
			q.setParameter("submapName", submapName)
					.setParameter("nodeId", node_id)
					.setParameter("mapId", mapId).executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param mapName
	 * @param nodeId
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean updateEntityNodeToTable(String mapId, String nodeId,
			String x, String y) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createSQLQuery("update nms_topo_node t set t.x = :x ,  t.y= :y  where t.node_id = :nodeId and t.map_id= :mapId");
			q.setParameter("x", x).setParameter("y", y)
					.setParameter("nodeId", nodeId)
					.setParameter("mapId", mapId).executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public boolean addentityLink(NmsTopoLink link) {
		try {
		//	link.setId((int) 0);
			hibernateTemplate.save(link);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 修改节点名称
	 * 
	 * @param node_id
	 * @param node_alias
	 * @param mapId
	 * @return
	 */
	public boolean updateNodeAlias(String node_id, String node_alias,
			String mapId) {

		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createSQLQuery("update nms_topo_node t set t.alias = :alias  where t.node_id = :nodeId and t.map_id= :mapId ");
			q.setParameter("alias", node_alias).setParameter("nodeId", node_id)
					.setParameter("mapId", mapId);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param map_id
	 */
	public boolean deletelinkById(int map_id) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete  NmsTopoLink na where na.nmsTopoMap = :mapId ");
			q.setInteger("mapId", map_id);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public boolean deletenodeById(int map_id) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete from NmsTopoNode na where na.nmsTopoMap = :mapId ");
			q.setInteger("mapId", map_id);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public boolean deletemapById(int map_id) {
		try {

			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery(" delete from NmsTopoMap na where na.id = :mapId ");
			q.setInteger("mapId", map_id);
			q.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public boolean updatenodeById(String current_topo_map) {
		try {

			String relmap = "";
			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createQuery("update NmsTopoNode t set t.relMap = ''  where t.relMap = :currentTopomap ");
			q.setParameter("currentTopomap", current_topo_map).executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 查询节点信息通过mapid
	 * 
	 * @param mapId
	 * @param start_node_id
	 * @return
	 */
	public List<NmsTopoNode> findNodeById(NmsTopoMap nmsmap,
			String start_node_id) {
		String hsql = "from NmsTopoNode na where na.nmsTopoMap = ?  and na.nodeId= ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoNode> list = (List<NmsTopoNode>) hibernateTemplate.find(
				hsql, nmsmap, start_node_id);

		return list;
	}

	/**
	 * 通过name查询图id
	 * 
	 * @param current_topo_map
	 * @return
	 */
	public List<NmsTopoMap> findmapByname(String current_topo_map) {
		String hsql = "from NmsTopoMap na where na.TName = ?   ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				current_topo_map);

		return list;
	}

	/**
	 * 查询全部资产
	 * 
	 * @return
	 */
	public List<NmsAsset> findasset(String map_id) {

		Session session = (hibernateTemplate.getSessionFactory())
				.getCurrentSession();
	//	String hql = "from NmsAsset na where na.AIp not in (select nb.ip from NmsTopoNode nb where nb.nmsTopoMap= ? )";
		String hql = "from NmsAsset where deled = 0";
		Query query = session.createQuery(hql);
	//	query.setString(0, map_id);
		List<NmsAsset> list = query.list();

		return list;
	}

	public List<NmsTopoNode> findNodeBymapId(NmsTopoMap map) {
		String hsql = "from NmsTopoNode na  where na.nmsTopoMap = ?  ";
		@SuppressWarnings("unchecked")
		List<NmsTopoNode> list = (List<NmsTopoNode>) hibernateTemplate.find(
				hsql, map);

		return list;
	}

	/**
	 * 查询资产类型（全部）
	 * 
	 * @return
	 */
	public List<NmsAssetType> findassetType() {
		String hsql = "from NmsAssetType na    ";
		@SuppressWarnings("unchecked")
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate
				.find(hsql);

		return list;
	}

	public List<NmsAsset> findassetTypeById(String start_node_id) {
		String hsql = "from NmsAsset na  where na.id= ?  ";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql,
				Integer.parseInt(start_node_id));

		return list;
	}

	public List<NmsAsset> findassetEndById(String end_node_id) {
		String hsql = "from NmsAsset na  where na.id= ?  ";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql,
				Integer.parseInt(end_node_id));

		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findInterfaceById(String asset_id) {
		if (asset_id == null) {
			return null;
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_netif_info as a where a.freq = (select max(freq) from nms_netif_info b where b.asset_id = " + asset_id + ") and a.asset_id = " + asset_id;
		
		SQLQuery queryList = session.createSQLQuery(sql);
		List<NmsNetifInfo> list = null;
		if (queryList.list() != null && queryList.list().size() > 0) {
			queryList.addEntity("NmsNetifInfo", NmsNetifInfo.class);
			list = queryList.list();
		}
		session.close();
		
		return list;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findRepeatLink(String map_id, String start_node_id,
			String start_node_index, String end_node_id, String end_node_index) {

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_topo_link where map_id = '" + map_id
				+ "' and s_node_id = '" + start_node_id + "' and s_index = '"
				+ start_node_index + "' and e_node_id ='" + end_node_id
				+ "' and e_index = '" + end_node_index + "' limit 1";
		
		SQLQuery queryList = session.createSQLQuery(sql);
		List<NmsTopoLink> list = null;
		if (queryList.list() != null && queryList.list().size() > 0) {
			queryList.addEntity("NmsTopoLink", NmsTopoLink.class);
			list = queryList.list();
		}
		session.close();

		return list;
	}	
	
	public List<NmsTopoLink> findlinkedById(String id) {
		String hsql = "from NmsTopoLink na  where na.id= ?  ";
		@SuppressWarnings("unchecked")
		List<NmsTopoLink> list = (List<NmsTopoLink>) hibernateTemplate.find(
				hsql, Integer.parseInt(id));

		return list;
	}

	public List<NmsNetifInfo> findinfoById(String start_node_id,
			String start_Index) {

		Session session = (hibernateTemplate.getSessionFactory()).getCurrentSession();
		String hql = "from NmsNetifInfo na where na.nmsAsset= ?  and  na.ifIndex= ? order by itime desc";
		Query query = session.createQuery(hql);

		query.setString(0, start_node_id);
		query.setString(1, start_Index);
		List<NmsNetifInfo> list = query.list();

		return list;

	}
	

	public List<NmsNetifInfo> find2infoById(String start_node_id, String start_Index) {

		if (start_node_id == null || start_Index == null) {
			return null;
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_netif_info as a where asset_id = " + start_node_id + " and if_index = " + start_Index + " order by itime desc limit 2";

		SQLQuery queryList = session.createSQLQuery(sql);
		List<NmsNetifInfo> list = null;
		if (queryList.list() != null && queryList.list().size() > 0) {
			queryList.addEntity("NmsNetifInfo", NmsNetifInfo.class);
			list = queryList.list();
		}
		
		session.close();

		return list;

	}	

	/**
	 * 查询assettype通过id
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<NmsAsset> findassetBynodeId(String nodeId) {
		String hsql = "from NmsAsset na  where na.id = ? and deled = 0";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql,
				Integer.parseInt(nodeId));

		return list;
	}

	public List<NmsAlarm> findalarmById(String nodeId) {
		String hsql = "from NmsAlarm na  where na.id= ? and na.DStatus = 0 and na.AType = '系统轮询'  ";
		@SuppressWarnings("unchecked")
		List<NmsAlarm> list = (List<NmsAlarm>) hibernateTemplate.find(hsql,
				Integer.parseInt(nodeId));

		return list;
	}

	public List<NmsAlarm> findwealarmById(String nodeId) {
		
		List<NmsAlarm>  list = new ArrayList();
		if (nodeId == null) {
			return list;
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_alarm as a where asset_id = " + nodeId + " and d_status = 0";
		

		SQLQuery queryList = session.createSQLQuery(sql);
		if (queryList.list() != null && queryList.list().size() >= 0) {
			queryList.addEntity("NmsAlarm", NmsAlarm.class);
			list = queryList.list();
		}
		
		session.close();

		return list;
	}

	public List<NmsAssetType> findtypeById(String node_subtype) {
		String hsql = "from NmsAssetType na  where na.chSubtype= ?  ";
		@SuppressWarnings("unchecked")
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate.find(
				hsql, node_subtype);

		return list;
	}

	public List<NmsPingInfo> findpingById(String nodeId) {
		List<NmsPingInfo>  list = null;
		if (nodeId == null) {
			return list;
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_ping_info as a where asset_id = " + nodeId + " order by id desc limit 1";

		SQLQuery queryList = session.createSQLQuery(sql);
		if (queryList.list() != null && queryList.list().size() > 0) {
			queryList.addEntity("NmsPingInfo", NmsPingInfo.class);
			list = queryList.list();
		}
		session.close();
		
		return list;		
	}

	public List<NmsCpuInfo> findcpuById(String nodeId) {

		List<NmsCpuInfo>  list = null;
		if (nodeId == null) {
			return list;
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_cpu_info as a where freq = (select max(freq) from nms_cpu_info b where b.asset_id = " + nodeId + ") and a.asset_id = " + nodeId;

		SQLQuery queryList = session.createSQLQuery(sql);
		if (queryList.list() != null && queryList.list().size() > 0) {
			queryList.addEntity("NmsCpuInfo", NmsCpuInfo.class);
			list = queryList.list();
		}
		session.close();
		
		return list;	
	}

	public List<NmsMemInfo> findmeminfoById(String nodeId) {

		List<NmsMemInfo> list = null;
		if (nodeId == null) {
			return list;
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select * from nms_mem_info as a where a.asset_id = " + nodeId + " order by id desc limit 1";

		SQLQuery queryList = session.createSQLQuery(sql);
		if (queryList.list() != null && queryList.list().size() > 0) {
			queryList.addEntity("NmsMemInfo", NmsMemInfo.class);
			list = queryList.list();
		}
		session.close();
		
		return list;		
	
	}

	public NmsTopoMap findBycurrent_topo_map(String current_topo_map) {
		String hsql = "from NmsTopoMap na where na.TName = ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				current_topo_map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询虚拟节点
	 * 
	 * @return
	 */
	public List<NmsTopoMeta> findByTopoMeta() {
		String hsql = "    from NmsTopoMeta na";
		return (List<NmsTopoMeta>) hibernateTemplate.find(hsql);
	}

	/**
	 * 修改链路宽度
	 * 
	 * @param link_id
	 * @param link_width
	 * @param mapId
	 * @return
	 */
	public boolean updatelinkWidth(String link_id, String link_width,
			String mapId) {
		try {

			link_id = link_id.substring(5);
			Session session = (hibernateTemplate.getSessionFactory())
					.getCurrentSession();
			Query q = session
					.createSQLQuery("update nms_topo_link t set t.l_width = :linkwidth   where t.id = :linkId and t.map_id= :mapId ");
			q.setParameter("linkwidth", link_width)
					.setParameter("linkId", link_id)
					.setParameter("mapId", mapId).executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 查询资产带入条件（key，value）
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public List<NmsAsset> findassetBycondition(String key, String value) {
		if (key.equals("ip")) {
			Session session = (hibernateTemplate.getSessionFactory()).getCurrentSession();
			String hql = "from NmsAsset na where na.AIp like ? and deled = 0";
			Query query = session.createQuery(hql);
			query.setString(0, "%" + value + "%");
			List<NmsAsset> list = query.list();
			return list;
		} else if (key.equals("name")) {
			Session session = (hibernateTemplate.getSessionFactory()).getCurrentSession();
			String hql = "from NmsAsset na where na.AName like ? and deled = 0";
			Query query = session.createQuery(hql);
			query.setString(0, "%" + value + "%");
			List<NmsAsset> list = query.list();
			return list;
		} else if (key.equals("no")) {
			Session session = (hibernateTemplate.getSessionFactory()).getCurrentSession();
			String hql = "from NmsAsset na where na.ANo like ? and deled = 0";
			Query query = session.createQuery(hql);
			query.setString(0, "%" + value + "%");
			List<NmsAsset> list = query.list();
			return list;
		} else {
			Session session = (hibernateTemplate.getSessionFactory()).getCurrentSession();
			String hql = "from NmsAsset na where deled = 0";
			Query query = session.createQuery(hql);
			List<NmsAsset> list = query.list();
			return list;
		}
	}

	/**
	 * 是否采集
	 * 
	 * @param start_node_id
	 * @return
	 */
	public List<NmsAsset> nodeIsNotMonitor(String start_node_id) {

		String hsql = " from NmsAsset na where na.id = ? ";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql,
				Integer.parseInt(start_node_id));
		return list;

	}

	public List<NmsDiskioInfo> finddiskById(String node_id) {

		NmsAsset as = new NmsAsset();
		as.setId(Integer.parseInt(node_id));

		String hsql = "from NmsDiskioInfo na  where na.nmsAsset= ?  ";
		@SuppressWarnings("unchecked")
		List<NmsDiskioInfo> list = (List<NmsDiskioInfo>) hibernateTemplate
				.find(hsql, as);

		return list;

	}

	/**
	 * 查询告警事件（条件查询）
	 * 
	 * @param nodeId
	 * @return
	 */
	public int findeventlistById(String nodeId) {

		int level = 0;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sql = "select max(a_level) from nms_alarm as a where a.asset_id = " + nodeId + " and d_status = 0 limit 1";

		SQLQuery queryList = session.createSQLQuery(sql);
		if (queryList.list() != null && queryList.list().size() > 0) {
			if (queryList.list().size() > 0 && queryList.list().get(0) != null) {
				level = (int) queryList.list().get(0);
			}
		}
		session.close();		

		return level;
	}

	/**
	 * NmsNetifInfo(条件查询)
	 * 
	 * @param node_id
	 * @return
	 */

	public List<NmsNetifInfo> findNetifById(String node_id) {

		NmsAsset as = new NmsAsset();
		as.setId(Integer.parseInt(node_id));

		String hsql = "from NmsNetifInfo na where na.nmsAsset = ?  ";
		@SuppressWarnings("unchecked")
		List<NmsNetifInfo> list = (List<NmsNetifInfo>) hibernateTemplate.find(
				hsql, as);
		return list;

	}

	/**
	 * 判断选中的状态
	 * 
	 * @param name
	 * @return
	 */

	public NmsTopoMap findmapByName(String name) {
		String hsql = "from NmsTopoMap na where na.TName = ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				name);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public boolean updateNmsTopoMap(NmsTopoMap na) {
		try {
			hibernateTemplate.saveOrUpdate(na);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 查询不被选中的图
	 * 
	 * @param name
	 * @return
	 */
	public List<NmsTopoMap> findmapNotselected(String name) {
		String hsql = "from NmsTopoMap na where na.TName != ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoMap> list = (List<NmsTopoMap>) hibernateTemplate.find(hsql,
				name);

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<NmsTopoArea> findAllArea(String mapId) {
		String hsql = "from NmsTopoArea na where na.deled = 0 and mapId = " + mapId;
		List<NmsTopoArea> list = (List<NmsTopoArea>) hibernateTemplate.find(hsql);
		if (list == null) {
			return new ArrayList<NmsTopoArea>();
		}

		return list;
	}

	public NmsTopoArea findAreaByDivId(int id) {
		String hsql = "from NmsTopoArea na where na.id = ? ";
		@SuppressWarnings("unchecked")
		List<NmsTopoArea> list = (List<NmsTopoArea>) hibernateTemplate.find(hsql,id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 通过拓扑域和名称获取拓扑对象
	 * @param divName
	 * @return
	 */
	public NmsTopoArea findAreaByDivNameAndMapId(String divName, Integer mapId) {
		String hsql = "from NmsTopoArea na where na.deled = 0  and na.divName = ? and na.mapId = ?";
		@SuppressWarnings("unchecked")
		List<NmsTopoArea> list = (List<NmsTopoArea>) hibernateTemplate.find(hsql, divName, mapId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 更改可用域
	 * @param na
	 * @return
	 */
	public Map<String, String> updateArea(NmsTopoArea na) {
		Map<String, String> data = new HashMap<String, String>();
		String divName = na.getDivName();
		if (StringUtils.isBlank(divName)) {
			data.put("state", "1");
			data.put("info", "修改失败!修改的名称禁止为空");
			return data;
		}
		
		// 1、通过name查询,是否重复
		NmsTopoArea updateNms = this.findAreaByDivNameAndMapId(divName, na.getMapId());

		// 2、重复的数据id和当前id是否一样，如果一样则提示信息，不用修改
		Integer sourceId = na.getId();
		if (updateNms != null) {
			Integer targetId = updateNms.getId();
			if (Objects.equals(sourceId, targetId)) {
				data.put("state", "0");
				data.put("info", "修改成功！但名称没有任何变化");
				return data;
			} else {
				data.put("state", "1");
				data.put("info", "修改失败！已经存在相同名称的可用域，请尝试修改不同名称");
				return data;
			}
		} else {
			updateNms = this.findAreaByDivId(sourceId);
			updateNms.setDivName(divName);
		}
		
		// 3、修改
		try {
			hibernateTemplate.saveOrUpdate(updateNms);
		} catch (Exception e) {
			data.put("state", "1");
			data.put("info", "修改失败!更新异常，请重新尝试");
			return data;
		}

		data.put("state", "0");
		data.put("info", "修改成功!");
		return data;
	}
	
	public boolean updateAreaByDivId(NmsTopoArea na) {
		try {
			hibernateTemplate.saveOrUpdate(na);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean addTopoArea(NmsTopoArea na) {
		try {
			Session session = (hibernateTemplate.getSessionFactory()).getCurrentSession();
			Query q = session.createSQLQuery("insert into nms_topo_area " +
					"(mapId, divLeft, divTop, divWidth, divHeight, divName, lineDashed, lineWidth, lineColor, deled) " +
					"values (:mapId,:divLeft,:divTop,:divWidth,:divHeight,:divName,:lineDashed,:lineWidth,:lineColor,'0')");
			q.setParameter("mapId", na.getMapId())
					.setParameter("divLeft", na.getDivLeft())
					.setParameter("divTop", na.getDivTop())
					.setParameter("divWidth", na.getDivWidth())
					.setParameter("divHeight", na.getDivHeight())
					.setParameter("divName", na.getDivName())
					.setParameter("lineDashed", na.getLineDashed())
					.setParameter("lineWidth", na.getLineWidth())
					.setParameter("lineColor", na.getLineColor()).executeUpdate();

		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	public PageBean<NmsTopoArea> findAllArea(Map<String, Object> map) {
		// 创建PageBean对象返回数据
			PageBean<NmsTopoArea> page = new PageBean<NmsTopoArea>();
		if (MapUtils.isNotEmpty(map)) {
			try {
				DetachedCriteria criteria = DetachedCriteria.forClass(NmsTopoArea.class);

				// 是否已删除
				criteria.add(Restrictions.eq("deled", 0));

				// 关联可用拓扑
				if (map.get("mapId") != null) {
					criteria.add(Restrictions.eq("mapId", Integer.valueOf(map.get("mapId").toString())));
				}

				// 可用域名称
				if (map.get("divName") != null && StringUtils.isNotBlank((String)map.get("divName"))) {
					criteria.add(Restrictions.like("divName", "%" + (String)map.get("divName") + "%"));
				}
				int begin = 1;
				int offset= -1;
				if (map.get("begin") != null && map.get("offset") != null 
						&& (int) map.get("begin") != -1
						&& (int) map.get("offset") != -1) {
					 begin = (int) map.get("begin");
					 offset= (int) map.get("offset");
				}
				// 获取数据总数
				criteria.setProjection(Projections.rowCount());
				Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria).get(0);

				// 分页查找所需数据
				criteria.setProjection(null);
				@SuppressWarnings("unchecked")
				List<NmsTopoArea> list = (List<NmsTopoArea>) hibernateTemplate.findByCriteria(criteria, (begin - 1) * offset,
						offset);

				// 计算总页数
				int totalPage = 1;
				if (totalCount == 0) {
					totalPage = totalCount.intValue() / offset + 1;
				} else if (totalCount % offset == 0) {
					totalPage = totalCount.intValue() / offset;
				} else {
					totalPage = totalCount.intValue() / offset + 1;
				}
				page.setTotalCount(totalCount.intValue());
				page.setPage(begin);
				page.setTotalPage(totalPage);
				String key = null;
				String value = null;
				page.setKey(key);
				page.setValue(value);
				page.setList(list);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		return page;

	}
	
	/**
	 * 查询拓扑节点信息
	 * 
	 * @param id
	 * @return
	 */
	public List<NmsTopoNode> findNodeByParames(int mapId, int containerId) {
		List<NmsTopoNode>  result = null;
		try {
			String hql = " from NmsTopoNode na left outer join na.nmsTopoMap where na.nmsTopoMap.id = ? and na.containerId =?";
			result =  (List<NmsTopoNode>) hibernateTemplate.find(hql, mapId, containerId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据设备id查询节点信息
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NmsTopoNode> findNodeById(int nodeId) {

		String hsql = " from NmsTopoNode na where na.id = ? ";
		return (List<NmsTopoNode>) hibernateTemplate.find(hsql, nodeId);
	}
	
	/**
	 * 绑定设备和可用域
	 * 
	 * @param id
	 * @return
	 */
	public boolean bindContainer(int id,int containerId) {
		try {
			String hql = "update NmsTopoNode u set u.containerId = ? where u.id = ?";
			Session session = hibernateTemplate.getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			query.setInteger(0,containerId);
			query.setInteger(1,id);
			query.executeUpdate();
			session.close();
		} catch (HibernateException e) {
			return false;
		}
		return true;
	}
}
