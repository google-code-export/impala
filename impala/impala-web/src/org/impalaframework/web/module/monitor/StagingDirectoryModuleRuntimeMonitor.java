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

package org.impalaframework.web.module.monitor;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.runtime.BaseModuleRuntime;
import org.impalaframework.module.spi.ModuleRuntimeMonitor;
import org.springframework.core.io.Resource;

/**
 * Implements a strategy for passing a list of monitorable resources to
 * {@link ModuleChangeMonitor} based on the assumption that module updates will be first copied to
 * a staging directory. It is this directory which is monitored for changed modules.
 * 
 * Modified modules are copied to the application directory after module loading occurs.
 * 
 * @author Phil Zoio
 */
public class StagingDirectoryModuleRuntimeMonitor implements ModuleRuntimeMonitor {

	private static Log logger = LogFactory.getLog(BaseModuleRuntime.class);
	
	private ModuleChangeMonitor moduleChangeMonitor;
	
	

	/**
	 * Nothing to do, as monitored module resources are already in correct location.
	 */
	public void beforeModuleLoads(ModuleDefinition definition) {
		//nothing to do here. Subclasses may implement strategies for copying modified resources into
		//proper location
	}
	
	/**
	 * Called after module loading takes place. Sets resources to monitor as exactly those which comprise
	 * the resources local to the module class path.
	 */
	public void afterModuleLoaded(ModuleDefinition definition) {
		if (moduleChangeMonitor != null) {

			Resource[] toMonitor = null;
		
			if (logger.isDebugEnabled()) {
				logger.debug("Monitoring resources " + Arrays.toString(toMonitor) + " using ModuleChangeMonitor " + moduleChangeMonitor);
			}
			
			moduleChangeMonitor.setResourcesToMonitor(definition.getName(), toMonitor);
		}
	}

	public void setModuleChangeMonitor(ModuleChangeMonitor moduleChangeMonitor) {
		this.moduleChangeMonitor = moduleChangeMonitor;
	}
}
