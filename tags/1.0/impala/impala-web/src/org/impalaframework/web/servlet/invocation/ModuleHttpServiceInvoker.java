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

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.web.servlet.invoker.HttpServiceInvoker;
import org.impalaframework.web.utils.WebPathUtils;

/**
 * Implementation of {@link HttpServiceInvoker} responsible for invoking filters or servlets registered within the module.
 * Registration of filters or servlets within the module is based on suffix.
 * 
 * @author Phil Zoio
 */
public class ModuleHttpServiceInvoker implements HttpServiceInvoker {
    
    public static final String EMPTY_SUFFIX = "[none]";
    public static final String ALL_EXTENSIONS = "*";

    /**
     * Mapping of extensions to filters. Note that filters are listed in invocation order
     */
    private Map<String,List<Filter>> filters = new LinkedHashMap<String, List<Filter>>();
    
    /**
     * Mapping of extensions to servlet. Only a single servlet can be mapped to an extension within 
     * a module
     */
    private Map<String,Servlet> servlets = new LinkedHashMap<String, Servlet>();
    
    private boolean globalMappingOnly;
    
    public ModuleHttpServiceInvoker(
            Map<String, List<Filter>> suffixFilters,
            Map<String, Servlet> suffixServlets) {
        this.filters.putAll(suffixFilters);
        this.servlets.putAll(suffixServlets);
        
        this.globalMappingOnly = determineGlobalMappingStatus();
    }

    /**
     * Determines suffix for incoming request. Then builds an invocation chain using the filters and servlets
     * which are mapped to the suffix for the module. Invokes this chain.
     *
     * If processing is complete, either through the request being handled by a servlet, or one of the filters
     * in the chain not invoking {@link FilterChain#doFilter(ServletRequest, ServletResponse)}, the method simply
     * returns after invoking the chain.
     * 
     * However, is processing is not complete (through absence of both the above conditions), then if a
     * {@link FilterChain} argument is supplied, this will be invoked.
     * 
     * In other words, if the request is not fully processed by the module, it is still possible for another
     * filter or even servlet to process the request. This however, is likely to be an unusual case: for the most
     * part, if a request directed to the module, it will be processed within that module.
     */
    public void invoke(
            HttpServletRequest request,
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        String suffix = null;
        
        if (this.globalMappingOnly) {
            suffix = ALL_EXTENSIONS;
        }
        else {
            //check suffix of request
            String uri = request.getRequestURI();
            suffix = WebPathUtils.getSuffix(uri);
            if (suffix == null) {
                suffix = EMPTY_SUFFIX;
            }
        }
        
        //create invocation chain with 
        List<Filter> invocationFilters = filters.get(suffix);
        Servlet invocationServlet = servlets.get(suffix);
        
        if (invocationFilters == null && invocationServlet == null && !this.globalMappingOnly) {
            invocationFilters = filters.get(ALL_EXTENSIONS);
            invocationServlet = servlets.get(ALL_EXTENSIONS);
        }
        
        InvocationChain chain = new InvocationChain(invocationFilters, invocationServlet);
        
        chain.invoke(request, response, filterChain);
        
        if (filterChain != null) {
            if (!chain.isComplete()) {
                filterChain.doFilter(request, response);
            }
        }
    }
    
    private boolean determineGlobalMappingStatus() {
        boolean maybeGlobal = true;
        
        if (this.filters.size() > 1 || this.servlets.size() > 1) {
            //one of filters or servlets has more than one key
            maybeGlobal = false;
        }
        if (maybeGlobal) {
            if (this.filters.size() == 0 && this.servlets.size() == 0) {
                //both of filters and servlets have no keys
                maybeGlobal = false;
            }
        }
        
        if (maybeGlobal) {
            String firstFilterKey = ObjectMapUtils.getFirstKey(this.filters);
            if (firstFilterKey != null && !"*".equals(firstFilterKey)) {
                //filter has non-global mapping key
                maybeGlobal = false;
            }
        }
        
        if (maybeGlobal) {
            String firstServletKey = ObjectMapUtils.getFirstKey(this.servlets);
            if (firstServletKey != null && !"*".equals(firstServletKey)) {
                //filter has non-global mapping key
                maybeGlobal = false;
            }
        }
        return maybeGlobal;
    }
    
    public Map<String, List<Filter>> getFilters() {
        return Collections.unmodifiableMap(filters);
    }
    
    public Map<String, Servlet> getServlets() {
        return Collections.unmodifiableMap(servlets);
    } 
    
    public boolean isGlobalMappingOnly() {
        return globalMappingOnly;
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
        buffer.append("servlets = ");
        if ( this.servlets!= null )
        buffer.append(this.servlets.toString());
        else buffer.append("value is null"); 
        buffer.append("\n");
        return  buffer.toString();
    }

    
  
}
