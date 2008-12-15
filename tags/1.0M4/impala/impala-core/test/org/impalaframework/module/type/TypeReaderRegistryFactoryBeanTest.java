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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleTypes;

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
		
		TypeReaderRegistry registry = (TypeReaderRegistry) factoryBean.getObject();
		Map<String, TypeReader> factoryTypeReaders = registry.getTypeReaders();
		
		Map<String, TypeReader> typeReaders = getTypeReaders();
		assertEquals(factoryTypeReaders.size(), typeReaders.size());
		
		Set<String> keys = typeReaders.keySet();
		for (String key : keys) {
			assertEquals(typeReaders.get(key).getClass().getName(), factoryTypeReaders.get(key).getClass().getName());
		}
	}
	
	public void testExtraContributions() throws Exception {
		
		final TypeReader dummyTypeReader = EasyMock.createMock(TypeReader.class);
		Map<String,TypeReader> extraContributions = new HashMap<String, TypeReader>();
		extraContributions.put(ModuleTypes.APPLICATION, dummyTypeReader);
		extraContributions.put("another", dummyTypeReader);
		
		factoryBean.setExtraContributions(extraContributions);
		factoryBean.afterPropertiesSet();
		TypeReaderRegistry registry = (TypeReaderRegistry) factoryBean.getObject();
		
		assertSame(dummyTypeReader, registry.getTypeReader(ModuleTypes.APPLICATION));
		assertSame(dummyTypeReader, registry.getTypeReader("another"));
		assertFalse(registry.getTypeReader(ModuleTypes.ROOT) == dummyTypeReader);	
	}
	
	public void testFactoryBean() {
		assertEquals(TypeReaderRegistry.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
	}
	
	public void testNoService() {
		final TypeReaderRegistry registry = factoryBean.getTypeReaders();
		try {
			registry.getTypeReader("duff");
		} catch (NoServiceException e) {
			assertEquals("No instance of org.impalaframework.module.TypeReader available for type named 'duff'. Available types: []", e.getMessage());
		}
	}

	protected TypeReaderRegistryFactoryBean newFactoryBean() {
		return new TypeReaderRegistryFactoryBean();
	}

	protected Map<String, TypeReader> getTypeReaders() {
		Map<String, TypeReader> typeReaders = TypeReaderRegistryFactory.getTypeReaders();
		return typeReaders;
	}

}
