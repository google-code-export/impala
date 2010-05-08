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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * - Overrides resolveBeanClass for dynamic scope
 */
public class DynamicBeanFactory extends DefaultListableBeanFactory {

	public DynamicBeanFactory(BeanFactory internalParentBeanFactory) {
		super(internalParentBeanFactory);
	}

	public DynamicBeanFactory() {
		super();
	}

	@Override
	protected Class<?> resolveBeanClass(RootBeanDefinition mbd, String beanName) throws CannotLoadBeanClassException {
		if (!"dynamic".equals(mbd.getScope())) {
			return super.resolveBeanClass(mbd, beanName);
		}
		else {
			try {
				return mbd.resolveBeanClass(getBeanClassLoader());
			}
			catch (ClassNotFoundException ex) {
				throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(),
						ex);
			}
			catch (LinkageError err) {
				throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(),
						err);
			}
		}
	}
}
