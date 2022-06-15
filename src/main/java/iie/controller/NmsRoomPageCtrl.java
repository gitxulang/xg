package iie.controller;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import iie.domain.RoomPageConfigJson;
import iie.pojo.NmsAdmin;
import iie.pojo.NmsRoomPage;
import iie.service.NmsAuditLogService;
import iie.service.NmsRoomPageService;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/RoomPage")
public class NmsRoomPageCtrl {
	@Autowired
	NmsRoomPageService nrpService;

    @Autowired
    NmsAuditLogService auditLogService;
    
	Logger logger = Logger.getLogger("roomPage");

	@RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsRoomPage> getAll() {
		List<NmsRoomPage> res = nrpService.getAll();
		return res;
	}

	@RequestMapping(value = "/getDefaultRoom", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Integer getDefaultRoom() {
		Integer roomId = nrpService.getDefaultShowRoomId();
		return roomId;
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsRoomPage getByRoomId(HttpServletRequest request, HttpSession session) {
		
		
		auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
				"进入机房管理", "机房管理", "查询数据", "成功");
		
		
        String roomId = request.getParameter("roomId");
		if (roomId != null) {
			roomId = StringEscapeUtils.escapeSql(roomId);
		}	
        
        if (!StringUtils.isEmpty(roomId)){
            NmsRoomPage nrp = nrpService.findById(Integer.parseInt(roomId));

            return nrp;
        }
        return null;
	}

	@RequestMapping(value = "/setDefault",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object setDefault(HttpServletRequest request){
		String roomId = request.getParameter("roomid");

		if (roomId != null) {
			roomId = StringEscapeUtils.escapeSql(roomId);
		}	
		
		nrpService.setDefault(Integer.parseInt(roomId));
		Map<String, String> data = new HashMap<String, String>();
		data.put("state", "0");
		data.put("info", "添加成功!");
		return data;
	}

	@RequestMapping(value = "/addRoom", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addRoom(@RequestBody String body){
	//	System.out.println(body);
		JSONObject jsonObject = JSONObject.fromObject(body);
		NmsRoomPage room = new NmsRoomPage();
		String roomId = jsonObject.getString("roomId");
		String roomCode = jsonObject.getString("roomCode");
		String roomDesc = jsonObject.getString("roomDesc");
		String roomLocation = jsonObject.getString("roomLocation");
		
		if (roomId != null) {
			roomId = StringEscapeUtils.escapeSql(roomId);
		}	
		
		if (roomCode != null) {
			roomCode = StringEscapeUtils.escapeSql(roomCode);
		}	
		
		if (roomDesc != null) {
			roomDesc = StringEscapeUtils.escapeSql(roomDesc);
		}	
		
		if (roomLocation != null) {
			roomLocation = StringEscapeUtils.escapeSql(roomLocation);
		}	
		
		
		Map<String, String> data = new HashMap<String, String>();
		if (!StringUtils.isEmpty(roomId)){
			room.setRoomId(0);
			try {
				room.setRoomId(Integer.parseInt(roomId));
			} catch (NumberFormatException e) {
                data.put("state", "1");
                data.put("info", "机房编号必须是数字类型!");
				return data;
			}
			
		//	room.setRoomId(Integer.parseInt(roomId));
			room.setRoomCode(roomCode);
			room.setRoomLocation(roomLocation);
			room.setRoomDesc(roomDesc);
			if (nrpService.addRoomPage(room)){
                data.put("state", "0");
                data.put("info", "添加成功!");
            }else {
                data.put("state", "1");
                data.put("info", "失败，检查机房编号是否已存在!");
            }
			return data;
		}
		data.put("state", "1");
		data.put("info", "添加失败!");
		return data;
	}

	@RequestMapping(value = "/deleteRoom",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteRoom(HttpServletRequest request){
		String ids = request.getParameter("id");
		
		String[] idArray = ids.split(",");
		Integer[] intIds = new Integer[idArray.length];
		for (int i=0;i<idArray.length;i++) {
			intIds[i] = Integer.parseInt(idArray[i]);
		}
		Map<String, String> data = new HashMap<String, String>();
		boolean b = nrpService.deleteRoom(intIds);
		if (b){
			data.put("state", "0");
			data.put("info", "删除成功!");
		}else {
			data.put("state", "1");
			data.put("info", "删除失败!");
		}
		return data;
	}



	/**
	 * 新增机房页面配置 传统接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNmsRoomPage(HttpServletRequest request)
			throws Exception {
		Map<String, String> data = new HashMap<String, String>();
		
		String roomId = request.getParameter("roomId");
		String webJson = request.getParameter("webJson");

		if (roomId != null) {
			roomId = StringEscapeUtils.escapeSql(roomId);
		}	

		
		NmsRoomPage nr = new NmsRoomPage();
		if (!StringUtils.isEmpty(roomId)){
			nr.setRoomId(Integer.parseInt(roomId));
		}
		if (!StringUtils.isEmpty(webJson)){
			nr.setWebJson(webJson);
		}
		if (nrpService.addRoomPage(nr)) {
			data.put("state", "0");
			data.put("info", "添加成功!");
			return data;
		} else {
			data.put("state", "1");
			data.put("info", "添加失败!");
			return data;
		}
	}

	/**
	 * 废弃
	 * 请求或者更新机房页面配置
	 * @param json
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/pagecfg", method = RequestMethod.POST)
	@ResponseBody
	public Object pagecfg(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String, String> data = new HashMap<String, String>();
		int roomId= jsonObject.getInt("roomid");
		String resource = jsonObject.getString("resource");

		
		
		if (resource.equalsIgnoreCase("getDefaultRackCfg")){
			// 返回webJson
			NmsRoomPage nmsRoomPage = nrpService.findById(roomId);
			if (null!=nmsRoomPage){
				//机房存在
				//获取数据库中的webjson字符串
				String webJson = nmsRoomPage.getWebJson();
				RoomPageConfigJson.Webjson[] ars = new Gson().fromJson(webJson,RoomPageConfigJson.Webjson[].class);
				//返回jsonArray
				return ars;
//				return nmsRoomPage;
			}else {
				data.put("state", "1");
				data.put("info", "机房不存在!");
				return data;
			}
		}

		if (resource.equalsIgnoreCase("uploadcfg")){
			// 更新pagecfg
//			String webjson = jsonObject.getString("webjson");
			String webjson = jsonObject.getJSONArray("webjson").toString();
			if (nrpService.updateRoomPage(roomId,webjson)){
				data.put("state", "0");
				data.put("info", "修改成功!");
				return data;
			}else {
				data.put("state", "1");
				data.put("info", "修改失败!");
				return data;
			}
		}
		return null;
	}

	@Deprecated
	@RequestMapping(value = "/toEdit")
	public String goRoomEdit(@RequestParam("roomid") Integer roomid, Model model){
		logger.info("roomid="+roomid);
		model.addAttribute("roomid",roomid);
		return "/roompage/roomedit";
	}

	/**
	 * 保存3d配置文件
	 */
	@RequestMapping(value = "/saveJson",method = RequestMethod.POST)
	@ResponseBody
	public Object saveRoomJson(@RequestParam String roomid, HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
		logger.info("saveJson --- roomid-----"+roomid);
		String filepath = request.getServletContext().getRealPath(File.separator+"roompage"+File.separator+"roomscene");
		File file = new File(filepath+File.separator+"room_"+roomid+".json");
		if (file.exists()){
			file.delete();
		}
		OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(file),
				"UTF-8");
		BufferedReader br = null;
		BufferedWriter bw = new BufferedWriter(outStream);
		try {
			InputStreamReader isr = new InputStreamReader(request.getInputStream(),"UTF-8");
			String str;
			br = new BufferedReader(isr);
			while ((str=br.readLine())!=null){
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (null!=br){
				br.close();
			}
			if (null!=bw){
				bw.close();
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("state", "0");
		result.put("info", "修改成功!");
		return result;
	}

	/**
	 * 保存3d room配置文件
	 */
	@RequestMapping(value = "/saveSetJson",method = RequestMethod.POST)
	@ResponseBody
	public Object saveRoomSetJson(@RequestParam String roomid, HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
		
		
		String filepath = request.getServletContext().getRealPath(File.separator+"roompage"+File.separator+"roomscene");
		File file = new File(filepath+File.separator+"room_set_"+roomid+".json");
		if (file.exists()){
			file.delete();
		}
		OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(file),
				"UTF-8");
		BufferedWriter bw = new BufferedWriter(outStream);
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(request.getInputStream(),"UTF-8");
			String str;
			br = new BufferedReader(isr);
			while ((str=br.readLine())!=null){
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (null!=br){
				br.close();
			}
			if (null!=bw){
				bw.close();
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("state", "0");
		result.put("info", "修改成功!");
		return result;
	}

}
