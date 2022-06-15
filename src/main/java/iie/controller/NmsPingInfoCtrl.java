package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsPingInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsPingInfoService;
import iie.tools.NmsPingStaticsInfo;
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
import java.util.regex.Pattern;

@Controller
@RequestMapping("/PingInfo")
public class NmsPingInfoCtrl {
	@Autowired
	NmsPingInfoService pService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;
	
	 @RequestMapping(value="/ServerDetail/PerformanceInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	    @ResponseBody
	    public NmsPingStaticsInfo pingInfoServiceDetailPerformanceInfo(HttpServletRequest request) throws Exception{
	        String assetIdParam = request.getParameter("assetId");
	        String startDate = request.getParameter("startDate");
	        String endDate = request.getParameter("endDate");
	        Integer assetId = -1;
	        if(assetIdParam != null && assetIdParam.length()>0){
	            assetId = Integer.valueOf(assetIdParam);
	        }
	        return pService.pingInfoServiceDetailPerformanceInfo(assetId,startDate,endDate);

	    }
	 
	 /**
	  * 如果不存在当日数据，统计数据库中已经存在的距当日最近的三天数据
	  * @param request
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="/ServerDetail/PerformanceInfoV02",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	    @ResponseBody
	    public NmsPingStaticsInfo pingInfoServiceDetailPerformanceInfoV02(HttpServletRequest request, HttpSession session) throws Exception{
	        String assetIdParam = request.getParameter("assetId");
	        String startDate = request.getParameter("startDate");
	        String endDate = request.getParameter("endDate");
	        Integer assetId = -1;
	        if(assetIdParam != null && assetIdParam.length()>0){
	            assetId = Integer.valueOf(assetIdParam);
	        }

			// 增加日志
			if (assetId != null ) {
				NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
				if (nmsAsset != null) {
					String name = nmsAsset.getAName();
					String ip = nmsAsset.getAIp();
					
					auditLogService.add(
							((NmsAdmin) session.getAttribute("user")).getRealname(),
							request.getRemoteAddr(), "查看设备详情信息 | " + name + " | " + ip, "监控管理", "查询数据", "成功");	
				}
			}	
	        
	        return pService.pingInfoServiceDetailPerformanceInfoV02(assetId, startDate, endDate);
	    }

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsPingInfo> getAll() {
		List<NmsPingInfo> res = pService.getAll();
		return res;
	}
	
	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}
	
	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsPingInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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
    	if (!isNumber(assetId)) {
    		return null;
    	}
    	
		PageBean<NmsPingInfo> page = pService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：连通信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
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

		List<NmsPingInfo> list;

		try {
			list = pService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("网元连通率（%）");
			namesOfHeader.add("响应时间（μs）");
			namesOfHeader.add("采集时间");
			
			/* namesOfHeader.add("操作"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("pingRate");
			namesOfField.add("pingRtt");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "连通信息报表"+asset.getAIp();
			List<Map> varList = new ArrayList<Map>();//
			for (NmsPingInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				String var1 = String.format("%.2f", Double.valueOf((var.getPingRate()).toString()));
				vpd.put("var1",var1);
				vpd.put("var2",String.valueOf(var.getPingRtt()));
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var3",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			/*NmsExcelUtilForPingInfo excelUtil = new NmsExcelUtilForPingInfo(list,
					NmsPingInfo.class, "连通信息报表"+asset.getAIp(), "连通信息报表"+asset.getAIp(), "连通信息报表"+asset.getAIp(),
					namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备连通信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
