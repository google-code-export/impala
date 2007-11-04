package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.resolver.PropertyClassLocationResolver;

import junit.framework.TestCase;

public class ManualReloadingParentPluginLoaderTest extends TestCase {

	public final void testGetClassLocations() {
		assertEquals(0, new ManualReloadingParentPluginLoader(new PropertyClassLocationResolver()).getClassLocations(null, null).length);
	}

}
