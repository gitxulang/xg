package iie.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import iie.pojo.NmsCollectModule;
import iie.service.NmsCollectModuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/CollectModule")
public class NmsCollectModuleCtrl {
	
	@Autowired
	NmsCollectModuleService cmService;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
    public List<NmsCollectModule> addUser2(HttpServletRequest request) {
        String username=request.getParameter("name");
        String password=request.getParameter("pwd");
   //     System.out.println("username is:"+username);
   //     System.out.println("password is:"+password);
        List<NmsCollectModule> res = cmService.getAll();
/*        for(NmsCollectModule ncm : res){
        	System.out.println(ncm.getId()+","+ncm.getCollName());
        }*/
        return res;
    }
}
