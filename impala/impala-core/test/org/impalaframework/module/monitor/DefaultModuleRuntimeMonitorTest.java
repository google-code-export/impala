package org.impalaframework.module.monitor;

import static org.easymock.EasyMock.*;
import org.impalaframework.module.ModuleDefinition;

import junit.framework.TestCase;

public class DefaultModuleRuntimeMonitorTest extends TestCase {

	private ModuleDefinition definition;
	private DefaultModuleRuntimeMonitor monitor;

	@Override
	protected void setUp() throws Exception {
		monitor = new DefaultModuleRuntimeMonitor();
		definition = createMock(ModuleDefinition.class);
	}
	
	public void testModuleIsReloadableDefault() {
		assertTrue(monitor.moduleIsReloadable(definition));
	}
	
	public void testModuleIsReloadableEnforcedTrue() {
		expect(definition.isReloadable()).andReturn(true);
		
		replay(definition);

		monitor.setEnforceReloadability(true);
		assertTrue(monitor.moduleIsReloadable(definition));
		
		verify(definition);
	}
	
	public void testModuleIsReloadableEnforcedFalse() {
		expect(definition.isReloadable()).andReturn(false);
		
		replay(definition);

		monitor.setEnforceReloadability(true);
		assertFalse(monitor.moduleIsReloadable(definition));
		
		verify(definition);
	}

}
