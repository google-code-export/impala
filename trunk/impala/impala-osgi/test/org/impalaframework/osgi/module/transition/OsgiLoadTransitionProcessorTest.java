package org.impalaframework.osgi.module.transition;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.core.io.Resource;

public class OsgiLoadTransitionProcessorTest extends TestCase {
	
	private OsgiLoadTransitionProcessor processor;
	private BundleContext bundleContext;
	private ModuleLoaderRegistry moduleLoaderRegistry;
	private ModuleLoader moduleLoader;
	private ApplicationContextLoader applicationContextLoader;
	private Bundle bundle;


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		bundle = createMock(Bundle.class);
		applicationContextLoader = createMock(ApplicationContextLoader.class);
		bundleContext = createMock(BundleContext.class);
		moduleLoaderRegistry = createMock(ModuleLoaderRegistry.class);
		moduleLoader = createMock(ModuleLoader.class);
		
		initProcessor(bundle);
	}

	private void initProcessor(Bundle bundle) {
		processor = new TestProcessor(applicationContextLoader, bundle);
		processor.setBundleContext(bundleContext);
		processor.setModuleLoaderRegistry(moduleLoaderRegistry);
	}
	
	public void testFindAndStartActiveBundle() {
		expect(bundle.getState()).andReturn(Bundle.ACTIVE);
		
		replayMocks();
		
		processor.findAndStartBundle(new SimpleModuleDefinition("myModule"));
		
		verifyMocks();
	}
	
	public void testFindAndStartResolvedBundle() throws BundleException {
		expect(bundle.getState()).andReturn(Bundle.RESOLVED);
		expectBundleStart();
		
		replayMocks();
		
		processor.findAndStartBundle(new SimpleModuleDefinition("myModule"));
		
		verifyMocks();
	}
	
	public void testNoBundleNullClassLocations() throws BundleException {
		final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
		final Resource[] classLocations = null;
		
		expect(moduleLoaderRegistry.getModuleLoader("APPLICATION")).andReturn(moduleLoader);
		expect(moduleLoader.getClassLocations(moduleDefinition)).andReturn(classLocations);
		
		initProcessor(null);
		
		replayMocks();
		
		try {
			processor.findAndStartBundle(moduleDefinition);
			fail();
		} catch (InvalidStateException e) {
			assertTrue(e.getMessage().contains("returned null bundle class locations. Cannot install bundle for module 'myModule'"));
		}
		
		verifyMocks();
	}
	
	public void testNoBundleEmptyClassLocations() throws BundleException {
		final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
		final Resource[] classLocations = new Resource[0];
		
		expect(moduleLoaderRegistry.getModuleLoader("APPLICATION")).andReturn(moduleLoader);
		expect(moduleLoader.getClassLocations(moduleDefinition)).andReturn(classLocations);
		
		initProcessor(null);
		
		replayMocks();
		
		try {
			processor.findAndStartBundle(moduleDefinition);
			fail();
		} catch (InvalidStateException e) {
			assertTrue(e.getMessage().contains("returned empty bundle class locations. Cannot install bundle for module 'myModule'"));
		}
		
		verifyMocks();
	}
	
	public void testNoBundle() throws BundleException, IOException {
		final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
		final Resource resource = createMock(Resource.class);
		final Resource[] classLocations = new Resource[] { resource };
		final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
		
		expect(moduleLoaderRegistry.getModuleLoader("APPLICATION")).andReturn(moduleLoader);
		expect(moduleLoader.getClassLocations(moduleDefinition)).andReturn(classLocations);
		expect(resource.getInputStream()).andReturn(stream);
		expect(bundleContext.installBundle("myModule", stream)).andReturn(bundle);
		
		//this wouldn't happen, but just to simplify the test
		expect(bundle.getState()).andReturn(Bundle.ACTIVE);
		
		initProcessor(null);
		
		replay(resource);
		replayMocks();
		
		processor.findAndStartBundle(moduleDefinition);
		
		verify(resource);
		verifyMocks();
	}
	
	public void testFindAndStartResolvedBundleThrowsException() throws BundleException {
		expect(bundle.getState()).andReturn(Bundle.RESOLVED);
		expectBundleStart();
		expectLastCall().andThrow(new BundleException("it broke"));
		expect(bundle.getSymbolicName()).andReturn("symbolicname");
		
		replayMocks();
		
		try {
			processor.findAndStartBundle(new SimpleModuleDefinition("myModule"));
			fail();
		} catch (ExecutionException e) {
			assertEquals("Unable to start bundle 'symbolicname': it broke", e.getMessage());
		}
		
		verifyMocks();
	}

	@SuppressWarnings("unchecked")
	private void expectBundleStart() throws BundleException {
		expect(bundle.getHeaders()).andReturn(new Hashtable());
		bundle.start();
	}
	
	private void replayMocks() {
		replay(bundleContext);
		replay(moduleLoaderRegistry);
		replay(moduleLoader);
		replay(applicationContextLoader);
		replay(bundle);
	}
	
	private void verifyMocks() {
		verify(bundleContext);
		verify(moduleLoaderRegistry);
		verify(moduleLoader);
		verify(applicationContextLoader);
		verify(bundle);
	}

}

class TestProcessor extends OsgiLoadTransitionProcessor {

	private Bundle bundle;

	public TestProcessor(ApplicationContextLoader loader, Bundle bundle) {
		super(loader);
		this.bundle = bundle;
	}

	@Override
	Bundle findBundle(ModuleDefinition moduleDefinition) {
		return bundle;
	}
	
}
