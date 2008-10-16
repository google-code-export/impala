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

import org.impalaframework.module.TypeReader;
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
