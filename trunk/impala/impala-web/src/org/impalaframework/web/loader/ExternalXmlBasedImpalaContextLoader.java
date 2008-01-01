package org.impalaframework.web.loader;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.bootstrap.ExternalBootstrapLocationResolutionStrategy;
import org.impalaframework.web.module.WebModuleUtils;
import org.impalaframework.web.module.WebXmlRootDefinitionBuilder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ExternalXmlBasedImpalaContextLoader extends BaseImpalaContextLoader {

	@Override
	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		return new ExternalBootstrapLocationResolutionStrategy().getBootstrapContextLocations(servletContext);
	}	
	
	@Override
	public ModuleDefinitionSource getModuleDefinitionSource(ServletContext servletContext) {
		
		String locationsResourceName = WebModuleUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM);

		if (locationsResourceName == null) {
			throw new IllegalStateException(
					"Unable to resolve locations resource name parameter '"
							+ WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM
							+ "' from either a system property or a 'context-param' entry in the web application's WEB-INF/web.xml");
		}

		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(locationsResourceName);

		if (!resource.exists()) {
			throw new IllegalStateException("Plugin spec XML resource '" + resource.getDescription()
					+ "' does not exist");
		}

		WebXmlRootDefinitionBuilder moduleDefinitionSource = new WebXmlRootDefinitionBuilder();
		moduleDefinitionSource.setResource(resource);
		return moduleDefinitionSource;
	}

	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

}
