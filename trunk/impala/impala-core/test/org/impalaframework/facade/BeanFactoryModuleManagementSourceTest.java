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

package org.impalaframework.facade;

import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryModuleManagementSourceTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.clearProperty("bootstrapLocationsResource");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty("bootstrapLocationsResource");
	}
	
	public final void testBootstrapBeanFactory() {

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/impala-bootstrap.xml");
		Object bean = appContext.getBean("moduleManagementFacade");
		ModuleManagementFacade facade = ObjectUtils.cast(bean, ModuleManagementFacade.class);

		assertNotNull(facade.getModuleLocationResolver());
		assertNotNull(facade.getModuleLoaderRegistry());
		assertNotNull(facade.getModificationExtractorRegistry());
		assertNotNull(facade.getModuleStateHolder());
		assertNotNull(facade.getTransitionProcessorRegistry());
		assertNotNull(facade.getModuleLocationResolver());
		assertNotNull(facade.getModuleOperationRegistry());
		
		Object managementFactory = facade.getBean("moduleManagementFacade", new Object[0]);
		assertNotNull(managementFactory);
	}
	
	public final void notestDefaultGetLocationProperties() {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/impala-bootstrap.xml");
		Properties properties = (Properties) appContext.getBean("locationProperties");
		assertEquals("someValue", properties.get("impalaprop"));
	}
	
	public final void testAlternativeGetLocationProperties() {
		System.setProperty("bootstrapLocationsResource", "impala-alternative.properties");
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/impala-bootstrap.xml");
		Properties properties = (Properties) appContext.getBean("locationProperties");
		assertEquals("alternativeValue", properties.get("impalaprop"));
	}

	public final void testDuffGetLocationProperties() {
		System.setProperty("bootstrapLocationsResource", "duff.properties");
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/impala-bootstrap.xml");
		Properties properties = (Properties) appContext.getBean("locationProperties");
		assertEquals(null, properties.get("impalaprop"));
	}


}
