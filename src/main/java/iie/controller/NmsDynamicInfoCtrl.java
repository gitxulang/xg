package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsDynamicInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsDynamicInfoService;
import iie.tools.NmsDynamicInfoAndWorkingHours;
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
@RequestMapping("/DynamicInfo")
public class NmsDynamicInfoCtrl {

	@Autowired
	NmsDynamicInfoService dService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsDynamicInfo> getAll() {
		List<NmsDynamicInfo> res = dService.getAll();
		return res;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNd(HttpServletRequest request)
			throws Exception {

		Map<String, String> data = new HashMap<String, String>();

		long sys_uptime = Long.valueOf(request.getParameter("sys_uptime"));
		long sys_update_time = Long.valueOf(request.getParameter("sys_update_time"));
		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		NmsAsset na = dService.findById(asset_id);
		NmsDynamicInfo nd = new NmsDynamicInfo(na, sys_uptime, sys_update_time,
				new Timestamp(System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (dService.saveDynamic(nd)) {
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
	public PageBean<NmsDynamicInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsDynamicInfo> page = dService.getPageByDate(orderKey,
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
	
	@SuppressWarnings("rawtypes")
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

	//	System.out.println(orderKey + "," + startDate + endDate);

		List<NmsDynamicInfoAndWorkingHours> list;

		try {
			list = dService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("??????????????????");
			namesOfHeader.add("??????????????????");
			namesOfHeader.add("??????????????????");
			namesOfHeader.add("????????????");
			
			/* namesOfHeader.add("??????"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("sysUptime");
			namesOfField.add("workingHours");
			namesOfField.add("sysUpdateTime");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "??????????????????"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsDynamicInfoAndWorkingHours var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",DateUtil.date2Str(new Date(var.getSysUptime())));
				vpd.put("var2",var.getWorkingHours());

				String var3 = var.getSysUpdateTime()==0?"--":DateUtil.date2Str(new Date(1000*var.getSysUpdateTime()));
				
				vpd.put("var3",var3);
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var4",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			/*NmsExcelUtilForDynamicInfo excelUtil = new NmsExcelUtilForDynamicInfo(
					list, NmsDynamicInfoAndWorkingHours.class, "??????????????????"+asset.getAIp(), "??????????????????"+asset.getAIp(),
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
