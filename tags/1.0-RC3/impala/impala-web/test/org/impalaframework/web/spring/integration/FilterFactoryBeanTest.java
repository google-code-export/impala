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

import static org.easymock.EasyMock.createMock;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

public class FilterFactoryBeanTest extends TestCase {

    private HttpServletRequest request;
    private ServletContext context;
    private FilterFactoryBean factoryBean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = createMock(HttpServletRequest.class);
        context = createMock(ServletContext.class);
        factoryBean = new FilterFactoryBean();
        factoryBean.setInitParameters(null);
        factoryBean.setFilterName("myfilter");
        factoryBean.setFilterClass(TestFilter.class);
        factoryBean.setServletContext(context);
    }

    public void testGetObject() throws Exception {

        assertTrue(factoryBean.isSingleton());
        assertEquals(Filter.class, factoryBean.getObjectType());
        
        factoryBean.afterPropertiesSet();

        final TestFilter testFilter = (TestFilter) factoryBean.getObject();
        assertTrue(testFilter.isInitialized());
        
        testFilter.doFilter(request, null, null);
        
        factoryBean.destroy();
        assertTrue(testFilter.isDestroyed());
    }

}

class TestFilter implements Filter {

    private boolean initialized = false;
    private boolean destroyed = false;

    public void destroy() {
        destroyed = true;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    }

    public void init(FilterConfig config) throws ServletException {
        initialized = true;
    }

    boolean isInitialized() {
        return initialized;
    }

    boolean isDestroyed() {
        return destroyed;
    }

}
