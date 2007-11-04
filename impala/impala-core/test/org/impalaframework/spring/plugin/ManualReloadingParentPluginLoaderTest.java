package org.impalaframework.spring.plugin;

import org.impalaframework.location.PropertyClassLocationResolver;
import org.impalaframework.spring.plugin.ManualReloadingParentPluginLoader;

import junit.framework.TestCase;

public class ManualReloadingParentPluginLoaderTest extends TestCase {

	public final void testGetClassLocations() {
		assertEquals(0, new ManualReloadingParentPluginLoader(new PropertyClassLocationResolver()).getClassLocations(null, null).length);
	}

}
