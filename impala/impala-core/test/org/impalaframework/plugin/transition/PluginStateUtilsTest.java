package org.impalaframework.plugin.transition;

import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.plugin.transition.DefaultPluginStateManager;
import org.impalaframework.plugin.transition.PluginStateUtils;

import junit.framework.TestCase;

public class PluginStateUtilsTest extends TestCase {

	public final void testRemovePluginWithInvalidParent() {
		ParentSpec parentSpec = new SimpleParentSpec("c.xml");
		PluginSpec pluginSpec = new SimplePluginSpec("p");
		parentSpec.add(pluginSpec);
		pluginSpec.setParent(null);
		
		DefaultPluginStateManager pluginStateManager = new DefaultPluginStateManager();
		pluginStateManager.setParentSpec(parentSpec);
		
		try {
			PluginStateUtils.removePlugin(pluginStateManager, new PluginModificationCalculator(), "p");
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin to remove does not have a parent plugin. This is unexpected state and may indicate a bug", e.getMessage());
		}
	}

}
