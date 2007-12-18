package org.impalaframework.module.web;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class WebParentPluginLoader extends WebRootPluginLoader implements ModuleLoader {

	public WebParentPluginLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebParentPluginLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(moduleDefinition.getContextLocations(), classLoader);
	}

}
