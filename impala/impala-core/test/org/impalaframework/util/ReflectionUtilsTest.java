package org.impalaframework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {
	public void testInvokeMethod() {

		HashMap map = new HashMap() {
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
		void method1(Map context) {
		}

		String method2() {
			return "me";
		}
	}

}
