package org.impalaframework.module.spec;

import org.impalaframework.module.spec.ConstructedModuleDefinitionSource;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;

import junit.framework.TestCase;

public class ConstructedModuleDefinitionSourceTest extends TestCase {

	public final void testConstructedPluginSpecProvider() {
		ConstructedModuleDefinitionSource provider = new ConstructedModuleDefinitionSource(null);
		assertNull(provider.getModuleDefintion());

		SimpleRootModuleDefinition spec = new SimpleRootModuleDefinition("p");
		provider = new ConstructedModuleDefinitionSource(spec);
		assertSame(spec, provider.getModuleDefintion());
	}

}
