package net.java.impala.location;

import java.io.File;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class PropertyClassLocationResolver implements ClassLocationResolver {

	protected static final String FOLDER_SEPARATOR = "/";

	protected static final String PLUGIN_CLASS_DIR_PROPERTY = "impala.plugin.class.dir";

	protected static final String PLUGIN_SPRING_DIR_PROPERTY = "impala.plugin.spring.dir";

	protected static final String PARENT_CLASS_DIR = "impala.parent.class.dir";

	protected static final String PARENT_TEST_DIR = "impala.parent.test.dir";

	private static final Log log = LogFactory.getLog(PropertyClassLocationResolver.class);

	private Properties properties;

	public PropertyClassLocationResolver() {
		super();
		this.properties = new Properties();
		init();
	}

	public PropertyClassLocationResolver(Properties properties) {
		super();
		Assert.notNull(properties);
		this.properties = (Properties) properties.clone();
		init();
	}

	public File[] getParentClassLocations(String parentName) {
		String suffix = StringUtils.cleanPath(getProperty(PARENT_CLASS_DIR));
		String path = getPath(getRootDirectoryPath(), parentName);
		path = getPath(path, suffix);
		return new File[] { new File(path) };
	}
	
	public File[] getTestClassLocations(String parentName) {
		String suffix = StringUtils.cleanPath(getProperty(PARENT_TEST_DIR));
		String path = getPath(getRootDirectoryPath(), parentName);
		path = getPath(path, suffix);
		return new File[] { new File(path) };
	}

	public File[] getApplicationPluginClassLocations(String plugin) {
		String classDir = getProperty(PLUGIN_CLASS_DIR_PROPERTY);

		String path = getPath(getRootDirectoryPath(), plugin);
		path = getPath(path, classDir);
		return new File[] { new File(path) };
	}

	public File getApplicationPluginSpringLocation(String plugin) {
		String classDir = getProperty(PLUGIN_SPRING_DIR_PROPERTY);

		String path = getPath(getRootDirectoryPath(), plugin);
		path = getPath(path, classDir);
		return new File(path, plugin + "-context.xml");
	}

	private void init() {

		// the plugin directory which is expected to contain classes
		mergeProperty(PLUGIN_CLASS_DIR_PROPERTY, "bin", null);

		// the plugin directory which is expected to plugin Spring context files
		mergeProperty(PLUGIN_SPRING_DIR_PROPERTY, "spring", null);

		// the parent directory in which classes are expected to be found
		mergeProperty(PARENT_CLASS_DIR, "bin", null);

		// the parent directory in which tests are expected to be found
		mergeProperty(PARENT_TEST_DIR, "bin", null);
	}

	protected String getPath(String root, String suffix) {
		if (!suffix.startsWith(FOLDER_SEPARATOR)) {
			suffix = FOLDER_SEPARATOR + suffix;
		}
		String path = root + suffix;
		return path;
	}

	void mergeProperty(String propertyName, String defaultValue, String extraToSupplied) {
		String systemProperty = System.getProperty(propertyName);
		String value = null;

		if (systemProperty != null) {

			if (log.isInfoEnabled()) {
				log.info("Resolved location property '" + propertyName + "' from system property: " + systemProperty);
			}
			value = systemProperty;
		}
		else {
			String suppliedValue = this.properties.getProperty(propertyName);
			if (suppliedValue != null) {
				if (log.isInfoEnabled())
					log.info("Resolved location property '" + propertyName + "' from supplied properties: "
							+ suppliedValue);
				value = suppliedValue;
			}
			else {

				if (log.isInfoEnabled())
					log.info("Unable to resolve location '" + propertyName
							+ "' from system property or supplied properties. Using default value: " + defaultValue);
				value = defaultValue;
			}
		}
		if (value != null) {
			if (extraToSupplied != null) {
				if (!value.endsWith(extraToSupplied)) {
					value += extraToSupplied;
				}
			}

			this.properties.put(propertyName, value);
		}
	}

	protected File getRootDirectory() {
		String workspace = properties.getProperty("workspace.root");
		if (workspace != null) {
			File candidate = new File(workspace);

			if (!candidate.exists()) {
				throw new IllegalStateException("'workspace.root' (" + workspace + ") does not exist");
			}
			if (!candidate.isDirectory()) {
				throw new IllegalStateException("'workspace.root' (" + workspace + ") is not a directory");
			}
			return candidate;
		}
		return new File("../");
	}

	protected String getRootDirectoryPath() {
		File rootDirectory = getRootDirectory();
		String absolutePath = rootDirectory.getAbsolutePath();
		String cleanPath = StringUtils.cleanPath(absolutePath);
		return cleanPath;
	}

	protected String getProperty(String key) {
		return properties.getProperty(key);
	}

}
