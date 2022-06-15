package iie.controller;

import com.alibaba.fastjson.JSONObject;
import iie.pojo.*;
import iie.rest.RestClient;
import iie.service.*;
import iie.tools.DesUtil;
import iie.tools.JwtUtil;
import iie.tools.MD5Tools;
import iie.tools.PageBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused"})
@Controller
@RequestMapping("/Admin")
public class NmsAdminCtrl {
    public static List<NmsRoleFunction> RoleFunction;

    @Autowired
    NmsAdminService naService;

    @Autowired
    NmsSecRuleService nslService;

    @Autowired
    NmsRoleService nrService;

    @Autowired
    NmsDepartmentService ndService;

    @Autowired
    NmsAuditLogService auditLogService;

    @Autowired
    NmsRoleFunctionService roleFunctionService;

    @Autowired
    NmsLicenseService licenseService;

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsAdmin> getAll(HttpServletRequest request, HttpSession session) {
        List<NmsAdmin> res = naService.getAll();

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看用户管理 ", "系统管理", "查询数据", "成功");

        return res;
    }

    @RequestMapping(value = "/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAdmin> listPageByCondition(
            HttpServletRequest request, HttpSession session) throws Exception {
        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");
        int orderValue = 0;
        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "itime";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String username = request.getParameter("username");
        String phone = request.getParameter("phone");
        String beginParameter = request.getParameter("begin");
        String offsetParameter = request.getParameter("offset");

        if (username != null) {
            username = StringEscapeUtils.escapeSql(username);
        }
        if (phone != null) {
            phone = StringEscapeUtils.escapeSql(phone);
        }

        Integer begin = 1;
        Integer offset = 10;
        if (beginParameter != null && beginParameter.length() > 0) {
            begin = Integer.valueOf(beginParameter);
        }
        if (offsetParameter != null && offsetParameter.length() > 0) {
            offset = Integer.valueOf(offsetParameter);
        }

        return naService.getPageByCondition(orderKey, orderValue, username, phone, begin, offset);
    }

    /**
     * @description: 检查是否包含中文字符
     * @param: char c
     * @return: boolean
     * @date: 2020/12/20
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> addNa(HttpServletRequest request,
                                     HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        NmsRole nmsRole = nrService.findById(new Integer(request.getParameter("role")));
        if (nmsRole == null) {
            data.put("state", "1");
            data.put("info", "角色信息错误!");
            return data;
        }

        NmsDepartment nmsDepartment = ndService.findById(request.getParameter("dept"));
        if (nmsDepartment == null) {
            data.put("state", "1");
            data.put("info", "部门信息错误!");
            return data;
        }


        String username = request.getParameter("username");
        if (nmsDepartment == null) {
            data.put("state", "1");
            data.put("info", "账号名称不能为空!");
            return data;
        }

        for (int i = 0; i < username.length(); i++) {
            if (isChinese(username.charAt(i))) {
                data.put("state", "1");
                data.put("info", "账号名称不能含有中文字符!");
                return data;
            }
        }
        String realname = request.getParameter("realname") == null ? "" : request.getParameter("realname");
        Integer sex = new Integer(request.getParameter("sex") == null ? "0" : request.getParameter("sex"));
        String phone = request.getParameter("mobile") == null ? "" : request.getParameter("mobile");
        String email = request.getParameter("email") == null ? "" : request.getParameter("email");
        Integer deled = 0;

        NmsAdmin na = new NmsAdmin(nmsRole, nmsDepartment, username, "123Qwe!@#$", realname, sex, phone, email, deled, new Timestamp(System.currentTimeMillis()));
        na.setLastErrorTime(new Timestamp(System.currentTimeMillis()));
        na.setLoginCount(5);
        na.setLastPasswordChangeTime(new Timestamp(System.currentTimeMillis()));

        if (naService.saveAdmin(na)) {
            data.put("state", "0");
            data.put("info", "添加成功!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加了用户" + username, "系统管理",
                    "新增数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "添加失败!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加用户" + username + "失败", "系统管理",
                    "新增数据", "失败");
        }
        return data;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateUser(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.equals("")) {
            data.put("state", "1");
            data.put("info", "传递的账号唯一标识为空非法！");
            return data;
        } else {
            // 判断传递的userId是否是当前登录用户,防止被伪造攻击修改密码
            NmsAdmin sessionAdmin = (NmsAdmin) session.getAttribute("user");
            if (sessionAdmin == null) {
                data.put("state", "1");
                data.put("info", "当前登录会话超时，请重新登录！");
                return data;
            }
            System.out.println("[DEBUG] sessionAdmin.getId() = " + sessionAdmin.getId() + ", idStr = " + idStr);
            if (!idStr.trim().equals(sessionAdmin.getId().toString())) {
                data.put("state", "1");
                data.put("info", "禁止修改越权修改当前登录账号信息！");
                return data;
            }
        }

        Integer id = 0;
        try {
            id = Integer.valueOf(idStr);
        } catch (Exception e) {
            data.put("state", "1");
            data.put("info", "传递的账号唯一标识为空非法！");
            return data;
        }
        String role = request.getParameter("role");
        NmsRole nmsRole = naService.findById(id).getNmsRole();
        if (role != null && !role.equals("")) {
            nmsRole = nrService.findById(new Integer(role));
        }

        if (nmsRole == null) {
            data.put("state", "1");
            data.put("info", "角色信息错误!");
            return data;
        }

        NmsDepartment nmsDepartment = ndService.findById(request.getParameter("dept"));
        if (nmsDepartment == null) {
            data.put("state", "1");
            data.put("info", "部门信息错误!");
            return data;
        }

        String username = request.getParameter("username");
        if (nmsDepartment == null) {
            data.put("state", "1");
            data.put("info", "账号名称不能为空!");
            return data;
        }

        for (int i = 0; i < username.length(); i++) {
            if (isChinese(username.charAt(i))) {
                data.put("state", "1");
                data.put("info", "账号名称不能含有中文字符!");
                return data;
            }
        }

        String old_password = request.getParameter("old_password");
        String password = request.getParameter("password");
        String realname = request.getParameter("realname") == null ? "" : request.getParameter("realname");
        Integer sex = new Integer(request.getParameter("sex") == null ? "0" : request.getParameter("sex"));
        String phone = request.getParameter("mobile") == null ? "" : request.getParameter("mobile");
        String email = request.getParameter("email") == null ? "" : request.getParameter("email");

        NmsAdmin user = naService.findByIdForUpdate(id);
        user.setNmsRole(nmsRole);
        user.setNmsDepartment(nmsDepartment);
        user.setSex(sex);
        user.setPhone(phone);
        user.setEmail(email);
        // 禁止修改
        // user.setRealname(realname);
        if (password != null && old_password != null) {
            byte[] bytes = new BASE64Decoder().decodeBuffer(old_password);
            old_password = new String(bytes, "UTF-8");
            bytes = new BASE64Decoder().decodeBuffer(password);
            password = new String(bytes, "UTF-8");
            if (password.equals(old_password)) {
                data.put("state", "1");
                data.put("info", "新密码不能和原密码相同！");
                auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "用户" + username + "修改用户密码失败，新密码不能和原密码相同", "用户管理", "修改数据", "失败");
                return data;
            }

            NmsSecRule rule = nslService.getTheLastRule();
            if (user != null) {
                if (user.getLoginCount() == 0) {
                    long now = System.currentTimeMillis();// 获得当前系统毫秒数
                    if ((now - user.getLastErrorTime().getTime()) < (rule.getSecInterval() * 60 * 1000)) {// rule的分钟需要转换成毫秒,如果小于，说明处于锁定状态
                        data.put("state", "1");
                        data.put("info", "账号被锁定，锁定剩余时间："
                                + (((rule.getSecInterval() * 60 * 1000) - (now - user.getLastErrorTime().getTime()))
                                / 60000 + 1)
                                + "分钟！");
                        auditLogService.add(user.getUsername(), request.getRemoteAddr(), "用户" + username + "账号被锁定，不能修密码", "系统管理", "修改数据", "失败");

                        return data;
                    } else {
                        // 锁定时间已过可以进行用户登录尝试
                        user.setLoginCount(rule.getLoginAttempt());
                        naService.updateAdmin(user);
                        data.put("state", "1");
                        data.put("info", "账号已解锁，请重新操作！");
                        auditLogService.add(user.getUsername(), request.getRemoteAddr(), "用户" + username + "修密码成功", "系统管理", "修改数据", "成功");

                        return data;
                    }
                } else if (password != null && old_password != null) {
                    if (naService.judgeOldPasswordByUsername(username, MD5Tools.MD5(old_password))) {
                        // 验证密码强度是否达到
                        boolean satisfyFlag = true;
                        String errorCodeString = "修改密码失败，密码未满足";
                        if (password.length() < rule.getPwdMinSize()) {
                            satisfyFlag = false;
                            errorCodeString += "，长度不小于" + rule.getPwdMinSize();
                        } else {
                            // 完整正则表达式
                            // (?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{10,}
                            if ((nslService.getThePwdComplexity().charAt(0) == '1')
                                    && !naService.getRegularAns("(?=.*[A-Z]).{10,}", password)) {// 校验是否存在大写字母
                                satisfyFlag = false;
                                errorCodeString += "，大写英文字母";
                            }
                            if ((nslService.getThePwdComplexity().charAt(1) == '1')
                                    && !naService.getRegularAns("(?=.*[a-z]).{10,}", password)) {// 校验是否存在大写字母
                                satisfyFlag = false;
                                errorCodeString += "，小写英文字母";
                            }
                            if ((nslService.getThePwdComplexity().charAt(2) == '1')
                                    && !naService.getRegularAns("(?=.*[0-9]).{10,}", password)) {// 校验是否存在大写字母
                                satisfyFlag = false;
                                errorCodeString += "，数字";
                            }
                            if ((nslService.getThePwdComplexity().charAt(3) == '1')
                                    && !naService.getRegularAns("(?=.*[^a-zA-Z0-9]).{10,}", password)) {// 校验是否存在大写字母
                                satisfyFlag = false;
                                errorCodeString += "，特殊字符";
                            }
                            if ((nslService.getThePwdComplexity().charAt(4) == '1') && (old_password.equals(username))) {
                                satisfyFlag = false;
                                errorCodeString += "，不能与账户相同";
                            }
                            if ((nslService.getThePwdComplexity().charAt(5) == '1') && (old_password.equals(password))) {
                                satisfyFlag = false;
                                errorCodeString += "，不能使用原密码";
                            }
                        }
                        if (satisfyFlag) {
                            user.setPassword(MD5Tools.MD5(password));
                            user.setLastPasswordChangeTime(new Timestamp(System.currentTimeMillis()));
                            nslService.getThePwdComplexity();
                            if (naService.updateAdmin(user)) {
                                data.put("state", "0");
                                data.put("info", "修改成功！");
                                auditLogService.add(user.getUsername(), request.getRemoteAddr(), "用户" + username + "修改密码成功", "系统管理", "修改数据", "成功");
                            }
                        } else {
                            data.put("state", "1");
                            data.put("info", errorCodeString + "！");
                            auditLogService.add(user.getUsername(), request.getRemoteAddr(), "用户" + username + errorCodeString, "系统管理", "修改数据", "失败");
                            return data;
                        }
                    } else {
                        data.put("state", "1");
                        Integer count = user.getLoginCount();
                        count--;
                        user.setLoginCount(count);
                        user.setLastErrorTime(new Timestamp(System.currentTimeMillis()));
                        naService.updateAdmin(user);
                        data.put("info", "原密码错误！" + ",密码尝试次数剩余：" + count + "次，请确认密码后再次输入！");
                        auditLogService.add(user.getUsername(), request.getRemoteAddr(), "用户" + username + "尝试登录失败", "系统管理", "修改数据", "失败");

                        return data;
                    }
                }
            }
        }

        if (naService.updateAdmin(user)) {
            data.put("state", "0");
            data.put("info", "修改成功！");
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                    "修改用户" + username, "用户管理", "修改数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "修改失败！");
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                    "修改用户" + username + "失败", "用户管理", "修改数据", "失败");
        }
        return data;
    }

    @RequestMapping(value = "/updatePwdOfLongTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updatePwdOfLongTime(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        String userId = request.getParameter("userId");
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        if (userId == null || oldPwd == null || newPwd == null) {
            data.put("state", "1");
            data.put("info", "参数错误，不能为空！");
            return data;
        }

        // 判断传递的userId是否是当前登录用户,防止被伪造攻击修改密码
        NmsAdmin sessionAdmin = (NmsAdmin) session.getAttribute("user");
        if (sessionAdmin == null) {
            data.put("state", "1");
            data.put("info", "当前登录会话超时，请重新登录！");
            return data;
        }

        //System.out.println("[DEBUG] userId = " + userId + ", sessionAdmin.getId().toString() = " + sessionAdmin.getId().toString());

        if (!userId.trim().equals(sessionAdmin.getId().toString())) {
            data.put("state", "1");
            data.put("info", "禁止修改越权修改当前登录账号信息！");
            return data;
        }


        byte[] bytes = new BASE64Decoder().decodeBuffer(oldPwd);
        oldPwd = new String(bytes, "UTF-8");
        bytes = new BASE64Decoder().decodeBuffer(newPwd);
        newPwd = new String(bytes, "UTF-8");

        NmsSecRule rule = nslService.getTheLastRule();
        NmsAdmin admin = naService.findByIdForUpdatePassword(Integer.valueOf(userId));
        if (admin != null) {
            if (admin.getLoginCount() == 0) {
                long now = System.currentTimeMillis();
                // rule分钟需要转换成毫秒,如果小于，说明处于锁定状态
                if ((now - admin.getLastErrorTime().getTime()) < (rule
                        .getSecInterval() * 60 * 1000)) {
                    data.put("state", "1");
                    data.put(
                            "info",
                            "账号被锁定，锁定剩余时间："
                                    + (((rule.getSecInterval() * 60 * 1000) - (now - admin
                                    .getLastErrorTime().getTime())) / 60000 + 1)
                                    + "分钟！");
                    auditLogService.add(admin.getUsername(),
                            request.getRemoteAddr(), "用户" + admin.getUsername()
                                    + "账号被锁定，不能修密码", "系统管理", "修改数据", "失败");
                    return data;
                } else {
                    // 锁定时间已过可以进行用户登录尝试
                    admin.setLoginCount(rule.getLoginAttempt());
                    naService.updateAdmin(admin);
                    data.put("state", "1");
                    data.put("info", "账号已解锁，请重新操作！");
                    auditLogService.add(admin.getUsername(),
                            request.getRemoteAddr(), "用户" + admin.getUsername() + "修密码成功",
                            "系统管理", "修改数据", "成功");
                    return data;
                }
            } else if (newPwd != null && oldPwd != null) {
                if (naService.judgeOldPasswordByUsername(admin.getUsername(),
                        MD5Tools.MD5(oldPwd))) {
                    // 验证密码强度是否达到
                    boolean satisfyFlag = true;
                    String errorCodeString = "修改密码失败，密码未满足要求";
                    if (newPwd.length() < rule.getPwdMinSize()) {
                        satisfyFlag = false;
                        errorCodeString += "，长度不小于" + rule.getPwdMinSize();
                    } else {
                        // 完整正则表达式:
                        // (?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{10,}
                        // 校验是否存在大写字母
                        if ((nslService.getThePwdComplexity().charAt(0) == '1')
                                && !naService.getRegularAns(
                                "(?=.*[A-Z]).{10,}", newPwd)) {
                            satisfyFlag = false;
                            errorCodeString += "，大写英文字母";
                        }
                        // 校验是否存在大写字母
                        if ((nslService.getThePwdComplexity().charAt(1) == '1')
                                && !naService.getRegularAns(
                                "(?=.*[a-z]).{10,}", newPwd)) {
                            satisfyFlag = false;
                            errorCodeString += "，小写英文字母";
                        }
                        // 校验是否存在大写字母
                        if ((nslService.getThePwdComplexity().charAt(2) == '1')
                                && !naService.getRegularAns(
                                "(?=.*[0-9]).{10,}", newPwd)) {
                            satisfyFlag = false;
                            errorCodeString += "，数字";
                        }
                        // 校验是否存在大写字母
                        if ((nslService.getThePwdComplexity().charAt(3) == '1')
                                && !naService.getRegularAns(
                                "(?=.*[^a-zA-Z0-9]).{10,}", newPwd)) {
                            satisfyFlag = false;
                            errorCodeString += "，特殊字符";
                        }
                        if ((nslService.getThePwdComplexity().charAt(4) == '1')
                                && (oldPwd.equals(admin.getUsername()))) {
                            satisfyFlag = false;
                            errorCodeString += "，不能与账户相同";
                        }
                        if ((nslService.getThePwdComplexity().charAt(5) == '1')
                                && (oldPwd.equals(newPwd))) {
                            satisfyFlag = false;
                            errorCodeString += "，不能使用原密码";
                        }
                    }
//                    System.out.println("newPwd = " + newPwd + ", MD5Tools.MD5(newPwd)");
                    if (satisfyFlag) {
                        admin.setPassword(MD5Tools.MD5(newPwd));
                        admin.setLastPasswordChangeTime(new Timestamp(System
                                .currentTimeMillis()));

                        nslService.getThePwdComplexity();
                        if (naService.updateAdmin(admin)) {
                            data.put("state", "0");
                            data.put("info", "修改成功！");
                            auditLogService.add(admin.getUsername(),
                                    request.getRemoteAddr(), "用户" + admin.getUsername()
                                            + "修改密码成功", "系统管理", "修改数据", "成功");
                        }
                    } else {
                        data.put("state", "1");
                        data.put("info", errorCodeString + "！");
                        auditLogService.add(admin.getUsername(),
                                request.getRemoteAddr(), "用户" + admin.getUsername()
                                        + errorCodeString, "系统管理", "修改数据",
                                "失败");
                        return data;
                    }
                } else {
                    data.put("state", "1");
                    Integer count = admin.getLoginCount();
                    count--;
                    admin.setLoginCount(count);
                    admin.setLastErrorTime(new Timestamp(System
                            .currentTimeMillis()));
                    naService.updateAdmin(admin);
                    data.put("info", "原密码错误！" + ",密码尝试次数剩余：" + count
                            + "次，请确认密码后再次输入！");
                    auditLogService.add(admin.getUsername(),
                            request.getRemoteAddr(),
                            "用户" + admin.getUsername() + "尝试登录失败", "登录管理", "修改数据", "失败");
                    return data;
                }
            }
        } else {
            data.put("state", "1");
            data.put("info", "未知账号名称错误！");
            auditLogService.add("未知账号", request.getRemoteAddr(), "未知账号尝试登录失败", "登录管理", "修改数据", "失败");

            return data;
        }
        return data;
    }

    @RequestMapping(value = "/list/deleteUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean listDelete(HttpServletRequest request, HttpSession session) {
		String idstr = request.getParameter("id");
		List<String> list = Arrays.asList(idstr.split(","));
		boolean result = true;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println("[DEBUG] delete " + list.get(i) + " username...");
				if (list.get(i).equals("1")|| list.get(i).equals("2")|| list.get(i).equals("3") || list.get(i).equals("4")) {
					NmsAdmin admin = naService.findByIdAny(Integer.valueOf(list.get(i)));
					auditLogService.add(((NmsAdmin) session
							.getAttribute("user")).getRealname(), request
							.getRemoteAddr(), "尝试删除系统内置账号：" + admin.getUsername(),
							"系统管理", "删除数据", "失败");
					return false;
				}
				result = result && naService.deleteAdmin(Integer.valueOf(list.get(i)));
				if (result) {
					NmsAdmin admin = naService.findByIdAny(Integer.valueOf(list.get(i)));
					auditLogService.add(((NmsAdmin) session
							.getAttribute("user")).getRealname(), request
							.getRemoteAddr(), "删除账号：" + admin.getUsername(),
							"系统管理", "删除数据", "成功");
				}
			}
		}
		return result;
	}

    @RequestMapping(value = "/getAdminByUserAndPwd", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAdmin getAdminByUserAndPwd(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        NmsAdmin admin = naService.findByLoginInfo(username, password);
        return admin;
    }

    @RequestMapping(value = "/getuser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String clearSession(HttpServletRequest request, HttpSession session) {
        NmsAdmin admin = (NmsAdmin) session.getAttribute("user");
        if (admin == null) {
            return null;
        }
        return admin.getRealname();
    }

    @RequestMapping(value = "/getAdminById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAdmin getAdminById(HttpServletRequest request, HttpSession session)
            throws Exception {
        String idStr = request.getParameter("id");
        int id = 0;
        NmsAdmin nowAdmin = (NmsAdmin) session.getAttribute("user");
        if (idStr == null || idStr.equals("")) {
            id = nowAdmin.getId();
        } else {
            id = Integer.valueOf(idStr);
        }
        NmsAdmin admin = naService.findById(id);
        System.out.println("admin = " + admin + ", id = " + id + ", idStr = " + idStr);
        if (nowAdmin != null && admin != null) {
            admin.setRealname(nowAdmin.getRealname());
        }
        return admin;
    }

    @RequestMapping(value = "/ifUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean ifUser(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        return naService.ifUser(username);
    }

    @RequestMapping(value = "/getErrorMsg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getErrorMsg(HttpSession session) {
        String errorMsg = (String) session.getAttribute("errorMsg");
        if (errorMsg == null) {
            errorMsg = "";
        }
        return errorMsg;
    }

    @SuppressWarnings({ "deprecation" })
	@RequestMapping(value = "/loginAjax", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String loginAjax(String username, String password,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		byte[] bytes;
		String decodeUsername = "";
		String decodePassword = "";
		username = request.getParameter("username");
		password = request.getParameter("password");
		try {
			if (username != null && password != null) {
				bytes = new BASE64Decoder().decodeBuffer(username);
				decodeUsername = new String(bytes, "UTF-8");

				bytes = new BASE64Decoder().decodeBuffer(password);
				decodePassword = new String(bytes, "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
			session.setAttribute("errorMsg", "账号或密码错误");
			auditLogService.add(decodeUsername, request.getRemoteAddr(),
					decodeUsername + "尝试登录系统，账号或密码错误", "登录管理", "系统登录", "失败");
			return "login.html";
		}
        RestClient restClient = new RestClient();
		NmsAdmin admin = naService.findByLoginInfo(decodeUsername, decodePassword);
		NmsAdmin user = naService.findByUsername(decodeUsername);
		NmsSecRule rule = nslService.getTheLastRule();
		RoleFunction = roleFunctionService.getAllReal();
		if (user == null) {
			session.setAttribute("errorMsg", "账号或密码错误");
			auditLogService.add(decodeUsername, request.getRemoteAddr(),
					decodeUsername + "尝试登录系统，账号或密码错误", "登录管理", "系统登录", "失败");
			return "login.html";
		} else if (user.getLoginCount() == 0) {
			// 用户当前状态被锁, 判断锁定时间是否达到, 达到后将count赋值为5
			long now = System.currentTimeMillis();
			if ((now - user.getLastErrorTime().getTime()) < (rule
					.getSecInterval() * 60 * 1000)) {
				// 需要转换成毫秒, 如果小于说明处于锁定状态
				session.setAttribute("errorMsg", "账号被锁定，锁定剩余时间："
								+ (((rule.getSecInterval() * 60 * 1000) - (now - user.getLastErrorTime().getTime())) / 60000 + 1) + "分钟");
				auditLogService.add(user.getUsername(), request.getRemoteAddr(), decodeUsername
								+ "尝试登录系统，账号被锁定", "登录管理", "系统登录", "失败");
				return "login.html";
			} else {
				// 锁定时间已过可以进行用户登录尝试
				user.setLoginCount(rule.getLoginAttempt());
				if (admin == null) {
					Integer count = user.getLoginCount();
					count--;
					user.setLoginCount(count);
					user.setLastErrorTime(new Timestamp(System.currentTimeMillis()));
					naService.updateAdmin(user);
					session.setAttribute("errorMsg", "账号或密码错误," + "密码尝试次数剩余：" + count + "次，请确认密码后再次输入");
					session.setAttribute("errorCount", "密码尝试次数剩余：" + count + "次，请确认密码后再次输入");
					auditLogService.add(user.getUsername(), request.getRemoteAddr(), decodeUsername
									+ "尝试登录系统，密码尝试数剩余" + count + "次", "登录管理", "系统登录", "失败");
					return "login.html";
				} else {
					admin.getNmsRole().getNmsRoleFunctions();
					session.setAttribute("user", admin);
					session.setMaxInactiveInterval(rule.getSessionTimeout());

					// 生成接口认证JWT Token
					String tokenRaw = admin.getUsername() + System.currentTimeMillis();
					String jwt = JwtUtil.generateToken(tokenRaw);
					session.setAttribute("token", tokenRaw);
					jwt = URLEncoder.encode(jwt);
					Cookie cookie_token = new Cookie("token", jwt);
					cookie_token.setMaxAge(60 * 60 * 24);
					cookie_token.setPath("/");
					response.addCookie(cookie_token);

					naService.updateAdmin(user);
					// 判断密码是否需要修改
					if (((System.currentTimeMillis() - user.getLastPasswordChangeTime().getTime()) / (24 * 3600 * 1000)) < rule.getPwdPeriod()) {
						session.setAttribute("exportPwd", decodePassword);
						// 判断是否已经注册
						NmsLicense license = licenseService.getLastLicense();
						if (!licenseAuth(license)) {
                            auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                                    decodeUsername + "登录系统", "登录管理", "未注册", "失败");
							session.setAttribute("errorMsg", "license");
							return "login.html";
						} else {
                            boolean authResult = restClient.restClient_auth(user.getUsername());
                            if (!authResult) {
                                auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                                        decodeUsername + "登录系统", "登录管理", "登录未授权", "失败");
                                session.setAttribute("errorMsg", "登录未授权，拒绝登录");
                                return "login.html";
                            }
                            auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                                    decodeUsername + "登录系统", "登录管理", "系统登录", "成功");
							session.setAttribute("ssoLogin", 0);
							return "main.html";
						}
					} else {
						// 返回修改密码的错误码，供前台判断，然后弹出修改密码的界面
						session.setAttribute("errorMsg", "password");
						auditLogService.add(user.getUsername(), request.getRemoteAddr(), decodeUsername
										+ "尝试登录系统，密码需要修改", "登录管理", "系统登录", "失败");
						return "login.html";
					}
				}
			}
		} else if (admin == null) {
			Integer count = user.getLoginCount();
			count--;
			user.setLoginCount(count);
			user.setLastErrorTime(new Timestamp(System.currentTimeMillis()));
			naService.updateAdmin(user);
			session.setAttribute("errorCount", "密码尝试次数剩余：" + count + "次，请确认密码后再次输入");
			session.setAttribute("errorMsg", "账号或密码错误," + "密码尝试次数剩余：" + count + "次，请确认密码后再次输入");
			auditLogService.add(user.getUsername(), request.getRemoteAddr(),
					decodeUsername + "账号或密码错误，" + "密码尝试次数剩余：" + count + "次", "登录管理", "系统登录", "失败");
			return "login.html";
		} else {
			admin.getNmsRole().getNmsRoleFunctions();
			session.setAttribute("user", admin);
			session.setMaxInactiveInterval(rule.getSessionTimeout());

			// JWT Token生成
			String tokenRaw = admin.getUsername() + System.currentTimeMillis();
			String jwt = JwtUtil.generateToken(tokenRaw);
			session.setAttribute("token", tokenRaw);
			jwt = URLEncoder.encode(jwt);
			Cookie cookie_token = new Cookie("token", jwt);
			cookie_token.setMaxAge(60 * 60 * 24);
			cookie_token.setPath("/");
			response.addCookie(cookie_token);

			// 重置可用登录次数
			user.setLoginCount(rule.getLoginAttempt());
			naService.updateAdmin(user);
			// 判断密码是否需要修改
			if (((System.currentTimeMillis() - user.getLastPasswordChangeTime().getTime()) / (24 * 3600 * 1000)) < rule.getPwdPeriod()) {
				session.setAttribute("exportPwd", decodePassword);
				// 判断是否已经注册
				NmsLicense license = licenseService.getLastLicense();
				if (!licenseAuth(license)) {
                    auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                            decodeUsername + "登录系统", "登录管理", "未注册", "失败");
					session.setAttribute("errorMsg", "license");
					return "login.html";
				} else {
                    boolean authResult = restClient.restClient_auth(user.getUsername());
                    if (!authResult) {
                        auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                                decodeUsername + "登录系统", "登录管理", "登录未授权", "失败");
                        session.setAttribute("errorMsg", "登录未授权，拒绝登录");
                        return "login.html";
                    }
                    auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                            decodeUsername + "登录系统", "登录管理", "系统登录", "成功");
					session.setAttribute("ssoLogin", 0);
					return "main.html";
				}
			} else {
				session.setAttribute("errorMsg", "password");
				auditLogService.add(user.getUsername(), request.getRemoteAddr(),
                        decodeUsername + "尝试登录系统，密码需要修改", "登录管理", "系统登录", "失败");
				return "login.html";
			}
		}
	}

	@RequestMapping(value = "/logoutAjax", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String logoutAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		if (session != null) {
			String user = ((NmsAdmin) session.getAttribute("user"))
					.getUsername();
			auditLogService.add(user, request.getRemoteAddr(), user
					+ "注销登录，登出系统", "登录管理", "系统登出", "成功");
			session.invalidate();
		}
		// JWT Token清空
		Cookie cookie_token = new Cookie("token", null);
		cookie_token.setMaxAge(0);
		cookie_token.setPath("/");
		response.addCookie(cookie_token);
		return "login.html";
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/ssoLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String ssoLogin2(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		String appId = request.getParameter("appId");
		String appName = request.getParameter("appName");
		String token = request.getParameter("token");

		System.out.println("[DEBUG] ssoLogin: appId="+ appId + ", appName=" + appName + ", token=" + token);
		try {
			RestClient restClient = new RestClient();
			String res = restClient.restClient_SSO(appId, appName, token);
			System.out.println("[DEBUG] ssoLogin: restClient.restClient_SSO(appId, appName, token) res=" + res);
			if (res == null || res.equals("")) {
				return "login.html";
			}
			JSONObject jsonObject =JSONObject.parseObject(res);
			String userName = jsonObject.getString("userName");
			String userRealName = jsonObject.getString("userRealName");
			String currentUserBiosName = jsonObject.getString("currentUserBiosName");
			String card = jsonObject.getString("card");
			String stateCode = jsonObject.getString("stateCode");
			String adminCode  = jsonObject.getString("adminCode");
			String message = jsonObject.getString("message");
			
			System.out.println("[DEBUG] ssoLogin: userName=" + userName + ", userRealName="+userRealName + ", currentUserBiosName=" + currentUserBiosName + ", card="+ card + ", stateCode=" + stateCode + ", adminCode=" + adminCode + ", message=" + message);
			
			// adminCode=001表示小系统管理员, adminCode=100表示大系统管理员
			if (stateCode != null && stateCode.equals("001") && adminCode != null && (adminCode.equals("001") || adminCode.equals("110"))) {
				NmsAdmin user = naService.findByUsername("root");
				if (user == null) {
					return "login.html";
				}
				NmsSecRule rule = nslService.getTheLastRule();
				RoleFunction = roleFunctionService.getAllReal();
				session.setMaxInactiveInterval(rule.getSessionTimeout());
				
				// 生成JWT Token
				String tokenRaw = user.getUsername() + System.currentTimeMillis();
				String jwt = JwtUtil.generateToken(tokenRaw);
				session.setAttribute("token", tokenRaw);
				jwt = URLEncoder.encode(jwt);
				Cookie cookie_token = new Cookie("token", jwt);
				cookie_token.setMaxAge(60 * 60 * 24);
				cookie_token.setPath("/");
				response.addCookie(cookie_token);

				// 重置可用登录次数
				System.out.println("[DEBUG] ssoLogin: userRealName:" + userRealName + " sso login success");
				user.setLoginCount(rule.getLoginAttempt());
				user.setRealname(userRealName);
				session.setAttribute("user", user);
				session.setAttribute("ssoLogin", 1);
				
				auditLogService.add(userRealName, request.getRemoteAddr(),
						userRealName + "单点登录系统，账号biosUserName", "登录管理", "系统登录", "成功");
				return "main.html";
			} else {
				System.out.println("[DEBUG] ssoLogin: refuse sso login from stateCode = " + stateCode + ", adminCode = " + adminCode);
				auditLogService.add(userRealName, request.getRemoteAddr(),
						userRealName + "单点登录系统，账号biosUserName", "登录管理", "系统登录", "失败");
				return "login.html";
			}
		} catch (Exception e) {
			System.out.println("[ERROR] ssoLogin: Exception: " + e.toString());
			return "login.html";
		}
	}

    @RequestMapping(value = "/ifsso", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public int ifsso(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        int sso = (int) session.getAttribute("ssoLogin");
        return sso;
    }

    @SuppressWarnings("static-access")
    public boolean licenseAuth(NmsLicense obj) {
        if (obj == null) {
            return false;
        }
        long regtime = obj.getRegtime();
        String license = obj.getLicense();
        DesUtil des = new DesUtil();
        String ret = null;
        try {
            ret = des.decrypt(license);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        // 分解解密后的证书序列号获取参数值
        String[] arr = ret.split("#");
        if (arr.length != 5) {
            return false;
        }
        int type = Integer.valueOf(arr[0]);
        int day = Integer.valueOf(arr[1]);
        long timestamp = Long.valueOf(arr[4]);
        // 进行验证当前时间戳和序列号里解析的应该一致
        if (timestamp != regtime) {
            return false;
        }
        // 如果是时间永久的或者服务器安装不受限制的直接返回成功
        if (type == 1 || type == 2) {
            return true;
        } else if (type == 0) {
            long deadtime = timestamp + day * 24 * 3600 * 1000L;
            System.out.println("System.currentTimeMillis() = "
                    + System.currentTimeMillis()
                    + ", timestamp + day * 24 * 3600 * 1000 = " + deadtime);
            if (System.currentTimeMillis() < deadtime) {
                return true;
            }
        }
        return false;
    }
}
