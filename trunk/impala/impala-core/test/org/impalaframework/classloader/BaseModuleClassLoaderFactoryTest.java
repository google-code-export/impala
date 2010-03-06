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

import junit.framework.TestCase;

public class BaseModuleClassLoaderFactoryTest extends TestCase {
    
    ClassLoader cl1 = new ModuleClassLoader(new File[0]);
    ClassLoader cl2 = new ModuleClassLoader(new URL[0]);
    private BaseModuleClassLoaderFactory factory;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factory = factory();
    }

    public void testFiless() throws Exception {
        assertSame(cl1, factory.newClassLoader(null, new File[0]));
    }
    
    public void testURLs() throws Exception {
        assertSame(cl2, factory.newClassLoader(null, new URL[0]));
    }

    private BaseModuleClassLoaderFactory factory() {
        BaseModuleClassLoaderFactory factory = new BaseModuleClassLoaderFactory() {

            @Override
            public ClassLoader newClassLoader(ClassLoader parent, File[] files) {
                return cl1;
            }

            @Override
            public ClassLoader newClassLoader(ClassLoader parent, URL[] urls) {
                return cl2;
            }   
        };
        return factory;
    }

}
