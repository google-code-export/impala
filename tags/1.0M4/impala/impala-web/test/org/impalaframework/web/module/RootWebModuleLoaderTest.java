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

package org.impalaframework.web.module;

import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import junit.framework.TestCase;

public class RootWebModuleLoaderTest extends TestCase {

	private RootWebModuleLoader moduleLoader;
	private ServletContext servletContext;
	private ConfigurableApplicationContext applicationContext;
	private SimpleModuleDefinition moduleDefinition;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleLoader = new RootWebModuleLoader();
		servletContext = createMock(ServletContext.class);
		applicationContext = createMock(ConfigurableApplicationContext.class);
		moduleDefinition = new SimpleModuleDefinition("module");
		moduleLoader.setServletContext(servletContext);
	}

	public void testAfterRefreshNoContextPresent() {
		expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(null);
		
		replay(servletContext);
		moduleLoader.afterRefresh(applicationContext, moduleDefinition);
		verify(servletContext);
	}

	public void testAfterRefreshContextPresent() {
		expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(new Object());
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
		
		replay(servletContext);
		moduleLoader.afterRefresh(applicationContext, moduleDefinition);
		verify(servletContext);
	}

}
