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

package org.impalaframework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {
	public void testInvokeMethod() {

		HashMap<?, ?> map = new HashMap<Object, Object>() {
			private static final long serialVersionUID = 1L;
			public String toString() {
				return "a map";
			}
		};

		try {

			ReflectionUtils.invokeMethod(new TestExample(), "METHOD", map);
			fail();
		}
		catch (UnsupportedOperationException e) {
			assertEquals("No method compatible with method: METHOD, args: [a map]", e.getMessage());
		}
		
		ReflectionUtils.invokeMethod(new TestExample(), "method1", map);
		assertEquals("me", ReflectionUtils.invokeMethod(new TestExample(), "method2"));
	}

	class TestExample {
		void method1(Map<?, ?> context) {
		}

		String method2() {
			return "me";
		}
	}

}
