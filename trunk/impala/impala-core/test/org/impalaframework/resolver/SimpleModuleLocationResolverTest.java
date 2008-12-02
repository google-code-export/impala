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

package org.impalaframework.resolver;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class SimpleModuleLocationResolverTest extends TestCase {

	private SimpleModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new SimpleModuleLocationResolver();
		resolver.setModuleClassDirectory("bin");
		resolver.setModuleTestDirectory("resources");
		resolver.setWorkspaceRoot("../");
		resolver.init();
	}

	public final void testClassLocations() throws IOException {
		List<Resource> classLocations = resolver.getApplicationModuleClassLocations("impala-interactive");
		System.out.println(classLocations);
		assertEquals(1, classLocations.size());
		Resource location = classLocations.get(0);
		assertTrue(location.exists());
		assertEquals("bin", location.getFilename());
		String absolutePath = location.getFile().getAbsolutePath();
		assertTrue(StringUtils.cleanPath(absolutePath).contains("impala-interactive/bin"));
	}

	public final void testDuffLocations() throws IOException {
		try {
			resolver.getApplicationModuleClassLocations("duff");
			fail();
		} catch (InvalidStateException e) {
		}
	}

	public final void testTestClassLocations() throws IOException {
		List<Resource> classLocations = resolver.getModuleTestClassLocations("impala-interactive");
		System.out.println(classLocations);
		assertEquals(1, classLocations.size());
		Resource location = classLocations.get(0);
		assertEquals("resources", location.getFilename());
		String absolutePath = location.getFile().getAbsolutePath();
		assertTrue(StringUtils.cleanPath(absolutePath).contains("impala-interactive/resources"));
	}

}
