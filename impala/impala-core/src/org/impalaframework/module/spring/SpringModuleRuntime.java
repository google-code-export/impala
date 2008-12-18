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

package org.impalaframework.module.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleRuntime;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ModuleRuntime} which uses Spring-specific {@link ApplicationContextLoader} to 
 * return a {@link SpringRuntimeModule} in the {@link #loadRuntimeModule(ModuleDefinition)} method.
 * 
 * @author Phil Zoio
 */
public class SpringModuleRuntime implements ModuleRuntime {

	private static Log logger = LogFactory.getLog(SpringModuleRuntime.class);
	
	private ApplicationContextLoader applicationContextLoader;
	
	private ModuleStateHolder moduleStateHolder;

	public RuntimeModule loadRuntimeModule(ModuleDefinition definition) {
		Assert.notNull(definition);
		Assert.notNull(applicationContextLoader);
		
		ConfigurableApplicationContext parentContext = null;
		ModuleDefinition parentDefinition = definition.getParentDefinition();
		if (parentDefinition != null) {
			parentContext = SpringModuleUtils.getModuleSpringContext(moduleStateHolder, parentDefinition.getName());
		}
		
		if (logger.isDebugEnabled()) logger.debug("Loading runtime module for module definition " + definition);
		
		if (logger.isTraceEnabled()) {
			logger.trace("Parent application context: " + parentContext);
		}
		
		ConfigurableApplicationContext context = applicationContextLoader.loadContext(definition, parentContext);
		
		if (logger.isTraceEnabled()) {
			logger.trace("New application context: " + parentContext);
		}
		
		return new DefaultSpringRuntimeModule(definition, context);
	}

	public void setApplicationContextLoader(ApplicationContextLoader applicationContextLoader) {
		this.applicationContextLoader = applicationContextLoader;
	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

}
