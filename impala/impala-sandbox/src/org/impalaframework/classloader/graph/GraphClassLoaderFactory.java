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

package org.impalaframework.classloader.graph;

import java.io.File;
import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class GraphClassLoaderFactory implements ClassLoaderFactory {
	
	private ModuleLocationResolver moduleLocationResolver;
	
	public GraphClassLoaderFactory(ModuleLocationResolver moduleLocationResolver) {
		super();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public ClassLoader newClassLoader(ClassLoader parent, Object data) {
		ModuleDefinition moduleDefinition = ObjectUtils.cast(data, ModuleDefinition.class);
		final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		final File[] files = ResourceUtils.getFiles(classLocations);
		CustomClassLoader classLoader = new CustomClassLoader(files);
		return classLoader;
	}

}
