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

package org.impalaframework.module.spring.loader;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryPostProcessor;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class DefaultApplicationContextLoader extends BaseApplicationContextLoader {
	
	private static Log logger = LogFactory.getLog(DefaultApplicationContextLoader.class);
	
	//FIXME ticket 117 move this to ModuleRuntime
	private ModuleChangeMonitor moduleChangeMonitor;

	private ServiceRegistry serviceRegistry;

	public DefaultApplicationContextLoader() {
	}

	protected void afterContextLoaded(ModuleDefinition definition,
			final ModuleLoader moduleLoader) {
		Resource[] toMonitor = moduleLoader.getClassLocations(definition);
		if (moduleChangeMonitor != null) {
			if (logger.isDebugEnabled()) logger.debug("Monitoring resources " + Arrays.toString(toMonitor) + " using ModuleChangeMonitor " + moduleChangeMonitor);
			moduleChangeMonitor.setResourcesToMonitor(definition.getName(), toMonitor);
		}
	}
	
	protected void addBeanPostProcessors(ModuleDefinition definition, ConfigurableListableBeanFactory beanFactory) {
		beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry));
		beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(definition));
	}
	

	public void setModuleChangeMonitor(ModuleChangeMonitor moduleChangeMonitor) {
		this.moduleChangeMonitor = moduleChangeMonitor;
	}
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
