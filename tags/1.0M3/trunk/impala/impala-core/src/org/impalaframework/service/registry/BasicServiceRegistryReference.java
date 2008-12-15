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

package org.impalaframework.service.registry;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.impalaframework.service.ServiceRegistryReference;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link ServiceRegistryReference}.
 * @author Phil Zoio
 */
public class BasicServiceRegistryReference implements ServiceRegistryReference {

	private final Object bean;
	private final String beanName;
	private final String contributingModule;
	private final List<String> tags;
	private final Map<String, ?> attributes;
	private final ClassLoader beanClassLoader;

	@SuppressWarnings("unchecked")
	public BasicServiceRegistryReference(Object bean, String beanName,
			String contributingModule, ClassLoader classLoader) {
		this(bean, beanName, contributingModule, null, Collections.EMPTY_MAP, classLoader);
	}

	@SuppressWarnings("unchecked")
	public BasicServiceRegistryReference(Object bean, String beanName,
			String contributingModule, List<String> tags, ClassLoader classLoader) {
		this(bean, beanName, contributingModule, tags, Collections.EMPTY_MAP, classLoader);
	}
	
	@SuppressWarnings("unchecked")
	public BasicServiceRegistryReference(Object bean, String beanName,
			String contributingModule, Map<String, ?> attributes, ClassLoader classLoader) {
		this(bean, beanName, contributingModule, null, attributes, classLoader);
	}

	@SuppressWarnings("unchecked")
	public BasicServiceRegistryReference(Object bean, String beanName,
			String contributingModule, List<String> tags,
			Map<String, ?> attributes, ClassLoader classLoader) {
		super();
		Assert.notNull(bean);
		Assert.notNull(beanName);
		Assert.notNull(contributingModule);
		Assert.notNull(classLoader);
		this.bean = bean;
		this.beanName = beanName;
		this.contributingModule = contributingModule;
		this.tags = (tags != null? tags : Collections.EMPTY_LIST);
		this.attributes = (attributes != null ? attributes : Collections.EMPTY_MAP);
		this.beanClassLoader = classLoader;
	}

	public final Object getBean() {
		return bean;
	}

	public String getBeanName() {
		return beanName;
	}

	public final String getContributingModule() {
		return contributingModule;
	}

	public List<String> getTags() {
		return tags;
	}

	public Map<String, ?> getAttributes() {
		return attributes;
	}

	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}

}
