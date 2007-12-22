package org.impalaframework.util;

import junit.framework.TestCase;

public class InstantiationUtilsTest extends TestCase {

	public final void testInstantiate() {
		InstantiationUtilsTest instantiate = InstantiationUtils.instantiate(InstantiationUtilsTest.class.getName());
		assertEquals(this.getClass(), instantiate.getClass());
	}
	
	public final void testWrongClass() {
		try {
			@SuppressWarnings("unused")
			String instantiate = InstantiationUtils.instantiate(InstantiationUtilsTest.class.getName());
		}
		catch (ClassCastException e) {
			assertEquals("org.impalaframework.util.InstantiationUtilsTest cannot be cast to java.lang.String", e.getMessage());
		}
	}
	
	public final void testClassNotFound() {
		try {
			@SuppressWarnings("unused")
			String instantiate = InstantiationUtils.instantiate("unknown");
		}
		catch (IllegalStateException e) {
			assertEquals("Unable to find class of type 'unknown'", e.getMessage());
		}
	}
	
	public final void testClassWithPrivateConstructor() {
		try {
			@SuppressWarnings("unused")
			String instantiate = InstantiationUtils.instantiate(ClassWithPrivateConstructor.class.getName());
		}
		catch (IllegalStateException e) {
			assertEquals("Error instantiating class of type 'org.impalaframework.util.ClassWithPrivateConstructor': Class org.impalaframework.util.InstantiationUtils can not access a member of class org.impalaframework.util.ClassWithPrivateConstructor with modifiers \"private\"", e.getMessage());
		}
	}

}

class ClassWithPrivateConstructor {

	private ClassWithPrivateConstructor() {
		super();
	}
	
}
