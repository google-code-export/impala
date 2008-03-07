/*
 * Copyright 2007-2008 the original author or authors.
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

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ClassLoaderUtilsTest extends TestCase {

	public final void testGetSuperclassNames() {
		List<String> superclassNames = ClassLoaderUtils.getClassHierarchyNames(this.getClass().getName());
		System.out.println(superclassNames);
		
		assertEquals(4, superclassNames.size());
		assertEquals(this.getClass().getName(), superclassNames.get(0));
		assertEquals(TestCase.class.getName(), superclassNames.get(1));
		assertEquals(Assert.class.getName(), superclassNames.get(2));
		assertEquals(Object.class.getName(), superclassNames.get(3));
	}

}
