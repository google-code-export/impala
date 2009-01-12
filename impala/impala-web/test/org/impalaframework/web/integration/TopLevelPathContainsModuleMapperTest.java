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

import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

public class TopLevelPathContainsModuleMapperTest extends TestCase {

	private HttpServletRequest request;
	private TopLevelPathContainsModuleMapper mapper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mapper = new TopLevelPathContainsModuleMapper();
		request = createMock(HttpServletRequest.class);
	}
	
	public void testNoModuleNames() {
		mapper.setModuleNames(new String[0]);
		
		replay(request);

		assertEquals(null, mapper.getModuleForRequest(request));
		
		verify(request);
	}
	
	public void testGetModuleSameCase() {
		mapper.setModuleNames(new String[]{"one", "two"});
		
		expect(request.getServletPath()).andReturn("/onePath/file.html");
		
		replay(request);
		
		assertEquals("one", mapper.getModuleForRequest(request));
		
		verify(request);
	}
	
	public void testGetModuleUpperCase() {
		mapper.setModuleNames(new String[]{"one", "two"});
		
		expect(request.getServletPath()).andReturn("/OnePath/file.html");
		
		replay(request);
		
		assertEquals("one", mapper.getModuleForRequest(request));
		
		verify(request);
	}
	
	public void testGetModuleNonMatching() {
		mapper.setModuleNames(new String[]{"one", "two"});
		
		expect(request.getServletPath()).andReturn("/ThreePath/file.html");
		
		replay(request);
		
		assertEquals(null, mapper.getModuleForRequest(request));
		
		verify(request);
	}

}
