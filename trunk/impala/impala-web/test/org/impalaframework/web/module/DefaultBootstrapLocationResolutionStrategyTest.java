package org.impalaframework.web.module;

import org.impalaframework.web.module.DefaultBootstrapLocationResolutionStrategy;

import junit.framework.TestCase;

public class DefaultBootstrapLocationResolutionStrategyTest extends TestCase {
	
	private DefaultBootstrapLocationResolutionStrategy strategy;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		strategy = new DefaultBootstrapLocationResolutionStrategy();
	}
	
	public final void testGetBootstrapContextLocations() {
		String[] locations = strategy.getBootstrapContextLocations(null);
		assertEquals(2, locations.length);
		assertEquals("META-INF/impala-bootstrap.xml", locations[0]);
	}

}
