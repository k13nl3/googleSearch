package vn.fsoft.googlesearch.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import vn.fsoft.googlesearch.dto.SearchResultDto;

public class ExcelUtils {
	final static Logger logger = Logger.getLogger(ExcelUtils.class);
	private static final int BEGIN_DATA_ROW = 1;
	private static final int RESULT_KEYWORD_COL = getColIndex("A");
	private static final int RESULT_TEXT_COL = getColIndex("B");
	private static final int RESULT_LINK_COL = getColIndex("C");
	private static final int RESULT_FOUND_CACHE_COL = getColIndex("D");
	private static final int RESULT_FOUND_PAGE_COL = getColIndex("E");
	private static final String FOUND_STRING = "O";

	private static int getColIndex(String columnName) {
		int number = 0;
		for (int i = 0; i < columnName.length(); i++) {
			number = number * 26 + (columnName.charAt(i) - ('A' - 1)) - 1;
		}
		return number;
	}

	public static void writeToExcel(List<SearchResultDto> listResult, List<SearchResultDto> listPreviousSearch,
			String filePath) {
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(AppConfig.TEMPLATE_PATH));
			CreationHelper createHelper = wb.getCreationHelper();
			XSSFCellStyle hyperlinkStyle = wb.createCellStyle();
			XSSFFont hlink_font = wb.createFont();
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hyperlinkStyle.setFont(hlink_font);
			Sheet sheet = wb.getSheetAt(0);
			int rowIndex = BEGIN_DATA_ROW;
			for (SearchResultDto dto : listResult) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				// Insert Keyword
				Cell cell = row.getCell(RESULT_KEYWORD_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_KEYWORD_COL);
				}
				cell.setCellType(CellType.STRING);
				cell.setCellValue(dto.getKeyword());
				
				// Insert Text
				cell = row.getCell(RESULT_TEXT_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_TEXT_COL);
				}
				cell.setCellType(CellType.STRING);
				cell.setCellValue(dto.getText());

				// Insert Link
				cell = row.getCell(RESULT_LINK_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_LINK_COL);
				}
				cell.setCellType(CellType.STRING);
				if (dto.getLink() != null) {
					XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
					link.setAddress(dto.getLink());
					cell.setHyperlink(link);
					cell.setCellValue(dto.getLink());
					cell.setCellStyle(hyperlinkStyle);
				}

				// Insert Found in cache
				cell = row.getCell(RESULT_FOUND_CACHE_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_FOUND_CACHE_COL);
				}
				cell.setCellType(CellType.STRING);
				cell.setCellValue(dto.isFoundInCache() ? "O" : "X");
				
				// Insert Found in page
				cell = row.getCell(RESULT_FOUND_PAGE_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_FOUND_PAGE_COL);
				}
				cell.setCellType(CellType.STRING);
				cell.setCellValue(dto.isFoundInPage() ? "O" : "X");
				// Next row
				rowIndex++;
			}
			sheet.setAutoFilter(new CellRangeAddress(0, rowIndex - 1, 0, RESULT_FOUND_PAGE_COL));
			FileOutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<SearchResultDto> readFromExcel(String previousPath) {
		// TODO Auto-generated method stub
		List<SearchResultDto> listResult = new ArrayList<SearchResultDto>();
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(previousPath));
			Sheet sheet = wb.getSheetAt(0);
			Row row;
			Cell cell;
			int numRow = sheet.getPhysicalNumberOfRows();
			for (int i = BEGIN_DATA_ROW; i < numRow; i++) {
				SearchResultDto resultDto = new SearchResultDto();
				row = sheet.getRow(i);
				if (row != null) {
					cell = row.getCell(RESULT_KEYWORD_COL);
					if (cell != null) {
						resultDto.setKeyword(cell.getStringCellValue());
					}
					cell = row.getCell(RESULT_TEXT_COL);
					if (cell != null) {
						resultDto.setText(cell.getStringCellValue());
					}
					cell = row.getCell(RESULT_LINK_COL);
					if (cell != null) {
						resultDto.setKeyword(cell.getStringCellValue());
					}
					cell = row.getCell(RESULT_FOUND_CACHE_COL);
					if (cell != null) {
						if (FOUND_STRING.equals(cell.getStringCellValue())) {
							resultDto.setFoundInCache(true);
						}
					}
					cell = row.getCell(RESULT_FOUND_PAGE_COL);
					if (cell != null) {
						if (FOUND_STRING.equals(cell.getStringCellValue())) {
							resultDto.setFoundInPage(true);
						}
					}
				}
				listResult.add(resultDto);
			}
			wb.close();
		} catch (IOException e) {
			logger.error(e);
		}
		return listResult;
	}
}
