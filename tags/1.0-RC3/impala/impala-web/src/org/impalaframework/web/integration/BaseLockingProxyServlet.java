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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.impalaframework.web.utils.WebPathUtils;

/**
 * Base {@link HttpServlet} which provides locking support for subclasses
 * through a final implementation of
 * {@link #service(HttpServletRequest, HttpServletResponse)} which wraps call to
 * {@link #doService(HttpServletRequest, HttpServletResponse, ServletContext)}
 * with a read lock obtained from {@link FrameworkLockHolder}
 * 
 * @author Phil Zoio
 */
public abstract class BaseLockingProxyServlet extends HttpServlet {

    private static final Log logger = LogFactory.getLog(BaseLockingProxyServlet.class);  
    
    private static final long serialVersionUID = 1L;

    private FrameworkLockHolder frameworkLockHolder;
    
    public BaseLockingProxyServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ModuleManagementFacade moduleManagementFacade = ImpalaServletUtils.getModuleManagementFacade(config.getServletContext());
        this.frameworkLockHolder = moduleManagementFacade.getFrameworkLockHolder();
    }

    @Override
    public final void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
        
            this.frameworkLockHolder.readLock();
            WebPathUtils.maybeLogRequest(request, logger);
            
            ServletContext context = getServletContext();
            doService(request, response, context);
        
        } finally {
            this.frameworkLockHolder.readUnlock();
        }
        
    }

    protected abstract void doService(HttpServletRequest request, HttpServletResponse response,
            ServletContext context)
            throws ServletException, IOException;
    
}
