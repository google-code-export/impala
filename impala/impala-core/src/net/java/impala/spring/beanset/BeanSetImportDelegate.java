/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.spring.beanset;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Strategy for implementing beanset: import operations
 * @author Phil Zoio
 */
@Deprecated
public class BeanSetImportDelegate {

	private static final ThreadLocal<Set<String>> namedResources = newResourceHolder();

	private static final ThreadLocal<Set<String>> importedResources = newResourceHolder();

	private static final ThreadLocal<Properties> beanSetMappings = newBeanSetMappings();

	public BeanSetImportDelegate(Properties beanSetProperties) {
		beanSetMappings.set(beanSetProperties);
	}

	public void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
		beanDefinitionReader.setDocumentReaderClass(BeanSetBeanDefinitionDocumentReader.class);
	}

	public void beforeRefresh(ConfigurableApplicationContext context) {
		namedResources.get().clear();
		importedResources.get().clear();
	}

	public void afterRefresh(ConfigurableApplicationContext context) {
		namedResources.get().clear();
		importedResources.get().clear();
	}

	static ThreadLocal<Set<String>> newResourceHolder() {
		ThreadLocal<Set<String>> resourceHolder = new ThreadLocal<Set<String>>();
		resourceHolder.set(new HashSet<String>());
		return resourceHolder;
	}

	static ThreadLocal<Properties> newBeanSetMappings() {
		ThreadLocal<Properties> propertiesHolder = new ThreadLocal<Properties>();
		propertiesHolder.set(new Properties());
		return propertiesHolder;
	}

	public static ThreadLocal<Properties> getBeanSetMappings() {
		return beanSetMappings;
	}

	public static ThreadLocal<Set<String>> getImportedResources() {
		return importedResources;
	}

	public static ThreadLocal<Set<String>> getNamedResources() {
		return namedResources;
	}

}
