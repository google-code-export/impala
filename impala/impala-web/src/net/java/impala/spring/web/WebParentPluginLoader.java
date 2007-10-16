package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import net.java.impala.location.ClassLocationResolver;
import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.PluginLoader;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.util.ResourceUtils;

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
