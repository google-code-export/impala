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

import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.constants.LocationConstants;
import org.springframework.core.io.Resource;

public class SimpleJarModuleLocationResolverTest extends TestCase {

	private SimpleJarModuleLocationResolver jarResolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jarResolver = new SimpleJarModuleLocationResolver();
		jarResolver.setWorkspaceRoot("../impala-core/files");
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../impala-core/files");
		jarResolver.init();
	}
	
	public void testGetApplicationModuleClassLocations() throws Exception {
		List<Resource> locations = jarResolver.getApplicationModuleClassLocations("MyTestClass");
		assertEquals(1, locations.size());
		assertNotNull(locations.get(0).getInputStream());
		assertEquals("MyTestClass.jar", locations.get(0).getFilename());
	}
	
	public void testWithApplicationVersion() throws Exception {
		jarResolver.setApplicationVersion("1.0");
		List<Resource> locations = jarResolver.getApplicationModuleClassLocations("MyTestClass");
		assertEquals(1, locations.size());
		assertEquals("MyTestClass-1.0.jar", locations.get(0).getFilename());
	}

}
