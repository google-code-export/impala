package org.impalaframework.module.web;

import javax.servlet.ServletContext;

import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class WebParentPluginLoader extends WebRootPluginLoader implements ModuleLoader {

	public WebParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}
	
	public WebParentPluginLoader(ClassLocationResolver classLocationResolver, ServletContext servletContext) {
		super(classLocationResolver, servletContext);
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(moduleDefinition.getContextLocations(), classLoader);
	}

}
