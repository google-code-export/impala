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

package org.impalaframework.osgi.test;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
import org.impalaframework.util.serialize.SerializationStreamFactory;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

/**
 * Implementation of {@link ModuleDefinitionSource} which is designed to run
 * within an OSGi container. Supports an {@link #inject(Object)} method which is
 * designed to be called from <i>outside</i> an OSGi container, through which a
 * {@link RootModuleDefinition} can be supplied. It will then use Java
 * Serialization to marshal the {@link RootModuleDefinition} such that it is
 * usable within OSGi.
 * 
 * @author Phil Zoio
 */
public class InjectableModuleDefinitionSource implements ModuleDefinitionSource {

    private static Log logger = LogFactory.getLog(InjectableModuleDefinitionSource.class);  
    
    private RootModuleDefinition rootModuleDefinition;

    private BundleContext bundleContext;

    public InjectableModuleDefinitionSource(BundleContext bundleContext) {
        super();
        this.bundleContext = bundleContext;
    }

    public void inject(Object o) {
        
        if (o == null) {
    
            return;
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Capturing root module definition from " + o);
        }
        
        if (!(o instanceof Serializable)) {
            throw new InvalidStateException("Attempting to inject non-serializable module definition class '" + o.getClass().getName() + "'");
        }

        final Object clone = clone(o);
        try {           
            rootModuleDefinition = ObjectUtils.cast(clone, RootModuleDefinition.class);
        } catch (ClassCastException e) {
            logger.error("Error converting to root module definition object " + o + " with class loader " + o.getClass().getClassLoader());
            throw e;
        }
        
        if (logger.isInfoEnabled()) {
            logger.info("Captured root module definition " + rootModuleDefinition);
        }
    }

    Object clone(Object o) {
        final BundleDelegatingClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
        final SerializationStreamFactory streamFactory = newStreamFactory(classLoader);
        SerializationHelper helper = new SerializationHelper(streamFactory);
        final Object clone = helper.clone((Serializable) o);
        return clone;
    }

    SerializationStreamFactory newStreamFactory(ClassLoader classLoader) {
        return new ClassLoaderAwareSerializationStreamFactory(classLoader);
    }

    public RootModuleDefinition getModuleDefinition() {
        return rootModuleDefinition;
    }

}
