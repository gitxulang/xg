package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsConfig;
import iie.rest.RestClient;
import iie.service.NmsAuditLogService;
import iie.service.NmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/Config")
public class NmsConfigCtrl {
    @Autowired
    NmsConfigService configService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/getConfig", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsConfig getConfigById(HttpServletRequest request) {
        NmsConfig res = configService.getConfig();
        if (res == null) {
            return new NmsConfig();
        } else {
            return res;
        }
    }

    @RequestMapping(value = "/getConfigById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsConfig getSSORules(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        NmsConfig obj = configService.findById(id);
        return obj;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsConfig> getAll() {
        List<NmsConfig> res = configService.getAll();
        return res;
    }

    public static boolean isNumber(Object o) {
        return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
    }

	public boolean isIP(String text) {
		if (text != null && !text.isEmpty()) {
			// 定义正则表达式
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			if (text.matches(regex)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateConfig(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();

        String id = request.getParameter("id");
        String jmsSwitch = request.getParameter("jmsSwitch");
        String jmsInterval = request.getParameter("jmsInterval");
        String jmsAddress = request.getParameter("jmsAddress");
        String jmsPort = request.getParameter("jmsPort");
        String jmsAgptIp = request.getParameter("jmsAgptIp");
        String jmsSfrzIp = request.getParameter("jmsSfrzIp");
        String jmsPtglIp = request.getParameter("jmsPtglIp");
        String dbDeleteInterval = request.getParameter("dbDeleteInterval");
        String dbOnlineInterval = request.getParameter("dbOnlineInterval");
        String alarmPingInterval = request.getParameter("alarmPingInterval");

        if (!isNumber(jmsSwitch)) {
            data.put("state", "1");
            data.put("info", "JMSClient接收基础数据开关非法！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改JMSClient接收基础数据开关非法不是数字", "系统配置", "修改数据", "失败");
            return data;
        } else {
            int jmsSwitchNum = Integer.valueOf(jmsSwitch);
            if (!(jmsSwitchNum == 0 || jmsSwitchNum == 1)) {
                data.put("state", "1");
                data.put("info", "JMSClient接收基础数据开关非法！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改JMSClient接收基础数据开关非法只能是开启1和关闭0状态", "系统配置", "修改数据", "失败");
                return data;
            }
        }

        if (!isNumber(jmsInterval)) {
            data.put("state", "1");
            data.put("info", "JMSClient接收基础数据开关非法！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "检测jms服务的周期参数非法不是数字", "系统配置", "修改数据", "失败");
            return data;
        } else {
            int jmsIntervalNum = Integer.valueOf(jmsInterval);
            if (jmsIntervalNum < 1 || jmsIntervalNum > 60) {
                data.put("state", "1");
                data.put("info", "JMSClient接收基础数据开关非法！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "检测jms服务的周期参数非法只能是1至60秒", "系统配置", "修改数据", "失败");
                return data;
            }
        }
        
        if (!isIP(jmsAddress)) {
            data.put("state", "1");
            data.put("info", "检测jms服务的IP地址非法！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "检测jms服务的IP地址非法", "系统配置", "修改数据", "失败");
            return data;
        }
        
        if (!isNumber(jmsPort)) {
            data.put("state", "1");
            data.put("info", "检测jms服务的端口号非法不是数字！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "检测jms服务的周期参数非法不是数字", "系统配置", "修改数据", "失败");
            return data;
        } else {
            int jmsPortNum = Integer.valueOf(jmsPort);
            if (jmsPortNum <= 0) {
                data.put("state", "1");
                data.put("info", "检测jms服务的周期参数非法端口号小于等于0！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "检测jms服务的周期参数非法端口号小于等于0", "系统配置", "修改数据", "失败");
                return data;
            }
        }

        if (!isIP(jmsAgptIp)) {
            data.put("state", "1");
            data.put("info", "jms接收安管平台数据运行日志的IP地址非法！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "jms接收安管平台数据运行日志的IP地址非法", "系统配置", "修改数据", "失败");
            return data;
        }
        
        if (!isIP(jmsSfrzIp)) {
            data.put("state", "1");
            data.put("info", "jms接收身份认证平台数据运行日志的IP地址非法！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "jms接收身份认证平台数据运行日志的IP地址非法", "系统配置", "修改数据", "失败");
            return data;
        }
        
        if (!isIP(jmsPtglIp)) {
            data.put("state", "1");
            data.put("info", "jms接收配置管理平台数据运行日志的IP地址非法！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "jms接收配置管理平台数据运行日志的IP地址非法", "系统配置", "修改数据", "失败");
            return data;
        }
        
        if (!isNumber(dbDeleteInterval)) {
            data.put("state", "1");
            data.put("info", "检测dbrouter的删除周期非法不是数字！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "检测dbrouter的删除周期非法不是数字", "系统配置", "修改数据", "失败");
            return data;
        } else {
            int dbDeleteIntervalNum = Integer.valueOf(dbDeleteInterval);
            if (dbDeleteIntervalNum <= 0 || dbDeleteIntervalNum > 7) {
                data.put("state", "1");
                data.put("info", "检测dbrouter的删除周期非法只能是1到7天！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "检测dbrouter的删除周期非法只能是1到7天", "系统配置", "修改数据", "失败");
                return data;
            }
        }
        
        if (!isNumber(dbOnlineInterval)) {
            data.put("state", "1");
            data.put("info", "检测dbrouter的online周期非法不是数字！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "检测dbrouter的online周期非法不是数字", "系统配置", "修改数据", "失败");
            return data;
        } else {
            int dbOnlineIntervalNum = Integer.valueOf(dbOnlineInterval);
            if (dbOnlineIntervalNum < 120) {
                data.put("state", "1");
                data.put("info", "检测dbrouter的online周期非法只能是120秒以上！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "检测dbrouter的online周期非法只能是120秒以上", "系统配置", "修改数据", "失败");
                return data;
            }
        }

        if (!isNumber(alarmPingInterval)) {
            data.put("state", "1");
            data.put("info", "检测alarmservice的ping周期非法不是数字！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "检测alarmservice的ping周期非法不是数字", "系统配置", "修改数据", "失败");
            return data;
        } else {
            int alarmPingIntervalNum = Integer.valueOf(alarmPingInterval);
            if (alarmPingIntervalNum < 30) {
                data.put("state", "1");
                data.put("info", "检测alarmservice的ping周期非法只能是30秒以上！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "检测alarmservice的ping周期非法只能是30秒以上", "系统配置", "修改数据", "失败");
                return data;
            }
        }

        NmsConfig config = configService.getConfig();
        config.setId(Integer.valueOf(id));
        config.setJmsSwitch(Integer.parseInt(jmsSwitch));
        config.setJmsInterval(Integer.parseInt(jmsInterval));
        config.setJmsAddress(jmsAddress);
        config.setJmsPort(jmsPort);
        config.setJmsAgptIp(jmsAgptIp);
        config.setJmsSfrzIp(jmsSfrzIp);
        config.setJmsPtglIp(jmsPtglIp);
        config.setDbDeleteInterval(Integer.parseInt(dbDeleteInterval));
        config.setDbOnlineInterval(Integer.parseInt(dbOnlineInterval));
        config.setAlarmPingInterval(Integer.parseInt(alarmPingInterval));
        
        if (configService.updateConfig(config)) {
        	// 修改成功自动同步至JMSClient配置文件
        	RestClient rc = new RestClient();
        	rc.restClient_JMSClient();
        	
            data.put("state", "0");
            data.put("info", "修改成功!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改系统配置成功", "系统配置", "修改数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "修改失败!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改系统配置失败", "规则管理", "修改数据", "失败");
        }
        return data;
    }
    
    @RequestMapping(value = "/syncConfig", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> syncConfig(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        
        RestClient rc = new RestClient();
		boolean ret = rc.restClient_JMSClient();
		
        if (ret) {
            data.put("state", "0");
            data.put("info", "同步成功!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "同步系统配置成功", "系统配置", "修改数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "同步失败!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "同步系统配置失败", "规则管理", "修改数据", "失败");
        }
        return data;
    }
}
