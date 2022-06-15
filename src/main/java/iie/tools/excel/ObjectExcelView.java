package iie.tools.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ObjectExcelView extends AbstractExcelView {
	private String excelName="";

	public ObjectExcelView(String excelName) {
		this.excelName = excelName;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> map,
                                      HSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
		Date date = new Date();
		//表头占用行数
		int headRowNum = 2;
		String filename = excelName + DateUtil.date2Str(date, "yyyyMMddHHmmss");
		HSSFSheet sheet;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
		
		HSSFWorkbook book = (HSSFWorkbook) workbook;
		sheet = book.createSheet(excelName==null?"sheet1":excelName);
		
		List<String> titles = (List<String>) map.get("titles");
		int len = titles.size();
		//标题样式
		HSSFCellStyle headerStyle = book.createCellStyle(); 
//		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		//标题字体
		HSSFFont headerFont = book.createFont();			
//		headerFont.setBold(true);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)15);
		headerStyle.setFont(headerFont);
		short height=25*20;

		// 合并单元格并设置标题内容
		CellRangeAddress cra0 = new CellRangeAddress(0, 1, 0, titles.size() - 1);
		sheet.addMergedRegion(cra0);
		Row rowHead = sheet.createRow(0);
		rowHead.setHeight((short) 800);
		rowHead.setRowStyle(headerStyle);
		Cell cell1 = rowHead.createCell(0);
		cell1.setCellStyle(headerStyle);
		cell1.setCellValue(excelName + "\r\n" + "时间:" + DateUtil.date2Str(new Date()));

		HSSFRow row = sheet.createRow(headRowNum++);
		for(int i=0; i<len; i++){ 
			//设置标题
			String title = titles.get(i);
			row.setRowStyle(headerStyle);
			row.createCell(i).setCellValue(title);  
		}
		sheet.getRow(0).setHeight(height);
		HSSFCellStyle contentStyle = book.createCellStyle(); //内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		List<Map<String,String>> varList = (List<Map<String,String>>) map.get("varList");
		int varCount = varList.size();
		for(int i=0; i<varCount; i++){
			Map<String,String> vpd = varList.get(i);
			HSSFRow rows = sheet.createRow(i+headRowNum);
			for(int j=0;j<len;j++){
				String varstr = vpd.get("var"+(j+1)) != null ? vpd.get("var"+(j+1)) : "";
				rows.setRowStyle(contentStyle);
				rows.createCell(j).setCellValue(varstr);
			}
		}
	}
}
