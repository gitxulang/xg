package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsStaticInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsStaticInfoService;
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
@RequestMapping("/StaticInfo")
public class NmsStaticInfoCtrl {

	@Autowired
	NmsStaticInfoService sService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsStaticInfo> getAll() {
		List<NmsStaticInfo> res = sService.getAll();
		return res;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNp(HttpServletRequest request)
			throws Exception {

	//	System.out.println("begin");

		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		String unique_ident = request.getParameter("unique_ident");
		String product_name = request.getParameter("product_name");
		String manufacturer = request.getParameter("manufacturer");
		String cpu_info = request.getParameter("cpu_info");
		String disk_sn = request.getParameter("disk_sn");
		String sys_name = request.getParameter("sys_name");
		String sys_arch = request.getParameter("sys_arch");
		int sys_bits = Integer.valueOf(request.getParameter("sys_bits"));
		String sys_version = request.getParameter("sys_version");
		String core_version = request.getParameter("core_version");
		int net_num = Integer.valueOf(request.getParameter("net_num"));
		int cpu_num = Integer.valueOf(request.getParameter("cpu_num"));
		String soc_version = request.getParameter("soc_version");
		String io_version = request.getParameter("io_version");

		NmsAsset na = sService.findById(asset_id);
		NmsStaticInfo np = new NmsStaticInfo(na, unique_ident, product_name,
				manufacturer, cpu_info, disk_sn, sys_name, sys_arch, sys_bits,
				sys_version, core_version, net_num, cpu_num, soc_version,
				io_version, new Timestamp(System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (sService.saveStatic(np)) {
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
	public PageBean<NmsStaticInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsStaticInfo> page = sService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：静态信息 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
			}
		}		

		return page;
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsStaticInfo getNetifInfoDetail(HttpServletRequest request) throws Exception {
		if (request.getParameter("id") == null || request.getParameter("id").equals("")) {
			return new NmsStaticInfo();	
		}
		int id = Integer.valueOf(request.getParameter("id"));
		NmsStaticInfo detail = sService.getStaticInfoDetail(id);
		if (detail == null) {
			detail = new NmsStaticInfo();
		}
		return detail;
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

		List<NmsStaticInfo> list;

		try {
			list = sService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, assetId);
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			if (asset == null) {
				return false;
			}
			
			List<String> namesOfHeader = new ArrayList<>();
			List<String> namesOfField = new ArrayList<>();
			if (asset.getNmsAssetType().getChType().equals("专用数通设备")) {
				namesOfHeader.add("ASPID");
				namesOfHeader.add("设备序列号");
				namesOfHeader.add("设备型号");
				namesOfHeader.add("系统描述");
				namesOfHeader.add("设备厂商对象标识OID");
				namesOfHeader.add("系统名称");
				namesOfHeader.add("设备位置");
				namesOfHeader.add("软件版本号");
				namesOfHeader.add("固件版本号");
				namesOfHeader.add("设备联系人");
				namesOfHeader.add("硬件版本号");
				namesOfHeader.add("采集时间");
				
				namesOfField.add("uniqueIdent");
				namesOfField.add("productName");
				namesOfField.add("manufacturer");
				namesOfField.add("cpuInfo");
				namesOfField.add("diskSn");
				namesOfField.add("sysName");
				namesOfField.add("sysArch");
				namesOfField.add("sysVersion");
				namesOfField.add("coreVersion");
				namesOfField.add("socVersion");
				namesOfField.add("ioVersion");
				namesOfField.add("itime");
			} else {
				namesOfHeader.add("ASPID");
				namesOfHeader.add("设备名称");
				namesOfHeader.add("操作系统名称");
				namesOfHeader.add("制造商");
				namesOfHeader.add("CPU信息");
				namesOfHeader.add("硬盘序列号");
				namesOfHeader.add("操作系统处理器架构");
				namesOfHeader.add("操作系统位数");
				namesOfHeader.add("操作系统版本");
				namesOfHeader.add("操作系统内核版本");
				namesOfHeader.add("物理网口数");
				namesOfHeader.add("CPU数");
				namesOfHeader.add("ASP卡版本");
				namesOfHeader.add("三合一模块版本");
				namesOfHeader.add("采集时间");
				
				namesOfField.add("uniqueIdent");
				namesOfField.add("productName");
				namesOfField.add("sysName");
				namesOfField.add("manufacturer");
				namesOfField.add("cpuInfo");
				namesOfField.add("diskSn");
				namesOfField.add("sysArch");
				namesOfField.add("sysBits");
				namesOfField.add("sysVersion");
				namesOfField.add("coreVersion");
				namesOfField.add("netNum");
				namesOfField.add("cpuNum");
				namesOfField.add("socVersion");
				namesOfField.add("ioVersion");
				namesOfField.add("itime");
			}
			
			String docName = "静态信息报表" + asset.getAIp();
			List<Map> varList = new ArrayList<Map>();
			for (NmsStaticInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				if (asset.getNmsAssetType().getChType().equals("专用数通设备")) {
					vpd.put("var1", var.getUniqueIdent());
					vpd.put("var2", var.getProductName());
					vpd.put("var3", var.getManufacturer());
					vpd.put("var4", var.getCpuInfo());
					vpd.put("var5", var.getDiskSn());
					vpd.put("var6", var.getSysName());
					vpd.put("var7", var.getSysArch());
					vpd.put("var8", var.getSysVersion());
					vpd.put("var9", var.getCoreVersion());
					vpd.put("var10", String.valueOf(var.getSocVersion()));
					vpd.put("var11", String.valueOf(var.getIoVersion()));
					Timestamp itime = var.getItime();
					String iTimeStr = "--" ;
					if (null != itime) {
						iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
					}
					vpd.put("var12", iTimeStr);
				} else {
					vpd.put("var1", var.getUniqueIdent());
					vpd.put("var2", var.getProductName());
					vpd.put("var3", var.getSysName());
					vpd.put("var4", var.getManufacturer());
					vpd.put("var5", var.getCpuInfo());
					vpd.put("var6", var.getDiskSn());
					vpd.put("var7", var.getSysArch());
					vpd.put("var8", String.valueOf(var.getSysBits()));
					vpd.put("var9", var.getSysVersion());
					vpd.put("var10", var.getCoreVersion());
					vpd.put("var11", var.getNetNum()==0||null==var.getNetNum() ? "--" : String.valueOf(var.getNetNum()));
					vpd.put("var12", String.valueOf(var.getCpuNum()));
					vpd.put("var13", String.valueOf(var.getSocVersion()));
					vpd.put("var14", String.valueOf(var.getIoVersion()));
					Timestamp itime = var.getItime();
					String iTimeStr = "--" ;
					if (null != itime) {
						iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
					}
					vpd.put("var15",iTimeStr);
				}

				varList.add(vpd);
			}

			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);			
			if (b) {
				auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备静态信息报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
