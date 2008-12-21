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

package org.impalaframework.web.servlet.wrapper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.spring.module.SpringRuntimeModule;
import org.impalaframework.web.WebConstants;
import org.springframework.util.ClassUtils;

public class ModuleAwareWrapperHttpServletRequestTest extends TestCase {

	private HttpServletRequest request;
	private HttpSession session;
	private ServletContext servletContext;
	private ModuleAwareWrapperHttpServletRequest wrapperRequest;
	private ModuleManagementFacade moduleManagementFacade;
	private ModuleStateHolder moduleStateHolder;
	private SpringRuntimeModule springRuntimeModule;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		request = createMock(HttpServletRequest.class);
		servletContext = createMock(ServletContext.class);
		session = createMock(HttpSession.class);
		moduleManagementFacade = createMock(ModuleManagementFacade.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);
		springRuntimeModule = createMock(SpringRuntimeModule.class);
		wrapperRequest = new ModuleAwareWrapperHttpServletRequest(request, "mymodule", servletContext );
	}
	
	public void testGetSession() {
	
		expect(springRuntimeModule.getClassLoader()).andReturn(ClassUtils.getDefaultClassLoader());
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(moduleManagementFacade);
		expect(moduleManagementFacade.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleStateHolder.getModule("mymodule")).andReturn(springRuntimeModule);
		
		replayMocks();

		HttpSession wrappedSession = wrapperRequest.wrapSession(session);
		assertTrue(wrappedSession instanceof ModuleAwareWrapperHttpSession);
		ModuleAwareWrapperHttpSession moduleAwareSession = (ModuleAwareWrapperHttpSession) wrappedSession;
		assertNotNull(moduleAwareSession.getModuleClassLoader());
		assertSame(session, moduleAwareSession.getRealSession());

		verifyMocks();
	}
	
	public void testGetSessionNoFactoryAvailable() {
		
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);
		
		replayMocks();

		HttpSession wrappedSession = wrapperRequest.wrapSession(session);
		assertSame(session, wrappedSession);

		verifyMocks();
	}

	private void verifyMocks() {
		verify(request);
		verify(servletContext);
		verify(moduleManagementFacade);
		verify(moduleStateHolder);
		verify(springRuntimeModule);
	}

	private void replayMocks() {
		replay(request);
		replay(servletContext);
		replay(moduleManagementFacade);
		replay(moduleStateHolder);
		replay(springRuntimeModule);
	}

}
