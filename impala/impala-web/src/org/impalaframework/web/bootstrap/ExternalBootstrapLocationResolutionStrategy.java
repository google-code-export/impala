package org.impalaframework.web.bootstrap;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.impalaframework.util.PropertyUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.loader.BaseImpalaContextLoader;
import org.impalaframework.web.module.WebModuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

public class ExternalBootstrapLocationResolutionStrategy extends DefaultBootstrapLocationResolutionStrategy {

	final Logger logger = LoggerFactory.getLogger(BaseImpalaContextLoader.class);

	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		String bootstrapLocationsResource = WebModuleUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);

		if (bootstrapLocationsResource == null) {
			// then look for init parameter which contains these
			return super.getBootstrapContextLocations(servletContext);
		}
		else {
			// figure out which resource loader to use
			ResourceLoader resourceLoader = getResourceLoader();
			Resource bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);

			if (bootStrapResource == null || !bootStrapResource.exists()) {
				logger.info("Unable to load locations resource from {}. Delegating to superclass",
						bootstrapLocationsResource);
				return super.getBootstrapContextLocations(servletContext);
			}
			Properties loadProperties = PropertyUtils.loadProperties(bootStrapResource);
			String property = loadProperties.getProperty(WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM);

			if (property == null) {
				throw new IllegalStateException("Bootstrap location resource '" + bootStrapResource
						+ "' does not contain property '" + WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM + "'");
			}

			return StringUtils.tokenizeToStringArray(property, " ,");
		}
	}

	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

}
