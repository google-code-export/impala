package org.impalaframework.spring.plugin;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class PluginMetadataPostProcessorTest extends TestCase {

	public final void testPostProcessBeforeInitialization() {
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition("context.xml");
		ModuleDefinitionPostProcessor postProcessor = new ModuleDefinitionPostProcessor(parentSpec);
		TestSpecAware testAware = new TestSpecAware();
		postProcessor.postProcessBeforeInitialization(testAware, null);
		assertSame(parentSpec, testAware.getModuleDefinition());
		
		assertSame(testAware, postProcessor.postProcessAfterInitialization(testAware, null));
	}
}

class TestSpecAware implements ModuleDefinitionAware {

	private ModuleDefinition moduleDefinition;

	public ModuleDefinition getModuleDefinition() {
		return moduleDefinition;
	}

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

}
