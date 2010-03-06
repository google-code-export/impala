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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.utils.WebPathUtils;

/**
 * <p>
 * Base implementation of Servlet who's job it is to figure out the mapping to a particular request and
 * redirect this to the correct module servlet instance. 
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleProxyServlet extends HttpServlet {

    private static final Log logger = LogFactory.getLog(BaseModuleProxyServlet.class);  
    
    private static final long serialVersionUID = 1L;
    
    private RequestModuleMapper requestModuleMapper;
    
    public BaseModuleProxyServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.requestModuleMapper = newRequestModuleMapper(config);
        this.requestModuleMapper.init(config);
    }

    protected RequestModuleMapper newRequestModuleMapper(ServletConfig config) {
        final String requestModuleMapperClass = config.getInitParameter(WebConstants.REQUEST_MODULE_MAPPER_CLASS_NAME);
        return ModuleProxyUtils.newRequestModuleMapper(requestModuleMapperClass);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        WebPathUtils.maybeLogRequest(request, logger);
        
        ServletContext context = getServletContext();
        doService(request, response, context);
        
    }

    void doService(HttpServletRequest request, HttpServletResponse response,
            ServletContext context)
            throws ServletException, IOException {
        
        RequestModuleMapping moduleMapping = getModuleMapping(request);
        String applicationId = getApplicationId(request, context);
        
        if (moduleMapping != null) {
            
            processMapping(context, request, response, moduleMapping, applicationId);
            
        } else {
            logger.warn("Not possible to figure out module name from servlet path " + request.getRequestURI());
        }
    }

    protected abstract void processMapping(
            ServletContext context,
            HttpServletRequest request, 
            HttpServletResponse response,
            RequestModuleMapping moduleMapping, String applicationId) throws ServletException,
            IOException;

    RequestModuleMapping getModuleMapping(HttpServletRequest request) {
        return requestModuleMapper.getModuleForRequest(request);
    }
    
    /* **************** protected methods ******************* */

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

    protected HttpServletRequest wrappedRequest(HttpServletRequest request, ServletContext servletContext, RequestModuleMapping moduleMapping, String applicationId) {
        return ModuleIntegrationUtils.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
    }
    
}
