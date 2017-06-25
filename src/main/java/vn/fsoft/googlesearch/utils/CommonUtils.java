package vn.fsoft.googlesearch.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class CommonUtils {
	final static Logger logger = Logger.getLogger(CommonUtils.class);

	public static void saveTextFile(String content, String filePath) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			out.write(content);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error when save file " + filePath, e);
		}
	}

	public static String readTextFile(String filePath) {
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error when reading file " + filePath, e);
		}
		return content;
	}

	public static String generateFilename() {
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		return df.format(new Date());
	}

	public static List<String> parseTextareaToList(String content) {
		String[] arrLine = content.split("\\r?\\n");
		return new ArrayList<String>(Arrays.asList(arrLine));
	}

	public static boolean containsIgnoreCase(String src, String what) {
		final int length = what.length();
		if (length == 0)
			return true; // Empty string is contained

		final char firstLo = Character.toLowerCase(what.charAt(0));
		final char firstUp = Character.toUpperCase(what.charAt(0));

		for (int i = src.length() - length; i >= 0; i--) {
			// Quick check before calling the more expensive regionMatches()
			// method:
			final char ch = src.charAt(i);
			if (ch != firstLo && ch != firstUp)
				continue;

			if (src.regionMatches(true, i, what, 0, length))
				return true;
		}

		return false;
	}
}
