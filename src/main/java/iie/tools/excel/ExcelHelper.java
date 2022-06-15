package iie.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ExcelHelper {
	// 2标题+1表头
	private int headRowNum = 1;
		
	private String docName;

	private List<String> titles;

	@SuppressWarnings("rawtypes")
	private List<Map> varList;

	private String timeStr;

	private HSSFWorkbook wb;

	/* 设置高亮间隔 */
	private int interval = 2;

	/** 间隔行样式 */
	private HSSFCellStyle styleCol;
	/** 普通行样式 */
	private HSSFCellStyle styleNormal;

	@SuppressWarnings("rawtypes")
	public ExcelHelper(String docName, List<String> titles, List<Map> varList) {
		this.docName = docName;
		this.titles = titles;
		this.varList = varList;
		timeStr = DateUtil.date2Str(new Date(), "yyyyMMddHHmmss");
	}

	@SuppressWarnings("unchecked")
	public ExcelHelper buildExcelDocument() {
		// 创建文件对象
		wb = new HSSFWorkbook();
		
		// 创建表对象
		Sheet sheet = wb.createSheet(docName);
		
		setSheetStyle(wb, sheet);
		
		// 内容样式
		HSSFCellStyle contentStyle = wb.createCellStyle(); 
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 写入数据
		int varCount = varList.size();
		int len = titles.size();
		for (int i = 0; i < varCount; i++) {
			Map<String, String> vpd = varList.get(i);
			Row rows = sheet.createRow(i + headRowNum);
			for (int j = 0; j < len; j++) {
				String varstr = vpd.get("var" + (j + 1)) != null ? vpd
						.get("var" + (j + 1)) : "";
				Cell cell = rows.createCell(j);
				cell.setCellValue(varstr);
				if ((i % interval) == 0) {
					cell.setCellStyle(styleCol);
				} else {
					cell.setCellStyle(styleNormal);
				}
			}
		}
		return this;
	}

	/**
	 * 输出excel
	 */
	public void exportExcel() {

	}

	/**
	 * 输出压缩文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean exportZip(HttpServletRequest request, HttpServletResponse response) {
		File xlsFile = new File("");
		File zipFile = new File("");
		ZipFile zip = null;
		String path = request.getSession().getServletContext()
				.getRealPath("temp");
		String strZip = path + File.separator + System.currentTimeMillis()
				+ "test.zip";
		try {
			xlsFile = new File(path, docName + timeStr + ".xls");
			
			// 写入导出内容到文件
			OutputStream out = new FileOutputStream(xlsFile);
			wb.write(out);
			
			// 强制清空缓存
			out.flush();
			
			// 关闭
			out.close();
			
			// 生成的临时压缩文件
			zip = new ZipFile(strZip);
			zip.setFileNameCharset("GBK");
			ZipParameters parameters = new ZipParameters();
			
			// 压缩方式
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			
			// 压缩级别
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			
			// 加密设置
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			parameters.setPassword(String.valueOf(request.getSession()
					.getAttribute("exportPwd")));
			// 要打包的文件夹
			zip.addFile(xlsFile, parameters);
			zipFile = new File(strZip);
		} catch (ZipException | IOException e) {
			e.printStackTrace();
		}
		response.reset();
		response.setContentType("application/x-zip-compressed;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		try {
			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ PingYinUtil.getFirstSpell(docName)
					+ timeStr + ".zip");
			OutputStream out = response.getOutputStream();
			// 输入流
			InputStream inStream = new FileInputStream(zipFile);
			IOUtils.copy(inStream, out);
			IOUtils.closeQuietly(inStream);
			IOUtils.closeQuietly(out);
			if (xlsFile.exists()) {
				xlsFile.delete();
			}
			if (zipFile.exists()) {
				FileUtils.forceDelete(zipFile);
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

	private void setSheetStyle(HSSFWorkbook wb, Sheet sheet) {
		// 设置默认列宽
		sheet.setDefaultColumnWidth((short) 25);
		
		// 创建标题格式对象并设置字体、自动换行和对齐方式
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setWrapText(true);
		org.apache.poi.hssf.usermodel.HSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		// 合并单元格并设置标题内容
		/*
		 * CellRangeAddress cra0 = new CellRangeAddress(0, headRowNum - 2, 0,
		 * titles.size() - 1); sheet.addMergedRegion(cra0); Row row =
		 * sheet.createRow(0); row.setHeight((short) 800); Cell cell1 =
		 * row.createCell(0); cell1.setCellValue(docName + "\r\n" + "时间:" + timeStr);
		 * cell1.setCellStyle(headerStyle);
		 */

		// 设置表头格式
		org.apache.poi.hssf.usermodel.HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		HSSFCellStyle style = wb.createCellStyle();
		org.apache.poi.hssf.usermodel.HSSFFont titleFont = wb.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setColor(HSSFColor.WHITE.index);//HSSFColor.VIOLET.index //字体颜色
		style.setFont(titleFont);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		// 定义间隔行样式
		styleCol = wb.createCellStyle();
		styleCol.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleCol.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleCol.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleCol.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleCol.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleCol.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleCol.setWrapText(true);
		
		// 定义普通行样式
		styleNormal = wb.createCellStyle();
		styleNormal.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		styleNormal.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleNormal.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleNormal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleNormal.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleNormal.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleNormal.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleNormal.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleNormal.setWrapText(true);
		
		// 设置表头内容
		Row row1 = sheet.createRow(headRowNum - 1);
		Cell cell = null;
		for (int i = 0; i < titles.size(); i++) {
			cell = row1.createCell(i);
			cell.setCellValue(titles.get(i));
			cell.setCellStyle(style);
		}
	}
}
