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

package org.impalaframework.module.type;

import java.util.Map;
import java.util.Set;

import org.impalaframework.module.TypeReader;

import junit.framework.TestCase;

public class TypeReaderRegistryFactoryBeanTest extends TestCase {

	private TypeReaderRegistryFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = newFactoryBean();
	}
	
	@SuppressWarnings("unchecked")
	public void testAfterPropertiesSet() throws Exception {
		factoryBean.afterPropertiesSet();
		
		Map<String, TypeReader> factoryTypeReaders = (Map<String, TypeReader>) factoryBean.getObject();
		
		Map<String, TypeReader> typeReaders = getTypeReaders();
		assertEquals(factoryTypeReaders.size(), typeReaders.size());
		
		Set<String> keys = typeReaders.keySet();
		for (String key : keys) {
			assertEquals(typeReaders.get(key).getClass().getName(), factoryTypeReaders.get(key).getClass().getName());
		}
	}
	
	public void testFactoryBean() {
		assertEquals(Map.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
	}

	protected TypeReaderRegistryFactoryBean newFactoryBean() {
		return new TypeReaderRegistryFactoryBean();
	}

	protected Map<String, TypeReader> getTypeReaders() {
		Map<String, TypeReader> typeReaders = TypeReaderRegistryFactory.getTypeReaders();
		return typeReaders;
	}

}
