package iie.controller;

import iie.tools.CaptchaUtil;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/captcha")
public class NmsCaptchaCtrl {
	
	@RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
	@ResponseBody
	public void getCaptcha(HttpServletRequest req, HttpServletResponse resp) {
		 Map<String, Object> codeMap = CaptchaUtil.generateCodeAndPic();

	        // 将四位数字的验证码保存到Session中。
	        HttpSession session = req.getSession();
	        session.setAttribute("code", codeMap.get("code").toString());

	        // 禁止图像缓存。
	        resp.setHeader("Pragma", "no-cache");
	        resp.setHeader("Cache-Control", "no-cache");
	        resp.setDateHeader("Expires", -1);

	        resp.setContentType("image/jpeg");

	        // 将图像输出到Servlet输出流中。
	        ServletOutputStream sos;
	        try {
	            sos = resp.getOutputStream();
	            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
	            sos.close();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	}
}
