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

package org.impalaframework.web.integration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

/**
 * Base implementation of filter which directs requests to modules
 * 
 * @see org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilter
 * @see ModuleProxyServlet
 * @author Phil Zoio
 */
public abstract class BaseModuleProxyFilter extends BaseLockingProxyFilter {
    
    private static final Log logger = LogFactory.getLog(BaseModuleProxyFilter.class);   
    
    private static final long serialVersionUID = 1L;
    
    private RequestModuleMapper requestModuleMapper;
    
    public BaseModuleProxyFilter() {
        super();
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        this.requestModuleMapper = newRequestModuleMapper(filterConfig);
        this.requestModuleMapper.init(filterConfig);
    }

    protected RequestModuleMapper newRequestModuleMapper(FilterConfig filterConfig) {
        final String requestModuleMapperClass = filterConfig.getInitParameter(WebConstants.REQUEST_MODULE_MAPPER_CLASS_NAME);
        return ModuleProxyUtils.newRequestModuleMapper(requestModuleMapperClass);
    }

    public void destroy() {
    }

    protected void doFilter(HttpServletRequest request, HttpServletResponse response,
            ServletContext context, FilterChain chain)
            throws ServletException, IOException {

        RequestModuleMapping moduleMapping = getModuleMapping(request);
        String applicationId = getApplicationId(request, context);
        
        if (moduleMapping != null) {
            processMapping(context, request, response, chain, moduleMapping, applicationId);
        } else {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Path + '" + request.getRequestURI() + "' does not correspond with any candidate module name. Chaining request.");
            }
            chain.doFilter(request, response);
            
        }
    }

    protected abstract void processMapping(ServletContext context,
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, 
            RequestModuleMapping moduleMapping, 
            String applicationId)
            throws IOException, ServletException;

    RequestModuleMapping getModuleMapping(HttpServletRequest request) {
        return requestModuleMapper.getModuleForRequest(request);
    }
    
    /* **************** protected methods ******************* */

    protected HttpServletRequest wrappedRequest(HttpServletRequest request, ServletContext servletContext, RequestModuleMapping moduleMapping, String applicationId) {
        return ModuleIntegrationUtils.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
    }
    
    /**
     * Subclasses can support this by wiring in a mechanism for retrieving an application identifier 
     * based on information in the {@link HttpServletRequest} and {@link ServletContext}.
     * 
     * This implementation simply returns an empty String
     * @see org.impalaframework.module.spi.Application
     */
    protected String getApplicationId(HttpServletRequest request, ServletContext servletContext) {
        return "";
    }
}
