package vn.fsoft.laruetools.service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.fsoft.laruetools.dto.SearchResultDto;
import vn.fsoft.laruetools.utils.AppConfig;

public class SearchService {
	public final String GOOGLE_SEARCH_URL = "https://www.google.com.vn/search";
	public final int SEARCH_MAX_NUMBER = 3;

	public final int BEGIN_DATA_ROW = 1;
	public int RESULT_TEXT_COL = getColIndex("A");
	public int RESULT_LINK_COL = getColIndex("B");
	public int RESULT_FOUND_COL = getColIndex("C");

	public void search(List<String> listKeyword, List<String> listSite, String filePath, JProgressBar progressBar,
			JLabel lblProgress) {
		progressBar.setMaximum(listKeyword.size() * listSite.size());
		List<SearchResultDto> listResult = searchGoogle(listKeyword, listSite, lblProgress);
		writeToExcel(listResult, filePath);
		lblProgress.setText("Search successfully!!!");
		// Open folder for view file
		try {
			Desktop.getDesktop().open(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeToExcel(List<SearchResultDto> listResult, String filePath) {
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
				// Insert Worklog ID
				Cell cell = row.getCell(RESULT_TEXT_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_TEXT_COL);
				}
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(dto.getText());

				// Insert Workdate
				cell = row.getCell(RESULT_LINK_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_LINK_COL);
				}
				cell.setCellType(CellType.STRING);
				XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
				link.setAddress(dto.getLink());
				cell.setHyperlink(link);
				cell.setCellValue(dto.getLink());
				cell.setCellStyle(hyperlinkStyle);

				// Insert Description
				cell = row.getCell(RESULT_FOUND_COL);
				if (cell == null) {
					cell = row.createCell(RESULT_FOUND_COL);
				}
				cell.setCellType(CellType.STRING);
				cell.setCellValue(dto.isFound() ? "O" : "X");
				// Next row
				rowIndex++;
			}
			sheet.setAutoFilter(new CellRangeAddress(0, rowIndex - 1, 0, RESULT_FOUND_COL));
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

	private List<SearchResultDto> searchGoogle(List<String> listKeyword, List<String> listSite, JLabel lblProgress) {
		// Taking search term input from console
		List<SearchResultDto> listResult = new ArrayList<SearchResultDto>();
		for (String site : listSite) {
			lblProgress.setText("Searching in site " + site + "...");
			for (String keyword : listKeyword) {
				try {
					String searchURL = GOOGLE_SEARCH_URL + "?q=" + URLEncoder.encode(keyword + " site:" + site, "UTF-8")
							+ "&num=" + SEARCH_MAX_NUMBER;
					// without proper User-Agent, we will get 403 error
					Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
					Elements listElement = doc.select("h3.r > a");
					if (listElement.size() == 0) {
						SearchResultDto result = new SearchResultDto(null, null, true);
						listResult.add(result);
					} else {
						for (Element element : listElement) {
							String linkHref = element.attr("href");
							linkHref = linkHref.replace("/url?q=", "");
							linkHref = linkHref.substring(0, linkHref.indexOf('&'));

							String linkText = element.text();
							SearchResultDto result = new SearchResultDto(linkText, linkHref, true);
							listResult.add(result);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return listResult;
	}

	private static int getColIndex(String columnName) {
		int number = 0;
		for (int i = 0; i < columnName.length(); i++) {
			number = number * 26 + (columnName.charAt(i) - ('A' - 1)) - 1;
		}
		return number;
	}
}
