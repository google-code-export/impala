package org.impalaframework.module.loader;

import junit.framework.TestCase;

import org.impalaframework.module.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.resolver.PropertyClassLocationResolver;

public class ManualReloadingParentPluginLoaderTest extends TestCase {

	public final void testGetClassLocations() {
		assertEquals(0, new ManualReloadingParentPluginLoader(new PropertyClassLocationResolver()).getClassLocations(null).length);
	}

}
