package iie.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsMysqlInfo;
import iie.pojo.NmsTomcatInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsTomcatInfoService;
import iie.tools.NmsExcelUtilForMysqlInfo;
import iie.tools.NmsExcelUtilForTomcatInfo;
import iie.tools.PageBean;

import iie.tools.excel.DateUtil;
import iie.tools.excel.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/TomcatInfo")
public class NmsTomcatInfoCtrl {
	@Autowired
	NmsTomcatInfoService tomcatInfoService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;	
	
	@RequestMapping(value = "/getTomcatById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsTomcatInfo getTomcatById(HttpServletRequest request) {
		Integer idInteger = Integer.valueOf(request.getParameter("assetId"));
		NmsTomcatInfo res = tomcatInfoService.getTomcatById(idInteger);
		if(res==null){
			return new NmsTomcatInfo();
		}
		else{
			return res;
		}
	}
	
	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsTomcatInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsTomcatInfo> page = tomcatInfoService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：Tomcat信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
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

		List<NmsTomcatInfo> list;

		try {
			list = tomcatInfoService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			
			namesOfHeader.add("名称");
			namesOfHeader.add("版本");
			namesOfHeader.add("厂商");
			namesOfHeader.add("启动时间");
			namesOfHeader.add("最大堆");
			namesOfHeader.add("提交堆");
			namesOfHeader.add("已用堆");
			namesOfHeader.add("提交非堆");
			namesOfHeader.add("已用非堆");
			namesOfHeader.add("线程数");
			namesOfHeader.add("加载类数");
			namesOfHeader.add("采集时间");
			
			/* namesOfHeader.add("操作"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("vmName");
			namesOfField.add("vmVersion");
			namesOfField.add("vmVendor");
			namesOfField.add("startTime");
			namesOfField.add("maxHeapMemory");
			namesOfField.add("commitHeapMemory");
			namesOfField.add("usedHeapMemory");
			namesOfField.add("commitNonHeapMemory");
			namesOfField.add("usedNonHeapMemory");
			namesOfField.add("threadCount");
			namesOfField.add("loadedClassCount");
			namesOfField.add("itime");
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "Tomcat信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsTomcatInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getVmName());
				vpd.put("var2",var.getVmVersion());
				vpd.put("var3",var.getVmVendor());
				vpd.put("var4",DateUtil.date2Str(DateUtil.fomatDate(var.getStartTime())));
				vpd.put("var5",String.valueOf(var.getMaxHeapMemory()));
				vpd.put("var6",String.valueOf(var.getCommitHeapMemory()));
				vpd.put("var7",String.valueOf(var.getUsedHeapMemory()));
				vpd.put("var8",String.valueOf(var.getCommitNonHeapMemory()));
				vpd.put("var9",String.valueOf(var.getUsedNonHeapMemory()));
				vpd.put("var10",String.valueOf(var.getThreadCount()));
				vpd.put("var11",String.valueOf(var.getLoadedClassCount()));
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var12",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			if(b){
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备MySQL信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
}