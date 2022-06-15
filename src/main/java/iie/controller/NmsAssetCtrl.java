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
    
    // list总数计算接口
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

    // menu加载接口
    @RequestMapping(value = "/menu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Menu> listMenu(HttpServletRequest request, HttpSession session) throws Exception {
		/*auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "进入资产管理");*/
        return nmsAssetService.getMenuList();
    }

    // 告警概览
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/typeAssetAlarm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List typeAssetAlarm(HttpServletRequest request, HttpSession session) throws Exception {
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看告警橄榄", "告警管理", "查询数据", "成功");	
		
        return nmsAssetService.getTypeAssetAlarmList();
    }

    // 性能概览
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/typeAssetAlarmDepartment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List typeAssetAlarmDepartment(HttpServletRequest request, HttpSession session) throws Exception {
        String deptId = request.getParameter("deptId");
		auditLogService.add(
				((NmsAdmin) session.getAttribute("user")).getRealname(),
				request.getRemoteAddr(), "查看性能概览", "监控管理", "查询数据", "成功");	

        if (deptId != null) {
            deptId = StringEscapeUtils.escapeSql(deptId);
        }

        return nmsAssetService.getTypeAssetAlarmDepartmentList(deptId);
    }

    // 首页底部
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/typeAsset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List typeAsset(HttpServletRequest request) throws Exception {
        return nmsAssetService.getTypeAssetList();
    }

    // list分页查询接口
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

        // 增加ip和设备名称查询
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
                request.getRemoteAddr(), "查看资产列表", "资产管理", "查询数据", "成功");
        
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
			// 定义正则表达式
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
		
        // 判断证书终端数是不是已经超过开始
    	NmsLicense licenseObj = licenseService.getLastLicense();
		if (licenseObj == null) {
			data.put("state", "1");
			data.put("info", "系统未激活，不能添加设备！");
			return data;
		}
		
		DesUtil des = new DesUtil();
		String ret = null;
        try {
        	ret = des.decrypt(licenseObj.getLicense());
        } catch(Exception e) {
			data.put("state", "1");
			data.put("info", "证书序列号解析失败（非法证书序列号）！");
			return data;
        }

        String[] arr = ret.split("#");
        if (arr.length != 5) {
			data.put("state", "1");
			data.put("info", "证书序列号解析失败（序列号参数数量不正确）！");
			return data;
        }

        // 判断参数computer是否合法
        if (arr[2] == null || arr[2].equals("")) {
			data.put("state", "1");
			data.put("info", "证书序列号解析失败（非法限制终端数参数）！");
			return data;
        }
        int licenseComputer = Integer.valueOf(arr[2]);
        int systemComputer = nmsAssetService.findAll();
        
        // System.out.println("licenseComputer = " + licenseComputer + ", systemComputer = " + systemComputer); 
        if (systemComputer >= licenseComputer) {
			data.put("state", "1");
			data.put("info", "当前已添加 " + systemComputer + " 个客户端, 超过授权 " + licenseComputer + " 个客户端！");
			return data;
        }
        
        // 判断证书终端数是不是已经超过结束
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
			data.put("info", "设备IP地址不能为空！");
			return data;
        }
        
        if (!isIP(AIp)) {
			data.put("state", "1");
			data.put("info", "请输入正确的IP地址: " + AIp);
			return data;
        }
        
        if (ANo == null || ANo.equals("")) {
			data.put("state", "1");
			data.put("info", "设备ASP卡ID不能为空！");
			return data;
        }
        
        if (type.getChType().equals("专用数通设备")) {
        	// 如果新增的设备是专用数据类设备就得判断是否和非专用数据设备类中IP地址重复
        	if (nmsAssetService.ifIpAndType(AIp, "专用数通设备")) {
    			data.put("state", "1");
    			data.put("info", "添加的专用数通设备：" + AIp + "，已在非专用数据设备中存在，仅专用数通类设备堆叠模式可添加相同IP设备！");
    			return data;
        	}
        } else {
        	// 如果新增的设备是非专用数据类设备就得判断是否所有设备中IP地址重复
        	if ( nmsAssetService.ifIp(AIp)) {
    			data.put("state", "1");
    			data.put("info", "添加的非专用数通设备：" + AIp + "，已存在，仅专用数通类设备堆叠模式可添加相同IP设备！");
    			return data;
        	}
        }

        if (nmsAssetService.ifNo(ANo)) {
			data.put("state", "1");
			data.put("info", "存在ASP卡ID相同设备: " + ANo + ", 请勿重复添加！");
			return data;
        }
        
        asset.setOnline(0);
        boolean bool = nmsAssetService.saveAssetAndRuleAsset(asset);
        if (bool) {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加设备：" + asset.getAName() + ", ASP卡ID：" + asset.getANo() + ", 管理IP：" + asset.getAIp(), "资产管理", "新增数据", "成功");
        } else {
            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "添加设备：" + asset.getAName() + ", ASP卡ID：" + asset.getANo() + ", 管理IP：" + asset.getAIp(), "资产管理", "新增数据", "失败");
			data.put("state", "1");
			data.put("info", "添加失败!");
			return data;
        }

		data.put("state", "0");
		data.put("info", "添加成功!");
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
			data.put("info", "设备IP地址不能为空！");
			return data;
        }
       
        if (!isIP(AIp)) {
			data.put("state", "1");
			data.put("info", "请输入正确的IP地址: " + AIp);
			return data;
        }
        
        if (ANo == null || ANo.equals("")) {
			data.put("state", "1");
			data.put("info", "设备APS卡ID不能为空！");
			return data;
        }
    
        // 根据id值获取设备类别的ID用来判断该设备类别是否修改
        NmsAsset oldAsset = nmsAssetService.findById(id);
        boolean flag = false;
        if (oldAsset != null) {
        	// 设备IP地址不允许被修改,现在用ASPID为唯一标识
/*        	
			if (!ANo.equals(oldAsset.getANo())) {
        		auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "设备ASP卡ID不允许修改, 修改节点: " + AIp, "资产管理", "修改数据", "失败");
    			data.put("state", "1");
    			data.put("info", "设备ASP卡ID：" + oldAsset.getANo() + "不允许被修改，修改失败！");
    			return data;
        	}
*/
        	asset.setOnline(oldAsset.getOnline());
        	if (type.getChType().equals("专用数通设备")) {
        		// 如果修改成专用数据设备类别,就得判断当前修改的IP地址不能与非专用数通设备中IP地址重复,可以和专用数通设备类IP重复（自己IP例外）
        		// 如果IP地址没有变化,说明是没有修改IP地址仅修改了其它属性,就说明IP地址一定是不重复的
        		if (AIp.equals(oldAsset.getAIp())) {
        		
        		// 如果IP地址已经变化,说明已经修改IP地址,那么就得判断修改后的IP地址不能与非专用数通设备IP地址重复,可以与专用数通设备IP堆叠模式重复
        		} else {
        			if (nmsAssetService.ifIpAndType(AIp, "专用数通设备")) {
            			data.put("state", "1");
            			data.put("info", "禁止修改成和非专用数通设备中已存在" + AIp + "节点，仅专用数通设备内堆叠可以重复IP地址！");
            			return data;
        			}
        		}
        	} else {
        		// 如果修改成非专用数据设备类别,就得判断当前修改的IP地址不能与所有设备中IP地址重复
        		// 如果IP地址没有变化,说明是没有修改IP地址仅修改了其它属性,但是应为现在是非专用数通设备,可能从专用数通设备修改成非专用数通设备
        		if (AIp.equals(oldAsset.getAIp())) {
        			// 就得判断之前该IP地址是否在专用数通设备中存在多个,不是自己的其它中也存在IP地址
        			if (!AIp.equals(oldAsset.getAIp()) && nmsAssetService.ifIp(AIp)) {
            			data.put("state", "1");
            			data.put("info", "修改成非专用数通设备类,系统中已经存在" + AIp + "节点，禁止修改！");
            			return data;
        			}
        		} else {
        			// 如果IP地址已经变化,说明已经修改IP地址,那么就得判断修改后的IP地址不能与所有设备IP地址重复
        			if (nmsAssetService.ifIp(AIp)) {
            			data.put("state", "1");
            			data.put("info", "修改成非专用数通设备类,系统中已经存在" + AIp + "节点，禁止修改！");
            			return data;
        			}
        		}
        	}

    		if (!ANo.equals(oldAsset.getANo()) && nmsAssetService.ifNo(ANo)) {
    			auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "存在相同ASP卡ID设备, 修改节点: " + AIp, "资产管理", "修改数据", "失败");
    			data.put("state", "1");
    			data.put("info", "存在ASP卡ID相同设备: " + ANo + ", 请勿重复修改！");
    			return data;
    		}

        	// 判断类型修改
        	int oldNmsAssetTypeId = oldAsset.getNmsAssetType().getId();
        	if (nmsAssetTypeId != oldNmsAssetTypeId) {
        		System.out.println("[DEBUG] nmsAssetTypeId = " + nmsAssetTypeId + ", oldNmsAssetTypeId = " + oldNmsAssetTypeId);
        		flag = true;
        	}
        }
        
        boolean bool = nmsAssetService.updateAsset(asset);
        if (bool) {
           auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(), request.getRemoteAddr(), "修改设备： " + asset.getAName() + ", ASP卡ID：" + asset.getANo() + ", 管理IP：" + asset.getAIp(), "资产管理", "修改数据", "成功");
            // 如果设备类别同时修改了需要更新规则表, 同时修改nms_rule_asset表
            if (flag) {
            	nmsAssetService.updateRuleAsset(asset);
            }
        }
        
        if (!bool) {
			data.put("state", "1");
			data.put("info", "修改失败！");
			return data;
        } 
		data.put("state", "0");
		data.put("info", "修改成功！");
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

                // 删除规则信息表, 做物理删除nms_rule_asset
                result = result && nmsRuleAssetService.deleteByAssetId(list.get(i));

                // 删除Ping信息表, 做物理删除nms_ping_info
                result = result && nmsPingInfoService.deleteByAssetId(list.get(i));

                // 删除静态信息表, 做物理删除nms_static_info
                result = result && nmsStaticInfoService.deleteByAssetId(list.get(i));

                // 删除动态信息表, 做物理删除nms_dynamic_info
                result = result && nmsDynamicInfoService.deleteByAssetId(list.get(i));

                // 删除CPU信息表, 做物理删除nms_cpu_info
                result = result && nmsCpuInfoService.deleteByAssetId(list.get(i));

                // 删除MEM信息表, 做物理删除nms_mem_info
                result = result && nmsMemInfoService.deleteByAssetId(list.get(i));

                // 删除文件系统分区信息表, 做物理删除nms_filesys_info
                result = result && nmsFilesysInfoService.deleteByAssetId(list.get(i));

                // 删除DiskIO信息表, 做物理删除nms_diskio_info
                result = result && nmsDiskioInfoService.deleteByAssetId(list.get(i));

                // 删除接口信息表, 做物理删除nms_netif_info
                result = result && nmsNetifInfoService.deleteByAssetId(list.get(i));

                // 删除进程信息表, 做物理删除nms_process_info
                result = result && nmsProcessInfoService.deleteByAssetId(list.get(i));

                // 删除mysql信息表, 做物理删除nms_mysql_info
                result = result && nmsMysqlInfoService.deleteByAssetId(list.get(i));

                // 删除tomcat信息表, 做物理删除nms_tomcat_info
                result = result && nmsTomcatInfoService.deleteByAssetId(list.get(i));

                // 删除终端账号信息表, 做物理删除nms_yth_account
                result = result && nmsAccountInfoService.deleteByAssetId(list.get(i));
                
                // 删除服务器授权信息信息表, 做物理删除nms_yth_app
                result = result && nmsAppInfoService.deleteByAssetId(list.get(i));
                
                // 删除终端和服务器软件安装信息表, 做物理删除nms_yth_soft
                result = result && nmsSoftInfoService.deleteByAssetId(list.get(i));

                // 更新告警表d_status = 4逻辑删除
                result = result && nmsAlarmService.deleteAlarmByAssetId(list.get(i));

                // 更新资产表deled=1逻辑删除, 重要信息nms_asset,nms_alarm,nms_alarm_report数据不做物理删除
                result = result && nmsAssetService.deleteAsset(id);

                if (result) {
                    // 添加删除nmn_topo_link表中用到该网元的记录
                    if (nmsNettopoService.deletealllink("net" + list.get(i))) {
                        // 添加删除nmn_topo_node表中用到该网元的记录
                        nmsNettopoService.deleteallnode("net" + list.get(i));
                    }

                    NmsAsset asset = nmsAssetService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                            request.getRemoteAddr(), "删除设备：" + asset.getAName() + ", ASP卡ID：" + asset.getANo() + ", 管理IP： " + asset.getAIp() + "成功", "资产管理", "删除数据", "成功");
                } else {
                    NmsAsset asset = nmsAssetService.findByIdAny(id);
                    auditLogService.add(
                            ((NmsAdmin) session.getAttribute("user")).getRealname(),
                           request.getRemoteAddr(), "删除设备：" + asset.getAName() + ", ASP卡ID：" + asset.getANo() + ", 管理IP： " + asset.getAIp() + "失败", "资产管理", "删除数据", "失败");
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
				request.getRemoteAddr(), "查看资产统计列表", "报表管理", "查询数据", "成功");  

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
            namesOfHeader.add("管理IP");
//            namesOfHeader.add("保密IP");
//            namesOfHeader.add("业务IP");
            namesOfHeader.add("名称");
            namesOfHeader.add("类型/类别");
            namesOfHeader.add("部门");
            namesOfHeader.add("存放位置");
            namesOfHeader.add("监控方式");
            namesOfHeader.add("是否监控");
            namesOfHeader.add("时间");

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
                            collModeStr = "专用代理";
                            break;
                        case 1:
                            collModeStr = "ICMP协议";
                            break;
                        case 2:
                            collModeStr = "SNMPv1&v2c协议";
                            break;
                        case 3:
                            collModeStr = "SNMPv3c协议";
                            break;
                        case 4:
                            collModeStr = "JMX协议";
                            break;
                    }
                }
                vpd.put("var7", collModeStr);        //9
                Short colled = nmsAsset.getColled();
                String colledStr = "";
                if (colled == 0) {
                    colledStr = "已监控";
                } else {
                    colledStr = "未监控";
                }
                vpd.put("var8", colledStr);        //10
                Timestamp itime = nmsAsset.getItime();
                String timeStr = String.valueOf(itime).replace(".0", "");
                vpd.put("var9", timeStr);        //11
                varList.add(vpd);
            }

            String docName = "资产统计报表";
            ExcelHelper excelHelper = new ExcelHelper(docName, namesOfHeader, varList);
            boolean b = excelHelper.buildExcelDocument().exportZip(request, response);

            auditLogService.add(
                    ((NmsAdmin) session.getAttribute("user")).getRealname(),
                    request.getRemoteAddr(), "导出资产报表", "资产管理", "导出文件", "成功");

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
            namesOfHeader.add("IP地址");
            namesOfHeader.add("设备名称");
            namesOfHeader.add("ASPID");
            namesOfHeader.add("资产类别");
            namesOfHeader.add("物理位置");
            namesOfHeader.add("厂商名称");
            namesOfHeader.add("资产管理员");
            namesOfHeader.add("所属部门");
            namesOfHeader.add("是否监控");
            namesOfHeader.add("监控方式");
            namesOfHeader.add("读团体名/用户名");
            namesOfHeader.add("写团体名/密码");
            namesOfHeader.add("端口号");
            namesOfHeader.add("采购日期");

            NmsExcelUtilForAssetsTemplate excelUtil = new NmsExcelUtilForAssetsTemplate(list,
                    NmsAsset.class, "资产模板", "资产模板", "资产模板",
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
            auditLogService.add(((NmsAdmin) session.getAttribute("user")).getRealname(),request.getRemoteAddr(), "导出资产模板", "资产管理", "导出文件", "成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }
    }
}
