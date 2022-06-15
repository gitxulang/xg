package iie.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class NmsExcelUtilForAssetsTemplate {

	public boolean exportExcel() {

		// 创建文件对象
		HSSFWorkbook wb = new HSSFWorkbook();

		// 创建表对象并设置列宽
		Sheet sheet = wb.createSheet(nameOfSheet);
		sheet.setDefaultColumnWidth((short) 15);

		// 暂不实现列宽设置
		 sheet.setColumnWidth(0, 20 * 256);
		 sheet.setColumnWidth(3, 30 * 256);
		 sheet.setColumnWidth(10, 20 * 256);
		 sheet.setColumnWidth(11, 20 * 256);
		// sheet.setColumnWidth(4, 20 * 256);


		Row row0 = sheet.createRow(0);

		// 设置表头内容
		Cell cell = null;
		for (int i = 0; i < namesOfHeader.size(); i++) {
			cell = row0.createCell(i);
			cell.setCellValue(namesOfHeader.get(i));
		}
		
		Row row1 = sheet.createRow(1);

		// 设置示例内容
		for (int i = 0; i < namesOfHeader.size(); i++) {
			cell = row1.createCell(i);
			switch (i) {
			case 0:
				cell.setCellValue("10.10.41.222");
				break;
			case 1:
				cell.setCellValue("数据库实验服务器");
				break;
			case 2:
				cell.setCellValue("DD-00001");
				break;
			case 3:
				cell.setCellValue("服务器/中科方德专用服务器");
				break;
			case 4:
				cell.setCellValue("206实验室");
				break;
			case 5:
				cell.setCellValue("DELL服务器");
				break;
			case 6:
				cell.setCellValue("李宁");
				break;
			case 7:
				cell.setCellValue("网络中心");
				break;
			case 8:
				cell.setCellValue("启用");
				break;
			case 9:
				cell.setCellValue("SNMP");
				break;
			case 10:
				cell.setCellValue("iie02");
				break;
			case 11:
				cell.setCellValue("iie03");
				break;
			case 12:
				cell.setCellValue("ii304");
				break;
			case 13:
				cell.setCellValue("2019-10-10");
				break;

			default:
				break;
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

	public NmsExcelUtilForAssetsTemplate() {
		super();
	}

	public NmsExcelUtilForAssetsTemplate(List<?> list, Class<?> clazz,
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
