package org.impalaframework.spring.plugin;

import junit.framework.TestCase;

import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;

public class PluginMetadataPostProcessorTest extends TestCase {

	public final void testPostProcessBeforeInitialization() {
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition("context.xml");
		PluginMetadataPostProcessor postProcessor = new PluginMetadataPostProcessor(parentSpec);
		TestSpecAware testAware = new TestSpecAware();
		postProcessor.postProcessBeforeInitialization(testAware, null);
		assertSame(parentSpec, testAware.getPluginSpec());
		
		assertSame(testAware, postProcessor.postProcessAfterInitialization(testAware, null));
	}
}

class TestSpecAware implements PluginSpecAware {

	private ModuleDefinition moduleDefinition;

	public ModuleDefinition getPluginSpec() {
		return moduleDefinition;
	}

	public void setPluginSpec(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

}
