package net.java.impala.spring.plugin;

import junit.framework.TestCase;

public class SimplePluginSpecTest extends TestCase {

	public void testHasPlugin() {
		SimplePluginSpec spec = new SimplePluginSpec(new String[] { "l1", "l2" }, new String[] { "p1", "p2" });
		assertTrue(spec.hasPlugin("p1"));
		assertTrue(spec.hasPlugin("p2"));
		assertFalse(spec.hasPlugin("p3"));
	}

}
