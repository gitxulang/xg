package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAlarm;
import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsCpuInfo;
import iie.pojo.NmsMemInfo;
import iie.pojo.NmsNetifInfo;
import iie.pojo.NmsPingInfo;
import iie.pojo.NmsSecRule;
import iie.pojo.NmsTopoArea;
import iie.pojo.NmsTopoLink;
import iie.pojo.NmsTopoMap;
import iie.pojo.NmsTopoMeta;
import iie.pojo.NmsTopoNode;
import iie.pojo.NmsDiskioInfo;
import iie.pojo.NmsYthAccount;
import iie.pojo.NmsYthApp;
import iie.pojo.NmsYthAppAccount;
import iie.pojo.NmsYthSoft;
import iie.service.NmsAccountInfoService;
import iie.service.NmsAppAccountInfoService;
import iie.service.NmsAppInfoService;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsNettopoService;
import iie.service.NmsPingInfoService;
import iie.service.NmsRuleAssetService;
import iie.service.NmsSecRuleService;
import iie.service.NmsSoftInfoService;
import iie.tools.Linkbean;
import iie.tools.Objectbean;
import iie.tools.PageBean;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Topo")
public class NmsNettopoCtrl {
	@Autowired
	NmsNettopoService nService;

	@Autowired
	NmsAuditLogService auditLogService;

	@Autowired
	NmsPingInfoService pingInfoService;

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	NmsRuleAssetService nmsRuleAssetService;
	
	@Autowired
	NmsSoftInfoService softInfoService;
	
	@Autowired
	NmsAccountInfoService accountInfoService;
	
	@Autowired
	NmsAppInfoService appInfoService;
	
	@Autowired
	NmsAppAccountInfoService appAccountInfoService;
	
	@Autowired
	NmsSecRuleService secRuleService;
	
	@RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsTopoMap> getAll(HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control", "no-cache");

		List<NmsTopoMap> res = nService.getAll();
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findByIdMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findByIdMap(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String mapId = request.getParameter("mapId");
		List<NmsTopoNode> Nodelist = nService.selectNodeById(mapId);
		List<NmsTopoLink> linklist = nService.selectlinkById(mapId);

		Map map = new HashMap();
		map.put("node", Nodelist);
		map.put("link", linklist);

		JSONObject jsonObject = JSONObject.fromObject(map);
		String result = jsonObject.toString();
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "?????????????????? ", "????????????", "????????????", "??????");

		return result;
	}

	@RequestMapping(value = "/selectName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String selectName(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("tname");
		NmsTopoMap mapVo = nService.findmapByName(name);
		mapVo.setTId("1");
		if (nService.updateNmsTopoMap(mapVo)) {
			// ????????????????????????
			List<NmsTopoMap> noselect = nService.findmapNotselected(name);
			for (NmsTopoMap na : noselect) {
				na.setTId("0");
				nService.updateNmsTopoMap(na);
			}
		}
		return "";
	}

	@RequestMapping(value = "/findnodeById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsTopoNode> findnodeById(HttpServletRequest request,
			HttpServletResponse response) {
		int id = new Integer(request.getParameter("mapId"));
		List<NmsTopoNode> na = nService.findnodeById(id);
		return na;
	}
/*
	@RequestMapping(value = "/findlinkById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsTopoLink findlinkById(HttpServletRequest request,
			HttpServletResponse response) {
		int id = new Integer(request.getParameter("mapId"));
		NmsTopoLink link = nService.findlinkById(id);
		return link;
	}
*/
	@RequestMapping(value = "/saveSubMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> saveSubMap(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = request.getParameter("current_topo_map");
		String topo_name = request.getParameter("topo_name");
		String region_ids = request.getParameter("region_ids");
		String topo_type = request.getParameter("topo_type");
		String t_picture = "submap.jpg";
		String t_id = "submap" + System.currentTimeMillis();
		Float z_percent = 1f;
		if (current_topo_map == null || current_topo_map.equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????XSS??????????????????????????????????????????????????????????????????!");
			return data;
		}
		if (topo_name == null || topo_name.equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????XSS??????????????????????????????????????????????????????????????????!");
			return data;
		}
		if (region_ids == null || region_ids.equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????XSS??????????????????????????????????????????????????????????????????!");
			return data;
		}
		current_topo_map = StringEscapeUtils.escapeSql(current_topo_map);
		topo_name = StringEscapeUtils.escapeSql(topo_name);
		region_ids = StringEscapeUtils.escapeSql(region_ids);
		// ????????????????????????????????????t_name?????????,?????????????????????
		int id = nService.findByName(topo_name);
		if (id != -1) {
			data.put("state", "1");
			data.put("info", "?????????????????? [" + topo_name + "] ??????????????????,?????????????????????!");
			return data;
		}

		String topoName = topo_name;
		String topoType = topo_type;
		NmsTopoMap na = new NmsTopoMap();
		na.setTName(topoName);
		na.setTType(Integer.parseInt(topoType));
		na.setTPicture(t_picture);
		na.setZPercent(z_percent);
		na.setItime(new Timestamp(System.currentTimeMillis()));
		na.setTId(t_id);
		if (nService.addtopoMap(na)) {
			data.put("state", "0");
			data.put("info", "?????????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????? [" + topo_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????? [" + topo_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/Edittopomap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> Edittopomap(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = request.getParameter("current_topo_map");
		String region_ids = request.getParameter("region_ids");
		String topo_name = request.getParameter("topo_name");

		if (current_topo_map == null || current_topo_map.equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????XSS??????????????????????????????????????????????????????????????????!");
			return data;
		}
		if (topo_name == null || topo_name.equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????XSS??????????????????????????????????????????????????????????????????!");
			return data;
		}
		if (region_ids == null || region_ids.equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????XSS??????????????????????????????????????????????????????????????????!");
			return data;
		}
		// ????????????????????????????????????t_name?????????,?????????????????????
		int id = nService.findByName(topo_name);
		if (id != -1) {
			data.put("state", "1");
			data.put("info", "?????????????????? [" + topo_name + "] ??????????????????,?????????????????????!");
			return data;
		}
		NmsTopoMap na = nService.findBycurrent_topo_map(current_topo_map);
		na.setTName(topo_name);
		
		if (nService.updateTopo(na)) {
			data.put("state", "0");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????? [" + topo_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????? [" + topo_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/findnhitnode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsTopoMeta> findnhitnode(HttpServletRequest request,
			HttpServletResponse response) {
		List<NmsTopoMeta> meta = nService.findallMeta();
		return meta;
	}

	@RequestMapping(value = "/saveHitnode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> saveHitnode(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		int mapId = new Integer(request.getParameter("mapId"));
		String nodeId = request.getParameter("nodeId");
		int typeId = new Integer(request.getParameter("typeId"));
		String img = request.getParameter("img");
		int x = new Integer(request.getParameter("x"));
		int y = new Integer(request.getParameter("y"));
		int width = new Integer(request.getParameter("width"));
		int height = new Integer(request.getParameter("height"));
		String ip = request.getParameter("ip");
		String alias = request.getParameter("alias");
		String relMap = request.getParameter("relMap");
		String col1 = request.getParameter("col1");
		String col2 = request.getParameter("col2");

		NmsTopoMap map = nService.findById(mapId);
		NmsAssetType type = nService.findByassettypeId(typeId);
		NmsTopoNode na = new NmsTopoNode();
		na.setNmsTopoMap(map);
		na.setNmsAssetType(type);
		na.setNodeId(nodeId);
		na.setImg(img);
		na.setX(x);
		na.setY(y);
		na.setHeight(height);
		na.setWidth(width);
		na.setIp(ip);
		na.setAlias(alias);
		na.setRelMap(relMap);
		na.setCol1(col1);
		na.setCol2(col2);
		na.setItime(new Timestamp(System.currentTimeMillis()));

		if (nService.addtopoNode(na)) {
			data.put("state", "0");
			data.put("info", "????????????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????[" + alias + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????[" + alias + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/readyAddNodes", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsAsset> readyAddNodes(HttpServletRequest request,
			HttpServletResponse response) {
		String current_topo_map = request.getParameter("current_topo_map");
		String type = request.getParameter("type");
		String keywords = request.getParameter("keywords");
		if (current_topo_map == null) {
			return null;
		}
		String map_id = "0";
		NmsTopoMap map = new NmsTopoMap();
		List<NmsTopoMap> list = nService.findmapByname(current_topo_map);
		if (list != null && list.size() > 0) {
			map = (NmsTopoMap) list.get(0);
			map_id = String.valueOf(map.getId());
		}
		List<NmsAsset> asset = new ArrayList<NmsAsset>();
		if (type != null && type.trim().length() > 0 && keywords != null
				&& keywords.trim().length() > 0) {
			if (keywords != null && keywords.trim().length() > 0) {
				if (type.equals("ip")) {
					asset = nService.findassetBycondition(type, keywords);
				} else {
					asset = nService.findassetBycondition(type, keywords);
				}
			}
		} else {
			asset = nService.findasset(map_id);
		}

		List<NmsTopoNode> toponodelist = nService.findNodeBymapId(map);
		List entityNodeList = new ArrayList();
		if (asset != null && asset.size() > 0) {
			for (int i = 0; i < asset.size(); i++) {
				NmsAsset node = (NmsAsset) asset.get(i);
				String id = String.valueOf(node.getId());
				int flag = 0;
				if (toponodelist != null && toponodelist.size() > 0) {
					for (int j = 0; j < toponodelist.size(); j++) {
						NmsTopoNode nodes = (NmsTopoNode) toponodelist.get(j);
						String topo_node_id = nodes.getNodeId();
						if (nodes.getNodeId() != null && nodes.getNodeId().substring(0, 3).equals("hin")) {
							continue;
						}
						topo_node_id = topo_node_id.substring(3);
						if (id.equals(topo_node_id)) {
							flag++;
						}
					}
				}
				if (flag == 0) {
					entityNodeList.add(asset.get(i));
				}
			}
		}
		return entityNodeList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/addNode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map addNode(HttpServletRequest request, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		Map dat = new HashMap();
		String current_topo_map = request.getParameter("current_topo_map");
		String node_object_Array = request.getParameter("node_object_Array");
		JSONArray array = JSONArray.fromObject(node_object_Array);
		ArrayList<NmsTopoNode> no = (ArrayList<NmsTopoNode>) JSONArray
				.toCollection(array, NmsTopoNode.class);

		String typeId = "";
		try {
			for (int i = 0; i < array.size(); i++) {
				JSONObject ob = (JSONObject) array.get(i);
				typeId = ob.getString("node_subtype");
				NmsAssetType assty = new NmsAssetType();
				assty.setId(Integer.parseInt(typeId));
				no.get(i).setNmsAssetType(assty);
			}
		} catch (JSONException e) {

		}
		if (current_topo_map == null || current_topo_map.equals("")) {
			return null;
		}
		String map_id = "";
		List<NmsTopoMap> list = nService.findmapByname(current_topo_map);
		if (list != null && list.size() > 0) {
			NmsTopoMap map = (NmsTopoMap) list.get(0);
			map_id = String.valueOf(map.getId());
		} else {
			return null;
		}

		List<NmsAssetType> assetTypeList = nService.findassetType();
		if (assetTypeList == null) {
			return null;
		}
		
		int assetTypeId;
		for (int i = 0; i < no.size(); i++) {
			assetTypeId = no.get(i).getNmsAssetType().getId();
			try {
				NmsTopoNode vo = new NmsTopoNode();
				String image_url = "";
				String image_width = "50";
				String image_height = "50";
				String node_tag = "net";
				for (int j = 0; j < assetTypeList.size(); j++) {
					NmsAssetType asset = (NmsAssetType) assetTypeList.get(j);
					if (asset.getId() == assetTypeId) {
						image_url = asset.getImgUrl();
						image_width = asset.getImgWid();
						image_height = asset.getImgHei();
						node_tag = asset.getNodeTag();
						break;
					}
				}

				NmsTopoMap map = nService.findById(Integer.valueOf(map_id));
				vo.setNmsAssetType(no.get(i).getNmsAssetType());
				vo.setNmsTopoMap(map);
				vo.setNodeId(node_tag + no.get(i).getNodeId());
				vo.setImg(image_url);
				vo.setX(no.get(i).getX());
				vo.setY(no.get(i).getY());
				vo.setWidth(Integer.parseInt(image_width));
				vo.setHeight(Integer.parseInt(image_height));
				vo.setIp(no.get(i).getIp());
				vo.setAlias(no.get(i).getAlias());
				vo.setRelMap("");
				vo.setCol1("");
				vo.setCol2("");
				vo.setContainerId(no.get(i).getContainerId());
				vo.setItime(new Timestamp(System.currentTimeMillis()));

				if (nService.addtopoNode(vo)) {
					dat.put("state", "0");
					dat.put("info", "????????????????????????!");
					auditLogService.add(((NmsAdmin) session
							.getAttribute("user")).getRealname(), request
							.getRemoteAddr(), "?????????????????? [" + no.get(i).getIp()
							+ "] ??????", "????????????", "????????????", "??????");
				} else {
					dat.put("state", "3");
					dat.put("info", "????????????????????????!");
					auditLogService.add(((NmsAdmin) session
							.getAttribute("user")).getRealname(), request
							.getRemoteAddr(), "?????????????????? [" + no.get(i).getIp()
							+ "] ??????", "????????????", "????????????", "??????");
					return dat;

				}

				int id = nService.getMaxId();
				no.get(i).setId(id);
				no.get(i).setNmsTopoMap(map);
				no.get(i).setNodeId(node_tag + no.get(i).getNodeId());
				no.get(i).setImg(image_url);
				no.get(i).setWidth(Integer.parseInt(image_width));
				no.get(i).setHeight(Integer.parseInt(image_height));
				no.get(i).setRelMap("");
			} catch (Exception e) {

			}
		}

		dat.put("flag", data);
		dat.put("data", no);
		return dat;
	}


	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> deleteLink(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String linkId = request.getParameter("linkId");
		String id = "";
		if (linkId == null || linkId.equals("") || !linkId.startsWith("line_")) {
			data.put("state", "3");
			data.put("info", "??????id??????????????????,????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + linkId + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}

		id = linkId.substring(5);
		if (nService.deletelink(id)) {
			data.put("state", "0");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + linkId + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + linkId + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/deleteNode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> deleteNode(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String node_ids = request.getParameter("node_ids");
		String current_map_name = request.getParameter("current_map_name");
		String[] node_array = node_ids.split(" ");
		
		int current_map_id;
		List<NmsTopoMap> list = nService.findBymapName(current_map_name);
		current_map_id = list.get(0).getId();
		if (current_map_id != 0) {
			for (int i = 0; i < node_array.length; i++) {
				String nodeId = node_array[i];
				nService.deletenode(current_map_id, nodeId);
				nService.deletelink(current_map_id, nodeId);
				auditLogService
						.add(((NmsAdmin) session.getAttribute("user"))
								.getUsername(), request.getRemoteAddr(),
								"???????????? [" + nodeId + "] ??????", "????????????", "????????????",
								"??????");
			}
			data.put("state", "0");
			data.put("info", "????????????!");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/createHintMeta", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsTopoMeta> createHintMeta(HttpServletRequest request,
			HttpServletResponse response) {
		List<NmsTopoMeta> meta = nService.findByTopoMeta();
		return meta;
	}

	@RequestMapping(value = "/addHinNode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addHinNode(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_name = request.getParameter("current_topo_name");
		String meta_name = request.getParameter("meta_name");
		String icon_path = request.getParameter("icon_path");
		String icon_name = request.getParameter("icon_name");
		String icon_width = request.getParameter("icon_width");
		String icon_height = request.getParameter("icon_height");
		if (current_topo_name == null || current_topo_name.equals("")) {
			return null;
		}
		if (meta_name == null || meta_name.equals("")) {
			return null;
		}
		if (icon_path == null || icon_path.equals("")) {
			return null;
		}
		if (icon_name == null || icon_name.equals("")) {
			return null;
		}
		if (icon_width == null || icon_width.equals("")) {
			return null;
		}
		if (icon_height == null || icon_height.equals("")) {
			return null;
		}

		int id;
		int mapId = 0;
		String nodeId = "";
		int typeId = 1;
		String img = icon_path;
		Integer x = 100;
		Integer y = 100;
		Integer width = Integer.parseInt(icon_width);
		Integer height = Integer.parseInt(icon_height);
		String ip = "????????????-" + icon_name;
		String alias = meta_name;
		String relMap = "";
		try {
			id = nService.getMaxId();
			if (id == 0) {
				id = 0;
			}
			nodeId = "hin" + (Integer.valueOf(id) + 1);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		List<NmsTopoMap> list = nService.findBymapName(current_topo_name);
		mapId = list.get(0).getId();
		NmsTopoMap map = nService.findById(mapId);
		NmsAssetType type = nService.findByassettypeId(typeId);
		NmsTopoNode na = new NmsTopoNode();
		na.setNmsTopoMap(map);
		na.setNmsAssetType(type);
		na.setNodeId(nodeId);
		na.setImg(img);
		na.setX(x);
		na.setY(y);
		na.setWidth(width);
		na.setHeight(height);
		na.setIp(ip);
		na.setRelMap(relMap);
		na.setAlias(alias);
		na.setCol1("y");
		na.setCol2("y");
		na.setItime(new Timestamp(System.currentTimeMillis()));
		if (nService.addtopoNode(na)) {
			data.put("state", "0");
			data.put("info", "????????????????????????!");
			data.put("nodeId", nodeId);
			data.put("mapId", map.getTId());
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????" + alias + "??????", "????????????",
					"????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????" + alias + "??????", "????????????",
					"????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/readyAddHintLink", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> readyAddHintLink(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = "";
		String start_node_id = "";
		String start_node_name = "";
		String start_node_desc = "";
		String end_node_id = "";
		String end_node_name = "";
		String end_node_desc = "";
		current_topo_map = request.getParameter("current_topo_map");
		start_node_id = request.getParameter("start_node_id");
		end_node_id = request.getParameter("end_node_id");
		if (current_topo_map == null || start_node_id == null
				|| end_node_id == null) {
			System.out.println("LinkManager.java->readyAddEntityLink parameters error");
			return null;
		}
		if (start_node_id.substring(0, 3).equals("hin")) {
			start_node_desc = "??????????????????";
		} else {
			start_node_desc = "??????????????????";
		}
		if (end_node_id.substring(0, 3).equals("hin")) {
			end_node_desc = "??????????????????";
		} else {
			end_node_desc = "??????????????????";
		}

		String mapId;
		List<NmsTopoMap> map = nService.selectmapById(current_topo_map);
		mapId = String.valueOf(map.get(0).getId());
		NmsTopoMap nmsmap = new NmsTopoMap();
		nmsmap.setId(Integer.parseInt(mapId));
		List<NmsTopoNode> na = nService.findNodeById(nmsmap, start_node_id);
		if (na != null && na.size() > 0) {
			NmsTopoNode node = (NmsTopoNode) na.get(0);
			start_node_name = node.getAlias();
		} else {
			System.out.println("LinkManager.java->readyAddHintLink start_node_id is null error");
			return null;
		}

		List<NmsTopoNode> endNode = nService.findNodeById(nmsmap, end_node_id);
		if (endNode != null && endNode.size() > 0) {
			NmsTopoNode node = (NmsTopoNode) endNode.get(0);
			end_node_name = node.getAlias();
		} else {
			System.out.println("LinkManager.java->readyAddHintLink start_node_id is null error");
			return null;
		}
		
		data.put("current_topo_map", current_topo_map);
		data.put("start_node_id", start_node_id);
		data.put("start_node_name", start_node_name);
		data.put("start_node_desc", start_node_desc);
		data.put("end_node_id", end_node_id);
		data.put("end_node_name", end_node_name);
		data.put("end_node_desc", end_node_desc);
		return data;
	}

	@RequestMapping(value = "/addHintLink", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addHintLink(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String returns = "";
		int id = 0;
		int map_id = 0;
		String current_topo_map = request.getParameter("current_topo_map");
		String start_node_id = request.getParameter("start_node_id");
		String start_node_desc = request.getParameter("start_node_desc");
		String end_node_id = request.getParameter("end_node_id");
		String end_node_desc = request.getParameter("end_node_desc");
		String link_type = request.getParameter("link_type");
		
		System.out.println("[DEBUG] addHintLink: link_type=" + link_type);
		
		// 1?????????????????????, 2?????????????????????, 3??????????????????, 4??????????????????, 5??????????????????, 6????????????,  ?????????????????????????????????????????????0???????????????
		id = nService.getMaxlinkId();
		List<NmsTopoMap> list = nService.findBymapName(current_topo_map);
		map_id = list.get(0).getId();
		NmsTopoMap map = nService.findById(map_id);
		returns = String.valueOf((id + 1));
		NmsTopoLink link = new NmsTopoLink();
		link.setNmsTopoMap(map);
		link.setSNodeId(start_node_id);
		link.setSIndex("0");
		link.setSIp("");
		link.setSDesc(start_node_desc);
		link.setSMac("");
		link.setENodeId(end_node_id);
		link.setEIndex("0");
		link.setEIp("");
		link.setEDesc(end_node_desc);
		link.setEMac("");
		if (link_type != null && link_type.equals("1")) {
			link.setLName("?????????(?????????)" + String.valueOf(id + 1));
			link.setLType(1);
			
			link.setLWidth(2);
			link.setLOffset(10);
			link.setCol1("0");
			link.setCol2("0,255,127");
		} else if (link_type != null && link_type.equals("2")) {
			link.setLName("????????????????????????" + String.valueOf(id + 1));
			link.setLType(2);
			
			link.setLWidth(2);
			link.setLOffset(10);
			link.setCol1("0");
			link.setCol2("255,255,113");
		} else if (link_type != null && link_type.equals("3")) {
			link.setLName("????????????????????????" + String.valueOf(id + 1));
			link.setLType(3);
			
			link.setLWidth(2);
			link.setLOffset(5);
			link.setCol1("0");
			link.setCol2("0,255,127");
		} else if (link_type != null && link_type.equals("4")) {
			link.setLName("????????????????????????" + String.valueOf(id + 1));
			link.setLType(4);
			
			link.setLWidth(2);
			link.setLOffset(5);
			link.setCol1("0");
			link.setCol2("255,255,113");
		} else if (link_type != null && link_type.equals("5")) {
			link.setLName("????????????????????????" + String.valueOf(id + 1));
			link.setLType(5);
			
			link.setLWidth(2);
			link.setLOffset(5);
			link.setCol1("10");
			link.setCol2("255,255,113");
		} else if (link_type != null && link_type.equals("6")) {
			link.setLName("?????????" + String.valueOf(id + 1));
			link.setLType(6);
	
			link.setLWidth(15);
			link.setLOffset(-2);
			link.setCol1("0");
			link.setCol2("128,128,105");
		} else {
			link.setLName("??????" + String.valueOf(id + 1));
			link.setLType(1);
			
			link.setLWidth(2);
			link.setLOffset(0);
			link.setCol1("0");
			link.setCol2("0,255,127");
		}
		link.setLDash("Solid");
		link.setItime(new Timestamp(System.currentTimeMillis()));

		if (nService.addHintLink(link)) {
			data.put("state", "0");
			data.put("info", "????????????????????????!");
			data.put("id", returns);
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????" + returns + "??????", "????????????",
					"????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????????" + returns + "??????", "????????????",
					"????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/saveRelationSubMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> saveRelationSubMap(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = request.getParameter("current_topo_map");
		String node_id = request.getParameter("node_id");
		String submap_name = request.getParameter("submap_name");
		if (current_topo_map == null || current_topo_map.equals("")) {
			return null;
		}
		if (node_id == null) {
			return null;
		}
		if (submap_name == null) {
			return null;
		}
		
		String mapId;
		List<NmsTopoMap> map = nService.selectmapById(current_topo_map);
		mapId = String.valueOf(map.get(0).getId());
		if (nService.updateRelation(node_id, submap_name, mapId)) {
			data.put("state", "0");
			data.put("info", "??????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + submap_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + submap_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/cancelRelationMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> cancelRelationMap(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = request.getParameter("current_topo_map");
		String node_id = request.getParameter("node_id");
		String submap_name = request.getParameter("submap_name");
		if (current_topo_map == null || current_topo_map.equals("")) {
			return null;
		}
		if (node_id == null) {
			return null;
		}
		
		String mapId;
		List<NmsTopoMap> map = nService.selectmapById(current_topo_map);
		mapId = String.valueOf(map.get(0).getId());
		if (nService.updateCencel(node_id, submap_name, mapId)) {
			data.put("state", "0");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "?????????????????? [" + submap_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "?????????????????? [" + submap_name + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/updateNodeAlias", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> updateNodeAlias(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_name = request.getParameter("current_topo_name");
		String node_id = request.getParameter("node_id");
		String node_alias = request.getParameter("node_alias");
		if (current_topo_name == null || current_topo_name.equals("")) {
			return null;
		}
		if (node_id == null || node_id.equals("")) {
			return null;
		}
		if (node_alias == null || node_alias.equals("")) {
			return null;
		}

		String mapId;
		List<NmsTopoMap> map = nService.selectmapById(current_topo_name);
		mapId = String.valueOf(map.get(0).getId());
		if (nService.updateNodeAlias(node_id, node_alias, mapId)) {
			data.put("state", "0");
			data.put("info", "??????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "?????????????????? [" + node_alias + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "??????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "?????????????????? [" + node_alias + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/updateLinkWidth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> updateLinkWidth(HttpServletRequest request,
			HttpSession session) {
		String current_topo_name = request.getParameter("current_topo_name");
		String link_id = request.getParameter("link_id");
		String link_width = request.getParameter("link_width");
		Map<String, String> data = new HashMap<String, String>();
		if (current_topo_name == null || "".equals(current_topo_name)) {
			return null;
		}
		if (link_id == null || "".equals(link_id)) {
			return null;
		}
		if (link_width == null || "".equals(link_width)) {
			return null;
		}
		String mapId;
		List<NmsTopoMap> map = nService.selectmapById(current_topo_name);
		mapId = String.valueOf(map.get(0).getId());
		if (nService.updatelinkWidth(link_id, link_width, mapId)) {
			data.put("state", "0");
			data.put("info", "????????????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(),
					"?????????????????? [" + link_width + "]px ??????", "????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(),
					"?????????????????? [" + link_width + "]px ??????", "????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/updateEntityNodeToTable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> updateEntityNodeToTable(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		String mapName = request.getParameter("current_topo_map");
		String nodeId = request.getParameter("node_id");
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		
		String mapId = "";
		List<NmsTopoMap> map = nService.selectmapById(mapName);
		if (map != null && map.size() > 0) {
			mapId = String.valueOf(map.get(0).getId());
		}

		if (nService.updateEntityNodeToTable(mapId, nodeId, x, y)) {
			data.put("state", "0");
			data.put("info", "????????????????????????!");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????????????????!");
			return data;
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addEntityLink", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addEntityLink(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = request.getParameter("current_topo_map");
		String link_name = request.getParameter("link_name");
		String start_node_id = request.getParameter("start_node_id");
		String start_node_name = request.getParameter("start_node_name");
		String start_node_ip = request.getParameter("start_node_ip");
		String start_node_index = request.getParameter("start_node_index");
		String start_node_mac = request.getParameter("start_node_mac");
		String end_node_id = request.getParameter("end_node_id");
		String end_node_name = request.getParameter("end_node_name");
		String end_node_ip = request.getParameter("end_node_ip");
		String end_node_index = request.getParameter("end_node_index");
		String end_node_mac = request.getParameter("end_node_mac");
		String link_type = request.getParameter("link_type");
		
		System.out.println("[DEBUG] addEntityLink: link_type=" + link_type);
		
		// ????????????????????????
		if (current_topo_map == null || current_topo_map == "") {
			data.put("state", "3");
			data.put("info", "????????????current_topo_map???, ????????????????????????!");
			return data;
		}
		if (link_name == null || link_name == "") {
			data.put("state", "3");
			data.put("info", "????????????link_name???, ????????????????????????!");
			return data;
		}
		if (start_node_index == null || start_node_index == "") {
			data.put("state", "3");
			data.put("info", "????????????start_node_index???, ????????????????????????!");
			return data;
		}
		if (start_node_id == null || start_node_id == "") {
			data.put("state", "3");
			data.put("info", "????????????start_node_id???, ????????????????????????!");
			return data;
		}
		if (start_node_ip == null || start_node_ip == "") {
			data.put("state", "3");
			data.put("info", "????????????start_node_ip???, ????????????????????????!");
			return data;
		}
		if (end_node_id == null || end_node_id == "") {
			data.put("state", "3");
			data.put("info", "????????????end_node_id???, ????????????????????????!");
			return data;
		}
		if (end_node_name == null || end_node_name == "") {
			data.put("state", "3");
			data.put("info", "????????????end_node_name???, ????????????????????????!");
			return data;
		}
		if (end_node_ip == null || end_node_ip == "") {
			data.put("state", "3");
			data.put("info", "????????????end_node_ip???, ????????????????????????!");
			return data;
		}
		if (end_node_index == null || end_node_index == "") {
			data.put("state", "3");
			data.put("info", "????????????end_node_index???, ????????????????????????!");
			return data;
		}
		if (end_node_mac == null || end_node_mac == "") {
			data.put("state", "3");
			data.put("info", "????????????end_node_mac???, ????????????????????????!");
			return data;
		}
		
		if (link_type == null || link_type == "") {
			data.put("state", "3");
			data.put("info", "??????????????????, ????????????????????????!");
			return data;
		}
		
		int id = 0;
		int map_id = 0;
		List<NmsTopoMap> list = nService.findBymapName(current_topo_map);
		map_id = list.get(0).getId();
		NmsTopoMap map = nService.findById(map_id);

		List<NmsTopoLink> linkList = nService.findRepeatLink(
				String.valueOf(map_id), start_node_id, start_node_index,
				end_node_id, end_node_index);
		if (linkList != null && linkList.size() > 0) {
			data.put("state", "3");
			data.put("info", "[" + current_topo_map + "] ??????????????????????????????,??????????????????!");
			return data;
		}

		NmsTopoLink link = new NmsTopoLink();
		link.setNmsTopoMap(map);
		link.setSNodeId(start_node_id);
		link.setSIndex(start_node_index);
		link.setSIp(start_node_ip);
		link.setSDesc(start_node_name);
		link.setSMac(start_node_mac);
		link.setENodeId(end_node_id);
		link.setEIndex(end_node_index);
		link.setEIp(end_node_ip);
		link.setEDesc(end_node_name);
		link.setEMac(end_node_mac);
		link.setLType(0);
		link.setLName(link_name);
		link.setLDash("Solid");
		link.setLWidth(2);

		// 1?????????????????????, 2?????????????????????, 3??????????????????, 4??????????????????, 5??????????????????, 6????????????,  ?????????????????????????????????????????????0???????????????
		if (link_type != null && link_type.equals("1")) {
			link.setLOffset(10);
			link.setCol1("0");
			link.setCol2("0,255,127");
		} else if (link_type != null && link_type.equals("2")) {
			link.setLOffset(10);
			link.setCol1("0");
			link.setCol2("255,255,113");
		} else if (link_type != null && link_type.equals("3")) {
			link.setLOffset(5);
			link.setCol1("0");
			link.setCol2("0,255,127");
		} else if (link_type != null && link_type.equals("4")) {
			link.setLOffset(5);
			link.setCol1("0");
			link.setCol2("255,255,113");
		} else if (link_type != null && link_type.equals("5")) {
			link.setLOffset(5);
			link.setCol1("10");
			link.setCol2("255,255,113");
		} else if (link_type != null && link_type.equals("6")) {
			link.setLWidth(15);
			link.setLOffset(-2);
			link.setCol1("0");
			link.setCol2("128,128,105");
		} else {
			link.setLOffset(0);
			link.setCol1("0");
			link.setCol2("0,255,127");
		}
		
		link.setItime(new Timestamp(System.currentTimeMillis()));
		if (nService.addentityLink(link)) {
			id = nService.getId(map_id, start_node_ip, start_node_index,
					end_node_ip, end_node_index);
			data.put("state", "0");
			data.put("info", "????????????????????????!");
			data.put("id", String.valueOf(id));
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "????????????????????????", "????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "????????????????????????", "????????????", "????????????", "??????");
			return data;
		}
	}

	@RequestMapping(value = "/deleteSubMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> deleteSubMap(HttpServletRequest request,
			HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = request.getParameter("current_topo_map");
		if (current_topo_map == null || current_topo_map.equals("")) {
			return null;
		}

		int map_id = 0;
		List<NmsTopoMap> list = nService.findBymapName(current_topo_map);
		if (list != null && list.size() > 0) {
			map_id = list.get(0).getId();
		} else {
			return null;
		}
		
		int flag1 = 0;
		int flag2 = 0;
		int flag3 = 0;
		int flag4 = 0;
		if (nService.deletelinkById(map_id)) {
			flag1 = 1;
		} else {
			flag1 = 2;
		}
		if (nService.deletenodeById(map_id)) {
			flag2 = 1;
		} else {
			flag2 = 2;
		}
		if (nService.deletemapById(map_id)) {
			flag3 = 1;
		} else {
			flag3 = 2;
		}
		if (nService.updatenodeById(current_topo_map)) {
			flag4 = 1;
		} else {
			flag4 = 2;
		}
		if (flag1 == 1 && flag2 == 1 && flag3 == 1 && flag4 == 1) {
			data.put("state", "0");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + current_topo_map
							+ "] ??????", "????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + current_topo_map
							+ "] ??????", "????????????", "????????????", "??????");
			return data;
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/readyAddEntityLink", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> readyAddEntityLink(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		String current_topo_map = "";
		String start_node_id = "";
		String start_node_id_all = "";
		String end_node_id = "";
		String end_node_id_all = "";
		String start_node_name = "";
		String start_node_ip = "";
		String start_interface_string = "";
		String end_node_name = "";
		String end_node_ip = "";
		String end_interface_string = "";
		
		current_topo_map = request.getParameter("current_topo_map");
		start_node_id_all = request.getParameter("start_node_id");
		end_node_id_all = request.getParameter("end_node_id");
		start_node_id = start_node_id_all.substring(3);
		end_node_id = end_node_id_all.substring(3);
		if (current_topo_map == null || start_node_id == null
				|| end_node_id == null) {
			System.out.println("LinkManager.java->readyAddEntityLink parameters error");
			return null;
		}

		List<NmsAsset> assetList = nService.findassetTypeById(start_node_id);
		if (assetList != null && assetList.size() > 0) {
			NmsAsset node = (NmsAsset) assetList.get(0);
			start_node_name = node.getAName();
			start_node_ip = node.getAIp();
		} else {
			System.out.println("LinkManager.java->readyAddEntityLink start_node_id is null error");
			return null;
		}

		List<NmsAsset> assetendList = nService.findassetEndById(end_node_id);
		if (assetendList != null && assetendList.size() > 0) {
			NmsAsset node = (NmsAsset) assetendList.get(0);
			end_node_name = node.getAName();
			end_node_ip = node.getAIp();
		} else {
			System.out.println("LinkManager.java->readyAddEntityLink start_node_id is null error");
			return null;
		}

		try {
			List<NmsNetifInfo> start_interface = new ArrayList<NmsNetifInfo>();
			start_interface = nService.findInterfaceById(start_node_id);
			for (int i = 0; start_interface != null && i < start_interface.size(); i++) {
				NmsNetifInfo obj = (NmsNetifInfo) start_interface.get(i);
				if (!obj.getIfDescr().equals("lo")) {
					int index = obj.getIfIndex();
					String name = obj.getIfDescr().replace(";", "")
							.replace("|", "");
					String mac = obj.getIfPhysaddr().replace(";", "")
							.replace("|", "");
					long speed = obj.getIfSpeed();

					String str = index + ";" + name + ";" + mac + ";" + speed
							+ "|";
					start_interface_string += str;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		try {
			List<NmsNetifInfo> end_interface = new ArrayList<NmsNetifInfo>();
			end_interface = nService.findInterfaceById(end_node_id);
			for (int i = 0; end_interface != null && i < end_interface.size(); i++) {
				NmsNetifInfo obj = (NmsNetifInfo) end_interface.get(i);
				if (!obj.getIfDescr().equals("lo")) {
					int index = obj.getIfIndex();
					String name = obj.getIfDescr().replace(";", "")
							.replace("|", "");
					String mac = obj.getIfPhysaddr().replace(";", "")
							.replace("|", "");
					long speed = obj.getIfSpeed();

					String str = index + ";" + name + ";" + mac + ";" + speed
							+ "|";
					end_interface_string += str;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		data.put("current_topo_map", current_topo_map);
		data.put("start_node_id", start_node_id_all);
		data.put("start_node_name", start_node_name);
		data.put("start_node_ip", start_node_ip);
		data.put("end_node_id", end_node_id_all);
		data.put("end_node_name", end_node_name);
		data.put("end_node_ip", end_node_ip);
		data.put("start_interface_string", start_interface_string);
		data.put("end_interface_string", end_interface_string);
		return data;
	}

	@RequestMapping(value = "/getShowLinkMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getShowLinkMessage(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String line_id = request.getParameter("line_id");
		StringBuffer msg = new StringBuffer(1024);
		String start_node_id = "";
		String end_node_id = "";
		String start_port_name = "";
		String start_ip = "";
		String start_mac = "";
		String start_in_speed = "";
		String start_in_band = "";
		String start_out_speed = "";
		String start_out_band = "";
		String start_admin_status = "";
		String start_oper_status = "";
		String end_port_name = "";
		String end_ip = "";
		String end_mac = "";
		String end_in_speed = "";
		String end_in_band = "";
		String end_out_speed = "";
		String end_out_band = "";
		String end_admin_status = "";
		String end_oper_status = "";
		String link_type = "";
		String[] nodeIdArray = line_id.split("_");
		if (nodeIdArray.length > 0) {
			start_node_id = nodeIdArray[0].substring(3);
			end_node_id = nodeIdArray[1].substring(3);
		} else {
			return null;
		}
		
		List<NmsTopoLink> link = nService.findlinkedById(id);
		if (link != null && link.size() > 0) {
			NmsTopoLink links = new NmsTopoLink();
			links = (NmsTopoLink) link.get(0);

			link_type = links.getLType() + "";
			start_ip = links.getSIp();
			start_mac = links.getSMac();
			start_port_name = links.getSDesc();
			end_ip = links.getEIp();
			end_mac = links.getEMac();
			end_port_name = links.getEDesc();
		}

		
		if (!link_type.equals("0")) {
			msg.append("<font color='#00ffff'>???????????????????????????</font>");
			return msg.toString();
		}
		// ??????????????????
		NmsAsset node = new NmsAsset();
		List<NmsAsset> list = nService.findassetBynodeId(start_node_id);
		try {
			if (list.size() == 0) {
				msg.append("<font color=\"red\">???????????????????????????????????????????????????</font><br/>");
				return msg.toString();
			} else {
				node = (NmsAsset) list.get(0);
				int online = node.getOnline();
				if (online != 1) {
					msg.append("<font color=\"red\">???????????????????????????????????????????????????</font><br/>");
					return msg.toString();
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] " + e.toString());
		}	
		//?????????????????????
		list = nService.findassetBynodeId(end_node_id);
		try {
			if (list.size() == 0) {
				msg.append("<font color=\"red\">???????????????????????????????????????????????????</font><br/>");
				return msg.toString();
			} else {
				node = (NmsAsset) list.get(0);
				int online = node.getOnline();
				if (online != 1) {
					msg.append("<font color=\"red\">???????????????????????????????????????????????????</font><br/>");
					return msg.toString();
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] " + e.toString());
		}	
		
		
		String start_Index = "0";
		if (link != null && link.size() > 0) {
			start_Index = link.get(0).getSIndex();
		}

		List<NmsNetifInfo> ifList = nService.find2infoById(start_node_id, start_Index);

		if (ifList != null && ifList.size() > 0) {
			NmsNetifInfo realObj = ifList.get(0);
			start_port_name = realObj.getIfDescr();
			start_ip = realObj.getIfIp();
			start_mac = realObj.getIfPhysaddr();
			start_admin_status = String.valueOf(realObj.getIfAdminStatus());
			start_oper_status = String.valueOf(realObj.getIfOperStatus());
			// ??????????????????????????????
			start_in_speed = "-";
			start_in_band = "-";
			start_out_speed = "-";
			start_out_band = "-";

			long speed = realObj.getIfSpeed();
			double realInBytes = realObj.getIfInOctets();
			double realOutBytes = realObj.getIfOutOctets();
			long realTime = realObj.getItime().getTime() / 1000;
			if (ifList.size() > 1) {
				NmsNetifInfo befObj = ifList.get(1);
				double befInBytes = befObj.getIfInOctets();
				double befOutBytes = befObj.getIfOutOctets();
				long befTime = befObj.getItime().getTime() / 1000;
				// ????????????????????????(???)
				long seconds = realTime - befTime;
				// ???????????????????????????????????????, ????????????????????????0
				double inBytes = realInBytes - befInBytes;
				if (inBytes < 0) {
					inBytes = 0;
				}
				double outBytes = realOutBytes - befOutBytes;
				if (outBytes < 0) {
					outBytes = 0;
				}
				DecimalFormat df = new DecimalFormat("0.00");
				if (seconds > 0) {
					double sspeed = inBytes / seconds / 1024;
					// ??????????????????????????????10Gb/s???????????????0kb/s
					if (sspeed > 10000) {
						sspeed = 0;
					}
					start_in_speed = df.format(sspeed) + " kB/s";
					double ospeed = outBytes / seconds / 1024;
					// ??????????????????????????????10Gb/s???????????????0kb/s
					if (ospeed > 10000) {
						ospeed = 0;
					}
					start_out_speed = df.format(ospeed) + " kB/s";
					if (speed > 0) {
						double sband = 100 * 8 * (inBytes / seconds) / speed
								/ 1000000;
						// ??????????????????????????????100%???????????????100%
						if (sband > 100) {
							sband = 100;
						}
						start_in_band = df.format(sband) + " %";
						double oband = 100 * 8 * (outBytes / seconds) / speed
								/ 1000000;
						// ??????????????????????????????100%???????????????100%
						if (oband > 100) {
							oband = 100;
						}
						start_out_band = df.format(oband) + " %";
					}
				}
			}
		}

		// ??????????????????????????????
		String end_Index = "0";
		if (link != null && link.size() > 0) {
			end_Index = link.get(0).getEIndex();
		}
		ifList = nService.find2infoById(end_node_id, end_Index);
		if (ifList != null && ifList.size() > 0) {
			NmsNetifInfo realObj = ifList.get(0);
			end_port_name = realObj.getIfDescr();
			end_ip = realObj.getIfIp();
			end_mac = realObj.getIfPhysaddr();
			end_admin_status = String.valueOf(realObj.getIfAdminStatus());
			end_oper_status = String.valueOf(realObj.getIfOperStatus());
			// ??????????????????????????????
			end_in_speed = "-";
			end_in_band = "-";
			end_out_speed = "-";
			end_out_band = "-";

			long speed = realObj.getIfSpeed();
			double realInBytes = realObj.getIfInOctets();
			double realOutBytes = realObj.getIfOutOctets();
			long realTime = realObj.getItime().getTime() / 1000;
			if (ifList.size() > 1) {
				NmsNetifInfo befObj = ifList.get(1);
				double befInBytes = befObj.getIfInOctets();
				double befOutBytes = befObj.getIfOutOctets();
				long befTime = befObj.getItime().getTime() / 1000;
				// ????????????????????????(???)
				long seconds = realTime - befTime;
				// ???????????????????????????????????????, ????????????????????????0
				double inBytes = realInBytes - befInBytes;
				if (inBytes < 0) {
					inBytes = 0;
				}
				double outBytes = realOutBytes - befOutBytes;
				if (outBytes < 0) {
					outBytes = 0;
				}
				DecimalFormat df = new DecimalFormat("0.00");
				if (seconds > 0) {
					double sspeed = inBytes / seconds / 1024;
					// ??????????????????????????????10Gb/s???????????????0kb/s
					if (sspeed > 10000000) {
						sspeed = 0;
					}
					end_in_speed = df.format(sspeed) + " kB/s";
					double ospeed = outBytes / seconds / 1024;
					// ??????????????????????????????10Gb/s???????????????0kb/s
					if (ospeed > 10000000) {
						ospeed = 0;
					}
					end_out_speed = df.format(ospeed) + " kB/s";
					if (speed > 0) {
						double sband = 100 * 8 * (inBytes / seconds) / speed
								/ 1000000;
						// ??????????????????????????????100%???????????????100%
						if (sband > 100) {
							sband = 100;
						}
						end_in_band = df.format(sband) + " %";
						double oband = 100 * 8 * (outBytes / seconds) / speed
								/ 1000000;
						// ??????????????????????????????100%???????????????100%
						if (oband > 100) {
							oband = 100;
						}
						end_out_band = df.format(oband) + " %";
					}
				}
			}
		}

		// ?????????????????????down?????????????????????down
		if (!(start_admin_status.equals("1") && start_oper_status.equals("1")
				&& end_admin_status.equals("1") && end_oper_status.equals("1"))) {
			start_oper_status = "2";
			end_oper_status = "2";
		}
		// 0??????????????????, 1??????????????????
		if (link_type.equals("0")) {
			if (start_admin_status.equals("1") && start_oper_status.equals("1")) {
				msg.append("?????????<font color='green'><b>" + start_port_name
						+ "(up)</b></font><br/>");
			} else if (start_admin_status.equals("1")
					&& start_oper_status.equals("2")) {
				msg.append("?????????<font color='red'><b>" + start_port_name
						+ "(down)</b></font><br/>");
			} else {
				msg.append("?????????<font color='808A87'><b>" + start_port_name
						+ "(??????)</b></font><br/>");
			}

			msg.append("IP?????????<font color='white'>" + start_ip + "</font><br/>");
			msg.append("MAC?????????<font color='white'>" + start_mac + "</font><br/>");
			msg.append("???????????????<font color='white'>" + start_in_speed
					+ "</font><br/>");
			msg.append("??????????????????<font color='white'>" + start_in_band
					+ "</font><br/>");
			msg.append("???????????????<font color='white'>" + start_out_speed
					+ "</font><br/>");
			msg.append("??????????????????<font color='white'>" + start_out_band
					+ "</font><br/>");
			msg.append("<br>");
			if (end_admin_status.equals("1") && end_oper_status.equals("1")) {
				msg.append("?????????<font color='green'><b>" + end_port_name
						+ "(up)</b></font><br/>");
			} else if (end_admin_status.equals("1")
					&& end_oper_status.equals("2")) {
				msg.append("?????????<font color='red'><b>" + end_port_name
						+ "(down)</b></font><br/>");
			} else {
				msg.append("?????????<font color='808A87'><b>" + end_port_name
						+ "(??????)</b></font><br/>");
			}
			msg.append("IP?????????<font color='white'>" + end_ip + "</font><br/>");
			msg.append("MAC?????????<font color='white'>" + end_mac + "</font><br/>");
			msg.append("???????????????<font color='white'>" + end_in_speed
					+ "</font><br/>");
			msg.append("??????????????????<font color='white'>" + end_in_band
					+ "</font><br/>");
			msg.append("???????????????<font color='white'>" + end_out_speed
					+ "</font><br/>");
			msg.append("??????????????????<font color='white'>" + end_out_band
					+ "</font><br/>");
		} else {
			msg.append("<font color='#00ffff'>???????????????????????????</font>");
		}
		return msg.toString();
	}

/*	
	@RequestMapping(value = "/getShowNodeMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> getShowNodeMessage(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("state", "0");
		
		String node_id = request.getParameter("node_id");
		String returns = "";
		String nodeId = node_id.substring(3);
		StringBuffer msg = new StringBuffer(4096);
		StringBuffer soft = new StringBuffer(4096);
		StringBuffer account = new StringBuffer(4096);
		StringBuffer app = new StringBuffer(4096);
		String location = "";
		String ip = "";
		String alarm_status = "";
		String ping_status = "";
		String cpu_status = "";
		String mem_status = "";
		
		// ??????????????????
		List<NmsYthSoft> softList = softInfoService.getListByAssetId(nodeId, 20);
		if (softList != null) {
			for (int i = 0; i < softList.size(); i++) {
				NmsYthSoft obj = (NmsYthSoft)softList.get(i);
				soft.append("(" + (i + 1) + ") ");
				soft.append(obj.getSoftName());
			//	soft.append(" / ");
			//	soft.append(obj.getSoftVersion());
				soft.append("<br/>");
			}
		}
		data.put("soft", soft.toString());
		
		// ????????????????????????
		List<NmsYthApp> appList = appInfoService.getListByAssetId(nodeId, 20);
		if (appList != null) {
			for (int i = 0; i < appList.size(); i++) {
				NmsYthApp obj = (NmsYthApp)appList.get(i);
				app.append("(" + (i + 1) + ") ");
				app.append(obj.getAppName());
				app.append(" / ");
				app.append(obj.getAppPort());
				app.append("<br/>");

				Long appId = obj.getId();
			//	System.out.println("[DEBUG] appId = " + appId);
				int num = i + 1;
				account.append("(" + num + ") " + obj.getAppName() + "<br>");
				try {
					List<NmsYthAppAccount> appCountList = appAccountInfoService.getList(String.valueOf(appId));
					for (int j = 0; j < appCountList.size() && j < 20; j++) {
						NmsYthAppAccount acObj = appCountList.get(j);
						account.append("&nbsp;&nbsp;&nbsp;&nbsp;" + acObj.getType() + "???" + acObj.getUserRealName() + "<br>");
						if (j == 19) {
							account.append("...<br>");
							break;
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		data.put("account", account.toString());
		
		// ??????????????????
		List<NmsAsset> as = nService.findassetBynodeId(nodeId);
		if (as.size() == 0) {
			msg.append("?????????????????????");
			data.put("info", msg.toString());
			return data;
		}
		
		try {
			try {
				NmsAsset node = new NmsAsset();
				List<NmsAsset> list = nService.findassetBynodeId(nodeId);
				if (list.size() == 0) {
					ip = "";
					location = "";
				} else {
					node = (NmsAsset) list.get(0);
					ip = node.getAIp();
					location = node.getAPos();
				}
			} catch (Exception e) {
				e.printStackTrace();
				data.put("state", "1");
			}

			msg.append("IP?????????<font color=\"white\">" + ip + "</font><br/>");
			msg.append("???????????????<font color=\"white\">" + location + "</font><br/>");

			 ???????????????????????? 
			try {
				NmsAlarm eventList = new NmsAlarm();
				List<NmsAlarm> alarmlist = nService.findwealarmById(nodeId);
				if (alarmlist.size() == 0) {
					alarm_status = "<font color=\"green\">?????????</font>";
				} else {
					int alarm_level = 0;
					for (int i = 0; i < alarmlist.size(); i++) {
						eventList = (NmsAlarm) alarmlist.get(i);
						if (eventList.getALevel() > alarm_level) {
							alarm_level = eventList.getALevel();
						}
					}
					if (alarm_level == 1) {
						alarm_status = "<font color=\"yellow\">????????????</font>";
					} else if (alarm_level == 2) {
						alarm_status = "<font color=\"orange\">????????????</font>";
					} else if (alarm_level == 3) {
						alarm_status = "<font color=\"red\">????????????</font>";
					} else {
						alarm_status = "";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				data.put("state", "1");
			}
			
		

			// ??????????????????
			List<NmsPingInfo> pinglist = nService.findpingById(nodeId);
			if (pinglist != null && pinglist.size() > 0) {
				NmsPingInfo pingObj = (NmsPingInfo) pinglist.get(0);
				DecimalFormat df = new DecimalFormat("0");
				ping_status = df.format(pingObj.getPingRate() * 100) + "%";
				ping_status = String.valueOf(pingObj.getPingRate() * 100) + "%";
			}
			// ??????CPU??????, ?????????????????????CPU????????????
			List<NmsCpuInfo> cpulist = nService.findcpuById(nodeId);
			if (cpulist != null && cpulist.size() > 0) {
				float cpuRate = 0;
				for (int i = 0; i < cpulist.size(); i++) {
					NmsCpuInfo cpuObj = (NmsCpuInfo) cpulist.get(i);
					cpuRate += cpuObj.getCpuRate();
				}
				cpuRate = cpuRate / cpulist.size();
				DecimalFormat df = new DecimalFormat("0.00");
				cpu_status = df.format(cpuRate) + "%";
			}

			// ?????????????????????
			List<NmsMemInfo> memlist = nService.findmeminfoById(nodeId);
			if (memlist != null && memlist.size() > 0) {
				NmsMemInfo memObj = (NmsMemInfo) memlist.get(0);
				long total = memObj.getMemTotal();
				long used = memObj.getMemTotal() - memObj.getMemFree();
				float memRate = 0;
				if (total > 0 && total >= used) {
					memRate = (float) (used * 1.0 / total * 1.0) * 100;
					DecimalFormat df = new DecimalFormat("0.00");
					mem_status = df.format(memRate) + "%";
				}
			}

			// ????????????HTML
			if (!ping_status.equals("")) {
				msg.append("???????????????<font color=\"white\">" + ping_status + "</font><br/>");
			}
			msg.append("???????????????<font color=\"red\">" + alarm_status + "</font><br/>");
			if (!cpu_status.equals("")) {
			//	msg.append("CPU?????????<font color=\"white\">" + cpu_status + "</font><br/>");
			}
			if (!mem_status.equals("")) {
			//	msg.append("???????????????<font color=\"white\">" + mem_status + "</font><br/>");
			}
			
			for (int i = 0; i < appList.size(); i++) {
				NmsYthApp obj = (NmsYthApp)appList.get(i);
				msg.append("???????????????<font color=\"white\">" + obj.getAppName() + "</font><br/>");
			}
			
			List<NmsYthAccount> accountList = accountInfoService.getListByAssetId(nodeId, 20);
			if (accountList != null) {
				for (int i = 0; i < accountList.size(); i++) {
					NmsYthAccount obj = (NmsYthAccount)accountList.get(i);
					msg.append("???????????????<font color=\"white\">" + obj.getType() + " / " + obj.getUserRealName() + "</font><br/>");
				}
			}

			returns = msg.toString();
		} catch (Exception e) {
			e.printStackTrace();
			data.put("state", "1");
			data.put("info", "?????????????????????");
			return data;
		}
		
		data.put("info", returns);
		return data;
	}
*/
	
	@RequestMapping(value = "/getShowNodeMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> getShowNodeMessage(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("state", "0");
		
		String node_id = request.getParameter("node_id");
		String returns = "";
		String nodeId = node_id.substring(3);
		StringBuffer msg = new StringBuffer(4096);
		StringBuffer soft = new StringBuffer(4096);
		StringBuffer account = new StringBuffer(4096);
		StringBuffer appAccount = new StringBuffer(4096);
		StringBuffer app = new StringBuffer(4096);
		String ip = "";
		String aspId = "";
		String alarm_status = "";
		String ping_status = "";
		String cpu_status = "";
		String mem_status = "";
		
		NmsAsset node = new NmsAsset();
		List<NmsAsset> list = nService.findassetBynodeId(nodeId);

		try {
			if (list.size() == 0) {
				ip = "";
			} else {
				node = (NmsAsset) list.get(0);
				ip = node.getAIp();
				aspId = node.getANo();
				int online = node.getOnline();
				msg.append("???????????????<font color=\"white\">" + ip + "</font><br/>");
				msg.append("ASPID???<font color=\"white\">" + aspId + "</font><br/>");
				if (online != 1) {
					msg.append("<font color=\"red\">??????????????????????????????????????????</font><br/>");
					data.put("info", msg.toString());
					return data;
				}
/*				//???????????????????????????????????????????????????????????????,??????????????????????????????????????????
				String chType = (String)node.getNmsAssetType().getChType();
				if (chType.contains("?????????")) { 
					data.put("title", "???????????????????????????");
					data.put("account", appAccount.toString());
				} else if (chType.contains("?????????")) {
					data.put("title", "???????????????????????????");
					data.put("account", account.toString());
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.put("state", "1");
		}
		
		// ??????????????????
		List<NmsYthSoft> softList = softInfoService.getListByAssetId(nodeId, 20);
		if (softList != null) {
			for (int i = 0; i < softList.size(); i++) {
				NmsYthSoft obj = (NmsYthSoft)softList.get(i);
				soft.append("(" + (i + 1) + ") ");
				soft.append(obj.getSoftName());
/*				soft.append("-");
				soft.append(obj.getSoftVersion());*/
				soft.append("<br/>");
			}
		}
		data.put("soft", soft.toString());
		
		// ??????????????????????????????
		NmsSecRule secObj = secRuleService.findById(1);
		int jmsStatus = 1;
		if (secObj.getJmsStatus() != null) {
			jmsStatus = secObj.getJmsStatus();
		}
		// ????????????????????????????????????????????????????????????
		List<NmsYthApp> appList = appInfoService.getListByAssetId(nodeId, 20);
		if (appList != null) {
			if (jmsStatus == 0) {
				appAccount.append("<font color='red'>AMQ????????????????????????????????????</font><br>");
			}
			for (int i = 0; i < appList.size(); i++) {
				NmsYthApp obj = (NmsYthApp)appList.get(i);
				app.append("(" + (i + 1) + ") ");
				app.append(obj.getAppName());
				app.append(" / ");
				app.append(obj.getAppPort());
				app.append("<br/>");
				Long appId = obj.getId();
				int num = i + 1;
				appAccount.append("(" + num + ") " + obj.getAppName() + "<br>");
				try {
					List<NmsYthAppAccount> appCountList = appAccountInfoService.getList(String.valueOf(appId));
					for (int j = 0; j < appCountList.size() && j < 20; j++) {
						NmsYthAppAccount acObj = appCountList.get(j);
						String typeDesc = "";
						if (acObj != null && acObj.getType() != null) {	
							if (acObj.getType().equals("001")) {
								typeDesc = "???????????????";
							} else if (acObj.getType().equals("010")) {
								typeDesc = "???????????????";
							} else if (acObj.getType().equals("100")) {
								typeDesc = "???????????????";
							} else if (acObj.getType().equals("000")) {
								typeDesc = "????????????";
							} else if (acObj.getType().equals("110")) {
								typeDesc = "?????????????????????";
							} else if (acObj.getType().equals("101")) {
								typeDesc = "?????????????????????";
							} else if (acObj.getType().equals("011")) {
								typeDesc = "?????????????????????";
							}
							appAccount.append(typeDesc + "???<font color=\"white\">" + acObj.getUserRealName() + "</font>  ?????????<font color=\"white\">" + acObj.getUserName() + "</font><br>");
							if (j == 19) {
								appAccount.append("...<br>");
								break;
							}
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		
		// ????????????????????????????????????
		List<NmsYthAccount> accountList = accountInfoService.getListByAssetId(nodeId, 20);
		if (accountList != null) {
			if (list.size() > 0) {
				node = (NmsAsset) list.get(0);
				String chType = (String)node.getNmsAssetType().getChType();
				if (chType.contains("?????????")) {
					if (jmsStatus == 0) {
						account.append("<font color='red'>AMQ????????????????????????????????????</font><br>");
					}
					for (int i = 0; i < accountList.size(); i++) {
						NmsYthAccount obj = (NmsYthAccount)accountList.get(i);
						String typeDesc = "";
						if (obj != null && obj.getType() != null) {
							if (obj.getType().equals("001")) {
								typeDesc = "???????????????";
							} else if (obj.getType().equals("010")) {
								typeDesc = "???????????????";
							} else if (obj.getType().equals("100")) {
								typeDesc = "???????????????";
							} else if (obj.getType().equals("000")) {
								typeDesc = "????????????";
							} else if (obj.getType().equals("110")) {
								typeDesc = "?????????????????????";
							} else if (obj.getType().equals("101")) {
								typeDesc = "?????????????????????";
							} else if (obj.getType().equals("011")) {
								typeDesc = "?????????????????????";
							}
							account.append(typeDesc + "???<font color=\"white\">"+ obj.getUserRealName() + "<br/>?????????" + obj.getBiosName() + "</font><br/>");
						}
					}
				}
			}
		}

		// ??????????????????
		List<NmsAsset> as = nService.findassetBynodeId(nodeId);
		if (as.size() == 0) {
			msg.append("?????????????????????");
			data.put("info", msg.toString());
			return data;
		}
		
		try {
			/* ???????????????????????? */
			try {
				NmsAlarm eventList = new NmsAlarm();
				List<NmsAlarm> alarmlist = nService.findwealarmById(nodeId);
				if (alarmlist.size() == 0) {
					alarm_status = "<font color=\"green\">?????????</font>";
				} else {
					int alarm_level = 0;
					for (int i = 0; i < alarmlist.size(); i++) {
						eventList = (NmsAlarm) alarmlist.get(i);
						if (eventList.getALevel() > alarm_level) {
							alarm_level = eventList.getALevel();
						}
					}
					if (alarm_level == 1) {
						alarm_status = "<font color=\"yellow\">????????????</font>";
					} else if (alarm_level == 2) {
						alarm_status = "<font color=\"orange\">????????????</font>";
					} else if (alarm_level == 3) {
						alarm_status = "<font color=\"red\">????????????</font>";
					} else {
						alarm_status = "";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				data.put("state", "1");
			}
			
			// ??????????????????
			List<NmsPingInfo> pinglist = nService.findpingById(nodeId);
			if (pinglist != null && pinglist.size() > 0) {
				NmsPingInfo pingObj = (NmsPingInfo) pinglist.get(0);
				if (pingObj != null) {
					DecimalFormat df = new DecimalFormat("0");
					ping_status = df.format(pingObj.getPingRate() * 100) + "%";
				}
			}
			// ??????CPU??????, ?????????????????????CPU????????????
			List<NmsCpuInfo> cpulist = nService.findcpuById(nodeId);
			if (cpulist != null && cpulist.size() > 0) {
				float cpuRate = 0;
				for (int i = 0; i < cpulist.size(); i++) {
					NmsCpuInfo cpuObj = (NmsCpuInfo) cpulist.get(i);
					if (cpuObj != null) {
						cpuRate += cpuObj.getCpuRate();
					}
				}
				cpuRate = cpuRate / cpulist.size();
				DecimalFormat df = new DecimalFormat("0.00");
				cpu_status = df.format(cpuRate) + "%";
			}

			// ?????????????????????
			List<NmsMemInfo> memlist = nService.findmeminfoById(nodeId);
			if (memlist != null && memlist.size() > 0) {
				NmsMemInfo memObj = (NmsMemInfo) memlist.get(0);
				if (memObj != null) {
					if (memObj.getMemTotal() != null && memObj.getMemFree() != null) {
						long total = memObj.getMemTotal();
						long used = memObj.getMemTotal() - memObj.getMemFree();
						float memRate = 0;
						if (total > 0 && total >= used) {
							memRate = (float) (used * 1.0 / total * 1.0) * 100;
							DecimalFormat df = new DecimalFormat("0.00");
							mem_status = df.format(memRate) + "%";
						}
					}
				}
			}

			// ????????????HTML
			if (!cpu_status.equals("")) {
				msg.append("CPU????????????<font color=\"white\">" + cpu_status + "</font><br/>");
			} else {
				msg.append("CPU????????????<font color=\"white\">-</font><br/>");
			}
			if (!mem_status.equals("")) {
				msg.append("??????????????????<font color=\"white\">" + mem_status + "</font><br/>");
			} else {
				msg.append("??????????????????<font color=\"white\">-</font><br/>");
			}
			if (!ping_status.equals("")) {
				msg.append("???????????????<font color=\"white\">" + ping_status + "</font><br/>");
			} else {
				msg.append("???????????????<font color=\"white\">-</font><br/>");
			}
			if (!alarm_status.equals("")) {
				msg.append("???????????????<font color=\"red\">" + alarm_status + "</font><br/>");
			} else {
				msg.append("???????????????<font color=\"red\">-</font><br/>");
			}
			
			// ?????????????????????????????????????????????????????????
			for (int i = 0; i < appList.size(); i++) {
				NmsYthApp obj = (NmsYthApp)appList.get(i);
				msg.append("???????????????<font color=\"white\">" + obj.getAppName() + "</font><br/>");
			}
			
			//???????????????????????????????????????????????????????????????,??????????????????????????????????????????
			String chType = (String)node.getNmsAssetType().getChType();
			if (chType.contains("?????????")) { 
				data.put("title", "???????????????????????????");
				data.put("account", appAccount.toString());
			} else if (chType.contains("?????????")) {
				data.put("title", "???????????????????????????");
				data.put("account", account.toString());
			}
			
			//?????????????????????????????????
			returns = msg.toString();
		} catch (Exception e) {
			e.printStackTrace();
			data.put("state", "1");
			data.put("info", "?????????????????????");
			return data;
		}
		data.put("info", returns);
		return data;
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/refreshImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map refreshImage(HttpServletRequest request, HttpServletResponse response) {
		Map data = new HashMap();
		String nodeArray = request.getParameter("nodeArray");
		if (nodeArray == null) {
			return null;
		}
		JSONArray array = JSONArray.fromObject(nodeArray);
		ArrayList<Objectbean> obj = (ArrayList<Objectbean>) JSONArray.toCollection(array, Objectbean.class);

		if (obj != null && obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				try {
					Objectbean objbean = obj.get(i);
					String nodeId = objbean.getNode_id().substring(3);
					if (objbean.getNode_id().substring(0, 3).equals("hin")) {
						continue;
					}
					// ???????????????????????????????????????????????????????????????????????????
					List<NmsAsset> startas = nService.findassetBynodeId(nodeId);
					if (startas.size() == 0) {
						String src = objbean.getImage_src();
						String path = src.substring(0, src.lastIndexOf("_"));
						objbean.setImage_src(path + "_green.png");
						objbean.setFont_color("deleted");
						continue;
					}
					// ?????????????????????????????????????????????????????????????????????????????????
					if (startas.get(0).getColled() != 0) {
						String src = objbean.getImage_src();
						String path = src.substring(0, src.lastIndexOf("_"));
						objbean.setImage_src(path + "_green.png");
						objbean.setFont_color("deleted");
						continue;
					}
					
					// ????????????????????????????????????????????????????????????????????????????????????
					if (startas.get(0).getOnline() != 1) {
						String src = objbean.getImage_src();
						String path = src.substring(0, src.lastIndexOf("_"));
						objbean.setImage_src(path + "_green.png");
						objbean.setFont_color("deleted");
						continue;
					}

					try {
						int alarm_level = nService.findeventlistById(nodeId);
						String src = objbean.getImage_src();
						String path = src.substring(0, src.lastIndexOf("_"));
						if (alarm_level == 0) {
							objbean.setImage_src(path + "_green.png");
							objbean.setFont_color("green");
						} else {
							if (alarm_level == 1) {
								objbean.setImage_src(path + "_yellow.png");
								objbean.setFont_color("yellow");
							} else if (alarm_level == 2) {
								objbean.setImage_src(path + "_orange.png");
								objbean.setFont_color("orange");
							} else if (alarm_level == 3) {
								objbean.setImage_src(path + "_red.png");
								objbean.setFont_color("red");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		data.put("value", obj);
		return data;
	}

/*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/refreshImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map refreshImage(HttpServletRequest request,
			HttpServletResponse response) {
		Map data = new HashMap();
		String nodeArray = request.getParameter("nodeArray");
		JSONArray array = JSONArray.fromObject(nodeArray);
		ArrayList<Objectbean> obj = (ArrayList<Objectbean>) JSONArray.toCollection(array, Objectbean.class);

		if (obj != null && obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				try {
					Objectbean objbean = obj.get(i);
					String nodeId = objbean.getNode_id().substring(3);
					if (objbean.getNode_id().substring(0, 3).equals("hin")) {
						continue;
					}
					List<NmsAsset> startas = nService.findassetBynodeId(nodeId);
					if (startas.size() == 0) {
						continue;
					}
					// ????????????????????????,???????????????????????????,?????????????????????????????????????????????????????????????????????,?????????????????????????????????
					List<NmsYthApp> appList = appInfoService.getListByAssetId(nodeId, 20);
					String src = objbean.getImage_src();
					String path = src.substring(0, src.lastIndexOf("_"));
					
					// ????????????????????????????????????????????????????????????????????????????????????????????????
					if (startas.get(0).getNmsAssetType().getChType().contains("???????????????")) {
						int status = 0;
						for (int j = 0; appList != null && j < appList.size() ; j++) {
							NmsYthApp appOobj = (NmsYthApp)appList.get(j);
							Long appId = appOobj.getId();
							List<NmsYthAppAccount> appCountList = appAccountInfoService.getList(String.valueOf(appId));
							if (appCountList.size() == 0) {
								status++;
							}
						}
						// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						if (appList.size() > 0 && status == 0) {
							objbean.setImage_src(path + "_yellow.png");
							objbean.setFont_color("yellow");
						} else {
							objbean.setImage_src(path + "_red.png");
							objbean.setFont_color("red");
						}
					} else if (startas.get(0).getNmsAssetType().getChType().contains("???????????????")){
						objbean.setImage_src(path + "_yellow.png");
						objbean.setFont_color("yellow");
					} else {
						objbean.setImage_src(path + "_green.png");
						objbean.setFont_color("green");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		data.put("value", obj);
		return data;
	}	
*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/refreshLink", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map refreshLink(HttpServletRequest request,
			HttpServletResponse response) {
		Map data = new HashMap();
		String linkArray = request.getParameter("linkArray");
		JSONArray array = JSONArray.fromObject(linkArray);
		ArrayList<Linkbean> link = (ArrayList<Linkbean>) JSONArray.toCollection(array, Linkbean.class);
		if (link != null && link.size() > 0) {
			for (int i = 0; i < link.size(); i++) {
				try {
					Linkbean linkbean = link.get(i);
					String startNodeId = linkbean.getStart_node_id();
					String start_node_id = linkbean.getStart_node_id().substring(3);
					String start_index = linkbean.getStart_index();
					String endNodeId = linkbean.getEnd_node_id();
					String end_node_id = linkbean.getEnd_node_id().substring(3);
					String end_index = linkbean.getEnd_index();
					String link_type = linkbean.getLink_type();

					// ???????????????????????????, 1??????????????????, 0??????????????????
					if (!link_type.equals("0")) {
						continue;
					}
					// ??????????????????????????????
					if (startNodeId.substring(0, 3).equals("hin") && endNodeId.substring(0, 3).equals("hin")) {
						continue;
					} else {
						if (start_node_id == null || start_node_id == "" || end_node_id == null || end_node_id == "") {
							continue;
						}
						List<NmsAsset> startas = nService.findassetBynodeId(start_node_id);
						List<NmsAsset> endas = nService.findassetBynodeId(end_node_id);
						if (startas == null || endas == null || startas.size() == 0 || endas.size() == 0) {
							linkbean.setLink_color("deleted");
							continue;
						}
						
						if (startas == null || endas == null || startas.get(0).getOnline() != 1 || endas.get(0).getOnline() != 1) {
							linkbean.setLink_color("deleted");
							continue;
						}
					}
					
					// ?????????????????????????????????????????????????????????????????????????????????
					List<NmsAsset> nmsasset = nService.nodeIsNotMonitor(start_node_id);
					if (nmsasset.get(0).getColled() != 0) {
						linkbean.setLink_dash("dashed");
						linkbean.setLink_color("gray");
						continue;
					} else {
						linkbean.setLink_dash("solid");
					}
					// ?????????????????????????????????????????????????????????????????????????????????
					nmsasset = nService.nodeIsNotMonitor(end_node_id);
					if (nmsasset.get(0).getColled() != 0) {
						linkbean.setLink_dash("dashed");
						linkbean.setLink_color("gray");
						continue;
					} else {
						linkbean.setLink_dash("solid");
					}
					// ??????????????????????????????????????????????????????????????????????????????????????????????????????
					int start_admin_status;
					int start_oper_status;
					int end_admin_status;
					int end_oper_status;
					List<NmsNetifInfo> netlist = nService.findinfoById(start_node_id, start_index);
					if (netlist != null && netlist.size() > 0) {
						NmsNetifInfo temp = (NmsNetifInfo) netlist.get(0);
						start_admin_status = temp.getIfAdminStatus();
						start_oper_status = temp.getIfOperStatus();
					} else {
						linkbean.setLink_color("red");
						continue;
					}
					
					List<NmsNetifInfo> netendlist = nService.findinfoById(end_node_id, end_index);
					if (netendlist != null && netendlist.size() > 0) {
						NmsNetifInfo temp = (NmsNetifInfo) netendlist.get(0);
						end_admin_status = temp.getIfAdminStatus();
						end_oper_status = temp.getIfOperStatus();
					} else {
						linkbean.setLink_color("red");
						continue;
					}

					if (start_admin_status == 1 && start_oper_status == 1 && end_admin_status == 1 && end_oper_status == 1) {
						// ????????????????????????(??????????????????????????????,??????????????????????????????,??????????????????????????????,???????????????????????????, ??????????????????)
						String linkId = linkbean.getId();
						if (linkId != null && linkId.contains("line_")) {
							String id = linkId.replace("line_", "");
							NmsTopoLink linkObj = nService.findlinkById(Integer.valueOf(id));
							if (linkObj != null) {
								String color = linkObj.getCol2();
								linkbean.setLink_color(color);
							} else {
								linkbean.setLink_color("green");
							}	
						} else {
							linkbean.setLink_color("green");
						}
					} else {
						linkbean.setLink_color("red");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		data.put("value", link);
		return data;
	}

	@SuppressWarnings("rawtypes")
	public boolean judgeRealTime(String table, String node_id, String index) {
		List list = null;
		if (table.equals("nms_ping_info")) {
			list = (List<NmsPingInfo>) nService.findpingById(node_id);

		}
		if (table.equals("nms_cpu_info")) {
			list = (List<NmsCpuInfo>) nService.findcpuById(node_id);
		}
		if (table.equals("nms_mem_info")) {
			list = (List<NmsMemInfo>) nService.findmeminfoById(node_id);
		}
		if (table.equals("nms_diskio_info")) {
			list = (List<NmsDiskioInfo>) nService.finddiskById(node_id);
		}
		if (table.equals("nms_netif_info")) {
			list = (List<NmsNetifInfo>) nService.findNetifById(node_id);
		}
		if (list != null && list.size() > 0) {
			NmsNetifInfo temp = (NmsNetifInfo) list.get(0);
			String itime = String.valueOf(temp.getItime());
			try {
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ntime = format.format(date);

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d_itime = df.parse(itime);
				Date d_now = df.parse(ntime);
				long diff = (d_now.getTime() - d_itime.getTime()) / 1000;
				if (diff < 300 * 60) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	
	@RequestMapping(value = "/readyTopoArea", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsTopoArea> readyTopoArea(HttpServletRequest request, HttpServletResponse response) {
		String mapId = request.getParameter("mapId");
		List<NmsTopoArea> areaList = new ArrayList<NmsTopoArea>();
		if (mapId != null) {
			areaList = nService.findAllArea(mapId);
		}
		return areaList;
	}
	
	/**
	 * ?????????????????????---????????????
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author antiy
	 */
	@RequestMapping(value = "/topoAreas", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsTopoArea> getAreas(HttpServletRequest request, HttpServletResponse response) {
		String mapId = request.getParameter("mapId");
		String divName = request.getParameter("divName");
		int begin = NumberUtils.toInt(request.getParameter("begin"), -1);
		int offset = NumberUtils.toInt(request.getParameter("offset"), -1);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mapId", mapId);
		map.put("divName", divName);
		map.put("begin", begin);
		map.put("offset", offset);
		return nService.findAllArea(map);
	}
	
	@RequestMapping(value = "/editTopoArea", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> editTopoArea(HttpServletRequest request, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String id = request.getParameter("id");
		String divLeft = request.getParameter("divLeft");
		String divTop = request.getParameter("divTop");
		String divWidth = request.getParameter("divWidth");
		String divHeight = request.getParameter("divHeight");
		
		if (id == null || id.equals("") || !id.contains("drsElement_")) {
			data.put("state", "1");
			data.put("info", "??????id?????????!");
			return data;
		}
		if (divLeft == null || divLeft.equals("")) {
			data.put("state", "1");
			data.put("info", "??????divLeft?????????!");
			return data;
		}
		if (divTop == null || divTop.equals("")) {
			data.put("state", "1");
			data.put("info", "??????divTop?????????!");
			return data;
		}
		if (divWidth == null || divWidth.equals("")) {
			data.put("state", "1");
			data.put("info", "??????divWidth?????????!");
			return data;
		}
		if (divHeight == null || divHeight.equals("")) {
			data.put("state", "1");
			data.put("info", "??????divHeight?????????!");
			return data;
		}
		int updateId = 0;
		try {
			updateId = Integer.valueOf(id.replace("drsElement_", ""));
		} catch(Exception e) {
			System.out.println("" + e.toString());
			data.put("state", "1");
			data.put("info", "???????????????id???" + id + "??????,????????????!");
			return data;
		}
//		System.out.println("id=" + updateId);
		
		NmsTopoArea na = nService.findAreaByDivId(updateId);
		if (na == null) {
			data.put("state", "1");
			data.put("info", "?????????" + id + "?????????!");
			return data;
		}
		na.setDivLeft(divLeft);
		na.setDivTop(divTop);
		na.setDivWidth(divWidth);
		na.setDivHeight(divHeight);
		
		if (nService.updateAreaByDivId(na)) {
			data.put("state", "0");
			data.put("info", "????????????!");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "????????????!");
			return data;
		}
	}	

	/**
	 * topu??????---????????????
	 * @param request
	 * @param session
	 * @return
	 * 
	 * @author antiy
	 */
	@RequestMapping(value = "/updateTopoArea", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> updateTopoArea(HttpServletRequest request, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		int id = NumberUtils.toInt(request.getParameter("id"), -1);
		String divName = request.getParameter("divName");
		int mapId = NumberUtils.toInt(request.getParameter("mapId"), -1);
		if (mapId == -1 || id == -1) {
			data.put("state", "3");
			data.put("info", "????????????!");
			return data;
		}
		NmsTopoArea na = new NmsTopoArea();
		na.setDivName(divName);
		na.setMapId(mapId);
		na.setId(id);
		return nService.updateArea(na);
	}
	
	@RequestMapping(value = "/deleteTopoArea", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> deleteTopoArea(HttpServletRequest request, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		int id = NumberUtils.toInt(request.getParameter("id"), -1);
		int mapId = NumberUtils.toInt(request.getParameter("mapId"), -1);
		if (id == -1) {
			data.put("state", "1");
			data.put("info", "??????id?????????!");
			return data;
		}
		if (mapId == -1) {
			data.put("state", "1");
			data.put("info", "???????????????id?????????!");
			return data;
		}

		NmsTopoArea na = nService.findAreaByDivId(id);
		if (na == null) {
			data.put("state", "1");
			data.put("info", "?????????" + id + "?????????");
			return data;
		}
		if(CollectionUtils.isNotEmpty(nService.findNodeByParames(mapId, id))) {
			data.put("state", "1");
			data.put("info", "??????????????????????????????????????????????????????????????????????????????");
			return data;
		}
		
		na.setDeled(1);
		if (nService.updateAreaByDivId(na)) {
			data.put("state", "0");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????? [" + na.getDivName() + "] ??????",
					"????????????", "???????????????", "??????");
			return data;
		} else {
			data.put("state", "1");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????????????? [" + na.getDivName() + "] ??????",
					"????????????", "???????????????", "??????");
			return data;
		}
	}
	
	
	@RequestMapping(value = "/updateNodeBinding", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> updateNodeBinding(HttpServletRequest request, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		// nodeId
		int id = NumberUtils.toInt(request.getParameter("id"), -1);
		// containerId
		int containerId = NumberUtils.toInt(request.getParameter("containerId"), -1);

		List<NmsTopoNode> nodes = nService.findNodeById(id);
		if (CollectionUtils.isEmpty(nodes)) {
			data.put("state", "1");
			data.put("info", "??????id=" + id + "?????????,?????????????????????");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "??????Id [" + id + "] ????????????",
					"????????????", "???????????????", "??????");
			return data;
		}

		if (nService.bindContainer(id, containerId)) {
			data.put("state", "0");
			data.put("info", "????????????!");
			
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "?????? [" + nodes.get(0).getAlias() + "] ?????????????????????",
					"????????????", "???????????????", "??????");
			return data;
		} else {
			data.put("state", "1");
			data.put("info", "????????????!");
			return data;
		}
	}
	
	
	@RequestMapping(value = "/addTopoArea", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addTopoArea(HttpServletRequest request, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		String mapId = request.getParameter("mapId");
		String divName = request.getParameter("divName");
		String divTop = request.getParameter("divTop");
		String divLeft = request.getParameter("divLeft");
		String divWidth = request.getParameter("divWidth");
		String divHeight = request.getParameter("divHeight");
		String lineWidth = request.getParameter("lineWidth");
		String lineDashed = request.getParameter("lineDashed");
		String lineColor = request.getParameter("lineColor");
		lineColor = "#" + lineColor;
		
		if(StringUtils.isBlank(divName)) {
			data.put("state", "1");
			data.put("info", "????????????!??????????????????");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + divName + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
		NmsTopoArea na = new NmsTopoArea();
		na.setId(0);
		na.setMapId(Integer.valueOf(mapId));
		na.setDivName(divName);
		na.setDivTop(divTop);
		na.setDivWidth(divWidth);
		na.setDivLeft(divLeft);
		na.setDivHeight(divHeight);
		na.setLineWidth(lineWidth);
		na.setLineDashed(lineDashed);
		na.setLineColor(lineColor);
		NmsTopoArea updateNms = nService.findAreaByDivNameAndMapId(divName, na.getMapId());
		if (updateNms != null) {
			data.put("state", "1");
			data.put("info", "????????????!????????????");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + divName + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
		if (nService.addTopoArea(na)) {
			data.put("state", "0");
			data.put("info", "??????????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + divName + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		} else {
			data.put("state", "1");
			data.put("info", "????????????!");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "???????????? [" + divName + "] ??????",
					"????????????", "????????????", "??????");
			return data;
		}
	}

}