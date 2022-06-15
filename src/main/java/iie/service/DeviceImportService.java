package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsDepartment;
import iie.pojo.NmsRule;
import iie.pojo.NmsRuleAsset;
import iie.tools.XssFilterUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("DeviceImportService")
public class DeviceImportService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Autowired
	NmsAssetService nmsAssetService;
	
	@Autowired
	NmsRuleService nmsRuleService;
	
	public boolean isIP(String text) {
		if (text != null && !text.isEmpty()) {
			// 定义正则表达式
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			if (text.matches(regex)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public int inputAssets(String path){
		List<NmsAsset> temp = new ArrayList<NmsAsset>();
		int res = 0;
		try {
			FileInputStream fileIn = new FileInputStream(path);
			Workbook wb0 = new HSSFWorkbook(fileIn);
			Sheet sht0 = wb0.getSheetAt(0);
			XssFilterUtil x = new XssFilterUtil();
			System.out.println("sht0.getLastRowNum()=" + sht0.getLastRowNum());
			for (int i = 1; i <= sht0.getLastRowNum(); i++) {
				Row r = sht0.getRow(i);
				NmsAsset asset = new NmsAsset();
				asset.setOnline(0);
				
				  if (r.getCell(3) != null) {
					//	System.out.println("[DEBUG] a_ip: " + r.getCell(3).getStringCellValue());
						if (r.getCell(3).getCellType() == 1) {
							if (!isIP(r.getCell(3).getStringCellValue())) {
								res = 7;
								return res;
							}
							asset.setAIp(x.stripXss(r.getCell(3).getStringCellValue()));
						} else if (r.getCell(3).getCellType() == 0) {
							if (!isIP(String.valueOf(r.getCell(3).getNumericCellValue()))) {
								res = 7;
								return res;
							}
							asset.setAIp(x.stripXss(String.valueOf(r.getCell(3).getNumericCellValue())));
						}
					}
					
				  asset.setBmIp("");
				  asset.setYwIp("");
//					if (r.getCell(4) != null) {
//					//	System.out.println("[DEBUG] bm_ip: " + r.getCell(4).getStringCellValue());
//						if (r.getCell(4).getCellType() == 1) {
//							if (r.getCell(4).getStringCellValue() == null) {
//								asset.setBmIp("");
//							} else {
//								asset.setBmIp(x.stripXss(r.getCell(4).getStringCellValue()));
//							}
//						} else if (r.getCell(4).getCellType() == 0) {
//							if (r.getCell(4).getStringCellValue() == null) {
//								asset.setBmIp("");
//							} else {
//								asset.setBmIp(x.stripXss(String.valueOf(r.getCell(4).getNumericCellValue())));
//							}
//						}
//						if (asset.getBmIp() == null) {
//							asset.setBmIp("");
//						}
//					}
//					
//					if (r.getCell(5) != null) {
//					//	System.out.println("[DEBUG] yw_ip: " + r.getCell(5).getStringCellValue());
//						if (r.getCell(5).getCellType() == 1) {
//							if (r.getCell(5).getStringCellValue() == null) {
//								asset.setYwIp("");
//							} else {
//								asset.setYwIp(x.stripXss(r.getCell(5).getStringCellValue()));
//							}
//						} else if (r.getCell(5).getCellType() == 0) {
//							if (r.getCell(5).getStringCellValue() == null) {
//								asset.setYwIp("");
//							} else {
//								asset.setYwIp(x.stripXss(String.valueOf(r.getCell(5).getNumericCellValue())));
//							}
//						}
//						if (asset.getYwIp() == null) {
//							asset.setYwIp("");
//						}
//					}
					
				if (r.getCell(1) != null) {
					if (r.getCell(1).getCellType() == 1) {
						asset.setAName(x.stripXss(r.getCell(1).getStringCellValue()));
					} else if (r.getCell(1).getCellType() == 0) {
						asset.setAName(x.stripXss(String.valueOf(r.getCell(1).getNumericCellValue())));
					}
				}
				if (r.getCell(0) != null) {
					if (r.getCell(0).getCellType() == 1) {
						asset.setANo(x.stripXss(r.getCell(0).getStringCellValue()));
					} else if (r.getCell(0).getCellType() == 0) {
						asset.setANo(x.stripXss(String.valueOf(r.getCell(0).getNumericCellValue())));
					}	
				}
				if (r.getCell(2) != null) {
					NmsAssetType nmsAssetType = null;
					try {
						String type = r.getCell(2).getStringCellValue().substring(0,r.getCell(2).getStringCellValue().indexOf("/")).trim();
						String subtype = r.getCell(2).getStringCellValue().substring(r.getCell(2).getStringCellValue().indexOf("/")+1, r.getCell(2).getStringCellValue().length());
					    nmsAssetType = nmsAssetService.findTypeByName(type, subtype.trim());
						if (nmsAssetType == null) {
						//	res = 5;
						//	return res;
							// 如果界面填写类别错误默认添加其它设备/其它设备
							System.out.println("[DEBUG] type=" + type + ", subtype= " + subtype + " not found, default 其它设备/其它设备");
							nmsAssetType = nmsAssetService.findTypeByName("其它设备", "其它设备");
						}
						asset.setNmsAssetType(nmsAssetType);
					} catch (Exception e) {
						nmsAssetType = nmsAssetService.findTypeByName("其它设备", "其它设备");
						asset.setNmsAssetType(nmsAssetType);
					}
				}
				if (r.getCell(4) != null) {
					if(r.getCell(4).getCellType() == 1){
						asset.setAPos(x.stripXss(r.getCell(4).getStringCellValue()));
					}else if(r.getCell(4).getCellType() == 0){
						asset.setAPos(x.stripXss(String.valueOf(r.getCell(4).getNumericCellValue())));
					}	
				}
				if (r.getCell(5) != null) {
					if(r.getCell(5).getCellType() == 1){
						asset.setAManu(x.stripXss(r.getCell(5).getStringCellValue()));
					}else if(r.getCell(5).getCellType() == 0){
						asset.setAManu(x.stripXss(String.valueOf(r.getCell(5).getNumericCellValue())));
					}	
				}
				if (r.getCell(6) != null) {
					if(r.getCell(6).getCellType() == 1){
						asset.setAUser(x.stripXss(r.getCell(6).getStringCellValue()));
					}else if(r.getCell(6).getCellType() == 0){
						asset.setAUser(x.stripXss(String.valueOf(r.getCell(6).getNumericCellValue())));
					}	
				}
				if (r.getCell(7) != null) {
					if (r.getCell(7).getCellType() == 1) {
						String dept = x.stripXss(r.getCell(7).getStringCellValue());
						NmsDepartment ndf = nmsAssetService.findDepartmentByName(dept);
						if (ndf == null) {
						//	res = 5;
						//	return res;
							System.out.println("[DEBUG] dept=" + dept + " not found, default 第一个部门");
							ndf = nmsAssetService.findDepartmentAll();
							if (ndf == null) {
								System.out.println("[DEBUG] 部门信息表为空,不能导入");
								res = 5;
								return res;
							} else {
								asset.setNmsDepartment(ndf);
							}
						} else {
							asset.setNmsDepartment(ndf);
						}
					} else if (r.getCell(7).getCellType() == 0) {
						NmsDepartment ndf = nmsAssetService.findDepartmentByName(String.valueOf(r.getCell(7).getNumericCellValue()));
						if (ndf == null) {
						//	res = 5;
						//	return res;
							System.out.println("[DEBUG] dept=" + String.valueOf(r.getCell(7).getNumericCellValue()) + " not found, default 第一个部门");
							ndf = nmsAssetService.findDepartmentAll();
							if (ndf == null) {
								System.out.println("[DEBUG] 部门信息表为空,不能导入");
								res = 5;
								return res;
							} else {
								asset.setNmsDepartment(ndf);
							}
						} else {
							asset.setNmsDepartment(ndf);
						}
					}
				}
				if (r.getCell(8) != null) {
					switch (x.stripXss(r.getCell(8).getStringCellValue())) {
						case "启用":
							asset.setColled((short) 0);
							break;
						case "禁用":
							asset.setColled((short) 1);
							break;
						default:
							asset.setColled((short) 0);
							break;
					}
				}
				if (r.getCell(9) != null) {
					switch (x.stripXss(r.getCell(9).getStringCellValue())) {
						case "专用代理":
							asset.setCollMode(0);
							break;
						case "ICMP协议":
							asset.setCollMode(1);
							break;
						case "SNMPv1和v2c协议":
							asset.setCollMode(2);
							break;
						case "SNMPv3协议":
							asset.setCollMode(3);
							break;
						default:
							asset.setCollMode(0);
							break;
					}
				}
				if (r.getCell(10) != null) {
					if(r.getCell(10).getCellType() == 1){
						asset.setRComm(x.stripXss(r.getCell(10).getStringCellValue()));
					}else if(r.getCell(10).getCellType() == 0){
						asset.setRComm(x.stripXss(String.valueOf(r.getCell(10).getNumericCellValue())));
					}else {
						asset.setRComm("");
					}
				}
				if (r.getCell(11) != null) {
					if (r.getCell(11).getCellType() == 1) {
						asset.setWComm(x.stripXss(r.getCell(11).getStringCellValue()));
					} else if (r.getCell(11).getCellType() == 0) {
						asset.setWComm(x.stripXss(String.valueOf(r.getCell(11).getNumericCellValue())));
					} else {
						asset.setWComm("");
					}
				}
				if (r.getCell(12) != null) {
					switch (r.getCell(12).getCellType()) {
                        case 1:  
                        	asset.setAuthPass(x.stripXss(r.getCell(12).getStringCellValue()));
                            break;  
                        case 0:  
                        	asset.setAuthPass(x.stripXss(String.valueOf((int)r.getCell(12).getNumericCellValue())));
                            break;  
                        default: 
                        	asset.setAuthPass("");
					} 
				}
                
				if(r.getCell(13) != null){
					System.out.println("Username=" + r.getCell(13).getStringCellValue());
					switch (r.getCell(13).getCellType()) {
                        case 1:  
                        	asset.setUsername(x.stripXss(r.getCell(13).getStringCellValue()));
                            break;  
                        case 0:  
                        	asset.setUsername(x.stripXss(String.valueOf((int)r.getCell(13).getNumericCellValue())));
                            break;  
                        default:  
                        	asset.setUsername("");
					} 
				}
				
				if(r.getCell(14) != null){
					System.out.println("Password=" + r.getCell(14).getStringCellValue());
					switch (r.getCell(14).getCellType()) {
                        case 1:  
                        	asset.setPassword(x.stripXss(r.getCell(14).getStringCellValue()));
                            break;  
                        case 0:  
                        	asset.setPassword(x.stripXss(String.valueOf((int)r.getCell(14).getNumericCellValue())));
                            break;  
                        default:  
                        	System.out.println("Password=" + r.getCell(14).getStringCellValue());
                        	asset.setPassword("");
					} 
				}
				
				if(r.getCell(15) != null){
					System.out.println("Sshport=" + r.getCell(15).getStringCellValue());
					switch (r.getCell(15).getCellType()) {
                        case 1:  
                        	asset.setSshport(x.stripXss(r.getCell(15).getStringCellValue()));
                            break;  
                        case 0:  
                        	asset.setSshport(x.stripXss(String.valueOf((int)r.getCell(15).getNumericCellValue())));
                            break;  
                        default:  
                        	asset.setSshport("");
					} 
				}
					
				if (r.getCell(16) != null) {
					System.out.println("date=" + r.getCell(16).getDateCellValue());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if (r.getCell(16).getDateCellValue() != null) {
						asset.setADate(sdf.format(r.getCell(16).getDateCellValue()));
					} else {
						asset.setADate("1970-01-01");
					}
				}
				temp.add(asset);
			}
			fileIn.close();		
			res = saveAssets(temp);
		} catch (Exception e) {
			e.printStackTrace();
			res = 1;
		}
		
		return res;
	}
	

	public int saveAssets(List<NmsAsset> assets){
		int res = 0;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		try {
			for (NmsAsset asset : assets) {
				if (asset.getAIp() == null) {
					continue;
				}
				
				int flag = 0;
		        if (asset.getNmsAssetType().getChType().equals("专用数通设备")) {
		        	// 如果添加设备是专用数据设备就得判断是否和其它非专用数据设备中IP地址重复
		        	if (nmsAssetService.ifIpAndType(asset.getAIp(), "专用数通设备")) {
		        		flag = 1;
		        	}
		        } else {
		        	// 如果新增的设备是非专用数据设备就得判断是否其它所有设备中IP地址重复
		        	if (nmsAssetService.ifIp(asset.getAIp())) {
		        		flag = 1;
		        	}
		        }
 				if (flag == 0) {
 					if (nmsAssetService.findByANo(asset.getANo()) == null) {
						asset.setId(0);
						asset.setDeled(0);
						asset.setItime(new Timestamp(System.currentTimeMillis()));
						
						System.out.println("[DEBUG] id:" + asset.getId() + ", ip:" + asset.getAIp() + ", name:" + asset.getAName() + ", no:" + asset.getANo() + 
								", pos:" + asset.getAPos() + ", namu:" + asset.getAManu() + ", date:" + asset.getADate() + ", user:" + asset.getAUser() + 
								", colled:" + asset.getColled() + ", collmode:" + asset.getCollMode() + ", authpass:" + asset.getAuthPass() + ", rcomm:" + 
								asset.getRComm() + ", wcomm:" + asset.getWComm() + ", deled:" + asset.getDeled() + ", username:" + asset.getUsername() + ", password:" + asset.getPassword() + ", sshport:" + asset.getSshport() + ", itime:" + asset.getItime());
						
						session.save(asset);
	
						//生成资产告警规则
						NmsAssetType type = asset.getNmsAssetType();
						List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(type.getId());
						for (NmsRule rule : rules) {
							NmsRuleAsset ruleAsset = new NmsRuleAsset();
							ruleAsset.setId(0);
							ruleAsset.setItime(new Timestamp(System.currentTimeMillis()));
							ruleAsset.setNmsAsset(asset);
							ruleAsset.setNmsAssetType(type);
							ruleAsset.setDType(rule.getDType());
							ruleAsset.setRName(rule.getRName());
							ruleAsset.setRContent(rule.getRContent());
							ruleAsset.setRUnit(rule.getRUnit());
							ruleAsset.setRSeq(rule.getRSeq());
							ruleAsset.setREnable(rule.getREnable());
							ruleAsset.setRValue1(rule.getRValue1().intValue());
							ruleAsset.setRValue2(rule.getRValue2().intValue());
							ruleAsset.setRValue3(rule.getRValue3().intValue());
							session.save(ruleAsset);
						}
 					} else {
 						res = 6;
 					}
				} else {
					res = 2;
				}
			}
			tran.commit();
		} catch (Exception e) {
			e.printStackTrace();
			res = 3;
		} finally {
			session.close();
		}
		return res;
	}
}
