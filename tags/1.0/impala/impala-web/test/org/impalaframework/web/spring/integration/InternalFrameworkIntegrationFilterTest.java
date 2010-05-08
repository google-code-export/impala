/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.impalaframework.web.integration.InvocationAwareFilterChain;
import org.springframework.web.context.WebApplicationContext;

public class InternalFrameworkIntegrationFilterTest extends TestCase {

    private InternalFrameworkIntegrationFilter filter;
    private ServletContext servletContext;
    private WebApplicationContext applicationContext;
    private Filter delegateFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private InvocationAwareFilterChain filterChain;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        filter = new InternalFrameworkIntegrationFilter();
        servletContext = createMock(ServletContext.class);
        applicationContext = createMock(WebApplicationContext.class);
        delegateFilter = createMock(Filter.class);
        filter.setApplicationContext(applicationContext);
        filter.setDelegateFilter(delegateFilter);
        
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        filterChain = new InvocationAwareFilterChain();
    }

    public void testInitDestroy() throws ServletException {

        replayMocks();
        filter.init(new IntegrationFilterConfig(new HashMap<String, String>(), servletContext, "myfilter"));
        filter.destroy();
        verifyMocks();
    }

    public void testService() throws ServletException, IOException {
        expect(applicationContext.getClassLoader()).andReturn(null);
        delegateFilter.doFilter(request, response, filterChain);

        replayMocks();
        filter.doFilter(request, response, filterChain);
        verifyMocks();
    }

    private void verifyMocks() {
        verify(servletContext);
        verify(applicationContext);
        verify(delegateFilter);
    }

    private void replayMocks() {
        replay(servletContext);
        replay(applicationContext);
        replay(delegateFilter);
    }
}
