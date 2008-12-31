package org.impalaframework.web.config;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.PropertiesLoader;
import org.impalaframework.util.PropertyUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.bootstrap.ServletContextLocationsRetriever;
import org.impalaframework.web.module.WebModuleUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public class ServletContextPropertiesLoader implements PropertiesLoader {

	private static final Log logger = LogFactory.getLog(ServletContextLocationsRetriever.class);
	
	private final ServletContext servletContext;
	private String defaultBootstrapResource;
	
	public ServletContextPropertiesLoader(
			ServletContext servletContext,
			String defaultBootstrapResource) {
		super();
		Assert.notNull(servletContext, "servletContext cannot be null");
		Assert.notNull(defaultBootstrapResource, "defaultBootstrapResource cannot be null");
		
		this.servletContext = servletContext;
		this.defaultBootstrapResource = defaultBootstrapResource;
	}

	public Properties loadProperties() {
		return getProperties();
	}
	
	protected Properties getProperties() {
		
		String bootstrapLocationsResource = WebModuleUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);

		ResourceLoader resourceLoader = getResourceLoader();
		Resource bootStrapResource = null;
		
		if (bootstrapLocationsResource == null) {
			bootStrapResource = resourceLoader.getResource(defaultBootstrapResource);
		}
		else {
			// figure out which resource loader to use
			bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);
		}
		Properties properties = null;
		if (bootStrapResource == null || !bootStrapResource.exists()) {
			logger.info("Unable to load locations resource from " + bootstrapLocationsResource + ".");
			properties = new Properties();
		} else { 
			properties = PropertyUtils.loadProperties(bootStrapResource);
		}
		
		return properties;
	}
	
	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

	public void setDefaultBootstrapResource(String defaultResource) {
		Assert.notNull(defaultResource);
		this.defaultBootstrapResource = defaultResource;
	}
}
