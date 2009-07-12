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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilter;

/**
 * <p><code>Filter</code> which performs a similar function to <code>ModuleProxyServlet</code>
 * </p>
 * 
 * @see InternalFrameworkIntegrationFilter
 * @see ModuleProxyServlet
 * @author Phil Zoio
 */
public class ModuleProxyFilter extends BaseModuleProxyFilter {

    private static final Log logger = LogFactory.getLog(ModuleProxyFilter.class);   
    
    protected void processMapping(ServletContext context,
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, 
            RequestModuleMapping moduleMapping)
            throws IOException, ServletException {
        
        Filter moduleFilter = WebServletUtils.getModuleFilter(context, moduleMapping.getModuleName());
        if (moduleFilter != null) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Found module filter [" + moduleFilter + "] for module name [" + moduleMapping + "]");
            }
            
            //explicitly go through service method
            HttpServletRequest wrappedRequest = wrappedRequest(request, context, moduleMapping);
            
            InvocationAwareFilterChain substituteChain = new InvocationAwareFilterChain();
            
            moduleFilter.doFilter(wrappedRequest, response, substituteChain);
            
            if (substituteChain.getWasInvoked()) {
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Filter [" + moduleFilter + "] did not process request. Chaining request.");
                }
                chain.doFilter(request, response);
                
            } else {
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Filter [" + moduleFilter + "] completed processing of request.");
                }
            }
            
        } else {
            
            if (logger.isDebugEnabled()) {
                logger.debug("No module filter found for candidate module name [" + moduleMapping + "]. Chaining request.");
            }
            chain.doFilter(request, response);
            
        }
    }
}
