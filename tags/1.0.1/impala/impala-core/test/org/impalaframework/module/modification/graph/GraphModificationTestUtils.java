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

import static org.impalaframework.classloader.graph.GraphTestUtils.printTransitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import java.util.List;

import junit.framework.Assert;

import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.Transition;

abstract class GraphModificationTestUtils {

    static Collection<? extends ModuleStateChange> assertTransitions(GraphModificationExtractorDelegate graphModificationExtractor, SimpleRootModuleDefinition root1,
            SimpleRootModuleDefinition root2, String expectedUnloads, String expectedLoads) {
        
        final Collection<? extends ModuleStateChange> transitions = graphModificationExtractor.getTransitions(null, root1, root2).getModuleTransitions();
        
        List<String> loads = new ArrayList<String>();
        List<String> unloads = new ArrayList<String>();
        for (ModuleStateChange moduleStateChange : transitions) {
            if (moduleStateChange.getTransition().equals(Transition.UNLOADED_TO_LOADED)) loads.add(moduleStateChange.getModuleDefinition().getName());
            if (moduleStateChange.getTransition().equals(Transition.LOADED_TO_UNLOADED)) unloads.add(moduleStateChange.getModuleDefinition().getName());
        }
    
        printTransitions(transitions);
        
        //now do expectations
        if (expectedUnloads != null) {
            List<String> expectedUnLoadsList = Arrays.asList(expectedUnloads.split(","));
            Assert.assertEquals("Failure comparing expected unload ordering", expectedUnLoadsList, unloads);
        } else {
            Assert.assertTrue("Unloads expected to be empty", unloads.isEmpty());
        }
        
        if (expectedLoads != null) {
            List<String> expectedLoadsList = Arrays.asList(expectedLoads.split(","));
            Assert.assertEquals("Failure comparing expected unload ordering", expectedLoadsList, loads);
        } else {
            Assert.assertTrue("Loads expected to be empty", loads.isEmpty());
        }
        
        return transitions;
    }

}
