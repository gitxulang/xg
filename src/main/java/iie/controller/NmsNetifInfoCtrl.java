package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsNetifInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsNetifInfoService;
import iie.tools.NmsNetifItem;
import iie.tools.PageBean;
import iie.tools.excel.DateUtil;
import iie.tools.excel.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/NetifInfo")
public class NmsNetifInfoCtrl {

	@Autowired
	NmsNetifInfoService nService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsNetifInfo> getAll() {
		List<NmsNetifInfo> res = nService.getAll();
		return res;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNn(HttpServletRequest request)
			throws Exception {

	//	System.out.println("begin");

		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		int freq = Integer.valueOf(request.getParameter("freq"));
		int if_index = Integer.valueOf(request.getParameter("if_index"));
		String if_descr = request.getParameter("if_descr");
		int if_type = Integer.valueOf(request.getParameter("if_type"));
		int if_mtu = Integer.valueOf(request.getParameter("if_mtu"));
		long if_speed = Long.valueOf(request.getParameter("if_speed"));
		String if_ip = request.getParameter("if_ip");
		String if_submask = request.getParameter("if_submask");
		String if_gateway = request.getParameter("if_gateway");
		String if_physaddr = request.getParameter("if_physaddr");
		int if_admin_status = Integer.valueOf(request
				.getParameter("if_admin_status"));
		int if_oper_status = Integer.valueOf(request
				.getParameter("if_oper_status"));
		long if_in_octets = Long.valueOf(request.getParameter("if_in_octets"));
		long if_in_ucastpkts = Long.valueOf(request
				.getParameter("if_in_ucastpkts"));
		long if_in_nucastpkts = Long.valueOf(request
				.getParameter("if_in_nucastpkts"));
		long if_in_discards = Long.valueOf(request
				.getParameter("if_in_discards"));
		long if_in_errors = Long.valueOf(request.getParameter("if_in_errors"));
		long if_out_octets = Long
				.valueOf(request.getParameter("if_out_octets"));
		long if_out_ucastpkts = Long.valueOf(request
				.getParameter("if_out_ucastpkts"));
		long if_out_nucastpkts = Long.valueOf(request
				.getParameter("if_out_nucastpkts"));
		long if_out_discards = Long.valueOf(request
				.getParameter("if_out_discards"));
		long if_out_errors = Long
				.valueOf(request.getParameter("if_out_errors"));
		long if_in_icmps = Long.valueOf(request.getParameter("if_in_icmps"));
		long if_out_icmps = Long.valueOf(request.getParameter("if_out_icmps"));
		NmsAsset na = nService.findById(asset_id);
		NmsNetifInfo nn = new NmsNetifInfo(na, freq, if_index, if_descr,
				if_type, if_mtu, if_speed, if_ip, if_submask, if_gateway,
				if_physaddr, if_admin_status, if_oper_status, if_in_octets,
				if_in_ucastpkts, if_in_nucastpkts, if_in_discards,
				if_in_errors, if_out_octets, if_out_ucastpkts,
				if_out_nucastpkts, if_out_discards, if_out_errors, if_in_icmps,
				if_out_icmps, new Timestamp(System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (nService.saveMem(nn)) {
				data.put("state", "0");
				data.put("info", "添加成功!");
			} else {
				data.put("state", "1");
				data.put("info", "添加失败!");
			}
		}
		return data;
	}

	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsNetifInfo> listPageByDate(HttpServletRequest request, HttpSession session)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");
		if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
			orderKey = "itime";
			orderValue = "1";
		}
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		int begin = Integer.valueOf(request.getParameter("begin"));
		int offset = Integer.valueOf(request.getParameter("offset"));
		String assetId = request.getParameter("assetId");


		PageBean<NmsNetifInfo> page = nService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：接口信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
			}
		}		
		
		return page;
	}

	@RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsNetifInfo getNetifInfoDetail(HttpServletRequest request)
			throws Exception {

		int id = Integer.valueOf(request.getParameter("id"));
		NmsNetifInfo detail = nService.getNetifInfoDetail(id);
		if (detail == null) {
			detail = new NmsNetifInfo();
		}
		return detail;
	}

	@RequestMapping(value = "/ServerDetail/NetifInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsNetifItem> listPageByAssetId(HttpServletRequest request, HttpSession session)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String strBegin = request.getParameter("begin");
		String stroffset = request.getParameter("offset");
		int begin = Integer.valueOf(strBegin);
		int offset = Integer.valueOf(stroffset);

		String if_descr = request.getParameter("if_descr");
		String if_ip = request.getParameter("if_ip");
		String if_physaddr = request.getParameter("if_physaddr");
		
		String assetId = request.getParameter("assetId");
		PageBean<NmsNetifItem> page = nService.getPageByAssetId(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId, if_descr, if_ip, if_physaddr);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看设备详情：流速信息 | " + name + " | " + ip, "监控管理", "查询数据", "成功");	
			}
		}
		return page;
	}

	@RequestMapping(value = "/list/date/ExportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean listPageByDateExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");
		
		if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
			orderKey = "itime";
			orderValue = "0";
		}		
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String assetId = request.getParameter("assetId");

		List<NmsNetifInfo> list;

		try {
			list = nService.getPageByDateExportExcel(orderKey, orderValue,
					startDate, endDate, assetId);
			
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("接口描述");
			namesOfHeader.add("管理状态");
			namesOfHeader.add("运行状态");
			namesOfHeader.add("类型");
			namesOfHeader.add("IP地址");
			namesOfHeader.add("子网掩码");
			namesOfHeader.add("网关地址");
			namesOfHeader.add("物理地址");
			
			namesOfHeader.add("MTU（Mb）");
			namesOfHeader.add("Speed（Mb）");
			namesOfHeader.add("收到字节（byte）");
			namesOfHeader.add("发送字节（byte）");
			namesOfHeader.add("因接收资源紧张丢弃包（个）");
			namesOfHeader.add("因发送资源紧张丢弃包（个）");
			namesOfHeader.add("因接收出错丢弃包（个）");
			namesOfHeader.add("因发送出错丢弃包（个）");
			
			namesOfHeader.add("收到单播包（个）");
			namesOfHeader.add("发送单播包（个）");
			namesOfHeader.add("收到广播包（个）");
			namesOfHeader.add("发送广播包（个）");
			namesOfHeader.add("设备接收ICMP报文（个）");
			namesOfHeader.add("设备发送ICMP报文（个）");
			
			namesOfHeader.add("采集时间");
			/* namesOfHeader.add("操作"); */
			
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("ifDescr");
			namesOfField.add("ifAdminStatus");
			namesOfField.add("ifOperStatus");
			namesOfField.add("ifType");
			namesOfField.add("ifIp");
			namesOfField.add("ifSubmask");
			namesOfField.add("ifGateway");
			namesOfField.add("ifPhysaddr");

			namesOfField.add("ifMtu");
			namesOfField.add("ifSpeed");
			namesOfField.add("ifInOctets");
			namesOfField.add("ifOutOctets");
			namesOfField.add("ifInDiscards");
			namesOfField.add("ifOutDiscards");
			namesOfField.add("ifInErrors");
			namesOfField.add("ifOutErrors");
			
			namesOfField.add("ifInUcastpkts");
			namesOfField.add("ifOutUcastpkts");
			namesOfField.add("ifInNucastpkts");
			namesOfField.add("ifOutNucastpkts");
			namesOfField.add("ifInIcmps");
			namesOfField.add("ifOutIcmps");
			
			
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "接口信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsNetifInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getIfDescr());
				String var2 = "--";
				int ifAdminStatus = var.getIfAdminStatus();
				if (ifAdminStatus == 1) {
					var2 = "up";
				} else if (ifAdminStatus == 2) {
					var2 = "down";
				} else {
					var2 = "其它";
				}
				vpd.put("var2",var2);
				String var3 = "--";
				int ifOperStatus = var.getIfOperStatus();
				if (ifOperStatus == 1) {
					var3 = "up";
				} else if (ifOperStatus == 2) {
					var3 = "down";
				} else {
					var3 = "其它";
				}
				vpd.put("var3",var3);
				String var4 = "--";
				int ifType = var.getIfType();
				if (ifType == 6) {
					var4 = "以太网";
				} else if (ifType == 24) {
					var4 = "本地回环";
				} else {
					var4 = "其它";
				}
				vpd.put("var4",var4);
				vpd.put("var5",var.getIfIp());
				vpd.put("var6",var.getIfSubmask());
				vpd.put("var7",var.getIfGateway());
				vpd.put("var8",var.getIfPhysaddr());
				vpd.put("var9",String.valueOf(var.getIfMtu()));
				vpd.put("var10",String.valueOf(var.getIfSpeed()));
				vpd.put("var11",String.valueOf(var.getIfInOctets()));
				vpd.put("var12",String.valueOf(var.getIfOutOctets()));
				vpd.put("var13",String.valueOf(var.getIfInDiscards()));
				vpd.put("var14",String.valueOf(var.getIfOutDiscards()));
				vpd.put("var15",String.valueOf(var.getIfInErrors()));
				vpd.put("var16",String.valueOf(var.getIfOutErrors()));
				vpd.put("var17",String.valueOf(var.getIfInUcastpkts()));
				vpd.put("var18",String.valueOf(var.getIfOutUcastpkts()));
				vpd.put("var19",String.valueOf(var.getIfInNucastpkts()));
				vpd.put("var20",String.valueOf(var.getIfOutNucastpkts()));
				vpd.put("var21",String.valueOf(var.getIfInIcmps()));
				vpd.put("var22",String.valueOf(var.getIfOutIcmps()));
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var23",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			/*NmsExcelUtilForNetInfo excelUtil = new NmsExcelUtilForNetInfo(list,
					NmsNetifInfo.class, "接口信息报表"+asset.getAIp(), "接口信息报表"+asset.getAIp(), "接口信息报表"+asset.getAIp(),
					namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){

				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备接口信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
