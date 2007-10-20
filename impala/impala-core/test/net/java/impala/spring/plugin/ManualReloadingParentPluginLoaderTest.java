package net.java.impala.spring.plugin;

import net.java.impala.location.PropertyClassLocationResolver;
import junit.framework.TestCase;

public class ManualReloadingParentPluginLoaderTest extends TestCase {

	public final void testGetClassLocations() {
		assertEquals(0, new ManualReloadingParentPluginLoader(new PropertyClassLocationResolver()).getClassLocations(null, null).length);
	}

}
