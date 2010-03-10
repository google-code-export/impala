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

package org.impalaframework.classloader;

import java.io.File;
import java.util.List;

import org.springframework.util.ClassUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ClassLoaderUtilsTest extends TestCase {

    public void testGetSuperclassNames() {
        List<String> superclassNames = ClassLoaderUtils.getClassHierarchyNames(this.getClass().getName());
        System.out.println(superclassNames);
        
        assertEquals(4, superclassNames.size());
        assertEquals(this.getClass().getName(), superclassNames.get(0));
        assertEquals(TestCase.class.getName(), superclassNames.get(1));
        assertEquals(Assert.class.getName(), superclassNames.get(2));
        assertEquals(Object.class.getName(), superclassNames.get(3));
    }
    
    public void testIsVisibleFrom() throws Exception {
        assertFalse(ClassLoaderUtils.isVisibleFrom(classLoader(""), classLoader("")));
        assertFalse(ClassLoaderUtils.isVisibleFrom(classLoader("location1"), classLoader("location2")));
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        assertFalse(ClassLoaderUtils.isVisibleFrom(classLoader(defaultClassLoader, ""), classLoader("")));
        
        //this one returns true - finally
        assertTrue(ClassLoaderUtils.isVisibleFrom(defaultClassLoader, classLoader(defaultClassLoader, "")));
        assertTrue(ClassLoaderUtils.isVisibleFrom(defaultClassLoader, defaultClassLoader));
        ClassLoader classLoader = classLoader("");
        assertTrue(ClassLoaderUtils.isVisibleFrom(classLoader, classLoader));

        //it doesn't work in reverse
        assertFalse(ClassLoaderUtils.isVisibleFrom(classLoader(defaultClassLoader, ""), defaultClassLoader));
    }

    private ClassLoader classLoader(String name) {
        return new ModuleClassLoader(new File[]{new File("./" + name)});
    }
    
    private ClassLoader classLoader(ClassLoader parent, String name) {
        return new ModuleClassLoader(parent, new File[]{new File("./" + name)});
    }

}
