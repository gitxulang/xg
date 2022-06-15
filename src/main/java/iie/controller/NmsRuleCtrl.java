package iie.controller;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsRule;
import iie.service.NmsAssetTypeService;
import iie.service.NmsAuditLogService;
import iie.service.NmsRuleService;
import iie.tools.NmsExcelUtil;
import iie.tools.NmsRuleItem;
import iie.tools.PageBean;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Rule")
public class NmsRuleCtrl {

	@Autowired
	NmsRuleService nmsRuleService;

	@Autowired
	NmsAssetTypeService nmsAssetTypeService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/findNmsRulesByAssetTypeId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsRule> findNmsRulesByAssetTypeId(HttpServletRequest request) {
		Integer assetId = Integer.valueOf(request.getParameter("assetId"));
		List<NmsRule> list = nmsRuleService.findNmsRulesByAssetTypeId(assetId);
		return list;
	}

	@RequestMapping(value = "/findNmsRules", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsRuleItem> findNmsRules(HttpServletRequest request, HttpSession session) {
		Integer assetTypeId = Integer.valueOf(request.getParameter("assetTypeId"));
		String ruleContent = request.getParameter("ruleContent");
		int begin = Integer.valueOf(request.getParameter("begin"));
		int offset = Integer.valueOf(request.getParameter("offset"));
		
		String orderKey = request.getParameter("orderKey");
		String orderValue = request.getParameter("orderValue");

		if (ruleContent != null) {
			ruleContent = StringEscapeUtils.escapeSql(ruleContent);
		}	
		
		if (orderKey == null || orderKey.equals("") || orderValue == null || orderValue.equals("")) {
			orderKey = "id";
			orderValue = "0";
		}
		
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看模板阈值列表", "阈值管理", "查询数据", "成功");		
		
		return nmsRuleService.findNmsRules(assetTypeId, ruleContent, begin,
				offset, orderKey, orderValue);
	}

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}
	
	@RequestMapping(value = "/findNmsRuleById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsRuleItem findNmsRuleById(HttpServletRequest request) {
    	String idStr = request.getParameter("id") + "";
    	if (!isNumber(idStr)) {
    		return null;
    	}
        int id = Integer.valueOf(idStr);
		return nmsRuleService.findNmsRuleById(id);
	}

	@RequestMapping(value = "/updateNmsRuleById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean updateNmsRuleById(HttpServletRequest request, HttpSession session) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Integer rEnable = Integer.valueOf(request.getParameter("rEnable"));
		Integer rSeq = Integer.valueOf(request.getParameter("rSeq"));
		Integer rValue1 = Integer.valueOf(request.getParameter("rValue1"));
		Integer rValue2 = Integer.valueOf(request.getParameter("rValue2"));
		Integer rValue3 = Integer.valueOf(request.getParameter("rValue3"));
		String content = request.getParameter("content");
		String aType = request.getParameter("aType");
		
		if (content != null) {
			content = StringEscapeUtils.escapeSql(content);
		}	
		
		if (aType != null) {
			aType = StringEscapeUtils.escapeSql(aType);
		}	
		
		
		
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "修改了模板阈值 | " + aType + " | " + content, "阈值管理", "修改数据", "成功");
		
		return nmsRuleService.updateNmsRuleById(id, rEnable, rSeq, rValue1,
				rValue2, rValue3);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> add(HttpServletRequest request) throws Exception {

		Map<String, String> data = new HashMap<String, String>();
		NmsAssetType type = nmsAssetTypeService.findById(Integer
				.valueOf(request.getParameter("assetTypeId")));
		if (type == null) {
			data.put("state", "1");
			data.put("info", "资产类型id信息错误!");
			return data;
		}

		Integer dType = Integer.valueOf(request.getParameter("dType"));
		String rName = request.getParameter("rName");
		String rContent = request.getParameter("rContent");
		String rUnit = request.getParameter("rUnit");
		Integer rSeq = Integer.valueOf(request.getParameter("rSeq"));
		Integer rEnable = Integer.valueOf(request.getParameter("rEnable"));
		Long rValue1 = Long.valueOf(request.getParameter("rValue1"));
		Long rValue2 = Long.valueOf(request.getParameter("rValue2"));
		Long rValue3 = Long.valueOf(request.getParameter("rValue3"));

		if (rName != null) {
			rName = StringEscapeUtils.escapeSql(rName);
		}	
		
		if (rContent != null) {
			rContent = StringEscapeUtils.escapeSql(rContent);
		}	
		
		if (rUnit != null) {
			rUnit = StringEscapeUtils.escapeSql(rUnit);
		}	
		
		
		NmsRule nr = new NmsRule(type, dType, rName, rContent, rUnit, rSeq,
				rEnable, rValue1, rValue2, rValue3, new Timestamp(
						System.currentTimeMillis()));

		if (nmsRuleService.add(nr)) {
			data.put("state", "0");
			data.put("info", "添加成功!");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "添加失败!");
			return data;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> update(HttpServletRequest request)
			throws Exception {

		Map<String, String> data = new HashMap<String, String>();
		NmsAssetType type = nmsAssetTypeService.findById(Integer
				.valueOf(request.getParameter("assetTypeId")));
		if (type == null) {
			data.put("state", "1");
			data.put("info", "资产类型id信息错误!");
			return data;
		}

		Integer dType = Integer.valueOf(request.getParameter("dType"));
		String rName = request.getParameter("rName");
		String rContent = request.getParameter("rContent");
		String rUnit = request.getParameter("rUnit");
		Integer rSeq = Integer.valueOf(request.getParameter("rSeq"));
		Integer rEnable = Integer.valueOf(request.getParameter("rEnable"));
		Long rValue1 = Long.valueOf(request.getParameter("rValue1"));
		Long rValue2 = Long.valueOf(request.getParameter("rValue2"));
		Long rValue3 = Long.valueOf(request.getParameter("rValue3"));

		if (rName != null) {
			rName = StringEscapeUtils.escapeSql(rName);
		}	
		
		if (rContent != null) {
			rContent = StringEscapeUtils.escapeSql(rContent);
		}	
		
		if (rUnit != null) {
			rUnit = StringEscapeUtils.escapeSql(rUnit);
		}	
		
		NmsRule nr = new NmsRule(type, dType, rName, rContent, rUnit, rSeq,
				rEnable, rValue1, rValue2, rValue3, new Timestamp(
						System.currentTimeMillis()));

		if (nmsRuleService.update(nr)) {
			data.put("state", "0");
			data.put("info", "修改成功!");
			return data;
		} else {
			data.put("state", "3");
			data.put("info", "修改失败!");
			return data;
		}
	}

	//导出Excel示例接口，后续需要添加请求参数。
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean exportExcel(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(1);
		List<String> strs = new ArrayList<>();
		strs.add("RContent");
		NmsExcelUtil excelUtil = new NmsExcelUtil(rules, NmsRule.class, "ddd",
				"eee", "fff", strs, strs, response);
		boolean b = excelUtil.exportExcel();
		return b;
	}
	@RequestMapping(value = "/updateNmsRuleMonitorById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean updateNmsRuleMonitorById(HttpServletRequest request, HttpSession session) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Integer rEnable = Integer.valueOf(request.getParameter("rEnable"));
		String begin = "启用";
		if(rEnable==1) {
			begin = "启用";
		}else if(rEnable==0) {
			begin = "禁用";
		}
		NmsRuleItem nri = nmsRuleService.findNmsRuleById(id);
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), begin+"资产类别告警策略", "阈值管理", "修改数据", "成功");
		
		return nmsRuleService.updateNmsRuleById(id, rEnable, nri.getrSeq(), Integer.valueOf(nri.getrValue1().toString()),
				Integer.valueOf(nri.getrValue2().toString()), Integer.valueOf(nri.getrValue3().toString()));
	}

}