package org.impalaframework.web.bootstrap;

import java.util.Arrays;

import junit.framework.TestCase;

public class AbridgedExternalBootstrapLocationResolutionStrategyTest extends TestCase {

	public final void testGetFullNames() {
		AbridgedExternalBootstrapLocationResolutionStrategy s = new AbridgedExternalBootstrapLocationResolutionStrategy();
		String[] fullNames = s.getFullNames(new String[]{"boostrap", "some.xml"});
		Arrays.equals(new String[]{"META-INF/impala-boostrap.xml", "some.xml"}, fullNames);
	}

}
