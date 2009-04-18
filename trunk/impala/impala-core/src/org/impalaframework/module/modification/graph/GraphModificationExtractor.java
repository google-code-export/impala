/*
 * Copyright 2007-2008 the original author or authors.
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

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.TransitionSet;

public class GraphModificationExtractor implements ModificationExtractor {

    private GraphModuleStateHolder moduleStateHolder;
    
    public final TransitionSet getTransitions(
            RootModuleDefinition originalDefinition,
            RootModuleDefinition newDefinition) {
        
        GraphAwareModificationExtractor delegate = newDelegate();
        TransitionSet transitions = delegate.getTransitions(originalDefinition, newDefinition);
        
        //method marked as final means this will be called
        moduleStateHolder.setDependencyManager(delegate.getNewDependencyManager());     
        return transitions;
    }

    public void setModuleStateHolder(GraphModuleStateHolder graphModuleStateHolder) {
        this.moduleStateHolder = graphModuleStateHolder;
    }

    protected GraphAwareModificationExtractor newDelegate() {
        GraphModificationExtractorDelegate delegate = new GraphModificationExtractorDelegate();
        return delegate;
    }
    
}
