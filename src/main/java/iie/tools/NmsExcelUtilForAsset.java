package iie.tools;

import iie.pojo.NmsAssetType;
import iie.pojo.NmsDepartment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class NmsExcelUtilForAsset {

	public boolean exportExcel() {

		// 创建文件对象
		HSSFWorkbook wb = new HSSFWorkbook();

		// 创建表对象并设置列宽
		Sheet sheet = wb.createSheet(nameOfSheet);
		sheet.setDefaultColumnWidth((short) 15);

		// 暂不实现列宽设置
		sheet.setColumnWidth(0, 20 * 256);
		sheet.setColumnWidth(2, 25 * 256);
		sheet.setColumnWidth(8, 30 * 256);
		// sheet.setColumnWidth(4, 20 * 256);

		// 创建标题格式对象并设置字体、自动换行和对齐方式
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setWrapText(true);
		org.apache.poi.hssf.usermodel.HSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 14);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);
		// 合并单元格并设置标题内容
		CellRangeAddress cra0 = new CellRangeAddress(0, 1, 0,
				namesOfField.size() - 1);
		sheet.addMergedRegion(cra0);
		Row row = sheet.createRow(0);
		row.setHeight((short) 800);
		Cell cell1 = row.createCell(0);
		Date now = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String hehe = sdf1.format(now);
		cell1.setCellValue(nameOfTitle + "\r\n" + "时间:" + hehe);
		cell1.setCellStyle(headerStyle);

		// 设置表头格式
		HSSFCellStyle datastyle = wb.createCellStyle();
		org.apache.poi.hssf.usermodel.HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		datastyle.setWrapText(true);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle cellstyles = wb.createCellStyle();
		org.apache.poi.hssf.usermodel.HSSFFont titleFont = wb.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		datastyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		datastyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellstyles.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		cellstyles.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellstyles.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellstyles.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Row row1 = sheet.createRow(2);

		// 设置表头内容
		Cell cell = null;
		for (int i = 0; i < namesOfHeader.size(); i++) {
			cell = row1.createCell(i);
			cell.setCellValue(namesOfHeader.get(i));
			cell.setCellStyle(style);
		}

		// 填充数据
		Integer n = 0;
		Integer y = 1;
		if (list != null && list.size() > 0) {
			for (int i = 0, imax = list.size(); i < imax; i++) {
				n++;
				y++;
				row1 = sheet.createRow(i + 3);
				row1.setHeight((short) 300);
				row1.createCell(0).setCellValue(n);
				// NmsAlarm alarm = (NmsAlarm) list.get(i);

				for (int j = 0; j < namesOfField.size(); j++) {
					Field field;
					try {
						field = clazz.getDeclaredField(namesOfField.get(j));
						field.setAccessible(true);
					//	String k = field.getName();
						String v = "";
						if (field.get(list.get(i)) == null
								|| "".equals(field.get(list.get(i)))) {
							v = "-";
						} else if (namesOfField.get(j).equals("nmsAssetType")) {
							NmsAssetType type = (NmsAssetType) (field.get(list
									.get(i)));
							v = type.getChType() + "/" + type.getChSubtype();
						} else if (namesOfField.get(j).equals("nmsDepartment")) {
							NmsDepartment dept = (NmsDepartment) (field
									.get(list.get(i)));
							v = dept.getDName();

						} else if (namesOfField.get(j).equals("collMode")) {
							if ((Integer) field.get(list.get(i)) == 0) {
								v = "专用代理";
							} else if ((Integer) field.get(list.get(i)) == 1) {
								v = "ICMP协议";
							} else if ((Integer) field.get(list.get(i)) == 2) {
								v = "SNMP协议";
							} else if ((Integer) field.get(list.get(i)) == 3) {
								v = "JDBC协议";
							} else if ((Integer) field.get(list.get(i)) == 4) {
								v = "JMX协议";
							} else {
								v = "--";
							}
						} else if (namesOfField.get(j).equals("colled")) {
							if ((Short) field.get(list.get(i)) == 0) {
								v = "已监控";
							} else {
								v = "未监控";
							}
						} else if (namesOfField.get(j).equals("itime")) {
							String timeStr = String.valueOf(field.get(list
									.get(i)));
							v = timeStr.replace(".0", "");
						} else if (field.get(list.get(i)) instanceof String) {
							v = (String) field.get(list.get(i));
						} else {
							v = field.get(list.get(i)).toString();
						}
						// System.out.println(k + ": " + v);
						if (v == null || "".equals(v)) {
							row1.createCell(j).setCellValue("无");
						} else {
							row1.createCell(j).setCellValue(v);
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
						return false;
					} catch (SecurityException e) {
						e.printStackTrace();
						return false;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return false;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return false;
					}
				}

				// 给查询出来的单元格添加样式
				for (Iterator<Cell> cit = row1.cellIterator(); cit.hasNext();) {
					HSSFCell celln = (HSSFCell) cit.next();
					celln.setCellStyle(datastyle);
					if (y % 3 == 0) {
						celln.setCellStyle(cellstyles);
					}
				}
			}
		}

		File f = new File("");
		File f1 = new File("");
		String strZip = "";
		try {
			String path = request.getSession().getServletContext()
					.getRealPath("temp");
			// 使用File找到一个文件
			SimpleDateFormat newsdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = newsdf.format(new Date());
			f = new File(path, nameOfFile + date + ".xls");
			// 写入导出内容到文件
			OutputStream out = new FileOutputStream(f);
			wb.write(out);

			// 强制清空缓存
			out.flush();
			// 关闭
			out.close();
			System.out.println(f.getAbsolutePath());
			// 生成的压缩文件
			strZip = path + File.separator + "test.zip";
			ZipFile zipFile = new ZipFile(path + File.separator + "test.zip");
			ZipParameters parameters = new ZipParameters();
			// 压缩方式
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			// 压缩级别
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword(String.valueOf(request.getSession().getAttribute("exportPwd"))); 
			// 要打包的文件夹
			zipFile.addFile(f, parameters);
			f1 = new File(strZip);
		} catch (ZipException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.reset();
		response.setContentType("application/x-zip-compressed;charset=UTF-8");
		try {
			SimpleDateFormat newsdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = newsdf.format(new Date());
			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ new String((nameOfFile + date + ".zip").getBytes("GBK"),
							"ISO8859_1") + "\"");
			OutputStream out = response.getOutputStream();

			// 输入流
			InputStream inStream = new FileInputStream(strZip);
			InputStream in = new BufferedInputStream(inStream);
			byte[] bs = new byte[in.available()];
			in.read(bs);
			in.close();
			
			out.write(bs);

			out.flush();
			out.close();
			
			if(f.exists()){   
                f.delete();   
            }  
			if(f1.exists()){   
                f1.delete();   
            } 
			
			return true;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
			return false;
		}
	}

	private List<?> list;
	private Class<?> clazz;
	private String nameOfFile, nameOfSheet, nameOfTitle;
	private List<String> namesOfHeader, namesOfField;
	private HttpServletResponse response;
	private HttpServletRequest request;

	public NmsExcelUtilForAsset() {
		super();
	}

	public NmsExcelUtilForAsset(List<?> list, Class<?> clazz,
			String nameOfFile, String nameOfSheet, String nameOfTitle,
			List<String> namesOfHeader, List<String> namesOfField,
			HttpServletResponse response, HttpServletRequest request) {
		super();
		this.clazz = clazz;
		this.response = response;
		this.request = request;
		if (list == null) {
			list = new ArrayList<>();
		} else {
			this.list = list;
		}
		if (nameOfFile == null) {
			nameOfFile = "";
		} else {
			this.nameOfFile = nameOfFile;
		}
		if (nameOfSheet == null) {
			nameOfSheet = "";
		} else {
			this.nameOfSheet = nameOfSheet;
		}
		if (nameOfTitle == null) {
			nameOfTitle = "";
		} else {
			this.nameOfTitle = nameOfTitle;
		}
		if (namesOfField == null) {
			namesOfField = new ArrayList<String>();
		} else {
			this.namesOfField = namesOfField;
		}
		if (namesOfHeader == null) {
			namesOfHeader = new ArrayList<String>();
		} else {
			this.namesOfHeader = namesOfHeader;
		}
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getNameOfFile() {
		return nameOfFile;
	}

	public void setNameOfFile(String nameOfFile) {
		this.nameOfFile = nameOfFile;
	}

	public String getNameOfSheet() {
		return nameOfSheet;
	}

	public void setNameOfSheet(String nameOfSheet) {
		this.nameOfSheet = nameOfSheet;
	}

	public String getNameOfTitle() {
		return nameOfTitle;
	}

	public void setNameOfTitle(String nameOfTitle) {
		this.nameOfTitle = nameOfTitle;
	}

	public List<String> getNamesOfHeader() {
		return namesOfHeader;
	}

	public void setNamesOfHeader(List<String> namesOfHeader) {
		this.namesOfHeader = namesOfHeader;
	}

	public List<String> getNamesOfField() {
		return namesOfField;
	}

	public void setNamesOfField(List<String> namesOfField) {
		this.namesOfField = namesOfField;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
