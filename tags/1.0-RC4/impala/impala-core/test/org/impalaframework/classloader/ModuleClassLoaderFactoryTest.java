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
import java.net.URL;

import org.impalaframework.util.URLUtils;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ModuleClassLoaderFactoryTest extends TestCase {

    private CustomClassLoaderFactory factory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factory = new CustomClassLoaderFactory();
    }
    
    public final void testNewClassLoaderClassLoaderFileArray() {
        ClassLoader newClassLoader = factory.newClassLoader(ClassUtils.getDefaultClassLoader(), new File[]{ new File(System.getProperty("java.io.tmpdir"))});
        assertTrue(newClassLoader instanceof ModuleClassLoader);
        System.out.println(newClassLoader.toString());
    }

    public final void testNewClassLoaderClassLoaderURLArray() {
        File file = new File(System.getProperty("java.io.tmpdir"));
        File[] files = new File[]{ file};
        URL[] createUrls = URLUtils.createUrls(files);
        ClassLoader newClassLoader = factory.newClassLoader(ClassUtils.getDefaultClassLoader(), createUrls);
        assertTrue(newClassLoader instanceof ModuleClassLoader);
        System.out.println(newClassLoader.toString());
    }

}
