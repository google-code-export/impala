package net.java.impala.spring.plugin;

import junit.framework.TestCase;

public class ChildSpecContainerTest extends TestCase {

	public void testChildSpecContainer() {
		final PluginSpec[] strings = new PluginSpec[] { new SimplePluginSpec("p1"), new SimplePluginSpec("p2") };
		ChildSpecContainerImpl spec = new ChildSpecContainerImpl(strings);

		assertTrue(spec.hasPlugin("p1"));
		assertTrue(spec.hasPlugin("p2"));
		assertFalse(spec.hasPlugin("p3"));

		assertEquals(2, spec.getPluginNames().length);
	}

}
