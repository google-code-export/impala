package org.impalaframework.web.module;

import java.io.IOException;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.resolver.SimpleModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ServletModuleLoaderTest extends TestCase {

	public final void testGetSpringConfigResources() throws IOException {
		ServletModuleLoader moduleLoader = new ServletModuleLoader(new SimpleModuleLocationResolver());
		Resource[] springConfigResources = moduleLoader.getSpringConfigResources(new SimpleModuleDefinition(null, "plugin1", new String[]{"parentTestContext.xml"}), ClassUtils.getDefaultClassLoader());
	
		assertEquals(1, springConfigResources.length);
		Resource resource = springConfigResources[0];
		assertTrue(resource instanceof ClassPathResource);
		assertEquals("class path resource [parentTestContext.xml]", resource.getDescription());
	}

}