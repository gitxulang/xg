package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsFilesysInfo;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsFilesysInfoService;
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
@RequestMapping("/FilesysInfo")
public class NmsFilesysInfoCtrl {

	@Autowired
	NmsFilesysInfoService fService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsFilesysInfo> getAll() {
		List<NmsFilesysInfo> res = fService.getAll();
		return res;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> addNf(HttpServletRequest request)
			throws Exception {

		Map<String, String> data = new HashMap<String, String>();

		int freq = Integer.valueOf(request.getParameter("freq"));
		String file_sys = request.getParameter("file_sys");
		String file_type = request.getParameter("file_type");
		float part_total = Float.valueOf(request.getParameter("part_total"));
		float part_free = Float.valueOf(request.getParameter("part_free"));
		int part_inode_num = Integer.valueOf(request
				.getParameter("part_inode_num"));
		int part_inode_free = Integer.valueOf(request
				.getParameter("part_inode_free"));
		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
	//	System.out.println(file_sys);
		NmsAsset na = fService.findById(asset_id);
		NmsFilesysInfo nf = new NmsFilesysInfo(na, freq, file_sys, file_type,
				part_total, part_free, part_inode_num, part_inode_free,
				new Timestamp(System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (fService.saveFilesys(nf)) {
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
	public PageBean<NmsFilesysInfo> listPageByDate(HttpServletRequest request, HttpSession session)
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

		PageBean<NmsFilesysInfo> page = fService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset, assetId);

		// 增加日志
		if (assetId != null ) {
			NmsAsset nmsAsset = assetService.findById(Integer.valueOf(assetId));
			if (nmsAsset != null) {
				String name = nmsAsset.getAName();
				String ip = nmsAsset.getAIp();
				
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "查看单个设备报表：文件分区 | " + name + " | " + ip, "报表管理", "查询数据", "成功");	
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
	public List<NmsFilesysInfo> listPageByDateV02(HttpServletRequest request)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		int orderValue = Integer.valueOf(request.getParameter("orderValue"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		int begin = Integer.valueOf(request.getParameter("begin"));
		int offset = Integer.valueOf(request.getParameter("offset"));

		PageBean<NmsFilesysInfo> page = fService.getPageByDateV02(orderKey,
				orderValue, startDate, endDate, begin, offset);

		return page.getList();
	}


	@RequestMapping(value="/ServerDetail/DiskUtilization",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object filesysInfoServerDetailDiskUtilization(HttpServletRequest request){
		String assetIdParam = request.getParameter("assetId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer assetId = -1;
		if(assetIdParam != null && assetIdParam.length()>0){
			assetId = Integer.valueOf(assetIdParam);
		}
		return fService.filesysInfoServerDetailDiskUtilization(assetId,startDate,endDate);

	}
	
	@RequestMapping(value="/ServerDetail/DiskUtilizationV02",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object filesysInfoServerDetailDiskUtilizationV02(HttpServletRequest request){
		String assetIdParam = request.getParameter("assetId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer assetId = -1;
		if(assetIdParam != null && assetIdParam.length()>0){
			assetId = Integer.valueOf(assetIdParam);
		}
		return fService.filesysInfoServerDetailDiskUtilizationV02(assetId,startDate,endDate);
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

	//	System.out.println(orderKey + "," + startDate + endDate);

		List<NmsFilesysInfo> list;

		try {
			list = fService.getPageByDateExportExcel(orderKey,
					orderValue, startDate, endDate, assetId);
			List<String> namesOfHeader = new ArrayList<>();
			namesOfHeader.add("文件系统名称");
			/*namesOfHeader.add("分区类型");*/
			namesOfHeader.add("总容量（G）");
			namesOfHeader.add("剩余容量（G）");
			namesOfHeader.add("总iNode（个）");
			namesOfHeader.add("剩余iNode（个）");
			namesOfHeader.add("采集时间");

			List<String> namesOfField = new ArrayList<>();
			namesOfField.add("fileSys");
			/*namesOfField.add("fileType");*/
			namesOfField.add("partTotal");
			namesOfField.add("partFree");
			namesOfField.add("partInodeNum");
			namesOfField.add("partInodeFree");
			namesOfField.add("itime");
			
			NmsAsset asset = assetService.findById(Integer.valueOf(assetId));
			String docName = "文件分区报表"+asset.getAIp();

			List<Map> varList = new ArrayList<Map>();//
			for (NmsFilesysInfo var:list) {
				Map<String, String> vpd = new HashMap<>();
				vpd.put("var1",var.getFileSys());
				vpd.put("var2",String.valueOf(var.getPartTotal()));
				String var3 = String.format("%.2f", Double.valueOf((var.getPartFree()).toString()));
				vpd.put("var3",var3);
				vpd.put("var4",String.valueOf(var.getPartInodeNum()));
				vpd.put("var5",String.valueOf(var.getPartInodeFree()));
				Timestamp itime = var.getItime();
				String iTimeStr="--" ;
				if (null!= itime){
					iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
				}
				vpd.put("var6",iTimeStr);
				varList.add(vpd);
			}
			ExcelHelper excelHelper = new ExcelHelper(docName,namesOfHeader,varList);
			boolean b = excelHelper.buildExcelDocument().exportZip(request,response);
			/*NmsExcelUtilForFilesysInfo excelUtil = new NmsExcelUtilForFilesysInfo(
					list, NmsFilesysInfo.class, "文件分区报表"+asset.getAIp(), "文件分区报表"+asset.getAIp(),
					"文件分区报表"+asset.getAIp(), namesOfHeader, namesOfField, response);
			boolean b = excelUtil.exportExcel();*/
			
			if(b){
				auditLogService.add(
						((NmsAdmin) session.getAttribute("user")).getRealname(),
						request.getRemoteAddr(), "导出设备文件分区报表 | 设备名称："+asset.getAName()+" | 设备IP："+asset.getAIp(), "监控管理", "导出文件", "成功");
			}
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
