package org.impalaframework.module.definition;

import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.SimpleModuleDefinition;

import junit.framework.TestCase;

public class ModuleDefinitionUtilsTest extends TestCase {

	public final void testFindPlugin() {
		
		SimpleModuleDefinition definition = new SimpleModuleDefinition("p1");

		assertEquals(1, definition.getContextLocations().size());
		assertEquals("p1-context.xml", definition.getContextLocations().get(0));
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1-full");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
		SimpleModuleDefinition child3 = new SimpleModuleDefinition(child2, "c3");
		
		assertSame(ModuleDefinitionUtils.findDefinition("c1-full", definition, true), child1);
		assertSame(ModuleDefinitionUtils.findDefinition("c2", definition, true), child2);
		assertSame(ModuleDefinitionUtils.findDefinition("c3", definition, true), child3);
		assertNull(ModuleDefinitionUtils.findDefinition("c4", definition, true));
		
		assertSame(ModuleDefinitionUtils.findDefinition("c1-full", definition, true), child1);
		assertSame(ModuleDefinitionUtils.findDefinition("c1", definition, false), child1);
	}

}
