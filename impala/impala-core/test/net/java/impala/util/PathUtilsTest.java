package net.java.impala.util;

import junit.framework.TestCase;

public class PathUtilsTest extends TestCase {

	public void testGetCurrentDirectoryName() {
		assertEquals("impala-core", PathUtils.getCurrentDirectoryName());
	}

}
