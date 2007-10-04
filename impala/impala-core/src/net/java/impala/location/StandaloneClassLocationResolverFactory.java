package net.java.impala.location;

import java.util.Properties;

import net.java.impala.util.PropertyUtils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class StandaloneClassLocationResolverFactory implements ClassLocationResolverFactory {

	static String EXECUTION_PROPERTIES_FILE_PATH = "impala.execution.file.path";

	static String EXECUTION_PROPERTIES_FILE_NAME = "impala.execution.file.name";

	public ClassLocationResolver getClassLocationResolver() {

		// check for system property for property file as an absolute location
		String filePath = System.getProperty(EXECUTION_PROPERTIES_FILE_PATH);

		if (filePath != null) {
			FileSystemResource r = new FileSystemResource(filePath);
			if (!r.exists()) {
				throw new IllegalStateException("System property '" + EXECUTION_PROPERTIES_FILE_PATH
						+ "' points to location which does not exist: " + filePath);
			}

			return load(r);
		}
		else {

			String fileName = System.getProperty(EXECUTION_PROPERTIES_FILE_NAME);

			if (fileName != null) {
				ClassPathResource r = new ClassPathResource(fileName);
				if (!r.exists()) {
					throw new IllegalStateException("System property '" + EXECUTION_PROPERTIES_FILE_NAME
							+ "' points to classpath location which could not be found: " + fileName);
				}
				return load(r);
			}
		}
		
		ClassPathResource defaultResource = new ClassPathResource("execution-locations.properties");
		if (defaultResource.exists()) {
			return load(defaultResource);
		}
		
		PropertyClassLocationResolver resolver = new PropertyClassLocationResolver();
		return resolver;
	}

	private ClassLocationResolver load(Resource r) {
		Properties props = PropertyUtils.loadProperties(r);
		return new PropertyClassLocationResolver(props);
	}
}
