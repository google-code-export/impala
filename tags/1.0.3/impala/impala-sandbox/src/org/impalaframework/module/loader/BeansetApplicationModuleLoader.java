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

package org.impalaframework.module.loader;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.beanset.BeanSetPropertiesReader;
import org.impalaframework.module.beanset.RecordingImportingBeanDefinitionDocumentReader;
import org.impalaframework.module.definition.BeansetModuleDefinition;
import org.impalaframework.spring.module.loader.ApplicationModuleLoader;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public class BeansetApplicationModuleLoader extends ApplicationModuleLoader {

	public BeansetApplicationModuleLoader() {
		super();
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(String applicationId, ConfigurableApplicationContext context, ModuleDefinition definition) {
		if (definition instanceof BeansetModuleDefinition) {
			BeansetModuleDefinition beanSetDefinition = (BeansetModuleDefinition) definition;
			Map<String, Set<String>> overrides = beanSetDefinition.getOverrides();

			ClassLoader classLoader = context.getClassLoader();

			Properties properties = new BeanSetPropertiesReader().readBeanSetDefinition(classLoader, overrides);			
			
			final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
			final RecordingImportingBeanDefinitionDocumentReader documentReader = new RecordingImportingBeanDefinitionDocumentReader(properties);

			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ModuleUtils.castToBeanDefinitionRegistry(beanFactory)){
				protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
					return documentReader;
				}
			};
			return xmlReader;
		}
		else {
			return super.newBeanDefinitionReader(applicationId, context, definition);
		}
	}
	
}
