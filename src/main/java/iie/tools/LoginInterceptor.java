package iie.tools;

import iie.controller.NmsAdminCtrl;
import iie.pojo.NmsAdmin;
import iie.pojo.NmsRoleFunction;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

public class LoginInterceptor implements HandlerInterceptor {
	private static final PathMatcher pathMatcher = new AntPathMatcher();

    @SuppressWarnings("deprecation")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        NmsAdmin user = (NmsAdmin) request.getSession().getAttribute("user");
        String sessionToken = (String) request.getSession().getAttribute("token");
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"  + request.getServerPort() + request.getContextPath(); 
        boolean validateToken = true;
        if (!isProtectedUrl(request)) {
            String token = request.getHeader("Authorization");
            if (token == null || token == "") {
            	validateToken = false;
            } else {
            	token = URLDecoder.decode(token);
            	validateToken = JwtUtil.validateToken(token, sessionToken);
            }
        }
        if (user != null && validateToken) {
        	int roleId = user.getNmsRole().getId();
        	StringBuffer path = new StringBuffer();
        	if (NmsAdminCtrl.RoleFunction != null) {
	        	for (NmsRoleFunction nrc : NmsAdminCtrl.RoleFunction) {
	        		if (nrc.getNmsRole().getId() == roleId) { 
	        			String urls  = nrc.getNmsFunction().getFunUrl();
	        			if (urls != null && urls != "" && urls != " " && !urls.isEmpty()) {
	        				path.append(urls);
	        				path.append(";");
	        			}
	        		}
	        	}
        	}
        	if (path.toString().contains(request.getServletPath())) {
        	//	System.out.println("[DEBUG] Accept: " + request.getServletPath());
        		return true;
        	} else {
        		if(request.getServletPath().contains("input")) {
        			System.out.println("workaround");
        			return true;
        		}
        		System.out.println("[DEBUG] Refuse: " + request.getServletPath());
                response.setHeader("REDIRECT", "FORBIDDEN"); // 设置AJAX调用该接口无权限访问标记
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        		return false;
        	}
        } else {
        	// 如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是AJAX请求, 需要重定向到登录页面
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                response.setHeader("REDIRECT", "REDIRECT");
                response.setHeader("CONTENTPATH", basePath + "/ant/login.html");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
            	response.sendRedirect(basePath + "/ant/login.html");
            }
            return false;
		}
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	
    }
    
    private boolean isProtectedUrl(HttpServletRequest request) {
        return (pathMatcher.match("/Admin/loginAjax", request.getServletPath()) || 
        		pathMatcher.match("/AuditLog/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/alarm/list/page/condition/exportExcel", request.getServletPath()) ||
				pathMatcher.match("/alarm/list/page/soft/condition/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/alarm/list/statics/asset/condition/exportExcel", request.getServletPath()) ||
				pathMatcher.match("/alarm/list/statics/soft/condition/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/alarm/list/alarm/condition/exportExcel", request.getServletPath()) ||
				pathMatcher.match("/alarm/list/alarm/soft/condition/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/Asset/list/date/reportSelect/exportExcel", request.getServletPath()) ||
				pathMatcher.match("/Asset/exportTemplate", request.getServletPath()) ||
        		pathMatcher.match("/Soft/exportTemplate", request.getServletPath()) ||
				pathMatcher.match("/Soft/list/date/reportSelect/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/CpuInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/DiskioInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/DynamicInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/FilesysInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/MemInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/MysqlInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/NetifInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/performance/list/page/condition/realtime/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/PingInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/ProcessInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/SoftInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/AccountInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/AppInfo/list/date/ExportExcel", request.getServletPath()) ||
				pathMatcher.match("/MiddlewareBasic/list/date/ExportExcel", request.getServletPath()) ||
				pathMatcher.match("/MiddlewareConfig/list/date/ExportExcel", request.getServletPath()) ||
				pathMatcher.match("/MiddlewareStatus/list/date/ExportExcel", request.getServletPath()) ||
				pathMatcher.match("/MiddlewareInstance/list/date/ExportExcel", request.getServletPath()) ||
                pathMatcher.match("/DatabaseBasic/list/date/ExportExcel", request.getServletPath()) ||
                pathMatcher.match("/DatabaseConfig/list/date/ExportExcel", request.getServletPath()) ||
                pathMatcher.match("/DatabaseStatus/list/date/ExportExcel", request.getServletPath()) ||
                pathMatcher.match("/DatabaseSql/list/date/ExportExcel", request.getServletPath()) ||
                pathMatcher.match("/DatabaseStorage/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/Rule/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/StaticInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/TomcatInfo/list/date/ExportExcel", request.getServletPath()) ||
        		pathMatcher.match("/AuditLog/exportExcel", request.getServletPath()) ||
        		pathMatcher.match("/Admin/updatePwdOfLongTime", request.getServletPath()) || 
        		pathMatcher.match("/css/**", request.getServletPath()) ||
        		pathMatcher.match("/images/**", request.getServletPath()) ||
        		pathMatcher.match("/js/**", request.getServletPath()) ||
        		pathMatcher.match("/login.jsp", request.getServletPath()) ||
        		pathMatcher.match("/topo/**", request.getServletPath()) ||
        		pathMatcher.match("/roompage/**", request.getServletPath()) ||
        		pathMatcher.match("/ant/**", request.getServletPath()) ||
        		pathMatcher.match("/**.html", request.getServletPath()));
    }
}
