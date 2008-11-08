package org.impalaframework.osgi.module.transition;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

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
		processor = new TestModuleLoader(applicationContextLoader, bundle);
		processor.setBundleContext(bundleContext);
		processor.setModuleLoaderRegistry(moduleLoaderRegistry);
	}
	
	public void testFindAndStartBundle() {
		replayMocks();
		
		verifyMocks();
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
class TestModuleLoader extends OsgiLoadTransitionProcessor {

	private Bundle bundle;

	public TestModuleLoader(ApplicationContextLoader loader, Bundle bundle) {
		super(loader);
		this.bundle = bundle;
	}

	@Override
	Bundle findBundle(ModuleDefinition moduleDefinition) {
		return bundle;
	}
	
}
