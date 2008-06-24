package org.impalaframework.module.type;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class TypeReaderRegistryFactoryBean implements FactoryBean, InitializingBean {

	private Map<String, TypeReader> typeReaders = new HashMap<String, TypeReader>();

	public Object getObject() throws Exception {
		return typeReaders;
	}

	public Class<?> getObjectType() {
		return Map.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		typeReaders.putAll(TypeReaderRegistryFactory.getTypeReaders());
	}

	protected Map<String, TypeReader> getTypeReaders() {
		return typeReaders;
	}
	
}
