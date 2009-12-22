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

package org.impalaframework.module.spring.transition;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.runtime.ModuleRuntimeUtils;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionProcessor;
import org.impalaframework.spring.module.SpringModuleLoader;
import org.impalaframework.spring.module.SpringModuleUtils;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public class AddLocationsTransitionProcessor implements TransitionProcessor {

	private ModuleLoaderRegistry moduleLoaderRegistry;
	
	private ModuleStateHolder moduleStateHolder;

	public AddLocationsTransitionProcessor(ModuleLoaderRegistry moduleLoaderRegistry) {
		super();
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public void process(Application application, RootModuleDefinition newRootDefinition, ModuleDefinition moduleDefinition) {

		final ModuleLoader loader = moduleLoaderRegistry.getModuleLoader(ModuleRuntimeUtils.getModuleLoaderKey(newRootDefinition));
		final SpringModuleLoader moduleLoader = ObjectUtils.cast(loader, SpringModuleLoader.class);
		
		ConfigurableApplicationContext parentContext = SpringModuleUtils.getRootSpringContext(moduleStateHolder);
		ClassLoader classLoader = parentContext.getClassLoader();

		RootModuleDefinition existingModuleDefinition = moduleStateHolder.getRootModuleDefinition();
		String applicationId = application.getId();
		Resource[] existingResources = moduleLoader.getSpringConfigResources(applicationId, existingModuleDefinition, classLoader);
		Resource[] newResources = moduleLoader.getSpringConfigResources(applicationId, newRootDefinition, classLoader);

		// compare difference
		List<Resource> existingResourceList = newResourceList(existingResources);
		List<Resource> newResourceList = newResourceList(newResources);
		List<Resource> toAddList = new ArrayList<Resource>();

		for (Resource resource : newResourceList) {
			if (!existingResourceList.contains(resource)) {
				toAddList.add(resource);
			}
		}

		BeanDefinitionReader beanDefinitionReader = moduleLoader.newBeanDefinitionReader(applicationId,
				parentContext, newRootDefinition);
		beanDefinitionReader.loadBeanDefinitions(toAddList.toArray(new Resource[toAddList.size()]));
	}

	private List<Resource> newResourceList(Resource[] array) {
		List<Resource> list = new ArrayList<Resource>();
		for (Resource resource : array) {
			list.add(resource);
		}
		return list;
	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

}
