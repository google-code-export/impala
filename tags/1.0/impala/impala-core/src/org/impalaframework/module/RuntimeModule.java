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

package org.impalaframework.module;


/**
 * Encapsulates a module which exists at runtime, as opposed to simply a
 * {@link org.impalaframework.module.ModuleDefinition} instance. For
 * example a {@link org.impalaframework.spring.module.SpringRuntimeModule} will
 * be backed by a
 * {@link org.springframework.context.ConfigurableApplicationContext} instance.
 * 
 * @author Phil Zoio
 */
public interface RuntimeModule {

    /**
     * Returns the {@link ClassLoader} associated with the {@link RuntimeModule} instance.
     */
    public ClassLoader getClassLoader();
    
    /**
     * Returns the {@link ModuleDefinition} which contains the metadata for the module.
     */
    public ModuleDefinition getModuleDefinition();
    
    /**
     * Returns a bean created or managed within the scope of this module runtime. In the case of 
     * Spring, delegates to {@link org.springframework.context.ApplicationContext#getBean}
     */
    public Object getBean(String beanName);

}
