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

package net.java.impala.spring.shared;

import java.io.File;

import junit.framework.TestCase;
import net.java.impala.classloader.ParentClassLoader;

/**
 * @author Phil Zoio
 */
public class ParentClassLoaderTest extends TestCase {

	public void testLoadClassString() throws Exception {
		ParentClassLoader pcl = new ParentClassLoader(new File[] { new File("bin") });

		// check that this class loader loads the named class
		Class cls1 = Class.forName("net.java.impala.spring.shared.ClassToLoad", false, pcl);
		assertSame(cls1.getClassLoader(), pcl);
	}

}
