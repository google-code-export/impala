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

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.graph.DependencyManager;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.springframework.context.ConfigurableApplicationContext;

// FIXME add synchronization and comments
/**
 * Extension of {@link DefaultModuleStateHolder}, which also holds data items
 * required for arranging modules as graph rather than as a hierarchy.
 */
public class GraphModuleStateHolder extends DefaultModuleStateHolder {

	private static final Log logger = LogFactory.getLog(UnloadTransitionProcessor.class);

	private DependencyManager oldDependencyManager;

	private DependencyManager newDependencyManager;

	private GraphClassLoaderRegistry classLoaderRegistry;
	
	private ReentrantLock lock = new ReentrantLock();

	@Override
	public void processTransitions(TransitionSet transitions) {
		super.processTransitions(transitions);
	}

	/**
	 * Calls the superclass's {@link #removeModule(String)} method after
	 * removing the named module's {@link ClassLoader} from the
	 * {@link GraphClassLoaderRegistry}
	 */
	@Override
	public ConfigurableApplicationContext removeModule(String moduleName) {
		logger.info("Removing class loader from registry for module: " + moduleName);
		classLoaderRegistry.removeClassLoader(moduleName);
		return super.removeModule(moduleName);
	}

	public void setOldDependencyManager(DependencyManager oldDependencyManager) {
		this.oldDependencyManager = oldDependencyManager;
	}

	public void setNewDependencyManager(DependencyManager newDependencyManager) {
		this.newDependencyManager = newDependencyManager;
	}

	public DependencyManager getOldDependencyManager() {
		return oldDependencyManager;
	}

	public DependencyManager getNewDependencyManager() {
		return newDependencyManager;
	}

	public void setClassLoaderRegistry(GraphClassLoaderRegistry classLoaderRegistry) {
		this.classLoaderRegistry = classLoaderRegistry;
	}
	
	public void lock() {
		this.lock.lock();
	}
	
	public void unlock() {
		this.lock.unlock();
	}

	public boolean hasLock() {
		return this.lock.isHeldByCurrentThread();
	}
}
