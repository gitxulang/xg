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
				data.put("info", "????????????!");
			} else {
				data.put("state", "1");
				data.put("info", "????????????!");
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

		// ????????????
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "??????????????????????????????????????? | " + name + " | " + ip, "????????????", "????????????", "??????");	
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
			namesOfHeader.add("????????????");
			namesOfHeader.add("?????????");
			namesOfHeader.add("???????????????/??????");
			namesOfHeader.add("???????????????/??????");
			namesOfHeader.add("??????????????????kB/??????");
			namesOfHeader.add("??????????????????kB/??????");
			namesOfHeader.add("????????????");
			/* namesOfHeader.add("??????"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("diskName");
			namesOfField.add("diskSn");
			namesOfField.add("readNum");
			namesOfField.add("writeNum");
			namesOfField.add("kbRead");
			namesOfField.add("kbWrtn");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "??????????????????"+asset.getAIp();

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
					list, NmsDiskioInfo.class, "??????????????????"+asset.getAIp(), "??????????????????"+asset.getAIp(),
					"??????????????????"+asset.getAIp(), namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "?????????????????????????????? | ???????????????"+asset.getAName()+" | ??????IP???"+asset.getAIp(), "????????????", "????????????", "??????");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
