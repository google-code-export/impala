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

import org.impalaframework.classloader.graph.DependencyManager;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

//FIXME add synchronization and comments
/**
 * Extension of {@link DefaultModuleStateHolder}, which also holds data items required for arranging
 * modules as graph rather than as a hierarchy.
 */
public class GraphModuleStateHolder extends DefaultModuleStateHolder implements ModuleStateHolder {

	private DependencyManager oldDependencyManager;
	
	private DependencyManager newDependencyManager;
	
	private GraphClassLoaderRegistry graphClassLoaderRegistry;
	
	@Override
	public void processTransitions(TransitionSet transitions) {
		super.processTransitions(transitions);
	}

	public void setOldDependencyManager(DependencyManager oldDependencyManager) {
		this.oldDependencyManager = oldDependencyManager;
	}

	public void setNewDependencyManager(DependencyManager newDependencyManager) {
		this.newDependencyManager = newDependencyManager;
	}

	public void setGraphClassLoaderRegistry(GraphClassLoaderRegistry graphClassLoaderRegistry) {
		this.graphClassLoaderRegistry = graphClassLoaderRegistry;
	}

	public DependencyManager getOldDependencyManager() {
		return oldDependencyManager;
	}

	public DependencyManager getNewDependencyManager() {
		return newDependencyManager;
	}

	public GraphClassLoaderRegistry getGraphClassLoaderRegistry() {
		return graphClassLoaderRegistry;
	}
	
}
