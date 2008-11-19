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

package org.impalaframework.module.holder.graph;

import org.impalaframework.classloader.graph.DependencyRegistry;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

//FIXME add synchronization and comments
/**
 * Extension of {@link DefaultModuleStateHolder}, which also holds data items required for arranging
 * modules as graph rather than as a hierarchy.
 */
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
