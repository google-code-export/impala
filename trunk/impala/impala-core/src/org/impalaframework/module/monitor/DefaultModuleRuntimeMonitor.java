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

package org.impalaframework.module.monitor;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.runtime.BaseModuleRuntime;
import org.impalaframework.module.runtime.ModuleRuntimeUtils;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.module.spi.ModuleRuntimeMonitor;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Implements a strategy for passing monitorable resources to {@link ModuleChangeMonitor}
 * following the loading of a module
 * 
 * @author Phil Zoio
 */
public class DefaultModuleRuntimeMonitor implements ModuleRuntimeMonitor {

	private static Log logger = LogFactory.getLog(BaseModuleRuntime.class);
	
	private ModuleLoaderRegistry moduleLoaderRegistry;
	
	private ModuleChangeMonitor moduleChangeMonitor;
	
	public void setupMonitoring(ModuleDefinition definition) {
		if (moduleChangeMonitor != null) {
			
			Assert.notNull(moduleLoaderRegistry, "ModuleChangeMonitor required if ModuleLoaderRegistry is wired in.");
			final ModuleLoader loader = moduleLoaderRegistry.getModuleLoader(ModuleRuntimeUtils.getModuleLoaderKey(definition), false);
			
			if (loader != null) {
				Resource[] toMonitor = loader.getClassLocations(definition);
				
				//FIXME Issue 171 - If staging directory, then you want to use this to detect changes, rather than the class locations
				//FIXME Also, will probably need to move this to and expose through separate interface
			
				if (logger.isDebugEnabled()) logger.debug("Monitoring resources " + Arrays.toString(toMonitor) + " using ModuleChangeMonitor " + moduleChangeMonitor);
				moduleChangeMonitor.setResourcesToMonitor(definition.getName(), toMonitor);
			}
		}
	}

	public void setModuleLoaderRegistry(ModuleLoaderRegistry moduleLoaderRegistry) {
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public void setModuleChangeMonitor(ModuleChangeMonitor moduleChangeMonitor) {
		this.moduleChangeMonitor = moduleChangeMonitor;
	}
	
}
