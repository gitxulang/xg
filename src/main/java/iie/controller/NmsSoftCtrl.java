package iie.controller;

import iie.pojo.*;
import iie.service.NmsAuditLogService;
import iie.service.NmsLicenseService;
import iie.service.NmsRuleSoftService;
import iie.service.NmsSoftService;
import iie.tools.DesUtil;
import iie.tools.PageBean;
import iie.tools.excel.ExcelHelper;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xczhao
 * @date 2020/10/13 - 20:48
 */
@Controller
@RequestMapping(value = "/Soft")
public class NmsSoftCtrl {

    @Autowired
    NmsSoftService nmsSoftService;

    @Autowired
    NmsRuleSoftService nmsRuleSoftService;

    @Autowired
    NmsAuditLogService auditLogService;

    @Autowired
    NmsLicenseService licenseService;

    @RequestMapping(value = "/updateSoft", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateSoft(HttpServletRequest request, HttpSession session) throws Exception {
        int id = Integer.valueOf(request.getParameter("id"));
        String AIp = request.getParameter("AIp");
        String AManu = request.getParameter("AManu");
        String AName = request.getParameter("AName");
        String APort = request.getParameter("APort");
        String APos = request.getParameter("APos");
        String AUser = request.getParameter("AUser");
        String ADate = request.getParameter("ADate");
        Short colled = Short.valueOf(request.getParameter("colled"));
        Integer collMode = Integer.valueOf(request.getParameter("collMode"));
        String nmsAssetDepartmentId = request.getParameter("nmsAssetDepartmentId");
        int nmsAssetTypeId = Integer.valueOf(request.getParameter("nmsAssetTypeId"));

        NmsDepartment department = nmsSoftService.findDepartmentById(nmsAssetDepartmentId);
        NmsAssetType type = nmsSoftService.findTypeById(nmsAssetTypeId);

        NmsSoft soft = new NmsSoft();
        soft.setId(id);
        soft.setAIp(AIp);
        soft.setAPort(APort);
        soft.setColled(colled);
        soft.setNmsDepartment(department);
        soft.setNmsAssetType(type);
        if (AManu != null) {
            soft.setAManu(AManu);
        }
        if (AName != null) {
            soft.setAName(AName);
        }
        if (APos != null) {
            soft.setAPos(APos);
        }
        if (AUser != null) {
            soft.setAUser(AUser);
        }
        if (collMode != null) {
            soft.setCollMode(collMode);
        }
        if (ADate != null) {
            soft.setADate(ADate);
        }

        soft.setDeled(0);

        // 根据id值获取软件类别的ID用来判断该软件类别是否修改
        NmsSoft oldSoft = nmsSoftService.findById(id);
        boolean flag = false;
        if (oldSoft != null) {
            // 软件IP地址不允许被修改
            if (!AIp.equals(oldSoft.getAIp())) {
                return "软件IP地址" + oldSoft.getAIp() + "不允许被修改，修改失败！";
            }

            // 软件端口号不允许被修改
            if (!APort.equals(oldSoft.getAPort())) {
                return "软件端口号" + oldSoft.getAPort() + "不允许被修改，修改失败！";
            }

            // 判断类型修改
            int oldNmsAssetTypeId = oldSoft.getNmsAssetType().getId();
            if (nmsAssetTypeId != oldNmsAssetTypeId) {
                System.out.println("[DEBUG] nmsAssetTypeId = " + nmsAssetTypeId + ", oldNmsAssetTypeId = " + oldNmsAssetTypeId);
                flag = true;
            }
        }

        boolean bool = nmsSoftService.updateSoft(soft);
        if (bool) {
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "修改节点: " + AIp + ":" + APort, "软件资产管理", "修改数据", "成功");
            // 如果设备类别同时修改了需要更新规则表, 同时修改nms_rule_asset表
            if (flag) {
                nmsSoftService.updateRuleSoft(soft);
            }
        }

        if (!bool) {
            return "修改失败！";
        }
        return "修改成功！";
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/addSoft", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addSoft(HttpServletRequest request, HttpSession session) throws Exception {
        String AIp = request.getParameter("AIp");
        String AManu = request.getParameter("AManu");
        String AName = request.getParameter("AName");
        String APort = request.getParameter("APort");
        String APos = request.getParameter("APos");
        String AUser = request.getParameter("AUser");
        String ADate = request.getParameter("ADate");
        Short colled = Short.valueOf(request.getParameter("colled"));
        String collMode = request.getParameter("collMode");
        String nmsAssetDepartmentId = request.getParameter("nmsAssetDepartmentId");
        int nmsAssetTypeId = Integer.valueOf(request.getParameter("nmsAssetTypeId"));

        // 判断证书终端数是不是已经超过开始
        NmsLicense licenseObj = licenseService.getLastLicense();
        if (licenseObj == null) {
            return "系统未激活，不能添加设备！";
        }

        DesUtil des = new DesUtil();
        String ret = null;
        try {
            ret = des.decrypt(licenseObj.getLicense());
        } catch (Exception e) {
            return "证书序列号解析失败（非法证书序列号）！";
        }

        String[] arr = ret.split("#");
        if (arr.length != 5) {
            return "证书序列号解析失败（序列号参数数量不正确）！";
        }

        // 判断参数computer是否合法
        if (arr[2] == null || arr[2].equals("")) {
            return "证书序列号解析失败（非法限制终端数参数）！";
        }
        int licenseComputer = Integer.valueOf(arr[2]);
        int systemComputer = nmsSoftService.findAll();

        System.out.println("licenseComputer = " + licenseComputer + ", systemComputer = " + systemComputer);
        if (systemComputer >= licenseComputer) {
            return "当前已添加 " + systemComputer + " 个客户端, 超过授权 " + licenseComputer + " 个客户端！";
        }
        // 判断证书终端数是不是已经超过结束

        NmsDepartment department = nmsSoftService.findDepartmentById(nmsAssetDepartmentId);
        NmsAssetType type = nmsSoftService.findTypeById(nmsAssetTypeId);

        NmsSoft soft = new NmsSoft();
        soft.setAIp(AIp);
        soft.setAPort(APort);
        soft.setColled(colled);
        soft.setNmsDepartment(department);
        soft.setNmsAssetType(type);
        if (AManu != null) {
            soft.setAManu(AManu);
        }
        if (AName != null) {
            soft.setAName(AName);
        }
        if (APos != null) {
            soft.setAPos(APos);
        }
        if (AUser != null) {
            soft.setAUser(AUser);
        }
        if (collMode != null) {
            soft.setCollMode(Integer.valueOf(collMode));
        }
        if (ADate != null) {
            soft.setADate(ADate);
        }
        if (AIp == null || AIp.equals("")) {
            return "软件IP地址不能为空！";
        }

        if (APort == null || APort.equals("")) {
            return "软件端口号不能为空！";
        }

        if (nmsSoftService.ifIpOrPort(AIp, APort)) {
            return "存在相同IP:端口号网元节点: " + AIp + ", 无法添加！";
        }

        boolean bool = nmsSoftService.saveSoftAndRuleSoft(soft);
        if (bool) {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加节点: " + AIp + ":" + APort, "软件资产管理", "新增数据", "成功");
        } else {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加节点: " + AIp + ":" + APort, "软件资产管理", "新增数据", "失败");
            return "添加失败!";
        }

        return "添加成功!";
    }

    @RequestMapping(value = "/list/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean listDelete(HttpServletRequest request, HttpSession session) {
        String idstr = request.getParameter("id");
        List<String> list = Arrays.asList(idstr.split(","));
        boolean result = true;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }

                int id = 0;
                try {
                    id = Integer.valueOf(list.get(i));
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] list.get(i) is null, " + e.toString());
                    continue;
                }

                // 删除规则信息表, 做物理删除nms_rule_soft
                result = result && nmsRuleSoftService.deleteBySoftId(list.get(i));

                // 更新软件资产表deled=1逻辑删除, 重要信息nms_soft,nms_alarm,nms_alarm_report数据不做物理删除
                result = result && nmsSoftService.deleteSoft(id);

                if (result) {
                    NmsSoft soft = nmsSoftService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                            request.getRemoteAddr(), "删除节点：" + soft.getAIp() + ":" + soft.getAPort() + "成功", "软件资产管理", "删除数据", "成功");
                } else {
                    NmsSoft soft = nmsSoftService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                            request.getRemoteAddr(), "删除节点：" + soft.getAIp() + ":" + soft.getAPort() + "失败", "软件资产管理", "删除数据", "失败");
                }
            }
        }
        return result;
    }

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}

    @RequestMapping(value = "/findByIdToUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsSoft findByIdToUpdate(HttpServletRequest request) {
    	String idStr = request.getParameter("id") + "";
    	if (!isNumber(idStr)) {
    		return null;
    	}
        int id = Integer.valueOf(idStr);
        NmsSoft asset = nmsSoftService.findById(id);
        return asset;
    }

    @RequestMapping(value = "/loadLastSoft", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsSoft loadLastSoft(HttpServletRequest request)
            throws Exception {

        return nmsSoftService.loadLastSoft();
    }

    @RequestMapping(value = "/ifIpOrPort", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean ifIpOrPort(HttpServletRequest request) throws Exception {
        String ip = request.getParameter("AIp");
        String port = request.getParameter("APort");

        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }

        if (port != null) {
            port = StringEscapeUtils.escapeSql(port);
        }

        return nmsSoftService.ifIpOrPort(ip, port);
    }

    @RequestMapping(value = "/list/date/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsSoft> listPageByDateCondition(HttpServletRequest request, HttpSession session) throws Exception {
        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        if (orderKey == null || orderKey == "") {
            orderKey = "id";
        }

        int orderValue1 = 0;
        if (orderValue == null || orderValue == "") {
            orderValue1 = 0;
        } else {
            try {
                orderValue1 = Integer.valueOf(orderValue);
            } catch (NumberFormatException e) {
                orderValue1 = 0;
            }
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String nmsAssetKey = request.getParameter("nmsAssetKey");
        String nmsAssetValue = request.getParameter("nmsAssetValue");
        String nmsDepartmentKey = request.getParameter("nmsDepartmentKey");
        String nmsDepartmentValue = request.getParameter("nmsDepartmentValue");
        String nmsAssetTypeKey = request.getParameter("nmsAssetTypeKey");
        String nmsAssetTypeValue = request.getParameter("nmsAssetTypeValue");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        if (nmsAssetKey != null) {
            nmsAssetKey = StringEscapeUtils.escapeSql(nmsAssetKey);
        }

        if (nmsAssetValue != null) {
            nmsAssetValue = StringEscapeUtils.escapeSql(nmsAssetValue);
        }

        if (nmsDepartmentKey != null) {
            nmsDepartmentKey = StringEscapeUtils.escapeSql(nmsDepartmentKey);
        }

        if (nmsDepartmentValue != null) {
            nmsDepartmentValue = StringEscapeUtils.escapeSql(nmsDepartmentValue);
        }

        if (nmsAssetTypeKey != null) {
            nmsAssetTypeKey = StringEscapeUtils.escapeSql(nmsAssetTypeKey);
        }

        if (nmsAssetTypeValue != null) {
            nmsAssetTypeValue = StringEscapeUtils.escapeSql(nmsAssetTypeValue);
        }

        PageBean<NmsSoft> page = nmsSoftService.getPageByConditionDate(
                orderKey, orderValue1, startDate, endDate, nmsAssetKey,
                nmsAssetValue, nmsDepartmentKey, nmsDepartmentValue,
                nmsAssetTypeKey, nmsAssetTypeValue, begin, offset);

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看软件资产列表", "软件资产管理", "查询数据", "成功");

        return page;
    }

    @RequestMapping(value = "/list/date/reportSelect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsSoft> reportSelect(HttpServletRequest request, HttpSession session)
            throws Exception {

        String orderKey = request.getParameter("orderKey");
        int orderValue = 0;
        if (orderKey == null || orderKey.equals("")) {
            orderKey = "id";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(request.getParameter("orderValue"));
        }

        String startDate = request.getParameter("itime_start");
        String endDate = request.getParameter("itime_end");
        String AName = request.getParameter("a_name");
        String AIp = request.getParameter("a_ip");
        String APort = request.getParameter("a_port");
        String AType = request.getParameter("a_type");
        String ADept = request.getParameter("a_dept");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));


        if (startDate != null) {
            startDate = StringEscapeUtils.escapeSql(startDate);
        }

        if (endDate != null) {
            endDate = StringEscapeUtils.escapeSql(endDate);
        }

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (APort != null) {
            APort = StringEscapeUtils.escapeSql(APort);
        }

        if (AType != null) {
            AType = StringEscapeUtils.escapeSql(AType);
        }

        if (ADept != null) {
            ADept = StringEscapeUtils.escapeSql(ADept);
        }

        PageBean<NmsSoft> page = nmsSoftService.reportSelect(orderKey,
                orderValue, startDate, endDate, AName, AIp, APort, AType, ADept,
                begin, offset);

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看资产统计列表", "报表管理", "查询数据", "成功");

        return page;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsSoft findById(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        NmsSoft soft = nmsSoftService.findById(id);
        return soft;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/list/date/reportSelect/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean reportSelectExportExcel(HttpServletRequest request,
                                           HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        int orderValue = 0;
        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");
        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "id";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String startDate = request.getParameter("itime_start");
        String endDate = request.getParameter("itime_end");
        String AName = request.getParameter("a_name");
        String AIp = request.getParameter("a_ip");
        String APort = request.getParameter("a_port");
        String AType = request.getParameter("a_type");
        String ADept = request.getParameter("a_dept");

        if (startDate != null) {
            startDate = StringEscapeUtils.escapeSql(startDate);
        }

        if (endDate != null) {
            endDate = StringEscapeUtils.escapeSql(endDate);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (APort != null) {
            APort = StringEscapeUtils.escapeSql(APort);
        }

        if (AType != null) {
            AType = StringEscapeUtils.escapeSql(AType);
        }

        if (ADept != null) {
            ADept = StringEscapeUtils.escapeSql(ADept);
        }


        List<NmsSoft> list;
        try {
            list = nmsSoftService.reportSelectExportExcel(orderKey,
                    orderValue, startDate, endDate, AName, AIp, APort, AType, ADept);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("软件名称");
            namesOfHeader.add("IP地址");
            namesOfHeader.add("端口号");
            namesOfHeader.add("类型/类别");
            namesOfHeader.add("安装位置");
            namesOfHeader.add("所属部门");
            namesOfHeader.add("监控方式");
            namesOfHeader.add("是否监控");
            namesOfHeader.add("时间");

            List<Map> varList = new ArrayList<Map>();
            for (int i = 0; i < list.size(); i++) {
                NmsSoft nmsSoft = list.get(i);
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", nmsSoft.getAName());
                vpd.put("var2", nmsSoft.getAIp());
                vpd.put("var3", nmsSoft.getAPort());
                vpd.put("var4", nmsSoft.getNmsAssetType().getChSubtype());
                vpd.put("var5", nmsSoft.getAPos());
                vpd.put("var6", nmsSoft.getNmsDepartment().getDName());
                Integer collMode = nmsSoft.getCollMode();
                String collModeStr = "--";
                if (null != collMode) {
                    switch (collMode) {
                        case 0:
                            collModeStr = "专用代理";
                            break;
                        case 1:
                            collModeStr = "ICMP协议";
                            break;
                        case 2:
                            collModeStr = "SNMP协议";
                            break;
                        case 3:
                            collModeStr = "JDBC协议";
                            break;
                        case 4:
                            collModeStr = "JMX协议";
                            break;
                    }
                }
                vpd.put("var7", collModeStr);
                Short colled = nmsSoft.getColled();
                String colledStr = "";
                if (colled == 0) {
                    colledStr = "已监控";
                } else {
                    colledStr = "未监控";
                }
                vpd.put("var8", colledStr);
                Timestamp itime = nmsSoft.getItime();
                String timeStr = String.valueOf(itime).replace(".0", "");
                vpd.put("var9", timeStr);
                varList.add(vpd);
            }

            String docName = "软件统计报表";
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "导出软件资产报表", "资产管理", "导出文件", "成功");

            return true && b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @RequestMapping(value = "/exportTemplate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean exportTemplate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("/ui/assets/soft_import_template.xls");
    	System.out.println(path);
        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode("soft_import_template.xls", "UTF-8"));
        try {
			InputStream in = new FileInputStream(path);
			int count =0;
			byte[] by = new byte[4096];
			OutputStream out=  response.getOutputStream();
			while((count=in.read(by))!=-1){
				out.write(by, 0, count);
			}
			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			return false;
		}

        try {
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),request.getRemoteAddr(), "导出软件模板", "资产管理", "导出文件", "成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }
    }
}
