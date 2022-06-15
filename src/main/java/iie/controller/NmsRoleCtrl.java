package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsRole;
import iie.service.NmsAuditLogService;
import iie.service.NmsRoleService;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class NmsRoleCtrl {
    @Autowired
    NmsRoleService nrService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsRole> getAll() {
        List<NmsRole> res = nrService.getAll();
        return res;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> addNmsRole(HttpServletRequest request, HttpSession session)
            throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        String role = request.getParameter("role");
        if (role != null) {
            role = StringEscapeUtils.escapeSql(role);
        }

        if (nrService.countByName(role) > 0) {
            data.put("state", "2");
            data.put("info", "系统中已存在名称为  " + role + " 权限！");
            return data;
        }

        NmsRole nr = new NmsRole(role, new Timestamp(System.currentTimeMillis()));

        if (nrService.saveRole(nr)) {
            data.put("state", "0");
            data.put("info", "添加成功！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加了权限 [" + role + "] 成功", "系统管理", "新增数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "添加失败！");
        }
        return data;
    }

    @RequestMapping(value = "/getRoleById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsRole getRoleById(HttpServletRequest request)
            throws Exception {

        int id = new Integer(request.getParameter("id"));
        NmsRole role = nrService.findById(id);

        return role;
    }

    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> updateRole(HttpServletRequest request, HttpSession session)
            throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        int id = Integer.valueOf(request.getParameter("roleId"));
        String role = request.getParameter("role");
        NmsRole nr = nrService.findById(id);

        if (nrService.updateRole(nr, role)) {
            data.put("state", "0");
            data.put("info", "修改成功！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "修改了权限 [" + role + "] 成功", "系统管理", "修改数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "修改失败！");
        }
        return data;
    }
}
