package org.impalaframework.plugin.loader;

import junit.framework.TestCase;

import org.impalaframework.resolver.PropertyClassLocationResolver;

public class ManualReloadingParentPluginLoaderTest extends TestCase {

	public final void testGetClassLocations() {
		assertEquals(0, new ManualReloadingParentPluginLoader(new PropertyClassLocationResolver()).getClassLocations(null).length);
	}

}
