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

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

public class InjectableModuleDefinitionSource implements ModuleDefinitionSource {

	private RootModuleDefinition source;

	private BundleContext bundleContext;

	public InjectableModuleDefinitionSource(BundleContext bundleContext) {
		super();
		this.bundleContext = bundleContext;
	}

	public void inject(Object o) {
		//FIXME check is serializable
		
		//FIXME add error handling
		
		final BundleDelegatingClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
		SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(classLoader));
		final Object clone = helper.clone((Serializable) o);
		
		source = ObjectUtils.cast(clone, RootModuleDefinition.class);
	}

	public RootModuleDefinition getModuleDefinition() {
		return source;
	}

}
