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

package org.impalaframework.spring.module;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleRuntime;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.runtime.BaseModuleRuntime;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ModuleRuntime} which uses Spring-specific {@link ApplicationContextLoader} to 
 * return a {@link SpringRuntimeModule} in the {@link #loadRuntimeModule(ModuleDefinition)} method.
 * 
 * @author Phil Zoio
 */
public class SpringModuleRuntime extends BaseModuleRuntime implements ModuleRuntime {

	private static Log logger = LogFactory.getLog(SpringModuleRuntime.class);
	
	private String RUNTIME_NAME = "spring";
	
	private ApplicationContextLoader applicationContextLoader;
	
	/* ********************* ModuleRuntime method implementation ********************* */

	public String getRuntimeName() {
		return RUNTIME_NAME;
	}
	
	@Override
	protected RuntimeModule doLoadModule(ModuleDefinition definition) {

		Assert.notNull(definition);
		Assert.notNull(applicationContextLoader);
		
		ApplicationContext parentContext = getParentApplicationContext(definition);
		
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

	public void doCloseModule(RuntimeModule runtimeModule) {
		
		SpringRuntimeModule springRuntimeModule = ObjectUtils.cast(runtimeModule, SpringRuntimeModule.class);
		final ConfigurableApplicationContext applicationContext = springRuntimeModule.getApplicationContext();
		
		applicationContext.close();
	}

	/* ********************* protected methods ********************* */

	/**
	 * Retrieves {@link ApplicationContext} associated with module definition's parent defintion, if this is not null.
	 * If module definition has no parent, then returns null.
	 */
	protected ApplicationContext getParentApplicationContext(ModuleDefinition definition) {
		return internalGetParentApplicationContext(definition);
	}

	protected ApplicationContext internalGetParentApplicationContext(
			ModuleDefinition definition) {
		ConfigurableApplicationContext parentContext = null;
		ModuleDefinition parentDefinition = definition.getParentDefinition();
		
		while (parentDefinition != null) {
			
			final String parentName = parentDefinition.getName();
			final RuntimeModule parentModule = getModuleStateHolder().getModule(parentName);
			if (parentModule instanceof SpringRuntimeModule) {
				SpringRuntimeModule springRuntimeModule = (SpringRuntimeModule) parentModule;
				parentContext = springRuntimeModule.getApplicationContext();
				break;
			}
			
			parentDefinition = parentDefinition.getParentDefinition();			
		}
		return parentContext;
	}
	
	/* ********************* wired in setters ********************* */

	public void setApplicationContextLoader(ApplicationContextLoader applicationContextLoader) {
		this.applicationContextLoader = applicationContextLoader;
	}
}
