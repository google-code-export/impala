package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.loader.PluginLoader;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class WebParentPluginLoader extends WebPluginLoader implements PluginLoader {

	public WebParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}
	
	public WebParentPluginLoader(ClassLocationResolver classLocationResolver, ServletContext servletContext) {
		super(classLocationResolver, servletContext);
	}

	public Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(pluginSpec.getContextLocations(), classLoader);
	}

}
