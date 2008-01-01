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

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.spring.module.ContributionProxyFactoryBean;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;

/**
 * Unit org.impalaframework.testrun for <code>ContributionProxyFactoryBean</code>
 * @author Phil Zoio
 */
public class ContributionProxyFactoryBeanTest extends TestCase {

	public void test() throws Exception {
		ContributionProxyFactoryBean bean = new ContributionProxyFactoryBean();
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

		bean.registerTarget("pluginName", newChild());
		child.childMethod();
	}
	
	public void testAllowNoService() throws Exception {
		ContributionProxyFactoryBean bean = new ContributionProxyFactoryBean();
		bean.setProxyInterfaces(new Class[] { Child.class });
		bean.setBeanName("someBean");
		bean.setAllowNoService(true);
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
