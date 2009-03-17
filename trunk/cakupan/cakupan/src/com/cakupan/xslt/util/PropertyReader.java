package com.cakupan.xslt.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read properties from a propertyfile
 * @author Patrick Oosterveld
 *
 */
public class PropertyReader {

	/** properties */
	private static Properties properties = load();

	private static final String CVRG_PROPS = "cakupan.properties";


	
	/**
	 * Load xsltcoverage properties
	 * 
	 * @return
	 */
	public static Properties load() {
		// Read properties file.
		Properties props = new Properties();
		try {
			InputStream is = PropertyReader.class.getResourceAsStream(CVRG_PROPS);
			if (null != is) {
				props.load(is);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not load propertie file!", e);
		}
		return props;
	}
	
	public static String getProperty(String key){
		if (null == properties){
			return null;
		}
		return properties.getProperty(key);
	}

}
