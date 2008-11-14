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

package org.impalaframework.osgi.test;

import java.io.Serializable;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
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

	private RootModuleDefinition source;

	private BundleContext bundleContext;

	public InjectableModuleDefinitionSource(BundleContext bundleContext) {
		super();
		this.bundleContext = bundleContext;
	}

	public void inject(Object o) {
		
		if (o == null) {
			//TODO log
			return;
		}
		
		if (!(o instanceof Serializable)) {
			throw new InvalidStateException("Attempting to inject non-serializable module definition class '" + o.getClass().getName() + "'");
		}

		final Object clone = clone(o);
		try {
			source = ObjectUtils.cast(clone, RootModuleDefinition.class);
			//TODO log capturing root module definition
			
		} catch (ClassCastException e) {
			//TODO log class cast exception
			throw e;
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
		return source;
	}

}
