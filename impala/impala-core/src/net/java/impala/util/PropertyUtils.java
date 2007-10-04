package net.java.impala.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.java.impala.exception.ExecutionException;

import org.springframework.core.io.Resource;

public class PropertyUtils {

	public static Properties loadProperties(Resource resource) {
		Properties props = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = resource.getInputStream();
			props.load(inputStream);
		}
		catch (IOException e) {
			throw new ExecutionException("Unable to load properties file " + resource.getDescription());
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) {
				}
			}
		}
		return props;
	}

}
