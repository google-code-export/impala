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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Sets the context of the root module to that bound to {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE}
 * @author Phil Zoio
 */
public class RootWebModuleLoader extends WebModuleLoader {

    @Override
    protected void configureBeanFactoryAndApplicationContext(
            ModuleDefinition moduleDefinition,
            DefaultListableBeanFactory beanFactory,
            GenericWebApplicationContext context) {
        super.configureBeanFactoryAndApplicationContext(moduleDefinition, beanFactory, context);
        
        if (context.getParent() == null) {
            final Object attribute = getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            final WebApplicationContext wac = ObjectUtils.cast(attribute, WebApplicationContext.class);
            if (wac != null) {
                context.setParent(wac);
            }
        }
    }
    
}
