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

package org.impalaframework.web.servlet.wrapper.request;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.HttpRequestWrapper;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.servlet.wrapper.session.IdentityHttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.session.PartitionedHttpSessionWrapper;

/**
 * Implementation of {@link HttpRequestWrapper} which returns instance of
 * {@link PartitionedHttpSessionWrapper} if
 * {@link #enableModuleSessionProtection} or
 * {@link #enablePartitionedServletContext} is set. If not, then simply returns
 * {@link IdentityHttpSessionWrapper}.
 * 
 * @see ModuleAwareWrapperHttpServletRequest
 * @see HttpRequestWrapper
 * @author Phil Zoio
 */
public class PartitionedRequestWrapper implements HttpRequestWrapper {
    
    private boolean enableModuleSessionProtection;
    
    private boolean enablePartitionedServletContext;
    
    private WebAttributeQualifier webAttributeQualifier;
    
    public HttpServletRequest getWrappedRequest(HttpServletRequest request, ServletContext servletContext, RequestModuleMapping moduleMapping, String applicationId) {
        
        final HttpSessionWrapper sessionWrapper;
        if (enableModuleSessionProtection || enablePartitionedServletContext) {
            PartitionedHttpSessionWrapper httpSessionWrapper = new PartitionedHttpSessionWrapper();
            httpSessionWrapper.setServletContext(servletContext);
            httpSessionWrapper.setWebAttributeQualifier(webAttributeQualifier);
            httpSessionWrapper.setEnableModuleSessionProtection(enableModuleSessionProtection);
            httpSessionWrapper.setEnablePartitionedServletContext(enablePartitionedServletContext);
            sessionWrapper = httpSessionWrapper;
        } else {

            IdentityHttpSessionWrapper httpSessionWrapper = new IdentityHttpSessionWrapper();
            sessionWrapper = httpSessionWrapper;
        }
        
        final String wrappedServletContextAttributeName = webAttributeQualifier.getQualifiedAttributeName("wrapped_servlet_context", applicationId, moduleMapping.getModuleName());
        final ServletContext wrappedServletContext = (ServletContext) servletContext.getAttribute(wrappedServletContextAttributeName);
        
        final MappedHttpServletRequest mappedRequest = new MappedHttpServletRequest((wrappedServletContext != null ? wrappedServletContext : servletContext), request, sessionWrapper, moduleMapping, applicationId);
        
        String baseAttributeName = webAttributeQualifier.getQualifierPrefix(applicationId, moduleMapping.getModuleName());
        request.setAttribute(WebAttributeQualifier.MODULE_QUALIFIER_PREFIX, baseAttributeName);
        
        return mappedRequest;
    }

    public void setEnableModuleSessionProtection(boolean enableModuleSessionProtection) {
        this.enableModuleSessionProtection = enableModuleSessionProtection;
    }
    
    public void setEnablePartitionedServletContext(boolean enablePartitionedServletContext) {
        this.enablePartitionedServletContext = enablePartitionedServletContext;
    }
    
    public void setWebAttributeQualifier(WebAttributeQualifier webAttributeQualifier) {
        this.webAttributeQualifier = webAttributeQualifier;
    }
}
