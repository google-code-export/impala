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

import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;

public class GraphModificationExtractor implements ModificationExtractor {

	private GraphModuleStateHolder graphModuleStateHolder;
	
	public TransitionSet getTransitions(
			RootModuleDefinition originalDefinition,
			RootModuleDefinition newDefinition) {
		
		//FIXME test
		
		GraphModificationExtractorDelegate delegate = new GraphModificationExtractorDelegate();
		TransitionSet transitions = delegate.getTransitions(originalDefinition, newDefinition);
		
		//do something with the dependencyRegistry
		graphModuleStateHolder.setNewDependencyRegistry(delegate.getNewDependencyRegistry());
		graphModuleStateHolder.setOriginalDependencyRegistry(delegate.getOriginalDependencyRegistry());
		
		return transitions;
	}

	public void setGraphModuleStateHolder(GraphModuleStateHolder graphModuleStateHolder) {
		this.graphModuleStateHolder = graphModuleStateHolder;
	}
	
}
