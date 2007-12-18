package org.impalaframework.module.web;

import java.io.IOException;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.web.ServletPluginLoader;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ServletPluginLoaderTest extends TestCase {

	public final void testGetSpringConfigResources() throws IOException {
		ServletPluginLoader pluginLoader = new ServletPluginLoader(new PropertyClassLocationResolver());
		Resource[] springConfigResources = pluginLoader.getSpringConfigResources(new SimpleModuleDefinition("myplugin"), ClassUtils.getDefaultClassLoader());
	
		assertEquals(1, springConfigResources.length);
		Resource resource = springConfigResources[0];
		assertTrue(resource.getURL().toString().endsWith("myplugin/spring/myplugin-context.xml"));
	}

}