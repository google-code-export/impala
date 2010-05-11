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

package org.impalaframework.osgi.startup;

import java.net.URL;
import java.util.List;

import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.startup.ContextStarter;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;
import org.springframework.osgi.util.BundleDelegatingClassLoader;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ContextStarter} designed to bootstrap Impala in
 * an OSGi environment. 
 */
public class OsgiContextStarter implements ContextStarter, BundleContextAware {
    
    private BundleContext bundleContext;

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * Finds bundle resources corresponding with the specified location list.
     * Unlike {@link OsgiBundleXmlApplicationContext}, finds resources not just in the host
     * bundle but in any bundle which contains the locations. Uses the host bundle's class
     * loader as the Thread's context class loader during the {@link ApplicationContext} 
     * startup cycle.
     */
    public ApplicationContext startContext(List<String> locations) {

        Assert.notNull(locations);
        
        Thread currentThread = Thread.currentThread();
        ClassLoader oldTCCL = currentThread.getContextClassLoader();

        try {
            ClassLoader classLoader = 
                BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
            currentThread.setContextClassLoader(classLoader);

            final Resource[] resources = getResourcesForLocations(locations);

            return newApplicationContext(resources);
            
        } finally {
            currentThread.setContextClassLoader(oldTCCL);
        }
    }

    private Resource[] getResourcesForLocations(List<String> locations) {
        //should cycle through bundles in reverse, so that most recently 
        //loaded bundles are encountered first. However, does
        //not guarantee that any resource is loaded from any particular bundle.
        //Instead, relies on sensible naming conventions for Impala bootstrap bundles.
        URL[] urls = OsgiUtils.findResources(bundleContext, locations.toArray(new String[locations.size()]));
        
        Resource[] resources = new Resource[urls.length];
        for (int i = 0; i < resources.length; i++) {
            resources[i] = new UrlResource(urls[i]);
        }
        
        return resources;
    }

    private ApplicationContext newApplicationContext(Resource[] resources) {
        ImpalaOsgiApplicationContext applicationContext = new ImpalaOsgiApplicationContext();
        applicationContext.setConfigResources(resources);
        applicationContext.setBundleContext(bundleContext);
        applicationContext.refresh();
        return applicationContext;
    }

}
