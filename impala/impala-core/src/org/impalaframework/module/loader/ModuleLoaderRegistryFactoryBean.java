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

import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ModuleLoaderRegistryFactoryBean implements FactoryBean, InitializingBean {

	private boolean reloadableParent;

	private ModuleLocationResolver moduleLocationResolver;

	private ModuleLoaderRegistry registry;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");

		registry = new ModuleLoaderRegistry();
		if (reloadableParent)
			registry.setModuleLoader(ModuleTypes.ROOT, new ManualReloadingRootModuleLoader(moduleLocationResolver));
		else
			registry.setModuleLoader(ModuleTypes.ROOT, new SystemRootModuleLoader(moduleLocationResolver));

		registry.setModuleLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(moduleLocationResolver));
		registry.setModuleLoader(ModuleTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationModuleLoader(
				moduleLocationResolver));
	}

	public Object getObject() throws Exception {
		return registry;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return ModuleLoaderRegistry.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/* ****************** injected setters **************** */

	public void setClassLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public void setReloadableParent(boolean reloadableParent) {
		this.reloadableParent = reloadableParent;
	}

}
