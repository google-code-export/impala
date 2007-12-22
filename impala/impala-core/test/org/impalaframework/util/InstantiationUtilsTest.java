package org.impalaframework.util;

import junit.framework.TestCase;

public class InstantiationUtilsTest extends TestCase {

	public final void testInstantiate() {
		Object instantiate = InstantiationUtils.instantiate(InstantiationUtilsTest.class.getName());
		assertEquals(this.getClass(), instantiate.getClass());
		
		//FIXME more test
	}

}
