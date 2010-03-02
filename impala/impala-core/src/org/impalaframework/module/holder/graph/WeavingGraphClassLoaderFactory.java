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

package org.impalaframework.module.holder.graph;

import java.util.List;

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.classloader.graph.DelegateClassLoader;
import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.classloader.graph.WeavableGraphClassLoader;
import org.impalaframework.module.ModuleDefinition;

/**
 * Extension {@link GraphClassLoaderFactory} which returns instance of {@link WeavableGraphClassLoader} rather than {@link GraphClassLoader}.
 * @author Phil Zoio
 */
public class WeavingGraphClassLoaderFactory extends GraphClassLoaderFactory {
   
    @Override
    protected GraphClassLoader newGraphClassLoader(
            ModuleDefinition moduleDefinition, 
            ClassRetriever resourceLoader,
            List<GraphClassLoader> classLoaders, 
            ClassLoader parentClassLoader) {
        return new WeavableGraphClassLoader(parentClassLoader, new DelegateClassLoader(classLoaders), resourceLoader, moduleDefinition, isParentClassLoaderFirst());
    }
}
