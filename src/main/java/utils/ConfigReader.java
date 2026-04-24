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
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to load config.properties");
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
	}


