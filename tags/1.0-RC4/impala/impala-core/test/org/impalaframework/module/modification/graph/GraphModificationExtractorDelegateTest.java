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

package org.impalaframework.module.modification.graph;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.TransitionSet;

public class GraphModificationExtractorDelegateTest extends TestCase {
    
    private Application application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = TestApplicationManager.newApplicationManager().getCurrentApplication(); 
    }

    public void testGetTransitions() {
        GraphModificationExtractorDelegate delegate = new GraphModificationExtractorDelegate();
        SimpleRootModuleDefinition rootDefinitionOld = rootDefinition("orig");
        SimpleRootModuleDefinition rootDefinitionNew = rootDefinition("new");
        TransitionSet transitions = delegate.getTransitions(application, rootDefinitionOld, rootDefinitionNew);
        assertEquals(2, transitions.getModuleTransitions().size());
        
        assertNotNull(delegate.getNewDependencyManager());
        assertNotNull(delegate.getOldDependencyManager());
    }

    private SimpleRootModuleDefinition rootDefinition(String name) {
        SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(name, 
                new String[] {"context.xml"}, 
                new String[0], 
                null,
                new ModuleDefinition[0], null);
        return rootDefinition;
    }

}
