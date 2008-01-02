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

package org.impalaframework.spring.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.ClassUtils;

/**
 * <code>BeanPostProcessor</code> which attempts to register the created bean
 * with the parent's bean factory's <code>ContributionProxyFactoryBean</code>
 * 
 * @author Phil Zoio
 */
public class AutoRegisteringModuleContributionExporter extends BaseModuleContributionExporter implements
		BeanClassLoaderAware {

	final Logger logger = LoggerFactory.getLogger(AutoRegisteringModuleContributionExporter.class);

	private ClassLoader beanClassLoader;

	private Map<String, List<Class>> contributionClassMap;

	private Map<String, String> contributions;

	public void afterPropertiesSet() throws Exception {
		//FIXME check contributions or contributionClassMap is set
		
		Set<String> beanNames = null;
		if (contributionClassMap != null) {
			beanNames = contributionClassMap.keySet();
		} else {
			beanNames = contributions.keySet();
		}
		processContributions(beanNames);
	}

	protected ContributionEndpoint getContributionEndPoint(String beanName, Object bean) {
		ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(getBeanFactory(), beanName);
		
		if (endPoint == null) {
			
			List<Class> contributionClasses = null;
			
			if (contributionClassMap != null) {
				contributionClasses = contributionClassMap.get(beanName);
			} else {
				String value = contributions.get(beanName);
				List<Class> interfaceClasses = getContributionClasses(bean, value);
				contributionClasses = interfaceClasses;
			}
		
			//FIXME verify that bean implements all of the contribution classes
			
			//FIXME create BeanDefinition entry, and register it with bean factory
			
			//call getBean on new entry to get endPoint
			
		}
		
		return endPoint;
	}

	private List<Class> getContributionClasses(Object bean, String typeList) {
		String[] interfaces = typeList.split(",");

		List<Class> interfaceClasses = new ArrayList<Class>();
		for (String interfaceClass : interfaces) {
			Class resolvedClassName = ClassUtils.resolveClassName(interfaceClass.trim(), beanClassLoader);
			
			if (resolvedClassName.isAssignableFrom(bean.getClass())) {

				// FIXME
				throw new RuntimeException("FIXME");
			}
			
			interfaceClasses.add(resolvedClassName);
		}
		return interfaceClasses;
	}

	public void setContributionClassMap(Map<String, List<Class>> contributionClassMap) {
		this.contributionClassMap = contributionClassMap;
	}

	public void setContributions(Map<String, String> contributions) {
		this.contributions = contributions;
	}

	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

}
