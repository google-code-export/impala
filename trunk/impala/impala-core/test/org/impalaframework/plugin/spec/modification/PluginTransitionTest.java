package org.impalaframework.plugin.spec.modification;

import junit.framework.TestCase;

public class PluginTransitionTest extends TestCase {

	public final void testPluginModificationOperation() {
		PluginTransition op = new PluginTransition(PluginState.LOADED, PluginState.UNLOADED);
		assertEquals(PluginState.LOADED, op.getBeforeState());
		assertEquals(PluginState.UNLOADED, op.getAfterState());
	}

	
}
