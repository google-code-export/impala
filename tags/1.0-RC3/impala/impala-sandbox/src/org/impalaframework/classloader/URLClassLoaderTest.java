/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
