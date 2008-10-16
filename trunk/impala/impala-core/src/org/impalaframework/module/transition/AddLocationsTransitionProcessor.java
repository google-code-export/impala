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

package org.impalaframework.module.transition;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionProcessor;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public class AddLocationsTransitionProcessor implements TransitionProcessor {

	private ModuleLoaderRegistry moduleLoaderRegistry;

	public AddLocationsTransitionProcessor(ModuleLoaderRegistry moduleLoaderRegistry) {
		super();
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition newRootDefinition,
			ModuleDefinition moduleDefinition) {

		ModuleLoader moduleLoader = moduleLoaderRegistry.getModuleLoader(newRootDefinition.getType());
		ConfigurableApplicationContext parentContext = moduleStateHolder.getRootModuleContext();

		ClassLoader classLoader = parentContext.getClassLoader();

		RootModuleDefinition existingModuleDefinition = moduleStateHolder.getRootModuleDefinition();
		Resource[] existingResources = moduleLoader.getSpringConfigResources(existingModuleDefinition, classLoader);
		Resource[] newResources = moduleLoader.getSpringConfigResources(newRootDefinition, classLoader);

		// compare difference
		List<Resource> existingResourceList = newResourceList(existingResources);
		List<Resource> newResourceList = newResourceList(newResources);
		List<Resource> toAddList = new ArrayList<Resource>();

		for (Resource resource : newResourceList) {
			if (!existingResourceList.contains(resource)) {
				toAddList.add(resource);
			}
		}

		BeanDefinitionReader beanDefinitionReader = moduleLoader.newBeanDefinitionReader(parentContext,
				newRootDefinition);
		beanDefinitionReader.loadBeanDefinitions(toAddList.toArray(new Resource[toAddList.size()]));

		return true;
	}

	private List<Resource> newResourceList(Resource[] array) {
		List<Resource> list = new ArrayList<Resource>();
		for (Resource resource : array) {
			list.add(resource);
		}
		return list;
	}

}
