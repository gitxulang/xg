package iie.controller;

import iie.pojo.NmsLicense;
import iie.service.NmsAuditLogService;
import iie.service.NmsLicenseService;
import iie.tools.DesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/License")
public class NmsLicenseCtrl {
    @Autowired
    NmsLicenseService licenseService;

    @Autowired
    NmsAuditLogService auditLogService;


    @SuppressWarnings("static-access")
    @RequestMapping(value = "/Licensecreate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> licenseCreate(HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();

        String stype = request.getParameter("type");
        if (stype == null || !isNumber(stype) || !(stype.equals("0") || stype.equals("1") || stype.equals("2"))) {
            data.put("state", "1");
            data.put("info", "证书时间限制类别必须为0,1,2的数字！");
            return data;
        }

        String sday = request.getParameter("day");
        if (sday == null || !isNumber(sday)) {
            data.put("state", "1");
            data.put("info", "证书时间有效天数必须是数字！");
            return data;
        }

        String scomputer = request.getParameter("computer");
        if (scomputer == null || !isNumber(scomputer)) {
            data.put("state", "1");
            data.put("info", "证书限制终端数量必须是数字！");
            return data;
        }

        int type = Integer.valueOf(stype);
        int day = Integer.valueOf(sday);
        if (day <= 0) {
            data.put("state", "1");
            data.put("info", "证书时间有效天数必须为大于0的正整数！");
            return data;
        }

        int computer = Integer.valueOf(scomputer);
        if (computer <= 0) {
            data.put("state", "1");
            data.put("info", "证书限制终端数量必须为大于0的正整数！");
            return data;
        }

        String uniquecode = request.getParameter("uniquecode");
        if (uniquecode == null || uniquecode.equals("")) {
            data.put("state", "1");
            data.put("info", "安装服务器唯一码不能为空！");
            return data;
        }

        // 获取时间戳作为生成序列号唯一标识符
        long timestamp = System.currentTimeMillis();

        String res = type + "#" + day + "#" + computer + "#" + uniquecode + "#" + timestamp;
        System.out.println("加密前的证书序列号 = " + res);

        DesUtil des = new DesUtil();
        String ret = null;
        try {
            ret = des.encrypt(res);
        } catch (Exception e) {
            System.out.println(e.toString());
            data.put("state", "1");
            data.put("info", "证书序列号生成失败！");
            return data;
        }

        System.out.println("加密后的证书序列号 = " + ret);
        data.put("state", "0");
        data.put("info", ret);

        return data;
    }


    @SuppressWarnings("static-access")
    @RequestMapping(value = "/Licenseregister", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> licenseRegister(HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();

        // 获取前台传递的证书序列号
        String license = request.getParameter("license");
        if (license == null || license.equals("")) {
            data.put("state", "1");
            data.put("info", "您输入的激活码字符串不能为空！");
            return data;
        }

        // 解析前台传递的证书序列号
        System.out.println("解密前的证书序列号 = " + license);
        DesUtil des = new DesUtil();
        String ret = null;
        try {
            ret = des.decrypt(license);
        } catch (Exception e) {
            System.out.println(e.toString());
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法证书序列号）！");
            return data;
        }
        System.out.println("解密后的证书序列号 = " + ret);

        // 分解解密后的证书序列号获取参数值
        String[] arr = ret.split("#");
        if (arr.length != 5) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（序列号参数数量不正确）！");
            return data;
        }

        // 判断参数type是否合法
        if (arr[0] == null || arr[0].equals("") || !isNumber(arr[0])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法证书类型参数）！");
            return data;
        }
        int type = Integer.valueOf(arr[0]);

        // 判断参数type是否合法
        if (arr[1] == null || arr[1].equals("") || !isNumber(arr[1])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法限制天数参数）！");
            return data;
        }
        int day = Integer.valueOf(arr[1]);

        // 判断参数computer是否合法
        if (arr[2] == null || arr[2].equals("") || !isNumber(arr[2])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法限制终端数参数）！");
            return data;
        }
        int computer = Integer.valueOf(arr[2]);

        // 判断参数uniquecode是否合法
        if (arr[3] == null || arr[3].equals("")) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法服务器唯一序列号参数）！");
            return data;
        }
        String uniquecode = arr[3];

        // 判断参数timestamp是否合法
        if (arr[4] == null || arr[4].equals("") || !isNumber(arr[4])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法时间戳参数）！");
            return data;
        }
        long timestamp = Long.valueOf(arr[4]);

        System.out.println("type=" + type + ", day=" + day +
                ", computer=" + computer + ", uniquecode=" + uniquecode + ", timestamp=" + timestamp);

        // 0表示时间限制序列号, 1表示时间永久序列号, 2表示服务器安装不限制且时间永久序列号
        if (licenseService.findByLicense(license) > 0) {
            data.put("state", "1");
            data.put("info", "该序列号证书已经被注册过，不可重复注册！");
            return data;
        } else {
            System.out.println("该序列号证书还没有被注册过");
        }

        if (type == 0 || type == 1) {
            String realuniquecode = getUniqueCode("/etc/.systeminfo");
            System.out.println("realuniquecode = " + realuniquecode + ", uniquecode = " + uniquecode);

            if (realuniquecode == null || !realuniquecode.equals(uniquecode)) {
                data.put("state", "1");
                data.put("info", "您的激活码只能安装在唯一标识为 " + uniquecode + " 涉密专用服务器！");
                return data;
            }

            // 激活成功存入数据库
            NmsLicense obj = new NmsLicense();
            obj.setRegtime(timestamp);
            obj.setLicense(license);

            if (!licenseService.save(obj)) {
                data.put("state", "1");
                data.put("info", "证书序列号激活失败（存入数据库失败）！");
                return data;
            }
        } else if (type == 2) {
            // 激活成功存入数据库
            NmsLicense obj = new NmsLicense();
            obj.setRegtime(timestamp);
            obj.setLicense(license);

            if (!licenseService.save(obj)) {
                data.put("state", "1");
                data.put("info", "证书序列号激活失败（存入数据库失败）！");
                return data;
            }

        } else {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法证书类型参数）！");
            return data;
        }

        data.put("state", "0");
        data.put("info", "激活成功！");

        return data;
    }


    public String getUniqueCode(String name) {
        String uniquecode = null;

        try {
            // 判断文件是否存在
            File file = new File(name);
            if (!(file.isFile() && file.exists())) {
                return null;
            }


            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bf = new BufferedReader(inputReader);
            String str;

            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if (str != null && !str.equals("")) {

                    System.out.println(str);

                    if (str.contains("标识码（产品唯一标识）")) {
                        // 中科方德的产品唯一标识
                        if (str.contains("=")) {
                            String[] ret = str.split("=");
                            if (ret.length == 2) {
                                uniquecode = ret[1].trim();
                                break;
                            }
                        }

                        // 中标麒麟的产品唯一标识
                        if (str.contains(":")) {
                            String[] ret = str.split(":");
                            if (ret.length == 2) {
                                uniquecode = ret[1].trim();
                                break;
                            }
                        }

                        // 中标麒麟的产品唯一标识
                        if (str.contains("：")) {
                            String[] ret = str.split("：");
                            if (ret.length == 2) {
                                uniquecode = ret[1].trim();
                                break;
                            }
                        }
                    }

                    // 银河飞腾的产品唯一标识
                    if (str.contains("ID=")) {
                        String[] ret = str.split("=");
                        if (ret.length == 2) {
                            uniquecode = ret[1].trim();
                            break;
                        }
                    }
                }
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniquecode;
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/Licenseinfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, String> licenseInfo(HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();

        NmsLicense licenseObj = licenseService.getLastLicense();
        if (licenseObj == null) {
            data.put("state", "2");
            data.put("info", "系统未激活!");
            return data;
        }

        // 解析前台传递的证书序列号
        //System.out.println("解密前的证书序列号 = " + licenseObj.getLicense());
        DesUtil des = new DesUtil();
        String ret = null;
        try {
            ret = des.decrypt(licenseObj.getLicense());
        } catch (Exception e) {
            System.out.println(e.toString());
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法证书序列号）！");
            return data;
        }
        System.out.println("解密后的证书序列号 = " + ret);

        // 分解解密后的证书序列号获取参数值
        String[] arr = ret.split("#");
        if (arr.length != 5) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（序列号参数数量不正确）！");
            return data;
        }

        // 判断参数type是否合法
        if (arr[0] == null || arr[0].equals("") || !isNumber(arr[0])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法证书类型参数）！");
            return data;
        }
        int type = Integer.valueOf(arr[0]);

        // 判断参数type是否合法
        if (arr[1] == null || arr[1].equals("") || !isNumber(arr[1])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法限制天数参数）！");
            return data;
        }
        int day = Integer.valueOf(arr[1]);

        // 判断参数computer是否合法
        if (arr[2] == null || arr[2].equals("") || !isNumber(arr[2])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法限制终端数参数）！");
            return data;
        }
        int computer = Integer.valueOf(arr[2]);

        // 判断参数uniquecode是否合法
        if (arr[3] == null || arr[3].equals("")) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法服务器唯一序列号参数）！");
            return data;
        }
        String uniquecode = arr[3];

        // 判断参数timestamp是否合法
        if (arr[4] == null || arr[4].equals("") || !isNumber(arr[4])) {
            data.put("state", "1");
            data.put("info", "证书序列号解析失败（非法时间戳参数）！");
            return data;
        }
        long timestamp = Long.valueOf(arr[4]);

        /**
        System.out.println(
                ", type=" + type +
                        ", day=" + day +
                        ", computer=" + computer +
                        ", uniquecode=" + uniquecode +
                        ", timestamp=" + timestamp);
         **/

        // 计算时间有效期
        data.put("state", "0");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long deadtime = timestamp + day * 24 * 3600 * 1000L;

        //System.out.println("timestamp=" + timestamp + ", registertime=" + sdf.format(timestamp));
        //System.out.println("deadtime=" + deadtime + ", deadtime=" + sdf.format(deadtime));

        String stime = sdf.format(deadtime);
        //System.out.println("证书有效时间：" + stime);
        if (type == 0) {
            data.put("info", "激活码有效期至：" + stime + ", 授权客户端数：" + computer + " 台客户端！");
        } else {
            data.put("info", "激活码永久有效, 授权客户端数：" + computer + " 台客户端！");
        }

        return data;
    }


    @RequestMapping(value = "/getLastLicense", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsLicense getLastLicense(HttpServletRequest request) {
        NmsLicense res = licenseService.getLastLicense();
        if (res == null) {
            return new NmsLicense();
        } else {
            return res;
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<NmsLicense> getAll() {
        List<NmsLicense> res = licenseService.getAll();
        return res;
    }

    @RequestMapping(value = "/findLicenseById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public NmsLicense findLicenseById(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        NmsLicense obj = licenseService.findById(id);
        return obj;
    }

    public static boolean isNumber(Object o) {
        return (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
    }

}
