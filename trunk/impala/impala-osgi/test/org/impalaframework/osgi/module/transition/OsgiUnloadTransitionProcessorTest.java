package org.impalaframework.osgi.module.transition;

import static org.easymock.classextension.EasyMock.createMock;
import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class OsgiUnloadTransitionProcessorTest extends TestCase {

	private BundleContext bundleContext;
	private OsgiUnloadTransitionProcessor processor;
	private Bundle bundle;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bundleContext = createMock(BundleContext.class);
		bundle = createMock(Bundle.class);
		processor = new TestUnloadProcessor(bundle);
		processor.setBundleContext(bundleContext);
	}
	
	public void testFindAndUnloadBundle() {
		processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
	}

}

class TestUnloadProcessor extends OsgiUnloadTransitionProcessor {

	private Bundle bundle;

	public TestUnloadProcessor(Bundle bundle) {
		super();
		this.bundle = bundle;
	}

	@Override
	Bundle findBundle(ModuleDefinition moduleDefinition) {
		return bundle;
	}
	
}
