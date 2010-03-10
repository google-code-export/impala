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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker;
import org.impalaframework.web.servlet.invoker.HttpServiceInvoker;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

/**
 * <p><code>Filter</code> which performs a similar function to <code>ModuleProxyServlet</code>
 * </p>
 * 
 * @see org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilter
 * @see ModuleProxyServlet
 * @author Phil Zoio
 */
public class ModuleProxyFilter extends BaseModuleProxyFilter {

    private static final long serialVersionUID = 1L;
    
    private static final Log logger = LogFactory.getLog(ModuleProxyFilter.class);  

    @Override
    protected void processMapping(ServletContext context,
            HttpServletRequest request, 
            HttpServletResponse response,
            FilterChain chain, 
            RequestModuleMapping moduleMapping, String applicationId)
            throws IOException, ServletException {
        
        String attributeName = ModuleHttpServiceInvoker.class.getName()+ "."+ moduleMapping.getModuleName();
        Object attribute = context.getAttribute(attributeName);
        
        HttpServiceInvoker invoker = ObjectUtils.cast(attribute, HttpServiceInvoker.class);
        
        if (logger.isInfoEnabled()) {
            if (invoker != null) {
                logger.info("Invoker for attribute '" + attributeName + "': " + invoker);
            }
            else {
                if (logger.isDebugEnabled()) logger.debug("No invoker found for attribute '" + attributeName);
            }
        }
        
        if (invoker != null) {
            HttpServletRequest wrappedRequest = wrappedRequest(request, context, moduleMapping, applicationId);
            invoker.invoke(wrappedRequest, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }
}
