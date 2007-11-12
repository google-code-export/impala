package org.impalaframework.plugin.spec.transition;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.ParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.RegistryBasedApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;

import junit.framework.TestCase;

public class PluginTransitionManagerTest extends TestCase {

	public void testProcessTransitions() {
		PluginTransitionManager tm = new PluginTransitionManager();
		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));
		tm.setContextLoader(new RegistryBasedApplicationContextLoader(registry));

		ParentSpec parentSpec = spec("app-context.xml", "plugin1, plugin2");
		PluginModificationCalculator calculator = new PluginModificationCalculator();
		PluginTransitionSet transitions = calculator.getTransitions(null, parentSpec);
		tm.processTransitions(transitions);
	}

	private ParentSpec spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleParentSpec(locations),
				pluginString);
		ParentSpec parentSpec = builder.getParentSpec();
		return parentSpec;
	}

}
