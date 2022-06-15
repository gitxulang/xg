package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAlarm;
import iie.pojo.NmsAlarmReport;
import iie.pojo.NmsAsset;
import iie.service.NmsAlarmService;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.tools.*;
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

@Controller
@RequestMapping(value = "alarm")
public class NmsAlarmCtrl {

    @Autowired
    private NmsAlarmService nmsAlarmService;

    @Autowired
    private NmsAssetService assetService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAlarm findById(HttpServletRequest request) {
        if (request.getParameter("id") != null) {
            int id = Integer.valueOf(request.getParameter("id"));
            NmsAlarm alarm = nmsAlarmService.findById(id);
            return alarm;
        }
        return new NmsAlarm();
    }

    @RequestMapping(value = "/deal/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
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
        if (nmsAlarmService.updateAlarmDeal(id, dPeople)) {
            data.put("state", "0");
            data.put("info", "提交成功！");
        } else {
            data.put("state", "1");
            data.put("info", "提交失败！");
        }

        NmsAlarm alarm = nmsAlarmService.findById(id);
        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), dPeople + "记录了告警 | 告警来源：" + alarm.getNmsAsset().getAIp() + " | 告警描述：" + alarm.getAContent() + "，成功", "告警管理", "修改数据", "成功");

        return data;
    }

    @RequestMapping(value = "/deal/report", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
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
        if (nmsAlarmService.addAlarmReportAndUpdateAlarm(id, rPeople, rContent,
                dTime)) {
            data.put("state", "0");
            data.put("info", "提交成功！");
            NmsAlarm alarm = nmsAlarmService.findById(id);
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), rPeople + "处理了告警 | 告警来源：" + alarm.getNmsAsset().getAIp() + " | 告警描述：" + alarm.getAContent() + "，成功", "告警管理", "修改数据", "成功");
        } else {
            data.put("state", "1");
            data.put("info", "提交失败！");
            NmsAlarm alarm = nmsAlarmService.findById(id);
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), rPeople + "处理了告警 | 告警来源：" + alarm.getNmsAsset().getAIp() + " | 告警描述：" + alarm.getAContent() + "，失败", "告警管理", "修改数据", "失败");
        }
        return data;
    }

    @RequestMapping(value = "/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAlarmInfoDetail> listPageByCondition(
            HttpServletRequest request, HttpSession session) {
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
        String nmsAssetIdParameter = request.getParameter("nmsAssetId");
        String AIp = request.getParameter("AIp");
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

        if (AContent != null) {
            AContent = StringEscapeUtils.escapeSql(AContent);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        Integer ALevel = null;
        Integer nmsAssetId = null;
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
        if (nmsAssetIdParameter != null && nmsAssetIdParameter.length() > 0) {
            nmsAssetId = Integer.valueOf(nmsAssetIdParameter);
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
        if (nmsAssetId != null) {
            NmsAsset nmsAsset = assetService.findById(Integer.valueOf(nmsAssetId));
            if (nmsAsset != null) {
                String name = nmsAsset.getAName();
                String ip = nmsAsset.getAIp();
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "查看设备详情：告警信息 | " + name + " | " + ip, "监控管理", "查询数据", "成功");
            }
        } else {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "查看告警列表", "告警管理", "查询数据", "成功");
        }

        return nmsAlarmService.listAlarmPageByCondition(orderKey, orderValue,
                ALevel, nmsAssetId, AIp, DStatus, MStatus, AContent, AName, startDate,
                endDate, begin, offset);
    }

    @RequestMapping(value = "/query/rule", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> queryAlarmRule(HttpServletRequest request) {
        Integer alarmId = null;
        String idAlarmParameter = request.getParameter("id");
        if (idAlarmParameter != null && idAlarmParameter.length() > 0) {
            alarmId = Integer.valueOf(idAlarmParameter);
        }
        return nmsAlarmService.queryAlarmRule(alarmId);
    }

    @RequestMapping(value = "/report/list/page/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAlarmReport> reportListPageByCondtion(
            HttpServletRequest request) {
        String rPeople = request.getParameter("rPeople");
        String dTimeStart = request.getParameter("dTimeStart");
        String dTimeEnd = request.getParameter("dTimeEnd");
        String rTimeStart = request.getParameter("rTimeStart");
        String rTimeEnd = request.getParameter("rTimeEnd");
        String orderKey = request.getParameter("orderKey");
        String alarmIdParameter = request.getParameter("alarmId");
        String alarmReportIdParameter = request.getParameter("alarmReportId");
        String beginParameter = request.getParameter("begin");
        String offsetParameter = request.getParameter("offset");
        String orderValueParameter = request.getParameter("orderValue");
        int orderValue = 0;
        if (orderValueParameter != null && orderValueParameter.length() > 0) {
            orderValue = Integer.valueOf(orderValueParameter);
        }
        Integer alarmId = null;
        Integer alarmReportId = null;
        int begin = 1;
        int offset = 10;
        if (alarmIdParameter != null && alarmIdParameter.length() > 0) {
            alarmId = Integer.valueOf(alarmIdParameter);
        }
        if (alarmReportIdParameter != null
                && alarmReportIdParameter.length() > 0) {
            alarmReportId = Integer.valueOf(alarmReportIdParameter);
        }
        if (beginParameter != null && beginParameter.length() > 0) {
            begin = Integer.valueOf(beginParameter);
        }
        if (offsetParameter != null && offsetParameter.length() > 0) {
            offset = Integer.valueOf(alarmIdParameter);
        }

        if (rPeople != null) {
            rPeople = StringEscapeUtils.escapeSql(rPeople);
        }

        if (dTimeStart != null) {
            dTimeStart = StringEscapeUtils.escapeSql(dTimeStart);
        }

        if (dTimeEnd != null) {
            dTimeEnd = StringEscapeUtils.escapeSql(dTimeEnd);
        }


        if (rTimeStart != null) {
            rTimeStart = StringEscapeUtils.escapeSql(rTimeStart);
        }

        if (rTimeEnd != null) {
            rTimeEnd = StringEscapeUtils.escapeSql(rTimeEnd);
        }

        return nmsAlarmService.listAlarmReportByCondition(orderKey, orderValue,
                alarmId, alarmReportId, rPeople, dTimeStart, dTimeEnd,
                rTimeStart, rTimeEnd, begin, offset);
    }

    @RequestMapping(value = "/findByAlarmId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAlarmReport findByAlarmId(HttpServletRequest request) {
        String alarmIdParameter = request.getParameter("alarmId");
        Integer alarmId = null;
        if (alarmIdParameter != null && alarmIdParameter.length() > 0) {
            alarmId = Integer.valueOf(alarmIdParameter);
        }
        NmsAlarmReport obj = nmsAlarmService.findByAlarmId(alarmId);
        if (obj == null) {
            obj = new NmsAlarmReport();
        }
        return obj;
    }

    @RequestMapping(value = "/deleteAlarmById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> deleteAlarmById(HttpServletRequest request, HttpSession session) {
        String idstr = request.getParameter("id");
        List<String> list = Arrays.asList(idstr.split(","));
        boolean result = nmsAlarmService.deleteAlarm(list);

        Map<String, String> data = new HashMap<String, String>();
        String log = "";
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
            data.put("state", "0");
            data.put("info", "删除失败！");
        }

        return data;
    }


    @RequestMapping(value = "/list/statics/asset/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAlarmStaticsDetail> staticsNmsAssetAlarm(
            HttpServletRequest request, HttpSession session) {
        String nmsAssetName = request.getParameter("nmsAssetName");
        String nmsAssetIp = request.getParameter("nmsAssetIp");
        String nmsAssetTypeName = request.getParameter("nmsAssetTypeName");
        String alarmStartDate = request.getParameter("alarmStartDate");
        String alarmEndDate = request.getParameter("alarmEndDate");
        String nmsAssetIdParameter = request.getParameter("nmsAssetId");
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
        Integer nmsAssetId = null;
        Integer nmsAssetTypeId = null;
        Integer alarmLevel = null;
        if (nmsAssetIdParameter != null && nmsAssetIdParameter.length() > 0) {
            nmsAssetId = Integer.valueOf(nmsAssetIdParameter);
        }
        if (nmsAssetTypeIdParameter != null
                && nmsAssetTypeIdParameter.length() > 0) {
            nmsAssetTypeId = Integer.valueOf(nmsAssetTypeIdParameter);
        }
        if (alarmLevelParameter != null && alarmLevelParameter.length() > 0) {
            alarmLevel = Integer.valueOf(alarmLevelParameter);
        }

        if (nmsAssetName != null) {
            nmsAssetName = StringEscapeUtils.escapeSql(nmsAssetName);
        }

        if (nmsAssetIp != null) {
            nmsAssetIp = StringEscapeUtils.escapeSql(nmsAssetIp);
        }

        if (nmsAssetTypeName != null) {
            nmsAssetTypeName = StringEscapeUtils.escapeSql(nmsAssetTypeName);
        }

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看告警统计报表", "报表管理", "查询数据", "成功");

        return nmsAlarmService.staticsNmsAssetAlarm(nmsAssetName, nmsAssetIp,
                nmsAssetId, nmsAssetTypeName, nmsAssetTypeId, alarmLevel,
                alarmStartDate, alarmEndDate, begin, offset, orderKey, orderValue);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/list/page/condition/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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
        String nmsAssetIdParameter = request.getParameter("nmsAssetId");
        String AIp = request.getParameter("AIp");
        String DStatusParameter = request.getParameter("DStatus");
        String AContent = request.getParameter("AContent");
        String AName = request.getParameter("AName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Integer ALevel = null;
        Integer nmsAssetId = null;
        Integer DStatus = null;


        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (AContent != null) {
            AContent = StringEscapeUtils.escapeSql(AContent);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        if (nmsAssetIdParameter != null && nmsAssetIdParameter.length() > 0) {
            nmsAssetId = Integer.valueOf(nmsAssetIdParameter);
        }

        if (DStatusParameter != null && DStatusParameter.length() > 0) {
            DStatus = Integer.valueOf(DStatusParameter);
        }
        if (ALevelParameter != null && ALevelParameter.length() > 0) {
            ALevel = Integer.valueOf(ALevelParameter);
        }

        List<NmsAlarmInfoDetail> list;
        try {
            list = nmsAlarmService.listAlarmPageByConditionExportExcel(
                    orderKey, orderValue, ALevel, nmsAssetId, AIp, DStatus,
                    AContent, AName, startDate, endDate);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("编号");
            namesOfHeader.add("告警级别");
            namesOfHeader.add("IP");
            namesOfHeader.add("告警内容");
            namesOfHeader.add("告警时间");
            namesOfHeader.add("处理时间");
            namesOfHeader.add("状态");

            List<Map> varList = new ArrayList<Map>();
            for (int i = 0; i < list.size(); i++) {
                NmsAlarmInfoDetail var = list.get(i);
                Map<String, String> vpd = new HashMap<>();

                NmsAlarm nmsAlarm = var.getNmsAlarm();
                NmsAsset nmsAsset = nmsAlarm.getNmsAsset();
                vpd.put("var1", String.valueOf(i + 1));
                Integer aLevel = nmsAlarm.getALevel();
                String aLevelStr = "";
                if (aLevel == 1) {
                    aLevelStr = "低风险";
                } else if (aLevel == 2) {
                    aLevelStr = "中风险";
                } else if (aLevel == 3) {
                    aLevelStr = "高风险";
                } else {
                    aLevelStr = "其它";
                }
                vpd.put("var2", aLevelStr);
                String aIpStr = "--";
                if (null != nmsAsset) {
                    aIpStr = nmsAsset.getAIp();
                }
                vpd.put("var3", aIpStr);
                vpd.put("var4", nmsAlarm.getAContent());
                vpd.put("var5", nmsAlarm.getATime());
                vpd.put("var6", nmsAlarm.getDTime());
                int dStatusCode = nmsAlarm.getDStatus();
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
            ExcelHelper excelHelper = new ExcelHelper("告警详情报表", namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            if (b) {
                NmsAsset asset = assetService.findById(nmsAssetId);
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出单个设备告警报表 | 设备名称：" + asset.getAName() + " | IP地址：" + asset.getAIp(), "告警管理", "导出文件", "成功");
            }

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/list/statics/dept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, List<NmsAlarmDeptDetail>> staticsDeptNmsAlarm(
            HttpServletRequest request) {
        return nmsAlarmService.staticsDeptNmsAlarm();
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/list/statics/asset/condition/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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

        String nmsAssetName = request.getParameter("nmsAssetName");
        String nmsAssetIp = request.getParameter("nmsAssetIp");
        String nmsAssetTypeName = request.getParameter("nmsAssetTypeName");
        String alarmStartDate = request.getParameter("alarmStartDate");
        String alarmEndDate = request.getParameter("alarmEndDate");
        String nmsAssetIdParameter = request.getParameter("nmsAssetId");
        String nmsAssetTypeIdParameter = request.getParameter("nmsAssetTypeId");
        String alarmLevelParameter = request.getParameter("alarmLevel");
        Integer nmsAssetId = null;
        Integer nmsAssetTypeId = null;
        Integer alarmLevel = null;
        if (nmsAssetIdParameter != null && nmsAssetIdParameter.length() > 0) {
            nmsAssetId = Integer.valueOf(nmsAssetIdParameter);
        }
        if (nmsAssetTypeIdParameter != null
                && nmsAssetTypeIdParameter.length() > 0) {
            nmsAssetTypeId = Integer.valueOf(nmsAssetTypeIdParameter);
        }
        if (alarmLevelParameter != null && alarmLevelParameter.length() > 0) {
            alarmLevel = Integer.valueOf(alarmLevelParameter);
        }


        if (nmsAssetName != null) {
            nmsAssetName = StringEscapeUtils.escapeSql(nmsAssetName);
        }

        if (nmsAssetIp != null) {
            nmsAssetIp = StringEscapeUtils.escapeSql(nmsAssetIp);
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

        List<NmsAlarmStaticsDetail> list;
        try {
            list = nmsAlarmService
                    .staticsNmsAssetAlarmExportExcel(nmsAssetName, nmsAssetIp,
                            nmsAssetId, nmsAssetTypeName, nmsAssetTypeId,
                            alarmLevel, alarmStartDate, alarmEndDate, orderKey, orderValue);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("IP");
            namesOfHeader.add("名称");
            namesOfHeader.add("类型/类别");
            namesOfHeader.add("待处理（个）");
            namesOfHeader.add("处理中（个）");
            namesOfHeader.add("已处理（个）");
            namesOfHeader.add("告警总数（个）");

            /* namesOfHeader.add("操作"); */
            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("nmsAssetIp");
            namesOfField.add("nmsAssetName");
            namesOfField.add("nmsAssetType");
            namesOfField.add("unDealTotalCount");
            namesOfField.add("dealingTotalCount");
            namesOfField.add("dealedTotalCount");
            namesOfField.add("alarmTotalCount");

            List<Map> varList = new ArrayList<Map>();
            for (NmsAlarmStaticsDetail var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", var.getNmsAssetIp());
                vpd.put("var2", var.getNmsAssetName());
                vpd.put("var3", var.getNmsAssetType());
                vpd.put("var4", var.getUnDealTotalCount() + "");
                vpd.put("var5", var.getDealingTotalCount() + "");
                vpd.put("var6", var.getDealedTotalCount() + "");
                vpd.put("var7", var.getAlarmTotalCount() + "");
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper("告警统计报表", namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            if (b) {
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出告警统计报表", "告警管理", "导出文件", "成功");
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/list/alarm/condition/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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

        String AName = request.getParameter("AName");
        String AIp = request.getParameter("AIp");
        String ALevelParameter = request.getParameter("ALevel");
        String DStatusParameter = request.getParameter("DStatus");
        String AContent = request.getParameter("AContent");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
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
            list = nmsAlarmService.listPageByConditionExportExcel(orderKey, orderValue,
                    AIp, AName, ALevel, DStatus, AContent, startDate, endDate);

            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("IP");
            namesOfHeader.add("告警内容");
            namesOfHeader.add("首次告警时间");
            namesOfHeader.add("末次告警时间");
            namesOfHeader.add("告警累加次数");
            namesOfHeader.add("处置时间");
            namesOfHeader.add("风险等级");
            namesOfHeader.add("状态");

            /* namesOfHeader.add("操作"); */
            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("ip");
            namesOfField.add("content");
            namesOfField.add("stime");
            namesOfField.add("atime");
            namesOfField.add("acount");
            namesOfField.add("dtime");
            namesOfField.add("level");
            namesOfField.add("status");
            List<Map> varList = new ArrayList<Map>();
            for (NmsAlarmDetail var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", var.getIp());
                vpd.put("var2", var.getContent());
                vpd.put("var3", var.getStime());
                vpd.put("var4", var.getAtime());
                vpd.put("var5", var.getAcount());
                vpd.put("var6", var.getDtime()==null?"--":var.getDtime());
                vpd.put("var7", var.getLevel());
                vpd.put("var8", var.getStatus());
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper("告警数据报表", namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);
            if (b) {
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出告警数据报表", "告警管理", "导出文件", "成功");
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
