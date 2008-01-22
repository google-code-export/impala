package org.impalaframework.module.loader;

import junit.framework.TestCase;

import org.impalaframework.module.loader.ManualReloadingRootModuleLoader;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class ManualReloadingRootModuleLoaderTest extends TestCase {

	public final void testGetClassLocations() {
		assertEquals(0, new ManualReloadingRootModuleLoader(new StandaloneModuleLocationResolver()).getClassLocations(null).length);
	}

}
