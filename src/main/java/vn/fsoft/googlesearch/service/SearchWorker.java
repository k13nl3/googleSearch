package vn.fsoft.googlesearch.service;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.fsoft.googlesearch.dto.SearchResultDto;
import vn.fsoft.googlesearch.dto.SearchWorkerDto;
import vn.fsoft.googlesearch.utils.AppConfig;
import vn.fsoft.googlesearch.utils.CommonUtils;
import vn.fsoft.googlesearch.utils.ExcelUtils;

public class SearchWorker extends SwingWorker<Boolean, SearchWorkerDto> {
	final static Logger logger = Logger.getLogger(SearchWorker.class);
	private SearchWorkerDto workerDto = new SearchWorkerDto();
	private List<String> listKeyword;
	private List<String> listSite;
	private String outputPath;
	private String previousPath;
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private JLabel progressPercent;
	private JFrame mainFrame;

	public void setListKeyword(List<String> listKeyword) {
		this.listKeyword = listKeyword;
	}

	public void setListSite(List<String> listSite) {
		this.listSite = listSite;
	}

	public void setOutputPath(String filePath) {
		this.outputPath = filePath;
	}

	public void setPreviousPath(String previousPath) {
		this.previousPath = previousPath;
	}

	public SearchWorker(JFrame mainFrame, JProgressBar progressBar, JLabel progressLabel, JLabel progressPercent) {
		super();
		this.progressBar = progressBar;
		this.progressLabel = progressLabel;
		this.progressPercent = progressPercent;
		this.mainFrame = mainFrame;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		// TODO Auto-generated method stub
		progressBar.setMaximum(listKeyword.size() * listSite.size());
		progressPercent.setVisible(true);
		progressBar.setVisible(true);
		List<SearchResultDto> listResult = new ArrayList<SearchResultDto>();
		int progress = 0;
		boolean firstRequest = true;
		Document doc;
		Proxy proxy = null;
		if ("true".equals(AppConfig.config.getProperty(AppConfig.USE_PROXY_PROP))) {
			// Using proxy
			proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(AppConfig.getProxyHost(), AppConfig.getProxyPort()));
			if ("true".equals(AppConfig.config.getProperty(AppConfig.REQUIRE_AUTHEN_PROP))) {
				// Requires Authenticated
				Authenticator.setDefault(new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(AppConfig.getProxyUser(),
								AppConfig.getProxyPass().toCharArray());
					}
				});
			}
		}
		for (String keyword : listKeyword) {
			updateProgress(-1, "Searching keyword " + keyword + "...");
			for (String site : listSite) {
				try {
					if (firstRequest) {
						firstRequest = false;
					} else {
						// Delay for prevent Google Captcha
						TimeUnit.SECONDS.sleep(Long.valueOf(AppConfig.config.getProperty(AppConfig.DELAY_TIME_PROP)));
					}
					String searchURL = AppConfig.config.getProperty(AppConfig.SEARCH_URL_PROP) + "?q="
							+ URLEncoder.encode("\"" + keyword + "\"" + " site:" + site, "UTF-8") + "&num="
							+ AppConfig.config.getProperty(AppConfig.MAX_RESULT_PROP);
					doc = Jsoup.connect(searchURL).userAgent(AppConfig.config.getProperty(AppConfig.USER_AGENT_PROP))
							.proxy(proxy).get();

					Elements listElement = doc.select("h3.r > a");
					if (listElement.size() == 0) {
						SearchResultDto result = new SearchResultDto(keyword, null, null, false, false);
						listResult.add(result);
					} else {
						for (Element element : listElement) {
							String linkHref = element.attr("href");
							linkHref = linkHref.replace("/url?q=", "");
							if (linkHref.indexOf('&') > 0) {
								linkHref = linkHref.substring(0, linkHref.indexOf('&'));
							}
							String linkText = element.text();
							// Search keyword in found link
							boolean foundInPage = false;
							try {
								doc = Jsoup.connect(linkHref)
										.userAgent(AppConfig.config.getProperty(AppConfig.USER_AGENT_PROP)).proxy(proxy)
										.get();
								if (CommonUtils.containsIgnoreCase(doc.html(), keyword)) {
									foundInPage = true;
								}
							} catch (HttpStatusException e) {
								if (e.getStatusCode() != 404) {
									throw e;
								}
							}
							SearchResultDto result = new SearchResultDto(keyword, linkText, linkHref, true,
									foundInPage);
							listResult.add(result);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					updateProgress(++progress, null);
				}
			}
		}
		updateProgress(-1, "Writing to Excel...");
		List<SearchResultDto> listPreviousSearch = null;

		try {
			if (previousPath != null) {
				listPreviousSearch = ExcelUtils.readFromExcel(previousPath);
			}
			ExcelUtils.writeToExcel(listResult, listPreviousSearch, outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateProgress(0, "Search successfully!!!");
		return true;
	}

	private void updateProgress(int progressNumber, String progressText) {
		if (progressText != null) {
			workerDto.setProgressText(progressText);
		}
		if (progressNumber >= 0) {
			workerDto.setProgressNumber(progressNumber);
		}
		publish(workerDto);
	}

	@Override
	protected void process(List<SearchWorkerDto> chunks) {
		// TODO Auto-generated method stub
		SearchWorkerDto dto = chunks.get(chunks.size() - 1);
		progressBar.setValue(dto.getProgressNumber());
		progressBar.repaint();
		progressBar.update(progressBar.getGraphics());
		progressPercent.setText((dto.getProgressNumber() * 100 / progressBar.getMaximum()) + "%");
		progressLabel.setText(dto.getProgressText());
	}

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		progressPercent.setVisible(false);
		try {
			boolean isOK = get();
			if (isOK) {
				Desktop.getDesktop().open(new File(outputPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			String msg = String.format("Unexpected problem: %s", e.getCause().toString());
			JOptionPane.showMessageDialog(mainFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
