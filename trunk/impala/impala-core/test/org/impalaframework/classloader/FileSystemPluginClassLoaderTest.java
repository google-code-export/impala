/*
 * Copyright 2007 the original author or authors.
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

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class FileSystemPluginClassLoaderTest extends TestCase {

	public void testLoadClassString() throws Exception {
		FileSystemPluginClassLoader pcl = new FileSystemPluginClassLoader(new File[] { new File("../impala-core/bin") });

		// check that this class loader loads the named class
		Class cls1 = Class.forName("org.impalaframework.classloader.ClassToLoad", false, pcl);
		assertSame(cls1.getClassLoader(), pcl);
	}

}
