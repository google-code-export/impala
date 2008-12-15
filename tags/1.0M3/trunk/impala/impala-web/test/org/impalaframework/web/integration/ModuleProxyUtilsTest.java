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

package org.impalaframework.web.integration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.wrapper.IdentityHttpRequestWrapperFactory;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class ModuleProxyUtilsTest extends TestCase {
	
	public void testGetModuleName() {
		assertEquals("somepath", ModuleProxyUtils.getModuleName("/somepath/more.htm", null));
		assertEquals(null, ModuleProxyUtils.getModuleName("/apage.htm", null));
	}

	public void testGetModuleNameWithPrefix() {
		assertEquals("prefix-somepath", ModuleProxyUtils.getModuleName("/somepath/more.htm", "prefix-"));
		assertEquals(null, ModuleProxyUtils.getModuleName("/apage.htm", "prefix-"));
	}
	
	public void testGetRequestWrapper() throws Exception {
		final ServletContext servletContext = createMock(ServletContext.class);
		final HttpServletRequest request = createMock(HttpServletRequest.class);
		final ModuleManagementFacade facade = createMock(ModuleManagementFacade.class);
		
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(facade);
		expect(facade.getBean(WebConstants.REQUEST_WRAPPER_FACTORY_BEAN_NAME)).andReturn(new IdentityHttpRequestWrapperFactory());
		
		replayMocks(servletContext, request, facade);
		
		assertSame(request, ModuleProxyUtils.getWrappedRequest(request, servletContext, "mymodule"));
		
		verifyMocks(servletContext, request, facade);
	}	

	public void testGetRequestWrapperFacadeNull() throws Exception {
		final ServletContext servletContext = createMock(ServletContext.class);
		final HttpServletRequest request = createMock(HttpServletRequest.class);
		final ModuleManagementFacade facade = createMock(ModuleManagementFacade.class);
		
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);
		
		replayMocks(servletContext, request, facade);
		
		assertSame(request, ModuleProxyUtils.getWrappedRequest(request, servletContext, "mymodule"));
		
		verifyMocks(servletContext, request, facade);
	}
	
	public void testGetRequestWrapperFactoryNull() throws Exception {
		final ServletContext servletContext = createMock(ServletContext.class);
		final HttpServletRequest request = createMock(HttpServletRequest.class);
		final ModuleManagementFacade facade = createMock(ModuleManagementFacade.class);

		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(facade);
		expect(facade.getBean(WebConstants.REQUEST_WRAPPER_FACTORY_BEAN_NAME)).andReturn(null);
		
		replayMocks(servletContext, request, facade);
		
		assertSame(request, ModuleProxyUtils.getWrappedRequest(request, servletContext, "mymodule"));
		
		verifyMocks(servletContext, request, facade);
	}

	private void verifyMocks(final ServletContext servletContext,
			final HttpServletRequest request,
			final ModuleManagementFacade facade) {
		verify(request);
		verify(servletContext);
		verify(facade);
	}

	private void replayMocks(final ServletContext servletContext,
			final HttpServletRequest request,
			final ModuleManagementFacade facade) {
		replay(request);
		replay(servletContext);
		replay(facade);
	}
	
}
