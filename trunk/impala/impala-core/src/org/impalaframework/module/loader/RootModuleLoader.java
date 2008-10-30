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

package org.impalaframework.module.loader;


import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinitionUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class RootModuleLoader extends BaseModuleLoader {

	private ModuleLocationResolver moduleLocationResolver;

	public RootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super();
		Assert.notNull("moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		List<String> projectNameList = RootModuleDefinitionUtils.getProjectNameList(moduleDefinition);
		
		Assert.notEmpty(projectNameList, "Root project name list is empty. For example, you may not have set up the root-project-name element in your module definition XML correctly");
		
		Resource[] rootClassLoader = ModuleUtils.getRootClassLocations(moduleLocationResolver, projectNameList);

		//FIXME can we make class loader factory more generic
		return getClassLoaderFactory().newClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(rootClassLoader));
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<String> projectNameList = RootModuleDefinitionUtils.getProjectNameList(moduleDefinition);
		return ModuleUtils.getRootClassLocations(moduleLocationResolver, projectNameList);
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		// FIXME: issue 24 return tweaked version of BeanDefinitionReader which
		// will not add bean definitions if the applicationContext is not active
		return super.newBeanDefinitionReader(context, definition);
	}

}
