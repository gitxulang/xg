package iie.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

@SuppressWarnings("deprecation")
public class NmsExcelUtilForAlarmStatics {

	public boolean exportExcel() {

		// 创建文件对象
		HSSFWorkbook wb = new HSSFWorkbook();

		// 创建表对象并设置列宽
		Sheet sheet = wb.createSheet(nameOfSheet);
		sheet.setDefaultColumnWidth((short) 15);

		// 暂不实现列宽设置
		sheet.setColumnWidth(1, 25 * 256);
		sheet.setColumnWidth(2, 25 * 256);
		sheet.setColumnWidth(3, 25 * 256);
		// sheet.setColumnWidth(2, 20 * 256);
		// sheet.setColumnWidth(3, 100 * 256);
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
				namesOfField.size());
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
		cell = row1.createCell(0);
		cell.setCellValue("编号");
		cell.setCellStyle(style);
		for (int i = 0; i < namesOfHeader.size(); i++) {
			cell = row1.createCell(i + 1);
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
						} else {
							switch (j) {
							case 0:
								v = (String) field.get(list.get(i));
								break;
							case 1:
								v = (String) field.get(list.get(i));
								break;
							case 2:
								v = (String) field.get(list.get(i));
								break;
							case 3:
								v = field.get(list.get(i)).toString();
								break;
							case 4:
								v = field.get(list.get(i)).toString();
								break;
							case 5:
								v = field.get(list.get(i)).toString();
								break;
					//		case 6:
					//			v = field.get(list.get(i)).toString();
					//			break;
							case 6:
								v = field.get(list.get(i)).toString();
								break;
							default:
								v = "-";
								break;
							}
						}

						row1.createCell(j + 1).setCellValue(v);
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

		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			SimpleDateFormat newsdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = newsdf.format(new Date());
			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ new String((nameOfFile + date + ".xls").getBytes("GBK"),
							"ISO8859_1") + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
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

	public NmsExcelUtilForAlarmStatics() {
		super();
	}

	public NmsExcelUtilForAlarmStatics(List<?> list, Class<?> clazz,
			String nameOfFile, String nameOfSheet, String nameOfTitle,
			List<String> namesOfHeader, List<String> namesOfField,
			HttpServletResponse response) {
		super();
		this.clazz = clazz;
		this.response = response;
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
}
