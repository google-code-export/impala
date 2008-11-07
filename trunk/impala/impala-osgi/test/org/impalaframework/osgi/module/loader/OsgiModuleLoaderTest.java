package org.impalaframework.osgi.module.loader;

import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.service.ServiceRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.io.OsgiBundleResource;

import junit.framework.TestCase;

public class OsgiModuleLoaderTest extends TestCase {

	private OsgiModuleLoader moduleLoader;
	private BundleContext bundleContext;
	private ClassLoaderFactory classLoaderFactory;
	private ModuleLocationResolver moduleLocationResolver;
	private ServiceRegistry serviceRegistry;
	private Bundle bundle;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bundleContext = createMock(BundleContext.class);
		classLoaderFactory = createMock(ClassLoaderFactory.class);
		moduleLocationResolver = createMock(ModuleLocationResolver.class);
		serviceRegistry = createMock(ServiceRegistry.class);
		bundle = createMock(Bundle.class);

		initLoader(bundle);
	}

	private void initLoader(Bundle bundle) {
		moduleLoader = new TestModuleLoader(bundle);
		moduleLoader.setClassLoaderFactory(classLoaderFactory);
		moduleLoader.setModuleLocationResolver(moduleLocationResolver);
		moduleLoader.setBundleContext(bundleContext);
		moduleLoader.setServiceRegistry(serviceRegistry);
	}
	
	public void testGetClassLocations() throws Exception {
		final FileSystemResource resource1 = new FileSystemResource("resource1");
		final FileSystemResource resource2 = new FileSystemResource("resource2");
		
		final List<Resource> asList = Arrays.asList(new Resource[] {resource1, resource2});
		expect(moduleLocationResolver.getApplicationModuleClassLocations("mymodule")).andReturn(asList);
		
		replayMocks();
		
		final Resource[] classLocations = moduleLoader.getClassLocations(new SimpleModuleDefinition("mymodule"));
		assertSame(resource1, classLocations[0]);
		assertSame(resource2, classLocations[1]);
		assertEquals(2, classLocations.length);
		
		verifyMocks();
	}
	
	public void testSpringConfigResources() throws Exception {
		replayMocks();
		
		final Resource[] springConfigResources = moduleLoader.getSpringConfigResources(new SimpleModuleDefinition("mymodule"), null);
		assertEquals(1, springConfigResources.length);
		assertEquals("mymodule-context.xml", springConfigResources[0].getFilename());
		assertTrue(springConfigResources[0] instanceof OsgiBundleResource);
		
		verifyMocks();
	}
	
	public void testSpringConfigResourceNullBundle() throws Exception {
		initLoader(null);
		
		replayMocks();
		
		try {
			moduleLoader.getSpringConfigResources(new SimpleModuleDefinition("mymodule"), null);
			fail();
		} catch (InvalidStateException e) {
			assertEquals("Unable to find bundle with name corresponding with module 'name=mymodule, contextLocations=[mymodule-context.xml], type=APPLICATION'. Check to see whether this module installed properly.", e.getMessage());
		}
		
		verifyMocks();
	}
	
	private void replayMocks() {
		replay(serviceRegistry);
		replay(classLoaderFactory);
		replay(moduleLocationResolver);
		replay(bundleContext);
		replay(bundle);
	}

	private void verifyMocks() {
		verify(serviceRegistry);
		verify(classLoaderFactory);
		verify(moduleLocationResolver);
		verify(bundleContext);
		verify(bundle);
	}

}

class TestModuleLoader extends OsgiModuleLoader {

	private Bundle bundle;

	public TestModuleLoader(Bundle bundle) {
		super();
		this.bundle = bundle;
	}

	@Override
	Bundle findBundle(ModuleDefinition moduleDefinition) {
		return bundle;
	}
	
}

