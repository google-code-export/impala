package org.impalaframework.classloader;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ClassLoaderUtilsTest extends TestCase {

	public final void testGetSuperclassNames() {
		List<String> superclassNames = ClassLoaderUtils.getClassHierarchyNames(this.getClass().getName());
		System.out.println(superclassNames);
		
		assertEquals(4, superclassNames.size());
		assertEquals(this.getClass().getName(), superclassNames.get(0));
		assertEquals(TestCase.class.getName(), superclassNames.get(1));
		assertEquals(Assert.class.getName(), superclassNames.get(2));
		assertEquals(Object.class.getName(), superclassNames.get(3));
	}

}
