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

import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import net.java.impala.spring.beanset.DebuggingBeanSetDefinitionDocumentReader;
import net.java.impala.spring.beanset.DebuggingBeanSetImportDelegate;
import net.java.impala.spring.beanset.impl.Bean1;
import net.java.impala.spring.beanset.impl.Bean2;
import net.java.impala.spring.beanset.impl.Bean3;
import net.java.impala.spring.beanset.impl.Bean4;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Demonstrates use of smart imports with the beanset prefix
 * 
 * @author Phil Zoio
 */
public class BeanSetTest extends TestCase {

	public void test() throws Exception {

		Properties properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("beanset/beanset.properties"));

		GenericApplicationContext context = createContext(properties, "imported-context.xml");

		assertTrue(context.getBean("bean1") instanceof Bean1);
		assertTrue(context.getBean("bean2") instanceof Bean2);
		assertTrue(context.getBean("bean3") instanceof Bean3);

		properties.setProperty("bean2and3", "alternative-context.xml");
		context = createContext(properties, "alternative-context.xml");

		assertTrue(context.getBean("bean1") instanceof Bean1);
		assertTrue(context.getBean("bean2") instanceof Bean4);

		try {
			context.getBean("bean3");
			fail();
		}
		catch (NoSuchBeanDefinitionException e) {
		}

	}

	private GenericApplicationContext createContext(Properties properties, String expectedResource) {
		GenericApplicationContext context = new GenericApplicationContext();

		DebuggingBeanSetImportDelegate delegate = new DebuggingBeanSetImportDelegate(properties);

		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
		delegate.initBeanDefinitionReader(xmlReader);

		delegate.beforeRefresh(context);

		xmlReader.setDocumentReaderClass(DebuggingBeanSetDefinitionDocumentReader.class);
		xmlReader.loadBeanDefinitions(new ClassPathResource("beanset/beanset-context.xml"));

		context.refresh();

		delegate.afterRefresh(context);
		
		List<BeanSetNode> nodes = DebuggingBeanSetImportDelegate.getTopLevelNodes().get();
		
		assertEquals(1, nodes.size());
		final BeanSetNode topLevel = nodes.get(0);
		
		Set<BeanSetNode> children = topLevel.getChildren();
		assertEquals(1, children.size());
		BeanSetNode next = children.iterator().next();
		assertEquals(expectedResource, next.getResource());
		assertNotNull(topLevel.getChildNode(expectedResource));
		
		return context;
	}

}
