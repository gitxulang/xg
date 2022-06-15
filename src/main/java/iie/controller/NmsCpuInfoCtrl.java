package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsCpuInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsCpuInfoService;
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
@RequestMapping("/CpuInfo")
public class NmsCpuInfoCtrl {
	@Autowired
	NmsCpuInfoService cService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsCpuInfo> getAll() {
		List<NmsCpuInfo> res = cService.getAll();
		return res;
	}

	@RequestMapping(value = "/add/{freq}/{cpuName}/{cpuCores}/{cpuFreq}/{cpuRate}/{asset_id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addNc(HttpServletRequest request)
			throws Exception {
		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		int freq = Integer.valueOf(request.getParameter("freq"));
		String cpuName = request.getParameter("cpuName");
		String cpuCores = request.getParameter("cpuCores");
		String cpuFreq = request.getParameter("cpuFreq");
		float cpuRate = Float.valueOf(request.getParameter("cpuRate"));

		NmsAsset na = cService.findById(asset_id);
		NmsCpuInfo nc = new NmsCpuInfo(na, freq, cpuName, cpuCores, cpuFreq,
				cpuRate, new Timestamp(System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (cService.saveCpu(nc)) {
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
	public PageBean<NmsCpuInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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
		PageBean<NmsCpuInfo> page = cService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：CPU信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");		
			}
		}

		return page;
	}

	@RequestMapping(value="/ServerDetail/PerformanceInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object cpuInfoServiceDetailPerformanceInfo(HttpServletRequest request) throws Exception{
		String assetIdParam = request.getParameter("assetId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer assetId = -1;
		if(assetIdParam != null && assetIdParam.length()>0){
			assetId = Integer.valueOf(assetIdParam);
		}
		return cService.cpuInfoServiceDetailPerformanceInfo(assetId,startDate,endDate);

	}
	
	@RequestMapping(value="/ServerDetail/PerformanceInfoV02",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object cpuInfoServiceDetailPerformanceInfoV02(HttpServletRequest request) throws Exception{
		String assetIdParam = request.getParameter("assetId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer assetId = -1;
		if(assetIdParam != null && assetIdParam.length()>0){
			assetId = Integer.valueOf(assetIdParam);
		}
		
		return cService.cpuInfoServiceDetailPerformanceInfoV02(assetId,startDate,endDate);
	}
	
	@RequestMapping(value = "/list/date/ExportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean listPageByDateExportExcel(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws UnsupportedEncodingException {

		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");
		
		if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
			orderKey = "itime";
			orderValue = "0";
		}		

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String assetId = request.getParameter("assetId");


		List<NmsCpuInfo> list;

		try {
			list = cService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("逻辑核描述");
			namesOfHeader.add("逻辑核索引");
			namesOfHeader.add("逻辑核频率（Hz）");
			namesOfHeader.add("逻辑核使用率（%）");
			namesOfHeader.add("采集时间");
			
			/* namesOfHeader.add("操作"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("cpuName");
			namesOfField.add("cpuCores");
			namesOfField.add("cpuFreq");
			namesOfField.add("cpuRate");
			namesOfField.add("itime");

			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "CPU信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsCpuInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getCpuName());
				vpd.put("var2",var.getCpuCores());
				vpd.put("var3",var.getCpuFreq()+"Hz");
				vpd.put("var4",String.format("%.2f", var.getCpuRate())+"%");
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var5",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);

			/*NmsExcelUtilForCpuInfo excelUtil = new NmsExcelUtilForCpuInfo(
					list, NmsCpuInfo.class, "CPU信息报表"+asset.getAIp(), "CPU信息报表"+asset.getAIp(),
					"CPU信息报表"+asset.getAIp(), namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备CPU信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
