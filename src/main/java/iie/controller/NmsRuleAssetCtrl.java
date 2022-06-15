package iie.controller;

import java.util.regex.Pattern;

import iie.pojo.NmsAdmin;
import iie.service.NmsAuditLogService;
import iie.service.NmsRuleAssetService;
import iie.tools.NmsRuleAssetItem;
import iie.tools.NmsRuleItem;
import iie.tools.PageBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/RuleAsset")
public class NmsRuleAssetCtrl {

	@Autowired
	NmsRuleAssetService nmsRuleAssetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		Integer assetId = Integer.valueOf(request.getParameter("assetId"));
		Integer assetTypeId = Integer.valueOf(request
				.getParameter("assetTypeId"));
		boolean ifSuccess = nmsRuleAssetService.add(assetId, assetTypeId);
		return ifSuccess;
	}

	@RequestMapping(value = "/findNmsRuleAsset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsRuleAssetItem> findNmsRules(HttpServletRequest request, HttpSession session) {
		String ip = request.getParameter("ip");
		Integer assetTypeId = Integer.valueOf(request
				.getParameter("assetTypeId"));
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
		
		if (ruleContent != null) {
			ruleContent = StringEscapeUtils.escapeSql(ruleContent);
		}	
		
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看资产阈值列表", "阈值管理", "查询数据", "成功");	
		
		return nmsRuleAssetService.findNmsRuleAsset(ip, assetTypeId,
				ruleContent, begin, offset, orderKey, orderValue);
	}

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}
	
	@RequestMapping(value = "/findNmsRuleAssetById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsRuleAssetItem findNmsRuleAssetById(HttpServletRequest request) {
    	String idStr = request.getParameter("id") + "";
    	if (!isNumber(idStr)) {
    		return null;
    	}
        int id = Integer.valueOf(idStr);
		return nmsRuleAssetService.findNmsRuleAssetById(id);
	}

	@RequestMapping(value = "/updateNmsRuleAssetById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean updateNmsRuleAssetById(
			HttpServletRequest request, HttpSession session) {
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
				request.getRemoteAddr(), "修改了"+ip+"网元阈值 | " + aType + " | " + content, "阈值管理", "修改数据", "成功");
		
		return nmsRuleAssetService.updateNmsRuleAssetById(id, rEnable, rSeq, rValue1,
				rValue2, rValue3);
	}
	@RequestMapping(value = "/updateNmsRuleAssetMonitorById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean updateNmsRuleAssetMonitorById(
			HttpServletRequest request, HttpSession session) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Integer rEnable = Integer.valueOf(request.getParameter("rEnable"));
		NmsRuleAssetItem ruleAssetItem = nmsRuleAssetService.findNmsRuleAssetById(id);
		ruleAssetItem.setrEnable(rEnable);
		String begin = "启用";
		if(rEnable==1) {
			begin = "启用";
		}else if(rEnable==0) {
			begin = "禁用";
		}
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), begin+"资产告警策略", "阈值管理", "修改数据", "成功");
		
		return nmsRuleAssetService.updateNmsRuleAssetById(id, rEnable, ruleAssetItem.getrSeq(), Integer.valueOf(ruleAssetItem.getrValue1().toString()),
				Integer.valueOf(ruleAssetItem.getrValue2().toString()), Integer.valueOf(ruleAssetItem.getrValue3().toString()));
	}

}
