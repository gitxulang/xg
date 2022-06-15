package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsMemInfo;
import iie.pojo.NmsPingInfo;
import iie.service.NmsAuditLogService;
import iie.service.NmsPerformanceService;
import iie.tools.*;
import iie.tools.excel.ExcelHelper;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "performance")
public class NmsPerformanceCtrl {

	@Autowired
	private NmsPerformanceService nmsPerformanceService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/cpu/info/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsCpuInfoDetail listCpuInfoByIdCondition(HttpServletRequest request) {
		String AIp = request.getParameter("AIp");
		String idParameter = request.getParameter("id");
		Integer id = null;
		if (idParameter != null && idParameter.length() > 0) {
			id = Integer.valueOf(idParameter);
		}

		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	
		
		return nmsPerformanceService.listCpuInfoByCondition(AIp, id);
	}

	@RequestMapping(value = "/mem/info/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsMemInfo listMemInfoByIdCondition(HttpServletRequest request) {
		String AIp = request.getParameter("AIp");
		String idParameter = request.getParameter("id");
		Integer id = null;
		if (idParameter != null && idParameter.length() > 0) {
			id = Integer.valueOf(idParameter);
		}

		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	
		
		return nmsPerformanceService.listMemInfoByCondition(AIp, id);
	}

	@RequestMapping(value = "/net/info/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsNetInfoDetail listNetInfoByCondition(HttpServletRequest request) {
		String AIp = request.getParameter("AIp");
		String idParameter = request.getParameter("id");
		Integer id = null;
		if (idParameter != null && idParameter.length() > 0) {
			id = Integer.valueOf(idParameter);
		}

		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	
		
		return nmsPerformanceService.listNetInfoByCondition(AIp, id);
	}

	@RequestMapping(value = "/ping/info/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsPingInfo listPingInfoByCondition(HttpServletRequest request) {
		String AIp = request.getParameter("AIp");
		String idParameter = request.getParameter("id");
		Integer id = null;
		if (idParameter != null && idParameter.length() > 0) {
			id = Integer.valueOf(idParameter);
		}
		
		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	

		return nmsPerformanceService.listPingInfoByCondition(AIp, id);
	}

	@RequestMapping(value = "/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsPerformanceRecord> listPageByCondition(
			HttpServletRequest request, HttpSession session) {

		String orderValueParameter = request.getParameter("orderValue");
		int orderValue = 0;
		if (orderValueParameter != null && orderValueParameter.length() > 0) {
			orderValue = Integer.valueOf(orderValueParameter);
		}
		
		String orderKey = request.getParameter("orderKey");
		String AName = request.getParameter("AName");
		String AIp = request.getParameter("AIp");
		String nmsAssetType = request.getParameter("nmsAssetType");
		String APos = request.getParameter("APos");
		String ADept = request.getParameter("ADept");
		String beginParameter = request.getParameter("begin");
		String offsetParameter = request.getParameter("offset");
		int begin = 1;
		int offset = 10;
		if (beginParameter != null && beginParameter.length() > 0) {
			begin = Integer.valueOf(beginParameter);
		}
		if (offsetParameter != null && offsetParameter.length() > 0) {
			offset = Integer.valueOf(offsetParameter);
		}

		
		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	
		
		if (AName != null) {
			AName = StringEscapeUtils.escapeSql(AName);
		}	
		
		if (nmsAssetType != null) {
			nmsAssetType = StringEscapeUtils.escapeSql(nmsAssetType);
		}	
		
		if (APos != null) {
			APos = StringEscapeUtils.escapeSql(APos);
		}	
		
		if (ADept != null) {
			ADept = StringEscapeUtils.escapeSql(ADept);
		}	
		
		
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看性能列表", "监控管理", "查询数据", "成功");	
		
		return nmsPerformanceService.listNmsPerformanceByConditionById(orderKey,
				orderValue, AName, AIp, APos, nmsAssetType, ADept, begin, offset);
	}

	@RequestMapping(value = "/soft/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsPerformanceRecord> listSoftPageByCondition(
			HttpServletRequest request, HttpSession session) {

		String orderValueParameter = request.getParameter("orderValue");
		int orderValue = 0;
		if (orderValueParameter != null && orderValueParameter.length() > 0) {
			orderValue = Integer.valueOf(orderValueParameter);
		}

		String orderKey = request.getParameter("orderKey");
		String AName = request.getParameter("AName");
		String AIp = request.getParameter("AIp");
		String APort = request.getParameter("APort");
		String nmsAssetType = request.getParameter("nmsAssetType");
		String beginParameter = request.getParameter("begin");
		String offsetParameter = request.getParameter("offset");
		int begin = 1;
		int offset = 10;
		if (beginParameter != null && beginParameter.length() > 0) {
			begin = Integer.valueOf(beginParameter);
		}
		if (offsetParameter != null && offsetParameter.length() > 0) {
			offset = Integer.valueOf(offsetParameter);
		}

		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}

		if (APort != null) {
			APort = StringEscapeUtils.escapeSql(APort);
		}

		if (AName != null) {
			AName = StringEscapeUtils.escapeSql(AName);
		}

		if (nmsAssetType != null) {
			nmsAssetType = StringEscapeUtils.escapeSql(nmsAssetType);
		}

		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看性能列表", "监控管理", "查询数据", "成功");

		return nmsPerformanceService.listNmsPerformanceSoftByConditionById(orderKey,
				orderValue, AName, AIp, APort, nmsAssetType, begin, offset);
	}

	@RequestMapping(value = "/list/page/condition/realtime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsPerformanceItem> listPageByConditionRealtime(
			HttpServletRequest request, HttpSession session) {

		String orderValueParameter = request.getParameter("orderValue");
		int orderValue = 0;
		if (orderValueParameter != null && orderValueParameter.length() > 0) {
			orderValue = Integer.valueOf(orderValueParameter);
		}
		
		String orderKey = request.getParameter("orderKey");
		String AName = request.getParameter("AName");
		String AIp = request.getParameter("AIp");
		String nmsAssetType = request.getParameter("nmsAssetType");
		String APos = request.getParameter("APos");
		String ADept = request.getParameter("ADept");
		String beginParameter = request.getParameter("begin");
		String offsetParameter = request.getParameter("offset");
		int begin = 1;
		int offset = 10;
		if (beginParameter != null && beginParameter.length() > 0) {
			begin = Integer.valueOf(beginParameter);
		}
		if (offsetParameter != null && offsetParameter.length() > 0) {
			offset = Integer.valueOf(offsetParameter);
		}
		
		if (AName != null) {
			AName = StringEscapeUtils.escapeSql(AName);
		}	
		
		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	
		
		if (nmsAssetType != null) {
			nmsAssetType = StringEscapeUtils.escapeSql(nmsAssetType);
		}	
		
		if (APos != null) {
			APos = StringEscapeUtils.escapeSql(APos);
		}	
		
		if (ADept != null) {
			ADept = StringEscapeUtils.escapeSql(ADept);
		}	
		
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看实时性能报表", "报表管理", "查询数据", "成功");	
		
		return nmsPerformanceService.listNmsPerformanceByConditionRealtimeById(
				orderKey, orderValue, AName, AIp, APos, nmsAssetType, ADept, begin,
				offset);
	}

	@RequestMapping(value = "/list/page/condition/realtime/ExportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean listPageByConditionRealtimeExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException {

		int orderValue = 0;
		String orderKey = request.getParameter("orderKey");
		String orderValueParameter = request.getParameter("orderValue");

		if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
			orderKey = "id";
			orderValue = 0;
		} else {
			orderValue = Integer.valueOf(orderValueParameter);
		}
		
		String AName = request.getParameter("AName");
		String AIp = request.getParameter("AIp");
		String nmsAssetType = request.getParameter("nmsAssetType");
		String APos = request.getParameter("APos");
		String ADept = request.getParameter("ADept");
		List<NmsPerformanceItem> list;
		
		
		if (AName != null) {
			AName = StringEscapeUtils.escapeSql(AName);
		}	
		
		if (AIp != null) {
			AIp = StringEscapeUtils.escapeSql(AIp);
		}	
		
		if (nmsAssetType != null) {
			nmsAssetType = StringEscapeUtils.escapeSql(nmsAssetType);
		}	
		
		if (APos != null) {
			APos = StringEscapeUtils.escapeSql(APos);
		}	
		
		if (ADept != null) {
			ADept = StringEscapeUtils.escapeSql(ADept);
		}		
		
		try {
			list = nmsPerformanceService
					.listNmsPerformanceByConditionRealtimeExportExcelById(orderKey,
							orderValue, AName, AIp, APos, nmsAssetType, ADept);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("IP");
			namesOfHeader.add("名称");
			namesOfHeader.add("类型/类别");
			namesOfHeader.add("设备在线状态");
			namesOfHeader.add("连通率（%）");
			namesOfHeader.add("CPU使用（%）");
			namesOfHeader.add("内存容量（G）");
			namesOfHeader.add("内存利用率（%）");
			namesOfHeader.add("虚拟/SWAP内存（G）");
			namesOfHeader.add("虚拟/SWAP内存利用率（%）");
			namesOfHeader.add("流入速率（kB/s）");
			namesOfHeader.add("流出速率（kB/s）");
			/* namesOfHeader.add("操作"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("AName");
			namesOfField.add("AIp");
			namesOfField.add("chType");
			namesOfField.add("online");
			namesOfField.add("pingRate");
			namesOfField.add("cpuRate");
			namesOfField.add("memTotal");
			namesOfField.add("memRate");
			namesOfField.add("swapTotal");
			namesOfField.add("swapRate");
			namesOfField.add("netInSpeed");
			namesOfField.add("netOutSpeed");

			String docName = "性能报表管理";

			List<Map> varList = new ArrayList<Map>();//
			for (NmsPerformanceItem var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getAIp());
				vpd.put("var2",var.getAName());
				vpd.put("var3",var.getChType()+"/"+var.getSubChType());

				String var4 = "其它";
				String var5 = "--";
				String var6 = "--";
				String var7 = "--";
				String var8 = "--";
				String var9 = "--";
				String var10 = "--";
				String var11 = "--";
				String var12 = "--";
				if (var.getOnline() != null && var.getOnline() == 1) {
					var4 = "在线";
					var5 = new BigDecimal(Double.valueOf(var.getPingRate()) * 100).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
					var6 = new BigDecimal(Double.valueOf(var.getCpuRate())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();;
					var7 = new BigDecimal(Double.valueOf(var.getMemTotal())/1024/1024).setScale(2, BigDecimal.ROUND_HALF_UP).toString();;
					var8 = new BigDecimal(Double.valueOf(var.getMemRate())*100).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					var9 = new BigDecimal(Double.valueOf(var.getSwapTotal())/1024/1024).setScale(2, BigDecimal.ROUND_HALF_UP).toString();;
					var10 = new BigDecimal(Double.valueOf(var.getSwapRate())*100).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					var11 = String.valueOf(var.getNetInSpeed());
					var12 = String.valueOf(var.getNetOutSpeed());
				} else if(var.getOnline() != null && var.getOnline() == 0) {
					var4 = "离线";
				}
				vpd.put("var4",var4);
				vpd.put("var5",var5);
				vpd.put("var6",var6);
				vpd.put("var7",var7);
				vpd.put("var8",var8);
				vpd.put("var9",var9);
				vpd.put("var10",var10);
				vpd.put("var11",var11);
				vpd.put("var12",var12);

				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);

		/*	NmsExcelUtilForPerformanceRealtime excelUtil = new NmsExcelUtilForPerformanceRealtime(
					list, NmsPerformanceItem.class, "运行状态报表", "运行状态报表",
					"运行状态报表", namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			if(b){
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出实时性能报表", "监控管理", "导出文件", "成功");
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
