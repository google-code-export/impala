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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;

/**
 * Module-aware implementation of <code>HttpServletRequest</code>.
 * Implementation of <code>getSession()</code>, returns
 * <code>ModuleAwareWrapperHttpSession</code> instance. Otherwise, simply
 * handled by <code>HttpServletWrapper</code> superclass.
 * 
 * @author Phil Zoio
 */
public class ModuleAwareWrapperHttpServletRequest extends
        MappedWrapperHttpServletRequest {

    private final String moduleName;
    private final HttpSessionWrapper httpSessionWrapper;

    public ModuleAwareWrapperHttpServletRequest(HttpServletRequest request, 
            HttpSessionWrapper httpSessionWrapper,
            RequestModuleMapping moduleMapping) {
        
        super(request, httpSessionWrapper, moduleMapping);
        Assert.notNull(request);
        Assert.notNull(moduleMapping);
        Assert.notNull(httpSessionWrapper);
        this.moduleName = moduleMapping.getModuleName();
        this.httpSessionWrapper = httpSessionWrapper;
    }

    @Override
    public HttpSession getSession() {
        
        HttpSession session = super.getSession();
        return wrapSession(session);
    }

    @Override
    public HttpSession getSession(boolean create) {
        
        HttpSession session = super.getSession(create);
        return wrapSession(session);
    }
    
    /* ****************** Helper methods ****************** */

    HttpSession wrapSession(HttpSession session) {
        return httpSessionWrapper.wrapSession(session, moduleName);
    }   

}
