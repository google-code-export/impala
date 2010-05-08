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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.servlet.wrapper.HttpRequestWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

public class ModuleIntegrationUtils {

    public static HttpServletRequest getWrappedRequest(
            HttpServletRequest request,
            ServletContext servletContext, 
            RequestModuleMapping moduleMapping, 
            String applicationId) {
        
        final ModuleManagementFacade moduleManagementFactory = WebServletUtils.getModuleManagementFacade(servletContext);
        HttpServletRequest wrappedRequest = null;
        
        if (moduleManagementFactory != null) {
            HttpRequestWrapper factory = ObjectUtils.cast(moduleManagementFactory.getBean(WebConstants.REQUEST_WRAPPER_FACTORY_BEAN_NAME), HttpRequestWrapper.class);
            if (factory != null) {
                wrappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
            } else {
                wrappedRequest = request;
            }
        } else {
            wrappedRequest = request;
        }
        return wrappedRequest;
    }

}
