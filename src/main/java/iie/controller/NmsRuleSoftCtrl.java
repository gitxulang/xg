package iie.controller;

import java.util.regex.Pattern;

import iie.pojo.NmsAdmin;
import iie.service.NmsAuditLogService;
import iie.service.NmsRuleSoftService;
import iie.tools.NmsRuleAssetItem;
import iie.tools.NmsRuleSoftItem;
import iie.tools.PageBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xczhao
 * @date 2020/10/18 - 22:22
 */
@Controller
@RequestMapping("/RuleSoft")
public class NmsRuleSoftCtrl {
    @Autowired
    NmsRuleSoftService nmsRuleSoftService;

    @Autowired
    NmsAuditLogService auditLogService;

    @RequestMapping(value = "/findNmsRuleSoft", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsRuleSoftItem> findNmsRules(HttpServletRequest request, HttpSession session) {
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        Integer assetTypeId = Integer.valueOf(request.getParameter("assetTypeId"));
        String ruleContent = request.getParameter("ruleContent");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");

        if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
            orderKey = "id";
            orderValue = "0";
        }

        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }

        if (port != null) {
            port = StringEscapeUtils.escapeSql(port);
        }

        if (ruleContent != null) {
            ruleContent = StringEscapeUtils.escapeSql(ruleContent);
        }

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "查看软件阈值列表", "阈值管理", "查询数据", "成功");

        return nmsRuleSoftService.findNmsRuleSoft(ip, port, assetTypeId,
                ruleContent, begin, offset, orderKey, orderValue);
    }

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}
	
    @RequestMapping(value = "/findNmsRuleSoftById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsRuleSoftItem findNmsRuleSoftById(HttpServletRequest request) {
    	String idStr = request.getParameter("id") + "";
    	if (!isNumber(idStr)) {
    		return null;
    	}
        int id = Integer.valueOf(idStr);
        return nmsRuleSoftService.findNmsRuleSoftById(id);
    }

    @RequestMapping(value = "/updateNmsRuleSoftById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean updateNmsRuleSoftById(HttpServletRequest request, HttpSession session) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        Integer rEnable = Integer.valueOf(request.getParameter("rEnable"));
        Integer rSeq = Integer.valueOf(request.getParameter("rSeq"));
        Integer rValue1 = Integer.valueOf(request.getParameter("rValue1"));
        Integer rValue2 = Integer.valueOf(request.getParameter("rValue2"));
        Integer rValue3 = Integer.valueOf(request.getParameter("rValue3"));
        String content = request.getParameter("content");
        String aType = request.getParameter("aType");
        String ip = request.getParameter("ip");

        if (aType != null) {
            aType = StringEscapeUtils.escapeSql(aType);
        }

        if (content != null) {
            content = StringEscapeUtils.escapeSql(content);
        }

        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "修改了" + ip + "网元阈值 | " + aType + " | " + content, "阈值管理", "修改数据", "成功");

        return nmsRuleSoftService.updateNmsRuleSoftById(id, rEnable, rSeq, rValue1, rValue2, rValue3);
    }
}
