package net.java.impala.spring.plugin;

import junit.framework.TestCase;

public class SimpleParentSpecTest extends TestCase {

	public void testEquals() {
		SimpleParentSpec spec1 = new SimpleParentSpec(new String[]{"p1", "p2"});
		SimpleParentSpec spec2 = new SimpleParentSpec(new String[]{"p1", "p2"});
		assertEquals(spec1, spec2);
		SimpleParentSpec spec3 = new SimpleParentSpec(new String[]{"p1"});
		SimpleParentSpec spec4 = new SimpleParentSpec(new String[]{"p1", "p3"});
		assertFalse(spec1.equals(spec3));
		assertFalse(spec1.equals(spec4));
	}
	
	public void testContains() {
		SimpleParentSpec spec1 = new SimpleParentSpec(new String[]{"p1", "p2"});
		SimpleParentSpec spec2 = new SimpleParentSpec(new String[]{"p1", "p2"});
		assertTrue(spec1.containsAll(spec2));
		assertTrue(spec2.containsAll(spec2));
		SimpleParentSpec spec3 = new SimpleParentSpec(new String[]{"p1"});
		SimpleParentSpec spec4 = new SimpleParentSpec(new String[]{"p1", "p3"});
		assertTrue(spec1.containsAll(spec3));
		assertFalse(spec3.containsAll(spec1));
		assertFalse(spec1.containsAll(spec4));
	}

}
