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

package org.impalaframework.module.runtime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.ModuleRuntime;
import org.impalaframework.module.spi.ModuleRuntimeMonitor;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Base implementation of {@link ModuleRuntime} interface. Does not contain
 * support for loading or unloading of modules of a particular runtime type
 * (e.g. Spring). However, does implements some generic {@link ModuleRuntime}
 * methods.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleRuntime implements ModuleRuntime {

	private static Log logger = LogFactory.getLog(BaseModuleRuntime.class);
	
	private ModuleStateHolder moduleStateHolder;
	
	private ModuleRuntimeMonitor moduleRuntimeMonitor;
	
	private ClassLoaderRegistry classLoaderRegistry;
	
	/* ********************* ModuleRuntime method implementation ********************* */

	public final RuntimeModule loadRuntimeModule(ModuleDefinition definition) {
		
		try {
			final RuntimeModule runtimeModule = doLoadModule(definition);
			Assert.notNull(classLoaderRegistry);
			
			final String moduleName = definition.getName();
			//note that GraphClassLoaderFactory will also populate the ClassLoaderRegistry, hence, this check
			if (!classLoaderRegistry.hasClassLoaderFor(moduleName)) {
				classLoaderRegistry.addClassLoader(moduleName, runtimeModule.getClassLoader());
				
				if (logger.isDebugEnabled()) {
					logger.debug("Added new class loader " + ObjectUtils.identityToString(runtimeModule.getClassLoader()) 
							+ " to class loader registry for module: " + moduleName);
				}
			}
			
			return runtimeModule;
		} finally {
			afterModuleLoaded(definition);
		}
	}
	
	public final void closeModule(RuntimeModule runtimeModule) {
		final ModuleDefinition moduleDefinition = runtimeModule.getModuleDefinition();
		classLoaderRegistry.removeClassLoader(moduleDefinition.getName());
		doCloseModule(runtimeModule);
	}

	protected abstract void doCloseModule(RuntimeModule runtimeModule);

	protected abstract RuntimeModule doLoadModule(ModuleDefinition definition);

	/**
	 * Provides support for reloading, as long as the a {@link ModuleChangeMonitor} is wired in, a
	 * {@link ModuleLoaderRegistry}
	 */
	protected void afterModuleLoaded(ModuleDefinition definition) {
		if (moduleRuntimeMonitor != null) {
			moduleRuntimeMonitor.setupMonitoring(definition);
		}
	}

	public RuntimeModule getRootRuntimeModule() {
		Assert.notNull(moduleStateHolder);
		
		final RuntimeModule runtimeModule = moduleStateHolder.getRootModule();
		return runtimeModule;
	}

	public RuntimeModule getRuntimeModule(String moduleName) {
		Assert.notNull(moduleStateHolder);
		Assert.notNull(moduleName);
		
		final RuntimeModule runtimeModule = moduleStateHolder.getModule(moduleName);
		return runtimeModule;
	}
	
	/* ********************* protected methods ********************* */
	
	protected ModuleStateHolder getModuleStateHolder() {
		return moduleStateHolder;
	}

	public ClassLoaderRegistry getClassLoaderRegistry() {
		return classLoaderRegistry;
	}
	
	/* ********************* wired in setters ********************* */

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setClassLoaderRegistry(ClassLoaderRegistry classLoaderRegistry) {
		this.classLoaderRegistry = classLoaderRegistry;
	}

	public void setModuleRuntimeMonitor(ModuleRuntimeMonitor moduleRuntimeMonitor) {
		this.moduleRuntimeMonitor = moduleRuntimeMonitor;
	}
}
