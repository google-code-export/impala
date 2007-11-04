package org.impalaframework.spring.web;

import javax.servlet.ServletContext;


import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.spring.plugin.ApplicationContextSet;
import org.impalaframework.spring.plugin.PluginLoader;
import org.impalaframework.spring.plugin.PluginSpec;
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
