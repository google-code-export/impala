package org.impalaframework.spring.plugin;

import junit.framework.TestCase;

import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.SimpleParentSpec;

public class PluginMetadataPostProcessorTest extends TestCase {

	public final void testPostProcessBeforeInitialization() {
		SimpleParentSpec parentSpec = new SimpleParentSpec("context.xml");
		PluginMetadataPostProcessor postProcessor = new PluginMetadataPostProcessor(parentSpec);
		TestSpecAware testAware = new TestSpecAware();
		postProcessor.postProcessBeforeInitialization(testAware, null);
		assertSame(parentSpec, testAware.getPluginSpec());
		
		assertSame(testAware, postProcessor.postProcessAfterInitialization(testAware, null));
	}
}

class TestSpecAware implements PluginSpecAware {

	private PluginSpec pluginSpec;

	public PluginSpec getPluginSpec() {
		return pluginSpec;
	}

	public void setPluginSpec(PluginSpec pluginSpec) {
		this.pluginSpec = pluginSpec;
	}

}
