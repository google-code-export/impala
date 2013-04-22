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

package org.impalaframework.web.spring.integration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.integration.RequestModuleMapper;

/**
 * Extension of {@link org.impalaframework.web.integration.ModuleProxyServlet} which uses {@link RequestModuleMapper} retrieved from 
 * Spring application context to perform request to module mapping.
 * 
 * @author Phil Zoio
 */
public class ModuleProxyServlet extends org.impalaframework.web.integration.ModuleProxyServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected RequestModuleMapper newRequestModuleMapper(ServletConfig config) {
        final ServletContext servletContext = config.getServletContext();

        ModuleManagementFacade facade = WebServletUtils.getModuleManagementFacade(servletContext);
        if (facade.containsBean("requestModuleMapper")) {
            return ObjectUtils.cast(facade.getBean("requestModuleMapper"), RequestModuleMapper.class);
        }
        return super.newRequestModuleMapper(config);
    }
    
}
