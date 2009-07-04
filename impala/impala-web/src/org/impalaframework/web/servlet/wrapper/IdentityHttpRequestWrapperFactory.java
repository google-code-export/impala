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

package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.integration.RequestModuleMapping;


/**
 * Implementation of {@link HttpRequestWrapperFactory} which simply returns the passed {@link HttpServletRequest}
 * 
 * @author Phil Zoio
 */
public class IdentityHttpRequestWrapperFactory implements
        HttpRequestWrapperFactory {
    
    /**
     * Simply returns <code>request</code> passed in.
     */
    public HttpServletRequest getWrappedRequest(HttpServletRequest request, ServletContext servletContext, RequestModuleMapping moduleMapping) {
        return request;
    }
}
