package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsProcessInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsProcessInfoService;
import iie.tools.NmsExcelUtilForProcessInfo;
import iie.tools.PageBean;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
@RequestMapping("/ProcessInfo")
public class NmsProcessInfoCtrl {

	@Autowired
	NmsProcessInfoService pService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsProcessInfo> getAll() {
		List<NmsProcessInfo> res = pService.getAll();
		return res;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNp(HttpServletRequest request)
			throws Exception {

	//	System.out.println("begin");

		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		long freq = Long.valueOf(request.getParameter("freq"));
		long proc_id = Long.valueOf(request.getParameter("proc_id"));
		String proc_name = request.getParameter("proc_name");
		String proc_path = request.getParameter("proc_path");
		int proc_state = Integer.valueOf(request.getParameter("proc_state"));
		float proc_cpu = Float.valueOf(request.getParameter("proc_cpu"));
		float proc_mem = Float.valueOf(request.getParameter("proc_mem"));

		NmsAsset na = pService.findById(asset_id);
		NmsProcessInfo np = new NmsProcessInfo(na, freq, proc_id, proc_name,
				proc_path, proc_state, proc_cpu, proc_mem, new Timestamp(
						System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (pService.saveProcess(np)) {
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
	public PageBean<NmsProcessInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsProcessInfo> page = pService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：进程信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
			}
		}		
		
		return page;
	}
	
	@RequestMapping(value = "/ServerDetail/ProcessInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsProcessInfo> listPageByAssetId(HttpServletRequest request, HttpSession session)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");
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

		String proc_name = request.getParameter("proc_name");
		String proc_id = request.getParameter("proc_id");
		String proc_path = request.getParameter("proc_path");

		String assetId = request.getParameter("assetId");
		PageBean<NmsProcessInfo> page = pService.getPageByAssetId(orderKey,
				orderValue, begin, offset, assetId, proc_name, proc_id, proc_path);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看设备详情：进程信息 | " + name + " | " + ip, "监控管理", "查询数据", "成功");	
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

		List<NmsProcessInfo> list;

		try {
			list = pService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("进程ID");
			namesOfHeader.add("名称");
			namesOfHeader.add("路径");
			namesOfHeader.add("状态");
			namesOfHeader.add("CPU使用（%）");
			namesOfHeader.add("内存使用（%）");
			namesOfHeader.add("采集时间");
			/* namesOfHeader.add("操作"); */
			
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("procId");
			namesOfField.add("procName");
			namesOfField.add("procPath");
			namesOfField.add("procState");
			namesOfField.add("procCpu");
			namesOfField.add("procMem");
			namesOfField.add("itime");
			/* namesOfField.add("action"); */
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "进程信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsProcessInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",String.valueOf(var.getProcId()));
				vpd.put("var2",var.getProcName());
				vpd.put("var3",var.getProcPath());
				int state = var.getProcState();
				String var4="--";
				if (state == 0) {
					var4 = "running";
				} else if (state == 1) {
					var4 = "stopped";
				} else if (state == 2) {
					var4 = "sleeping";
				} else if (state == 3) {
					var4 = "zombie";
				} else {
					var4 = "未知";
				}
				vpd.put("var4",var4);
				String var5 = new BigDecimal(Double.valueOf(var.getProcCpu())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();;
				vpd.put("var5",var5);
				String var6 = new BigDecimal(Double.valueOf(var.getProcMem())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();;
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
			/*NmsExcelUtilForProcessInfo excelUtil = new NmsExcelUtilForProcessInfo(list,
					NmsProcessInfo.class, "进程信息报表"+asset.getAIp(), "进程信息报表"+asset.getAIp(), "进程信息报表"+asset.getAIp(),
					namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备进程信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
