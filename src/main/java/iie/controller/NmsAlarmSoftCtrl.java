package iie.controller;

import iie.pojo.*;
import iie.service.NmsAlarmSoftService;
import iie.service.NmsAuditLogService;
import iie.service.NmsSoftService;
import iie.tools.NmsAlarmDetail;
import iie.tools.NmsAlarmSoftInfoDetail;
import iie.tools.NmsAlarmSoftStaticsDetail;
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
import java.util.*;

/**
 * @author xczhao
 * @date 2020/10/17 - 22:37
 */
@Controller
@RequestMapping(value = "alarm")
public class NmsAlarmSoftCtrl {
    @Autowired
    private NmsAlarmSoftService nmsAlarmSoftService;

    @Autowired
    private NmsSoftService softService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/list/statics/soft/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAlarmSoftStaticsDetail> staticsNmsSoftAlarm(
            HttpServletRequest request, HttpSession session) {

        String nmsSoftName = request.getParameter("name");
        String nmsSoftIp = request.getParameter("ip");
        String nmsSoftPort = request.getParameter("port");
        String nmsAssetTypeName = request.getParameter("nmsAssetTypeName");
        String alarmStartDate = request.getParameter("alarmStartDate");
        String alarmEndDate = request.getParameter("alarmEndDate");
        String nmsSoftIdParameter = request.getParameter("nmsSoftId");
        String nmsAssetTypeIdParameter = request.getParameter("nmsAssetTypeId");
        String alarmLevelParameter = request.getParameter("alarmLevel");

        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "id";
            orderValue = "0";
        }

        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        Integer nmsSoftId = null;
        Integer nmsAssetTypeId = null;
        Integer alarmLevel = null;
        if (nmsSoftIdParameter != null && nmsSoftIdParameter.length() > 0) {
            nmsSoftId = Integer.valueOf(nmsSoftIdParameter);
        }
        if (nmsAssetTypeIdParameter != null
                && nmsAssetTypeIdParameter.length() > 0) {
            nmsAssetTypeId = Integer.valueOf(nmsAssetTypeIdParameter);
        }
        if (alarmLevelParameter != null && alarmLevelParameter.length() > 0) {
            alarmLevel = Integer.valueOf(alarmLevelParameter);
        }

        if (nmsAssetTypeName != null) {
            nmsAssetTypeName = StringEscapeUtils.escapeSql(nmsAssetTypeName);
        }

        if (nmsSoftName != null) {
            nmsSoftName = StringEscapeUtils.escapeSql(nmsSoftName);
        }

        if (nmsSoftIp != null) {
            nmsSoftIp = StringEscapeUtils.escapeSql(nmsSoftIp);
        }

        if (nmsSoftPort != null) {
            nmsSoftPort = StringEscapeUtils.escapeSql(nmsSoftPort);
        }

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看软件告警统计报表", "报表管理", "查询数据", "成功");

        return nmsAlarmSoftService.staticsNmsSoftAlarm(nmsSoftName, nmsSoftIp,
                nmsSoftPort, nmsSoftId, nmsAssetTypeName, nmsAssetTypeId, alarmLevel,
                alarmStartDate, alarmEndDate, begin, offset, orderKey, orderValue);
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list/statics/soft/condition/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean staticsNmsAssetAlarmExportExcel(HttpServletRequest request,
                                                   HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

        int orderValue = 0;
        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");

        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "id";
            orderValue = 1;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String nmsSoftName = request.getParameter("name");
        String nmsSoftIp = request.getParameter("ip");
        String nmsSoftPort = request.getParameter("port");
        String nmsAssetTypeName = request.getParameter("nmsAssetTypeName");
        String alarmStartDate = request.getParameter("alarmStartDate");
        String alarmEndDate = request.getParameter("alarmEndDate");
        String nmsSoftIdParameter = request.getParameter("nmsSoftId");
        String nmsAssetTypeIdParameter = request.getParameter("nmsAssetTypeId");
        String alarmLevelParameter = request.getParameter("alarmLevel");
        Integer nmsSoftId = null;
        Integer nmsAssetTypeId = null;
        Integer alarmLevel = null;
        if (nmsSoftIdParameter != null && nmsSoftIdParameter.length() > 0) {
            nmsSoftId = Integer.valueOf(nmsSoftIdParameter);
        }
        if (nmsAssetTypeIdParameter != null
                && nmsAssetTypeIdParameter.length() > 0) {
            nmsAssetTypeId = Integer.valueOf(nmsAssetTypeIdParameter);
        }
        if (alarmLevelParameter != null && alarmLevelParameter.length() > 0) {
            alarmLevel = Integer.valueOf(alarmLevelParameter);
        }
        if (nmsSoftName != null) {
            nmsSoftName = StringEscapeUtils.escapeSql(nmsSoftName);
        }
        if (nmsSoftIp != null) {
            nmsSoftIp = StringEscapeUtils.escapeSql(nmsSoftIp);
        }
        if (nmsSoftPort != null) {
            nmsSoftPort = StringEscapeUtils.escapeSql(nmsSoftPort);
        }
        if (nmsAssetTypeName != null) {
            nmsAssetTypeName = StringEscapeUtils.escapeSql(nmsAssetTypeName);
        }
        if (alarmStartDate != null) {
            alarmStartDate = StringEscapeUtils.escapeSql(alarmStartDate);
        }
        if (alarmEndDate != null) {
            alarmEndDate = StringEscapeUtils.escapeSql(alarmEndDate);
        }
        List<NmsAlarmSoftStaticsDetail> list;
        try {
            list = nmsAlarmSoftService
                    .staticsNmsSoftAlarmExportExcel(nmsSoftName, nmsSoftIp, nmsSoftPort,
                            nmsSoftId, nmsAssetTypeName, nmsAssetTypeId,
                            alarmLevel, alarmStartDate, alarmEndDate, orderKey, orderValue);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("软件名称");
            namesOfHeader.add("类型/类别");
            namesOfHeader.add("IP地址");
            namesOfHeader.add("端口号");
            namesOfHeader.add("待处理（个）");
            namesOfHeader.add("处理中（个）");
            namesOfHeader.add("已处理（个）");
            namesOfHeader.add("告警总数（个）");

            /* namesOfHeader.add("操作"); */
            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("nmsSoftName");
            namesOfField.add("nmsAssetType");
            namesOfField.add("nmsSoftIp");
            namesOfField.add("nmsSoftPort");
            namesOfField.add("unDealTotalCount");
            namesOfField.add("dealingTotalCount");
            namesOfField.add("dealedTotalCount");
            namesOfField.add("alarmTotalCount");

            List<Map> varList = new ArrayList<Map>();
            for (NmsAlarmSoftStaticsDetail var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", var.getNmsSoftName());
                vpd.put("var2", var.getNmsAssetType());
                vpd.put("var3", var.getNmsSoftIp());
                vpd.put("var4", var.getNmsSoftPort());
                vpd.put("var5", var.getUnDealTotalCount() + "");
                vpd.put("var6", var.getDealingTotalCount() + "");
                vpd.put("var7", var.getDealedTotalCount() + "");
                vpd.put("var8", var.getAlarmTotalCount() + "");
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper("软件告警统计报表", namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            if (b) {
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出软件告警统计报表", "告警管理", "导出文件", "成功");
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list/page/soft/condition/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean listPageByConditionExportExcel(HttpServletRequest request,
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

        String ALevelParameter = request.getParameter("ALevel");
        String nmsSoftIdParameter = request.getParameter("nmsSoftId");
        String AIp = request.getParameter("AIp");
        String APort = request.getParameter("APort");
        String DStatusParameter = request.getParameter("DStatus");
        String AContent = request.getParameter("AContent");
        String AName = request.getParameter("AName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Integer ALevel = null;
        Integer nmsSoftId = null;
        Integer DStatus = null;

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (APort != null) {
            APort = StringEscapeUtils.escapeSql(APort);
        }

        if (AContent != null) {
            AContent = StringEscapeUtils.escapeSql(AContent);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        if (nmsSoftIdParameter != null && nmsSoftIdParameter.length() > 0) {
            nmsSoftId = Integer.valueOf(nmsSoftIdParameter);
        }

        if (DStatusParameter != null && DStatusParameter.length() > 0) {
            DStatus = Integer.valueOf(DStatusParameter);
        }
        if (ALevelParameter != null && ALevelParameter.length() > 0) {
            ALevel = Integer.valueOf(ALevelParameter);
        }

        List<NmsAlarmSoftInfoDetail> list;
        try {
            list = nmsAlarmSoftService.listAlarmPageByConditionExportExcel(
                    orderKey, orderValue, ALevel, nmsSoftId, AIp, APort, DStatus,
                    AContent, AName, startDate, endDate);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("序号");
            namesOfHeader.add("告警级别");
            namesOfHeader.add("软件地址");
            namesOfHeader.add("告警内容");
            namesOfHeader.add("告警时间");
            namesOfHeader.add("处理时间");
            namesOfHeader.add("状态");

            List<Map> varList = new ArrayList<Map>();
            for (int i = 0; i < list.size(); i++) {
                NmsAlarmSoftInfoDetail var = list.get(i);
                Map<String, String> vpd = new HashMap<>();

                NmsAlarmSoft nmsAlarmSoft = var.getNmsAlarmSoft();
                NmsSoft nmsSoft = nmsAlarmSoft.getNmsSoft();
                vpd.put("var1", String.valueOf(i + 1));
                Integer aLevel = nmsAlarmSoft.getALevel();
                String aLevelStr = "";
                if (aLevel == 1) {
                    aLevelStr = "一级告警";
                } else if (aLevel == 2) {
                    aLevelStr = "二级告警";
                } else if (aLevel == 3) {
                    aLevelStr = "三级告警";
                } else {
                    aLevelStr = "其它";
                }
                vpd.put("var2", aLevelStr);
                String aIpStr = "--";
                if (null != nmsSoft) {
                    aIpStr = nmsSoft.getAIp() + ":" + nmsSoft.getAPort();
                }
                vpd.put("var3", aIpStr);
                vpd.put("var4", nmsAlarmSoft.getAContent());
                vpd.put("var5", nmsAlarmSoft.getATime());
                vpd.put("var6", nmsAlarmSoft.getDTime());
                int dStatusCode = nmsAlarmSoft.getDStatus();
                String dStatusStr = "--";
                switch (dStatusCode) {
                    case 0:
                        dStatusStr = "待处理";
                        break;
                    case 1:
                        dStatusStr = "处理中";
                        break;
                    case 2:
                        dStatusStr = "已处理";
                        break;
                }
                vpd.put("var7", dStatusStr);
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper("软件告警详情报表", namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            if (b) {
                NmsSoft soft = softService.findById(nmsSoftId);
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出单个软件告警报表 | 设备名称：" + soft.getAName() + " | 软件地址：" + soft.getAIp() + ":" + soft.getAPort(), "告警管理", "导出文件", "成功");
            }

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list/alarm/soft/condition/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean listDataByConditionExportExcel(HttpServletRequest request,
                                                  HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");

        int orderValue = 0;
        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "itime";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String AIp = request.getParameter("AIp");
        String APort = request.getParameter("APort");
        String AName = request.getParameter("AName");
        String ALevelParameter = request.getParameter("ALevel");
        String DStatusParameter = request.getParameter("DStatus");
        String AContent = request.getParameter("AContent");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (APort != null) {
            APort = StringEscapeUtils.escapeSql(APort);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        if (AContent != null) {
            AContent = StringEscapeUtils.escapeSql(AContent);
        }

        Integer ALevel = null;
        Integer DStatus = null;

        if (DStatusParameter != null && DStatusParameter.length() > 0) {
            DStatus = Integer.valueOf(DStatusParameter);
        }

        if (ALevelParameter != null && ALevelParameter.length() > 0) {
            ALevel = Integer.valueOf(ALevelParameter);
        }

        List<NmsAlarmDetail> list;
        try {
            list = nmsAlarmSoftService.listPageByConditionExportExcel(orderKey, orderValue,
                    AIp, APort, AName, ALevel, DStatus, AContent, startDate, endDate);

            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("软件名称");
            namesOfHeader.add("IP地址");
            namesOfHeader.add("端口号");
            namesOfHeader.add("级别");
            namesOfHeader.add("告警内容");
            namesOfHeader.add("首次告警时间");
            namesOfHeader.add("末次告警时间");
            namesOfHeader.add("告警累加次数");
            namesOfHeader.add("处置时间");
            namesOfHeader.add("状态");

            /* namesOfHeader.add("操作"); */
            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("name");
            namesOfField.add("ip");
            namesOfField.add("port");
            namesOfField.add("level");
            namesOfField.add("content");
            namesOfField.add("stime");
            namesOfField.add("atime");
            namesOfField.add("acount");
            namesOfField.add("dtime");
            namesOfField.add("status");
            List<Map> varList = new ArrayList<Map>();
            for (NmsAlarmDetail var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", var.getName());
                vpd.put("var2", var.getIp());
                vpd.put("var3", var.getPort());
                vpd.put("var4", var.getLevel());
                vpd.put("var5", var.getContent());
                vpd.put("var6", var.getStime());
                vpd.put("var7", var.getAtime());
                vpd.put("var8", var.getAcount());
                vpd.put("var9", var.getDtime());
                vpd.put("var10", var.getStatus());
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper("软件告警数据报表", namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);
            if (b) {
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出软件告警数据报表", "告警管理", "导出文件", "成功");
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/soft/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAlarmSoftInfoDetail> listPageByCondition(HttpServletRequest request, HttpSession session) {

        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");

        int orderValue = 0;
        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "itime";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String ALevelParameter = request.getParameter("ALevel");
        String nmsSoftIdParameter = request.getParameter("nmsSoftId");
        String AIp = request.getParameter("AIp");
        String APort = request.getParameter("APort");
        String DStatusParameter = request.getParameter("DStatus");
        String MStatusParameter = request.getParameter("MStatus");
        String AContent = request.getParameter("AContent");
        String AName = request.getParameter("AName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String beginParameter = request.getParameter("begin");
        String offsetParameter = request.getParameter("offset");

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (APort != null) {
            APort = StringEscapeUtils.escapeSql(APort);
        }

        if (AContent != null) {
            AContent = StringEscapeUtils.escapeSql(AContent);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        Integer ALevel = null;
        Integer nmsSoftId = null;
        Integer begin = 1;
        Integer offset = 10;
        Integer DStatus = null;
        Short MStatus = null;
        if (beginParameter != null && beginParameter.length() > 0) {
            begin = Integer.valueOf(beginParameter);
        }
        if (offsetParameter != null && offsetParameter.length() > 0) {
            offset = Integer.valueOf(offsetParameter);
        }
        if (nmsSoftIdParameter != null && nmsSoftIdParameter.length() > 0) {
            nmsSoftId = Integer.valueOf(nmsSoftIdParameter);
        }
        if (DStatusParameter != null && DStatusParameter.length() > 0) {
            DStatus = Integer.valueOf(DStatusParameter);
        }
        if (MStatusParameter != null && MStatusParameter.length() > 0) {
            MStatus = Short.valueOf(MStatusParameter);
        }
        if (ALevelParameter != null && ALevelParameter.length() > 0) {
            ALevel = Integer.valueOf(ALevelParameter);
        }

        // 增加日志
        if (nmsSoftId != null) {
            NmsSoft nmsSoft = softService.findById(Integer.valueOf(nmsSoftId));
            if (nmsSoft != null) {
                String name = nmsSoft.getAName();
                String ip = nmsSoft.getAIp();
                String port = nmsSoft.getAPort();

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "查看软件详情：告警信息 | " + name + " | " + ip + ":" + port, "监控管理", "查询数据", "成功");
            }
        } else {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "查看软件告警列表", "告警管理", "查询数据", "成功");
        }

        return nmsAlarmSoftService.listAlarmPageByCondition(orderKey, orderValue, ALevel, nmsSoftId,
                AIp, APort, DStatus, MStatus, AContent, AName, startDate, endDate, begin, offset);
    }

    @RequestMapping(value = "/soft/deleteAlarmById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> deleteAlarmById(HttpServletRequest request, HttpSession session) {
        String idstr = request.getParameter("id");
        List<String> list = Arrays.asList(idstr.split(","));
        boolean result = nmsAlarmSoftService.deleteAlarm(list);

        String log = "";
        Map<String, String> data = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            log += (String) list.get(i);
            if (i < list.size() - 1) {
                log += ",";
            }
        }

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "清除告警标识ID为 [" + log + "] 成功", "告警管理", "删除数据", "成功");

        if (result == true) {
            data.put("state", "0");
            data.put("info", "删除成功！");
        } else {
            data.put("state", "1");
            data.put("info", "删除失败！");
        }

        return data;
    }

    @RequestMapping(value = "/soft/findByAlarmId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAlarmSoftReport findByAlarmId(HttpServletRequest request) {
        String alarmIdParameter = request.getParameter("alarmId");
        Integer alarmId = null;
        if (alarmIdParameter != null && alarmIdParameter.length() > 0) {
            alarmId = Integer.valueOf(alarmIdParameter);
        }
        NmsAlarmSoftReport obj = nmsAlarmSoftService.findByAlarmId(alarmId);
        if (obj == null) {
            obj = new NmsAlarmSoftReport();
        }
        return obj;
    }

    @RequestMapping(value = "/soft/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAlarmSoft findById(HttpServletRequest request) {
        if (request.getParameter("id") != null) {
            int id = Integer.valueOf(request.getParameter("id"));
            NmsAlarmSoft alarm = nmsAlarmSoftService.findById(id);
            return alarm;
        }
        return new NmsAlarmSoft();
    }

    @RequestMapping(value = "/soft/deal/report", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> dealAlarmReport(HttpServletRequest request, HttpSession session) {

        String idParameter = request.getParameter("id");
        String rContent = request.getParameter("rContent");
        String rPeople = request.getParameter("rPeople");
        String dTime = request.getParameter("dTime");
        Integer id = null;
        if (idParameter != null && idParameter.length() > 0) {
            id = Integer.parseInt(idParameter);
        }

        if (rContent != null) {
            rContent = StringEscapeUtils.escapeSql(rContent);
        }

        if (rPeople != null) {
            rPeople = StringEscapeUtils.escapeSql(rPeople);
        }

        if (dTime != null) {
            dTime = StringEscapeUtils.escapeSql(dTime);
        }

        Map<String, String> data = new HashMap<String, String>();
        if (nmsAlarmSoftService.addAlarmReportAndUpdateAlarm(id, rPeople, rContent, dTime)) {
            data.put("state", "0");
            data.put("info", "提交成功！");
            NmsAlarmSoft alarm = nmsAlarmSoftService.findById(id);
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), rPeople + "处理了告警 | 告警来源：" + alarm.getNmsSoft().getAIp() + " | 告警描述：" + alarm.getAContent() + "，成功", "告警管理", "新增数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "提交失败！");
            NmsAlarmSoft alarm = nmsAlarmSoftService.findById(id);
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), rPeople + "处理了告警 | 告警来源：" + alarm.getNmsSoft().getAIp() + " | 告警描述：" + alarm.getAContent() + "，失败", "告警管理", "新增数据", "成功");
        }
        return data;
    }

    @RequestMapping(value = "/soft/deal/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> dealAlarm(HttpServletRequest request, HttpSession session) {
        String dPeople = request.getParameter("dPeople");
        String idParameter = request.getParameter("id");
        Integer id = null;
        if (idParameter != null && idParameter.length() > 0) {
            id = Integer.parseInt(idParameter);
        }

        if (dPeople != null) {
            dPeople = StringEscapeUtils.escapeSql(dPeople);
        }

        Map<String, String> data = new HashMap<String, String>();
        if (nmsAlarmSoftService.updateAlarmDeal(id, dPeople)) {
            data.put("state", "0");
            data.put("info", "提交成功！");
        } else {
            data.put("state", "1");
            data.put("info", "提交失败！");
        }

        NmsAlarmSoft alarm = nmsAlarmSoftService.findById(id);
        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), dPeople+"记录了告警 | 告警来源："+alarm.getNmsSoft().getAIp()+" | 告警描述："+alarm.getAContent() + "，成功", "告警管理", "修改数据", "成功");

        return data;
    }
}
