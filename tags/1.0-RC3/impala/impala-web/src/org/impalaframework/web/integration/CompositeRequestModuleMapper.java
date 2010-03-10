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

import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.springframework.util.Assert;

/**
 * Implementation of {@link RequestModuleMapper} which delegates to a list of wired in
 * {@link RequestModuleMapper} instances.
 * 
 * @author Phil Zoio
 */
public class CompositeRequestModuleMapper implements RequestModuleMapper {
    
    private List<RequestModuleMapper> requestModuleMappers;

    public void init(ServletConfig servletConfig) {
    }

    public void init(FilterConfig filterConfig) {
    }
    
    public RequestModuleMapping getModuleForRequest(HttpServletRequest request) {
        Assert.notNull(requestModuleMappers, "requestModuleMappers cannot be null");
        Assert.notEmpty(requestModuleMappers, "requestModuleMappers cannot be empty");
        for (RequestModuleMapper requestModuleMapper : requestModuleMappers) {
            final RequestModuleMapping module = requestModuleMapper.getModuleForRequest(request);
            if (module != null) return module;
        }
        return null;
    }

    public void setRequestModuleMappers(List<RequestModuleMapper> requestModuleMappers) {
        this.requestModuleMappers = requestModuleMappers;
    }

}
