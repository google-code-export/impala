package org.impalaframework.plugin.spec;

import junit.framework.TestCase;

public class ConstructedPluginSpecProviderTest extends TestCase {

	public final void testConstructedPluginSpecProvider() {
		ConstructedPluginSpecProvider provider = new ConstructedPluginSpecProvider(null);
		assertNull(provider.getPluginSpec());

		SimpleParentSpec spec = new SimpleParentSpec("p");
		provider = new ConstructedPluginSpecProvider(spec);
		assertSame(spec, provider.getPluginSpec());
	}

}
