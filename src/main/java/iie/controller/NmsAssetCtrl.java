package iie.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import iie.pojo.NmsAdmin;
import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsDepartment;
import iie.pojo.NmsLicense;
import iie.service.NmsAccountInfoService;
import iie.service.NmsAlarmService;
import iie.service.NmsAppInfoService;
import iie.service.NmsAssetService;
import iie.service.NmsAuditLogService;
import iie.service.NmsCpuInfoService;
import iie.service.NmsDiskioInfoService;
import iie.service.NmsDynamicInfoService;
import iie.service.NmsFilesysInfoService;
import iie.service.NmsLicenseService;
import iie.service.NmsMemInfoService;
import iie.service.NmsMysqlInfoService;
import iie.service.NmsNetifInfoService;
import iie.service.NmsNettopoService;
import iie.service.NmsPerformanceService;
import iie.service.NmsPingInfoService;
import iie.service.NmsProcessInfoService;
import iie.service.NmsRuleAssetService;
import iie.service.NmsSoftInfoService;
import iie.service.NmsStaticInfoService;
import iie.service.NmsTomcatInfoService;
import iie.tools.DesUtil;
import iie.tools.Menu;
import iie.tools.NmsExcelUtilForAssetsTemplate;
import iie.tools.NmsPerformanceRecord;
import iie.tools.NmsSystemDetail;
import iie.tools.NmsSystemOverview;
import iie.tools.PageBean;
import iie.tools.excel.ExcelHelper;

@Controller
@RequestMapping(value = "/Asset")
public class NmsAssetCtrl {

    @Autowired
    NmsAssetService nmsAssetService;

    @Autowired
    NmsAuditLogService auditLogService;

    @Autowired
    NmsNettopoService nmsNettopoService;

    @Autowired
    NmsRuleAssetService nmsRuleAssetService;

    @Autowired
    NmsAlarmService nmsAlarmService;

    @Autowired
    NmsPingInfoService nmsPingInfoService;

    @Autowired
    NmsStaticInfoService nmsStaticInfoService;

    @Autowired
    NmsDynamicInfoService nmsDynamicInfoService;

    @Autowired
    NmsCpuInfoService nmsCpuInfoService;

    @Autowired
    NmsMemInfoService nmsMemInfoService;

    @Autowired
    NmsFilesysInfoService nmsFilesysInfoService;

    @Autowired
    NmsDiskioInfoService nmsDiskioInfoService;

    @Autowired
    NmsNetifInfoService nmsNetifInfoService;

    @Autowired
    NmsProcessInfoService nmsProcessInfoService;

    @Autowired
    NmsMysqlInfoService nmsMysqlInfoService;

    @Autowired
    NmsTomcatInfoService nmsTomcatInfoService;
    
    @Autowired
    NmsSoftInfoService nmsSoftInfoService;
    
    @Autowired
    NmsAccountInfoService nmsAccountInfoService;
    
    @Autowired
    NmsAppInfoService nmsAppInfoService;
    
    @Autowired
    NmsLicenseService licenseService;    

    @Autowired
    NmsPerformanceService nmsPerformanceService;
    
    // list??????????????????
    @RequestMapping(value = "/queryTotal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> count(HttpServletRequest request)
            throws Exception {

        String orderKey = request.getParameter("orderKey");
        if (orderKey != null) {
        }
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String nmsAssetKey = request.getParameter("nmsAssetKey");
        String nmsAssetValue = request.getParameter("nmsAssetValue");
        String nmsDepartmentKey = request.getParameter("nmsDepartmentKey");
        String nmsDepartmentValue = request.getParameter("nmsDepartmentValue");
        String nmsAssetTypeKey = request.getParameter("nmsAssetTypeKey");
        String nmsAssetTypeValue = request.getParameter("nmsAssetTypeValue");
        int offset = Integer.valueOf(request.getParameter("offset"));

        if (nmsAssetKey != null) {
            nmsAssetKey = StringEscapeUtils.escapeSql(nmsAssetKey);
        }

        if (nmsAssetValue != null) {
            nmsAssetValue = StringEscapeUtils.escapeSql(nmsAssetValue);
        }

        if (nmsDepartmentKey != null) {
            nmsDepartmentKey = StringEscapeUtils.escapeSql(nmsDepartmentKey);
        }

        if (nmsDepartmentValue != null) {
            nmsDepartmentValue = StringEscapeUtils.escapeSql(nmsDepartmentValue);
        }

        if (nmsAssetTypeKey != null) {
            nmsAssetTypeKey = StringEscapeUtils.escapeSql(nmsAssetTypeKey);
        }

        if (nmsAssetTypeValue != null) {
            nmsAssetTypeValue = StringEscapeUtils.escapeSql(nmsAssetTypeValue);
        }

        return nmsAssetService.getCount(startDate, endDate, nmsAssetKey,
                nmsAssetValue, nmsDepartmentKey, nmsDepartmentValue,
                nmsAssetTypeKey, nmsAssetTypeValue, offset);
    }

    // menu????????????
    @RequestMapping(value = "/menu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Menu> listMenu(HttpServletRequest request, HttpSession session) throws Exception {
		/*auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "??????????????????");*/
        return nmsAssetService.getMenuList();
    }

    // ????????????
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/typeAssetAlarm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List typeAssetAlarm(HttpServletRequest request, HttpSession session) throws Exception {
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");	
		
        return nmsAssetService.getTypeAssetAlarmList();
    }

    // ????????????
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/typeAssetAlarmDepartment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List typeAssetAlarmDepartment(HttpServletRequest request, HttpSession session) throws Exception {
        String deptId = request.getParameter("deptId");
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");	

        if (deptId != null) {
            deptId = StringEscapeUtils.escapeSql(deptId);
        }

        return nmsAssetService.getTypeAssetAlarmDepartmentList(deptId);
    }

    // ????????????
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/typeAsset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List typeAsset(HttpServletRequest request) throws Exception {
        return nmsAssetService.getTypeAssetList();
    }

    // list??????????????????
    @RequestMapping(value = "/queryAssetForPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAsset> listPage(HttpServletRequest request)
            throws Exception {
        String orderKey = request.getParameter("orderKey");
        int orderValue = 0;
        if (orderKey != null) {
            orderValue = Integer.valueOf(request.getParameter("orderValue"));
        }
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String nmsAssetKey = request.getParameter("nmsAssetKey");
        String nmsAssetValue = request.getParameter("nmsAssetValue");
        String nmsDepartmentKey = request.getParameter("nmsDepartmentKey");
        String nmsDepartmentValue = request.getParameter("nmsDepartmentValue");
        String nmsAssetTypeKey = request.getParameter("nmsAssetTypeKey");
        String nmsAssetTypeValue = request.getParameter("nmsAssetTypeValue");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        if (nmsAssetKey != null) {
            nmsAssetKey = StringEscapeUtils.escapeSql(nmsAssetKey);
        }

        if (nmsAssetValue != null) {
            nmsAssetValue = StringEscapeUtils.escapeSql(nmsAssetValue);
        }

        if (nmsDepartmentKey != null) {
            nmsDepartmentKey = StringEscapeUtils.escapeSql(nmsDepartmentKey);
        }

        if (nmsDepartmentValue != null) {
            nmsDepartmentValue = StringEscapeUtils.escapeSql(nmsDepartmentValue);
        }

        if (nmsAssetTypeKey != null) {
            nmsAssetTypeKey = StringEscapeUtils.escapeSql(nmsAssetTypeKey);
        }

        if (nmsAssetTypeValue != null) {
            nmsAssetTypeValue = StringEscapeUtils.escapeSql(nmsAssetTypeValue);
        }

        return nmsAssetService.getPage(orderKey, orderValue, startDate,
                endDate, nmsAssetKey, nmsAssetValue, nmsDepartmentKey,
                nmsDepartmentValue, nmsAssetTypeKey, nmsAssetTypeValue, begin,
                offset);
    }

    @RequestMapping(value = "/list/page")
    @ResponseBody
    public List<NmsAsset> queryAssetForPage(String departmentName, String chType, Integer currentPage, Integer pageNum) {
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageNum == null) {
            pageNum = 10;
        }
        if (chType != null) {
            chType = StringEscapeUtils.escapeSql(chType);
        }
        return nmsAssetService.queryList(departmentName, chType, currentPage, pageNum);
    }

    @RequestMapping(value = "/count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> queryTotal(String departmentName, String chType, Integer pageNum) {
        return nmsAssetService.queryTotal(departmentName, chType, pageNum);
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list/menu", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsAsset> query() {
        return nmsAssetService.queryData();
    }

    @RequestMapping(value = "/list/date/condition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAsset> listPageByDateCondition(HttpServletRequest request, HttpSession session)
            throws Exception {

        String orderKey = request.getParameter("orderKey");
        String orderValue = request.getParameter("orderValue");
        if (orderKey == null || orderKey == "") {
            orderKey = "id";
        }

        int orderValue1 = 0;
        if (orderValue == null || orderValue == "") {
            orderValue1 = 0;
        } else {
            try {
                orderValue1 = Integer.valueOf(orderValue);
            } catch (NumberFormatException e) {
                orderValue1 = 0;
            }
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String nmsAssetKey = request.getParameter("nmsAssetKey");
        String nmsAssetValue = request.getParameter("nmsAssetValue");
        String nmsDepartmentKey = request.getParameter("nmsDepartmentKey");
        String nmsDepartmentValue = request.getParameter("nmsDepartmentValue");
        String nmsAssetTypeKey = request.getParameter("nmsAssetTypeKey");
        String nmsAssetTypeValue = request.getParameter("nmsAssetTypeValue");
        String nmsAssetValueDeveiceId = request.getParameter("nmsAssetValueDeveiceId");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        // ??????ip?????????????????????
        String nmsAssetValueIp = request.getParameter("nmsAssetValueIp");
        String nmsAssetValueEquip = request.getParameter("nmsAssetValueEquip");

        String nmsAssetValueBmIp = request.getParameter("nmsAssetValueBmIp");
        String nmsAssetValueYwIp = request.getParameter("nmsAssetValueYwIp");
        
        if (nmsAssetKey != null) {
            nmsAssetKey = StringEscapeUtils.escapeSql(nmsAssetKey);
        }

        if (nmsAssetValue != null) {
            nmsAssetValue = StringEscapeUtils.escapeSql(nmsAssetValue);
        }

        if (nmsDepartmentKey != null) {
            nmsDepartmentKey = StringEscapeUtils.escapeSql(nmsDepartmentKey);
        }

        if (nmsDepartmentValue != null) {
            nmsDepartmentValue = StringEscapeUtils.escapeSql(nmsDepartmentValue);
        }

        if (nmsAssetTypeKey != null) {
            nmsAssetTypeKey = StringEscapeUtils.escapeSql(nmsAssetTypeKey);
        }

        if (nmsAssetTypeValue != null) {
            nmsAssetTypeValue = StringEscapeUtils.escapeSql(nmsAssetTypeValue);
        }
        
        if (nmsAssetValueDeveiceId != null) {
        	nmsAssetValueDeveiceId = StringEscapeUtils.escapeSql(nmsAssetValueDeveiceId);
        }

        PageBean<NmsAsset> page = nmsAssetService.getPageByConditionDate(
                orderKey, orderValue1, startDate, endDate, nmsAssetKey,
                nmsAssetValue, nmsDepartmentKey, nmsDepartmentValue,
                nmsAssetTypeKey, nmsAssetTypeValue, begin, offset,nmsAssetValueIp, nmsAssetValueBmIp, nmsAssetValueYwIp, nmsAssetValueEquip,nmsAssetValueDeveiceId);

        auditLogService.add(
                ((NmsAdmin) session.getAttribute("user")).getRealname(),
                request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");
        
        return page;
    }

    @RequestMapping(value = "/loadLastAsset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAsset loadLastAsset(HttpServletRequest request)
            throws Exception {

        return nmsAssetService.loadLastAsset();
    }

	public boolean isIP(String text) {
		if (text != null && !text.isEmpty()) {
			// ?????????????????????
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			if (text.matches(regex)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

    @SuppressWarnings("static-access")
	@RequestMapping(value = "/addAsset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> addAsset(HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, String> data = new HashMap<String, String>();
        String AIp = request.getParameter("AIp");
        String BmIp = request.getParameter("BmIp");
        String YwIp = request.getParameter("YwIp");
        String AManu = request.getParameter("AManu");
        String AName = request.getParameter("AName");
        String ANo = request.getParameter("ANo");
        String APos = request.getParameter("APos");
        String AUser = request.getParameter("AUser");
        String RComm = request.getParameter("RComm");
        String WComm = request.getParameter("WComm");
        String ADate = request.getParameter("ADate");
        String authPass = request.getParameter("authPass");
        Short colled = Short.valueOf(request.getParameter("colled"));
        String collMode = request.getParameter("collMode");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sshport = request.getParameter("sshport");
        String nmsAssetDepartmentId = request.getParameter("nmsAssetDepartmentId");
        int nmsAssetTypeId = Integer.valueOf(request.getParameter("nmsAssetTypeId"));
		
        // ????????????????????????????????????????????????
    	NmsLicense licenseObj = licenseService.getLastLicense();
		if (licenseObj == null) {
			data.put("state", "1");
			data.put("info", "???????????????????????????????????????");
			return data;
		}
		
		DesUtil des = new DesUtil();
		String ret = null;
        try {
        	ret = des.decrypt(licenseObj.getLicense());
        } catch(Exception e) {
			data.put("state", "1");
			data.put("info", "?????????????????????????????????????????????????????????");
			return data;
        }

        String[] arr = ret.split("#");
        if (arr.length != 5) {
			data.put("state", "1");
			data.put("info", "??????????????????????????????????????????????????????????????????");
			return data;
        }

        // ????????????computer????????????
        if (arr[2] == null || arr[2].equals("")) {
			data.put("state", "1");
			data.put("info", "???????????????????????????????????????????????????????????????");
			return data;
        }
        int licenseComputer = Integer.valueOf(arr[2]);
        int systemComputer = nmsAssetService.findAll();
        
        // System.out.println("licenseComputer = " + licenseComputer + ", systemComputer = " + systemComputer); 
        if (systemComputer >= licenseComputer) {
			data.put("state", "1");
			data.put("info", "??????????????? " + systemComputer + " ????????????, ???????????? " + licenseComputer + " ???????????????");
			return data;
        }
        
        // ????????????????????????????????????????????????
        NmsDepartment department = nmsAssetService.findDepartmentById(nmsAssetDepartmentId);
        NmsAssetType type = nmsAssetService.findTypeById(nmsAssetTypeId);
        NmsAsset asset = new NmsAsset();
        asset.setAIp(AIp);
        asset.setBmIp(BmIp==null?"":BmIp);
        asset.setYwIp(YwIp==null?"":YwIp);
        asset.setColled(colled);
        asset.setNmsDepartment(department);
        asset.setNmsAssetType(type);
        if (AManu != null) {
            asset.setAManu(AManu);
        }
        if (AName != null) {
            asset.setAName(AName);
        }
        if (ANo != null) {
            asset.setANo(ANo);
        }
        if (APos != null) {
            asset.setAPos(APos);
        }
        if (AUser != null) {
            asset.setAUser(AUser);
        }
        if (collMode != null) {
            asset.setCollMode(Integer.valueOf(collMode));
        }
        if (RComm != null) {
            asset.setRComm(RComm);
        }
        if (WComm != null) {
            asset.setWComm(WComm);
        }
        if (ADate != null) {
            asset.setADate(ADate);
        }
        if (authPass != null) {
            asset.setAuthPass(authPass);
        }

        if (username != null) {
            asset.setUsername(username);
        }
        
        if (password != null) {
            asset.setPassword(password);
        }
        
        if (sshport != null) {
            asset.setSshport(sshport);
        }
        
        if (AIp == null || AIp.equals("")) {
			data.put("state", "1");
			data.put("info", "??????IP?????????????????????");
			return data;
        }
        
        if (!isIP(AIp)) {
			data.put("state", "1");
			data.put("info", "??????????????????IP??????: " + AIp);
			return data;
        }
        
        if (ANo == null || ANo.equals("")) {
			data.put("state", "1");
			data.put("info", "??????ASP???ID???????????????");
			return data;
        }
        
        if (type.getChType().equals("??????????????????")) {
        	// ?????????????????????????????????????????????????????????????????????????????????????????????IP????????????
        	if (nmsAssetService.ifIpAndType(AIp, "??????????????????")) {
    			data.put("state", "1");
    			data.put("info", "??????????????????????????????" + AIp + "?????????????????????????????????????????????????????????????????????????????????????????????IP?????????");
    			return data;
        	}
        } else {
        	// ?????????????????????????????????????????????????????????????????????????????????IP????????????
        	if ( nmsAssetService.ifIp(AIp)) {
    			data.put("state", "1");
    			data.put("info", "?????????????????????????????????" + AIp + "??????????????????????????????????????????????????????????????????IP?????????");
    			return data;
        	}
        }

        if (nmsAssetService.ifNo(ANo)) {
			data.put("state", "1");
			data.put("info", "??????ASP???ID????????????: " + ANo + ", ?????????????????????");
			return data;
        }
        
        asset.setOnline(0);
        boolean bool = nmsAssetService.saveAssetAndRuleAsset(asset);
        if (bool) {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "???????????????" + asset.getAName() + ", ASP???ID???" + asset.getANo() + ", ??????IP???" + asset.getAIp(), "????????????", "????????????", "??????");
        } else {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "???????????????" + asset.getAName() + ", ASP???ID???" + asset.getANo() + ", ??????IP???" + asset.getAIp(), "????????????", "????????????", "??????");
			data.put("state", "1");
			data.put("info", "????????????!");
			return data;
        }

		data.put("state", "0");
		data.put("info", "????????????!");
		return data;
    }

    @RequestMapping(value = "/updateAsset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> updateAsset(HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, String> data = new HashMap<String, String>();
        int id = Integer.valueOf(request.getParameter("id"));
        String AIp = request.getParameter("AIp");
        String BmIp = request.getParameter("BmIp");
        String YwIp = request.getParameter("YwIp");
        String AManu = request.getParameter("AManu");
        String AName = request.getParameter("AName");
        String ANo = request.getParameter("ANo");
        String APos = request.getParameter("APos");
        String AUser = request.getParameter("AUser");
        String RComm = request.getParameter("RComm");
        String WComm = request.getParameter("WComm");
        String ADate = request.getParameter("ADate");
        String authPass = request.getParameter("authPass");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sshport = request.getParameter("sshport");
        
        Short colled = Short.valueOf(request.getParameter("colled"));
        Integer collMode = Integer.valueOf(request.getParameter("collMode"));
        String nmsAssetDepartmentId = request.getParameter("nmsAssetDepartmentId");
        int nmsAssetTypeId = Integer.valueOf(request.getParameter("nmsAssetTypeId"));
        
        NmsDepartment department = nmsAssetService.findDepartmentById(nmsAssetDepartmentId);
        NmsAssetType type = nmsAssetService.findTypeById(nmsAssetTypeId);

        NmsAsset asset = new NmsAsset();
        asset.setId(id);
        if (AIp != null && !AIp.equals("")) {
        	asset.setAIp(AIp);
        }
         
        if (BmIp != null && !BmIp.equals("")) {
        	asset.setBmIp(BmIp);
        } else {
        	asset.setBmIp("");
        }
        
        if (YwIp != null && !YwIp.equals("")) {
        	asset.setYwIp(YwIp);
        } else {
        	asset.setYwIp("");
        }
        asset.setColled(colled);
        if (department != null) {
        	asset.setNmsDepartment(department);
        }
        if (type != null && !type.equals("")) {
        	asset.setNmsAssetType(type);
        }
        if (AManu != null && !AManu.equals("")) {
            asset.setAManu(AManu);
        }else {
        	asset.setAManu("");
        }
        if (AName != null && !AName.equals("")) {
            asset.setAName(AName);
        }else {
        	asset.setAName("");
        }
        if (ANo != null && !ANo.equals("")) {
            asset.setANo(ANo);
        } else {
        	asset.setANo("");
        }
        if (APos != null && !APos.equals("")) {
            asset.setAPos(APos);
        }else {
        	asset.setAPos("");
        }
        if (AUser != null && !AUser.equals("")) {
            asset.setAUser(AUser);
        }else {
        	asset.setAUser("");
        }
        if (collMode != null && !collMode.equals("")) {
            asset.setCollMode(collMode);
        }
        if (RComm != null && !RComm.equals("")) {
            asset.setRComm(RComm);
        }
        if (WComm != null && !WComm.equals("")) {
            asset.setWComm(WComm);
        }
        if (ADate != null && !ADate.equals("")) {
            asset.setADate(ADate);
        }
        if (authPass != null && !authPass.equals("")) {
            asset.setAuthPass(authPass);
        }     
        
        if (username != null && !username.equals("")) {
            asset.setUsername(username);
        }
        
        if (password != null && !password.equals("")) {
            asset.setPassword(password);
        }

        if (sshport != null && !sshport.equals("")) {
            asset.setSshport(sshport);
        }
        
        asset.setDeled(0);

        if (AIp == null || AIp.equals("")) {
			data.put("state", "1");
			data.put("info", "??????IP?????????????????????");
			return data;
        }
       
        if (!isIP(AIp)) {
			data.put("state", "1");
			data.put("info", "??????????????????IP??????: " + AIp);
			return data;
        }
        
        if (ANo == null || ANo.equals("")) {
			data.put("state", "1");
			data.put("info", "??????APS???ID???????????????");
			return data;
        }
    
        // ??????id????????????????????????ID???????????????????????????????????????
        NmsAsset oldAsset = nmsAssetService.findById(id);
        boolean flag = false;
        if (oldAsset != null) {
        	// ??????IP????????????????????????,?????????ASPID???????????????
/*        	
			if (!ANo.equals(oldAsset.getANo())) {
        		auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "??????ASP???ID???????????????, ????????????: " + AIp, "????????????", "????????????", "??????");
    			data.put("state", "1");
    			data.put("info", "??????ASP???ID???" + oldAsset.getANo() + "????????????????????????????????????");
    			return data;
        	}
*/
        	asset.setOnline(oldAsset.getOnline());
        	if (type.getChType().equals("??????????????????")) {
        		// ???????????????????????????????????????,???????????????????????????IP???????????????????????????????????????IP????????????,??????????????????????????????IP???????????????IP?????????
        		// ??????IP??????????????????,?????????????????????IP??????????????????????????????,?????????IP???????????????????????????
        		if (AIp.equals(oldAsset.getAIp())) {
        		
        		// ??????IP??????????????????,??????????????????IP??????,??????????????????????????????IP????????????????????????????????????IP????????????,???????????????????????????IP??????????????????
        		} else {
        			if (nmsAssetService.ifIpAndType(AIp, "??????????????????")) {
            			data.put("state", "1");
            			data.put("info", "???????????????????????????????????????????????????" + AIp + "???????????????????????????????????????????????????IP?????????");
            			return data;
        			}
        		}
        	} else {
        		// ??????????????????????????????????????????,???????????????????????????IP??????????????????????????????IP????????????
        		// ??????IP??????????????????,?????????????????????IP??????????????????????????????,??????????????????????????????????????????,?????????????????????????????????????????????????????????
        		if (AIp.equals(oldAsset.getAIp())) {
        			// ?????????????????????IP????????????????????????????????????????????????,?????????????????????????????????IP??????
        			if (!AIp.equals(oldAsset.getAIp()) && nmsAssetService.ifIp(AIp)) {
            			data.put("state", "1");
            			data.put("info", "?????????????????????????????????,?????????????????????" + AIp + "????????????????????????");
            			return data;
        			}
        		} else {
        			// ??????IP??????????????????,??????????????????IP??????,??????????????????????????????IP???????????????????????????IP????????????
        			if (nmsAssetService.ifIp(AIp)) {
            			data.put("state", "1");
            			data.put("info", "?????????????????????????????????,?????????????????????" + AIp + "????????????????????????");
            			return data;
        			}
        		}
        	}

    		if (!ANo.equals(oldAsset.getANo()) && nmsAssetService.ifNo(ANo)) {
    			auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "????????????ASP???ID??????, ????????????: " + AIp, "????????????", "????????????", "??????");
    			data.put("state", "1");
    			data.put("info", "??????ASP???ID????????????: " + ANo + ", ?????????????????????");
    			return data;
    		}

        	// ??????????????????
        	int oldNmsAssetTypeId = oldAsset.getNmsAssetType().getId();
        	if (nmsAssetTypeId != oldNmsAssetTypeId) {
        		System.out.println("[DEBUG] nmsAssetTypeId = " + nmsAssetTypeId + ", oldNmsAssetTypeId = " + oldNmsAssetTypeId);
        		flag = true;
        	}
        }
        
        boolean bool = nmsAssetService.updateAsset(asset);
        if (bool) {
           auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "??????????????? " + asset.getAName() + ", ASP???ID???" + asset.getANo() + ", ??????IP???" + asset.getAIp(), "????????????", "????????????", "??????");
            // ??????????????????????????????????????????????????????, ????????????nms_rule_asset???
            if (flag) {
            	nmsAssetService.updateRuleAsset(asset);
            }
        }
        
        if (!bool) {
			data.put("state", "1");
			data.put("info", "???????????????");
			return data;
        } 
		data.put("state", "0");
		data.put("info", "???????????????");
		return data;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAsset findById(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        NmsAsset asset = nmsAssetService.findById(id);
        return asset;
    }

	public static boolean isNumber(Object o) {
		return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
	}

    @RequestMapping(value = "/findByIdToUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsAsset findByIdToUpdate(HttpServletRequest request) {
    	String idStr = request.getParameter("id") + "";
    	if (!isNumber(idStr)) {
    		return null;
    	}
        int id = Integer.valueOf(idStr);
        NmsAsset asset = nmsAssetService.findById(id);
        return asset;
    }

    @RequestMapping(value = "/list/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean listDelete(HttpServletRequest request, HttpSession session) {
        String idstr = request.getParameter("id");
        List<String> list = Arrays.asList(idstr.split(","));
        boolean result = true;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }

                int id = 0;
                try {
                    id = Integer.valueOf(list.get(i));
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] list.get(i) is null, " + e.toString());
                    continue;
                }

                // ?????????????????????, ???????????????nms_rule_asset
                result = result && nmsRuleAssetService.deleteByAssetId(list.get(i));

                // ??????Ping?????????, ???????????????nms_ping_info
                result = result && nmsPingInfoService.deleteByAssetId(list.get(i));

                // ?????????????????????, ???????????????nms_static_info
                result = result && nmsStaticInfoService.deleteByAssetId(list.get(i));

                // ?????????????????????, ???????????????nms_dynamic_info
                result = result && nmsDynamicInfoService.deleteByAssetId(list.get(i));

                // ??????CPU?????????, ???????????????nms_cpu_info
                result = result && nmsCpuInfoService.deleteByAssetId(list.get(i));

                // ??????MEM?????????, ???????????????nms_mem_info
                result = result && nmsMemInfoService.deleteByAssetId(list.get(i));

                // ?????????????????????????????????, ???????????????nms_filesys_info
                result = result && nmsFilesysInfoService.deleteByAssetId(list.get(i));

                // ??????DiskIO?????????, ???????????????nms_diskio_info
                result = result && nmsDiskioInfoService.deleteByAssetId(list.get(i));

                // ?????????????????????, ???????????????nms_netif_info
                result = result && nmsNetifInfoService.deleteByAssetId(list.get(i));

                // ?????????????????????, ???????????????nms_process_info
                result = result && nmsProcessInfoService.deleteByAssetId(list.get(i));

                // ??????mysql?????????, ???????????????nms_mysql_info
                result = result && nmsMysqlInfoService.deleteByAssetId(list.get(i));

                // ??????tomcat?????????, ???????????????nms_tomcat_info
                result = result && nmsTomcatInfoService.deleteByAssetId(list.get(i));

                // ???????????????????????????, ???????????????nms_yth_account
                result = result && nmsAccountInfoService.deleteByAssetId(list.get(i));
                
                // ????????????????????????????????????, ???????????????nms_yth_app
                result = result && nmsAppInfoService.deleteByAssetId(list.get(i));
                
                // ?????????????????????????????????????????????, ???????????????nms_yth_soft
                result = result && nmsSoftInfoService.deleteByAssetId(list.get(i));

                // ???????????????d_status = 4????????????
                result = result && nmsAlarmService.deleteAlarmByAssetId(list.get(i));

                // ???????????????deled=1????????????, ????????????nms_asset,nms_alarm,nms_alarm_report????????????????????????
                result = result && nmsAssetService.deleteAsset(id);

                if (result) {
                    // ????????????nmn_topo_link??????????????????????????????
                    if (nmsNettopoService.deletealllink("net" + list.get(i))) {
                        // ????????????nmn_topo_node??????????????????????????????
                        nmsNettopoService.deleteallnode("net" + list.get(i));
                    }

                    NmsAsset asset = nmsAssetService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                            request.getRemoteAddr(), "???????????????" + asset.getAName() + ", ASP???ID???" + asset.getANo() + ", ??????IP??? " + asset.getAIp() + "??????", "????????????", "????????????", "??????");
                } else {
                    NmsAsset asset = nmsAssetService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                           request.getRemoteAddr(), "???????????????" + asset.getAName() + ", ASP???ID???" + asset.getANo() + ", ??????IP??? " + asset.getAIp() + "??????", "????????????", "????????????", "??????");
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/list/date/reportSelect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageBean<NmsAsset> reportSelect(HttpServletRequest request, HttpSession session)
            throws Exception {
        String orderKey = request.getParameter("orderKey");
        int orderValue = 0;
        if (orderKey == null || orderKey.equals("")) {
            orderKey = "id";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(request.getParameter("orderValue"));
        }

        String startDate = request.getParameter("itime_start");
        String endDate = request.getParameter("itime_end");
        String AName = request.getParameter("a_name");
        String AIp = request.getParameter("a_ip");
        String AType = request.getParameter("a_type");
        String ADept = request.getParameter("a_dept");
        int begin = Integer.valueOf(request.getParameter("begin"));
        int offset = Integer.valueOf(request.getParameter("offset"));

        if (startDate != null) {
            startDate = StringEscapeUtils.escapeSql(startDate);
        }

        if (endDate != null) {
            endDate = StringEscapeUtils.escapeSql(endDate);
        }

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (AType != null) {
            AType = StringEscapeUtils.escapeSql(AType);
        }

        if (ADept != null) {
            ADept = StringEscapeUtils.escapeSql(ADept);
        }

        PageBean<NmsAsset> page = nmsAssetService.reportSelect(orderKey,
                orderValue, startDate, endDate, AName, AIp, AType, ADept,
                begin, offset);

		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "????????????????????????", "????????????", "????????????", "??????");  

        return page;
    }

    @RequestMapping(value = "/SystermInfo/Overview")
    @ResponseBody
    public NmsSystemOverview assetSystermInfoOverview(HttpServletRequest request) {
        String assetIdParam = request.getParameter("assetId");
        Integer assetId = -1;
        if (assetIdParam != null && assetIdParam.length() > 0) {
            assetId = Integer.valueOf(assetIdParam);
        }
        NmsSystemOverview nso = new NmsSystemOverview();
        
        NmsSystemDetail nsd = nmsAssetService.assetSystermInfoOverview(assetId);
        PageBean<NmsPerformanceRecord> npr = nmsPerformanceService.listNmsPerformanceByConditionById("",
				0, "", nsd.getAssetIP(), null, null, null, 1, 10);
        if(CollectionUtils.isNotEmpty(npr.getList())) {
        	NmsPerformanceRecord item = npr.getList().get(0);
        	nso.setNpr(item);
        }
        nso.setNd(nsd);
         return nso; 
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list/date/reportSelect/exportExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean reportSelectExportExcel(HttpServletRequest request,
                                                HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

        System.out.println(request.getSession().getServletContext()
                .getRealPath("temp"));

        int orderValue = 0;
        String orderKey = request.getParameter("orderKey");
        String orderValueParameter = request.getParameter("orderValue");
        if (orderKey == null || orderKey.equals("") || orderValueParameter == null || orderValueParameter.equals("")) {
            orderKey = "id";
            orderValue = 0;
        } else {
            orderValue = Integer.valueOf(orderValueParameter);
        }

        String startDate = request.getParameter("itime_start");
        String endDate = request.getParameter("itime_end");
        String AName = request.getParameter("a_name");
        String AIp = request.getParameter("a_ip");
        String AType = request.getParameter("a_type");
        String ADept = request.getParameter("a_dept");


        if (startDate != null) {
            startDate = StringEscapeUtils.escapeSql(startDate);
        }

        if (endDate != null) {
            endDate = StringEscapeUtils.escapeSql(endDate);
        }

        if (AName != null) {
            AName = StringEscapeUtils.escapeSql(AName);
        }

        if (AIp != null) {
            AIp = StringEscapeUtils.escapeSql(AIp);
        }

        if (AType != null) {
            AType = StringEscapeUtils.escapeSql(AType);
        }

        if (ADept != null) {
            ADept = StringEscapeUtils.escapeSql(ADept);
        }


        List<NmsAsset> list;
        try {
            list = nmsAssetService.reportSelectExportExcel(orderKey,
                    orderValue, startDate, endDate, AName, AIp, AType, ADept);
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("ASPID");
            namesOfHeader.add("??????IP");
//            namesOfHeader.add("??????IP");
//            namesOfHeader.add("??????IP");
            namesOfHeader.add("??????");
            namesOfHeader.add("??????/??????");
            namesOfHeader.add("??????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("??????");

            List<Map> varList = new ArrayList<Map>();
            for (int i = 0; i < list.size(); i++) {
                NmsAsset nmsAsset = list.get(i);
                Map<String, String> vpd = new HashMap<>();
                vpd.put("var1", nmsAsset.getANo());   
                vpd.put("var2", nmsAsset.getAIp());  
//                vpd.put("var3", nmsAsset.getBmIp());         // 3
//                vpd.put("var4", nmsAsset.getYwIp());     
                vpd.put("var3", nmsAsset.getAName());        
                vpd.put("var4", nmsAsset.getNmsAssetType().getChSubtype());    
                vpd.put("var5", nmsAsset.getNmsDepartment().getDName());        
                vpd.put("var6", nmsAsset.getAPos());   
                Integer collMode = nmsAsset.getCollMode();
                String collModeStr = "--";
                if (null != collMode) {
                    switch (collMode) {
                        case 0:
                            collModeStr = "????????????";
                            break;
                        case 1:
                            collModeStr = "ICMP??????";
                            break;
                        case 2:
                            collModeStr = "SNMPv1&v2c??????";
                            break;
                        case 3:
                            collModeStr = "SNMPv3c??????";
                            break;
                        case 4:
                            collModeStr = "JMX??????";
                            break;
                    }
                }
                vpd.put("var7", collModeStr);        //9
                Short colled = nmsAsset.getColled();
                String colledStr = "";
                if (colled == 0) {
                    colledStr = "?????????";
                } else {
                    colledStr = "?????????";
                }
                vpd.put("var8", colledStr);        //10
                Timestamp itime = nmsAsset.getItime();
                String timeStr = String.valueOf(itime).replace(".0", "");
                vpd.put("var9", timeStr);        //11
                varList.add(vpd);
            }

            String docName = "??????????????????";
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");

            return true && b;
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }
    }

    @RequestMapping(value = "/ifIp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean ifUser(HttpServletRequest request) throws Exception {
        String ip = request.getParameter("AIp");

        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }

        return nmsAssetService.ifIp(ip);
    }

    @RequestMapping(value = "/template/importExcel", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean templateImportExcel(HttpServletRequest request,
                                       HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

        List<NmsAsset> list = new ArrayList<NmsAsset>();
        List<String> namesOfField = new ArrayList<String>();
        try {
            List<String> namesOfHeader = new ArrayList<>();
            namesOfHeader.add("IP??????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("ASPID");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("???????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????");
            namesOfHeader.add("????????????/?????????");
            namesOfHeader.add("????????????/??????");
            namesOfHeader.add("?????????");
            namesOfHeader.add("????????????");

            NmsExcelUtilForAssetsTemplate excelUtil = new NmsExcelUtilForAssetsTemplate(list,
                    NmsAsset.class, "????????????", "????????????", "????????????",
                    namesOfHeader, namesOfField, response);
            boolean b = excelUtil.exportExcel();

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean ifIp(String ip) throws Exception {
        if (ip != null) {
            ip = StringEscapeUtils.escapeSql(ip);
        }
        return nmsAssetService.ifIp(ip);
    }

    @RequestMapping(value = "/exportTemplate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean exportTemplate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("/ui/assets/asset_import_template.xls");
    	System.out.println(path);
        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode("asset_import_template.xls", "UTF-8"));
        try {
			InputStream in = new FileInputStream(path);
			int count =0;
			byte[] by = new byte[4096];
			OutputStream out=  response.getOutputStream();
			while((count=in.read(by))!=-1){
				out.write(by, 0, count);
			}
			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			return false;
		}

        try {
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),request.getRemoteAddr(), "??????????????????", "????????????", "????????????", "??????");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }
    }
}
