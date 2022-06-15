package iie.tools;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		XSSRequestWrapper XSSRequestWrapper = new XSSRequestWrapper(
				(HttpServletRequest) servletRequest);
		filterChain.doFilter(XSSRequestWrapper, servletResponse);
	}

	@Override
	public void destroy() {

	}
}