package iie.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class IllegalCharacter implements Filter {
	private static List<Pattern> patterns = null;

	public void destroy() {
		
	}

    private static List<Object[]> getXssPatternList() {
        List<Object[]> ret = new ArrayList<Object[]>();
        ret.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"</script>", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|οnerrοr=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        return ret;
    }
    
    private static List<Pattern> getPatterns() {
        if (patterns == null) {
            List<Pattern> list = new ArrayList<Pattern>();
            String regex = null;
            Integer flag = null;
            int arrLength = 0;
            for (Object[] arr : getXssPatternList()) {
                arrLength = arr.length;
                for (int i = 0; i < arrLength; i++) {
                    regex = (String) arr[0];
                    flag = (Integer) arr[1];
                    list.add(Pattern.compile(regex, flag));
                }
            }
            patterns = list;
        }
        return patterns;
    }
    
    public boolean stripXss(String value) {
        if (StringUtils.isNotBlank(value)) {
            Matcher matcher = null;
            for (Pattern pattern : getPatterns()) {
                matcher = pattern.matcher(value);
                // 过来特殊脚本的关键字
                if (matcher.find()) {
                	if (!value.equals(matcher.replaceAll(""))) {
                		System.out.println("[DEBUG] IllegalCharacter.java->stripXss: 用户输入了非法表达式 ：" + value + "\n");
                		System.out.println("[ERROR] 用户输入了攻击关键字 = " + value + "\n");
                        return true;
                	}
                }
            }

            // 过来特殊字符
            if (StringUtils.isNotBlank(value)) {
              String specialStr = "%20|!=|--|;|'|%|#|[+]|//|\\|<|>";
                for (String str : specialStr.split("\\|")) {
                    if (value.indexOf(str) > -1) {
                    	System.out.println("[DEBUG] IllegalCharacter.java->stripXss: 用户输入了非法字符 ：" + str + "\n");
                    	return true;
                    }
                }
            }
        }
        return false;
    }	

	@SuppressWarnings({ "rawtypes", "unused" })
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest servletrequest = (HttpServletRequest) request;
		HttpServletResponse servletresponse = (HttpServletResponse) response;
		boolean status = false;
		java.util.Enumeration params = request.getParameterNames();
		String param = "";
		String paramValue = "";
		servletresponse.setContentType("text/html");
		servletresponse.setCharacterEncoding("utf-8");
		while (params.hasMoreElements()) {
			param = (String) params.nextElement();
			String values = request.getParameter(param);
			paramValue = "";
			paramValue = paramValue + values;
			if (stripXss(paramValue)) {
				status = true;
				break;
			}
		}
		if (status) {
			servletresponse.getWriter().write("对不起，您提交的表单中 " + paramValue + " 含有非法字符！");
			return;	
		} else {
			arg2.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException {

	}
}
