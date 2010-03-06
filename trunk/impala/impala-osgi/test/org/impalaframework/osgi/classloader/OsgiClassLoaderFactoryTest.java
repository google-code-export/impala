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

package org.impalaframework.osgi.classloader;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.module.ModuleDefinition;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.BundleDelegatingClassLoader;
import org.springframework.util.ClassUtils;

public class OsgiClassLoaderFactoryTest extends TestCase {

    private OsgiClassLoaderFactory factory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factory = new OsgiClassLoaderFactory() {

            @Override
            Bundle findBundle(ModuleDefinition moduleDefinition) {
                return EasyMock.createMock(Bundle.class);
            }
            
        };
    }
    
    public void testNewClassLoader() throws Exception {
        final ClassLoader classLoader = factory.newClassLoader(null, ClassUtils.getDefaultClassLoader(), EasyMock.createMock(ModuleDefinition.class));
        assertTrue(classLoader instanceof BundleDelegatingClassLoader);
    }

}
