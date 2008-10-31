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

package org.impalaframework.osgi.classloader;

import org.easymock.EasyMock;
import org.impalaframework.exception.InvalidStateException;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.BundleDelegatingClassLoader;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class OSGiClassLoaderFactoryTest extends TestCase {

	private OSGiClassLoaderFactory factory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = new OSGiClassLoaderFactory();
	}
	
	public void testNewClassLoaderInvalid() {
		try {
			factory.newClassLoader(ClassUtils.getDefaultClassLoader(), "duffvalue");
			fail();
		} catch (InvalidStateException e) {
			assertEquals("'data' must be instance of Bundle. Actual type: java.lang.String", e.getMessage());
		}
	}
	
	public void testNewClassLoader() throws Exception {
		final ClassLoader classLoader = factory.newClassLoader(ClassUtils.getDefaultClassLoader(), EasyMock.createMock(Bundle.class));
		assertTrue(classLoader instanceof BundleDelegatingClassLoader);
	}

}
