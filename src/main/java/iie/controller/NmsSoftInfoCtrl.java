package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsYthSoft;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsSoftInfoService;
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
@RequestMapping("/SoftInfo")
public class NmsSoftInfoCtrl {

	@Autowired
	NmsSoftInfoService pService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsYthSoft> getAll() {
		List<NmsYthSoft> res = pService.getAll();
		return res;
	}

	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsYthSoft> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsYthSoft> page = pService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：软件信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
			}
		}		
		
		return page;
	}
	
	@RequestMapping(value = "/ServerDetail/SoftInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsYthSoft> listPageByAssetId(HttpServletRequest request, HttpSession session)
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
		
		String soft_name = request.getParameter("soft_name");
		String product_type = request.getParameter("product_type");
		String unique_ident = request.getParameter("unique_ident");

		String assetId = request.getParameter("assetId");
		PageBean<NmsYthSoft> page = pService.getPageByAssetId(orderKey, orderValue, begin, offset, assetId, soft_name, product_type, unique_ident);
		
		//System.out.println("1111111111 assetId = " + assetId + ", strBegin = " + strBegin + ", stroffset = " + stroffset);
		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看设备详情：软件信息 | " + name + " | " + ip, "监控管理", "查询数据", "成功");	
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

		List<NmsYthSoft> list;
		try {
			list = pService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("软件名称");
			namesOfHeader.add("版本");
			namesOfHeader.add("架构");
			namesOfHeader.add("类别");
			namesOfHeader.add("sm3校验值");
			namesOfHeader.add("任务ID");
			namesOfHeader.add("描述信息");
			namesOfHeader.add("更新时间");
			namesOfHeader.add("唯一序列号");
			namesOfHeader.add("平台类型");
			namesOfHeader.add("采集时间");
			
			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("softName");
			namesOfField.add("softVersion");
			namesOfField.add("architecture");
			namesOfField.add("productType");
			namesOfField.add("sm3");
			namesOfField.add("jobId");
			namesOfField.add("decInfo");
			namesOfField.add("updateTime");
			namesOfField.add("uniqueIdent");
			namesOfField.add("platformType");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "软件信息报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();
			for (NmsYthSoft var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",String.valueOf(var.getSoftName()));
				vpd.put("var2",String.valueOf(var.getSoftVersion()));
				vpd.put("var3",String.valueOf(var.getArchitecture()));
				vpd.put("var4",String.valueOf(var.getProductType()));
				vpd.put("var5",String.valueOf(var.getSm3()));
				vpd.put("var6",String.valueOf(var.getJobId()));
				vpd.put("var7",String.valueOf(var.getDecInfo()));
				vpd.put("var8",String.valueOf(var.getUpdateTime()));
				vpd.put("var9",String.valueOf(var.getUniqueIdent()));
				vpd.put("var10",String.valueOf(var.getPlatformType()));
				
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
					request.getRemoteAddr(), "导出设备软件信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
