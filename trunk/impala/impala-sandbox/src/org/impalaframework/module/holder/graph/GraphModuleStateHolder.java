package org.impalaframework.module.holder.graph;

import org.impalaframework.classloader.graph.DependencyRegistry;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

//FIXME add synchronization
public class GraphModuleStateHolder extends DefaultModuleStateHolder implements ModuleStateHolder {

	private DependencyRegistry originalDependencyRegistry;
	
	private DependencyRegistry newDependencyRegistry;
	
	private GraphClassLoaderRegistry graphClassLoaderRegistry;
	
	@Override
	public void processTransitions(TransitionSet transitions) {
		super.processTransitions(transitions);
	}

	public void setOriginalDependencyRegistry(DependencyRegistry originalDependencyRegistry) {
		this.originalDependencyRegistry = originalDependencyRegistry;
	}

	public void setNewDependencyRegistry(DependencyRegistry newDependencyRegistry) {
		this.newDependencyRegistry = newDependencyRegistry;
	}

	public void setGraphClassLoaderRegistry(GraphClassLoaderRegistry graphClassLoaderRegistry) {
		this.graphClassLoaderRegistry = graphClassLoaderRegistry;
	}

	public DependencyRegistry getOriginalDependencyRegistry() {
		return originalDependencyRegistry;
	}

	public DependencyRegistry getNewDependencyRegistry() {
		return newDependencyRegistry;
	}

	public GraphClassLoaderRegistry getGraphClassLoaderRegistry() {
		return graphClassLoaderRegistry;
	}
	
}
