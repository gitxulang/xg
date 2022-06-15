package iie.service;

import iie.pojo.NmsRuleSoft;
import iie.pojo.NmsSoft;
import iie.pojo.NmsAssetType;
import iie.pojo.NmsDepartment;
import iie.pojo.NmsRule;
import iie.tools.XssFilterUtil;

import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("SoftImportService")
public class SoftImportService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Autowired
	NmsSoftService nmsSoftService;
	
	@Autowired
	NmsRuleService nmsRuleService;

	@SuppressWarnings("static-access")
	public int inputSofts(String path){
		List<NmsSoft> temp = new ArrayList<NmsSoft>();
		int res = 0;
		try {
			FileInputStream fileIn = new FileInputStream(path);
			Workbook wb0 = new HSSFWorkbook(fileIn);
			Sheet sht0 = wb0.getSheetAt(0);
			XssFilterUtil x = new XssFilterUtil();
			System.out.println("sht0.getLastRowNum()=" + sht0.getLastRowNum());
			for (int i = 1; i <= sht0.getLastRowNum(); i++) {
				Row r = sht0.getRow(i);
				NmsSoft soft = new NmsSoft();
				if (r.getCell(0) != null) {
					if(r.getCell(0).getCellType() == 1){
						soft.setAIp(x.stripXss(r.getCell(0).getStringCellValue()));
					}else if(r.getCell(0).getCellType() == 0){
						soft.setAIp(x.stripXss(String.valueOf(r.getCell(0).getNumericCellValue())));
					}
				}
				if (r.getCell(1) != null) {
					if(r.getCell(1).getCellType() == 1){
						soft.setAPort(x.stripXss(r.getCell(1).getStringCellValue()));
					}else if(r.getCell(1).getCellType() == 0){
						soft.setAPort(x.stripXss(String.valueOf(r.getCell(1).getNumericCellValue())));
					}
				}
				if (r.getCell(2) != null) {
					if(r.getCell(2).getCellType() == 1){
						soft.setAName(x.stripXss(r.getCell(2).getStringCellValue()));
					}else if(r.getCell(2).getCellType() == 0){
						soft.setAName(x.stripXss(String.valueOf(r.getCell(2).getNumericCellValue())));
					}	
				}
				if (r.getCell(3) != null) {
					String type = r.getCell(3).getStringCellValue().substring(0,r.getCell(3).getStringCellValue().indexOf("/"));
					String subtype = r.getCell(3).getStringCellValue().substring(r.getCell(3).getStringCellValue().indexOf("/")+1, r.getCell(3).getStringCellValue().length());
					System.out.println("[DEBUG] type = " + type);
					System.out.println("[DEBUG] subtype = " + subtype);
					NmsAssetType nmsAssetType = nmsSoftService.findTypeByName(type, subtype);
					if (nmsAssetType == null) {
						res = 4;
						return res;
					}
					soft.setNmsAssetType(nmsAssetType);
				}
				if (r.getCell(4) != null) {
					if(r.getCell(4).getCellType() == 1){
						soft.setAPos(x.stripXss(r.getCell(4).getStringCellValue()));
					}else if(r.getCell(4).getCellType() == 0){
						soft.setAPos(x.stripXss(String.valueOf(r.getCell(4).getNumericCellValue())));
					}	
				}
				if (r.getCell(5) != null) {
					if(r.getCell(5).getCellType() == 1){
						soft.setAUser(x.stripXss(r.getCell(5).getStringCellValue()));
					}else if(r.getCell(5).getCellType() == 0){
						soft.setAUser(x.stripXss(String.valueOf(r.getCell(5).getNumericCellValue())));
					}	
				}
				
				if (r.getCell(6) != null) {
					if(r.getCell(6).getCellType() == 1){
						String dept = x.stripXss(r.getCell(6).getStringCellValue());
						System.out.println("[DEBUG] dept = " + dept);
						NmsDepartment ndf = nmsSoftService.findDepartmentByName(dept);
						if (ndf == null) {
							res = 5;
							return res;
						} else {
							soft.setNmsDepartment(ndf);
						}
					} else if (r.getCell(6).getCellType() == 0) {
						NmsDepartment ndf = nmsSoftService.findDepartmentByName(String.valueOf(r.getCell(6).getNumericCellValue()));
						if (ndf == null) {
							res = 5;
							return res;
						} else {
							soft.setNmsDepartment(ndf);
						}
					}
				}
				if (r.getCell(7) != null) {
					if(r.getCell(7).getCellType() == 1){
						soft.setAManu(x.stripXss(r.getCell(7).getStringCellValue()));
					}else if(r.getCell(7).getCellType() == 0){
						soft.setAManu(x.stripXss(String.valueOf(r.getCell(7).getNumericCellValue())));
					}	
				}
				
				if (r.getCell(8) != null) {
					System.out.println("date=" + r.getCell(8).getDateCellValue());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if (r.getCell(8).getDateCellValue() != null) {
						soft.setADate(sdf.format(r.getCell(8).getDateCellValue()));
					} else {
						soft.setADate("1970-01-01");
					}
				}
				if (r.getCell(9) != null) {
					switch (x.stripXss(r.getCell(9).getStringCellValue())) {
						case "启用":
							soft.setColled((short) 0);
							break;
						case "禁用":
							soft.setColled((short) 1);
							break;
						default:
							soft.setColled((short) 0);
							break;
					}
				}
				if (r.getCell(10) != null) {
					switch (x.stripXss(r.getCell(10).getStringCellValue())) {
						case "专用代理":
							soft.setCollMode(0);
							break;
						default:
							soft.setCollMode(0);
							break;
					}
				}
				temp.add(soft);
			}
			fileIn.close();		
			res = saveSofts(temp);
		} catch (Exception e) {
			e.printStackTrace();
			res = 1;
		}
		return res;
	}
	
	public int saveSofts(List<NmsSoft> softs){
		int res = 0;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		try {
			for (NmsSoft soft : softs) {
				if (soft.getAIp() == null || soft.getAPort() == null) {
					continue;
				}
 				if (nmsSoftService.findByIpOrPort(soft.getAIp(), soft.getAPort()) == null) {
 					soft.setId(0);
 					soft.setDeled(0);
 					soft.setItime(new Timestamp(System.currentTimeMillis()));
					
					System.out.println("[DEBUG] id:" + soft.getId() + ", ip:" + soft.getAIp() + ", port:" + soft.getAPort() + ", name:" + soft.getAName()
							+ ", pos:" + soft.getAPos() + ", namu:" + soft.getAManu() + ", date:" + soft.getADate() + ", user:" + soft.getAUser() + 
							", colled:" + soft.getColled() + ", collmode:" + soft.getCollMode());
					session.save(soft);

					//生成软件告警规则
					NmsAssetType type = soft.getNmsAssetType();
					List<NmsRule> rules = nmsRuleService.findNmsRulesByAssetTypeId(type.getId());
					for (NmsRule rule : rules) {
						NmsRuleSoft ruleSoft = new NmsRuleSoft();
						ruleSoft.setItime(new Timestamp(System.currentTimeMillis()));
						ruleSoft.setNmsSoft(soft);
						ruleSoft.setNmsAssetType(type);
						ruleSoft.setDType(rule.getDType());
						ruleSoft.setRName(rule.getRName());
						ruleSoft.setRContent(rule.getRContent());
						ruleSoft.setRUnit(rule.getRUnit());
						ruleSoft.setRSeq(rule.getRSeq());
						ruleSoft.setREnable(rule.getREnable());
						ruleSoft.setRValue1(rule.getRValue1().intValue());
						ruleSoft.setRValue2(rule.getRValue2().intValue());
						ruleSoft.setRValue3(rule.getRValue3().intValue());
						session.save(ruleSoft);
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
