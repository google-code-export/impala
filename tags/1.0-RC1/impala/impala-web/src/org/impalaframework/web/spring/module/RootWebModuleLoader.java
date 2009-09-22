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

package org.impalaframework.web.spring.module;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.spring.module.loader.DefaultApplicationContextLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * Implementation of {@link ModuleLoader} which in addition to the tasks
 * performed by the superclass {@link WebModuleLoader}, also provides an
 * implementation of
 * {@link #afterRefresh(ConfigurableApplicationContext, ModuleDefinition)}, in
 * which it binds the provided {@link ConfigurableApplicationContext} to the
 * {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE} servlet
 * context attribute. In other words, the application context provided is the
 * one which is exposed by default to other web frameworks via the web
 * application context root attribute.
 * 
 * @author Phil Zoio
 */
public class RootWebModuleLoader extends BaseWebModuleLoader {

    private static Log logger = LogFactory.getLog(DefaultApplicationContextLoader.class);

    @Override
    public void afterRefresh(ConfigurableApplicationContext context, ModuleDefinition definition) {
        
        Object existingRoot = getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (existingRoot != null) {
            logger.info("Republishing root web application context using key: " + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        }
    }
    
}
