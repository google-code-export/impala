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

package org.impalaframework.resolver;

import org.impalaframework.exception.ConfigurationException;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class StandaloneModuleLocationResolverFactoryTest extends TestCase {

	private StandaloneModuleLocationResolverFactory factory;

	@Override
	protected void setUp() throws Exception {
		factory = new StandaloneModuleLocationResolverFactory();
	}

	public void testFactoryDefault() throws Exception {
		StandaloneModuleLocationResolver resolver = (StandaloneModuleLocationResolver) factory.getClassLocationResolver();
		assertEquals("bin", resolver.getProperty("impala.module.class.dir"));
	}

	public void testFactoryWithPathNotExists() throws Exception {
		try {
			System.setProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH,
					"resources/locations/filethatdoesnotexist");
			factory.getClassLocationResolver();
		}
		catch (ConfigurationException e) {
			assertEquals("System property 'impala.execution.file.path' points to location which does not exist: resources/locations/filethatdoesnotexist", e.getMessage());
		}
		finally {
			System.clearProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH);
		}
	}

	public void testFactoryWithNameNotExists() throws Exception {
		try {
			System.setProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME,
					"resources/locations/filethatdoesnotexist");
			factory.getClassLocationResolver();
		}
		catch (ConfigurationException e) {
			assertEquals("System property 'impala.execution.file.name' points to classpath location which could not be found: resources/locations/filethatdoesnotexist", e.getMessage());
		}
		finally {
			System.clearProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME);
		}
	}

	public void testFactoryWithPath() throws Exception {
		try {
			System.setProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME,
					"locations/execution-2.properties");
			StandaloneModuleLocationResolver resolver = (StandaloneModuleLocationResolver) factory.getClassLocationResolver();
			assertEquals("classdir2", resolver.getProperty("impala.module.class.dir"));
		}
		finally {
			System.clearProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH);
		}
	}

	public void testFactoryWithName() throws Exception {
		try {
			System.setProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH,
					"../impala-core/resources/locations/execution-1.properties");
			System.setProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME,
					"../impala-core/locations/execution-2.properties");
			StandaloneModuleLocationResolver resolver = (StandaloneModuleLocationResolver) factory.getClassLocationResolver();
			assertEquals("classdir1", resolver.getProperty("impala.module.class.dir"));
		}
		finally {
			System.clearProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH);
			System.clearProperty(StandaloneModuleLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME);
		}
	}
}
