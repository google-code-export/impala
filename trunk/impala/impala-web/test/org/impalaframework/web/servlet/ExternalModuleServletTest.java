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

package org.impalaframework.web.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.ModuleStateChangeListener;
import org.impalaframework.module.ModuleStateChangeNotifier;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spring.DefaultSpringRuntimeModule;
import org.impalaframework.web.WebConstants;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public class ExternalModuleServletTest extends TestCase {
	
	private ServletConfig servletConfig;

	private ServletContext servletContext;

	private ModuleManagementFacade facade;

	private ModuleStateHolder moduleStateHolder;
	
	private ModuleStateChangeNotifier notifier;

	private ExternalModuleServlet servlet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servletConfig = createMock(ServletConfig.class);
		servletContext = createMock(ServletContext.class);
		facade = createMock(ModuleManagementFacade.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);
		notifier = createMock(ModuleStateChangeNotifier.class);


		servlet = new ExternalModuleServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public ServletConfig getServletConfig() {
				return servletConfig;
			}
		};
	}

	public final void testNull() {
		commonExpections();
		expect(moduleStateHolder.getModule("servletName")).andReturn(null);

		replayMocks();

		try {
			servlet.createWebApplicationContext();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("No module registered under the name of servlet 'servletName'", e.getMessage());
		}

		verifyMocks();
	}

	public final void testNot() {
		commonExpections();
		ConfigurableApplicationContext applicationContext = createMock(ConfigurableApplicationContext.class);
		expect(moduleStateHolder.getModule("servletName")).andReturn(springRuntimeModule(applicationContext));

		replayMocks();

		try {
			servlet.createWebApplicationContext();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Module registered under name of servlet 'servletName' needs to be an instance of org.springframework.web.context.WebApplicationContext", e.getMessage());
		}

		verifyMocks();
	}

	private DefaultSpringRuntimeModule springRuntimeModule(
			ConfigurableApplicationContext applicationContext) {
		DefaultSpringRuntimeModule springRuntimeModule = new DefaultSpringRuntimeModule(new SimpleModuleDefinition(""), applicationContext);
		return springRuntimeModule;
	}

	public final void testWeb() {
		commonExpections();
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
		expect(moduleStateHolder.getModule("servletName")).andReturn(springRuntimeModule(applicationContext));

		replayMocks();

		assertSame(applicationContext, servlet.createWebApplicationContext());

		verifyMocks();
	}

	public final void testPublish() {
		servlet.setPublishServlet(true);
		commonExpections();
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
		expect(moduleStateHolder.getModule("servletName")).andReturn(springRuntimeModule(applicationContext));
		expect(servlet.getServletContextAttributeName()).andReturn("servletContextAttribute");
		expect(servlet.getServletContext()).andReturn(servletContext);
		servletContext.setAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX + "servletContextAttribute", applicationContext);
		expect(servlet.getServletContext()).andReturn(servletContext);
		expect(servlet.getServletName()).andReturn("servletName");
		servletContext.setAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + "servletName", servlet);
		servletContext.setAttribute("module_servletName:" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
		
		replayMocks();

		servlet.initWebApplicationContext();

		verifyMocks();
	}

	private void commonExpections() {
		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(facade);
		expect(facade.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(facade.getModuleStateChangeNotifier()).andReturn(notifier);
		notifier.addListener(isA(ModuleStateChangeListener.class));
		expect(servletConfig.getServletName()).andReturn("servletName");
	}

	private void verifyMocks() {
		verify(servletConfig);
		verify(servletContext);
		verify(facade);
		verify(moduleStateHolder);
		verify(notifier);
	}

	private void replayMocks() {
		replay(servletConfig);
		replay(servletContext);
		replay(facade);
		replay(moduleStateHolder);
		replay(notifier);
	}

}
