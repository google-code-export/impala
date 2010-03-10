/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.spring.dynamic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class DynamicScope implements Scope {

	private long delay = 1000 * 10L;

	private long lastRefreshTime;

	private DynamicClassLoader classLoader;

	private Map<String, Object> beans = new ConcurrentHashMap<String, Object>();

	public synchronized Object get(String name, ObjectFactory objectFactory) {

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastRefreshTime > delay) {
			classLoader.refresh();
			beans.clear();
			lastRefreshTime = currentTime;
		}

		Object bean = beans.get(name);
		if (bean == null) {
			bean = objectFactory.getObject();
			beans.put(name, bean);
		}
		return bean;
	}

	public String getConversationId() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable callback) {
	}

	public Object remove(String name) {
		return null;
	}

	public void setClassLoader(DynamicClassLoader classLoader) {
		this.classLoader = classLoader;
	}

}
