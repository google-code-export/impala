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

import java.io.File;
import java.net.URL;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Base implementation of {@link ClassLoaderFactory} which uses wired in
 * {@link ModuleLocationResolver} to retrieve a list or resource locations, then
 * passing these to the abstract {@link #newClassLoader(ClassLoader, File[])}
 * method.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleClassLoaderFactory implements ClassLoaderFactory {
    
    private ModuleLocationResolver moduleLocationResolver;

    public abstract ClassLoader newClassLoader(ClassLoader parent, File[] files);

    public abstract ClassLoader newClassLoader(ClassLoader parent, URL[] urls);

    public ClassLoader newClassLoader(Application application, ClassLoader parent, ModuleDefinition moduleDefinition) {
        Assert.notNull(moduleLocationResolver);
        Assert.notNull(moduleDefinition);
        
        List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
        return newClassLoader(parent, ResourceUtils.getFiles(locations));
    }

    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }
}
