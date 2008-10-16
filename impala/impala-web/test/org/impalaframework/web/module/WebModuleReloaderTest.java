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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.web.WebConstants;

public class WebModuleReloaderTest extends TestCase {

	private ServletContext servletContext;

	private WebModuleReloader reloader;

	private ModuleManagementFacade impalaBootstrapFactory;
	
	private ModuleOperationRegistry moduleOperationRegistry;

	private ModuleOperation moduleOperation;

	private ModuleDefinitionSource moduleDefinitionSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		impalaBootstrapFactory = createMock(ModuleManagementFacade.class);
		moduleDefinitionSource = createMock(ModuleDefinitionSource.class);

		reloader = new WebModuleReloader();
		reloader.setServletContext(servletContext);

		moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		moduleOperation = createMock(ModuleOperation.class);
	}

	public final void testReloadModules() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE)).andReturn(moduleDefinitionSource);
		
		expect(impalaBootstrapFactory.getModuleOperationRegistry()).andReturn(moduleOperationRegistry);
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadRootModuleOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(isA(ModuleOperationInput.class))).andReturn(ModuleOperationResult.TRUE);
	
		replayMocks();
		reloader.reloadModules();
		verifyMocks();
	}

	public final void testNoFactory() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);

		replayMocks();
		try {
			reloader.reloadModules();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("No instance of org.impalaframework.facade.ModuleManagementFacade found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.IMPALA_FACTORY_ATTRIBUTE", e.getMessage());
		}
		verifyMocks();
	}

	public final void testNoModuleDefinitionSource() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE)).andReturn(null);

		replayMocks();
		try {
			reloader.reloadModules();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("No instance of org.impalaframework.module.definition.ModuleDefinitionSource found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE", e.getMessage());
		}
		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(moduleDefinitionSource);
		verify(impalaBootstrapFactory);
		verify(moduleOperationRegistry);
		verify(moduleOperation);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(moduleDefinitionSource);
		replay(impalaBootstrapFactory);
		replay(moduleOperationRegistry);
		replay(moduleOperation);
	}

}
