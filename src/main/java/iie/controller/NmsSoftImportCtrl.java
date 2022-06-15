package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsLicense;
import iie.service.NmsAuditLogService;
import iie.service.NmsLicenseService;
import iie.service.NmsSoftService;
import iie.service.SoftImportService;
import iie.tools.DesUtil;
import iie.tools.excel.ObjectExcelRead;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/softimport")
public class NmsSoftImportCtrl {
	@Autowired
	SoftImportService softImportService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@Autowired
	NmsLicenseService licenseService;
	
	@Autowired
	NmsSoftService nmsSoftService;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/input",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile importFile, HttpSession session){
		Map<String, String> data = new HashMap<String, String>();
		String filePath = request.getServletContext().getRealPath("temp") + File.separator + System.currentTimeMillis() + "upload.xls";
		File target = new File(filePath);
		String msg = "";
		try {
			FileUtils.copyInputStreamToFile(importFile.getInputStream(), target);
			List<Object> objects = ObjectExcelRead.readExcel(target, 1, 1, 0);
			if (objects.size() > 5000){
				data.put("state", "1");
				data.put("info", "行数不能大于5000行！");
				return data;
			}
			
			// 判断当前行数是否超过授权客户端数开始
		  	NmsLicense licenseObj = licenseService.getLastLicense();
			if (licenseObj == null) {
				data.put("state", "1");
				data.put("info", "系统未激活，不能添加设备！");
				return data;
			}
			
			DesUtil des = new DesUtil();
			String ret = null;
	        try {
	        	ret = des.decrypt(licenseObj.getLicense());
	        } catch(Exception e) {
				data.put("state", "1");
				data.put("info", "证书序列号解析失败（非法证书序列号）！");
				return data;
	        }

	        String[] arr = ret.split("#");
	        if (arr.length != 5) {
				data.put("state", "1");
				data.put("info", "证书序列号解析失败（序列号参数数量不正确）！");
				return data;
	        }

	        // 判断参数computer是否合法
	        if (arr[2] == null || arr[2].equals("")) {
				data.put("state", "1");
				data.put("info", "证书序列号解析失败（非法限制终端数参数）！");
				return data;
	        }
	        int licenseComputer = Integer.valueOf(arr[2]);
	        int systemComputer = nmsSoftService.findAll();
	        
	        System.out.println("licenseComputer = " + licenseComputer + ", systemComputer = " + systemComputer); 
	        int total = objects.size() + systemComputer;
	        if (total > licenseComputer) {
				data.put("state", "1");
				data.put("info", "当前已添加 "+ systemComputer +" 个软件, 准备导入 " + objects.size() + " 个客户端, 总数超过授权 " + licenseComputer + " 个软件！");
				return data;
	        }
	        // 判断当前行数是否超过授权客户端数结束

			switch(softImportService.inputSofts(filePath)) {
				case 1:
						msg = "文件解析出错，请检查资产类别、所属部门、是否监控、监控方式格式是否正确！";
						auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加软件文件格式解析出错", "资产管理", "导入文件", "失败");
						data.put("state", "1");
						data.put("info", msg);
						return data;
					case 2:
						msg = "IP和端口组合重复，请检查是否重复！";
						auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加软件IP地址和端口组合重复", "资产管理", "导入文件", "失败");
						data.put("state", "1");
						data.put("info", msg);
						return data;
					case 3:
						msg = "数据解析错误，请检查数据格式是否正确！";
						auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加设备数据解析错误", "资产管理", "导入文件", "失败");
						data.put("state", "1");
						data.put("info", msg);
						return data;
					case 4:
						msg = "监控类别数据解析错误，请检查导入文件内所填写类别名称和所属类别下拉框中资产类别名称是否一致（所有资产）！";
						auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加设备数据解析错误", "资产管理", "导入文件", "失败");
						data.put("state", "1");
						data.put("info", msg);
						return data;
					case 5:
						msg = "部门名称数据解析错误，请检查导入文件内部门名称和所属部门下拉框中部门名称是否一致（所有资产）！";
						auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加设备数据解析错误", "资产管理", "导入文件", "失败");
						data.put("state", "1");
						data.put("info", msg);
						return data;
				}
				
				auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加设备", "资产管理", "导入文件", "成功");
				msg = "导入成功！";
				data.put("state", "0");
				data.put("info", msg);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null!=target && target.exists()){
				target.delete();
			}
		}
		
		auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "批量添加设备", "资产管理", "导入文件", "失败");
		data.put("state", "1");
		data.put("info", "导入失败");
		return data;
	}	
}
