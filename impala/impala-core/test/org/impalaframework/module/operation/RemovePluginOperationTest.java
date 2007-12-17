package org.impalaframework.module.operation;

import junit.framework.TestCase;

import org.impalaframework.module.modification.StrictPluginModificationCalculator;
import org.impalaframework.module.operation.RemovePluginOperation;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.SimpleParentSpec;
import org.impalaframework.module.spec.SimplePluginSpec;
import org.impalaframework.module.transition.DefaultPluginStateManager;

public class RemovePluginOperationTest extends TestCase {

	public final void testRemovePluginWithInvalidParent() {
		ParentSpec parentSpec = new SimpleParentSpec("c.xml");
		PluginSpec pluginSpec = new SimplePluginSpec("p");
		parentSpec.add(pluginSpec);
		pluginSpec.setParent(null);
		
		TestPluginStateManager pluginStateManager = new TestPluginStateManager();
		pluginStateManager.setParentSpec(parentSpec);
		
		try {
			RemovePluginOperation.removePlugin(pluginStateManager, new StrictPluginModificationCalculator(), "p");
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin to remove does not have a parent plugin. This is unexpected state and may indicate a bug", e.getMessage());
		}
	}

}

class TestPluginStateManager extends DefaultPluginStateManager {

	@Override
	protected void setParentSpec(ParentSpec parentSpec) {
		super.setParentSpec(parentSpec);
	}
	
}
