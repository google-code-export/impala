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

import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.util.ReflectionUtils;

import junit.framework.TestCase;

public class ModuleStateHolderFactoryTest extends TestCase {

    public void testNewModuleStateHolder() {
        SimpleModuleStateHolderFactory factory = new SimpleModuleStateHolderFactory();
        factory.setExternalRootModuleName("rootModule");
        ModuleStateHolder moduleStateHolder = factory.newModuleStateHolder();
        assertTrue(moduleStateHolder instanceof DefaultModuleStateHolder);
        
        assertEquals("rootModule",  ReflectionUtils.getFieldValue(moduleStateHolder, "externalRootModuleName", String.class));
    }
    
    public void testNewGraphModuleStateHolder() {
        SimpleModuleStateHolderFactory factory = new GraphModuleStateHolderFactory();
        factory.setExternalRootModuleName("rootModule");
        ModuleStateHolder moduleStateHolder = factory.newModuleStateHolder();
        assertTrue(moduleStateHolder instanceof GraphModuleStateHolder);
        
        assertEquals("rootModule",  ReflectionUtils.getFieldValue(moduleStateHolder, "externalRootModuleName", String.class));
    }

}
