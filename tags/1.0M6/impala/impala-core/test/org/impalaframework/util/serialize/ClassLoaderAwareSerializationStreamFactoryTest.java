package org.impalaframework.util.serialize;

import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ClassLoaderAwareSerializationStreamFactoryTest extends TestCase {

    public void testSerialize() {
        SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(ClassUtils.getDefaultClassLoader()));
        Integer clone = (Integer) helper.clone(new Integer(10));
        assertEquals(10, clone.intValue());
    }
    
    public void testArray() {
        SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(ClassUtils.getDefaultClassLoader()));
        String[] clone = (String[]) helper.clone(new String[]{"one", "two"});
        assertEquals(2, clone.length);
    }

}
