package iie.controller;

import iie.pojo.NmsYthAppAccount;
import iie.service.NmsAppAccountInfoService;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.tools.PageBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/AppAccountInfo")
public class NmsAppAccountInfoCtrl {
	@Autowired
	NmsAppAccountInfoService pService;
	
	@Autowired
	NmsAssetService assetService;
	
	@Autowired
	NmsAuditLogService auditLogService;

	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageBean<NmsYthAppAccount> listPageByDate(HttpServletRequest request, HttpSession session) throws Exception {
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
		String appId = request.getParameter("appId");

		PageBean<NmsYthAppAccount> page = pService.getPageByDate(orderKey, orderValue, startDate, endDate, begin, offset, appId);

		return page;
	}
}
