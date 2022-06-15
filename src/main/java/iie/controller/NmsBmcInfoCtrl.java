package iie.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import iie.pojo.NmsAsset;
import iie.pojo.NmsBmcInfo;
import iie.pojo.NmsCpuInfo;
import iie.service.NmsBmcInfoService;
import iie.tools.PageBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/BmcInfo")
public class NmsBmcInfoCtrl {

	@Autowired
	NmsBmcInfoService bService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsBmcInfo> getAll() {
		List<NmsBmcInfo> res = bService.getAll();
		return res;
	}

	@RequestMapping(value = "/add/{cpuNum}/{cpuTemp}/{dimmNum}/{dimmTemp}/{raidTemp}/{inletTemp}/{outletTemp}/{fanNum}/{fanSpeed}/{psuNum}/{psuTemp}/{psuPower}/{voltage12v}/{voltage5v}/{voltage3v3}/{voltageBat}/{asset_id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addNb(HttpServletRequest request)
			throws Exception {
		Map<String, String> data = new HashMap<String, String>();

		int asset_id = Integer.valueOf(request.getParameter("asset_id"));
		int cpuNum = Integer.valueOf(request.getParameter("cpuNum"));
		String cpuTemp = request.getParameter("cpuTemp");
		int dimmNum = Integer.valueOf(request.getParameter("dimmNum"));
		String dimmTemp = request.getParameter("dimmTemp");
		float raidTemp = Float.valueOf(request.getParameter("raidTemp"));
		float inletTemp = Float.valueOf(request.getParameter("inletTemp"));
		float outletTemp = Float.valueOf(request.getParameter("outletTemp"));
		int fanNum = Integer.valueOf(request.getParameter("fanNum"));
		String fanSpeed = request.getParameter("fanSpeed");
		int psuNum = Integer.valueOf(request.getParameter("psuNum"));
		String psuTemp = request.getParameter("psuTemp");
		String psuPower = request.getParameter("psuPower");
		float voltage12v = Float.valueOf(request.getParameter("raidTemp"));
		float voltage5v = Float.valueOf(request.getParameter("voltage5v"));
		float voltage3v3 = Float.valueOf(request.getParameter("voltage3v3"));
		float voltageBat = Float.valueOf(request.getParameter("voltageBat"));

		NmsAsset na = bService.findById(asset_id);
		NmsBmcInfo nb = new NmsBmcInfo(na, cpuNum, cpuTemp, dimmNum, dimmTemp,
				raidTemp, inletTemp, outletTemp, fanNum, fanSpeed, psuNum,
				psuTemp, psuPower, voltage12v, voltage5v, voltage3v3,
				voltageBat, new Timestamp(System.currentTimeMillis()));

		for (int i = 0; i < 100; i++) {
			if (bService.saveBmc(nb)) {
				data.put("state", "0");
				data.put("info", "添加成功!");
			} else {
				data.put("state", "1");
				data.put("info", "添加失败!");
			}
		}
		return data;
	}

	@RequestMapping(value = "/list/date", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsBmcInfo> listPageByDate(HttpServletRequest request)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		int orderValue = Integer.valueOf(request.getParameter("orderValue"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		int begin = Integer.valueOf(request.getParameter("begin"));
		int offset = Integer.valueOf(request.getParameter("offset"));

		PageBean<NmsBmcInfo> page = bService.getPageByDate(orderKey,
				orderValue, startDate, endDate, begin, offset);

		return page.getList();
	}
	
	/*@RequestMapping(value = "/list/date/condition", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsAsset> listPageByDateCondition(HttpServletRequest request)
			throws Exception {

		String orderKey = request.getParameter("orderKey");
		int orderValue = Integer.valueOf(request.getParameter("orderValue"));
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

		
		System.out.println("orderKey:"+orderKey);
		System.out.println("orderValue:"+orderValue);
		System.out.println("startDate:"+startDate);
		System.out.println("endDate:"+endDate);
		System.out.println("nmsAssetKey:"+nmsAssetKey);
		System.out.println("nmsAssetValue:"+nmsAssetValue);
		System.out.println("nmsDepartmentKey:"+nmsDepartmentKey);
		System.out.println("nmsDepartmentValue:"+nmsDepartmentValue);
		System.out.println("nmsAssetTypeKey:"+nmsAssetTypeKey);
		System.out.println("nmsAssetTypeValue:"+nmsAssetTypeValue);
		System.out.println("begin:"+begin);
		System.out.println("offset:"+offset);
		
		PageBean<NmsAsset> page = bService.getPageByConditionDate(
				orderKey, orderValue, startDate, endDate, nmsAssetKey,
				nmsAssetValue, nmsDepartmentKey, nmsDepartmentValue,
				nmsAssetTypeKey, nmsAssetTypeValue, begin, offset);

		return page.getList();
	}*/

}
