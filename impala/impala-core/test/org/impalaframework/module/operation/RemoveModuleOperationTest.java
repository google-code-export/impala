package org.impalaframework.module.operation;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.module.operation.RemoveModuleOperation;

public class RemoveModuleOperationTest extends TestCase {

	public final void testRemovePluginWithInvalidParent() {
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition("c.xml");
		ModuleDefinition moduleDefinition = new SimpleModuleDefinition("p");
		rootModuleDefinition.add(moduleDefinition);
		moduleDefinition.setParentDefinition(null);
		
		TestPluginStateManager pluginStateManager = new TestPluginStateManager();
		pluginStateManager.setParentSpec(rootModuleDefinition);
		
		try {
			RemoveModuleOperation.removePlugin(pluginStateManager, new StrictModificationExtractor(), "p");
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin to remove does not have a parent plugin. This is unexpected state and may indicate a bug", e.getMessage());
		}
	}

}

class TestPluginStateManager extends DefaultModuleStateHolder {

	@Override
	protected void setParentSpec(RootModuleDefinition rootModuleDefinition) {
		super.setParentSpec(rootModuleDefinition);
	}
	
}
