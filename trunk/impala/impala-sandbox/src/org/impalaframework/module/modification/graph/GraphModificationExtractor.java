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
