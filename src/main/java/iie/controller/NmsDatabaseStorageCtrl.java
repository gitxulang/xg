package iie.controller;

import iie.pojo.NmsDatabaseStorage;
import iie.service.NmsDatabaseStorageService;
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
 * @date 2020/7/23 - 18:38
 */
@Controller
@RequestMapping("/DatabaseStorage")
public class NmsDatabaseStorageCtrl {
    @Autowired
    NmsDatabaseStorageService dsService;

    @RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsDatabaseStorage> listPageByDate(HttpServletRequest request, HttpSession session) throws Exception {
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
        String statusId = request.getParameter("statusId");

        PageBean<NmsDatabaseStorage> page = dsService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, statusId);

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
        String statusId = request.getParameter("statusId");

        List<NmsDatabaseStorage> list;

        try {
            list = dsService.getPageByDateExportExcel(orderKey, orderValue, startDate, endDate, statusId);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("存储数据路径");
            namesOfHeader.add("总存储容量");
            namesOfHeader.add("已使用存储容量");
            namesOfHeader.add("采集时间");

            List<String> namesOfField = new ArrayList<>();
            namesOfField.add("path");
            namesOfField.add("totalSize");
            namesOfField.add("usedSize");
            namesOfField.add("itime");

            String docName = "专用中间件存储信息报表";
            List<Map> varList = new ArrayList<Map>();
            for (NmsDatabaseStorage var : list) {
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", var.getPath());
                vpd.put("var2", String.valueOf(var.getTotalSize()));
                vpd.put("var3", String.valueOf(var.getUsedSize()));
                Timestamp itime = var.getItime();
                String iTimeStr = "--";
                if (null != itime) {
                    iTimeStr = DateUtil.date2Str(new Date(itime.getTime()));
                }
                vpd.put("var4", iTimeStr);
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
