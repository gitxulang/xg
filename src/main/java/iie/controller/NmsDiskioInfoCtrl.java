package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsDiskioInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsDiskioInfoService;
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
@RequestMapping("/DiskioInfo")
public class NmsDiskioInfoCtrl {

	@Autowired
	NmsDiskioInfoService dService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsDiskioInfo> getAll() {
		List<NmsDiskioInfo> res = dService.getAll();
		return res;
	}

	@RequestMapping(value = "/add/{freq}/{diskName}/{diskSn}/{readNum}/{writeNum}/{kbRead}/{kbWrtn}/{asset_id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addNb(HttpServletRequest request)
			throws Exception {
		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		int freq = Integer.valueOf(request.getParameter("freq"));
		String diskName = request.getParameter("diskName");
		String diskSn = request.getParameter("diskSn");
		float readNum = Float.valueOf(request.getParameter("readNum"));
		float writeNum = Float.valueOf(request.getParameter("writeNum"));
		float kbRead = Float.valueOf(request.getParameter("kbRead"));
		float kbWrtn = Float.valueOf(request.getParameter("kbWrtn"));

		NmsAsset na = dService.findById(asset_id);
		NmsDiskioInfo nd = new NmsDiskioInfo(na, freq, diskName, diskSn,
				readNum, writeNum, kbRead, kbWrtn, new Timestamp(
						System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (dService.saveDiskio(nd)) {
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
	public PageBean<NmsDiskioInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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
		PageBean<NmsDiskioInfo> page = dService.getPageByDate(orderKey,
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

		List<NmsDiskioInfo> list;

		try {
			list = dService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("磁盘名称");
			namesOfHeader.add("序列号");
			namesOfHeader.add("读速率（次/秒）");
			namesOfHeader.add("写速率（次/秒）");
			namesOfHeader.add("读字节速率（kB/秒）");
			namesOfHeader.add("写字节速率（kB/秒）");
			namesOfHeader.add("采集时间");
			/* namesOfHeader.add("操作"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("diskName");
			namesOfField.add("diskSn");
			namesOfField.add("readNum");
			namesOfField.add("writeNum");
			namesOfField.add("kbRead");
			namesOfField.add("kbWrtn");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "磁盘信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsDiskioInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getDiskName());
				vpd.put("var2",var.getDiskSn());
				String var3 = String.format("%.2f", Double.valueOf((var.getReadNum()).toString()));
				vpd.put("var3",var3);
				String var4 = String.format("%.2f", Double.valueOf((var.getWriteNum()).toString()));
				vpd.put("var4",var4);
				String var5 = String.format("%.2f", Double.valueOf((var.getKbRead()).toString()));
				vpd.put("var5",var5);
				String var6 = String.format("%.2f", Double.valueOf((var.getKbWrtn()).toString()));
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
			/*NmsExcelUtilForDiskioInfo excelUtil = new NmsExcelUtilForDiskioInfo(
					list, NmsDiskioInfo.class, "磁盘信息报表"+asset.getAIp(), "磁盘信息报表"+asset.getAIp(),
					"磁盘信息报表"+asset.getAIp(), namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备磁盘信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
