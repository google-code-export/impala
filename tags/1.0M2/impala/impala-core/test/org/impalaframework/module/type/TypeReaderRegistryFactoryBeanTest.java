package org.impalaframework.module.type;

import java.util.Map;
import java.util.Set;

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
