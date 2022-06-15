package iie.controller;

import iie.pojo.NmsFunction;
import iie.pojo.NmsRole;
import iie.pojo.NmsRoleFunction;
import iie.service.NmsAdminService;
import iie.service.NmsAuditLogService;
import iie.service.NmsRoleFunctionService;
import iie.service.NmsRoleService;
import iie.tools.PageBean;
import iie.tools.RoleFunctionBean;
import org.apache.commons.lang.StringEscapeUtils;
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

@Controller
@RequestMapping("/NmsRoleFunction")
public class NmsRoleFunctionCtrl {
    @Autowired
    NmsRoleService nrService;

    @Autowired
    NmsRoleFunctionService nrfService;

    @Autowired
    NmsAuditLogService auditLogService;

    @Autowired
    NmsAdminService naService;

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsRoleFunction> getAll() {
        List<NmsRoleFunction> res = nrfService.getAll();
        return res;
    }

    @RequestMapping(value = "/loadRoleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<RoleFunctionBean> staticsNmsAssetAlarm(
            HttpServletRequest request, HttpSession session) {
        String role = request.getParameter("role");
        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "id";
            orderValue = "0";
        }
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        if (role != null) {
            role = StringEscapeUtils.escapeSql(role);
        }

        PageBean<RoleFunctionBean> page = nrfService.getRoleFunctionPage(role, begin, offset, orderKey, orderValue);
        return page;
    }

    @RequestMapping(value = "/findByRoleId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsRoleFunction> findByRoleId(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        List<NmsRoleFunction> res = nrfService.findByRoleId(id);
        return res;
    }

    @RequestMapping(value = "/addRoleFunctions", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> addRoleFunctions(HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();
        String role = request.getParameter("role");
        if (role != null) {
            role = StringEscapeUtils.escapeSql(role);
        }
        String[] functions = request.getParameter("functions").split(",");
        NmsRole nmsRole = nrfService.findRoleByName(role);
        boolean success = true;
        for (String str : functions) {
            int functionId = Integer.valueOf(str);
            NmsFunction function = nrfService.findFunctionById(functionId);
            NmsRoleFunction roleFunction = new NmsRoleFunction();
            roleFunction.setNmsRole(nmsRole);
            roleFunction.setNmsFunction(function);
            success = success && nrfService.add(roleFunction);
        }

        if (success) {
            data.put("state", "0");
            data.put("info", "添加成功！");
        } else {
            data.put("state", "1");
            data.put("info", "添加失败！");
        }

        return data;
    }

    @RequestMapping(value = "/updateRoleFunction", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateRoleFunction(HttpServletRequest request) {
        boolean res = true;
        Map<String, String> data = new HashMap<String, String>();
        int id = Integer.valueOf(request.getParameter("roleId"));
        String[] functions = request.getParameter("functions").split(",");

        List<NmsRoleFunction> nowRoleFunctions = nrfService.findRoleFunctionByRoleId(id);

        if (nowRoleFunctions != null && nowRoleFunctions.size() > 0) {
            for (int i = 0; i < nowRoleFunctions.size(); i++) {
                boolean flag = true;
                for (int j = 0; j < functions.length; j++) {
                    if (nowRoleFunctions.get(i).getNmsFunction().getId() == Integer.valueOf(functions[j])) {
                        functions[j] = "0";
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    res = res && nrfService.delete(nowRoleFunctions.get(i));
                }
            }
        }
        for (String str : functions) {
            if (!str.equals("0")) {
                NmsRoleFunction roleFunction = new NmsRoleFunction();
                roleFunction.setNmsRole(nrService.findById(id));
                NmsFunction nmsFunction = nrfService.findFunctionById(Integer.valueOf(str));
                roleFunction.setNmsFunction(nmsFunction);
                res = res && nrfService.add(roleFunction);
            }
        }

        if (res) {
            data.put("state", "0");
            data.put("info", "修改成功！");
        } else {
            data.put("state", "1");
            data.put("info", "修改失败！");
        }

        return data;
    }

    @RequestMapping(value = "/deleteRoleFunction", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> deleteRoleFunction(HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();
        String roleId = request.getParameter("roleId");
        if (roleId == null || roleId.equals("")) {
            data.put("state", "1");
            data.put("info", "参数错误, 删除失败！");
            return data;
        }
        int id = Integer.valueOf(roleId);
        if (id == 1 || id == 2 || id == 3 || id == 4) {
            data.put("state", "1");
            data.put("info", "该角色是系统内默认角色, 禁止删除！");
            return data;
        }

        if (naService.findByRoleId(id) > 0) {
            data.put("state", "1");
            data.put("info", "该角色被管理员或用户引用, 禁止删除！");
            return data;
        }

        boolean res = nrfService.deleteByRoleId(id);

        if (!res) {
            data.put("state", "1");
            data.put("info", "删除权限映射关系失败！");
            return data;
        }

        res = nrService.deleteById(id);
        if (!res) {
            data.put("state", "1");
            data.put("info", "删除角色信息失败！");
            return data;
        }

        data.put("state", "0");
        data.put("info", "删除成功！");
        return data;
    }
}


