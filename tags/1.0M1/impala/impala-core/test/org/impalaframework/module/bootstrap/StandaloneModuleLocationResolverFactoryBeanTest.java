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

package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.StandaloneModuleLocationResolverFactoryBean;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class StandaloneModuleLocationResolverFactoryBeanTest extends TestCase {

	public final void testFactoryBean() throws Exception {
		StandaloneModuleLocationResolverFactoryBean factoryBean = new StandaloneModuleLocationResolverFactoryBean();
		assertEquals(ModuleLocationResolver.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		assertTrue(factoryBean.getObject() instanceof StandaloneModuleLocationResolver);
	}

}
