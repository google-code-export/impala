package org.impalaframework.web.module;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

public class WebRootModuleLoaderTest extends TestCase {

	private ServletContext servletContext;
	private WebRootModuleLoader loader;

	public void setUp() {
		servletContext = createMock(ServletContext.class);
		StandaloneModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		loader = new WebRootModuleLoader(resolver, servletContext);
	}
	
	public final void testNewApplicationContext() {
		final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		final GenericApplicationContext parent = new GenericApplicationContext();
		final GenericWebApplicationContext applicationContext = loader.newApplicationContext(parent, new SimpleRootModuleDefinition(new String[]{"loc"}), classLoader);

		assertNotNull(applicationContext);
		assertNotNull(applicationContext.getBeanFactory());
		assertSame(classLoader, applicationContext.getClassLoader());
		assertSame(servletContext, applicationContext.getServletContext());
	}
	
	public final void testGetClassLocations() {
		final String[] locations = new String[] {"context1", "context2"};
		WebRootModuleDefinition definition = new WebRootModuleDefinition(new SimpleRootModuleDefinition(new String[]{"loc"}), "impala-web", locations);
		final Resource[] classLocations = loader.getClassLocations(definition);
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}
	
	public void testGetSpringLocations() {
		final String[] locations = new String[] {"context1", "context2"};
		WebRootModuleDefinition definition = new WebRootModuleDefinition(new SimpleRootModuleDefinition(new String[]{"loc"}), "name", locations);
		loader.setServletContext(servletContext);
		
		expect(servletContext.getRealPath("/context1")).andReturn("../impala-web/resources/loader/context1.xml").times(2);
		expect(servletContext.getRealPath("/context2")).andReturn("../impala-web/resources/loader/context2.xml").times(2);
		
		replay(servletContext);
		
		final Resource[] resources = loader.getSpringConfigResources(definition, ClassUtils.getDefaultClassLoader());
		assertEquals(2, resources.length);
		for (int i = 0; i < resources.length; i++) {
			assertTrue(resources[i] instanceof ServletContextResource);
			assertEquals(locations[i], resources[i].getFilename());
		}

		verify(servletContext);
	}

}
