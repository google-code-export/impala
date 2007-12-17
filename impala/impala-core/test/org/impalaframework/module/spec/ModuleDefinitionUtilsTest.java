package org.impalaframework.module.spec;

import org.impalaframework.module.spec.ModuleDefinitionUtils;
import org.impalaframework.module.spec.SimpleModuleDefinition;

import junit.framework.TestCase;

public class ModuleDefinitionUtilsTest extends TestCase {

	public final void testFindPlugin() {
		
		SimpleModuleDefinition spec = new SimpleModuleDefinition("p1");

		assertEquals(1, spec.getContextLocations().size());
		assertEquals("p1-context.xml", spec.getContextLocations().get(0));
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(spec, "c1-full");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(spec, "c2");
		SimpleModuleDefinition child3 = new SimpleModuleDefinition(child2, "c3");
		
		assertSame(ModuleDefinitionUtils.findPlugin("c1-full", spec, true), child1);
		assertSame(ModuleDefinitionUtils.findPlugin("c2", spec, true), child2);
		assertSame(ModuleDefinitionUtils.findPlugin("c3", spec, true), child3);
		assertNull(ModuleDefinitionUtils.findPlugin("c4", spec, true));
		
		assertSame(ModuleDefinitionUtils.findPlugin("c1-full", spec, true), child1);
		assertSame(ModuleDefinitionUtils.findPlugin("c1", spec, false), child1);
	}

}
