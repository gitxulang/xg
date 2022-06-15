package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsFilesysInfo;
import iie.pojo.NmsMemInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsMemInfoService;
import iie.tools.NmsExcelUtilForFilesysInfo;
import iie.tools.NmsExcelUtilForMemInfo;
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
@RequestMapping("/MemInfo")
public class NmsMemInfoCtrl {

	@Autowired
	NmsMemInfoService mService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsMemInfo> getAll() {
		List<NmsMemInfo> res = mService.getAll();
		return res;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNm(HttpServletRequest request)
			throws Exception {
		
	//	System.out.println("begin");

		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		long mem_total = Long.valueOf(request.getParameter("mem_total"));
		long mem_free = Long.valueOf(request.getParameter("mem_free"));
		long mem_available = Long
				.valueOf(request.getParameter("mem_available"));
		long buffers = Long.valueOf(request.getParameter("buffers"));
		long cached = Long.valueOf(request.getParameter("cached"));
		long swap_total = Long.valueOf(request.getParameter("swap_total"));
		long swap_free = Long.valueOf(request.getParameter("swap_free"));
		long swap_cached = Long.valueOf(request.getParameter("swap_cached"));
		NmsAsset na = mService.findById(asset_id);
		NmsMemInfo nm = new NmsMemInfo(na, mem_total, mem_free, mem_available,
				buffers, cached, swap_total, swap_free, swap_cached,
				new Timestamp(System.currentTimeMillis()));
		
	//	System.out.println(mem_free);

		for (int i = 0; i < 100; i++) {
			if (mService.saveMem(nm)) {
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
	public PageBean<NmsMemInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsMemInfo> page = mService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：内存信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
			}
		}
		
		return page;
	}
	
	/**
	 * 不推荐使用此接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list/date/v02", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsMemInfo> listPageByDateV02(HttpServletRequest request)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		int orderValue = Integer.valueOf(request.getParameter("orderValue"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		int begin = Integer.valueOf(request.getParameter("begin"));
		int offset = Integer.valueOf(request.getParameter("offset"));

		PageBean<NmsMemInfo> page = mService.getPageByDateV02(orderKey,
				orderValue, startDate, endDate, begin, offset);

		return page.getList();
	}


	@RequestMapping(value="/ServerDetail/MemInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object memInfoServiceDetailMemInfo(HttpServletRequest request) throws Exception{
		String assetIdParam = request.getParameter("assetId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer assetId = -1;
		if(assetIdParam != null && assetIdParam.length()>0){
			assetId = Integer.valueOf(assetIdParam);
		}
		return mService.memInfoServiceDetailMemInfo(assetId,startDate,endDate);

	}
	
	@RequestMapping(value="/ServerDetail/MemInfoV02",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object memInfoServiceDetailMemInfoV02(HttpServletRequest request) throws Exception{
		String assetIdParam = request.getParameter("assetId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer assetId = -1;
		if(assetIdParam != null && assetIdParam.length()>0){
			assetId = Integer.valueOf(assetIdParam);
		}
		return mService.memInfoServiceDetailMemInfoV02(assetId,startDate,endDate);

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


		List<NmsMemInfo> list;

		try {
			list = mService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("物理内存（G）");
			namesOfHeader.add("有效内存（G）");
		//	namesOfHeader.add("剩余应用程序内存(KB)");
			namesOfHeader.add("缓冲容量（G）");
			namesOfHeader.add("缓存容量（G）");
			namesOfHeader.add("交换内存（G）");
			namesOfHeader.add("有效内存（G）");
		/*	namesOfHeader.add("交换缓存（G）");*/
			namesOfHeader.add("采集时间");
			
			/* namesOfHeader.add("操作"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("memTotal");
			namesOfField.add("memFree");
		//	namesOfField.add("memAvailable");
			namesOfField.add("buffers");
			namesOfField.add("cached");
			namesOfField.add("swapTotal");
			namesOfField.add("swapFree");
	/*		namesOfField.add("swapCached");*/
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "内存信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsMemInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				double value = var.getMemTotal()*1.0;
				value = value / 1024 / 1024;
				String var1 = String.format("%.2f", value);
				vpd.put("var1",var1);
				String var2 = String.format("%.2f", (double)var.getMemFree()*1.0 /1024/1024);
				vpd.put("var2",var2);
				String var3 = String.format("%.2f", (double)var.getBuffers()*1.0 /1024/1024);
				vpd.put("var3",var3);
				String var4 = String.format("%.2f", (double)var.getCached()*1.0 /1024/1024);
				vpd.put("var4",var4);
				String var5 = String.format("%.2f", (double)var.getSwapTotal()*1.0 /1024/1024);
				vpd.put("var5",var5);
				String var6 = String.format("%.2f", (double)var.getSwapFree()*1.0 /1024/1024);
				vpd.put("var6",var6);
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var7",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			/*NmsExcelUtilForMemInfo excelUtil = new NmsExcelUtilForMemInfo(
					list, NmsMemInfo.class, "内存信息报表"+asset.getAIp(), "内存信息报表"+asset.getAIp(),
					"内存信息报表"+asset.getAIp(), namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备内存信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
