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

package org.impalaframework.web.servlet.invocation;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.util.ObjectMapUtils;

public class InvocationChainTest extends TestCase {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Filter filter1;
    private Filter filter2;
    
    private List<Filter> filters;
    private HttpServlet servlet;
    private RealFilter realFilter1;
    private RealFilter realFilter2;
    private RealServlet realServlet;

    public void setUp() throws Exception {
        filters = new ArrayList<Filter>();
        
        servlet = createMock(HttpServlet.class);
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        filter1 = createMock(Filter.class);
        filter2 = createMock(Filter.class);
        
        realFilter1 = new RealFilter("realFilter1");
        realFilter2 = new RealFilter("realFilter2");
        realServlet = new RealServlet();
    }    
    
    public void testNone() throws Exception {

        replay(servlet);
        replay(request, response, filter1, filter2);
        
        InvocationChain chain = new InvocationChain(filters, null);
        chain.invoke(request, response, null);

        verify(servlet);
        verify(request, response, filter1, filter2);
    }
    
    public void testServletOnly() throws Exception {
        
        servlet.service(request, response);

        replay(servlet);
        replay(request, response, filter1, filter2);
        
        InvocationChain chain = new InvocationChain(filters, servlet);
        chain.invoke(request, response, null);

        verify(servlet);
        verify(request, response, filter1, filter2);
    }
    
    public void testFiltersOnly() throws Exception {
        
        filters.add(filter1);
        filters.add(filter2);

        InvocationChain chain = new InvocationChain(filters, null);
        
        filter1.doFilter(request, response, chain);

        replay(servlet);
        replay(request, response, filter1, filter2);

        chain.invoke(request, response, null);

        verify(servlet);
        verify(request, response, filter1, filter2);
        
        //complete because not all filters executed
        assertTrue(chain.isComplete());
    }
    
    public void testRealFiltersWithServlet() throws Exception {
        
        filters.add(realFilter1);
        filters.add(realFilter2);

        InvocationChain chain = new InvocationChain(filters, realServlet);

        chain.invoke(request, response, null);
        
        assertEquals(1, realFilter1.getInvokeCount());
        assertEquals(1, realFilter2.getInvokeCount());
        assertEquals(1, realServlet.getInvokeCount());
        
        //complete because servlet at end
        assertTrue(chain.isComplete());
    }
    
    public void testRealFiltersWithNoServlet() throws Exception {
        
        filters.add(realFilter1);
        filters.add(realFilter2);

        InvocationChain chain = new InvocationChain(filters, null);

        chain.invoke(request, response, null);
        
        //incomplete because no servlet at end
        assertFalse(chain.isComplete());
    }
    
    public void testModuleServiceInvoker() throws Exception {
        
        filters.add(filter1);
        filters.add(filter2);
       
        Map<String, List<Filter>> filterMap = Collections.singletonMap("exe", filters);
        Map<String, Servlet> servletMap = new HashMap<String, Servlet>();
        servletMap.put("exe", servlet);
        ModuleHttpServiceInvoker invoker = new ModuleHttpServiceInvoker(filterMap, servletMap);
        
        expect(request.getRequestURI()).andReturn("/apath/afile.exe");
        filter1.doFilter(eq(request), eq(response), isA(InvocationChain.class));

        replay(servlet);
        replay(request, response, filter1, filter2);

        invoker.invoke(request, response, null);
        
        verify(servlet);
        verify(request, response, filter1, filter2);
    }

    @SuppressWarnings("unchecked")
    public void testInvokeGlobalOnly() throws Exception {
        
        filters.add(filter1);
        filters.add(filter2);
        
        ModuleHttpServiceInvoker invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap("*", filters), 
                ObjectMapUtils.newMap("*", servlet));
        assertTrue(invoker.isGlobalMappingOnly());
        
        //note the absense of any calls to figure out the suffix
        filter1.doFilter(eq(request), eq(response), isA(InvocationChain.class));
        
        replay(servlet);
        replay(request, response, filter1, filter2);

        invoker.invoke(request, response, null);
        
        verify(servlet);
        verify(request, response, filter1, filter2);
    }
    
    @SuppressWarnings("unchecked")
    public void testInvokeNone() throws Exception {
        
        filters.add(filter1);
        filters.add(filter2);
        
        ModuleHttpServiceInvoker invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap("*", filters), 
                ObjectMapUtils.newMap("[none]", servlet));
        assertFalse(invoker.isGlobalMappingOnly());
        
        //note the absense of any calls to figure out the suffix
        expect(request.getRequestURI()).andReturn("/apath/filewithoutextension");
        servlet.service(request, response);
        
        replay(servlet);
        replay(request, response, filter1, filter2);

        invoker.invoke(request, response, null);
        
        verify(servlet);
        verify(request, response, filter1, filter2);
    }
    
    @SuppressWarnings("unchecked")
    public void testIsGlobalMappingOnly() throws Exception {
        
        filters.add(filter1);
        ModuleHttpServiceInvoker invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap("*", filter1), 
                ObjectMapUtils.newMap("*", servlet));
        assertTrue(invoker.isGlobalMappingOnly());

        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap(), 
                ObjectMapUtils.newMap("*", servlet));
        assertTrue(invoker.isGlobalMappingOnly());

        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap("*", filter1), 
                ObjectMapUtils.newMap());
        assertTrue(invoker.isGlobalMappingOnly());

        //no mappings - must be false
        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap(), 
                ObjectMapUtils.newMap());
        assertFalse(invoker.isGlobalMappingOnly());

        //more than one mapping
        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap("*", filter1, "another", filter1), 
                ObjectMapUtils.newMap());
        assertFalse(invoker.isGlobalMappingOnly());
        
        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap(), 
                ObjectMapUtils.newMap("*", servlet, "another", servlet));
        assertFalse(invoker.isGlobalMappingOnly());
        
        //mapping to different extension
        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap(), 
                ObjectMapUtils.newMap("exe", servlet));
        assertFalse(invoker.isGlobalMappingOnly());

        invoker = new ModuleHttpServiceInvoker(
                ObjectMapUtils.newMap("exe", filter1), 
                ObjectMapUtils.newMap());
        assertFalse(invoker.isGlobalMappingOnly());
    }
    
    public void testInvokerDuffSuffix() throws Exception {
        
        filters.add(filter1);
        filters.add(filter2);
        
        Map<String, List<Filter>> singletonMap = Collections.singletonMap("exe", filters);
        Map<String, Servlet> servlets = new HashMap<String, Servlet>();
        servlets.put("exe", servlet);
        ModuleHttpServiceInvoker invoker = new ModuleHttpServiceInvoker(singletonMap, servlets);
        
        expect(request.getRequestURI()).andReturn("/apath/afile.duff");
        //note no filter call

        replay(servlet);
        replay(request, response, filter1, filter2);

        invoker.invoke(request, response, null);
        
        verify(servlet);
        verify(request, response, filter1, filter2);
    }
    
}

class RealFilter implements Filter {

    private int invokeCount;
    
    private String name;

    public RealFilter(String name) {
        this.name = name;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, 
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        System.out.println("Invoking " + name);
        invokeCount++;
        chain.doFilter(request, response);
        System.out.println("Finished invoking " + name);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
    
    public int getInvokeCount() {
        return invokeCount;
    }
    
}

class RealServlet extends HttpServlet {
    
    private int invokeCount;
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        invokeCount++;
        System.out.println("Invoking servlet");
    }
    
    public int getInvokeCount() {
        return invokeCount;
    }
}
