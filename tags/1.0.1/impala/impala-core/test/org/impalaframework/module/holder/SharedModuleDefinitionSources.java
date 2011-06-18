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

package org.impalaframework.module.holder;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;

public class SharedModuleDefinitionSources {

    public static final String plugin1 = "sample-module1";

    public static final String plugin2 = "sample-module2";

    public static final String plugin3 = "sample-module3";
    
    public static final String rootProjectName = "impala";

    public static ModuleDefinitionSource newTest1() {
        return new Test1();
    }

    public static ModuleDefinitionSource newTest1a() {
        return new Test1a();
    }

    public static ModuleDefinitionSource newTest2() {
        return new Test2();
    }

    public static class Test1 implements ModuleDefinitionSource {
        ModuleDefinitionSource definitionSource = new SimpleModuleDefinitionSource(rootProjectName, new String[] { "parentTestContext.xml" }, new String[] { plugin1, plugin2 });

        public Test1() {
        }

        public RootModuleDefinition getModuleDefinition() {
            return definitionSource.getModuleDefinition();
        }
    }

    public static class Test1a implements ModuleDefinitionSource {
        ModuleDefinitionSource definitionSource = new SimpleModuleDefinitionSource(rootProjectName, new String[] { "parentTestContext.xml",
                        "extra-context.xml" }, new String[] { plugin1, plugin2 });

        public Test1a() {
        }

        public RootModuleDefinition getModuleDefinition() {
            return definitionSource.getModuleDefinition();
        }
    }

    static class Test2 implements ModuleDefinitionSource {
        ModuleDefinitionSource definitionSource = new SimpleModuleDefinitionSource(rootProjectName, new String[] { "parentTestContext.xml" }, new String[] { plugin1, plugin2 });

        public Test2() {

            ModuleDefinition p2 = definitionSource.getModuleDefinition().getChildModuleDefinition(plugin2);
            new SimpleModuleDefinition(p2, plugin3);
        }

        public RootModuleDefinition getModuleDefinition() {
            return definitionSource.getModuleDefinition();
        }
    }

}
