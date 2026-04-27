package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

	private static Properties prop;

	public static void loadConfig() {
		try {
			FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
			prop = new Properties();
			prop.load(fis);
			fis.close();
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config.properties", e);
		}
	}

	public static String getProperty(String key) {
		if (prop == null) {
			loadConfig();
		}
		return prop.getProperty(key);
	}

	public static String getBaseUrl() {
		return getProperty("base_url");
	}

	public static String getApiKey() {
		return getProperty("api_key");
	}

	public static String getExcelPath() {
		return getProperty("excel_path");
	}

	public static String getUserId() {
		return getProperty("user_id");
	}

	public static String getProjectId() {
		return getProperty("project_id");
	}

	public static String get(String key) {
		return getProperty(key);
	}
}