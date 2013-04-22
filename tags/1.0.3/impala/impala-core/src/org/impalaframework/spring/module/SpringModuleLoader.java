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

package org.impalaframework.spring.module;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.module.spi.TypeReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

/**
 * <p>
 * <code>ModuleLoader</code> is used to encapsulate differences in the load
 * strategies required for different types of modules, in the same way as
 * ifferent types of modules need to have their metadata read differently by
 * different {@link TypeReader} implementations. Differences in the module load
 * strategy cover a number of aspects.
 * <ul>
 * <li>provision of a class loader for the module.
 * <li>what kind of Spring {@link ApplicationContext} is constructed. For example, web
 * module types need to be backed by instances of 
 * {@link org.springframework.web.context.WebApplicationContext}
 * <li>what {@link BeanDefinitionReader} is used to extract the bean information.
 * </ul>
 * <code>ModuleLoader</code> encapsulates differences that exist between
 * module types in these respects.
 * <p>
 * However, module types backed by a <code>ModuleLoader</code> will observe
 * some restrictions on how they are constructed and initialized, with the
 * benefit of reducing the responsibilities of module loader implementations.
 * However, if these restrictions are inappropriate or cannot be observed, it is
 * possible to allow a module to be backed by a  {@link DelegatingContextLoader}
 * 
 * @see DelegatingContextLoader
 * @author Phil Zoio
 */
public interface SpringModuleLoader extends ModuleLoader {
    
    /**
     * Return an array of {@link Resource} instances which represent the Spring config locations for the module
     */
    Resource[] getSpringConfigResources(String applicationId, ModuleDefinition moduleDefinition, ClassLoader classLoader);
    
    /**
     * Returns a new {@link ConfigurableApplicationContext} instance which contains the module's declarative services
     */
    ConfigurableApplicationContext newApplicationContext(Application application, ApplicationContext parent, ModuleDefinition moduleDefinition, ClassLoader classLoader);
    
    /**
     * Returns a new {@link BeanDefinitionReader} which may be used to read the module definitions. If this 
     * method returns null, it is assumed that the {@link ConfigurableApplicationContext} instance already contains
     * its own {@link BeanDefinitionReader}.
     */
    BeanDefinitionReader newBeanDefinitionReader(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition);
    
    /**
     * A callback which will typically, although not always, be used to invoke the {@link ConfigurableApplicationContext#refresh()} method.
     */
    void handleRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition);
    
    /**
     * Callback which can be used for any post-refresh operations
     */
    void afterRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition);
    
    /**
     * Callback which is called before application context is closed
     */
    void beforeClose(String applicationId, ApplicationContext applicationContext, ModuleDefinition moduleDefinition);
    
}
