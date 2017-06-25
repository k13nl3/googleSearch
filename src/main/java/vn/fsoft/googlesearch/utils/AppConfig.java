package vn.fsoft.googlesearch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import vn.fsoft.googlesearch.service.AppException;

public class AppConfig {
	final static Logger logger = Logger.getLogger(AppConfig.class);
	// Constants
	public static final String OUTPUT_EXTENSION = ".xlsx";
	public static final String OUTPUT_FILE_NAME = "Search_Result_";
	public static final int DELAY_TIME_MIN = 3;
	public static final int DELAY_TIME_MAX = 30;
	public static final int MAX_RESULT_MIN = 1;
	public static final int MAX_RESULT_MAX = 10;

	// Resources path
	public static String KEYWORD_FILE_PATH = "resources/keyword.txt";
	public static String SITE_FILE_PATH = "resources/site.txt";
	public static String TEMPLATE_PATH = "resources/template.xlsx";
	public static String CONFIG_PATH = "conf/app.conf";

	// Proxy properties
	public static final String PROXY_HOST_PROP = "proxy.host";
	public static final String PROXY_PORT_PROP = "proxy.port";
	public static final String PROXY_USER_PROP = "proxy.user";
	public static final String PROXY_PASS_PROP = "proxy.pass";
	public static final String USE_PROXY_PROP = "proxy.active";
	public static final String REQUIRE_AUTHEN_PROP = "proxy.authen";

	// App properties
	public static final String DELAY_TIME_PROP = "delay.time";
	public static final String MAX_RESULT_PROP = "max.result";
	public static final String USER_AGENT_PROP = "user.agent";
	public static final String SEARCH_URL_PROP = "search.url";

	public static Properties config = new Properties();

	public static void refreshConfig() {
		InputStream input = null;
		try {
			input = new FileInputStream(CONFIG_PATH);
			// Load properties file
			config.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void setProxyHost(String value) {
		config.setProperty(PROXY_HOST_PROP, value);
	}

	public static void setProxyPort(int value) {
		config.setProperty(PROXY_PORT_PROP, String.valueOf(value));
	}

	public static void setProxyUser(String value) {
		config.setProperty(PROXY_USER_PROP, value);
	}

	public static void setProxyPass(String value) {
		config.setProperty(PROXY_PASS_PROP, value);
	}
	
	public static String getProxyHost() {
		return config.getProperty(PROXY_HOST_PROP);
	}
	
	public static int getProxyPort() {
		return Integer.valueOf(config.getProperty(PROXY_PORT_PROP));
	}
	
	public static String getProxyUser() {
		return config.getProperty(PROXY_USER_PROP);
	}
	
	public static String getProxyPass() {
		return config.getProperty(PROXY_PASS_PROP);
	}

	public static void setDelayTime(int value) {
		int time = Integer.valueOf(value);
		if (time < DELAY_TIME_MIN || time > DELAY_TIME_MAX) {
			throw new AppException("Port must be in (3, 30) seconds");
		}
		config.setProperty(DELAY_TIME_PROP, String.valueOf(value));
	}

	public static void setMaxResult(int value) {
		if (value < MAX_RESULT_MIN || value > MAX_RESULT_MAX) {
			throw new AppException("Port must be in (3, 30) seconds");
		}
		config.setProperty(MAX_RESULT_PROP, String.valueOf(value));
	}

	public static void setSearchUrl(String value) {
		config.setProperty(SEARCH_URL_PROP, value);
	}

	public static void setUserAgent(String value) {
		config.setProperty(USER_AGENT_PROP, value);
	}

	public static void saveConfig() {
		try {
			File f = new File(CONFIG_PATH);
			OutputStream out = new FileOutputStream(f);
			config.store(out, null);
			out.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static void setProxyActive(boolean value) {
		config.setProperty(USE_PROXY_PROP, value ? "true" : "false");
	}
	
	public static void setProxyAuthen(boolean value) {
		config.setProperty(REQUIRE_AUTHEN_PROP, value ? "true" : "false");
	}
}
