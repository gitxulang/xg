package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsMiddlewareConfig;
import iie.pojo.NmsSoft;
import iie.service.NmsAuditLogService;
import iie.service.NmsMiddlewareConfigService;
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
 * @date 2020/7/13 - 21:17
 */
@Controller
@RequestMapping("/MiddlewareConfig")
public class NmsMiddlewareConfigCtrl {
    @Autowired
    NmsMiddlewareConfigService mcService;

    @Autowired
    NmsSoftService softService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsMiddlewareConfig> listPageByDate(HttpServletRequest request, HttpSession session) throws Exception {
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

        PageBean<NmsMiddlewareConfig> page = mcService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, assetId);

        if (assetId != null) {
            NmsSoft nmsAsset = softService.findById(Integer.valueOf(assetId));

            if (nmsAsset != null) {
                String name = nmsAsset.getAName();
                String ip = nmsAsset.getAIp();

                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(),
                        "??????????????????????????????????????? | " + name + " | " + ip,
                        "????????????",
                        "????????????",
                        "??????"
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

        List<NmsMiddlewareConfig> list;

        try {
            list = mcService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, assetId);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????HTTPS??????");
            namesOfHeader.add("??????????????????");
            namesOfHeader.add("???????????????");
            namesOfHeader.add("???????????????");
            namesOfHeader.add("??????JVM?????????");
            namesOfHeader.add("????????????");

            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("startTime");
            namesOfField.add("httpsProtocol");
            namesOfField.add("protocols");
            namesOfField.add("listenPorts");
            namesOfField.add("maxConnNum");
            namesOfField.add("maxHreadNum");
            namesOfField.add("itime");

            NmsSoft asset = softService.findById(Integer.valueOf(assetId));
            String docName = "?????????????????????????????????" + asset.getAIp();
            List<Map> varList = new ArrayList<Map>();
            for (NmsMiddlewareConfig var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", DateUtil.date2Str(new Date(var.getStartTime())));
                int httpsProtocol = var.getHttpsProtocol();
                String var2 = "--";
                if (httpsProtocol == 0) {
                    var2 = "?????????";
                } else {
                    var2 = "?????????";
                }
                vpd.put("var2", var2);
                vpd.put("var3", var.getProtocols());
                vpd.put("var4", var.getListenPorts());
                vpd.put("var5", String.valueOf(var.getMaxConnNum()));
                vpd.put("var6", String.valueOf(var.getMaxHreadNum()));
                Timestamp itime = var.getItime();
                String iTimeStr = "--";
                if (null != itime) {
                    iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
                }
                vpd.put("var7", iTimeStr);
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            if (b) {
                auditLogService.add(
                        ((NmsAdmin) session.getAttribute("user")).getRealname(),
                        request.getRemoteAddr(), "??????????????????????????????????????? | ???????????????" + asset.getAName() + " | ??????IP???????????????" + asset.getAIp() + ":" + asset.getAPort(), "????????????", "????????????", "??????");
            }

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
