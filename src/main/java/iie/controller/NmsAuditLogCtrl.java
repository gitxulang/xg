package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAuditLog;
import iie.service.NmsAuditLogService;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/AuditLog")
public class NmsAuditLogCtrl {

    @Autowired
    NmsAuditLogService nalService;

    @Autowired
    NmsAuditLogService auditLogService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void exportExcel(HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "itime";
            orderValue = "0";
        }

        String username = request.getParameter("username");
        String ip = request.getParameter("ip");
        String content = request.getParameter("content");

        String modname = request.getParameter("modname");
        String logtype = request.getParameter("logtype");
        String logrest = request.getParameter("logrest");

        String atimeStart = request.getParameter("itime_start");
        String atimeEnd = request.getParameter("itime_end");

        if (username != null) {
            username = new String(request.getParameter("username").getBytes("ISO-8859-1"), "UTF-8");
            username = StringEscapeUtils.escapeSql(username);
        }

        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }

        if (content != null) {
            content = StringEscapeUtils.escapeSql(content);
        }

        if (modname != null) {
            modname = StringEscapeUtils.escapeSql(modname);
        }


        if (logtype != null) {
            logtype = StringEscapeUtils.escapeSql(logtype);
        }

        if (logrest != null) {
            logrest = StringEscapeUtils.escapeSql(logrest);
        }

        if (atimeStart != null) {
            atimeStart = StringEscapeUtils.escapeSql(atimeStart);
        }

        if (atimeEnd != null) {
            atimeEnd = StringEscapeUtils.escapeSql(atimeEnd);
        }

        if (username != null) {
            username = StringEscapeUtils.escapeSql(username);
        }

        // 获取查询数据，在service层实现
        List<NmsAuditLog> list = nalService.getAllexportExcel(username, ip, content, modname, logtype, logrest, orderKey, orderValue,
                atimeStart, atimeEnd, (NmsAdmin) session.getAttribute("user"));
        String curUsername = ((NmsAdmin) session.getAttribute("user")).getRealname();
        String docName = "";
        if (curUsername.equals("secadm")) {
            docName = "审计员操作日志报表";
        } else {
            docName = "管理员操作日志报表";
        }

        List<String> namesOfHeader = new ArrayList<>();
        namesOfHeader.add("编号");
        namesOfHeader.add("用户名");
        namesOfHeader.add("IP地址/类别");
        namesOfHeader.add("操作内容");
        namesOfHeader.add("模块名称");
        namesOfHeader.add("类型");
        namesOfHeader.add("结果");
        namesOfHeader.add("操作时间");
        List<Map> varList = new ArrayList<Map>();
        for (int i = 0; i < list.size(); i++) {
            NmsAuditLog var = list.get(i);
            Map<String, String> vpd = new HashMap<>();
            vpd.put("var1", String.valueOf(i + 1));        // 1
            vpd.put("var2", var.getUsername());          // 2
            vpd.put("var3", var.getIp());                // 3
            vpd.put("var4", var.getContent());           // 4
            vpd.put("var5", var.getModname());           // 5
            vpd.put("var6", var.getLogtype());           // 6
            vpd.put("var7", var.getLogrest());           // 7
            vpd.put("var8", var.getATime());             // 8
            varList.add(vpd);
        }
        ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
        boolean b = excelHelper.buildExcelDocument().exportZip(request, response);
        if (b) {
            nalService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "导出审计日志成功", "审计日志", "导出文件", "成功");
        } else {
            nalService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "导出审计日志失败", "审计日志", "导出文件", "失败");
        }
    }

    @RequestMapping(value = "/list/find", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAuditLog> findList(HttpServletRequest request, HttpSession session)
            throws Exception {

        NmsAdmin admin = (NmsAdmin) session.getAttribute("user");
        if (admin == null) {
            return null;
        }

        String username = request.getParameter("username");
        String ip = request.getParameter("ip");
        String content = request.getParameter("content");

        String modname = request.getParameter("modname");
        String logtype = request.getParameter("logtype");
        String logrest = request.getParameter("logrest");

        String atimeStart = request.getParameter("itime_start");
        String atimeEnd = request.getParameter("itime_end");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        /*if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "itime";
            orderValue = "0";
        }*/

        if (username != null) {
            username = StringEscapeUtils.escapeSql(username);
        }
        if(admin.getNmsRole().getRole().equals("审计管理员") || admin.getNmsRole().getRole().equals("安全保密员")) {
        	username=null;
        }

        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }

        if (content != null) {
            content = StringEscapeUtils.escapeSql(content);
        }

        if (modname != null) {
            modname = StringEscapeUtils.escapeSql(modname);
        }

        if (logtype != null) {
            logtype = StringEscapeUtils.escapeSql(logtype);
        }

        if (logrest != null) {
            logrest = StringEscapeUtils.escapeSql(logrest);
        }

        if (atimeStart != null) {
            atimeStart = StringEscapeUtils.escapeSql(atimeStart);
        }
        if (atimeEnd != null) {
            atimeEnd = StringEscapeUtils.escapeSql(atimeEnd);
        }

        PageBean<NmsAuditLog> res = nalService.getListByCondition(username, ip, content, modname, logtype, logrest,
                atimeStart, atimeEnd, begin, offset, orderKey, orderValue, admin);

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看审计日志", "日志管理", "查询数据", "成功");

        return res;
    }
}
