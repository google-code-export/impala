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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.impalaframework.util.InstantiationUtils;
import org.impalaframework.util.ObjectUtils;
import org.springframework.util.ClassUtils;

/**
 * Class with static methods shared by <code>ModuleProxyServlet</code> and <code>ModuleProxyFilter</code>.
 * @author Phil Zoio
 */
public class ModuleProxyUtils {

    public static void maybeLogRequest(HttpServletRequest request, Log logger) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Request context path: " + request.getContextPath());
            logger.debug("Request local address: " + request.getLocalAddr());
            logger.debug("Request local name: " + request.getLocalName());
            logger.debug("Request path info: " + request.getPathInfo());
            logger.debug("Request path translated: " + request.getPathTranslated());
            logger.debug("Request query string: " + request.getQueryString());
            logger.debug("Request servlet path: " + request.getServletPath());
            logger.debug("Request request URI: " + request.getRequestURI());
            logger.debug("Request request URL: " + request.getRequestURL());
            logger.debug("Request session ID: " + request.getRequestedSessionId());
        }
    }
    
    public static RequestModuleMapper newRequestModuleMapper(final String requestModuleMapperClass) {
        if (requestModuleMapperClass != null) {
            //remember, ClassUtils will use the thread context class loader if it is available
            final Object object = InstantiationUtils.instantiate(requestModuleMapperClass, ClassUtils.getDefaultClassLoader());
            return ObjectUtils.cast(object, RequestModuleMapper.class);
        } else {
            return new ServletPathRequestModuleMapper();
        }
    }

    public static String getTopLevelPathSegment(String uri) {
        //request URI includes context path, so that should be stripped off
        String toCheck = uri.startsWith("/") ? uri.substring(1) : uri;
        
        //we are dealing with the top level path
        String[] split = toCheck.split("/");
        
        if (split.length == 1) {
            return null;
        }
        return split[1];
    }
}
