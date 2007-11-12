package org.impalaframework.plugin.spec.modification;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;

import junit.framework.TestCase;

public class PluginModificationCalculatorTest extends TestCase {

	public final void testGetTransitions() {
		PluginModificationCalculator calculator = new PluginModificationCalculator();
		ParentSpec parentSpec = spec("app-context.xml", "plugin1, plugin2");
		PluginTransitionSet transitionsFromOriginal = calculator.getTransitions(parentSpec, null);
		assertSame(parentSpec, transitionsFromOriginal.getNewSpec());
		
		PluginTransitionSet transitionsToNew = calculator.getTransitions(null, parentSpec);
		assertSame(parentSpec, transitionsToNew.getNewSpec());
		
		PluginTransitionSet nullTransitions = calculator.getTransitions(null, null);
		assertNull(nullTransitions.getNewSpec());
	}

	private ParentSpec spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleParentSpec(locations),
				pluginString);
		ParentSpec parentSpec = builder.getParentSpec();
		return parentSpec;
	}

}
