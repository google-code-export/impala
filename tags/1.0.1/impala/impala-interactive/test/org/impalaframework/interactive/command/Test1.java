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

package org.impalaframework.interactive.command;

import junit.framework.TestCase;

import org.impalaframework.facade.Impala;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;

public class Test1 extends TestCase implements ModuleDefinitionSource {
    
    public static final String plugin1 = "sample-module1";

    ModuleDefinitionSource source = new SimpleModuleDefinitionSource("impala-core", new String[] { "parentTestContext.xml" }, new String[] { plugin1 });

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Impala.init(this);
    }
    
    public void testMyMethod() throws Exception {
        System.out.println("Running test method with " + Impala.getRootRuntimeModule());
    }
    
    public RootModuleDefinition getModuleDefinition() {
        return source.getModuleDefinition();
    }
}
