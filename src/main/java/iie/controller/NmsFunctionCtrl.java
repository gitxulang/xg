package iie.controller;

import iie.pojo.NmsFunction;
import iie.service.NmsFunctionService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/NmsFunction")
public class NmsFunctionCtrl {
	
	@Autowired
	NmsFunctionService nfService;

	@RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<NmsFunction> getAll() {
		List<NmsFunction> res = nfService.getAll();
		return res;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> addNa(HttpServletRequest request)
			throws Exception {
		Map<String, String> data = new HashMap<String, String>();

		String functionDesc = request.getParameter("functionDesc");
		String chineseDesc = request.getParameter("chineseDesc");
		String levelDesc = request.getParameter("levelDesc");
		String fatherNode = request.getParameter("fatherNode");
		String funUrl = request.getParameter("funUrl");
		String imgUrl = request.getParameter("imgUrl");

		NmsFunction nf = new NmsFunction(functionDesc, chineseDesc, levelDesc, fatherNode,
				funUrl, imgUrl, new Timestamp(
						System.currentTimeMillis()));

		if (nfService.saveFunction(nf)) {
			data.put("state", "0");
			data.put("info", "添加成功!");
			return data;
		} else {
			data.put("state", "1");
			data.put("info", "添加失败!");
			return data;
		}
	}
	
	@RequestMapping(value = "/getNmsFunctionById", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsFunction getNmsFunctionById(HttpServletRequest request)
			throws Exception {
		
		int id=new Integer(request.getParameter("id"));
		NmsFunction role=nfService.findById(id);
		
		return role;
	}
	
	@RequestMapping(value = "/getNmsFunctionByFunctionDesc", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public NmsFunction getNmsFunctionByFunctionDesc(HttpServletRequest request)
			throws Exception {
		
		String functionDesc=request.getParameter("functionDesc");
		NmsFunction role=nfService.findByFunctionDesc(functionDesc);
		
		return role;
	}
}
