package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsDepartment;
import iie.pojo.NmsUser;
import iie.service.NmsAuditLogService;
import iie.service.NmsDepartmentService;
import iie.service.NmsUserService;
import iie.tools.PageBean;
import iie.tools.excel.DateUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author xczhao
 * @date 2020/10/25 - 17:39
 */
@Controller
@RequestMapping("/User")
public class NmsUserCtrl {

    @Autowired
    NmsUserService nmsUserService;

    @Autowired
    NmsDepartmentService nmsDepartmentService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsUser> listPageByCondition(HttpServletRequest request, HttpSession session) throws Exception {
        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");
        int orderValue = 0;
        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "itime";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String name = request.getParameter("name");
        String card = request.getParameter("card");
        String beginParameter = request.getParameter("begin");
        String offsetParameter = request.getParameter("offset");

        if (name != null) {
            name = StringEscapeUtils.escapeSql(name);
        }
        if (card != null) {
            card = StringEscapeUtils.escapeSql(card);
        }

        Integer begin = 1;
        Integer offset = 10;
        if (beginParameter != null && beginParameter.length() > 0) {
            begin = Integer.valueOf(beginParameter);
        }
        if (offsetParameter != null && offsetParameter.length() > 0) {
            offset = Integer.valueOf(offsetParameter);
        }

        return nmsUserService.getPageByCondition(orderKey, orderValue, name, card, begin, offset);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> addUser(HttpServletRequest request,
                                       HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        NmsDepartment nmsDepartment = nmsDepartmentService.findById(request.getParameter("dept"));
        if (nmsDepartment == null) {
            data.put("state", "2");
            data.put("info", "单位部门信息错误!");
            return data;
        }

        String name = request.getParameter("name");
        String card = request.getParameter("card");
        String sex = request.getParameter("sex");
        String education = request.getParameter("education");
        String nationality = request.getParameter("nationality");
        String birthDate = request.getParameter("birthDate");
        Integer deled = 0;

        NmsUser nu = new NmsUser(name, card, nmsDepartment, education, nationality, sex, birthDate, deled, DateUtil.date2Str(new Date(), "yyyy-MM-dd"), new Timestamp(System.currentTimeMillis()), null, null);

        if (nmsUserService.saveUser(nu)) {
            data.put("state", "0");
            data.put("info", "添加成功!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加了用户" + name + "/" + card, "系统管理",
                    "新增数据", "成功");
        } else {
            data.put("state", "3");
            data.put("info", "添加失败!");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加用户" + name + "/" + card + "失败", "系统管理",
                    "新增数据", "失败");
        }

        return data;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateUser(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        String idStr = request.getParameter("id");
        Integer id = 0;
        if (idStr != null && idStr != "") {
            id = Integer.valueOf(idStr);
        }

        if (id == null) {
            return null;
        } else {
            NmsDepartment nmsDepartment = nmsDepartmentService.findById(request.getParameter("dept"));
            if (nmsDepartment == null) {
                data.put("state", "2");
                data.put("info", "部门信息错误!");
                return data;
            }

            String name = request.getParameter("name");
            String sex = request.getParameter("sex");
            String education = request.getParameter("education");
            String nationality = request.getParameter("nationality");
            String birthDate = request.getParameter("birthDate");
            NmsUser user = nmsUserService.findByIdForUpdate(id);
            user.setName(name);
            user.setNmsDepartment(nmsDepartment);
            user.setSex(sex);
            user.setBirthDate(birthDate);
            user.setEducation(education);
            user.setNationality(nationality);
            if (nmsUserService.updateUser(user)) {
                data.put("state", "0");
                data.put("info", "修改成功!");
                auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                        "修改用户" + name + "/" + user.getCard(), "用户管理", "修改数据", "成功");
            } else {
                data.put("state", "3");
                data.put("info", "修改失败!");
                auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                        "修改用户" + name + "/" + user.getCard() + "失败", "用户管理", "修改数据", "失败");
            }

            return data;
        }

    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean deleteUser(HttpServletRequest request, HttpSession session) {
        String idstr = request.getParameter("id");
        List<String> list = Arrays.asList(idstr.split(","));
        boolean result = true;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                result = result && nmsUserService.deleteUser(Integer.valueOf(list.get(i)));
                if (result) {
                    NmsUser user = nmsUserService.findByIdAny(Integer.valueOf(list.get(i)));
                    auditLogService.add(((NmsAdmin) session
                                    .getAttribute("user")).getRealname(), request
                                    .getRemoteAddr(), "删除用户：" + user.getName() + "/" + user.getCard(),
                            "系统管理", "删除数据", "成功");
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/getUserById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsUser getUserById(HttpServletRequest request, HttpSession session) throws Exception {
        String idStr = request.getParameter("id");
        int id = 0;

        if (idStr != null && idStr != "") {
            id = Integer.valueOf(idStr);
        }

        NmsUser user = nmsUserService.findById(id);
        return user;

    }

    @RequestMapping(value = "/ifUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean ifUser(HttpServletRequest request) throws Exception {
        String card = request.getParameter("card");
        return nmsUserService.ifUser(card);
    }
}
