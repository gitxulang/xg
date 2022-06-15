package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsDepartment;
import iie.service.NmsAuditLogService;
import iie.service.NmsDepartmentService;
import iie.tools.NmsDepartmentNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Department")
public class NmsDepartmentCtrl {
    @Autowired
    NmsDepartmentService ndService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsDepartment> getAll() {
        List<NmsDepartment> res = ndService.getAll();
        return res;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsDepartment findById(HttpServletRequest request) {
        String id = request.getParameter("id");
        NmsDepartment res = ndService.findById(id);
        return res;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> addNmsDepartment(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        String ParentId = request.getParameter("ParentId");
        String DName = request.getParameter("DName");
        String DDesc = request.getParameter("DDesc");

        if (ParentId != null) {
            NmsDepartment nmsDepartment = ndService.findById(ParentId);
            if (nmsDepartment == null) {
                data.put("state", "1");
                data.put("info", "上级组织机构信息错误！");
                return data;
            }
        }

        NmsDepartment nd = new NmsDepartment(ParentId, DName, DDesc, 0, new Timestamp(System.currentTimeMillis()));

        if (ndService.saveDepartment(nd)) {
            data.put("state", "0");
            data.put("info", "添加成功！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加组织机构" + DName, "系统管理",
                    "新增数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "添加失败！");
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加组织机构" + DName + "失败", "系统管理",
                    "新增数据", "失败");
        }

        return data;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateDepartment(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        String idStr = request.getParameter("id");
        String id = "";
        if (idStr != null && idStr != "") {
            id = idStr;
        }

        if (id == null) {
            return null;
        } else {
            String ParentId = request.getParameter("ParentId");
            String DName = request.getParameter("DName");
            String DDesc = request.getParameter("DDesc");

            NmsDepartment department = ndService.findByIdForUpdate(id);

            if (ParentId != null) {
                NmsDepartment nmsDepartment = ndService.findById(ParentId);
                if (nmsDepartment == null || ParentId.equals(id)) {
                    data.put("state", "1");
                    data.put("info", "上级组织机构信息错误！");
                    return data;
                }
                department.setParentId(ParentId);
            }

            department.setDName(DName);
            department.setDDesc(DDesc);

            if (ndService.updateDepartment(department)) {
                data.put("state", "0");
                data.put("info", "修改成功！");
                auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                        "修改组织机构" + DName, "用户管理", "修改数据", "成功");
            } else {
                data.put("state", "1");
                data.put("info", "修改失败！");
                auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                        "修改组织机构" + DName + "失败", "用户管理", "修改数据", "失败");
            }

            return data;
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean  deleteDepartment(HttpServletRequest request, HttpSession session) {
        String idstr = request.getParameter("id");
        List<String> list = Arrays.asList(idstr.split(","));
        boolean result = true;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                result = result && ndService.deleteDepartment(String.valueOf(list.get(i)));
                if (result) {
                    NmsDepartment department = ndService.findById(String.valueOf(list.get(i)));
                    auditLogService.add(((NmsAdmin) session
                                    .getAttribute("user")).getRealname(), request
                                    .getRemoteAddr(), "删除组织机构：" + department.getDName(),
                            "系统管理", "删除数据", "成功");
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/tree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsDepartmentNode> getTree() {
        List<NmsDepartmentNode> res = ndService.getTree();
        return res;
    }

}
