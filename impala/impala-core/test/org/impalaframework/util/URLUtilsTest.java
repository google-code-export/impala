package org.impalaframework.util;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

public class URLUtilsTest extends TestCase {

	public final void testCreateUrls() throws Exception {
		File file1 = new File("../impala-core/src");
		File file2 = new File("../impala-repository/test/junit-3.8.1.jar");
		URL[] urls = URLUtils.createUrls(new File[] { file1, file2 });
		assertEquals(2, urls.length);
		
		assertTrue(urls[0].getFile().endsWith("/impala-core/src/"));
		assertTrue(urls[1].getFile().endsWith("impala-repository/test/junit-3.8.1.jar"));
		
		for (URL url : urls) {
			assertNotNull(url.openConnection());
			assertNotNull(url.openStream());
		}
	}

}
