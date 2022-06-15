package iie.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = clearXss(values[i]);
		}
		return encodedValues;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return clearXss(value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map getParameterMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, String[]> requestMap = super.getParameterMap();
		Iterator<Entry<String, String[]>> it = requestMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String[]> entry = it.next();
			if (entry.getValue().length == 1) {
				paramMap.put(xssEncode(entry.getKey()),
						xssEncode(entry.getValue()[0]));
			} else {
				String[] values = entry.getValue();
				String value = "";
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
				paramMap.put(xssEncode(entry.getKey()),
						xssEncode(entry.getValue()[0]));
			}
		}
		return paramMap;
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (value != null && value instanceof String) {
			clearXss((String) value);
		}
		return value;
	}

	private String clearXss(String value) {
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		return XssFilterUtil.stripXss(value);
	}

	private static String xssEncode(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("'", "&apos;");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
		value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
		value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
		value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");	
		System.out.println("[DEBUG] XSSRequestWrapper.java->xssEncode: value = " + value + "\n");
		return value;
	}
}