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

package org.impalaframework.module.beanset;

import java.util.Arrays;

import junit.framework.TestCase;

import org.impalaframework.module.definition.BeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.loader.BeansetApplicationModuleLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

public class BeanSetApplicationModuleLoaderTest extends TestCase {

	private static final String plugin4 = "sample-module4";

	private ConfigurableApplicationContext parent;

	private ConfigurableApplicationContext child;

	public final void testInitialModuleDefinition() {
		BeansetModuleDefinition definition = new SimpleBeansetModuleDefinition(plugin4);
		loadChild(definition);
		System.out.println(Arrays.toString(child.getBeanDefinitionNames()));
		assertTrue(child.containsBean("bean1"));
		assertTrue(child.containsBean("importedBean1"));
		assertTrue(child.containsBean("importedBean2"));
	}
	
	public final void testModifiedModuleDefinition() {
		BeansetModuleDefinition definition = new SimpleBeansetModuleDefinition(plugin4, "alternative: myImports");
		loadChild(definition);
		System.out.println(Arrays.toString(child.getBeanDefinitionNames()));
		assertTrue(child.containsBean("bean1"));
		assertTrue(child.containsBean("importedBean1"));
		assertFalse(child.containsBean("importedBean2"));
	}
	
	public final void testNewBeanDefinitionReader() {
		BeansetModuleDefinition definition = new SimpleBeansetModuleDefinition(plugin4);
		BeansetApplicationModuleLoader loader = new BeansetApplicationModuleLoader();
	
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader("id", new GenericApplicationContext(), definition);
		int definitions = reader.loadBeanDefinitions(new ClassPathResource("parentTestContext.xml"));
		assertTrue(definitions > 0);
	}

	private void loadChild(BeansetModuleDefinition definition) {
		parent = new ClassPathXmlApplicationContext("parentTestContext.xml");
		BeansetApplicationModuleLoader moduleLoader = new BeansetApplicationModuleLoader();
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		child = moduleLoader.newApplicationContext(null, parent, definition, classLoader);
		XmlBeanDefinitionReader xmlReader = moduleLoader.newBeanDefinitionReader("id", child, definition);
		xmlReader.setBeanClassLoader(classLoader);
		xmlReader.loadBeanDefinitions(moduleLoader.getSpringConfigResources("id", definition, classLoader));
		child.refresh();
	}

	public void tearDown() {
		try {
			child.close();
			parent.close();
		} catch (Exception e) {			
		}
	}
}
