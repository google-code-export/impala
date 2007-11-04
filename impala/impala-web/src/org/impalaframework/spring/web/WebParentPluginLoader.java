package org.impalaframework.spring.web;

import javax.servlet.ServletContext;


import org.impalaframework.plugin.plugin.ApplicationContextSet;
import org.impalaframework.plugin.plugin.PluginLoader;
import org.impalaframework.plugin.plugin.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class WebParentPluginLoader extends WebPluginLoader implements PluginLoader {

	public WebParentPluginLoader(ClassLocationResolver classLocationResolver, ServletContext servletContext) {
		super(classLocationResolver, servletContext);
	}

	public Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec,
			ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(pluginSpec.getContextLocations(), classLoader);
	}

}
