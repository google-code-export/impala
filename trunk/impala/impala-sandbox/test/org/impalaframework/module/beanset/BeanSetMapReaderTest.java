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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;

public class BeanSetMapReaderTest extends TestCase {

	public final void testNull() {
		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> map = reader.readBeanSetDefinition(null);
		assertEquals(0, map.size());
	}	
	
	public final void testReadBeanSetSpec() {
		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> map = reader.readBeanSetDefinition("null: set1, set2; mock: set3, ");
		assertEquals(2, map.size());
		
		final Set<String> setNull = map.get("null");
		assertEquals(2, setNull.size());
		final Iterator<String> iteratorNull = setNull.iterator();
		
		assertEquals("set1", iteratorNull.next());
		assertEquals("set2", iteratorNull.next());
		
		final Set<String> setMock = map.get("mock");
		assertEquals(1, setMock.size());
		final Iterator<String> iteratorMock = setMock.iterator();
		
		assertEquals("set3", iteratorMock.next());
	}
	
	public final void testNothingAtEnd() {
		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> map = reader.readBeanSetDefinition("null:, set1; mock:");
		assertEquals(1, map.size());
		
		final Set<String> setNull = map.get("null");
		assertEquals(1, setNull.size());
		final Iterator<String> iteratorNull = setNull.iterator();
		assertEquals("set1", iteratorNull.next());
	}
	
	public void testMissingColonIndex() {
		try {
			new BeanSetMapReader().readBeanSetDefinition("null: set1, set2; mock set3");
			fail("Expected missing colon index failure");
		}
		catch (ConfigurationException e) {
			assertEquals(
					"Invalid beanset definition. Missing ':' from string ' mock set3' in 'null: set1, set2; mock set3'",
					e.getMessage());
		}
	}

}