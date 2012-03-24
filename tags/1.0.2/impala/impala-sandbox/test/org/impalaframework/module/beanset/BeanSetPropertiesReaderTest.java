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

import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.util.ClassUtils;

public class BeanSetPropertiesReaderTest extends TestCase {

	public void testReadModuleDefinition() {
		Properties definition = new BeanSetPropertiesReader()
				.readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null: set1, set2; mock: set3, duff");
		assertEquals("applicationContext-set1-null.xml", definition.getProperty("set1"));
		assertEquals("applicationContext-set2-null.xml", definition.getProperty("set2"));
		assertEquals("applicationContext-set3-mock.xml", definition.getProperty("set3"));
	}

	public void testReadAll() {
		Properties definition = new BeanSetPropertiesReader().readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null: all_beans");
		assertEquals("applicationContext-set1-null.xml", definition.getProperty("set1"));
		assertEquals("applicationContext-set2-null.xml", definition.getProperty("set2"));
		assertEquals("applicationContext-set3-null.xml", definition.getProperty("set3"));
	}

	public void testMissingColonIndex() {
		try {
			new BeanSetPropertiesReader().readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null: set1, set2; mock set3");
			fail("Expected missing colon index failure");
		}
		catch (ConfigurationException e) {
			assertEquals(
					"Invalid beanset definition. Missing ':' from string ' mock set3' in 'null: set1, set2; mock set3'",
					e.getMessage());
		}
	}

	public void testNothingAtEnd() {
		Properties definition = new BeanSetPropertiesReader().readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null:, set1; mock:");
		assertEquals("applicationContext-set1-null.xml", definition.getProperty("set1"));
	}

}
