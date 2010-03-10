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

package org.impalaframework.osgi.classloader;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.osgi.util.OsgiUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

/**
 * Implementation of {@link ClassLoaderFactory} which returns a class loader backed by a
 * OSGi {@link Bundle}.
 * @author Phil Zoio
 */
public class OsgiClassLoaderFactory implements ClassLoaderFactory, BundleContextAware {
    
    private BundleContext bundleContext;

    public ClassLoader newClassLoader(Application application, ClassLoader parent, ModuleDefinition moduleDefinition) {
        
        Bundle bundle = findAndCheckBundle(moduleDefinition);
        return BundleDelegatingClassLoader.createBundleClassLoaderFor(bundle);
    }
    
    Bundle findBundle(ModuleDefinition moduleDefinition) {
        Bundle bundle = OsgiUtils.findBundle(bundleContext, moduleDefinition.getName());
        return bundle;
    }

    private Bundle findAndCheckBundle(ModuleDefinition moduleDefinition) {
        Bundle bundle = findBundle(moduleDefinition);
        OsgiUtils.checkBundle(moduleDefinition, bundle);
        return bundle;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

}
