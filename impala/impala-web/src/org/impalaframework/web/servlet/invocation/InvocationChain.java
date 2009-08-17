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

package org.impalaframework.web.servlet.invocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.servlet.invoker.HttpServiceInvoker;

/**
 * Implements Filter and Servlet invocation chain. 
 * Filters invoked in order (as long as each filter calls {@link FilterChain#doFilter(ServletRequest, ServletResponse)}.
 * Finally, {@link Servlet#service(ServletRequest, ServletResponse)} is invoked.
 * 
 * This allows for the same filter/servlet invocation orders as occurs in a web container.
 * The main difference, however, is that only filters/servlets defined within the module are invoked.
 * 
 * @author Phil Zoio
 */
public class InvocationChain implements HttpServiceInvoker, FilterChain {
    
    private final List<Filter> filters = new ArrayList<Filter>();
    private final Servlet servlet;
    private int filterCount;
    
    private boolean incomplete;
    
    /**
     * Invocation chain consisting of zero or more filters, plus optional servlet
     * @param filters
     * @param servlet
     */
    public InvocationChain(
            List<Filter> filters, 
            Servlet servlet) {
        super();
        if (filters != null) {
            this.filters.addAll(filters);
        }
        this.servlet = servlet;
    }

    public void invoke(
            HttpServletRequest request,
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        if (filterCount < filters.size()) {
            int currentCount = filterCount;
            filterCount++;
            
            System.out.println("Getting filter for " + currentCount);
            Filter filter = filters.get(currentCount);
            filter.doFilter(request, response, this);
        }
        else if (servlet != null) {
            servlet.service(request, response);
        } else {
            incomplete = true;
        }
    }    
    
    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        
        invoke((HttpServletRequest)request, (HttpServletResponse)response, this);
        
    }
    
    public boolean isComplete() {
        return !incomplete;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("filters = ");
        if ( this.filters!= null )
        buffer.append(this.filters.toString());
        else buffer.append("value is null"); 
        buffer.append(", ");
        buffer.append("servlet = ");
        if ( this.servlet!= null )
        buffer.append(this.servlet.toString());
        else buffer.append("value is null"); 
        buffer.append("\n");
        return  buffer.toString();
    }
    
}
