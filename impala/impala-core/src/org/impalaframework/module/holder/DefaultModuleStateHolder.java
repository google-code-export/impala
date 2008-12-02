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

package org.impalaframework.module.holder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.ModuleStateChangeNotifier;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionProcessor;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
//FIXME add synchronization
public class DefaultModuleStateHolder implements ModuleStateHolder {
	
	private RootModuleDefinition rootModuleDefinition;

	private TransitionProcessorRegistry transitionProcessorRegistry;
	
	private ModuleStateChangeNotifier moduleStateChangeNotifier;

	private ReentrantLock lock = new ReentrantLock();
	
	private Map<String, ConfigurableApplicationContext> moduleContexts = new HashMap<String, ConfigurableApplicationContext>();

	public DefaultModuleStateHolder() {
		super();
	}

	public void processTransitions(TransitionSet transitions) {
		
		try {
			Assert.notNull(transitionProcessorRegistry, TransitionProcessorRegistry.class.getSimpleName() + " cannot be null");

			Collection<? extends ModuleStateChange> changes = transitions.getModuleTransitions();

			for (ModuleStateChange change : changes) {
				Transition transition = change.getTransition();
				ModuleDefinition currentModuleDefinition = change.getModuleDefinition();

				TransitionProcessor transitionProcessor = transitionProcessorRegistry.getTransitionProcessor(transition);
				transitionProcessor.process(this, transitions.getNewRootModuleDefinition(), currentModuleDefinition);
			
				if (moduleStateChangeNotifier != null) {
					moduleStateChangeNotifier.notify(this, change);
				}
			}
		} finally {
			rootModuleDefinition = transitions.getNewRootModuleDefinition();
		}
	}

	public ConfigurableApplicationContext getRootModuleContext() {
		if (rootModuleDefinition == null) return null;
		return moduleContexts.get(rootModuleDefinition.getName());
	}

	public ConfigurableApplicationContext getModule(String moduleName) {
		return moduleContexts.get(moduleName);
	}

	public RootModuleDefinition getRootModuleDefinition() {
		return rootModuleDefinition;
	}

	public RootModuleDefinition cloneRootModuleDefinition() {
		RootModuleDefinition newDefinition = ModuleDefinitionUtils.cloneAndUnfreeze(rootModuleDefinition);
		return newDefinition;
	}

	public boolean hasModule(String moduleName) {
		return (rootModuleDefinition.findChildDefinition(moduleName, true) != null);
	}

	public boolean hasRootModuleDefinition() {
		return getRootModuleDefinition() != null;
	}

	public Map<String, ConfigurableApplicationContext> getModuleContexts() {
		return Collections.unmodifiableMap(moduleContexts);
	}
	
	public void putModule(String name, ConfigurableApplicationContext context) {
		moduleContexts.put(name, context);
	}

	public ConfigurableApplicationContext removeModule(String moduleName) {
		return moduleContexts.remove(moduleName);
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return getRootModuleDefinition();
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

	/* ************************* protected methods ************************* */

	protected void setParentModuleDefinition(RootModuleDefinition rootModuleDefinition) {
		this.rootModuleDefinition = rootModuleDefinition;
	}

	/* ******************** injected setters ******************** */
	
	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}

	public void setModuleStateChangeNotifier(ModuleStateChangeNotifier moduleStateChangeNotifier) {
		this.moduleStateChangeNotifier = moduleStateChangeNotifier;
	}

}
