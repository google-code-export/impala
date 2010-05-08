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

package @project.package.name@.@main.project.name@;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.spring.module.SpringRuntimeModule;

import @project.package.name@.@main.project.name@.BaseIntegrationTest;


public class MessageIntegrationTest extends BaseIntegrationTest {

    public static void main(String[] args) {
        InteractiveTestRunner.run(MessageIntegrationTest.class);
    }

    public void testIntegration() {
        RuntimeModule rootRuntimeModule = Impala.getRootRuntimeModule();
        assertTrue(rootRuntimeModule instanceof SpringRuntimeModule);
    }

    public RootModuleDefinition getModuleDefinition() { 
        return new TestDefinitionSource("@full.main.project.name@", "@full.module.project.name@").getModuleDefinition();
    }

}
