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

package org.impalaframework.spring.service.proxy;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;
import org.impalaframework.spring.service.proxy.ContributionProxyFactoryBean;
import org.springframework.util.ClassUtils;

/**
 * Unit org.impalaframework.testrun for <code>ContributionProxyFactoryBean</code>
 * @author Phil Zoio
 */
public class ContributionProxyFactoryBeanTest extends TestCase {

	private ContributionProxyFactoryBean bean;
	private ServiceRegistryImpl serviceRegistry;
	private ClassLoader classLoader;
	private DynamicServiceProxyFactoryCreator proxyFactoryCreator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bean = new ContributionProxyFactoryBean();
		serviceRegistry = new ServiceRegistryImpl();
		
		proxyFactoryCreator = new DynamicServiceProxyFactoryCreator();
		bean.setServiceRegistry(serviceRegistry);
		proxyFactoryCreator.setServiceRegistry(serviceRegistry);
		
		classLoader = ClassUtils.getDefaultClassLoader();
	}
	
	public void testWithBeanName() throws Exception {
		bean.setProxyInterfaces(new Class[] { Child.class });
		bean.setBeanName("someBean");
		bean.afterPropertiesSet();

		Child child = (Child) bean.getObject();

		try {
			child.childMethod();
			fail();
		}
		catch (NoServiceException e) {
		}

		Child newChild = newChild();
		bean.registerTarget("pluginName", newChild);
		serviceRegistry.addService("someBean", "pluginName", newChild, classLoader);
		child.childMethod();
	}	
	
	public void testWithExportName() throws Exception {
		bean.setProxyInterfaces(new Class[] { Child.class });
		bean.setBeanName("someBean");
		bean.setExportedBeanName("exportBean");
		bean.afterPropertiesSet();

		Child child = (Child) bean.getObject();

		try {
			child.childMethod();
			fail();
		}
		catch (NoServiceException e) {
		}

		Child newChild = newChild();
		bean.registerTarget("pluginName", newChild);
		serviceRegistry.addService("exportBean", "pluginName", newChild, classLoader);
		child.childMethod();
	}
	
	public void testAllowNoService() throws Exception {
		bean.setProxyInterfaces(new Class[] { Child.class });
		bean.setBeanName("someBean");
		proxyFactoryCreator.setAllowNoService(true);
		bean.setProxyFactoryCreator(proxyFactoryCreator);
		
		bean.afterPropertiesSet();

		Child child = (Child) bean.getObject();
		child.childMethod();

		bean.registerTarget("pluginName", newChild());

		child.childMethod();
	}

	private Child newChild() {
		return new Child() {
			public void childMethod() {
			}

			public Parent tryGetParent() {
				return null;
			}
		};
	}

}
