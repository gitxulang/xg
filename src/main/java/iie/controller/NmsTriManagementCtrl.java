package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsTriManagement;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsTriManagementService;
import iie.tools.PageBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/triManagement")
public class NmsTriManagementCtrl {
    @Autowired
    NmsTriManagementService nmsTriManagementService;

    @Autowired
    NmsAssetService nmsAssetService;

    @Autowired
    NmsAuditLogService auditLogService;

    /**
     * 展示第三方管理端信息
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/date/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsTriManagement> listPageByDateCondition(HttpServletRequest request, HttpSession session) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderKey",request.getParameter("orderKey"));
        paramMap.put("orderValue",request.getParameter("orderValue"));
        paramMap.put("nmsAssetAspid",request.getParameter("nmsAssetAspid"));
        paramMap.put("nmsAssetName",request.getParameter("nmsAssetName"));
        paramMap.put("nmsAssetType",request.getParameter("nmsAssetType"));
        paramMap.put("nmsManageName",request.getParameter("nmsManageName"));
        paramMap.put("nmsManageUrl",request.getParameter("nmsManageUrl"));
        paramMap.put("begin",request.getParameter("begin"));
        paramMap.put("offset",request.getParameter("offset"));
        PageBean<NmsTriManagement> page = new PageBean<NmsTriManagement>();
        try {
            // 获取三方管理端信息列表
            page = nmsTriManagementService.getPageByConditionDate(paramMap);
            // 添加操作日志
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "查看三方管理端列表", "三方管理", "查询数据", "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return page;
    }

    /**
     * 添加三方管理端信息
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/addManagement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> addManagement(HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, String> data = new HashMap<String, String>();
        String aNo = request.getParameter("ANo");
        String managementName = request.getParameter("manageName");
        String managementUrl = request.getParameter("manageUrl");
        // 非空校验
        if (StringUtils.isEmpty(managementName)) {
            data.put("state", "1");
            data.put("info", "管理端名称不能为空！");
            return data;
        }
        if (StringUtils.isEmpty(managementUrl)) {
            data.put("state", "1");
            data.put("info", "管理端地址不能为空！");
            return data;
        }
        //根据ASPID获取资产信息，若不存在则验证不通过
        NmsAsset asset = nmsAssetService.findByANo(aNo);
        if (asset == null) {
            data.put("state", "1");
            data.put("info", "ASP卡ID无效，请重新输入！");
            return data;
        }

        NmsTriManagement nmsTriManagement = new NmsTriManagement();
        nmsTriManagement.setManageName(managementName);
        nmsTriManagement.setManageUrl(managementUrl);
        nmsTriManagement.setNmsAsset(asset);
        boolean bool = nmsTriManagementService.saveManagement(nmsTriManagement);

        if (bool) {
            data.put("state", "0");
            data.put("info", "添加成功!");
        } else {
			data.put("state", "1");
			data.put("info", "添加失败!");
        }
        auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                "添加三方管理端：" + nmsTriManagement.getManageName() + ", ASP卡ID：" + asset.getANo(), "三方管理", "新增数据", bool ? "成功" : "失败");
		return data;
    }

    /**
     * 修改三方管理端信息
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateTriManageMent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateTriManageMent(HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, String> data = new HashMap<String, String>();
        int id = Integer.valueOf(request.getParameter("id"));
        String managementName = request.getParameter("manageName");
        String managementUrl = request.getParameter("manageUrl");
        // 非空校验
        if (StringUtils.isEmpty(managementName)) {
            data.put("state", "1");
            data.put("info", "管理端名称不能为空！");
            return data;
        }
        if (StringUtils.isEmpty(managementUrl)) {
            data.put("state", "1");
            data.put("info", "管理端地址不能为空！");
            return data;
        }
        // 根据ID获取三方管理端信息
        NmsTriManagement management = nmsTriManagementService.findById(id);
        management.setManageName(managementName);
        management.setManageUrl(managementUrl);
        // 更新三方管理端信息
        boolean bool = nmsTriManagementService.updateManagement(management);
        String msg = bool ? "修改成功！" : "修改失败！";
        auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                "更新三方管理端：" + management.getManageName() + ", ASP卡ID：" + management.getNmsAsset().getANo(), "三方管理", "更新数据", bool ? "成功" : "失败");

        data.put("state", bool ? "1" : "0");
        data.put("info", bool ? "修改成功！" : "修改失败！");
        return data;
    }

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}

    /**
     * 根据id获取详细信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/findByIdToUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsTriManagement findByIdToUpdate(HttpServletRequest request) {
    	String idStr = request.getParameter("id") + "";
    	if (!isNumber(idStr)) {
    		return null;
    	}
        int id = Integer.valueOf(idStr);
        NmsTriManagement triManagement = nmsTriManagementService.findById(id);
        return triManagement;
    }


    /**
     * 根据id获取详细信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/findByAspId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAsset findByAspId(HttpServletRequest request) {
        String aspId = request.getParameter("aNo") + "";
        if (StringUtils.isEmpty(aspId)) {
            return null;
        }
        return nmsTriManagementService.findByAspId(aspId);
    }

    /**
     * 删除三方管理端信息（逻辑删除）
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/list/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean listDelete(HttpServletRequest request, HttpSession session) {
        boolean result = true;
        try {
            int id = Integer.valueOf(request.getParameter("id"));
            // 获取三方管理端信息
            NmsTriManagement management = nmsTriManagementService.findById(id);
            // 删除三方管理端信息
            result = nmsTriManagementService.deleteManagement(id);
            String msg = result ? "成功" : "失败";
            // 登记日志信息
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(),
                    "删除三方管理端：" + management.getManageName() + ", ASP卡ID：" + management.getNmsAsset().getANo(), "三方管理", "删除数据", msg);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
