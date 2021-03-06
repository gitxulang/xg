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

        // ??????id????????????????????????ID???????????????????????????????????????
        NmsSoft oldSoft = nmsSoftService.findById(id);
        boolean flag = false;
        if (oldSoft != null) {
            // ??????IP????????????????????????
            if (!AIp.equals(oldSoft.getAIp())) {
                return "??????IP??????" + oldSoft.getAIp() + "????????????????????????????????????";
            }

            // ?????????????????????????????????
            if (!APort.equals(oldSoft.getAPort())) {
                return "???????????????" + oldSoft.getAPort() + "????????????????????????????????????";
            }

            // ??????????????????
            int oldNmsAssetTypeId = oldSoft.getNmsAssetType().getId();
            if (nmsAssetTypeId != oldNmsAssetTypeId) {
                System.out.println("[DEBUG] nmsAssetTypeId = " + nmsAssetTypeId + ", oldNmsAssetTypeId = " + oldNmsAssetTypeId);
                flag = true;
            }
        }

        boolean bool = nmsSoftService.updateSoft(soft);
        if (bool) {
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "????????????: " + AIp + ":" + APort, "??????????????????", "????????????", "??????");
            // ??????????????????????????????????????????????????????, ????????????nms_rule_asset???
            if (flag) {
                nmsSoftService.updateRuleSoft(soft);
            }
        }

        if (!bool) {
            return "???????????????";
        }
        return "???????????????";
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

        // ????????????????????????????????????????????????
        NmsLicense licenseObj = licenseService.getLastLicense();
        if (licenseObj == null) {
            return "???????????????????????????????????????";
        }

        DesUtil des = new DesUtil();
        String ret = null;
        try {
            ret = des.decrypt(licenseObj.getLicense());
        } catch (Exception e) {
            return "?????????????????????????????????????????????????????????";
        }

        String[] arr = ret.split("#");
        if (arr.length != 5) {
            return "??????????????????????????????????????????????????????????????????";
        }

        // ????????????computer????????????
        if (arr[2] == null || arr[2].equals("")) {
            return "???????????????????????????????????????????????????????????????";
        }
        int licenseComputer = Integer.valueOf(arr[2]);
        int systemComputer = nmsSoftService.findAll();

        System.out.println("licenseComputer = " + licenseComputer + ", systemComputer = " + systemComputer);
        if (systemComputer >= licenseComputer) {
            return "??????????????? " + systemComputer + " ????????????, ???????????? " + licenseComputer + " ???????????????";
        }
        // ????????????????????????????????????????????????

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
            return "??????IP?????????????????????";
        }

        if (APort == null || APort.equals("")) {
            return "??????????????????????????????";
        }

        if (nmsSoftService.ifIpOrPort(AIp, APort)) {
            return "????????????IP:?????????????????????: " + AIp + ", ???????????????";
        }

        boolean bool = nmsSoftService.saveSoftAndRuleSoft(soft);
        if (bool) {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "????????????: " + AIp + ":" + APort, "??????????????????", "????????????", "??????");
        } else {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "????????????: " + AIp + ":" + APort, "??????????????????", "????????????", "??????");
            return "????????????!";
        }

        return "????????????!";
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

                // ?????????????????????, ???????????????nms_rule_soft
                result = result && nmsRuleSoftService.deleteBySoftId(list.get(i));

                // ?????????????????????deled=1????????????, ????????????nms_soft,nms_alarm,nms_alarm_report????????????????????????
                result = result && nmsSoftService.deleteSoft(id);

                if (result) {
                    NmsSoft soft = nmsSoftService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                            request.getRemoteAddr(), "???????????????" + soft.getAIp() + ":" + soft.getAPort() + "??????", "??????????????????", "????????????", "??????");
                } else {
                    NmsSoft soft = nmsSoftService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                            request.getRemoteAddr(), "???????????????" + soft.getAIp() + ":" + soft.getAPort() + "??????", "??????????????????", "????????????", "??????");
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
                request.getRemoteAddr(), "????????????????????????", "??????????????????", "????????????", "??????");

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
                request.getRemoteAddr(), "????????????????????????", "????????????", "????????????", "??????");

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
            namesOfHeader.add("????????????");
            namesOfHeader.add("IP??????");
            namesOfHeader.add("?????????");
            namesOfHeader.add("??????/??????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("??????");

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
                            collModeStr = "????????????";
                            break;
                        case 1:
                            collModeStr = "ICMP??????";
                            break;
                        case 2:
                            collModeStr = "SNMP??????";
                            break;
                        case 3:
                            collModeStr = "JDBC??????";
                            break;
                        case 4:
                            collModeStr = "JMX??????";
                            break;
                    }
                }
                vpd.put("var7", collModeStr);
                Short colled = nmsSoft.getColled();
                String colledStr = "";
                if (colled == 0) {
                    colledStr = "?????????";
                } else {
                    colledStr = "?????????";
                }
                vpd.put("var8", colledStr);
                Timestamp itime = nmsSoft.getItime();
                String timeStr = String.valueOf(itime).replace(".0", "");
                vpd.put("var9", timeStr);
                varList.add(vpd);
            }

            String docName = "??????????????????";
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "????????????????????????", "????????????", "????????????", "??????");

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
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }
    }
}
