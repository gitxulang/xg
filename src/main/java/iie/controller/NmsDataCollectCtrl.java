package iie.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import iie.service.NmsDataCollectServcie;

/**
 * @author TianYu
 * 2019-4-1
 * 数据采集接口
 */
@Controller
@RequestMapping("/DataCollect")
public class NmsDataCollectCtrl {
	
	@Autowired
	NmsDataCollectServcie dcService;
	
	@RequestMapping(value = "/monitor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public int info(HttpServletRequest request){
		int type = Integer.parseInt(request.getParameter("type"));
        int index = Integer.parseInt(request.getParameter("index"));
        String ip = request.getParameter("ip");
        String data = request.getParameter("data");
        
		return 0;//成功
		//return 1;//错误
	}

}
