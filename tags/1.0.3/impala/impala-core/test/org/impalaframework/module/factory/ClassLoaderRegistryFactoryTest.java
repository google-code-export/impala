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

package org.impalaframework.module.factory;

import junit.framework.TestCase;

import org.impalaframework.module.holder.ModuleClassLoaderRegistry;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;
import org.impalaframework.module.spi.ClassLoaderRegistry;

public class ClassLoaderRegistryFactoryTest extends TestCase {

    public void testNewModuleClassLoaderRegistry() {
        SimpleClassLoaderRegistryFactory factory = new SimpleClassLoaderRegistryFactory();
        ClassLoaderRegistry classLoaderRegistry = factory.newClassLoaderRegistry();
        assertTrue(classLoaderRegistry instanceof ModuleClassLoaderRegistry);
    }
    
    public void testNewGraphClassLoaderRegistry() {
        GraphClassLoaderRegistryFactory factory = new GraphClassLoaderRegistryFactory();
        ClassLoaderRegistry classLoaderRegistry = factory.newClassLoaderRegistry();
        assertTrue(classLoaderRegistry instanceof GraphClassLoaderRegistry);
    }

}
