package iie.controller;

import java.util.List;

import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsDepartment;
import iie.service.NmsAssetTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/assetType")
public class NmsAssetTypeCtrl {
	
	@Autowired
	NmsAssetTypeService natService;
	
	@RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsAssetType> getAll() {
		List<NmsAssetType> res = natService.getAll();
		return res;
	}
	
	@RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsAssetType findById(int id) {
		NmsAssetType res = natService.findById(id);
		return res;
	}
}
