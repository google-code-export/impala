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

import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.easymock.EasyMock.*;

import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.helper.ImpalaServletUtils;

import junit.framework.TestCase;

public class ModuleProxyFilterTest extends TestCase {

	private ModuleProxyFilter filter;
	private AttributeServletContext servletContext;
	private HashMap<String, String> initParameters;
	private IntegrationFilterConfig filterConfig;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private InvocationAwareFilterChain chain;
	private Filter delegateFilter;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		filter = new ModuleProxyFilter() {

			@Override
			protected HttpServletRequest wrappedRequest(
					HttpServletRequest request, ServletContext servletContext, String moduleName) {
				return request;
			}
			
		};
		servletContext = new AttributeServletContext();
		initParameters = new HashMap<String, String>();
		filterConfig = new IntegrationFilterConfig(initParameters, servletContext, "myfilter");
		
		request = createMock(HttpServletRequest.class);
		response = createMock(HttpServletResponse.class);
		delegateFilter = createMock(Filter.class);
		chain = new InvocationAwareFilterChain();
	}
	
	public void testModulePrefix() throws ServletException {
		filter.init(filterConfig);
		
		assertSame(filterConfig, filter.getFilterConfig());
		
		initParameters.put("modulePrefix", "myprefix");
		filter.init(filterConfig);
	}
	
	public void testDoFilter() throws Exception {

		expect(request.getServletPath()).andStubReturn("/mymodule/path");
		filter.init(filterConfig);
		
		replayMocks();
		
		filter.doFilter(request, response, servletContext, chain);
		assertTrue(chain.getWasInvoked());
		
		verifyMocks();
	}
	
	public void testDoWithNotMatchingModule() throws Exception {
		
		expect(request.getServletPath()).andStubReturn("/anothermodule/path");
		ImpalaServletUtils.publishFilter(servletContext, "mymodule", delegateFilter);
		filter.init(filterConfig);
		
		replayMocks();
		
		filter.doFilter(request, response, servletContext, chain);
		assertTrue(chain.getWasInvoked());
		
		verifyMocks();
	}
	
	public void testDoWithMatchingModule() throws Exception {

		expect(request.getServletPath()).andStubReturn("/mymodule/path");
		ImpalaServletUtils.publishFilter(servletContext, "mymodule", delegateFilter);
		filter.init(filterConfig);
		
		delegateFilter.doFilter(eq(request), eq(response), isA(InvocationAwareFilterChain.class));
		
		replayMocks();
		
		filter.doFilter(request, response, servletContext, chain);
		assertFalse(chain.getWasInvoked());
		
		verifyMocks();
	}
	
	public void testWithDifferentMapper() throws Exception {

		final HashMap<String, String> initParameters = new HashMap<String, String>();
		initParameters.put(WebConstants.REQUEST_MODULE_MAPPER_CLASS_NAME, TestMapper.class.getName());
		
		filter.init(new IntegrationFilterConfig(initParameters, servletContext, "proxyServlet"));
		
		//this method will eb called on TestMapper
		expect(request.getParameter("moduleName")).andReturn("alternativemodule");
		
		replayMocks();
		
		assertEquals("alternativemodule", filter.getModuleName(request));

		verifyMocks();
	}


	private void replayMocks() {
		replay(request);
		replay(response);
		replay(delegateFilter);
	}

	private void verifyMocks() {
		verify(request);
		verify(response);
		verify(delegateFilter);
	}
	
}
