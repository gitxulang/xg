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
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsMysqlInfoService;
import iie.tools.NmsExcelUtilForMysqlInfo;
import iie.tools.PageBean;

import iie.tools.excel.DateUtil;
import iie.tools.excel.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/MysqlInfo")
public class NmsMysqlInfoCtrl {
	@Autowired
	NmsMysqlInfoService mysqlInfoService;

	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;	
	
	@RequestMapping(value = "/getMysqlById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsMysqlInfo getMysqlById(HttpServletRequest request) {
		Integer idInteger = Integer.valueOf(request.getParameter("assetId"));
		NmsMysqlInfo res = mysqlInfoService.getMysqlById(idInteger);
		if(res==null){
			return new NmsMysqlInfo();
		}
		else{
			return res;
		}
	}
	
	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsMysqlInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsMysqlInfo> page = mysqlInfoService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);


		// ????????????
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "???????????????????????????MySQL?????? | " + name + " | " + ip, "????????????", "????????????", "??????");	
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

		List<NmsMysqlInfo> list;

		try {
			list = mysqlInfoService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			
			namesOfHeader.add("??????");
			namesOfHeader.add("???????????????");
			namesOfHeader.add("???????????????");
			namesOfHeader.add("???????????????");
			namesOfHeader.add("????????????");
			namesOfHeader.add("QPS");
			namesOfHeader.add("TPS");
			namesOfHeader.add("???????????????");
			namesOfHeader.add("Questions");
			namesOfHeader.add("?????????");
			namesOfHeader.add("????????????");
			
			/* namesOfHeader.add("??????"); */
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("dbVersion");
			namesOfField.add("maxConnections");
			namesOfField.add("threadsConnected");
			namesOfField.add("threadsRunning");
			namesOfField.add("dbReadOnly");
			namesOfField.add("qps");
			namesOfField.add("tps");
			namesOfField.add("abortedClients");
			namesOfField.add("questions");
			namesOfField.add("processlist");
			namesOfField.add("itime");
			
			
			
			/* namesOfField.add("action"); */
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "MySQL????????????"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsMysqlInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getDbVersion());
				vpd.put("var2",String.valueOf(var.getMaxConnections()));
				vpd.put("var3",String.valueOf(var.getThreadsConnected()));
				vpd.put("var4",String.valueOf(var.getThreadsRunning()));
				vpd.put("var5",String.valueOf(var.getDbReadOnly()));
				vpd.put("var6",String.valueOf(var.getQps()));
				vpd.put("var7",String.valueOf(var.getTps()));
				vpd.put("var8",String.valueOf(var.getAbortedClients()));
				vpd.put("var9",String.valueOf(var.getQuestions()));
				vpd.put("var10",String.valueOf(var.getProcesslist()));
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
			/*NmsExcelUtilForMysqlInfo excelUtil = new NmsExcelUtilForMysqlInfo(list,
					NmsMysqlInfo.class, "MySQL????????????"+asset.getAIp(), "MySQL????????????"+asset.getAIp(), "MySQL????????????"+asset.getAIp(),
					namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){

				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "????????????MySQL???????????? | ???????????????"+asset.getAName()+" | ??????IP???"+asset.getAIp(), "????????????", "????????????", "??????");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
}