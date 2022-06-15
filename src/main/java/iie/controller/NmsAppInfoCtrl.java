package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsYthApp;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsAppInfoService;
import iie.tools.PageBean;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import iie.tools.excel.DateUtil;
import iie.tools.excel.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/AppInfo")
public class NmsAppInfoCtrl {

	@Autowired
	NmsAppInfoService pService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsYthApp> getAll() {
		List<NmsYthApp> res = pService.getAll();
		return res;
	}

	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsYthApp> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsYthApp> page = pService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：应用系统信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
			}
		}		
		
		return page;
	}
	
	@RequestMapping(value = "/ServerDetail/AppInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsYthApp> listPageByAssetId(HttpServletRequest request, HttpSession session)
			throws Exception {
		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");
		if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
			orderKey = "itime";
			orderValue = "1";
		}
		
		String strBegin = request.getParameter("begin");
		String stroffset = request.getParameter("offset");
		int begin = 1;
		int offset = 10;
		if (strBegin != null) {
			begin = Integer.valueOf(strBegin);
		}
		if (stroffset != null) {
			offset = Integer.valueOf(stroffset);
		}
		
		String appName = request.getParameter("appName");
		String appPort = request.getParameter("appPort");
		String assetId = request.getParameter("assetId");
		PageBean<NmsYthApp> page = pService.getPageByAssetId(orderKey, orderValue, begin, offset, assetId, appName, appPort);
		
		System.out.println("2222222222 assetId = " + assetId + ", strBegin = " + strBegin + ", stroffset = " + stroffset);
		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看设备详情：应用系统信息 | " + name + " | " + ip, "监控管理", "查询数据", "成功");	
			}
		}

		return page;
	}
	
	@SuppressWarnings("rawtypes")
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

		List<NmsYthApp> list;
		try {
			list = pService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("应用系统名称");
			namesOfHeader.add("端口号");
			namesOfHeader.add("采集时间");
			
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("appName");
			namesOfField.add("appPort");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "应用系统信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();
			for (NmsYthApp var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",String.valueOf(var.getAppName()));
				vpd.put("var2",String.valueOf(var.getAppPort()));
				
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var11",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			if (b) {
				auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "导出应用系统信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
