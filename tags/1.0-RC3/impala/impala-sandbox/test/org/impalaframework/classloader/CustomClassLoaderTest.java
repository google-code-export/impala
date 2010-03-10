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
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class CustomClassLoaderTest extends TestCase {

	public void testLoadClassString() throws Exception {
		CustomClassLoader tcl = new CustomClassLoader(new File[] { new File("../impala-core/files") });

		// check that this class loader loads the named class
		Class<?> cls = Class.forName("ExternalClass", false, tcl);
		assertSame(cls.getClassLoader(), tcl);

		Map<String, Class<?>> loadedClasses = tcl.getLoadedClasses();
		assertEquals(1, loadedClasses.size());
		assertNotNull(loadedClasses.get("ExternalClass"));

		// but not a String
		cls = Class.forName("java.lang.String", false, tcl);
		assertNotSame(cls.getClassLoader(), tcl);

		cls = Class.forName("org.impalaframework.classloader.ClassLoaderFactory", false, tcl);
		assertNotSame(cls.getClassLoader(), tcl);
	}

}
