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

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

/**
 * Defines interface for determining module name from {@link HttpServletRequest}
 * @author Phil Zoio
 */
public interface RequestModuleMapper {
    
    void init(ServletConfig servletConfig);
    
    void init(FilterConfig filterConfig);

    /**
     * Returns name of module to which particular request should be mapped,
     * as well as the path (portion of the request URL) for which the mapping applies.
     */
    RequestModuleMapping getModuleForRequest(HttpServletRequest request);
    
}
