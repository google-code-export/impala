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

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.TransitionSet;
import org.impalaframework.util.ObjectUtils;

public class GraphModificationExtractor implements ModificationExtractor {
    
    public final TransitionSet getTransitions(
            Application application,
            RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition) {
        
        GraphAwareModificationExtractor delegate = newDelegate();
        TransitionSet transitions = delegate.getTransitions(application, originalDefinition, newDefinition);
        
        GraphModuleStateHolder moduleStateHolder = ObjectUtils.cast(application.getModuleStateHolder(), GraphModuleStateHolder.class);
        
        //method marked as final means this will be called
        moduleStateHolder.setDependencyManager(delegate.getNewDependencyManager());     
        return transitions;
    }
    
    protected GraphAwareModificationExtractor newDelegate() {
        GraphModificationExtractorDelegate delegate = new GraphModificationExtractorDelegate();
        return delegate;
    }
    
}
