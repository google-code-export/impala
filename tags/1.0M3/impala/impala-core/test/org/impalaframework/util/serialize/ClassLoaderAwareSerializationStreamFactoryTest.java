package org.impalaframework.util.serialize;

import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ClassLoaderAwareSerializationStreamFactoryTest extends TestCase {

	public void testSerialize() {
		SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(ClassUtils.getDefaultClassLoader()));
		Integer clone = (Integer) helper.clone(new Integer(10));
		assertEquals(10, clone.intValue());
	}

}
