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

package org.impalaframework.classloader;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.ModuleStateHolder;

/**
 * Interface representing a factory for {@link ClassLoader}s.
 * 
 * @author Phil Zoio
 */
public interface ClassLoaderFactory {
    
    /**
     * Method for creating a new {@link ClassLoader}
     * @param application the current {@link Application} instance. This allow access to the {@link ClassLoaderRegistry}
     * the {@link ModuleStateHolder}, which is required for some {@link ClassLoaderFactory} implementations.
     * @param parent the parent {@link ClassLoader} instance
     * @param moduleDefinition the {@link ModuleDefinition} for the module for which the classloader is being created.
     * @return a new {@link ClassLoader} instance.
     */
    public ClassLoader newClassLoader(Application application, ClassLoader parent, ModuleDefinition moduleDefinition);
}
