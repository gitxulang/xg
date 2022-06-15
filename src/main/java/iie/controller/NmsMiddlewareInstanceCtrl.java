package iie.controller;

import iie.pojo.NmsMiddlewareInstance;
import iie.service.NmsMiddlewareInstanceService;
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
 * @date 2020/7/16 - 20:25
 */
@Controller
@RequestMapping("/MiddlewareInstance")
public class NmsMiddlewareInstanceCtrl {
    @Autowired
    NmsMiddlewareInstanceService miService;

    @RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsMiddlewareInstance> listPageByDate(HttpServletRequest request, HttpSession session) throws Exception {
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
        String configId = request.getParameter("configId");

        PageBean<NmsMiddlewareInstance> page = miService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, configId);

        //todo: 日志记录 by xczhao

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
        String configId = request.getParameter("configId");

        List<NmsMiddlewareInstance> list;

        try {
            list = miService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, configId);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("实例名称");
            namesOfHeader.add("实例IP");
            namesOfHeader.add("实例域名");
            namesOfHeader.add("实例端口");
            namesOfHeader.add("采集时间");

            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("name");
            namesOfField.add("ip");
            namesOfField.add("domain");
            namesOfField.add("listenPorts");
            namesOfField.add("itime");

            String docName = "专用中间件基础信息报表";
            List<Map> varList = new ArrayList<Map>();
            for (NmsMiddlewareInstance var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", var.getName());
                vpd.put("var2", var.getIp());
                vpd.put("var3", var.getDomain());
                vpd.put("var4", var.getListenPorts());
                Timestamp itime = var.getItime();
                String iTimeStr = "--";
                if (null != itime) {
                    iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
                }
                vpd.put("var5", iTimeStr);
                varList.add(vpd);
            }
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            //todo: 日志记录 by xczhao

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
