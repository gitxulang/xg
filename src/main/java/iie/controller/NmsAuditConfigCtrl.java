package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAuditConfig;
import iie.service.NmsAuditConfigService;
import iie.service.NmsAuditLogService;
import iie.tools.NmsAuditConfigPart;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
@Controller
@RequestMapping("/AuditConfig")
public class NmsAuditConfigCtrl {
	@Autowired
	NmsAuditConfigService nmsAuditConfigService;

	@Autowired
	NmsAuditLogService auditLogService;

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}

	
	@RequestMapping(value = "/loadinfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsAuditConfig getInfo(HttpServletRequest request) {
		String basedir = nmsAuditConfigService.getDbBaseDir();
		String datadir = nmsAuditConfigService.getDbDataDir();
		
		List<NmsAuditConfig> list = nmsAuditConfigService.getAll();
		NmsAuditConfig obj = null;
		if (list != null && list.size() > 0) {
			obj = list.get(0);
			obj.setDbbasedir(basedir);
			obj.setDbdatadir(datadir);
		}
		// System.out.println("basedir = " + basedir + ", datadir = " + datadir);
		return obj;
	}

	@RequestMapping(value = "/loadpart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsAuditConfigPart> getPartInfo(HttpServletRequest request) {
		List<NmsAuditConfigPart> list = new ArrayList<NmsAuditConfigPart>();
		File[] fs = File.listRoots();
		for (int i = 0; i < fs.length; i++) {
			NmsAuditConfigPart obj = new NmsAuditConfigPart();
			obj.setPartdir(fs[i].getAbsolutePath());
			obj.setPartsize(fs[i].getTotalSpace() / 1024);
			obj.setPartused((fs[i].getTotalSpace() - fs[i].getUsableSpace()) / 1024);
			list.add(obj);
		}
		return list;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> update(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, String> data = new HashMap<String, String>();
		Integer id = 0;
		Long partsize = 0L;
		Long partused = 0L;
		Integer rule = 0;
		try {
			id = Integer.valueOf(request.getParameter("id"));
			partsize = Long.valueOf(request.getParameter("partsize"));
			partused = Long.valueOf(request.getParameter("partused"));
			rule = Integer.valueOf(request.getParameter("rule"));
		} catch (NumberFormatException e) {
			data.put("state", "1");
			data.put("info", "传输参数非数字类型！");
			return data;
		}
		
		if (rule < 0 || rule > 100) {
			data.put("state", "1");
			data.put("info", "数据库日志存储空间阈值必须在0到100之间（%）！");
			return data;
		}
		
		String dbbasedir = request.getParameter("dbbasedir");
		String dbdatadir = request.getParameter("dbdatadir");
		String partdir = request.getParameter("partdir");
		if (dbbasedir == null) {
			data.put("state", "1");
			data.put("info", "数据库安装路径为空更新失败！");
			return data;
		}
		if (dbdatadir == null) {
			data.put("state", "1");
			data.put("info", "数据库数据路径为空更新失败！");
			return data;
		}
		if (partdir == null) {
			data.put("state", "1");
			data.put("info", "数据库所在磁盘分区为空更新失败！");
			return data;
		}
		if (dbbasedir != null) {
			dbbasedir = StringEscapeUtils.escapeSql(dbbasedir);
		}
		if (dbdatadir != null) {
			dbdatadir = StringEscapeUtils.escapeSql(dbdatadir);
		}
		if (partdir != null) {
			partdir = StringEscapeUtils.escapeSql(partdir);
		}
		List<NmsAuditConfig> list = nmsAuditConfigService.findById(id);
		if (list == null) {
			data.put("state", "1");
			data.put("info", "没有找到记录！");
			return data;
		}
		NmsAuditConfig obj = list.get(0);
		if (!obj.getDbbasedir().equals(dbbasedir)
				&& !obj.getDbbasedir().equals("")) {
			data.put("state", "1");
			data.put("info", "数据库安装路径不允许被修改！");
			// System.out.println("dbbasedir = " + dbbasedir + ", obj.getDbbasedir() = " + obj.getDbbasedir());
			return data;
		}
		if (!obj.getDbdatadir().equals(dbdatadir)
				&& !obj.getDbdatadir().equals("")) {
			data.put("state", "1");
			data.put("info", "数据库分区路径不允许被修改！");
			return data;
		}

		obj.setDbbasedir(dbbasedir);
		obj.setDbdatadir(dbdatadir);
		obj.setPartdir(partdir);
		obj.setPartsize(partsize);
		obj.setPartused(partused);
		obj.setRule(rule);

		// 更新告警信息
		String alarm = "";
		double realRate = partused * 100.0 / partsize;
		double halfRule = rule * 1.0 / 2;

		if (realRate > halfRule && realRate <= rule) {
			alarm = "数据库所在磁盘分区使用率超过设定阈值[" + rule + "%]二分之一, 实际使用率["
					+ String.format("%.2f", realRate) + "%], 请管理员清理处理！";
		} else if (realRate > rule) {
			alarm = "数据库所在磁盘分区使用率超过设定阈值[" + rule + "%], 实际使用率["
					+ String.format("%.2f", realRate) + "%], 请管理员清理处理！";
		} else {
			alarm = "";
		}
		obj.setAlarm(alarm);

		if (nmsAuditConfigService.update(obj)) {
			data.put("state", "0");
			data.put("info", "修改成功！");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "修改了日志存储空间阈值", "日志管理", "修改数据",
					"成功");
		} else {
			data.put("state", "1");
			data.put("info", "修改失败！");
			auditLogService.add(
					((NmsAdmin) session.getAttribute("user")).getRealname(),
					request.getRemoteAddr(), "修改了日志存储空间阈值", "日志管理", "修改数据",
					"失败");
		}

		return data;
	}

	@RequestMapping(value = "/alarm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> getAlarmInfo(HttpServletRequest request) {
		Map<String, String> data = new HashMap<String, String>();
		// 从数据库nms_audit_conf找到已经配置的数据库分区记录
		Integer id = Integer.valueOf(request.getParameter("id"));
		List<NmsAuditConfig> listObj = nmsAuditConfigService.findById(id);
		if (listObj == null) {
			data.put("state", "1");
			data.put("info", "没有找到记录！");
			return data;
		}
		NmsAuditConfig confObj = listObj.get(0);
		String partdir = confObj.getPartdir();
		int rule = confObj.getRule();
		double halfRule = rule * 1.0 / 2;
		if (partdir == null) {
			partdir = "";
		}
		// 获取实时的配置数据库分区的磁盘空间利用率
		double realRate = 0d;
		File[] fs = File.listRoots();
		for (int i = 0; i < fs.length; i++) {
			if ((fs[i].getAbsolutePath()).equals(partdir)) {
				if (fs[i].getTotalSpace() > 0) {
					confObj.setPartsize(fs[i].getTotalSpace() / 1024);
					confObj.setPartused((fs[i].getTotalSpace() - fs[i]
							.getUsableSpace()) / 1024);
					realRate = 100.0
							* (fs[i].getTotalSpace() - fs[i].getUsableSpace())
							/ fs[i].getTotalSpace();
				}
			}
		}
		// 开始判断是否告警
		String alarm = "";
		if (realRate > halfRule && realRate <= rule) {
			data.put("state", "1");
			alarm = "数据库所在磁盘分区使用率超过设定阈值[" + rule + "%]二分之一, 实际使用率["
					+ String.format("%.2f", realRate) + "%], 请管理员清理处理！";
			data.put("info", alarm);
		} else if (realRate > rule) {
			data.put("state", "1");
			alarm = "数据库所在磁盘分区使用率超过设定阈值[" + rule + "%], 实际使用率["
					+ String.format("%.2f", realRate) + "%], 请管理员清理处理！";
			data.put("info", alarm);
		} else {
			data.put("state", "0");
			alarm = "数据库所在磁盘分区使用率正常！";
			data.put("info", alarm);
		}
		// 更新数据库告警内容和分区剩余空间大小分区总空间大小
		confObj.setAlarm(alarm);
		nmsAuditConfigService.update(confObj);
		// 返回实时的值
		return data;
	}
}
