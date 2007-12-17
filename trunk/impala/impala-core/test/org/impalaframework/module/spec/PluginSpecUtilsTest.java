package org.impalaframework.module.spec;

import org.impalaframework.module.spec.PluginSpecUtils;
import org.impalaframework.module.spec.SimplePluginSpec;

import junit.framework.TestCase;

public class PluginSpecUtilsTest extends TestCase {

	public final void testFindPlugin() {
		
		SimplePluginSpec spec = new SimplePluginSpec("p1");

		assertEquals(1, spec.getContextLocations().size());
		assertEquals("p1-context.xml", spec.getContextLocations().get(0));
		SimplePluginSpec child1 = new SimplePluginSpec(spec, "c1-full");
		SimplePluginSpec child2 = new SimplePluginSpec(spec, "c2");
		SimplePluginSpec child3 = new SimplePluginSpec(child2, "c3");
		
		assertSame(PluginSpecUtils.findPlugin("c1-full", spec, true), child1);
		assertSame(PluginSpecUtils.findPlugin("c2", spec, true), child2);
		assertSame(PluginSpecUtils.findPlugin("c3", spec, true), child3);
		assertNull(PluginSpecUtils.findPlugin("c4", spec, true));
		
		assertSame(PluginSpecUtils.findPlugin("c1-full", spec, true), child1);
		assertSame(PluginSpecUtils.findPlugin("c1", spec, false), child1);
	}

}
