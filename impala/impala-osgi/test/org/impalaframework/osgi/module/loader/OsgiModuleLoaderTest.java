package org.impalaframework.osgi.module.loader;

import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.service.ServiceRegistry;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

public class OsgiModuleLoaderTest extends TestCase {

	private OsgiModuleLoader moduleLoader;
	private BundleContext bundleContext;
	private ClassLoaderFactory classLoaderFactory;
	private ModuleLocationResolver moduleLocationResolver;
	private ServiceRegistry serviceRegistry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleLoader = new OsgiModuleLoader();
		bundleContext = createMock(BundleContext.class);
		classLoaderFactory = createMock(ClassLoaderFactory.class);
		moduleLocationResolver = createMock(ModuleLocationResolver.class);
		serviceRegistry = createMock(ServiceRegistry.class);
		
		
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
	private void replayMocks() {
		replay(serviceRegistry);
		replay(classLoaderFactory);
		replay(moduleLocationResolver);
		replay(bundleContext);
	}

	private void verifyMocks() {
		verify(serviceRegistry);
		verify(classLoaderFactory);
		verify(moduleLocationResolver);
		verify(bundleContext);
	}

}
