package iie.rest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import iie.pojo.NmsAdmin;
import iie.rest.HttpClientUtlis;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RestClient {
	private static Logger logger = Logger.getLogger(RestClient.class);
	public RestClient() {
		
	}

	public String getPropertyUrl(String key) {
		Properties pps = new Properties();
		try {
			String filePath = this.getClass().getResource("").getPath() + "../../conf/url.properties";
			InputStream in = new FileInputStream(filePath);
			pps.load(in);
			String url = pps.getProperty(key);
			return url;
		} catch (Exception e) {
			System.err.println("[ERROR] can't find url.properties");
			return null;
		}
	}

	public String restClient_SSO(String appId, String appName, String token) {
        String sso_url = getPropertyUrl("sso_url");
//		sso_url="http://10.10.41.165:8080/TokenCheckController/getUserNameByToken.do";
        System.out.println("[DEBUG] restClient_SSO: sso_url=" + sso_url);
		JSONObject obj = new JSONObject();
		try {
			obj.put("appId", appId);
			obj.put("appName", appName);
			obj.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("[DEBUG] restClient_SSO: request string=" + obj.toString());
		String ret = "{}";
		try {
			ret = HttpClientUtlis.httpPostRequestWithJson(sso_url, obj.toString());
		} catch (UnsupportedEncodingException e) {
			System.out.println("[ERROR] restClient_SSO: request HttpClientUtlis.httpPostRequestWithJson failed: " + e.toString());
		}
		System.out.println("[DEBUG] restClient_SSO: request ret=" + ret);
		
		return ret;
	}
	
	public boolean restClient_JMSClient() {
        String jms_url = getPropertyUrl("jms_url");
        System.out.println("[DEBUG] restClient_JMSClient: jms_url=" + jms_url);
		try {
			String retStr = HttpClientUtlis.httpGetRequest(jms_url);
			System.out.println("[DEBUG] restClient_SSO: request HttpClientUtlis.httpGetRequest ret: " + retStr + "\n");
			JSONObject obj = new JSONObject(retStr);
			if (obj.getString("code") != null && obj.getString("code").equals("200")) {
				System.out.println("[DEBUG] restClient_SSO: request HttpClientUtlis.httpGetRequest return 200\n");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("[ERROR] restClient_JMSClient: request HttpClientUtlis.httpGetRequest failed: " + e.toString() + "\n");
			return false;
		}
	}

	/**
	 * 登录授权验证接口
	 * @param user
	 * @return
	 */
	public boolean restClient_auth(String user) {
		try {
			boolean isAllow = Boolean.valueOf(getPropertyUrl("isAllow"));//验证开关 true-需要登录验证 false-跳过验证
			String agUrl = getPropertyUrl("agUrl");//验证地址
			String systemId = getPropertyUrl("systemId");
			String systemName = getPropertyUrl("systemName");
			if (isAllow) {
				// 组装验证接口信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("systemId", systemId);
				params.put("systemName", systemName);
				params.put("usr", user);
				logger.info("登录授权验证接口-before=>>>>>>>>" + JSON.toJSONString(params));
				String retStr = HttpClientUtlis.httpGetRequest(agUrl, params);
				logger.info("登录授权验证接口-after=>>>>>>>>" + retStr);
				if (StringUtils.isEmpty(retStr) || (!"OK".equals(new JSONObject(retStr).getString("isAllow")))) {
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("登录授权验证接口-ERROR=>>>>>>>>\n", e);
			return false;
		}
		return true;
	}
	
	public static void main(final String[] args) {
		RestClient rc = new RestClient();
		rc.restClient_JMSClient();
	//	rc.restClient_SSO("SYSMPT-0000000","系统管理基础平台","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBOYW1lIjoieXVud2VpIiwiZXhwIjoxNjA0OTAyMjI5LCJhcHBJZCI6IjEyMyIsInVzZXJJZCI6IjEifQ.SCYbi1HuiiF6tJ2ddhNCu2z18Xbic9Z2X4hAl77ReyA");
	}
}

