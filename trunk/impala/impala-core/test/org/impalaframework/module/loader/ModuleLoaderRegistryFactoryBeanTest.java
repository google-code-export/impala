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

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.BeansetApplicationModuleLoader;
import org.impalaframework.module.loader.ManualReloadingRootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.loader.ModuleLoaderRegistryFactoryBean;
import org.impalaframework.module.loader.SystemRootModuleLoader;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class ModuleLoaderRegistryFactoryBeanTest extends TestCase {

	private StandaloneModuleLocationResolver resolver;

	private ModuleLoaderRegistryFactoryBean factoryBean;

	public void setUp() {
		resolver = new StandaloneModuleLocationResolver();
		factoryBean = new ModuleLoaderRegistryFactoryBean();
	}

	public final void testMethods() throws Exception {
		assertEquals(true, factoryBean.isSingleton());
		assertEquals(ModuleLoaderRegistry.class, factoryBean.getObjectType());
	}
	public final void testNoResolver() throws Exception {
		try {
			factoryBean.afterPropertiesSet();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("moduleLocationResolver cannot be null", e.getMessage());
		}
	}

	public final void testAfterPropertiesSet() throws Exception {
		factoryBean.setClassLocationResolver(resolver);
		factoryBean.afterPropertiesSet();
		
		ModuleLoaderRegistry registry = (ModuleLoaderRegistry) factoryBean.getObject();
		assertEquals(SystemRootModuleLoader.class, registry.getModuleLoader(ModuleTypes.ROOT).getClass());
		assertEquals(ApplicationModuleLoader.class, registry.getModuleLoader(ModuleTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationModuleLoader.class, registry.getModuleLoader(ModuleTypes.APPLICATION_WITH_BEANSETS).getClass());
	}
	
	public final void testWithReloadableParent() throws Exception {
		factoryBean.setClassLocationResolver(resolver);
		factoryBean.setReloadableParent(true);
		factoryBean.afterPropertiesSet();
		
		ModuleLoaderRegistry registry = (ModuleLoaderRegistry) factoryBean.getObject();
		assertEquals(ManualReloadingRootModuleLoader.class, registry.getModuleLoader(ModuleTypes.ROOT).getClass());
		assertEquals(ApplicationModuleLoader.class, registry.getModuleLoader(ModuleTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationModuleLoader.class, registry.getModuleLoader(ModuleTypes.APPLICATION_WITH_BEANSETS).getClass());
	}

}
