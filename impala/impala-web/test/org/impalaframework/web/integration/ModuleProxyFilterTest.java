package org.impalaframework.web.integration;

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

import java.util.HashMap;

import javax.servlet.ServletException;

import org.impalaframework.web.AttributeServletContext;

import junit.framework.TestCase;

public class ModuleProxyFilterTest extends TestCase {

	private ModuleProxyFilter filter;
	private AttributeServletContext servletContext;
	private HashMap<String, String> initParameters;
	private IntegrationFilterConfig filterConfig;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		filter = new ModuleProxyFilter();
		servletContext = new AttributeServletContext();
		initParameters = new HashMap<String, String>();
		filterConfig = new IntegrationFilterConfig(initParameters, servletContext, "myfilter");
	}
	
	public void testModulePrefix() throws ServletException {
		filter.init(filterConfig);
		
		assertSame(filterConfig, filter.getFilterConfig());
		assertNull(filter.getModulePrefix());
		
		initParameters.put("modulePrefix", "myprefix");
		filter.init(filterConfig);
		
		assertEquals("myprefix", filter.getModulePrefix());
	}
	
}
