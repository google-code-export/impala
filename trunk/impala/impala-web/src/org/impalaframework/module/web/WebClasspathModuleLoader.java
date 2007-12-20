package org.impalaframework.module.web;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class WebClasspathModuleLoader extends WebRootModuleLoader implements ModuleLoader {

	public WebClasspathModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebClasspathModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(moduleDefinition.getContextLocations(), classLoader);
	}

}
