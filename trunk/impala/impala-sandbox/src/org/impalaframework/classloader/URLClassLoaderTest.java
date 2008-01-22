package org.impalaframework.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.springframework.util.StringUtils;

public class URLClassLoaderTest extends TestCase {

	private File file;
	private String path;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public final void testURLLoadFromJar() throws Exception {
		file = new File("../impala-core/files/MyTestClass.jar");
		path = StringUtils.cleanPath(file.getCanonicalPath());
		URL url = new URL("file:" + path);
		checkUrl(url);
		
		java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new URL[]{url});
		Class<?> forName = Class.forName("example.test.MyTestClass", false, classLoader);
		forName.newInstance();
	}

	public final void testURLLoadFromFileSystem() throws Exception {
		file = new File("../impala-interactive/bin");
		path = StringUtils.cleanPath(file.getCanonicalPath());
		URL url = new URL("file:" + path + "/");
		checkUrl(url);
		
		java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new URL[]{url});
		Class<?> forName = Class.forName("org.impalaframework.command.interactive.CommandStateConstants", false, classLoader);
		assertTrue(forName.isInterface());
	}
	

	private void checkUrl(URL url) throws IOException {
		File urlFile = new File(url.getFile());
		assertEquals(file.getCanonicalPath(), urlFile.getCanonicalPath());
		
		System.out.println("Using url " + url);
		InputStream openStream = url.openStream();
		assertNotNull(openStream);
	}

}
