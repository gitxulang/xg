package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsMiddlewareStatus;
import iie.pojo.NmsSoft;
import iie.service.NmsAuditLogService;
import iie.service.NmsMiddlewareStatusService;
import iie.service.NmsSoftService;
import iie.tools.PageBean;
import iie.tools.excel.DateUtil;
import iie.tools.excel.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author xczhao
 * @date 2020/7/13 - 22:21
 */
@Controller
@RequestMapping("/MiddlewareStatus")
public class NmsMiddlewareStatusCtrl {
    @Autowired
    NmsMiddlewareStatusService msService;

    @Autowired
    NmsSoftService softService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsMiddlewareStatus> listPageByDate(HttpServletRequest request, HttpSession session) throws Exception {
        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "itime";
            orderValue = "1";
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));
        String assetId = request.getParameter("assetId");

        PageBean<NmsMiddlewareStatus> page = msService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, assetId);

        if (assetId != null) {
            NmsSoft nmsAsset = softService.findById(Integer.valueOf(assetId));

            if (nmsAsset != null) {
                String name = nmsAsset.getAName();
                String ip = nmsAsset.getAIp();

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(),
                        "查看单个软件报表；状态信息 | " + name + " | " + ip,
                        "报表管理",
                        "查询数据",
                        "成功"
                );
            }
        }

        return page;
    }

    @RequestMapping(value = "/list/date/ExportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean listPageByDateExportExcel(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");

        if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "itime";
            orderValue = "0";
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String assetId = request.getParameter("assetId");

        List<NmsMiddlewareStatus> list;

        try {
            list = msService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, assetId);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("当前连接数");
            namesOfHeader.add("每秒请求数");
            namesOfHeader.add("最大内存量（KB）");
            namesOfHeader.add("已用内存量（KB）");
            namesOfHeader.add("堆内存量（KB）");
            namesOfHeader.add("已用堆内存量（KB）");
            namesOfHeader.add("非堆内存量（KB）");
            namesOfHeader.add("已用非堆内存量（KB）");
            namesOfHeader.add("当前JVM线程数（个）");
            namesOfHeader.add("采集时间");

            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("connNum");
            namesOfField.add("rps");
            namesOfField.add("memTotal");
            namesOfField.add("memUsed");
            namesOfField.add("heapTotal");
            namesOfField.add("heapUsed");
            namesOfField.add("nonHeapTotal");
            namesOfField.add("nonHeapUsed");
            namesOfField.add("jvmThreadNum");
            namesOfField.add("itime");

            NmsSoft asset = softService.findById(Integer.valueOf(assetId));
            String docName = "专用中间件状态信息报表" + asset.getAIp();
            List<Map> varList = new ArrayList<Map>();
            for (NmsMiddlewareStatus var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", String.valueOf(var.getConnNum()));
                vpd.put("var2", String.valueOf(var.getRps()));
                vpd.put("var3", String.valueOf(var.getMemTotal()));
                vpd.put("var4", String.valueOf(var.getMemUsed()));
                vpd.put("var5", String.valueOf(var.getHeapTotal()));
                vpd.put("var6", String.valueOf(var.getHeapUsed()));
                vpd.put("var7", String.valueOf(var.getNonHeapTotal()));
                vpd.put("var8", String.valueOf(var.getHeapUsed()));
                vpd.put("var9", String.valueOf(var.getJvmThreadNum()));
                Timestamp itime = var.getItime();
                String iTimeStr = "--";
                if (null != itime) {
                    iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
                }
                vpd.put("var10", iTimeStr);
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            if (b) {
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "导出专用中间件状态信息报表 | 软件名称：" + asset.getAName() + " | 软件IP及端口号：" + asset.getAIp() + ":" + asset.getAPort(), "监控管理", "导出文件", "成功");
            }

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
