package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsSecRule;
import iie.service.NmsAuditLogService;
import iie.service.NmsSecRuleService;
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
@RequestMapping("/SecRule")
public class NmsSecRuleCtrl {
    @Autowired
    NmsSecRuleService secRuleService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/getThelastRule", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsSecRule getTomcatById(HttpServletRequest request) {
        NmsSecRule res = secRuleService.getTheLastRule();
        if (res == null) {
            return new NmsSecRule();
        } else {
            return res;
        }
    }

    @RequestMapping(value = "/getSSORulesById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsSecRule getSSORules(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        NmsSecRule obj = secRuleService.findById(id);
        return obj;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsSecRule> getAll() {
        List<NmsSecRule> res = secRuleService.getAll();
        return res;
    }

    @RequestMapping(value = "/findByIdToUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsSecRule findByIdToUpdate(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        NmsSecRule obj = secRuleService.findById(id);
        return obj;
    }

    public static boolean isNumber(Object o) {
        return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
    }

    @RequestMapping(value = "/updateRule", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateUser(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();

        String id = request.getParameter("id");
        String sessionTimeout = request.getParameter("sessionTimeout");
        String pwdMinSize = request.getParameter("pwdMinSize");
        String pwdComplexity = request.getParameter("pwdComplexity");
        String pwdPeriod = request.getParameter("pwdPeriod");
        String loginAttempt = request.getParameter("loginAttempt");
        String secInterval = request.getParameter("secInterval");

        String appId = request.getParameter("appId");
        String appName = request.getParameter("appName");
        String ssoSwitch = request.getParameter("ssoSwitch");

        if (!isNumber(sessionTimeout)) {
            data.put("state", "3");
            data.put("info", "登录超时时长参数不是数字！");

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则登录超时时长参数不是数字", "规则管理", "修改数据", "失败");

            return data;
        } else {
            int num = Integer.valueOf(sessionTimeout);
            if (num > 300 || num <= 0) {
                data.put("state", "3");
                data.put("info", "登录超时时长只能设置0到300秒！");

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则登录超时时长不能大于300秒", "规则管理", "修改数据", "失败");

                return data;
            }
        }

        if (!isNumber(pwdMinSize)) {
            data.put("state", "3");
            data.put("info", "最小密码长度参数不是数字！");

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则最小密码长度参数不是数字", "规则管理", "修改数据", "失败");

            return data;
        } else {
            int num = Integer.valueOf(pwdMinSize);
            if (num < 10 || num <= 0) {
                data.put("state", "3");
                data.put("info", "最小密码长度不能小于10位！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则最小密码长度不能小于10位", "规则管理", "修改数据", "失败");

                return data;
            }

            if (num > 100) {
                data.put("state", "3");
                data.put("info", "最大密码长度不能大于50位！");
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则最大密码长度不能大于50位", "规则管理", "修改数据", "失败");

                return data;
            }
        }

        if (!isNumber(pwdComplexity)) {
            data.put("state", "3");
            data.put("info", "密码复杂度参数包含非数字字符！");

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则密码复杂度参数包含非数字字符", "规则管理", "修改数据", "失败");

            return data;
        } else {

            if (!pwdComplexity.equals("11111111")) {
                data.put("state", "3");
                data.put("info", "密码复杂度不允许修改, 必须包含以上规则！");

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则密码复杂度不符合标准", "规则管理", "修改数据", "失败");

                return data;
            }

            if (pwdComplexity.length() != 8) {
                data.put("state", "3");
                data.put("info", "密码复杂度长度不等于8位字符！");

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则密码复杂度长度不等于8位字符", "规则管理", "修改数据", "失败");

                return data;
            } else {
                for (int i = 0; i < 8; i++) {
                    if (!(pwdComplexity.charAt(i) == '0' || pwdComplexity.charAt(i) == '1')) {
                        data.put("state", "3");
                        data.put("info", "密码复杂度长度字符串包含非0或1字符！");

                        auditLogService.add(
                                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                                request.getRemoteAddr(), "修改规则密码复杂度长度字符串包含非0或1字符", "规则管理", "修改数据", "失败");

                        return data;
                    }
                }
            }
        }

        if (!isNumber(pwdPeriod)) {
            data.put("state", "3");
            data.put("info", "密码修改周期参数不是数字！");


            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则密码修改周期参数不是数字", "规则管理", "修改数据", "失败");

            return data;
        } else {
            int num = Integer.valueOf(pwdPeriod);
            if (num > 7 || num <= 0) {
                data.put("state", "3");
                data.put("info", "密码修改周期只能在1到7天之间！");

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则密码修改周期不能大于7天", "规则管理", "修改数据", "失败");

                return data;
            }
        }

        if (!isNumber(loginAttempt)) {
            data.put("state", "3");
            data.put("info", "最大尝试登录次数参数不是数字！");

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则最大尝试登录次数参数不是数字", "规则管理", "修改数据", "失败");

            return data;
        } else {
            int num = Integer.valueOf(loginAttempt);
            if (num > 5 || num <= 0) {
                data.put("state", "3");
                data.put("info", "最大尝试登录次数只能在1到5次之间！");

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则最大尝试登录次数不能大于5次", "规则管理", "修改数据", "失败");

                return data;
            }
        }

        if (!isNumber(secInterval)) {
            data.put("state", "3");
            data.put("info", "密码锁定时长参数不是数字！");

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则密码锁定时长参数不是数字", "规则管理", "修改数据", "失败");

            return data;
        } else {
            int num = Integer.valueOf(secInterval);
            if (num < 30) {
                data.put("state", "3");
                data.put("info", "密码锁定时长不能小于30分钟！");

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "修改规则密码锁定时长不能小于30分钟", "规则管理", "修改数据", "失败");

                return data;
            }
        }


        NmsSecRule rule = secRuleService.getTheLastRule();
        rule.setId(Integer.valueOf(id));
        rule.setSessionTimeout(Integer.parseInt(sessionTimeout));
        rule.setPwdMinSize(Integer.parseInt(pwdMinSize));
        rule.setPwdComplexity(pwdComplexity);
        rule.setPwdPeriod(Integer.parseInt(pwdPeriod));
        rule.setLoginAttempt(Integer.parseInt(loginAttempt));
        rule.setSecInterval(Integer.parseInt(secInterval));

        rule.setAppId(appId);
        rule.setAppName(appName);
        rule.setSsoSwitch(Integer.parseInt(ssoSwitch));

        if (secRuleService.updateRule(rule)) {
            data.put("state", "0");
            data.put("info", "修改成功!");

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则成功", "规则管理", "修改数据", "成功");

        } else {
            data.put("state", "3");
            data.put("info", "修改失败!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改规则失败", "规则管理", "修改数据", "失败");
        }
        return data;
    }
}
