package org.impalaframework.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class SystemPropertiesReader {

	public static final String SYSPROP_RESOURCE_NAME = "sysprop-resource";

	private ClassLoader classLoader;

	public SystemPropertiesReader(ClassLoader classLoader) {
		super();
		if (classLoader == null) {
			throw new IllegalArgumentException("Class loader must be specified");
		}
		this.classLoader = classLoader;
	}

	public void readSystemProperties() {

		String resourceName = System.getProperty("sysprop-resource");
		if (resourceName != null) {
			InputStream resourceAsStream = null;

			try {
				resourceAsStream = classLoader.getResourceAsStream(resourceName);

				if (resourceAsStream != null) {
					Properties props = new Properties();
					try {
						props.load(resourceAsStream);
					}
					catch (IOException e) {
						System.err.println("Unable to load properties from resource " + resourceAsStream);
						return;
					}

					Set<Object> keySet = props.keySet();
					for (Object keyObject : keySet) {
						String key = keyObject.toString();
						String value = props.getProperty(key);

						// allow explicitly set properties to override
						if (System.getProperty(key) == null) {
							System.setProperty(key, value);
						}
					}
				}
			}
			finally {
				if (resourceAsStream != null) {
					try {
						resourceAsStream.close();
					}
					catch (IOException e) {
					}
				}
			}

		}
	}

}
