package org.impalaframework.module.operation;

import junit.framework.TestCase;

import org.impalaframework.module.modification.StrictModuleModificationCalculator;
import org.impalaframework.module.operation.RemovePluginOperation;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;
import org.impalaframework.module.spec.SimpleModuleDefinition;
import org.impalaframework.module.transition.DefaultPluginStateManager;

public class RemovePluginOperationTest extends TestCase {

	public final void testRemovePluginWithInvalidParent() {
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition("c.xml");
		ModuleDefinition moduleDefinition = new SimpleModuleDefinition("p");
		rootModuleDefinition.add(moduleDefinition);
		moduleDefinition.setParent(null);
		
		TestPluginStateManager pluginStateManager = new TestPluginStateManager();
		pluginStateManager.setParentSpec(rootModuleDefinition);
		
		try {
			RemovePluginOperation.removePlugin(pluginStateManager, new StrictModuleModificationCalculator(), "p");
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin to remove does not have a parent plugin. This is unexpected state and may indicate a bug", e.getMessage());
		}
	}

}

class TestPluginStateManager extends DefaultPluginStateManager {

	@Override
	protected void setParentSpec(RootModuleDefinition rootModuleDefinition) {
		super.setParentSpec(rootModuleDefinition);
	}
	
}
